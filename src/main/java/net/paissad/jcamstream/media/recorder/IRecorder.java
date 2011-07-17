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
package net.paissad.jcamstream.media.recorder;

import java.io.File;

/**
 * @author Papa Issa DIAKHATE (paissad)
 * 
 */
public interface IRecorder {

    /**
     * Starts recording by creating a video file.
     * 
     * @param file
     *            - Where to save the file created by the capture.
     * @throws Exception
     */
    public void startRecording(File file) throws Exception;

    /**
     * Stops the recording.
     */
    public void stopRecording();

    /**
     * Gets one shot from and saves the image.
     * 
     * @param file
     *            - Where to save the image created by the shot.
     * @throws Exception
     */
    public void getSnapShot(File file) throws Exception;
}
