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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import net.paissad.jcamstream.exception.FTPException;

/**
 * This class contains convenient methods like for example uploading files to a
 * FTP server.
 * 
 * @author Papa Issa DIAKHATE (paissad)
 * 
 */
public class FTPUtils {

    private String    ftpUser;
    private String    ftpPassword;
    private String    ftpServerHost;
    private int       ftpServerPort;
    private String    ftpServerDir;

    private FTPClient ftpClient;

    // _________________________________________________________________________
    // Constructors ...

    /**
     * Constructs a <code>FTPUtils</code> object.
     * 
     * @param user
     *            - The user who connects to the FTP server.
     * @param password
     *            - The password of the user.
     * @param host
     *            - The hostname of the FTP server we want to connect to.
     * @param port
     *            - The port of the FTP server. Must be a value > 0. If the
     *            value is <= 0, then default port is used.
     * @param dir
     *            - The directory where we want to start the connection. For
     *            example, if we want to upload some files, the files will be
     *            put into that directory.<br>
     *            If the specified value is <code>null</code> or empty, the FTP
     *            root "/" is used.
     */
    public FTPUtils(String user, String password, String host, int port, String dir) {
        this.setFtpUser(user);
        this.setFtpPassword(password);
        this.setFtpServerHost(host);

        int local_port = (port > 0) ? port : FTPClient.DEFAULT_PORT;
        this.setFtpServerPort(local_port);

        String local_dir = (dir == null || dir.isEmpty()) ? "/" : dir;
        this.setFtpServerDir(local_dir);
    }

    // _________________________________________________________________________

    public void estabishConnection() throws SocketException, IOException, FTPException {

        this.setFtpClient(new FTPClient());
        String errMsg;

        FTPClient client = this.getFtpClient();
        PrintCommandListener listener = new PrintCommandListener(System.out);
        client.addProtocolCommandListener(listener);

        // Connects to the FTP server
        String host = this.getFtpServerHost();
        int port = this.getFtpServerPort();
        client.connect(host, port);
        if (!FTPReply.isPositiveCompletion(client.getReplyCode())) {
            client.disconnect();
            errMsg = "Unable to connect to the server " + this.getFtpServerHost();
            this.verifyReplyCode(errMsg);
        }

        // Login to the FTP server
        String username = this.getFtpUser();
        String pass = this.getFtpPassword();
        if (!client.login(username, pass)) {
            errMsg = "Unable to login to " + this.getFtpServerHost();
            this.verifyReplyCode(errMsg);
        }

        // Change the current directory
        String dirname = this.getFtpServerDir();
        if (!client.changeWorkingDirectory(dirname)) {

            System.out.println("Unable to change to the directory '" + dirname + "'.");
            System.out.println("Going to create the directory !");
            this.mkdirs(dirname);
            System.out.println("Creation of the directory is successful.");
        }

        client.changeWorkingDirectory(dirname);
        if (!FTPReply.isPositiveCompletion(client.getReplyCode())) {
            errMsg = "Unable to change to the directory : " + dirname;
            this.verifyReplyCode(errMsg);
        }

        client.pwd();
    }

    // _________________________________________________________________________

    /**
     * Upload files to the FTP server and into the specified directory.
     * 
     * @param files
     *            - The files to upload.
     * @throws IOException
     * @throws FTPException
     */
    public void uploadFiles(List<File> files) throws IOException, FTPException {

        for (File aFile : files) {
            String filename = aFile.getName();
            BufferedInputStream bis = null;

            try {
                bis = new BufferedInputStream(new FileInputStream(aFile));
                this.uploadStream(filename, bis);

            } finally {
                if (bis != null)
                    bis.close();
            }
        }
    }

    // _________________________________________________________________________

    /**
     * Stores a file on the server using the given name and taking input from
     * the given InputStream.
     * <p>
     * <b>Note</b>: The InputStream is not closed ! Feel free or responsible to
     * close it (or not) after use.
     * </p>
     * 
     * @param remoteFileName
     *            - The name that will be given to the file onto the FTP server.
     * @param in
     *            - The stream to upload the FTP server.
     * @throws IOException
     * @throws FTPException
     */
    public void uploadStream(String remoteFileName, InputStream in) throws IOException, FTPException {
        FTPClient client = this.getFtpClient();
        String errMsg;

        int filesize = in.available();
        client.allocate(filesize);
        String humanFileSize = CommonUtils.humanReadableByteCount(filesize, false);
        errMsg = "Unable to allocate the amount of size " + humanFileSize + " for the file " + remoteFileName;
        this.verifyReplyCode(errMsg);

        client.storeFile(remoteFileName, in);
        errMsg = "Unable to store the file " + remoteFileName + " to the server";
        this.verifyReplyCode(errMsg);
    }

