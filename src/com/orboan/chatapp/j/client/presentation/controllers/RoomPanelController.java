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
package com.orboan.chatapp.j.client.presentation.controllers;

import com.orboan.chatapp.j.client.presentation.views.PrivateView;
import com.orboan.chatapp.j.client.presentation.views.panes.LogPanel;
import com.orboan.chatapp.j.client.presentation.views.panes.PrivatePanel;
import com.orboan.chatapp.j.client.presentation.views.panes.RoomPanel;
import com.orboan.chatapp.j.client.integration.sockets.ChatAppClient;
import com.orboan.chatapp.j.client.presentation.controllers.PrivatePanelController.Printer;
import com.orboan.chatapp.j.shared.model.Message;
import com.orboan.chatapp.j.client.libraries.Messages;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

/**
 *
 * @author Oriol Boix Anfosso <dev@orboan.com>
 */
public class RoomPanelController implements ActionListener {

    private static RoomPanelController obj = null;

    private RoomPanelController() {
        this.loggedIn = false;
        dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        MessageScreener printer = new MessageScreener();
        Thread t = new Thread(printer);
        t.start();
    }

    public static RoomPanelController getInstance() {
        if (obj == null) {
            obj = new RoomPanelController();
        }
        return obj;
    }

    private final String NOBOBY_CONNECTED = "no users";
    private final int MAX_USERNAME_LENGTH = 12;
    private final int MAX_PASSWORD_LENGTH = 24;
    private final int MAX_MESSAGE_LENGTH = 500;
    private final DateFormat dateFormat;
    private ChatAppClient client;
    private RoomPanel roomPanel;
    private boolean loggedIn = false;
    private String username;
    private char[] password;
    private Set<PrivateView> privateChannels;
    private final String noConnectedLabelText = "Please Connect to ChatApp";
    private final String noLoggedLabelText = "Please Login and start Chatting";
    private final String loggedInLabeText = "Welcome to ChatApp, ";

    public String getUsername() {
        return username;
    }

