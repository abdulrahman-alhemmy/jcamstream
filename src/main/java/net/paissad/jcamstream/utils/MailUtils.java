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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * This class contains convenient methods for sending mails using a specified
 * SMTP server with the desired settings.<br>
 * It supports STARTTLS and SSL with the possibility to add some attachement
 * files too.
 * 
 * @author Papa Issa DIAKHATE (paissad)
 * 
 */
public class MailUtils {

    private static final String SMTP_PROTOCOL  = "smtp";
    private static final String SMTPS_PROTOCOL = "smtps";

    private String              smtpUser;
    private String              smtpPassword;
    private String              smtpHost;
    private int                 smtpPort;
    private boolean             smtpAuth;
    private boolean             starttls;
    private boolean             ssl;
    private List<File>          attachements;
    private String              subject;
    private String              content;
    private Set<String>         recipientsTO;
    private Set<String>         recipientsCC;
    private Set<String>         recipientsBCC;

    // _________________________________________________________________________
    // Constructors ...

    /**
     * By default, STARTTLS and SSL are set to false. If you want to use one of
     * them or both, just set them to true.
     * 
     * @param user
     *            - The user who sends the mail (from)
     * @param password
     *            - The password of the user who sends the mail
     * @param host
     *            - The hostname of the SMTP server to use for sending the mail.
     * @param port
     *            - The port of the SMTP server to use.
     * @param auth
     *            - Do we use auth ? (set it to true if authentication is needed
     *            to the SMTP server, set to false otherwise)
     * @param subject
     *            - The subject of the mail.
     * @param content
     *            - The content (body) of the mail. May be TEXT or HTML as well.
     * 
     * @see #setStarttls(boolean)
     * @see #setSsl(boolean)
     * @see #setRecipientsTO(Set)
     * @see #setAttachements(List)
     */
    public MailUtils(String user, String password, String host, int port, boolean auth,
            String subject, String content) {

        this.setSmtpUser(user);
        this.setSmtpPassword(password);
        this.setSmtpHost(host);
        this.setSmtpPort(port);
        this.setSmtpAuth(auth);
        this.setSubject(subject);
        this.setContent(content);

        this.setStarttls(false);
        this.setSsl(false);
    }

    // _________________________________________________________________________

    /**
     * Send the email to the recipients with no debugging.
     * 
     * @throws MessagingException
     * @see #sendMail(boolean)
     */
    public void sendMail() throws MessagingException {
        this.sendMail(false);
    }

    /**
     * Send the email to the recipients.
     * 
     * @param debug
     *            - Whether or not to debug the process.
     * 
     * @throws MessagingException
     */
    public void sendMail(final boolean debug) throws MessagingException {

        final Properties props = this.initializeSmtpProperties();

        Session mailSession = Session.getDefaultInstance(props, null);
        mailSession.setDebug(debug);
        Transport transport = null;
        try {
            // mail.transport.protocol => smtp
            transport = mailSession.getTransport();

            MimeMessage message = new MimeMessage(mailSession);
            String msgSubject = this.getSubject();
            message.setSubject(msgSubject);
            message.setSentDate(new Date());

            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(this.getContent(), "text/html; charset=ISO-8859-1");

            Multipart mp = new MimeMultipart();
            mp.addBodyPart(htmlPart);

            // Add the attachements files.
            this.addAttachementFilesToMessage(mp);

            message.setContent(mp);

            // Retrieve the recipients who should receive the mail.
            Set<String> to_recipients = this.getRecipientsTO();
            Set<String> cc_recipients = this.getRecipientsCC();
            Set<String> bcc_recipients = this.getRecipientsBCC();

            // Add the retrieved recipients to the Message object ...
            this.addRecipients(message, to_recipients, Message.RecipientType.TO);
            this.addRecipients(message, cc_recipients, Message.RecipientType.CC);
            this.addRecipients(message, bcc_recipients, Message.RecipientType.BCC);

            message.setFrom(new InternetAddress(this.getSmtpUser(), true));

            message.saveChanges();

            transport.connect(
                    this.getSmtpHost(),
                    this.getSmtpPort(),
                    this.getSmtpUser(),
                    this.getSmtpPassword());
            transport.sendMessage(message, message.getAllRecipients());

        } finally {
            if (transport != null)
                transport.close();
        }
    }

    // _________________________________________________________________________

    /**
     * Initializes the properties of the SMTP service to use.
     * 
     * @return The properties for the SMTP service to use.
     */
    private Properties initializeSmtpProperties() {
        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", SMTP_PROTOCOL);
        props.setProperty("mail.host", this.getSmtpHost());
        props.setProperty("mail.user", this.getSmtpUser());
        props.setProperty("mail.password", this.getSmtpPassword());
        props.setProperty("mail.smtp.port", String.valueOf(this.getSmtpPort()));
        props.setProperty("mail.smtp.auth", String.valueOf(this.isSmtpAuth()));
        props.setProperty("mail.smtp.starttls.enable", String.valueOf(this.isStarttls()));

        boolean isSSL = this.isSsl();
        if (isSSL) {
            props.setProperty("mail.transport.protocol", SMTPS_PROTOCOL);
            props.setProperty("mail.smtp.ssl.enable", String.valueOf(isSSL));
        }
        return props;
    }

