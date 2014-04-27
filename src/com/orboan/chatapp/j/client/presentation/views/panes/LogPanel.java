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
import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;
import com.orboan.chatapp.j.client.libraries.RiverLayout;

/**
 *
 * @author Oriol Boix Anfosso <dev@orboan.com>
 */
public class LogPanel extends javax.swing.JPanel {
    private static LogPanel obj = null;
    private LogPanel(){
        initComponents();
    }
    public static LogPanel getInstance(){
        if(obj == null) {
            obj = new LogPanel();
        }
        return obj;
    }
    
    private JScrollPane jScrollPane1;
    private JTextArea logTextArea;
    
    private void initComponents() {
        logTextArea = new JTextArea(18, 35);
        logTextArea.setEditable(false);
        logTextArea.setLineWrap(true);
        logTextArea.setWrapStyleWord(true);
        logTextArea.setBackground(new Color(236,247,217));
        jScrollPane1 = new JScrollPane(logTextArea);        
        final RiverLayout lm = new RiverLayout();
        this.setLayout(lm);        
        this.setBorder(BorderFactory.createBevelBorder(EtchedBorder.RAISED));
        this.setPreferredSize(new Dimension(880, 600));
        this.setBorder(BorderFactory.createTitledBorder("Client log"));
        this.add("p hfill vfill", this.jScrollPane1);                
    }

    public JTextArea getLogTextArea() {
        return logTextArea;
    }        
}
