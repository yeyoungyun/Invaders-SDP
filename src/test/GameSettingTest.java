package test;

import engine.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class GameSettingsTest {

    @Test
    void testLevelSettings_EasyMode_Level0() {
        GameSettings settings = createSettings(5, 5, -100, 200);
        GameSettings updatedSettings = settings.LevelSettings(
                settings.getFormationWidth(), settings.getFormationHeight(),
                settings.getBaseSpeed(), settings.getShootingFrecuency(),
                2, 0
        );

        Assertions.assertEquals(5, updatedSettings.getFormationWidth(), "Formation width mismatch for Easy mode.");
        Assertions.assertEquals(200, updatedSettings.getShootingFrecuency(), "Shooting frequency mismatch for Easy mode.");
    }

    @Test
    void testLevelSettings_NormalMode_Level1() {
        GameSettings settings = createSettings(10, 10, -120, 400);
        GameSettings updatedSettings = settings.LevelSettings(
                settings.getFormationWidth(), settings.getFormationHeight(),
                settings.getBaseSpeed(), settings.getShootingFrecuency(),
                6, 1
        );

        Assertions.assertEquals(10, updatedSettings.getFormationHeight(), "Formation height mismatch for Normal mode.");
        Assertions.assertEquals(-140, updatedSettings.getBaseSpeed(), "Base speed mismatch for Normal mode.");
        Assertions.assertEquals(100, updatedSettings.getShootingFrecuency(), "Shooting frequency mismatch for Normal mode.");
    }

    @Test
    void testLevelSettings_HardMode_EdgeCase() {
        GameSettings settings = createSettings(14, 9, -150, 100);
        GameSettings updatedSettings = settings.LevelSettings(
                settings.getFormationWidth(), settings.getFormationHeight(),
                settings.getBaseSpeed(), settings.getShootingFrecuency(),
                8, 2
        );

        Assertions.assertEquals(14, updatedSettings.getFormationWidth(), "Formation width mismatch for Hard mode.");
        Assertions.assertEquals(-150, updatedSettings.getBaseSpeed(), "Base speed mismatch for Hard mode.");
        Assertions.assertEquals(100, updatedSettings.getShootingFrecuency(), "Shooting frequency mismatch for Hard mode.");
    }

    @Test
    void testLevelSettings_ExtremeValues() {
        GameSettings settings = createSettings(1, 1, -10, 1000);
        GameSettings updatedSettings = settings.LevelSettings(
                settings.getFormationWidth(), settings.getFormationHeight(),
                settings.getBaseSpeed(), settings.getShootingFrecuency(),
                10, 2
        );

        Assertions.assertEquals(1, updatedSettings.getFormationHeight(), "Formation height mismatch for extreme values.");
        Assertions.assertEquals(-30, updatedSettings.getBaseSpeed(), "Base speed mismatch for extreme values.");
        Assertions.assertEquals(600, updatedSettings.getShootingFrecuency(), "Shooting frequency mismatch for extreme values.");
    }

    // Factory method for creating GameSettings
    private GameSettings createSettings(int width, int height, int speed, int frequency) {
        return new GameSettings(width, height, speed, frequency);
    }
}
