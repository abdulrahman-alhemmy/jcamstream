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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import javazoom.jl.decoder.JavaLayerException;

import org.slf4j.Logger;

import net.paissad.jcamstream.factory.JCSLoggerFactory;
import net.paissad.jcamstream.media.IAudioPlayer;

/**
 * @author Papa Issa DIAKHATE (paissad)
 * 
 */
public class AudioPlayerFactory {

    private static Logger logger = JCSLoggerFactory.getLogger(AudioPlayerFactory.class);

    private AudioPlayerFactory() {
    }

    // _________________________________________________________________________

    /**
     * 
     * @param in
     *            - The InputStream of a correct audio file.
     * @param listener
     * @return An instance of {@link IAudioPlayer} ready to be used for playing,
     *         <code>null</code> if the specified format is not recognized.
     * @throws JavaLayerException
     * @throws LineUnavailableException
     * @throws IOException
     * @throws UnsupportedAudioFileException
     */
    public static IAudioPlayer getIntance(
            final InputStream in,
            final AudioPlayerListener listener) throws
            JavaLayerException, UnsupportedAudioFileException, IOException, LineUnavailableException {

        List<AudioFileFormat.Type> supportedTypes =
                Arrays.asList(AudioSystem.getAudioFileTypes());

        IAudioPlayer player = null;

        try {
            AudioSystem.getAudioFileFormat(in);
            player = new BasicAudioPlayer(in, listener);

        } catch (UnsupportedAudioFileException e) {
            logger.trace("The system does not support this audio file format !");
            logger.trace("Supported types are {}, going to use the MP3 player.", supportedTypes);
            player = new MP3Player(in, listener);
        }

        return player;
    }

    // _________________________________________________________________________

    /*
     * For testing purpose only ! XXX
     */
    public static void main(String[] args) throws IOException, JavaLayerException, UnsupportedAudioFileException,
            LineUnavailableException, InterruptedException {
        File audioFile = new File("src/test/resources/sounds/alarm.mp3");
        // File audioFile = new File("src/test/resources/sounds/alarm.mp3");
        AudioPlayerListener listener = null;
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(audioFile));
        final IAudioPlayer player = AudioPlayerFactory.getIntance(in, listener);
        new Thread() {
            @Override
            public void run() {
                player.play();
            };
        }.start();
        TimeUnit.SECONDS.sleep(1);
        player.stop();
    }
}
