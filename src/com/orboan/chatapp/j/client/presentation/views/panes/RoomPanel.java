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

import com.orboan.chatapp.j.client.libraries.RiverLayout;
import com.orboan.chatapp.j.client.presentation.controllers.RoomPanelController;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
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
public class RoomPanel extends javax.swing.JPanel {

    private static RoomPanel obj = null;

    /**
     * Creates new form RoomPane
     */
    private RoomPanel() {
        model = new DefaultListModel();
        model.addElement(NOBOBY_CONNECTED);

        initComponents();
        sc = StyleContext.getDefaultStyleContext();
        doc = this.mainRoomTextPane.getStyledDocument();
        usersList.setSelectedIndex(0);
        messageTextArea.setLineWrap(true);
        messageTextArea.setWrapStyleWord(true);
        registerListeners();
    }

    public static RoomPanel getInstance() {
        if (obj == null) {
            obj = new RoomPanel();
        }
        return obj;
    }

    private final DefaultListModel model;
    private final String NOBOBY_CONNECTED = "no users";
    private final StyleContext sc;
    private final StyledDocument doc;

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        mainRoomTextPane = new javax.swing.JTextPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        usersList = new javax.swing.JList();
        jScrollPane3 = new javax.swing.JScrollPane();
        messageTextArea = new javax.swing.JTextArea();
        sendMessageButton = new javax.swing.JButton();
        connectButton = new javax.swing.JButton();
        disconnectButton = new javax.swing.JButton();
        usernameLabel = new javax.swing.JLabel();
        passwLabel = new javax.swing.JLabel();
        usernameTextField = new javax.swing.JTextField();
        passwordField = new javax.swing.JPasswordField();
        loginButton = new javax.swing.JButton();
        openPrivateButton = new javax.swing.JButton();
        nUsersLabel = new javax.swing.JLabel();
        usersOnLineLabel = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        roomBoxPanel = new javax.swing.JPanel();
        sideBarPanel = new javax.swing.JPanel();
        headerPanel = new javax.swing.JPanel();
        connectPanel = new javax.swing.JPanel();
        bodyPanel = new javax.swing.JPanel();
        footerPanel = new javax.swing.JPanel();

        setFont(new java.awt.Font("Dialog", 0, 20)); // NOI18N

        mainRoomTextPane.setEditable(false);
        mainRoomTextPane.setBackground(new java.awt.Color(236, 252, 248));
        mainRoomTextPane.setFont(new java.awt.Font("Dialog", 0, 20)); // NOI18N
        mainRoomTextPane.setAutoscrolls(true);
        mainRoomTextPane.setDoubleBuffered(true);
        jScrollPane1.setViewportView(mainRoomTextPane);

        usersList.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        usersList.setModel(model);
        usersList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        usersList.setEnabled(false);
        usersList.setFixedCellHeight(30);
        jScrollPane2.setViewportView(usersList);

        messageTextArea.setEditable(false);
        messageTextArea.setColumns(20);
        messageTextArea.setFont(new java.awt.Font("Dialog", 0, 24)); // NOI18N
        messageTextArea.setRows(4);
        jScrollPane3.setViewportView(messageTextArea);

        sendMessageButton.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        sendMessageButton.setText("Send");
        sendMessageButton.setEnabled(false);

        connectButton.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        connectButton.setLabel("Connect");

        disconnectButton.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        disconnectButton.setEnabled(false);
        disconnectButton.setLabel("Disconnect");

        usernameLabel.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        usernameLabel.setText("Username");

        passwLabel.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        passwLabel.setText("Password");

        usernameTextField.setEditable(false);
        usernameTextField.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        usernameTextField.setEnabled(false);

        passwordField.setEditable(false);
        passwordField.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        passwordField.setEnabled(false);

        loginButton.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        loginButton.setEnabled(false);
        loginButton.setLabel("Login");

        openPrivateButton.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        openPrivateButton.setText("Open Private");
        openPrivateButton.setEnabled(false);

        nUsersLabel.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        nUsersLabel.setText("#");

