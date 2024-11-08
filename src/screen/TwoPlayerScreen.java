package screen;

import engine.Core;
import engine.GameSettings;
import engine.GameState;
import engine.SoundManager;
import engine.Sound;
import entity.Wallet;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
/**
 * Implements the Two player mode screen, where the action happens.
 */
public class TwoPlayerScreen extends Screen {
    /** Thread pool executor **/
    private final ExecutorService executor;
    /** Game difficulty settings each player **/
    private final GameSettings[] gameSettings = new GameSettings[2];
    /** Current game wallet **/
    private final Wallet wallet;

    /** Game states for each player **/
    private final GameState[] gameStates = new GameState[2];

    /** Players game task **/
    private final Future<GameState>[] players = new Future[2];

    /** Player game finished flags **/
    private static final boolean[] gameFinished = new boolean[2];

    private boolean[] levelProgressionLocked = new boolean[2];
    private boolean gameRunning = true;

    /** Method to handle waiting until both players have cleared the level or one has lost. **/
    private synchronized void checkLevelCompletion() {
        if (gameFinished[PLAYER1_NUMBER] && gameFinished[PLAYER2_NUMBER]) {
            // 모든 플레이어가 완료되었으면 잠금을 해제하고 다음 레벨로 이동 준비
            levelProgressionLocked[PLAYER1_NUMBER] = false;
            levelProgressionLocked[PLAYER2_NUMBER] = false;
            // 다음 레벨로 이동을 위해 각 플레이어의 gameFinished 상태 초기화
            gameFinished[PLAYER1_NUMBER] = false;
            gameFinished[PLAYER2_NUMBER] = false;
            // 각 플레이어의 다음 레벨로 이동
            gameStates[PLAYER1_NUMBER] = new GameState(gameStates[PLAYER1_NUMBER], gameStates[PLAYER1_NUMBER].getLevel() + 1);
            gameStates[PLAYER2_NUMBER] = new GameState(gameStates[PLAYER2_NUMBER], gameStates[PLAYER2_NUMBER].getLevel() + 1);

            runGameScreen(PLAYER1_NUMBER);
            runGameScreen(PLAYER2_NUMBER);
        }
    }

    /** Player 1's number**/
    private static final int PLAYER1_NUMBER = 0;
    /** Player 2's number**/
    private static final int PLAYER2_NUMBER = 1;

    /**
     * Constructor, establishes the properties of the screen.
     *
     *
     * @param gameState
     *            Initial game state
     * @param gameSettings
     *            Game settings list.
     * @param width
     *            Screen width.
     * @param height
     *            Screen height.
     * @param fps
     *            Frames per second, frame rate at which the game is run.
     * @param wallet
     *            Wallet for each game.
     */
    public TwoPlayerScreen(final GameState gameState, final GameSettings gameSettings,
                           final int width, final int height, final int fps, Wallet wallet) {
        super(width * 2, height, fps * 2);

        for (int playerNumber = 0; playerNumber < 2; playerNumber++) {
            this.gameSettings[playerNumber] = new GameSettings(gameSettings);
            this.gameStates[playerNumber] = new GameState(gameState);
            gameFinished[playerNumber] = false;
        }

        this.wallet = wallet;
        executor = Executors.newFixedThreadPool(2);
        this.returnCode = 1;
    }

    /**
     * Starts the action.
     *
     * @return Next screen code.
     */
    public int run(){
        try {
            SoundManager.getInstance().loopSound(Sound.BGM);
            runGameScreen(PLAYER1_NUMBER);
            runGameScreen(PLAYER2_NUMBER);
        }
        catch (Exception e) {
            // TODO handle exception
            e.printStackTrace();
        }
        super.run();
        runGameLoop();
        return returnCode;
    }

    /**
     * Draws the elements associated with the screen.
     */
    private void draw() {
        drawManager.initDrawing(this);
        drawManager.mergeDrawing(this);
        drawManager.drawVerticalLine(this);
        drawManager.completeDrawing(this);
    }

    /**
     * Updates the elements on screen and checks for events.
     */
    protected final void update() {
        try {
            updatePlayerStatus();
            checkLevelCompletion();  // 두 플레이어의 상태에 따라 레벨 이동 확인

            if (gameRunning) {  // 게임이 실행 중일 때만 업데이트 수행
                // 각 플레이어의 상태에 따라 개별적으로 잠금 확인 및 완료 처리
                if (!levelProgressionLocked[PLAYER1_NUMBER] && players[PLAYER1_NUMBER].isDone()) {
                    gameFinished[PLAYER1_NUMBER] = true;
                }

                if (!levelProgressionLocked[PLAYER2_NUMBER] && players[PLAYER2_NUMBER].isDone()) {
                    gameFinished[PLAYER2_NUMBER] = true;
                }

                draw();
            } else {
                // 두 플레이어가 모두 완료된 경우, 리소스 정리 및 사운드 중지
                executor.shutdown();
                SoundManager.getInstance().stopSound(Sound.BGM);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Progression logic each games.
     */
    private void runGameScreen(int playerNumber){
        GameState gameState = playerNumber == 0 ? gameStates[PLAYER1_NUMBER] : gameStates[PLAYER2_NUMBER];

        if (gameState.getLivesRemaining() > 0) {
            boolean bonusLife = gameState.getLevel()
                    % Core.EXTRA_LIFE_FRECUENCY == 0
                    && gameState.getLivesRemaining() < Core.MAX_LIVES;
            logger.info("difficulty is " + Core.getLevelSetting());
            gameSettings[playerNumber] = gameSettings[playerNumber].LevelSettings(
                    gameSettings[playerNumber].getFormationWidth(),
                    gameSettings[playerNumber].getFormationHeight(),
                    gameSettings[playerNumber].getBaseSpeed(),
                    gameSettings[playerNumber].getShootingFrecuency(),
                    gameState.getLevel(),
                    Core.getLevelSetting()
            );
            GameScreen gameScreen = new GameScreen(gameState, gameSettings[playerNumber],
                    bonusLife, width / 2, height, fps / 2, wallet, playerNumber);
            gameScreen.initialize();
            players[playerNumber] = executor.submit(gameScreen);
        }
        else gameFinished[playerNumber] = true;
    }

    public GameState getWinnerGameState() {
        return gameStates[getWinnerNumber() - 1];
    }

    public int getWinnerNumber() {
        return ((gameStates[PLAYER1_NUMBER].getScore() >= gameStates[PLAYER2_NUMBER].getScore()) ? PLAYER1_NUMBER : PLAYER2_NUMBER) + 1;
    }

    public static boolean isSinglePlayerActive() {
        return (gameFinished[PLAYER1_NUMBER] && !gameFinished[PLAYER2_NUMBER]) ||
                (!gameFinished[PLAYER1_NUMBER] && gameFinished[PLAYER2_NUMBER]);
    }

    public void runGameLoop() {
        while (gameRunning) {
            update();
            try {
                Thread.sleep(60);  // 간단한 대기 시간을 추가하여 과부하 방지
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void updatePlayerStatus() {
        if (players[PLAYER1_NUMBER] != null && players[PLAYER1_NUMBER].isDone()) {
            gameFinished[PLAYER1_NUMBER] = true;
            checkLevelCompletion();
        }
        if (players[PLAYER2_NUMBER] != null && players[PLAYER2_NUMBER].isDone()) {
            gameFinished[PLAYER2_NUMBER] = true;
            checkLevelCompletion();
        }
    }
}
