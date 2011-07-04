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
package net.paissad.jcamstream.configuration;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

/**
 * This class contains convenient methods that read the main configuration file
 * of the application.
 * 
 * @author Papa Issa DIAKHATE (paissad)
 * 
 */
public class JCSConf {

    private static final String SMTP_HOST_KEY           = "mail.smtp.host";
    private static final String SMTP_USER_KEY           = "mail.smtp.user";
    private static final String SMTP_PASSWORD_KEY       = "mail.smtp.password";
    private static final String SMTP_PORT_KEY           = "mail.smtp.port";
    private static final String SMTP_AUTH_KEY           = "mail.smtp.auth";
    private static final String SMTP_USE_STARTTLS_KEY   = "mail.smtp.starttls.enable";
    private static final String SMTP_USE_SSL_KEY        = "mail.smtp.ssl.enable";
    private static final String MAIL_SUBJECT_KEY        = "mail.subject";
    private static final String MAIL_RECIPIENTS_TO_KEY  = "mail.recipients.to";
    private static final String MAIL_RECIPIENTS_CC_KEY  = "mail.recipients.cc";
    private static final String MAIL_RECIPIENTS_BCC_KEY = "mail.recipients.bcc";

    private static final int    DEFAULT_SMTP_PORT       = 25;
    private static final String RECIPIENTS_SEPARATOR    = ",";

    private static File         configFile;
    private static Properties   props;

    private JCSConf() throws IOException {
        loadConfig();
    }

    private static void loadConfig() throws IOException {
        props = new Properties();
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(getConfigFile()));
        try {
            props.load(bis);
        } catch (IOException ioe) {
            // String errMsg = "Error while loading the configuration file";
            // TODO: logger
            throw new IOException(ioe);
        }
    }

    /**
     * 
     * @return Le fichier de configuration courant qui est utilisé
     */
    private static File getConfigFile() {
        return configFile;
    }

    /**
     * @param configFile
     *            - Le fichier de configuration à utiliser.
     * @throws IOException
     *             - Si problème lors de lecture/ouverture du fichier spécifié.
     */
    public static void setConfigFile(File configFile) throws IOException {
        JCSConf.configFile = configFile;
        loadConfig(); // Do not forget to re-read the configuration file.
    }

    public static final String getSmtpHost() {
        return props.getProperty(SMTP_HOST_KEY);
    }

    public static final String getSmtpUser() {
        return props.getProperty(SMTP_USER_KEY);
    }

    public static final String getSmtpPassword() {
        return props.getProperty(SMTP_PASSWORD_KEY);
    }

    /**
     * @return The port specified into the configuration file, or the default
     *         SMTP port (usually 25) if the value was not set.
     */
    public static final int getSmtpPort() {
        String val = props.getProperty(SMTP_PORT_KEY);
        return (val != null) ? (Integer.parseInt(val)) : DEFAULT_SMTP_PORT;
    }

    public static final boolean isSmtpAuth() {
        String val = props.getProperty(SMTP_AUTH_KEY, "");
        return val.equals("true");
    }

    public static final boolean isUseSmtpSTARTTLS() {
        String val = props.getProperty(SMTP_USE_STARTTLS_KEY, "");
        return val.equals("true");
    }

    public static final boolean isUseSmtpSSL() {
        String val = props.getProperty(SMTP_USE_SSL_KEY, "");
        return val.equals("true");
    }

    public static final String getMailSubject() {
        return props.getProperty(MAIL_SUBJECT_KEY);
    }

    /**
     * Retrieve the set of recipients of type 'TO' from the configuration file.
     * 
     * @return The list of recipients stored into the configuration file.
     */
    public static final Set<String> getMailRecipientsTO() {
        String val = props.getProperty(MAIL_RECIPIENTS_TO_KEY, "");

        Set<String> recipients = new HashSet<String>();
        String[] array = val.split(RECIPIENTS_SEPARATOR);
        for (String aRecipient : array)
            recipients.add(aRecipient.trim());
        return recipients;
    }

    /**
     * Retrieve the set of recipients of type 'CC' from the configuration file.
     * 
     * @return The list of recipients stored into the configuration file.
     */
    public static final Set<String> getMailRecipientsCC() {
        String val = props.getProperty(MAIL_RECIPIENTS_CC_KEY, "");

        Set<String> recipients = new HashSet<String>();
        String[] array = val.split(RECIPIENTS_SEPARATOR);
        for (String aRecipient : array)
            recipients.add(aRecipient.trim());
        return recipients;
    }

    /**
     * Retrieve the set of recipients of type 'BCC' from the configuration file.
     * 
     * @return The list of recipients stored into the configuration file.
     */
    public static final Set<String> getMailRecipientsBCC() {
        String val = props.getProperty(MAIL_RECIPIENTS_BCC_KEY, "");

        Set<String> recipients = new HashSet<String>();
        String[] array = val.split(RECIPIENTS_SEPARATOR);
        for (String aRecipient : array)
            recipients.add(aRecipient.trim());
        return recipients;
    }

}
