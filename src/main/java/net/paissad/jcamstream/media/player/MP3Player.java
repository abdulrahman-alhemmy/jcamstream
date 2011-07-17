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

import java.io.InputStream;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;

import org.slf4j.Logger;

import net.paissad.jcamstream.factory.JCSLoggerFactory;
import net.paissad.jcamstream.media.IAudioPlayer;

/**
 * @author Papa Issa DIAKHATE (paissad)
 * 
 */
class MP3Player implements IAudioPlayer {

    private static Logger  logger    = JCSLoggerFactory.getLogger(MP3Player.class);

    private AdvancedPlayer mp3player = null;

    public MP3Player(final InputStream in, final AudioPlayerListener listener) throws
            JavaLayerException {

        try {
            this.mp3player = new AdvancedPlayer(in);
            if (listener != null) {
                PlaybackListener mp3Listener = new PlaybackListener() {
                    @Override
                    public void playbackStarted(PlaybackEvent evt) {
                        listener.playerStarted();
                    }

                    @Override
                    public void playbackFinished(PlaybackEvent evt) {
                        listener.playerStopped();
                    }
                };
                this.mp3player.setPlayBackListener(mp3Listener);
            }

        } catch (JavaLayerException jle) {
            String errMsg = "Error while creating an instance of MP3Player";
            logger.error("{} : {}", errMsg, jle);
            throw new JavaLayerException(errMsg, jle);
        }
    }

    @Override
    public void play() {
        try {
            mp3player.play();
        } catch (JavaLayerException jle) {
            logger.error("Error while playing the mp3 file : {}", jle);
        }
    }

    @Override
    public void stop() {
        mp3player.close();
    }
}
