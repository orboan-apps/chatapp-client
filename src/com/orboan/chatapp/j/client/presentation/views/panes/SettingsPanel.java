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

import com.orboan.chatapp.j.client.libraries.FlashingLabel;
import com.orboan.chatapp.j.client.libraries.RiverLayout;
import com.orboan.chatapp.j.client.model.ServerSettings;
import com.orboan.chatapp.j.client.presentation.controllers.SettingsPanelController;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

/**
 *
 * @author Oriol Boix Anfosso <dev@orboan.com>
 */
public class SettingsPanel extends javax.swing.JPanel {

    private static SettingsPanel obj = null;
    private final String defaultServerAddress = 
            ServerSettings.DEFAULT_SERVER;
    private final int defaultServerPort = 
            ServerSettings.DEFAULT_PORT;

    private SettingsPanel() {
        initComponents();
        registerListeners();
    }

    public static SettingsPanel getInstance() {
        if (obj == null) {
            obj = new SettingsPanel();
        }
        return obj;
    } 
    
    private JLabel serverLabel;
    private JLabel portLabel;
    private FlashingLabel feedbackLabel;
    private JTextField serverTextField;
    private JTextField portTextField;
    private JButton saveButton;

    public JTextField getServerTextField() {
        return serverTextField;
    }

    public JTextField getPortTextField() {
        return portTextField;
    }        

    public JButton getSaveButton() {
        return saveButton;
    }   

    public FlashingLabel getFeedbackLabel() {
        return feedbackLabel;
    }        

    public void setFeedbackLabel(FlashingLabel feedbackLabel) {
        this.remove(feedbackLabel);
        this.feedbackLabel = feedbackLabel;
        this.add("tab", feedbackLabel);
    }
    
    private void initComponents() {
        serverLabel = new JLabel("Server address: ");
        portLabel = new JLabel("Server Port number: ");
        serverTextField = new JTextField(defaultServerAddress, 20);
        portTextField = new JTextField(Integer.toString(defaultServerPort), 6); 
        feedbackLabel = new FlashingLabel();
        saveButton = new JButton("Save");
        final RiverLayout lm = new RiverLayout();
        this.setLayout(lm);
        this.setBorder(BorderFactory.createBevelBorder(EtchedBorder.RAISED));
        this.setPreferredSize(new Dimension(800, 250));
        this.setBorder(BorderFactory.createTitledBorder("Server Settings"));
        this.add("p", serverLabel);
        this.add("tab", serverTextField);
        this.add("p", portLabel);
        this.add("tab", portTextField);        
        this.add("p tab", saveButton);
        this.add("tab", feedbackLabel);
    }
    
    private void registerListeners() {
        this.saveButton.addActionListener(
                SettingsPanelController.getInstance());
    }
    
    public void initController() {
        SettingsPanelController.getInstance().setSettingsPane(this);
    }
}
