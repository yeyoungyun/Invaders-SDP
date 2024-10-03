package engine;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Manages sound effects and BGM.
 *
 * @author <a href="mailto:dpdudyyy@gmail.com">Yun Yeyoung</a>
 *
 */

public class SoundManager {

    /** Save the sound file **/
    private HashMap<String, Clip> soundClips;

    /** Init the HashMap **/
    public SoundManager() {
        soundClips = new HashMap<>();
    }

    /** Load the sound file **/
    public void loadSound(String soundName, String filePath) {
        try {
            File soundFile = new File(filePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);

            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);

            soundClips.put(soundName, clip);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException | NullPointerException e) {
            System.out.println("Error loading sound: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /** Play the sound file **/
    public void playSound(String soundName) {
        Clip clip = soundClips.get(soundName);
        if (clip != null) {
            clip.setFramePosition(0);
            clip.start();
        } else {
            System.out.println("Sound not found: " + soundName);
        }
    }

    /** Stop the sound file **/
    public void stopSound(String soundName) {
        Clip clip = soundClips.get(soundName);  // HashMap에서 사운드 찾기
        if (clip != null && clip.isRunning()) {
            clip.stop();
        } else {
            System.out.println("Sound not playing or not found: " + soundName);
        }
    }

    /** Loop the sound file **/
    public void loopSound(String soundName) {
        Clip clip = soundClips.get(soundName);
        if (clip != null) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } else {
            System.out.println("Sound not found: " + soundName);
        }
    }

    /** Close the sound file **/
    public void closeSound(String soundName) {
        Clip clip = soundClips.get(soundName);
        if (clip != null) {
            clip.close();
        }
    }
}