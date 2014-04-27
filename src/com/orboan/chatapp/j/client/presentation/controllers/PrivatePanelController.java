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

import com.orboan.chatapp.j.client.integration.sockets.ChatAppClient;
import com.orboan.chatapp.j.client.presentation.views.panes.PrivatePanel;
import com.orboan.chatapp.j.shared.model.Message;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Oriol Boix Anfosso <dev@orboan.com>
 */
public class PrivatePanelController {

    public PrivatePanelController(PrivatePanel panel) {
        client = ChatAppClient.getInstance();
        this.privatePanel = panel;
        thread = new Printer();
        thread.start();
    }

    private final ChatAppClient client;
    private final PrivatePanel privatePanel;
    private final Thread thread;
    private boolean threadSuspended = false;

    public ChatAppClient getClient() {
        return client;
    }

    public Thread getThread() {
        return thread;
    }

    public boolean isThreadSuspended() {
        return threadSuspended;
    }

    public void setThreadSuspended(boolean threadSuspended) {
        this.threadSuspended = threadSuspended;
    }
    
    public void sendMessage(String strMsg){
        Message m
                = new Message(Message.Type.MESSAGE,
                        privatePanel.getSender(),
                        strMsg,
                        privatePanel.getRecipient());
        client.send(m);
        ArrayList<Message> messages = new ArrayList<>();
        messages.add(m);
        printMessages(messages);
    }

    public void sendMessage() {
        sendMessage(privatePanel.getMessageTextArea().getText());
    }

    private void printMessages(ArrayList<Message> messages) {
        Iterator<Message> it = messages.iterator();
        Color c;
        while (it.hasNext()) {
            Message m = it.next();
            StringBuilder sb = new StringBuilder();
            if (m.getSender().equals(privatePanel.getSender())) {
                c = Color.BLUE;
                sb.append("> ");
                sb.append(m.getContent());
                privatePanel.appendToPane(sb.toString(), c, true);
                it.remove();

            } else if (m.getRecipient().equals(privatePanel.getSender())) {
                c = Color.MAGENTA;                
                sb.append(m.getContent().replace(Message.EOL, ""));
                sb.append(" <").append(Message.EOL);
                privatePanel.appendToPane(sb.toString(), c, false);
                it.remove();
            }
        }
    }

    class Printer extends Thread {

        public synchronized void suspendThread(boolean suspend) {
            threadSuspended = suspend;
            if (!threadSuspended) {
                notify();
            }
        }

        @Override
        public void run() {
            while (true) {
                try {
                    synchronized (this) {
                        while (threadSuspended) {
                            wait();
                        }
                    }
                } catch (InterruptedException ex) {
                    Logger.getLogger(PrivatePanelController.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (client != null && client.getMessages() != null && !client.getMessages().isEmpty()) {
                    ConcurrentHashMap<Message.Type, ArrayList<Message>> msgs = client.getMessages();
                    ArrayList<Message> list;
                    if ((list = msgs.get(Message.Type.MESSAGE)) != null) {
                        printMessages(list);
                        if (list.isEmpty()) {
                            msgs.remove(Message.Type.MESSAGE);
                        }
                    }
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    Logger.getLogger(RoomPanelController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

}
