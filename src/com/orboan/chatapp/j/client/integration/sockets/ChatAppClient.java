/* 
 * BSD 3-clause license
 * Copyright (c) 2014, Oriol Boix Anfosso (dev@orboan.com)
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * 
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 * 
 * * Neither the name of the {organization} nor the names of its
 *   contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.orboan.chatapp.j.client.integration.sockets;

import com.orboan.chatapp.j.client.integration.daos.SettingsDao;
import com.orboan.chatapp.j.client.presentation.controllers.RoomPanelController;
import com.orboan.chatapp.j.shared.model.Message;
import com.orboan.chatapp.j.client.libraries.Messages;
import static com.orboan.chatapp.j.client.model.ServerSettings.DEFAULT_PORT;
import static com.orboan.chatapp.j.client.model.ServerSettings.DEFAULT_SERVER;
import com.orboan.chatapp.j.utils.SocketUtils;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Oriol Boix Anfosso <dev@orboan.com>
 */
public class ChatAppClient implements IClient {

    private final long CHANNEL_READ_SLEEP = 100L;
    private final long LOGIN_TIMEOUT = 500L; //500 ms
    private final int MAX_CONNECT_RETRIES = 40;

    private byte[] messageBytes;
    private int serverPort;
    private String server;
    private final ConcurrentHashMap<Message.Type, ArrayList<Message>> messages;
    private ByteBuffer writeBuf, readBuf;
    private SocketChannel clientChannel;
    private Boolean authenticated;
    private String username;
    private Thread t;

    private static ChatAppClient obj = null;

    private ChatAppClient() {
        this.reloadSettings();
        this.messages = new ConcurrentHashMap<>(64);
    }

    public static ChatAppClient getInstance() {
        if (obj == null) {
            obj = new ChatAppClient();
        }
        return obj;
    }

    public ConcurrentHashMap getMessages() {
        return messages;
    }

    public SocketChannel getClientChannel() {
        return clientChannel;
    }

    @Override
    public final void reloadSettings() {
        SettingsDao settingsDao = SettingsDao.getInstance();
        if ((this.server = settingsDao.getServerAddr()) == null) {
            this.server = DEFAULT_SERVER;
        }
        if (settingsDao.getPort() == -1) {
            this.serverPort = DEFAULT_PORT;
        } else {
            this.serverPort = settingsDao.getPort();
        }
    }