    // _________________________________________________________________________

    /**
     * Logout and then disconnect from the FTP server.
     * 
     * @throws IOException
     * 
     * @see org.apache.commons.net.ftp.FTPClient#logout()
     * @see org.apache.commons.net.ftp.FTPClient#disconnect()
     */
    public void logoutAndDisconnect() throws IOException {
        FTPClient client = this.getFtpClient();

        if (client.isConnected() && !FTPReply.isPositiveIntermediate(client.getReplyCode())) {
            client.logout();
            client.disconnect();
            System.out.println("Disconnected successfully from FTP server.");
        }

        if (client.isConnected() && !client.completePendingCommand()) {
            System.err.println("Something failed !");
        }
    }

    // _________________________________________________________________________

    /**
     * Create a directory onto the FTP server and recursively when necessary.
     * 
     * @param dirname
     *            - The directory to create.
     * @throws IOException
     * @throws FTPException
     */
    private void mkdirs(String dirname) throws IOException, FTPException {
        FTPClient client = this.getFtpClient();
        String errMsg;

        char firstChar = dirname.charAt(0);
        String currentRoot = (firstChar == '/') ? "/" : "";

        String[] subDirnames = dirname.split("/");

        for (String name : subDirnames) {
            if (!name.isEmpty()) {
                currentRoot += name + "/";
                client.makeDirectory(currentRoot);
                errMsg = "Error while creating the directory " + currentRoot;
                this.verifyReplyCode(errMsg);
            }
        }
    }

    // _________________________________________________________________________

    /**
     * Verifies whether or not the last previous FTP action/command finished
     * successfully, if
     * not then display the specified error message and throw a
     * {@link FTPException}
     * 
     * @param errorMessage
     *            - The error message to show if ever the previous ftp command
     *            did not return a correct/positive reply code.
     * @throws FTPException
     */
    private void verifyReplyCode(String errorMessage) throws FTPException {
        FTPClient client = this.getFtpClient();
        if (!FTPReply.isPositiveCompletion(client.getReplyCode())) {
            System.err.println(errorMessage);
            throw new FTPException(errorMessage);
        }
    }

    // _________________________________________________________________________
    // Getters / Setters ...

    /**
     * Gets the current FTPClient which is used.
     * 
     * @return The FTPClient which is currently used.
     */
    public FTPClient getFtpClient() {
        return ftpClient;
    }

    private void setFtpClient(FTPClient ftpClient) {
        this.ftpClient = ftpClient;
    }

    public String getFtpUser() {
        return ftpUser;
    }

    public void setFtpUser(String ftpUser) {
        this.ftpUser = ftpUser;
    }

    public String getFtpPassword() {
        return ftpPassword;
    }

    public void setFtpPassword(String ftpPassword) {
        this.ftpPassword = ftpPassword;
    }

    public String getFtpServerHost() {
        return ftpServerHost;
    }

    public void setFtpServerHost(String ftpServerHost) {
        this.ftpServerHost = ftpServerHost;
    }

    public int getFtpServerPort() {
        return ftpServerPort;
    }

    public void setFtpServerPort(int ftpServerPort) {
        this.ftpServerPort = ftpServerPort;
    }

    public String getFtpServerDir() {
        return ftpServerDir;
    }

    public void setFtpServerDir(String ftpServerDir) {
        this.ftpServerDir = ftpServerDir;
    }

    // _________________________________________________________________________

    /*
     * For testing purpose only ! //XXX
     */
    public static void main(String[] args) throws SocketException, IOException, FTPException {
        String user = "user";
        String password = "pass";
        String host = "ftp-server";
        int port = FTPClient.DEFAULT_PORT;
        String dir = "/subdir/another_dir/foo/bar";

        List<File> files = new ArrayList<File>();
        files.add(new File("pom.xml"));
        files.add(new File("stuffs.txt"));

        FTPUtils ftp = new FTPUtils(user, password, host, port, dir);
        try {
            ftp.estabishConnection();
            ftp.uploadFiles(files);

        } finally {
            ftp.logoutAndDisconnect();
        }
    }

}
