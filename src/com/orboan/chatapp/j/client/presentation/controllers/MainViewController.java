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

import com.orboan.chatapp.j.client.presentation.views.MainView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Oriol Boix Anfosso <dev@orboan.com>
 */
public class MainViewController implements ActionListener, WindowListener {

    private static MainViewController obj = null;

    private MainViewController() {
        this.roomPanelController = RoomPanelController.getInstance();
    }

    public static MainViewController getInstance() {
        if (obj == null) {
            obj = new MainViewController();
        }
        return obj;
    }

    private final RoomPanelController roomPanelController;

    @Override
    public void actionPerformed(final ActionEvent e) {
        final Object obj = e.getSource();
        MainView mv = MainView.getInstance();

        int panelToShow = 0;
        if (obj == mv.getMenuItem1()) {
            panelToShow = 0;
        } else if (obj == mv.getMenuItem2()) {
            panelToShow = 1;
        } else if (obj == mv.getMenuItem3()) {
            panelToShow = 2;
        }
        mv.setPanel(panelToShow);
    }

    @Override
    public void windowOpened(WindowEvent e) {
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void windowClosing(WindowEvent e) {

        try {
            if (roomPanelController.getClient() != null
                    && roomPanelController.getClient().getClientChannel().isConnected()) {
                roomPanelController.getClient().close();
            }
        } catch (IOException ex) {
            Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void windowClosed(WindowEvent e) {
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void windowIconified(WindowEvent e) {
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void windowActivated(WindowEvent e) {
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
        //To change body of generated methods, choose Tools | Templates.
    }
}
