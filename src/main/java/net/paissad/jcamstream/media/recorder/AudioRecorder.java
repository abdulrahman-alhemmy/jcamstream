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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.Port;
import javax.sound.sampled.TargetDataLine;

import org.slf4j.Logger;

import net.paissad.jcamstream.factory.JCSLoggerFactory;

/**
 * @author Papa Issa DIAKHATE (paissad)
 * 
 */
public class AudioRecorder {

    private static final int     BUFFER_SIZE = 8192;

    private final TargetDataLine microphone;
    private final AudioFormat    audioFormat;

    private static Logger        logger;

    // _________________________________________________________________________

    static {
        logger = JCSLoggerFactory.getLogger(AudioRecorder.class);
    }

    // _________________________________________________________________________

    public AudioRecorder() throws LineUnavailableException {

        final float sampleRate = 44100.0f;
        final int sampleSizeInBits = 16;
        final int channels = 2;
        final boolean signed = true;
        final boolean bigEndian = true;

        audioFormat = new AudioFormat(
                sampleRate, sampleSizeInBits, channels, signed, bigEndian);

        final DataLine.Info info = new DataLine.Info(
                TargetDataLine.class, audioFormat, BUFFER_SIZE);

        if (!AudioSystem.isLineSupported(Port.Info.MICROPHONE)) {
            logger.warn("Unable to use the microphone in this system !");
        }

        if (!AudioSystem.isLineSupported(info)) {
            logger.error("The specified line is not supported : {}", info);
            throw new LineUnavailableException();
        }

        microphone = (TargetDataLine) AudioSystem.getLine(info);
    }

    // _________________________________________________________________________

    public void start() throws LineUnavailableException {
        if (microphone.isOpen()) {
            logger.info("The line (audio) is already opened !");
        } else {
            microphone.open(audioFormat, BUFFER_SIZE);
        }
        if (microphone.isRunning()) {
            logger.info("The line (audio) is already running");
        } else {
            logger.info("Starting the audio recorder ...");
            microphone.start();
        }
    }

    // _________________________________________________________________________

    public void stop() {
        if (microphone.isRunning()) {
            logger.info("Stopping the audio recorder.");
            microphone.stop();
            microphone.drain(); // Always call stop() before drain()
            microphone.close();
            logger.info("Audio recorder stopped successfully.");

        } else {
            logger.warn("The audio recorder was not running !");
        }
    }

    // _________________________________________________________________________

    /**
     * <b>Note</b>: The OutputStream is not closed.
     * 
     * @param out
     * @return The OutputStream coming from the microphone.
     * @throws IOException
     */
    public OutputStream stream(OutputStream out) throws IOException {

        AudioInputStream stream = new AudioInputStream(microphone);
        int bytesRead;
        byte[] data = new byte[BUFFER_SIZE];
        while ((bytesRead = stream.read(data, 0, BUFFER_SIZE)) != -1) {
            if (bytesRead > 0) {
                out.write(data, 0, bytesRead);
            }
        }
        return out;
    }

    // _________________________________________________________________________

    // XXX
    private void getInstalledMixers() {
        Mixer.Info[] availableMixers = AudioSystem.getMixerInfo();
        for (Mixer.Info mixerInfo : availableMixers) {

            System.out.println("Mixer info : " + mixerInfo);
            Mixer mixer = AudioSystem.getMixer(mixerInfo);
            if (mixer.isLineSupported(Port.Info.MICROPHONE))
                System.out.println(mixer + " is supported.");
            else
                System.out.println(mixer + " is not supported !");
            System.out.println("-------------");
        }
    }

    // _________________________________________________________________________

    /*
     * For testing purpose only ! XXX
     */
    public static void main(String[] args) throws
            LineUnavailableException, InterruptedException, IOException {

        final AudioRecorder audioRecoder = new AudioRecorder();

        audioRecoder.start();

        Thread t = new Thread("streamer") {
            public void run() {
                final File outputFile = new File("record.wav");
                BufferedOutputStream out = null;
                try {
                    out = new BufferedOutputStream(new FileOutputStream(outputFile));
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                }
                try {
                    audioRecoder.stream(out);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            };
        };
        t.start();

        // System.out.println("==================================");
        // audioRecoder.getInstalledMixers();

        TimeUnit.SECONDS.sleep(8);
        audioRecoder.stop();
    }
}
