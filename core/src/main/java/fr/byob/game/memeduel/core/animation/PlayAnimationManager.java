package fr.byob.game.memeduel.core.animation;

import pythagoras.f.Vector;
import fr.byob.game.memeduel.core.GamePool;
import fr.byob.game.memeduel.core.MemeDuelUtils;
import fr.byob.game.memeduel.core.god.AnimationGOD;
import fr.byob.game.memeduel.core.model.AbstractModel;
import fr.byob.game.memeduel.core.model.cannon.CannonListener;
import fr.byob.game.memeduel.core.model.object.ModelObject;
import fr.byob.game.memeduel.core.view.View;

public class PlayAnimationManager extends EditAnimationManager implements CannonListener {

	public PlayAnimationManager(final AbstractModel model, final View view) {
		super(model, view);
	}

	@Override
	public void cannonBallDamaged() {
	}

	@Override
	public void cannonBallFired(final Vector position, final float angle) {
		final AnimationGOD god = this.newCannonMuzzleFlashGOD(position, angle);
		this.model.addObject(god);
	}

	@Override
	public void cannonBallMoved(final Vector position, final float angle) {

	}

	@Override
	public void cannonBallBonusActivated(final Vector position, final float angle) {
	}

	@Override
	public void objectRemoved(final ModelObject modelObject) {
		if (MemeDuelUtils.isMeme(modelObject) || MemeDuelUtils.isCannonBall(modelObject)) {
			final Vector position = GamePool.instance().popVector();
			modelObject.getUpdateHandler().getCurrentPositionToOut(position);
			this.model.addObject(this.newSmokeExplosionParticleGOD(position));
			GamePool.instance().pushVector(1);
		}
	}

}
