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
package com.orboan.chatapp.j.client.presentation.views.panes;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;
import com.orboan.chatapp.j.client.libraries.RiverLayout;
import com.orboan.chatapp.j.client.presentation.controllers.PrivatePanelController;
import com.orboan.chatapp.j.shared.model.Message;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

/**
 *
 * @author Oriol Boix Anfosso <dev@orboan.com>
 */
public class PrivatePanel extends javax.swing.JPanel {

    private static List<PrivatePanel> cache = null;

    private PrivatePanel(String sender, String recipient) {
        this.sender = sender;
        this.recipient = recipient;
        sc = StyleContext.getDefaultStyleContext();
        initComponents();        
        doc = this.chatTextPane.getStyledDocument();
        registerListeners(this.sender, this.recipient);
    }

    public static PrivatePanel getInstance(String sender, String recipient) {
        if (cache == null) {
            cache = new ArrayList<>();
            cache.add(new PrivatePanel(sender, recipient));
            return cache.get(cache.size() - 1);
        } else {
            Iterator<PrivatePanel> it = cache.iterator();
            boolean found = false;
            PrivatePanel panel = null;
            while (it.hasNext() && !found) {
                panel = it.next();
                if (sender.equals(panel.getSender())
                        && recipient.equals(panel.getRecipient())) {
                    found = true;
                }
            }
            if (!found) {
                panel = new PrivatePanel(sender, recipient);
                cache.add(panel);
            }
            return panel;
        }
    }

    private final String sender;
    private final String recipient;
    private final StyleContext sc;
    private final StyledDocument doc;
    private final int MAX_MESSAGE_LENGTH = 500;
    private PrivatePanelController controller;

    private JScrollPane jScrollPane1, jScrollPane2;
    private JTextPane chatTextPane;
    private JTextArea messageTextArea;
    private JLabel header, senderLabel, recipientLabel;

    public String getSender() {
        return sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public PrivatePanelController getController() {
        return controller;
    }

    public void setController(PrivatePanelController controller) {
        if (this.controller == null) {
            this.controller = controller;
        }
    }

    private void initComponents() {
        senderLabel = new JLabel(sender, SwingConstants.LEFT);
        header = new JLabel("Chat[ing]App with", SwingConstants.CENTER);
        recipientLabel = new JLabel(recipient, SwingConstants.RIGHT);
        Font newLabelFont = new Font(header.getFont().getName(), Font.BOLD, header.getFont().getSize());
        header.setFont(newLabelFont);
        newLabelFont = new Font(senderLabel.getFont().getName(), Font.BOLD, senderLabel.getFont().getSize());
        senderLabel.setFont(newLabelFont);
        newLabelFont = new Font(recipientLabel.getFont().getName(), Font.BOLD, recipientLabel.getFont().getSize());
        recipientLabel.setFont(newLabelFont);
        JPanel p = new JPanel(new BorderLayout());
        p.add(senderLabel, BorderLayout.WEST);
        p.add(header, BorderLayout.CENTER);
        p.add(recipientLabel, BorderLayout.EAST);
        chatTextPane = new JTextPane();
        chatTextPane.setEditable(false);
        chatTextPane.setEditable(false);
        chatTextPane.setBackground(new java.awt.Color(240, 250, 255));
        chatTextPane.setFont(new java.awt.Font("Dialog", 0, 24)); // NOI18N
        chatTextPane.setPreferredSize(new Dimension(840, 500));
        jScrollPane1 = new JScrollPane();
        jScrollPane1.setViewportView(chatTextPane);
        messageTextArea = new JTextArea(6, 30);
        messageTextArea.setLineWrap(true);
        messageTextArea.setWrapStyleWord(true);
        messageTextArea.setBackground(new Color(250, 252, 255));
        messageTextArea.requestFocusInWindow();
        jScrollPane2 = new JScrollPane(messageTextArea);
        final RiverLayout lm = new RiverLayout();
        this.setLayout(lm);
        this.setBorder(BorderFactory.createBevelBorder(EtchedBorder.RAISED));
        this.setPreferredSize(new Dimension(880, 800));
        this.setBorder(BorderFactory.createTitledBorder("Private ChatApp ["
                + sender + " >> " + recipient + "]"));
        this.add("p hfill", p);
        this.add("p hfill vfill", this.jScrollPane1);
        this.add("br center", this.jScrollPane2);
    }

    public JTextArea getMessageTextArea() {
        return messageTextArea;
    }

    private void registerListeners(String sender, String recipient) {
        this.messageTextArea.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (controller != null) {
                        controller.sendMessage(validateMessage(
                        messageTextArea.getText()));
                    }
                }
            }

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    messageTextArea.setText("");
                }
            }
        });
    }

    private String validateMessage(String message) {
        if (message.length() > MAX_MESSAGE_LENGTH) {
            this.appendToPane("[Warning: max length is " + MAX_MESSAGE_LENGTH + " chars]" + Message.EOL, Color.red, true);
            return message.substring(0, MAX_MESSAGE_LENGTH - 1) + "...";
        }
        return message;
    }

    public void appendToPane(String msg, Color c, boolean isLocal) {
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);
        int alignment = StyleConstants.ALIGN_LEFT;
        if (!isLocal) {
            alignment = StyleConstants.ALIGN_RIGHT;
        }
        SimpleAttributeSet att = new SimpleAttributeSet();
        StyleConstants.setAlignment(att, alignment);

        try {
            doc.insertString(0, msg, aset);
            doc.setParagraphAttributes(0, msg.length(), att, false);
        } catch (BadLocationException ex) {
            Logger.getLogger(RoomPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
