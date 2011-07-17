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

import java.awt.image.BufferedImage;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Papa Issa DIAKHATE (paissad)
 * 
 */
public class CommonUtils {

    /**
     * Returns the filename extension without the dot.
     * 
     * @param filename
     * @return The extension without the dot, or <code>null</code> if the
     *         filename is null, or an empty String if the file has no
     *         extension.
     */
    public static String getFilenameExtension(final String filename) {
        if (filename == null)
            return null;
        Pattern pattern = Pattern.compile("\\.[^\\.]*$");
        Matcher matcher = pattern.matcher(filename);
        int count = matcher.groupCount();
        return (matcher.find()) ? matcher.group(count) : "";
    }

    // _________________________________________________________________________

    /**
     * @param bytes
     * @param si
     *            - If <code>true</code> then use 1000 unit, 1024 otherwise.
     * @return A String representation of the file size.
     * 
     */
    public static String humanReadableByteCount(final long bytes, final boolean si) {
        int unit = si ? 1000 : 1024;

        if (bytes < unit)
            return bytes + " B";

        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + "";
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

    // _________________________________________________________________________

    /**
     * Convert a {@link BufferedImage} of any type, to {@link BufferedImage} of
     * a specified type. If the source image is the
     * same type as the target type, then original image is returned,
     * otherwise new image of the correct type is created and the content
     * of the source image is copied into the new image.
     * 
     * @param sourceImage
     *            the image to be converted
     * @param targetType
     *            the desired BufferedImage type
     * 
     * @return a BufferedImage of the specified target type.
     * 
     * @see BufferedImage
     */
    public static BufferedImage convertImageToType(BufferedImage sourceImage, int targetType) {
        BufferedImage image;
        if (sourceImage.getType() == targetType)
            image = sourceImage;
        else {
            image = new BufferedImage(sourceImage.getWidth(), sourceImage.getHeight(), targetType);
            image.getGraphics().drawImage(sourceImage, 0, 0, null);
        }
        return image;
    }
}
