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
package net.paissad.jcamstream.media.player;

/**
 * @author Papa Issa DIAKHATE (paissad)
 * 
 */
public interface AudioPlayerListener {

    /**
     * This method in intended to be run when the player starts playing an audio
     * file.
     */
    public void playerStarted();

    /**
     * This method in intended to be run when the player stops playing an audio
     * file.
     */
    public void playerStopped();
}
