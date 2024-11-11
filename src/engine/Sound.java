package engine;

/**
 * Enum representing the different sounds used in the game.
 *
 * @author <a href="mailto:dpdudyyy@gmail.com">Yun Yeyoung</a>
 *
 */

public enum Sound {
    MENU_BACK("res/sound/SFX/menuBack.wav"),
    MENU_CLICK("res/sound/SFX/menuClick.wav"),
    MENU_MOVE("res/sound/SFX/menuMove.wav"),
    MENU_TYPING("res/sound/SFX/nameTyping.wav"),
    COUNTDOWN("res/sound/SFX/countdown.wav"),
    ALIEN_HIT("res/sound/SFX/alienHit.wav"),
    ALIEN_LASER("res/sound/SFX/alienLaser.wav"),
    PLAYER_HIT("res/sound/SFX/playerHit.wav"),
    PLAYER_LASER("res/sound/SFX/playerLaser.wav"),
    PLAYER_MOVE("res/sound/SFX/playerMove.wav"),
    COIN_USE("res/sound/SFX/coinUse.wav"),
    COIN_INSUFFICIENT("res/sound/SFX/coinInsufficient.wav"),
    GAME_END("res/sound/SFX/gameEnd.wav"),
    UFO_APPEAR("res/sound/SFX/ufoAppear.wav"),
    ITEM_2SHOT("res/sound/SFX/item_2shot.wav"),
    ITEM_3SHOT("res/sound/SFX/item_3shot.wav"),
    ITEM_BARRIER_ON("res/sound/SFX/item_barrierOn.wav"),
    ITEM_BARRIER_OFF("res/sound/SFX/item_barrierOff.wav"),
    ITEM_BOMB("res/sound/SFX/item_bomb.wav"),
    ITEM_GHOST("res/sound/SFX/item_ghost.wav"),
    ITEM_SPAWN("res/sound/SFX/item_spawn.wav"),
    ITEM_TIMESTOP_ON("res/sound/SFX/item_timestopOn.wav"),
    ITEM_TIMESTOP_OFF("res/sound/SFX/item_timestopOff.wav"),
    BULLET_BLOCKING("res/sound/SFX/bulletBlocking.wav"),
    BGM_MAIN("res/sound/BGM/MainTheme.wav"),
    BGM_GAMEOVER("res/sound/BGM/GameOver.wav"),
    BGM_SHOP("res/sound/BGM/Shop.wav"),
    BGM_LV1("res/sound/BGM/Lv1.wav"),
    BGM_LV2("res/sound/BGM/Lv2.wav"),
    BGM_LV3("res/sound/BGM/Lv3.wav"),
    BGM_LV4("res/sound/BGM/Lv4.wav"),
    BGM_LV5("res/sound/BGM/Lv5.wav"),
    BGM_LV6("res/sound/BGM/Lv6.wav"),
    BGM_LV7("res/sound/BGM/Lv7.wav");

    private final String filePath;

    Sound(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }
}
