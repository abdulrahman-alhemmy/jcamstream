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

/**
 * @author Papa Issa DIAKHATE (paissad)
 * 
 */
public class CommonUtils {

    /**
     * @param bytes
     * @param si
     *            - If <code>true</code> then use 1000 unit, 1024 otherwise.
     * @return A String representation of the file size.
     * 
     */
    public static String humanReadableByteCount(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;

        if (bytes < unit)
            return bytes + " B";

        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + "";
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

}