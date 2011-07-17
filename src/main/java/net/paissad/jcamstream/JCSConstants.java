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

/**
 * @author Papa Issa DIAKHATE (paissad)
 * 
 */
public interface JCSConstants {

    // General settings ...
    String JCS_NAME                 = "JCamStream";
    String JCS_MAJOR_VERSION        = "0";
    String JCS_MINOR_VERSION        = "0";
    String JCS_MICRO_VERSION        = "1";
    String JCS_VERSION              = new StringBuilder()
                                            .append(JCS_MAJOR_VERSION + ".")
                                            .append(JCS_MINOR_VERSION + ".")
                                            .append(JCS_MICRO_VERSION)
                                            .toString();

    String JCS_DESCRIPTION          = "A simple application for video surveillance written in Java";
    String JCS_AUTHOR               = "Papa Issa DIAKHATE (paissad)";
    String JCS_COPYRIGHT            = "Copyright (C) 2011 " + JCS_AUTHOR;

    // Logger settings ...
    String JCS_DEFAULT_LOG_FILENAME = "jcamstream.log";
    String JCS_DEFAULT_MAX_LOG_SIZE = "10MB";

    // System specifics settings ...
    String OS_NAME                  = System.getProperty("os.name");
    String OS_ARCH                  = System.getProperty("os.arch");
    String OS_VERSION               = System.getProperty("os.version");

    // JVM specifics settings ...
    String JVM_NAME                 = System.getProperty("java.vm.name");
    String JAVA_VERSION             = System.getProperty("java.version");
    String JAVA_VENDOR              = System.getProperty("java.vendor");

    String FILE_SEP                 = System.getProperty("file.separator");
    String PATH_SEP                 = System.getProperty("path.separator");
    String LINE_SEP                 = System.getProperty("line.separator");

    int    EXIT_SUCCESS             = 0;
    int    EXIT_ERROR               = 1;
}
