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
package com.orboan.chatapp.j.client.integration.sockets;

import com.orboan.chatapp.j.shared.model.Message;
import java.io.IOException;

/**
 *
 * @author Oriol Boix Anfosso <dev@orboan.com>
 */
public interface IClient {

    /**
     * Opens a new connection to the specified server and start a new thread to
     * repeatedly poll for new incoming messages. The server address (IP and
     * port) are specified via the settings options panel. If none is specified
     * the default server address is used (localhost and 13000).
     *
     * @throws IOException
     */
    public void open() throws IOException;

    /**
     * Returns whether this client is connected or not to a server.
     *
     * @return
     */
    public boolean isConnected();

    /**
     * It closes the connection to the server. Polling for incoming messages
     * ends.
     *
     * @throws IOException
     */
    public void close() throws IOException;

    /**
     * Sends a login request and waits a maximum of 5 seconds for a response. If
     * there is no response, the login must be attempted again.
     *
     * @param username The username used to authenticate the user
     * @param password The password for authentication
     * @return
     */
    public boolean login(String username, char[] password);

    /**
     * Sends a logout request and waits a maximum of 5 seconds for a response.
     * If there is no response, the logout should be attempted again.
     *
     * @param username The username for the user who wants to logout
     * @return
     */
    public boolean logout(String username);
    
    /**
     * Sends a message to the server.
     * @param message 
     */
    public void send(Message message);
    
    /**
     * To reload the settings this client is configured of.
     * For instance, the server address and port.
     * This method should be called when the user changes the
     * settings.
     * The settings are saved to a file and loaded from it.
     */
    public void reloadSettings();
}
