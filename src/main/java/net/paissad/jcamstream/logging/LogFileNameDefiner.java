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
package net.paissad.jcamstream.logging;

import static net.paissad.jcamstream.JCSConstants.JCS_DEFAULT_LOG_FILENAME;
import ch.qos.logback.core.PropertyDefinerBase;

/**
 * @author Papa Issa DIAKHATE (paissad)
 * 
 */
public class LogFileNameDefiner extends PropertyDefinerBase {

    private static String logFileName = null;

    /*
     * (non-Javadoc)
     * 
     * @see ch.qos.logback.core.spi.PropertyDefiner#getPropertyValue()
     */
    @Override
    public String getPropertyValue() {
        boolean ok = logFileName != null && !logFileName.isEmpty();
        return (ok) ? logFileName : JCS_DEFAULT_LOG_FILENAME;
    }

    /**
     * Sets the name of the log file.
     * <p>
     * <b>Note</b>: Do not forget to reload the logger after the use of this
     * function in order to make changes effective.
     * </p>
     * 
     * @param logFileName
     *            - The name of the log file to use.
     * @see LogReloader#reload()
     */
    public static void setLogFileName(String logFileName) {
        LogFileNameDefiner.logFileName = logFileName;
    }

}
