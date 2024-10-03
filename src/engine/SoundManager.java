package engine;

import javax.sound.sampled.*;
import java.io.*;
import java.util.*;
import java.util.logging.Logger;

/**
 * Manages sound effects and BGM.
 *
 * @author <a href="mailto:dpdudyyy@gmail.com">Yun Yeyoung</a>
 *
 */

public class SoundManager {

    /** Singleton instance of the class. */
    private static SoundManager instance;
    /** Save the sound file **/
    private static HashMap<String, Clip> soundClips;
    /** Application logger. */
    private static Logger logger;

    /**
     * Private constructor.
     */
    private SoundManager() {
        logger = Core.getLogger();
        logger.info("Started loading sound resources.");

        soundClips = new HashMap<>();

        try {
            loadSound("menuBack", "res/sound/SFX/menuBack.wav");
            loadSound("menuClick", "res/sound/SFX/menuClick.wav");
            loadSound("menuMove", "res/sound/SFX/menuMove.wav");
            loadSound("nameTyping", "res/sound/SFX/nameTyping.wav");

            logger.info("Finished loading all sounds.");
        } catch (IOException e) {
            logger.warning("Loading failed: IO Exception");
        } catch (UnsupportedAudioFileException e) {
            logger.warning("Loading failed: Unsupported audio file: ");
        } catch (LineUnavailableException e) {
            logger.warning("Loading failed: Line unavailable: ");
        }
    }

    /**
     * Returns shared instance of SoundManager.
     *
     * @return Shared instance of SoundManager.
     */
    protected static SoundManager getInstance() {
        if (instance == null)
            instance = new SoundManager();
        return instance;
    }

    /**
     * Load the sound and save the map.
     *
     * @param soundName Key value of sound
     * @param filePath Path of the sound file
     * @throws IOException,UnsupportedAudioFileException,LineUnavailableException exception
     */
    public void loadSound(String soundName, String filePath) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        File soundFile = new File(filePath);

        if (!soundFile.exists()) {
            throw new IOException("Sound file not found: " + filePath);
        }

        AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
        Clip clip = AudioSystem.getClip();
        clip.open(audioStream);

        soundClips.put(soundName, clip);
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

    /** Close all sound files **/
    public void closeAllSounds() {
        for (String soundName : soundClips.keySet()) {
            Clip clip = soundClips.get(soundName);
            if (clip != null) {
                clip.close();
            }
        }
    }

}