    @Override
    public void open() throws IOException {
        this.clientChannel = SocketChannel.open();
        clientChannel.configureBlocking(false);
        System.out.println("Server: " + server + " - Port: " + serverPort);
        //Initiate connection to server and repeteadly poll until complete
        if (!clientChannel.connect(new InetSocketAddress(server, serverPort))) {
            int i = 0;
            while (!clientChannel.finishConnect() && i < MAX_CONNECT_RETRIES) {
                System.out.print(".");
                try {
                    Thread.sleep(50);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ChatAppClient.class.getName()).log(Level.SEVERE, null, ex);
                }
                i++;
            }
            System.out.println("");
            if (i == MAX_CONNECT_RETRIES){
                throw new IOException();
            }
        }
        clientChannel.configureBlocking(true);
        //Repeatedly poll for incoming messages (in a new thread)
        receiveAll();
    }

    @Override
    public boolean isConnected() {
        if (this.clientChannel != null) {
            return clientChannel.isConnected();
        } else {
            return false;
        }
    }

    @Override
    public void close() throws IOException {
        if (this.clientChannel != null
                && this.clientChannel.isConnected()) {
            Message message = Messages.getBYE();
            if (authenticated != null && authenticated) {
                message.setSender(this.username);
            }
            this.send(message);

            Message.Type type = message.getType();
            ArrayList<Message> list;
            if ((list = this.messages.get(type)) == null) {
                list = new ArrayList<>();
            }
            list.add(message);
            this.messages.put(type, list);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(ChatAppClient.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (t != null && t.isAlive()) {
                t.interrupt();
            }
            this.clientChannel.close();
            System.out.println("Channel closed.");
        }
    }

    @Override
    public boolean login(String username, char[] password) {
        authenticated = null;
        Message loginMessage = Messages.getCLIENT_LOGIN_REQUEST();
        loginMessage.setSender(username);
        loginMessage.setPassword(password); //Needs to redo for cipher
        this.send(loginMessage);
        loginMessage.clearPassword();
        int i = 0;
        while (i < 10 && authenticated == null) {
            try {
                Thread.sleep(LOGIN_TIMEOUT);
            } catch (InterruptedException ex) {
                Logger.getLogger(ChatAppClient.class.getName()).log(Level.SEVERE, null, ex);
            }
            i++;
        }
        if (this.authenticated == null) {
            return false;
        } else {
            this.username = RoomPanelController.getInstance().getUsername();
            return this.authenticated;
        }
    }

    @Override
    public boolean logout(String username) {
        Message logoutMessage = Messages.getCLIENT_LOGOUT_REQUEST();
        logoutMessage.setSender(username);
        this.send(logoutMessage);
        int i = 0;
        while (i < 10 && authenticated) {
            try {
                Thread.sleep(LOGIN_TIMEOUT);
            } catch (InterruptedException ex) {
                Logger.getLogger(ChatAppClient.class.getName()).log(Level.SEVERE, null, ex);
            }
            i++;
        }
        if (authenticated) {
            return false;
        } else {
            this.username = null;
            authenticated = null;
            return true;
        }
    }

    @Override
    public void send(Message message) {
        if (this.clientChannel != null
                && this.clientChannel.isConnected()) {
            this.messageBytes = this.bytesToSend(message);
            writeBuf = ByteBuffer.wrap(messageBytes);
            while (true) {
                if (writeBuf.hasRemaining()) {
                    try {
                        this.clientChannel.write(writeBuf);
                    } catch (IOException ex) {
                        Logger.getLogger(ChatAppClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    break;
                }
            }
        } else {
            try {
                throw new Exception("Error: Channel not connected!");
            } catch (Exception e) {
                Logger.getLogger(ChatAppClient.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }

    //Prepare a Message object as a byte array to be sent
    private byte[] bytesToSend(Message message) {
        byte[] msg = SocketUtils.objectToBytes(message);
        int len = msg.length;
        byte[] lenBytes
                = {(byte) (len >> 24), (byte) (len >> 16), (byte) (len >> 8), (byte) (len)};
        return SocketUtils.concat(lenBytes, msg);
    }

    //Starts a new thread to poll repeteadly for incoming messages
    private void receiveAll() {
        Receive receive = new Receive(this.clientChannel);
        this.t = new Thread(receive);
        t.start();
    }

    /**
     * This class encapsulates the polling for the reception of new incoming
     * messages.
     */
    final class Receive implements Runnable {

        private final SocketChannel clientChannel;

        public Receive(SocketChannel clientChannel) {
            this.clientChannel = clientChannel;
        }

        @Override
        public void run() {
            Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
            if (this.clientChannel != null
                    && this.clientChannel.isConnected()) {
                Object o;
                int bytesRcvd = -1, totalBytesRcvd;
                while (this.clientChannel.isConnected()) {
//                    System.out.print("|");
                    readBuf = ByteBuffer.allocate(4);
                    totalBytesRcvd = 0;
                    while (totalBytesRcvd < 4) {
//                        System.out.print("^");
                        try {
                            if ((bytesRcvd = clientChannel.read(readBuf)) == -1) {
                                throw new SocketException("Connection closed prematurely. Maybe server is down?");
                            } else if (bytesRcvd == 0) {
                                try {
                                    Thread.sleep(CHANNEL_READ_SLEEP);
                                } catch (InterruptedException ex) {
                                    Logger.getLogger(ChatAppClient.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                            totalBytesRcvd += bytesRcvd;
                        } catch (IOException ex) {
                            try {
                                bytesRcvd = -1;
                                this.clientChannel.close();
                            } catch (IOException ex1) {
                                Logger.getLogger(ChatAppClient.class.getName()).log(Level.SEVERE, null, ex1);
                            }
//                            Logger.getLogger(ChatAppClient.class.getName()).log(Level.SEVERE, "IOException");
                            break;
                        }
                    }
                    if (bytesRcvd != -1) {
                        readBuf.flip();
                        int len = readBuf.getInt();
                        readBuf = ByteBuffer.allocate(len);
                        totalBytesRcvd = 0;
                        while (totalBytesRcvd < len) {
                            try {
                                if ((bytesRcvd = clientChannel.read(readBuf)) == -1) {
                                    throw new SocketException("Connection closed prematurely");
                                } else if (bytesRcvd == 0) {
                                    try {
                                        Thread.sleep(CHANNEL_READ_SLEEP);
                                    } catch (InterruptedException ex) {
                                        Logger.getLogger(ChatAppClient.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                                totalBytesRcvd += bytesRcvd;
                            } catch (IOException ex) {
                                try {
                                    clientChannel.close();
                                    totalBytesRcvd = len;
                                } catch (IOException ex1) {
                                    Logger.getLogger(ChatAppClient.class.getName()).log(Level.SEVERE, null, ex1);
                                }
                                Logger.getLogger(ChatAppClient.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        if (bytesRcvd != -1) {
                            readBuf.flip();
                            byte[] array;
                            array = new byte[readBuf.remaining()];
                            readBuf.get(array);
                            readBuf.clear();

                            if ((o = SocketUtils.bytesToObject(array)) != null) {
                                Message message = (Message) o;
                                if (message.getType().equals(Message.Type.NO_MORE_MESSAGES)) {
                                    try {
                                        Thread.sleep(CHANNEL_READ_SLEEP);
                                    } catch (InterruptedException ex) {
                                        Logger.getLogger(ChatAppClient.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                } else if (message.getType().equals(Message.Type.LOGIN)) {
                                    authenticated = message.getContent().equals("LOGIN SUCCESSFUL" + Message.EOL);
                                } else if (message.getType().equals(Message.Type.LOGOUT)) {
                                    if (message.getContent().equals("LOGOUT SUCCESSFUL" + Message.EOL)) {
                                        authenticated = false;
                                    }
                                }
                                if (message.getRecipient().equals(username)
                                        || message.getSender().equals(Message.Sender.CHATAPP.toString())
                                        || message.getSender().equals(Message.Sender.SERVER.toString())
                                        || message.getSender().equals(Message.Sender.CLIENT.toString())
                                        || message.getType().equals(Message.Type.WELCOME)
                                        || message.getRecipient().equals(Message.Recipient.ALL.toString())
                                        || message.getType().equals(Message.Type.LOG)) {
                                    addMessage(message);
                                }                                                                
                            }
                        } else {
                            System.out.println("CLIENT: Waiting...");
                        }
                    }
                    try {
                        Thread.sleep(CHANNEL_READ_SLEEP);
                    } catch (InterruptedException ex) {
//                        Logger.getLogger(Receive.class.getName()).log(Level.SEVERE, "Sleep interrupted.");
                    }
                }
            } else {
                addMessage(new Message(Message.Type.ERROR,
                        Message.Sender.CHATAPP.toString(), "No channel connection established",
                        Message.Recipient.CLIENT.toString()));
            }
        }

        private void addMessage(Message message) {
            ArrayList<Message> list;
            if ((list = messages.get(message.getType())) == null) {
                list = new ArrayList<>();
            }
            list.add(message);
            messages.put(message.getType(), list);
        }
    }
}
