package fr.byob.game.memeduel.core.model.handler.damage;

import fr.byob.game.memeduel.core.GameImage;
import fr.byob.game.memeduel.core.god.damage.DurationDefinition;
import fr.byob.game.memeduel.core.model.handler.AbstractModelHandler;
import fr.byob.game.memeduel.core.model.object.DefaultModelObject;

public class DurationDamageHandler extends AbstractModelHandler<DefaultModelObject> implements DamageHandler {

	private float durationLeft;

	public DurationDamageHandler() {
	}


	@Override
	public void init() {
		final DurationDefinition durationDefinition = (DurationDefinition) this.modelObject.getGOD().getDamageDefinition();
		this.durationLeft = durationDefinition.getDuration();
	}

	@Override
	public boolean update(final float delta) {
		final float deltaTime = 1 / delta;
		this.durationLeft -= deltaTime;
		return false;
	}

	@Override
	public boolean isDestroyed() {
		return this.durationLeft <= 0;
	}

	@Override
	public boolean isDamaged() {
		final DurationDefinition durationDefinition = (DurationDefinition) this.modelObject.getGOD().getDamageDefinition();
		return this.durationLeft != durationDefinition.getDuration();
	}

	@Override
	public void destroy() {
		this.durationLeft = 0;
	}

	@Override
	public boolean damage(final float damageAmount) {
		return false;
	}

	@Override
	public GameImage getDamageMDImage() {
		return null;
	}
}
