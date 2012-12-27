package fr.byob.game.memeduel.core.sound;

import pythagoras.f.Vector;
import fr.byob.game.memeduel.core.GameSound;
import fr.byob.game.memeduel.core.MemeDuelLoader;
import fr.byob.game.memeduel.core.MemeDuelUtils;
import fr.byob.game.memeduel.core.model.Border;
import fr.byob.game.memeduel.core.model.cannon.CannonListener;
import fr.byob.game.memeduel.core.model.object.ModelObject;

public class TestSoundManager extends AbstractSoundManager implements CannonListener {

	private final static String DMG = "_DAMAGE";
	private final static String RM = "_EXPLODE";

	@Override
	public void cannonBallDamaged() {
		MemeDuelLoader.CANNON_BALL_MOVED.stop();
		MemeDuelLoader.CANNON_BALL_DAMAGE.play();
	}

	@Override
	public void cannonBallFired(final Vector position, final float angle) {
		MemeDuelLoader.CANNON_FIRE.play();
		MemeDuelLoader.CANNON_BALL_MOVED.play();
	}

	@Override
	public void cannonBallMoved(final Vector position, final float angle) {
	}

	@Override
	public void cannonBallBonusActivated(final Vector position, final float angle) {
		MemeDuelLoader.CANNON_BALL_BONUS.play();
	}

	@Override
	public void objectAdded(final ModelObject modelObject) {
	}

	@Override
	public void objectRemoved(final ModelObject modelObject) {
		if (MemeDuelUtils.isObjectNotFragment(modelObject)) {
			final GameSound sound = GameSound.valueOf(modelObject.getTypeId() + RM);
			if (sound != null) {
				this.playSound(modelObject, sound);
			}
		}
	}

	@Override
	public void objectDamaged(final ModelObject modelObject) {
		if (MemeDuelUtils.isObjectNotFragment(modelObject)) {
			final GameSound sound = GameSound.valueOf(modelObject.getTypeId() + DMG);
			if (sound != null) {
				this.playSound(modelObject, sound);
			}
		}
	}

	@Override
	public void objectHitBorder(final ModelObject modelObject, final Border border, final Vector position) {
		if (modelObject != null && border == Border.GROUND) {
			this.playSound(modelObject, MemeDuelLoader.SAND_POUF);
		}
	}

}