    public ChatAppClient getClient() {
        return client;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        roomPanel = RoomPanel.getInstance();
        final Object component = e.getSource();

        if (component == roomPanel.getConnectButton()) {
            client = ChatAppClient.getInstance();
            try {
                client.open();
                roomPanel.getLoginButton().setEnabled(true);
                roomPanel.getLoginButton().setFocusable(true);
                roomPanel.getUsernameTextField().setEditable(true);
                roomPanel.getUsernameTextField().setFocusable(true);
                roomPanel.getPasswordField().setEditable(true);
                roomPanel.getPasswordField().setFocusable(true);
                roomPanel.getUsernameTextField().setEnabled(true);
                roomPanel.getPasswordField().setEnabled(true);
                roomPanel.getConnectButton().setEnabled(false);
                roomPanel.getDisconnectButton().setEnabled(true);
                roomPanel.getjLabel2().setText(noLoggedLabelText);

            } catch (IOException ex) {
                Logger.getLogger(RoomPanelController.class.getName()).log(Level.SEVERE, "Error: Is server running?");
                StringBuilder sb = new StringBuilder();
                LogPanel logPanel = LogPanel.getInstance();
                sb.append(dateFormat.format(new Date()));
                sb.append(" : [ChatApp] ");
                String str = "Connection Error: Is server running?" + Message.EOL;
                sb.append(str);
                logPanel.getLogTextArea().append(sb.toString());
                roomPanel.appendToPane(str, Color.red);
            }

        } else if (component == roomPanel.getDisconnectButton()) {
            if (username != null && loggedIn) {
                if (this.privateChannels != null) {
                    for (PrivateView frame : this.privateChannels) {
                        String recipient = frame.getRecipient();
                        client.send(new Message(Message.Type.CLOSE_PRIVATE,
                                frame.getSender(),
                                frame.getSender() + " is requesting to close the channel with "
                                + recipient,
                                recipient));

                        frame.setVisible(false);
                    }
                    this.privateChannels = null;
                }
                roomPanel.getMessageTextArea().setEditable(false);
                roomPanel.getSendMessageButton().setEnabled(false);
                roomPanel.getOpenPrivateButton().setEnabled(false);
                client.logout(username);
                this.loggedIn = false;
                this.username = null;
            } else {
                roomPanel.getLoginButton().setEnabled(false);
                roomPanel.getUsernameTextField().setEditable(false);
                roomPanel.getPasswordField().setEditable(false);
                roomPanel.getUsernameTextField().setEnabled(false);
                roomPanel.getPasswordField().setEnabled(false);
            }
            try {
                client.close();
                roomPanel.getConnectButton().setEnabled(true);
                roomPanel.getDisconnectButton().setEnabled(false);
                roomPanel.getModel().clear();
                roomPanel.getModel().addElement(NOBOBY_CONNECTED);
                roomPanel.getUsersList().setModel(roomPanel.getModel());
                roomPanel.getUsersList().setEnabled(loggedIn);
                setFocusableAllComponentsIn(roomPanel.getRootPane(), loggedIn);
                roomPanel.getConnectButton().setFocusable(!loggedIn);
                roomPanel.getConnectButton().requestFocusInWindow();
                this.clearPassword();
                roomPanel.getjLabel2().setText(noConnectedLabelText);
                client = null;
            } catch (IOException ex) {
                Logger.getLogger(RoomPanelController.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else if (component == roomPanel.getLoginButton()) {

            if ((this.username = roomPanel.getUsernameTextField().getText()) != null
                    && (this.password = roomPanel.getPasswordField().getPassword()) != null
                    && this.username.length() > 0 && this.password.length > 0) {
                if (client != null && client.isConnected()) {
                    validateCredentials(this.username, this.password);
                    this.loggedIn = client.login(this.username.trim(), this.password);
                    this.clearPassword();
                }
                if (loggedIn) {
                    roomPanel.getLoginButton().setEnabled(false);
                    roomPanel.getUsernameTextField().setEditable(false);
                    roomPanel.getPasswordField().setEditable(false);
                    roomPanel.getPasswordField().setText("");
                    roomPanel.getUsernameTextField().setEnabled(false);
                    roomPanel.getPasswordField().setEnabled(false);
                    roomPanel.getMessageTextArea().setEditable(true);
                    roomPanel.getSendMessageButton().setEnabled(true);
                    roomPanel.getMessageTextArea().setFocusable(true);
                    roomPanel.getMessageTextArea().requestFocusInWindow();
                    roomPanel.getUsersList().setEnabled(true);
                    roomPanel.getjLabel2().setText(loggedInLabeText + this.username);
                    this.privateChannels = Collections.synchronizedSet(new HashSet<PrivateView>());
                } else {
                    Message m = Messages.getLOGIN_FAILED();
                    StringBuilder sb = new StringBuilder();
                    sb.append("[ ");
                    sb.append(m.getContent().replace(Message.EOL, " ]" + Message.EOL));
                    roomPanel.appendToPane(sb.toString(), Color.RED);
                }
            } else {
                Message m = new Message("Username and password must not be empty.");
                StringBuilder sb = new StringBuilder();
                sb.append("[ ");
                sb.append(m.getContent()).append(" ]").append(Message.EOL);
                roomPanel.appendToPane(sb.toString(), Color.RED);
            }

        } else if (component == roomPanel.getSendMessageButton()) {

            if (this.loggedIn && client.isConnected()
                    && !(roomPanel.getMessageTextArea().getText().trim().isEmpty())) {

                client.send(new Message(
                        Message.Type.MESSAGE_ALL,
                        this.username,
                        validateMessage(roomPanel.getMessageTextArea().getText()),
                        Message.Recipient.ALL.toString()));
            }
            roomPanel.getMessageTextArea().setText("");
            roomPanel.getMessageTextArea().requestFocusInWindow();

        } else if (component == roomPanel.getOpenPrivateButton()) {

            String sender = roomPanel.getUsernameTextField().getText();
            String recipient = (String) roomPanel.getUsersList().getSelectedValue();
            if (!recipient.equals(NOBOBY_CONNECTED)
                    && !recipient.equals(sender)) {
                openPrivate(sender, recipient);
                client.send(new Message(Message.Type.OPEN_PRIVATE,
                        this.username,
                        this.username + " is requesting a private channel with " + recipient,
                        recipient));
                roomPanel.getOpenPrivateButton().setEnabled(false);
            }
        }
    }

    private boolean validateCredentials(String username, char[] password) {
        boolean shortened = false;
        if (username.length() > MAX_USERNAME_LENGTH) {
            this.username = username.substring(0, MAX_USERNAME_LENGTH - 1);
            roomPanel.getUsernameTextField().setText(this.username);
            shortened = true;
        }
        if (password.length > MAX_PASSWORD_LENGTH) {
            this.password = new char[MAX_PASSWORD_LENGTH];
            for (int i = 0; i < MAX_PASSWORD_LENGTH; i++) {
                this.password[i] = password[i];
            }
            shortened = true;
        }
        return !shortened;
    }

    private String validateMessage(String message) {
        if (message.length() > MAX_MESSAGE_LENGTH) {
            roomPanel.appendToPane("[Warning: max length is " + MAX_MESSAGE_LENGTH + " chars]" + Message.EOL, Color.red);
            return message.substring(0, MAX_MESSAGE_LENGTH - 1) + "...";
        }
        return message;
    }

    private void setFocusableAllComponentsIn(Container parent, boolean focusable) {
        for (Component c : parent.getComponents()) {
            c.setFocusable(focusable);

            if (c instanceof Container) {
                setFocusableAllComponentsIn((Container) c, focusable);
            }
        }
    }

    private void clearPassword() {
        if (password != null) {
            for (int i = 0; i < password.length; i++) {
                password[i] = ' ';
            }
        }
        if (roomPanel != null) {
            roomPanel.getPasswordField().setText("");
        }
    }

    private void openPrivate(String sender, String recipient) {
        PrivateView frame = new PrivateView();
        frame.setSender(sender);
        frame.setRecipient(recipient);
        if (privateChannels != null) {
            if (privateChannels.contains(frame)
                    || privateChannels.contains(frame.reversed())) {
                boolean found = false;
                Iterator<PrivateView> iterator = privateChannels.iterator();
                while (iterator.hasNext() && !found) {
                    PrivateView pv = iterator.next();
                    if (pv.equals(frame) || pv.equals(frame.reversed())) {
                        pv.setVisible(loggedIn);
                        found = true;
                    }
                }
            } else {
                privateChannels.add(frame.reversed());
                frame.setTitle("Private ChatApp [" + sender + " >> " + recipient + "]");
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                PrivatePanel panel = PrivatePanel.getInstance(sender, recipient);
                panel.setController(new PrivatePanelController(panel));
                frame.add(panel);
                frame.pack();
                frame.setVisible(loggedIn);
            }
        }
    }

    private class MessageScreener implements Runnable {

        private void printMessages(ArrayList<Message> messages) {
            Iterator<Message> it = messages.iterator();
            Color c;
            while (it.hasNext()) {
                Message m = it.next();
                StringBuilder sb = new StringBuilder();
                sb.append(m.getSender().toUpperCase());
                sb.append(" > ");
                sb.append(m.getContent());
                if (m.getSender().equals(roomPanel.getUsernameTextField().getText().trim())) {
                    c = Color.BLUE;
                } else {
                    c = Color.BLACK;
                }
                roomPanel.appendToPane(sb.toString(), c);
                it.remove();
            }
        }

        private void screenChatAppMessages(ArrayList<Message> messages, boolean remove) {
            Iterator<Message> it = messages.iterator();
            while (it.hasNext()) {
                Message m = it.next();
                StringBuilder sb = new StringBuilder();
                sb.append("[ ");
                sb.append(m.getContent().replace(Message.EOL, " ]" + Message.EOL));
                roomPanel.appendToPane(sb.toString(), Color.GREEN);
                if (m.getConnectedUsers() != null) {
                    roomPanel.getModel().clear();

                    for (String user : m.getConnectedUsers()) {
                        roomPanel.getModel().addElement(user.trim());
                    }

                    if (roomPanel.getModel().isEmpty()) {
                        roomPanel.getModel().addElement(NOBOBY_CONNECTED);
                    }
                    roomPanel.getnUsersLabel().setText(
                            Integer.toString(m.getConnectedUsers().size()));
                }
                if (remove) {
                    it.remove();
                }
            }
        }

        private void printLogMessages(ArrayList<Message> messages, boolean remove) {
            Iterator<Message> it = messages.iterator();
            while (it.hasNext()) {
                Message m = it.next();
                StringBuilder sb = new StringBuilder();
                LogPanel logPanel = LogPanel.getInstance();
                sb.append(dateFormat.format(new Date()));
                sb.append(" : [");
                sb.append(m.getSender());
                sb.append("] ");
                sb.append(m.getContent());
                logPanel.getLogTextArea().append(sb.toString());
                if (remove) {
                    it.remove();
                }
            }
        }

        private void openPrivates(ArrayList<Message> messages, boolean remove) {
            Iterator<Message> it = messages.iterator();
            while (it.hasNext()) {
                Message m = it.next();
                String sender = m.getSender();
                String recipient = m.getRecipient();
                openPrivate(recipient, sender);
                resumePrivatePanelPrinterThread(sender, recipient);
                resumePrivatePanelPrinterThread(recipient, sender);
                if (remove) {
                    it.remove();
                }
            }
        }

        private void closePrivates(ArrayList<Message> messages, boolean remove) {
            Iterator<Message> it = messages.iterator();
            if (privateChannels != null) {
                while (it.hasNext()) {
                    Message m = it.next();
                    for (PrivateView frame : privateChannels) {
                        if ((frame.getRecipient().equals(m.getSender())
                                && frame.getSender().equals(m.getRecipient()))
                                || (frame.getRecipient().equals(m.getRecipient())
                                && frame.getSender().equals(m.getSender()))) {
                            frame.setVisible(false);
                            suspendPrivatePanelPrinterThread(frame.getRecipient(), frame.getSender());
                            suspendPrivatePanelPrinterThread(frame.getSender(), frame.getRecipient());
                        }
                    }
                    if (remove) {
                        it.remove();
                    }
                }
            }
        }

        private void resumePrivatePanelPrinterThread(String sender, String recipient) {
            PrivatePanel panel = PrivatePanel.getInstance(sender, recipient);
            PrivatePanelController controller
                    = panel.getController();
            if (controller != null) {
                Thread t = controller.getThread();
                ((Printer) t).suspendThread(false);
            }
        }

        private void suspendPrivatePanelPrinterThread(String sender, String recipient) {
            PrivatePanel panel = PrivatePanel.getInstance(sender, recipient);
            PrivatePanelController controller
                    = panel.getController();
            if (controller != null) {
                Thread t = controller.getThread();
                ((Printer) t).suspendThread(true);
            }
        }

        @Override
        public void run() {
            Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
            while (true) {
                if (client != null && client.getMessages() != null && !client.getMessages().isEmpty()) {
                    ConcurrentHashMap<Message.Type, ArrayList<Message>> msgs = client.getMessages();
                    ArrayList<Message> list;
                    if ((list = msgs.get(Message.Type.MESSAGE_ALL)) != null) {
                        printMessages(list);
                        msgs.remove(Message.Type.MESSAGE_ALL);
                    }
                    if ((list = msgs.get(Message.Type.LOGIN_FROM)) != null) {
                        screenChatAppMessages(list, false);
                        printLogMessages(list, true);
                        msgs.remove(Message.Type.LOGIN_FROM);
                    }
                    if ((list = msgs.get(Message.Type.WELCOME)) != null) {
                        screenChatAppMessages(list, false);
                        printLogMessages(list, true);
                        msgs.remove(Message.Type.WELCOME);
                    }
                    if ((list = msgs.get(Message.Type.OPEN_PRIVATE)) != null) {
                        openPrivates(list, false);
                        printLogMessages(list, true);
                        msgs.remove(Message.Type.OPEN_PRIVATE);
                    }
                    if ((list = msgs.get(Message.Type.CLOSE_PRIVATE)) != null) {
                        closePrivates(list, false);
                        printLogMessages(list, true);
                        msgs.remove(Message.Type.CLOSE_PRIVATE);
                    }
                    Iterator it = msgs.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry<Message.Type, ArrayList<Message>> pairs = (Map.Entry) it.next();
                        if (pairs.getKey().equals(Message.Type.LOG) 
                                || pairs.getKey().equals(Message.Type.CONNECTION)) {
                            printLogMessages(pairs.getValue(), true);
                            it.remove(); // avoids a ConcurrentModificationException
                        }
                    }
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(RoomPanelController.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }
    }
}