    // _________________________________________________________________________

    /**
     * 
     * @param message
     *            - The message to use and where to add the recipients.
     * @param recipients
     *            - The recipients to add.
     * @param type
     *            - The type to use between (TO, CC, BCC)
     * @throws AddressException
     * @throws MessagingException
     */
    private void addRecipients(Message message, Set<String> recipients, Message.RecipientType type) throws
            AddressException, MessagingException {

        if (recipients == null)
            return; // Get out ! Do nothing ...

        for (String aRecipient : recipients)
            message.addRecipient(type, new InternetAddress(aRecipient));
    }

    // _________________________________________________________________________

    /**
     * Adds some attachements files to a {@link Multipart} object
     * 
     * @param multipart
     *            - The multipart object to use for adding the attachements to
     *            it !
     * @throws MessagingException
     */
    private void addAttachementFilesToMessage(Multipart multipart) throws MessagingException {

        List<File> files = this.getAttachements();
        if (files == null || files.isEmpty()) {
            return; // Get out ! There is no attachement file ...
        }

        // If there is at least one file, then attach them to the mail ...
        for (File aFile : files) {
            MimeBodyPart attachFilePart = new MimeBodyPart();
            FileDataSource fds = new FileDataSource(aFile);
            attachFilePart.setDataHandler(new DataHandler(fds));
            attachFilePart.setFileName(fds.getName());
            attachFilePart.setDisposition(MimeBodyPart.ATTACHMENT);
            multipart.addBodyPart(attachFilePart);
        }
    }

    // _________________________________________________________________________
    // Getters / Setters ...

    public final String getSmtpUser() {
        return smtpUser;
    }

    public final void setSmtpUser(String smtpUser) {
        this.smtpUser = smtpUser;
    }

    public final String getSmtpPassword() {
        return smtpPassword;
    }

    public final void setSmtpPassword(String smtpPassword) {
        this.smtpPassword = smtpPassword;
    }

    public final String getSmtpHost() {
        return smtpHost;
    }

    public final void setSmtpHost(String smtpHost) {
        this.smtpHost = smtpHost;
    }

    public final int getSmtpPort() {
        return smtpPort;
    }

    public final void setSmtpPort(int smtpPort) {
        this.smtpPort = smtpPort;
    }

    public final boolean isSmtpAuth() {
        return smtpAuth;
    }

    public final void setSmtpAuth(boolean smtpAuth) {
        this.smtpAuth = smtpAuth;
    }

    public final boolean isStarttls() {
        return starttls;
    }

    public final void setStarttls(boolean starttls) {
        this.starttls = starttls;
    }

    public final boolean isSsl() {
        return ssl;
    }

    public final void setSsl(boolean ssl) {
        this.ssl = ssl;
    }

    public final Set<String> getRecipientsTO() {
        return recipientsTO;
    }

    public final void setRecipientsTO(Set<String> recipients) {
        this.recipientsTO = recipients;
    }

    public final Set<String> getRecipientsCC() {
        return recipientsCC;
    }

    public final void setRecipientsCC(Set<String> recipients) {
        this.recipientsCC = recipients;
    }

    public final Set<String> getRecipientsBCC() {
        return recipientsBCC;
    }

    public final void setRecipientsBCC(Set<String> recipients) {
        this.recipientsBCC = recipients;
    }

    public final List<File> getAttachements() {
        return attachements;
    }

    public final void setAttachements(List<File> attachements) {
        this.attachements = attachements;
    }

    public final String getSubject() {
        return subject;
    }

    public final void setSubject(String subject) {
        this.subject = subject;
    }

    public final String getContent() {
        return content;
    }

    public final void setContent(String content) {
        this.content = content;
    }

    // _________________________________________________________________________

    /*
     * For testing purpose only ! //XXX
     */
    public static void main(String[] args) throws MessagingException {

        String user = "from@gmail.com";
        String password = "my-password";
        String host = "smtp.gmail.com";
        int port = 465;
        boolean auth = true;

        boolean starttls = true;
        boolean ssl = true;

        String subject = "Test mail from javamail API !";
        String htmlContent = "<h2>Hell guy, here are some attachements files ...</h2>";

        Set<String> to_recipients = new HashSet<String>();
        Set<String> cc_recipients = new HashSet<String>();
        Set<String> bcc_recipients = new HashSet<String>();

        MailUtils mail = new MailUtils(user, password, host, port, auth, subject, htmlContent);
        mail.setStarttls(starttls);
        mail.setSsl(ssl);

        // We add the recipients (to, cc & bcc)

        to_recipients.add("to@yahoo.fr");
        cc_recipients.add("cc@hotmail.fr");
        bcc_recipients.add("bcc@domain.com");

        mail.setRecipientsTO(to_recipients);
        mail.setRecipientsCC(cc_recipients);
        mail.setRecipientsBCC(bcc_recipients);

        // Let's add some files ...
        List<File> attachements = new ArrayList<File>();
        attachements.add(new File("pom.xml"));
        attachements.add(new File("stuffs.txt"));
        mail.setAttachements(attachements);

        boolean debug = true;
        mail.sendMail(debug);

    }
}
