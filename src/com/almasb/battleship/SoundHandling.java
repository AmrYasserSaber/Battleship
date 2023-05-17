package com.almasb.battleship;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class SoundHandling {
    public static Void play(String path,int numberOfTimes){
        try {
            // Load sound effect
            URL soundURL = SoundHandling.class.getResource(path);
            if (soundURL == null) {
                throw new IllegalArgumentException("Sound file not found.");
            }

            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundURL);

            // Get the audio format
            AudioFormat audioFormat = audioInputStream.getFormat();

            // Convert to a compatible format if needed
            if (audioFormat.getSampleSizeInBits() != 16 || audioFormat.getChannels() != 2 || audioFormat.getSampleRate() != 44100) {
                AudioFormat compatibleFormat = new AudioFormat(
                        AudioFormat.Encoding.PCM_SIGNED,
                        44100,
                        16,
                        2,
                        4,
                        44100,
                        false
                );
                audioInputStream = AudioSystem.getAudioInputStream(compatibleFormat, audioInputStream);
            }

            // Create clip
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);

            // Add a LineListener to perform cleanup operations when sound playback is complete
            AudioInputStream finalAudioInputStream = audioInputStream;
            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    clip.stop();
                    clip.close();
                    try {
                        finalAudioInputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            // Start playing the sound effect
            clip.start();
            if (numberOfTimes == 0){
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            }else {
                clip.loop(numberOfTimes - 1);
            }
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
