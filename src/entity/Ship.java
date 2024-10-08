package entity;

import java.awt.Color;
import java.util.Set;

import engine.Cooldown;
import engine.Core;
import engine.DrawManager.SpriteType;
import engine.Sound;
import engine.SoundManager;

/**
 * Implements a ship, to be controlled by the player.
 * 
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 * 
 */
public class Ship extends Entity {

	/** Time between shots. */
	private static int SHOOTING_INTERVAL = 750;
	/** Speed of the bullets shot by the ship. */
	private static int BULLET_SPEED = -6;
	/** Movement of the ship for each unit of time. */
	private static final int SPEED = 2;
	
	/** Minimum time between shots. */
	private Cooldown shootingCooldown;
	/** Time spent inactive between hits. */
	private Cooldown destructionCooldown;
	/** Singleton instance of SoundManager */
	private final SoundManager soundManager = SoundManager.getInstance();

	/**
	 * Constructor, establishes the ship's properties.
	 * 
	 * @param positionX
	 *            Initial position of the ship in the X axis.
	 * @param positionY
	 *            Initial position of the ship in the Y axis.
	 */
	public Ship(final int positionX, final int positionY) {
		super(positionX, positionY, 13 * 2, 8 * 2, Color.GREEN);

		this.spriteType = SpriteType.Ship;
		this.shootingCooldown = Core.getCooldown(SHOOTING_INTERVAL);
		this.destructionCooldown = Core.getCooldown(1000);
	}

	/**
	 * Moves the ship speed uni ts right, or until the right screen border is
	 * reached.
	 */
	public final void moveRight() {
		this.positionX += SPEED;
		soundManager.playSound(Sound.PLAYER_MOVE);
	}

	/**
	 * Moves the ship speed units left, or until the left screen border is
	 * reached.
	 */
	public final void moveLeft() {
		this.positionX -= SPEED;
		soundManager.playSound(Sound.PLAYER_MOVE);
	}

	/**
	 * Shoots a bullet upwards.
	 * 
	 * @param bullets
	 *            List of bullets on screen, to add the new bullet.
	 * @return Checks if the bullet was shot correctly.
	 */
	public final boolean shoot(final Set<Bullet> bullets) {
		if (this.shootingCooldown.checkFinished()) {
			this.shootingCooldown.reset();
			bullets.add(BulletPool.getBullet(positionX + this.width / 2,
					positionY, BULLET_SPEED));
			soundManager.playSound(Sound.PLAYER_LASER);
			return true;
		}
		return false;
	}

	/**
	 * Updates status of the ship.
	 */
	public final void update() {
		if (!this.destructionCooldown.checkFinished())
			this.spriteType = SpriteType.ShipDestroyed;
		else
			this.spriteType = SpriteType.Ship;
	}

	/**
	 * Switches the ship to its destroyed state.
	 */
	public final void destroy() {
		this.destructionCooldown.reset();
		soundManager.playSound(Sound.PLAYER_HIT);
	}

	/**
	 * Checks if the ship is destroyed.
	 * 
	 * @return True if the ship is currently destroyed.
	 */
	public final boolean isDestroyed() {
		return !this.destructionCooldown.checkFinished();
	}

	/**
	 * Getter for the ship's speed.
	 * 
	 * @return Speed of the ship.
	 */
	public final int getSpeed() {
		return SPEED;
	}

	public void applyItem(Wallet wallet){
		int bulletLv = wallet.getBullet_lv();
		switch (bulletLv){
			case 1:
				BULLET_SPEED = -6;
				break;
			case 2:
				BULLET_SPEED = -7;
				break;
			case 3:
				BULLET_SPEED = -9;
				break;
			case 4:
				BULLET_SPEED = -10;
				break;
			default:
				BULLET_SPEED = -6;
		}

		int intervalLv = wallet.getShot_lv();
		switch (intervalLv){
			case 1: //생성자에서 이미 초기화함
				break;
			case 2:
				SHOOTING_INTERVAL = 675;
				shootingCooldown = Core.getCooldown(SHOOTING_INTERVAL);
				break;
			case 3:
				SHOOTING_INTERVAL = 607;
				shootingCooldown = Core.getCooldown(SHOOTING_INTERVAL);
				break;
			case 4:
				SHOOTING_INTERVAL = 546;
				shootingCooldown = Core.getCooldown(SHOOTING_INTERVAL);
				break;
			default:
				SHOOTING_INTERVAL = 750;
				shootingCooldown = Core.getCooldown(SHOOTING_INTERVAL);
		}
	}
}