        usersOnLineLabel.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        usersOnLineLabel.setText("users online");

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 36)); // NOI18N
        jLabel1.setText("ChatApp");

        jLabel2.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        jLabel2.setText("Please Connect to ChatApp");

        BorderLayout blo = new BorderLayout();
        roomBoxPanel.setLayout(blo);
        roomBoxPanel.setPreferredSize(new Dimension(650, 750));
        RiverLayout rlo = new RiverLayout();
        sideBarPanel.setLayout(rlo);
        sideBarPanel.setPreferredSize(new Dimension(200, 650));

        JPanel auxPane;
        rlo = new RiverLayout();

        this.setLayout(rlo);
        headerPanel.setLayout(new BorderLayout());
        auxPane = new JPanel();
        auxPane.setLayout(new RiverLayout());
        auxPane.add("p center", jLabel1);
        auxPane.add("p center", jLabel2);
        headerPanel.add(auxPane, BorderLayout.CENTER);
        rlo = new RiverLayout();
        connectPanel.setLayout(rlo);
        connectPanel.add("p hfill", connectButton);
        connectPanel.add("p hfill", disconnectButton);
        headerPanel.add(connectPanel, BorderLayout.EAST);
        this.add("p hfill", headerPanel);

        bodyPanel.setLayout(new RiverLayout());
        JPanel auxPane2 = new JPanel();
        auxPane2.setLayout(new RiverLayout());
        auxPane2.add("right", loginButton);
        auxPane2.add("p", usernameLabel);
        auxPane2.add("hfill", usernameTextField);
        auxPane2.add("p", passwLabel);
        auxPane2.add("hfill",passwordField);        
        roomBoxPanel.add(auxPane2, BorderLayout.NORTH);             
        roomBoxPanel.add(jScrollPane1, BorderLayout.CENTER);
        sideBarPanel.add("p center", nUsersLabel);
        sideBarPanel.add(usersOnLineLabel);
        sideBarPanel.add("p right hfill vfill", jScrollPane2);
        sideBarPanel.add("p right", openPrivateButton);
        bodyPanel.add("hfill vfill", roomBoxPanel);
        bodyPanel.add("vfill", sideBarPanel);
        this.add("p hfill vfill", bodyPanel);

        footerPanel.setLayout(new BorderLayout());
        footerPanel.add(jScrollPane3, BorderLayout.CENTER);
        footerPanel.add(sendMessageButton, BorderLayout.EAST);
        this.add("p hfill", footerPanel);

        usernameLabel.getAccessibleContext().setAccessibleName("usernameLabel");
        passwLabel.getAccessibleContext().setAccessibleName("passwLabel");
        openPrivateButton.getAccessibleContext().setAccessibleName("openPrivateButton");
    }// </editor-fold>                            

    public JButton getConnectButton() {
        return connectButton;
    }

    public JButton getDisconnectButton() {
        return disconnectButton;
    }

    public JButton getLoginButton() {
        return loginButton;
    }

    public JButton getSendMessageButton() {
        return sendMessageButton;
    }

    public JButton getOpenPrivateButton() {
        return openPrivateButton;
    }

    public JLabel getnUsersLabel() {
        return nUsersLabel;
    }

    public JTextPane getMainRoomTextPane() {
        return mainRoomTextPane;
    }

    public JLabel getjLabel2() {
        return jLabel2;
    }

    // Variables declaration - do not modify                     
    private javax.swing.JButton connectButton;
    private javax.swing.JButton disconnectButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JButton loginButton;
    private javax.swing.JTextPane mainRoomTextPane;
    private javax.swing.JTextArea messageTextArea;
    private javax.swing.JLabel nUsersLabel;
    private javax.swing.JButton openPrivateButton;
    private javax.swing.JLabel passwLabel;
    private javax.swing.JPasswordField passwordField;
    private javax.swing.JPanel roomBoxPanel, sideBarPanel,
            bodyPanel, headerPanel, connectPanel, footerPanel;
    private javax.swing.JButton sendMessageButton;
    private javax.swing.JLabel usernameLabel;
    private javax.swing.JTextField usernameTextField;
    private javax.swing.JList usersList;
    private javax.swing.JLabel usersOnLineLabel;
    // End of variables declaration                   

    private void registerListeners() {
        this.connectButton.addActionListener(RoomPanelController.getInstance());
        this.loginButton.addActionListener(RoomPanelController.getInstance());
        this.sendMessageButton.addActionListener(RoomPanelController.getInstance());
        this.disconnectButton.addActionListener(RoomPanelController.getInstance());
        this.openPrivateButton.addActionListener(RoomPanelController.getInstance());
        this.usersList.addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent e) {
                String username = RoomPanelController.getInstance().getUsername();
                if (username != null && usersList.getModel().getSize() > 1
                        && usersList.getSelectedValue() != null
                        && !((String) usersList.getSelectedValue()).equals(username)) {
                    openPrivateButton.setEnabled(true);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                javax.swing.SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        String username = RoomPanelController.getInstance().getUsername();
                        if (openPrivateButton.hasFocus()
                                && username != null && usersList.getModel().getSize() > 1
                                && !((String) usersList.getSelectedValue()).equals(username)) {
                            openPrivateButton.doClick();
                        }
                        usersList.clearSelection();
                        openPrivateButton.setEnabled(false);
                    }
                });
            }

        });

        this.usersList.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                String username = RoomPanelController.getInstance().getUsername();
                if (username != null && usersList.getModel().getSize() > 1
                        && usersList.getSelectedValue() != null
                        && !((String) usersList.getSelectedValue()).equals(username)) {
                    openPrivateButton.setEnabled(true);
                } else {
                    openPrivateButton.setEnabled(false);
                }
            }
        });
        this.passwordField.addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent e) {

            }

            @Override
            public void focusLost(FocusEvent e) {
                loginButton.requestFocusInWindow();
            }
        });
        this.messageTextArea.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    sendMessageButton.doClick();
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

    public JPasswordField getPasswordField() {
        return passwordField;
    }

    public JTextField getUsernameTextField() {
        return usernameTextField;
    }

    public JTextArea getMessageTextArea() {
        return messageTextArea;
    }

    public DefaultListModel getModel() {
        return model;
    }

    public JList getUsersList() {
        return usersList;
    }

    public void appendToPane(String msg, Color c) {
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);
        try {
            doc.insertString(0, msg, aset);
        } catch (BadLocationException ex) {
            Logger.getLogger(RoomPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
