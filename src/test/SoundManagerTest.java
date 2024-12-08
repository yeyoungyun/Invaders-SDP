package test;

import engine.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SoundManagerTest {

    private final SoundManager soundManager = SoundManager.getInstance();

    @BeforeEach
    public void setUp() {
    }

    @Test
    public void testVolumeUp() {
        int initialVolume = soundManager.getBgmVolume();
        soundManager.volumeUp(true);
        assertFalse(soundManager.getBgmVolume() > initialVolume);
    }

    @Test
    public void testVolumeDown() {
        int initialVolume = soundManager.getBgmVolume();
        soundManager.volumeDown(true);
        assertTrue(soundManager.getBgmVolume() < initialVolume);
    }
}
