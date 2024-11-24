package engine;

/**
 * Implements an object that stores a single game's difficulty settings.
 */
public class GameSettings {

	// Constants for game settings
	private static final int MAX_FORMATION_WIDTH = 14;
	private static final int MAX_FORMATION_HEIGHT = 10;
	private static final int MIN_BASE_SPEED = -150;
	private static final int MIN_SHOOTING_FRECUENCY = 100;

	private int difficulty;         // Game difficulty
	private int formationWidth;     // Width of the enemy formation
	private int formationHeight;    // Height of the enemy formation
	private int baseSpeed;          // Base speed of the enemies
	private int shootingFrecuency;  // Shooting frequency of enemies

	/**
	 * Constructor for initializing game settings.
	 */
	public GameSettings(final int formationWidth, final int formationHeight,
						final int baseSpeed, final int shootingFrecuency) {
		this.formationWidth = formationWidth;
		this.formationHeight = formationHeight;
		this.baseSpeed = baseSpeed;
		this.shootingFrecuency = shootingFrecuency;
	}

	/**
	 * Copy constructor for game settings.
	 */
	public GameSettings(GameSettings gameSettings) {
		this(gameSettings.formationWidth, gameSettings.formationHeight,
				gameSettings.baseSpeed, gameSettings.shootingFrecuency);
	}

	/**
	 * Adjusts the settings based on level and difficulty.
	 *
	 * @param formationWidth Current formation width.
	 * @param formationHeight Current formation height.
	 * @param baseSpeed Current base speed.
	 * @param shootingFrecuency Current shooting frequency.
	 * @param level Current game level.
	 * @param difficulty Current game difficulty.
	 * @return Updated GameSettings object.
	 */
	public GameSettings LevelSettings(int formationWidth, int formationHeight,
									  int baseSpeed, int shootingFrecuency,
									  int level, int difficulty) {
		this.difficulty = difficulty;

		if (difficulty == 0) { // EASY
			if (level % 3 == 0 && level < 5) {
				updateFormationSize();
				baseSpeed = Math.max(MIN_BASE_SPEED, baseSpeed - 10);
				shootingFrecuency = Math.max(MIN_SHOOTING_FRECUENCY, shootingFrecuency - 100);
			} else if (level % 2 == 0 && level >= 5) {
				updateFormationSize();
				baseSpeed = Math.max(MIN_BASE_SPEED, baseSpeed - 10);
				shootingFrecuency = Math.max(MIN_SHOOTING_FRECUENCY, shootingFrecuency - 100);
			}
		} else if (difficulty == 1) { // NORMAL
			if (level % 2 == 0 && level < 5) {
				updateFormationSize();
				baseSpeed = Math.max(MIN_BASE_SPEED, baseSpeed - 10);
				shootingFrecuency = Math.max(MIN_SHOOTING_FRECUENCY, shootingFrecuency - 200);
			} else if (level >= 5) {
				updateFormationSize();
				baseSpeed = Math.max(MIN_BASE_SPEED, baseSpeed - 20);
				shootingFrecuency = Math.max(MIN_SHOOTING_FRECUENCY, shootingFrecuency - 300);
			}
		} else if (difficulty == 2) { // HARD
			if (level % 2 == 0 && level < 5) {
				updateFormationSize();
				baseSpeed = Math.max(MIN_BASE_SPEED, baseSpeed - 20);
				shootingFrecuency = Math.max(MIN_SHOOTING_FRECUENCY, shootingFrecuency - 300);
			} else if (level >= 5) {
				updateFormationSize();
				baseSpeed = Math.max(MIN_BASE_SPEED, baseSpeed - 20);
				shootingFrecuency = Math.max(MIN_SHOOTING_FRECUENCY, shootingFrecuency - 400);
			}
		}

		return new GameSettings(formationWidth, formationHeight, baseSpeed, shootingFrecuency);
	}

	/**
	 * Updates the size of the enemy formation.
	 */
	private void updateFormationSize() {
		if (formationWidth == formationHeight) {
			if (formationWidth < MAX_FORMATION_WIDTH) {
				formationWidth++;
			}
		} else {
			if (formationHeight < MAX_FORMATION_HEIGHT) {
				formationHeight++;
			}
		}
	}

	// Getters for game settings
	public int getFormationWidth() {
		return formationWidth;
	}

	public int getFormationHeight() {
		return formationHeight;
	}

	public int getBaseSpeed() {
		return baseSpeed;
	}

	public int getShootingFrecuency() {
		return shootingFrecuency;
	}

	public int getDifficulty() {
		return difficulty;
	}
}
