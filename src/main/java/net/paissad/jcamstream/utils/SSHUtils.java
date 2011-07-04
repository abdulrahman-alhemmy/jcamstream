/*
 * JCamStream, simple Java application for video surveillance from webcams.
 * Copyright (C) 2011 Papa Issa DIAKHATE (paissad).
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package net.paissad.jcamstream.utils;

import java.io.File;
import java.io.IOException;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.xfer.FileSystemFile;
import net.schmizz.sshj.xfer.scp.SCPFileTransfer;

/**
 * @author Papa Issa DIAKHATE (paissad)
 * 
 */
public class SSHUtils {

    /*
     * https://github.com/shikhar/sshj/tree/master/src/main/java/examples
     */

    private String    sshUser;
    private String    sshPassword;
    private String    sshServerHost;
    private int       sshServerPort;

    private SSHClient ssh;

    // _________________________________________________________________________
    // Constructors ...

    /**
     * 
     * @param user
     * @param password
     * @param host
     * @param port
     */
    public SSHUtils(String user, String password, String host, int port) {

        this.setSshUser(user);
        this.setSshPassword(password);
        this.setSshServerHost(host);

        int local_port = (port > 0) ? port : SSHClient.DEFAULT_PORT;
        this.setSshServerPort(local_port);

        ssh = new SSHClient();
    }

    // _________________________________________________________________________

    /**
     * Sends an InputStream to SSH server where the connection and then create a
     * file using the specified filename.
     * 
     * @param localFile
     *            - The local file to upload to the SSH server.
     * @param remoteDirName
     *            - The directory where to place the transfered file.
     * 
     * @throws IOException
     */
    public void scpUpload(File localFile, String remoteDirName) throws IOException {
        // TODO: the user name to use should not be hard coded !
        String localUserName = System.getProperty("user.name");
        ssh.authPublickey(localUserName);
        ssh.useCompression();
        SCPFileTransfer scp = ssh.newSCPFileTransfer();
        scp.upload(new FileSystemFile(localFile), remoteDirName);
    }

    // _________________________________________________________________________

    /**
     * Connects to the SSH server.
     * 
     * @throws IOException
     * 
     * @see #disconnect()
     * 
     */
    public void establishConnection() throws IOException {
        ssh.loadKnownHosts();
        ssh.connect(this.getSshServerHost(), this.getSshServerPort());
    }

    // _________________________________________________________________________

    /**
     * Disconnects from the SSH server.
     * 
     * @throws IOException
     */
    public void disconnect() throws IOException {
        ssh.disconnect();
    }

    // _________________________________________________________________________
    // Getters / Setters ...

    public String getSshUser() {
        return sshUser;
    }

    public void setSshUser(String sshUser) {
        this.sshUser = sshUser;
    }

    public String getSshPassword() {
        return sshPassword;
    }

    public void setSshPassword(String sshPassword) {
        this.sshPassword = sshPassword;
    }

    public String getSshServerHost() {
        return sshServerHost;
    }

    public void setSshServerHost(String sshServerHost) {
        this.sshServerHost = sshServerHost;
    }

    public int getSshServerPort() {
        return sshServerPort;
    }

    public void setSshServerPort(int sshServerPort) {
        this.sshServerPort = sshServerPort;
    }

    // _________________________________________________________________________

    /*
     * For testing purpose only ! //XXX
     */
    public static void main(String[] args) throws IOException {

        String user = "user";
        String password = "pass";
        String host = "server";
        int port = 2222;

        String remoteDirName = "/tmp";
        File localFile = new File("pom.xml");

        SSHUtils ssh = new SSHUtils(user, password, host, port);
        try {
            ssh.establishConnection();
            ssh.scpUpload(localFile, remoteDirName);

        } finally {
            ssh.disconnect();
        }

    }
}
