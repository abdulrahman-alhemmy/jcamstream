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
package net.paissad.jcamstream;

import static net.paissad.jcamstream.JCSConstants.EXIT_ERROR;
import static net.paissad.jcamstream.JCSConstants.EXIT_SUCCESS;
import static net.paissad.jcamstream.JCSConstants.JCS_NAME;
import static net.paissad.jcamstream.JCSConstants.JCS_VERSION;

import java.io.File;
import java.util.List;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.slf4j.Logger;

import net.paissad.jcamstream.factory.JCSLoggerFactory;
import net.paissad.jcamstream.logging.LogDirDefiner;
import net.paissad.jcamstream.logging.LogFileNameDefiner;
import net.paissad.jcamstream.logging.LogReloader;

/**
 * @author Papa Issa DIAKHATE (paissad)
 * 
 */
public class JCS {

    private static File         logfile    = null;
    private static File         configFile = null;
    private static List<String> arguments  = null;

    private static Logger       logger;

    // _________________________________________________________________________

    private JCS() {
    }

    // _________________________________________________________________________

    public static void main(String[] args) {

        processOptionsAndArguments(args);
        updatePathOfLogFile();

        /*
         * It's better to initialize the logger after the logger environnement
         * is already set !
         */
        logger = JCSLoggerFactory.getLogger(JCS.class);

        logger.info("Starting {} ({})", JCS_NAME, JCS_VERSION);
        logger.trace("a trace message ...");
        logger.error("error stuff ...");
        logger.warn("Don't bother for this warning !");
        logger.info("ok");
    }

    // _________________________________________________________________________

    /**
     * Reads the options and arguments specified from the command line.
     * 
     * @param args
     *            - The command line arguments.
     */
    private static void processOptionsAndArguments(String... args) {
        JCSOptions options = new JCSOptions();
        CmdLineParser cmdParser = new CmdLineParser(options);

        try {
            cmdParser.parseArgument(args);

            printHelpIfSpecifiedAndExit(options);
            printVersionIfSpecifiedAndExit(options);

            File logfileFromCmdLine = options.getLogfile();
            if (logfileFromCmdLine != null) {
                setLogfile(logfileFromCmdLine);
            }

            File confFileFromCmdLine = options.getConfigFile();
            if (confFileFromCmdLine != null) {
                setConfigFile(confFileFromCmdLine);
            }

            List<String> argsFromCmdLine = options.getArguments();
            if (argsFromCmdLine != null) {
                for (String arg : argsFromCmdLine)
                    System.out.println(arg); // XXX
                setArguments(argsFromCmdLine);
            }

        } catch (CmdLineException cle) {
            /*
             * If the '-v' or '-h' options are specified, a CmlLineException
             * should not be thrown for the only purpose that a required option
             * such as '-c' is not specified.
             */
            printHelpIfSpecifiedAndExit(options);
            printVersionIfSpecifiedAndExit(options);
            /*
             * At this step, no '-h' or '-v' options was specified while the
             * CmdLineException was thrown ... so we must print the error
             * message to STDERR and the help to STDOUT !
             */
            System.err.println(cle.getMessage());
            options.printHelpToStdout();
            System.exit(EXIT_ERROR);
        }
    }

    private static void printHelpIfSpecifiedAndExit(JCSOptions options) {
        if (options.isHelp()) {
            options.printHelpToStdout();
            System.exit(EXIT_SUCCESS);
        }
    }

    private static void printVersionIfSpecifiedAndExit(JCSOptions options) {
        if (options.isVersion()) {
            options.printVersionToStdout();
            System.exit(EXIT_SUCCESS);
        }
    }

    // _________________________________________________________________________

    private static void updatePathOfLogFile() {
        File f = getLogfile();
        if (f != null) {
            File logDir = f.getParentFile();
            logDir = (logDir == null) ? new File(".") : logDir;
            String logFileName = f.getName();
            LogDirDefiner.setLogDir(logDir);
            LogFileNameDefiner.setLogFileName(logFileName);
            LogReloader.reload();
        }
    }

    // _________________________________________________________________________
    // Getters / Setters ...

    private static void setLogfile(File logfile) {
        JCS.logfile = logfile;
    }

    private static File getLogfile() {
        return logfile;
    }

    public static void setConfigFile(File configFile) {
        JCS.configFile = configFile;
    }

    public static File getConfigFile() {
        return configFile;
    }

    public static void setArguments(List<String> arguments) {
        JCS.arguments = arguments;
    }

    public static List<String> getArguments() {
        return arguments;
    }

    // _________________________________________________________________________

}
