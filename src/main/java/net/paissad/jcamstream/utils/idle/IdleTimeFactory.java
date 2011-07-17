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
package net.paissad.jcamstream.utils.idle;

import org.slf4j.Logger;

import com.sun.jna.Platform;

import net.paissad.jcamstream.factory.JCSLoggerFactory;

/**
 * @author Papa Issa DIAKHATE (paissad)
 * 
 */
public class IdleTimeFactory {

    private static Logger logger = JCSLoggerFactory.getLogger(IdleTimeFactory.class);

    private IdleTimeFactory() {
    }

    /**
     * Get the idle time for a platform.
     * 
     * @return The idle time of the system, or -1 if the system is unknown.
     */
    public static int getIdleTime()
    {
        if (Platform.isWindows()) {
            return Win32IdleTime.getIdleTimeMillis();
        }
        else if (Platform.isLinux()) {
            return (int) LinuxIdleTime.getIdleTimeMillis();
        }
        else if (Platform.isMac()) {
            return (int) MacIdleTime.getIdleTimeMillis();
        }
        else {
            logger.error("Unable to get the idle time, this platform is unknown.");
            return -1;
        }
    }
}
