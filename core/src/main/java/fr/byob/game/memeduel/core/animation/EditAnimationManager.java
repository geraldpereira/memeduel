package fr.byob.game.memeduel.core.animation;

import pythagoras.f.Vector;
import fr.byob.game.memeduel.core.GamePool;
import fr.byob.game.memeduel.core.god.AnimationGOD;
import fr.byob.game.memeduel.core.model.AbstractModel;
import fr.byob.game.memeduel.core.model.Border;
import fr.byob.game.memeduel.core.model.object.ModelObject;
import fr.byob.game.memeduel.core.view.View;

public class EditAnimationManager extends MemeDuelAnimationManager {

	public EditAnimationManager(final AbstractModel model, final View view) {
		super(model, view);
	}

	@Override
	public void objectAdded(final ModelObject modelObject) {

	}

	@Override
	public void objectRemoved(final ModelObject modelObject) {
		// createParticles(modelObject);
	}

	@Override
	public void objectDamaged(final ModelObject modelObject) {
	}

	@Override
	public void objectHitBorder(final ModelObject modelObject, final Border border, final Vector position) {
		if (border == Border.GROUND) {
			final Vector tmp = GamePool.instance().popVector();
			tmp.set(position.x, this.model.getGodLoader().getGameGOD().getWorld().getPosition().y);
			final AnimationGOD god = this.newSandParticleGOD(tmp);
			this.model.addObject(god);
			GamePool.instance().pushVector(1);
		}
	}
}
