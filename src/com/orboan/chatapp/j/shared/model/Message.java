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
package com.orboan.chatapp.j.shared.model;

/**
 *
 * @author Oriol Boix Anfosso <dev@orboan.com>
 */
import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final Charset CHARSET = StandardCharsets.UTF_8;
    public static final String EOL = "\n";

    private final Type type;
    private final String content, recipient;
    private String sender;
    private char[] password;
    private ArrayList<String> connectedUsers;

    public Type getType() {
        return type;
    }

    public String getSender() {
        return sender;
    }

    public String getContent() {
        return content;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }        

    public ArrayList<String> getConnectedUsers() {
        return connectedUsers;
    }  

    public void setConnectedUsers(ArrayList<String> connectedUsers) {
        this.connectedUsers = connectedUsers;
    }    
    
    public Message(String content) {
        this.type = Type.LOG;
        this.sender = Message.Sender.CHATAPP.toString();
        this.content = content; 
        this.recipient = Message.Recipient.SERVER.toString();
    }

    public Message(Type type, String sender, String content, String recipient) {
        this.type = type;
        this.sender = sender;
        this.content = content + EOL;
        this.recipient = recipient;
    }

    public Message(Message message) {
        this.type = message.type;
        this.sender = message.sender;
        this.content = message.content;
        this.recipient = message.recipient;
    }

    @Override
    public String toString() {
        //Remove EOL from contents
        String contents = new StringBuilder(this.content).substring(0, this.content.length() - 1);
        return "{type='" + type + "' sender='" + sender + "' content='" + contents + "' recipient='" + recipient + "'}";
    }

    public enum Type {

        MESSAGE(100), WARNING(101), ERROR(102), CONNECTION(103),
        LOGIN(110), LOGIN_FROM(111), LOGOUT(112), SIGNUP(113), BYE(114),
        UPLOAD_REQ(120), NO_MORE_MESSAGES(121), MESSAGE_ALL(201),
        WELCOME(122), OPEN_PRIVATE(133), CLOSE_PRIVATE(134), LOG(199);
        private final int type;

        Type(int type) {
            this.type = type;
        }

        public int getType() {
            return this.type;
        }
    }

    public enum Recipient {

        CLIENT("CLIENT"), SERVER("SERVER"), CHATAPP("ChatApp"),
        ALL("All");
        private final String recipient;

        Recipient(String recipient) {
            this.recipient = recipient;
        }

        @Override
        public String toString() {
            return this.recipient;
        }
    }
    
    public enum Sender {

        CLIENT("CLIENT"), SERVER("SERVER"), CHATAPP("ChatApp");
        private final String sender;

        Sender(String sender) {
            this.sender = sender;
        }

        @Override
        public String toString() {
            return this.sender;
        }
    }    

    public char[] getPassword() {        
        return password;
    }

    public void setPassword(char[] password) {
        this.password = password;
    }

    public void clearPassword() {
        for (int i = 0; i < password.length; i++) {
            password[i] = ' ';
        }
    }
}