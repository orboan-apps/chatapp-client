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
package com.orboan.chatapp.j.client.libraries;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import javax.swing.Timer;

/**
 *
 * @author Oriol Boix Anfosso <dev@orboan.com>
 */
public class FlashingLabel extends JLabel {

    private int count;
    private Timer animator;
    private String blinkingText;
    private FlashingLabel flabel;
    private int maxSeconds;
    private static int DEFAULT_MAX_SECONDS = 3;
    
    {
        count = 0;
    }

    public FlashingLabel(String text) {
        super(text);   
        maxSeconds = DEFAULT_MAX_SECONDS;
    }

    public FlashingLabel() {
        super();        
        maxSeconds = DEFAULT_MAX_SECONDS;
    }
    
    public FlashingLabel(String text, int maxSecs){
        super(text);
        maxSeconds = maxSecs;
    }
    
    public FlashingLabel(int maxSecs){
        super();
        maxSeconds = maxSecs;
    }    

    public void setBlinkingText(String bt) {
        this.blinkingText = bt;
        this.flabel = this;
        super.setText("____________________");
    }

    @Override
    public void addNotify() {
        super.addNotify();
        animator = new Timer(500, animatorTask);
        count = 0;
        animator.start();
    }

    @Override
    public void paintComponent(Graphics g) {
//        super.paintComponent(g);                    
        if (count % 2 == 0) {
            g.setColor(Color.red);
        } else {
            g.setColor(Color.blue);
        }
        g.drawString(blinkingText, (getWidth() / 2) - 90, (getHeight() / 2) + 5);
        count++;
    }

    private final ActionListener animatorTask = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (count < maxSeconds*2) {
                repaint();
            } else {
                animator.stop();
                flabel.setText("");                
            }            
        }
    };
}
