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
package com.orboan.chatapp.j.client.presentation.views;

import com.orboan.chatapp.j.client.presentation.controllers.MainViewController;
import com.orboan.chatapp.j.client.presentation.views.panes.LogPanel;
import com.orboan.chatapp.j.client.presentation.views.panes.RoomPanel;
import com.orboan.chatapp.j.client.presentation.views.panes.SettingsPanel;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

/**
 *
 * @author Oriol Boix Anfosso <dev@orboan.com>
 */
public class MainView {

    private static MainView obj = null;

    public static MainView getInstance() {
        if (obj == null) {
            obj = new MainView();
        }
        return obj;
    }

    private MainView() {
        this.initComponents();
        this.registerListeners();
    }
    List<JPanel> panels = new ArrayList();

    public List<JPanel> getPanels() {
        return panels;
    }
    private JFrame jfSdi;
    private JMenuBar menuBar;
    private JMenu menu1;
    private JMenuItem menuItem1, menuItem2, menuItem3;

    public static MainView getObj() {
        return obj;
    }

    public JFrame getJfSdi() {
        return jfSdi;
    }

    public JMenuBar getMenuBar() {
        return menuBar;
    }

    public JMenu getMenu1() {
        return menu1;
    }

    public JMenuItem getMenuItem1() {
        return menuItem1;
    }

    public JMenuItem getMenuItem2() {
        return menuItem2;
    }

    public JMenuItem getMenuItem3() {
        return menuItem3;
    }

    private void initComponents() {
        this.jfSdi = new JFrame();
        this.jfSdi.setTitle("ChatApp client");
        this.jfSdi.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.jfSdi.setResizable(true);      

        this.menuBar = new JMenuBar();
        this.menu1 = new JMenu("Views");
        this.menuBar.add(menu1);
        this.menuItem1 = new JMenuItem("Chat Room");
        this.menu1.add(menuItem1);
        this.menuItem2 = new JMenuItem("Settings");
        this.menu1.add(menuItem2);
        this.menuItem3 = new JMenuItem("Logs");
        this.menu1.add(menuItem3);        

        jfSdi.setJMenuBar(menuBar);

        SettingsPanel settingsPanel = SettingsPanel.getInstance();
        settingsPanel.initController();
        
        panels.add(RoomPanel.getInstance()); //Panel 0
        panels.add(settingsPanel); //Panel 1
        panels.add(LogPanel.getInstance()); //Panel 2

        this.setPanel(panels, 0);
    }

    private void setPanel(List<JPanel> panels, int panelToShow) {
        for (JPanel panel : panels) {
            if (panel != null && panel.isShowing()) {
                this.jfSdi.remove(panel);
            }
        }
        this.jfSdi.add(panels.get(panelToShow));
        this.jfSdi.pack();
        this.jfSdi.setLocationRelativeTo(null);
        this.jfSdi.setVisible(true);
    }

    public void setPanel(int panelToShow) {
        this.setPanel(panels, panelToShow);
    }

    private void registerListeners() {
        this.menuItem1.addActionListener(MainViewController.getInstance());
        this.menuItem2.addActionListener(MainViewController.getInstance());
        this.menuItem3.addActionListener(MainViewController.getInstance());
        this.jfSdi.addWindowListener(MainViewController.getInstance());
    }

}
