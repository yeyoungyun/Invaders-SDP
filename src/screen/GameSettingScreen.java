package screen;

import engine.Cooldown;
import engine.Core;
import engine.InputManager;
import engine.SoundManager;

import java.awt.event.KeyEvent;

/**
 * Implements the game setting screen.
 * 
 * @author <a href="mailto:dayeon.dev@gmail.com">Dayeon Oh</a>
 * 
 */
public class GameSettingScreen extends Screen {

	/** Milliseconds between changes in user selection. */
	private static final int SELECTION_TIME = 200;
	/** Maximum number of characters for player name. */
	private static final int NAME_LIMIT = 6;


	/** Player name1 for record input. */
	private String name1;
	/** Player name2 for record input. */
	private String name2;
	/** Multiplayer mode. */
	private boolean isMultiplayer;
	/** Difficulty level. */
	private int difficultyLevel;
	/** Selected row. */
	private int selectedRow;
	/** Time between changes in user selection. */
	private final Cooldown selectionCooldown;
	/** SoundManager instance manages sound effects and BGM. **/
	private SoundManager soundManager;

	/** Total number of rows for selection. */
	private static final int TOTAL_ROWS = 3; // Multiplayer, Difficulty, Start

	/**
	 * Constructor, establishes the properties of the screen.
	 *
	 * @param width
	 *            Screen width.
	 * @param height
	 *            Screen height.
	 * @param fps
	 *            Frames per second, frame rate at which the game is run.
	 */
	public GameSettingScreen(final int width, final int height, final int fps) {
		super(width, height, fps);

		// row 0: multiplayer
		this.name1 = "P1";
		this.name2 = "P2";
		this.isMultiplayer = false;

		// row 1: difficulty level
		this.difficultyLevel = 1; 	// 0: easy, 1: normal, 2: hard

		// row 3: start

		this.selectedRow = 0;

		this.selectionCooldown = Core.getCooldown(SELECTION_TIME);
		this.selectionCooldown.reset();

		// SoundManager init and load sounds
		this.soundManager = new SoundManager();
		this.soundManager.loadSound("menuMove", "res/sound/SFX/menuMove.wav");
		this.soundManager.loadSound("menuClick", "res/sound/SFX/menuClick.wav");
		this.soundManager.loadSound("menuBack", "res/sound/SFX/menuBack.wav");
		this.soundManager.loadSound("nameTyping", "res/sound/SFX/nameTyping.wav");
	}

	/**
	 * Starts the action.
	 * 
	 * @return Next screen code.
	 */
	public final int run() {
		super.run();

		return this.returnCode;
	}

	/**
	 * Updates the elements on screen and checks for events.
	 */
	protected final void update() {
		super.update();

		draw();
		if (this.inputDelay.checkFinished() && this.selectionCooldown.checkFinished()) {
			if (inputManager.isKeyDown(KeyEvent.VK_UP)){
				this.selectedRow = (this.selectedRow - 1 + TOTAL_ROWS) % TOTAL_ROWS;
				this.selectionCooldown.reset();
				soundManager.playSound("menuMove");
			} else if (inputManager.isKeyDown(KeyEvent.VK_DOWN)) {
				this.selectedRow = (this.selectedRow + 1) % TOTAL_ROWS;
				this.selectionCooldown.reset();
				soundManager.playSound("menuMove");
			}

			if (this.selectedRow == 0) {
				if (inputManager.isKeyDown(KeyEvent.VK_LEFT)) {
					this.isMultiplayer = false;
					this.selectionCooldown.reset();
					soundManager.playSound("menuMove");
				} else if (inputManager.isKeyDown(KeyEvent.VK_RIGHT)) {
					this.isMultiplayer = true;
					this.selectionCooldown.reset();
					soundManager.playSound("menuMove");
				} else if (inputManager.isKeyDown(KeyEvent.VK_BACK_SPACE)) {
					if (isMultiplayer) {
						if (!this.name2.isEmpty()) {
							this.name2 = this.name2.substring(0, this.name2.length() - 1);
							this.selectionCooldown.reset();
							soundManager.playSound("nameTyping");
						}
					} else {
						if (!this.name1.isEmpty()) {
							this.name1 = this.name1.substring(0, this.name1.length() - 1);
							this.selectionCooldown.reset();
							soundManager.playSound("nameTyping");
						}
					}
				}
				handleNameInput(inputManager);
			} else if (this.selectedRow == 1) {
				if (inputManager.isKeyDown(KeyEvent.VK_LEFT)) {
					if (this.difficultyLevel != 0) {
						this.difficultyLevel--;
						this.selectionCooldown.reset();
						soundManager.playSound("menuMove");
					}
				} else if (inputManager.isKeyDown(KeyEvent.VK_RIGHT)) {
					if (this.difficultyLevel != 2) {
						this.difficultyLevel++;
						this.selectionCooldown.reset();
						soundManager.playSound("menuMove");
					}
				}
			} else if (this.selectedRow == 2) {
				if (inputManager.isKeyDown(KeyEvent.VK_SPACE)) {
					this.returnCode = 2;
					this.isRunning = false;
					soundManager.playSound("menuClick");
				}
			}
			if (inputManager.isKeyDown(KeyEvent.VK_ESCAPE)) {
				// Return to main menu.
				this.returnCode = 1;
				this.isRunning = false;
				soundManager.playSound("menuBack");
			}
		}

	}

	/**
	 * Handles the input for player name.
	 *
	 * @param inputManager
	 *            Input manager.
	 */
	private void handleNameInput(InputManager inputManager) {
		for (int keyCode = KeyEvent.VK_A; keyCode <= KeyEvent.VK_Z; keyCode++) {
			if (inputManager.isKeyDown(keyCode)) {
				if (isMultiplayer) {
					if (this.name2.length() < NAME_LIMIT) {
						this.name2 += (char) keyCode;
						this.selectionCooldown.reset();
						soundManager.playSound("nameTyping");
					}
				} else{
					if (this.name1.length() < NAME_LIMIT) {
						this.name1 += (char) keyCode;
						this.selectionCooldown.reset();
						soundManager.playSound("nameTyping");
					}
				}
			}
		}
	}

	/**
	 * Draws the elements associated with the screen.
	 */
	private void draw() {
		drawManager.initDrawing(this);

		drawManager.drawGameSetting(this);

		drawManager.drawGameSettingRow(this, this.selectedRow);

		drawManager.drawGameSettingElements(this, this.selectedRow, this.isMultiplayer, this.name1, this.name2,this.difficultyLevel);

		drawManager.completeDrawing(this);
	}
}
