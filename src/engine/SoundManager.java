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
    /** Application logger. */
    private static Logger logger;
    /** Sound manager activation flag */
    private boolean soundEnabled;
    /** Value of current volume */
    public static int currentBgmVolume = 10;
    public static int currentSfxVolume = 10;
    /** Maximum and minimum values of volume */
    private final float MIN_VOL = -80.0f;
    private final float MAX_VOL = 6.0f;
    /** Current playing BGM */
    private Sound currentBGM;

    /** Save the sound file **/
    private final Map<Sound, Object> soundClips = new HashMap<>();

    private static final Set<Sound> BGM_SOUNDS = Set.of(
            Sound.BGM_LV1, Sound.BGM_LV2, Sound.BGM_LV3, Sound.BGM_LV4, Sound.BGM_LV5, Sound.BGM_LV6,
            Sound.BGM_LV7, Sound.BGM_GAMEOVER, Sound.BGM_MAIN, Sound.BGM_SHOP
    );
    
    private static final Set<Sound> POSITIONAL_SOUNDS = Set.of(
            Sound.ALIEN_HIT, Sound.ALIEN_LASER, Sound.PLAYER_HIT, Sound.PLAYER_MOVE, Sound.PLAYER_LASER, Sound.ITEM_BOMB,
            Sound.ITEM_SPAWN, Sound.ITEM_BARRIER_ON, Sound.ITEM_BARRIER_OFF, Sound.ITEM_TIMESTOP_ON, Sound.ITEM_TIMESTOP_OFF,
            Sound.ITEM_2SHOT, Sound.ITEM_3SHOT, Sound.ITEM_GHOST, Sound.BULLET_BLOCKING, Sound.UFO_APPEAR
    );

    /**
     * Private constructor.
     */
    private SoundManager() {
        logger = Core.getLogger();
        logger.info("Started loading sound resources.");

        soundEnabled = false;
        try {
            preloadSounds();

            setVolume(currentBgmVolume, true);
            setVolume(currentSfxVolume, false);

            logger.info("Finished loading all sounds.");
            soundEnabled = true;

        } catch (IOException e) {
            logger.warning("Loading failed: IO Exception");
        } catch (UnsupportedAudioFileException e) {
            logger.warning("Loading failed: Unsupported audio file.");
        } catch (LineUnavailableException | IllegalArgumentException e) {
            logger.warning("Loading failed: Sound device not found.");
        }
    }

    /**
     * Returns shared instance of SoundManager.
     *
     * @return Shared instance of SoundManager.
     */
    public static SoundManager getInstance() {
        if (instance == null)
            instance = new SoundManager();
        return instance;
    }

    /**
     * Preload the sound and save the map.
     *
     * @throws IOException,UnsupportedAudioFileException,LineUnavailableException,IllegalArgumentException exception
     */
    private void preloadSounds() throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        for (Sound sound : Sound.values()) {
            if (POSITIONAL_SOUNDS.contains(sound)) {
                List<Clip> clips = new ArrayList<>();
                clips.add(loadClip(sound));
                clips.add(loadClip(sound));
                soundClips.put(sound, clips);
            } else {
                soundClips.put(sound, loadClip(sound));
            }
        }
    }

    /**
     * Load a sound clip individually.
     *
     * @throws IOException,UnsupportedAudioFileException,LineUnavailableException,IllegalArgumentException exception
     */
    private Clip loadClip(Sound sound) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        File soundFile = new File(sound.getFilePath());
        if (!soundFile.exists()) {
            throw new IOException("Sound file not found: " + sound.getFilePath());
        }
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
        Clip clip = AudioSystem.getClip();
        clip.open(audioStream);
        return clip;
    }

    /**
     * Apply volume to all audio files by converting integer volume to decibels non-linearly.
     *
     * @param volume Int value of volume (0-10)
     */
    private void setVolume(int volume, boolean isBGM) {
        float newVolume = MIN_VOL + (float)(Math.log(volume + 1) / Math.log(11)) * (MAX_VOL - MIN_VOL);

        if (isBGM) {
            for (Map.Entry<Sound, Object> entry : soundClips.entrySet()) {
                if (BGM_SOUNDS.contains(entry.getKey())) {
                    Clip clip = (Clip) entry.getValue();
                    setVolumeForClip(clip, newVolume);
                }
            }
        }
        else {
            for (Map.Entry<Sound, Object> entry : soundClips.entrySet()) {
                if (!BGM_SOUNDS.contains(entry.getKey())) {
                    Object soundObject = entry.getValue();
                    if (soundObject instanceof List) {
                        List<Clip> clips = (List<Clip>) soundObject;
                        for (Clip clip : clips) {
                            setVolumeForClip(clip, newVolume);
                        }
                    } else if (soundObject instanceof Clip clip) {
                        setVolumeForClip(clip, newVolume);
                    }
                }
            }
        }
    }

    private void setVolumeForClip(Clip clip, float newVolume) {
        try {
            FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            volumeControl.setValue(newVolume);
        } catch (IllegalArgumentException e) {
            logger.warning("Failed to set volume: " + e.getMessage());
        }
    }

    /**
     * @return current volume
     * */
    public int getBgmVolume() { return currentBgmVolume; }
    public int getSfxVolume() { return currentSfxVolume; }

    /**
     * @return current playing BGM
     * */
    public Sound getCurrentBGM() { return currentBGM; }

    /**
     * Increases the volume of all sounds by 1.
     */
    public void volumeUp(boolean isBGM) {
        if (soundEnabled) {
            if (isBGM) {
                if (currentBgmVolume < 10) {
                    currentBgmVolume++;
                    setVolume(currentBgmVolume, true);
                }
            } else {
                if (currentSfxVolume < 10) {
                    currentSfxVolume++;
                    setVolume(currentSfxVolume, false);
                }
            }
        }
    }

    /**
     * Decreases the volume of all sounds by 1.
     */
    public void volumeDown(boolean isBGM) {
        if (soundEnabled) {
            if (isBGM) {
                if (currentBgmVolume > 0) {
                    currentBgmVolume--;
                    setVolume(currentBgmVolume, true);
                }
            } else {
                if (currentSfxVolume > 0) {
                    currentSfxVolume--;
                    setVolume(currentSfxVolume, false);
                }
            }
        }
    }

    /**
     * Play the sound file.
     *
     * @param sound Key value of sound
     */
    public void playSound(Sound sound) {
        if (!soundEnabled) return;
        Object soundObject = soundClips.get(sound);
        if (soundObject != null) {
            if (soundObject instanceof List) {
                List<Clip> clips = (List<Clip>) soundObject;
                if (!clips.isEmpty()) {
                    Clip clip = clips.removeFirst();
                    clip.setFramePosition(0);
                    setVolumeBalance(clip, 0.0f, sound);
                    clip.start();
                    clips.add(clip);
                }
            } else if (soundObject instanceof Clip clip) {
                clip.setFramePosition(0);
                clip.start();
            }
        } else {
            logger.warning("Sound not found: " + sound);
        }
    }

    /**
     * Play the sound file.
     *
     * @param sound Key value of sound
     * @param balance Balance value (-1.0 for left, 1.0 for right, 0.0 for center)
     */
    public void playSound(Sound sound, float balance) {
        if (!soundEnabled) return;
        List<Clip> clips = (List<Clip>) soundClips.get(sound);
        if (clips != null) {
            Clip availableClip = clips.stream()
                    .filter(clip -> !clip.isRunning())
                    .findFirst()
                    .orElse(null);

            if (availableClip != null) {
                availableClip.setFramePosition(0);
                try {
                    if (POSITIONAL_SOUNDS.contains(sound)) {
                        setVolumeBalance(availableClip, balance, sound);
                    }
                    availableClip.start();
                } catch (Exception e) {
                    logger.warning("Error playing sound: " + sound + ". Error: " + e.getMessage());
                    e.printStackTrace();
                }
            } else {
                logger.warning("No available clips in pool for sound: " + sound);
            }
        } else {
            logger.warning("Sound not found: " + sound);
        }
    }

    private void setVolumeBalance(Clip clip, float balance, Sound sound) {
        try {
            if (clip.isControlSupported(FloatControl.Type.BALANCE)) {
                FloatControl balanceControl = (FloatControl) clip.getControl(FloatControl.Type.BALANCE);
                balanceControl.setValue(balance);
            } else if (clip.isControlSupported(FloatControl.Type.PAN)) {
                FloatControl panControl = (FloatControl) clip.getControl(FloatControl.Type.PAN);
                panControl.setValue(balance);
            } else {
                logger.info("No supported balance control. Playing in center for sound: " + sound);
            }
        } catch (Exception e) {
            logger.warning("Failed to set balance for sound: " + sound + ". Error: " + e.getMessage());
        }
    }

    /**
     * Stop the sound file.
     *
     * @param sound Key value of sound
     */
    public void stopSound(Sound sound) {
        if (!soundEnabled) return;
        Object soundObject = soundClips.get(sound);
        if (soundObject != null) {
            if (soundObject instanceof List) {
                List<Clip> clips = (List<Clip>) soundObject;
                clips.forEach(clip -> {
                    if (clip.isRunning()) {
                        clip.stop();
                    }
                });
            } else if (soundObject instanceof Clip clip) {
                if (clip.isRunning()) {
                    clip.stop();
                }
            }
        } else {
            logger.warning("Sound not playing or not found: " + sound);
        }
    }

    /**
     * Checks if the specified BGM is currently playing.
     *
     * @param sound Key value of the sound to check.
     * @return true if a BGM is playing, false otherwise.
     */
    public boolean isBGMPlaying(Sound sound) {
        if (soundEnabled) {
            Clip clip = (Clip) soundClips.get(sound);
            if (clip != null) {
                return clip.isRunning();
            } else {
                logger.warning("Sound not found: " + sound);
            }
        }
        return false;
    }

    /**
     * Loop the sound file.
     *
     * @param sound Key value of sound
     */
    public void loopSound(Sound sound) {
        if (!soundEnabled) return;

        Clip clip = (Clip) soundClips.get(sound);
        if (clip != null) {
            currentBGM = sound;
            clip.setFramePosition(0);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } else {
            logger.warning("Sound not found: " + sound);
        }
    }

    /** Stop and close all sound files **/
    public void closeAllSounds() {
        if (!soundEnabled) return;

        for (Object soundObject : soundClips.values()) {
            if (soundObject instanceof List) {
                List<Clip> clips = (List<Clip>) soundObject;
                for (Clip clip : clips) {
                    if (clip != null) {
                        if (clip.isRunning()) {
                            clip.stop();
                        }
                        clip.close();
                    }
                }
            } else if (soundObject instanceof Clip clip) {
                if (clip.isRunning()) {
                    clip.stop();
                }
                clip.close();
            }
        }
    }

}
