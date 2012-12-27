package fr.byob.game.memeduel.core.model.handler.damage;

import fr.byob.game.box2d.dynamics.Body;


public class RocketDamageHandler extends DecayDamageHandler {

	@Override
	public void init() {
		super.init();
		this.modelObject.getB2DBodyHandler().getBody().setFixedRotation(true);
	}

	@Override
	public boolean damage(final float damageAmount) {
		final boolean damaged = super.damage(damageAmount);
		// Lorsque la roquette est endomagée, on lui rend une rotation classique
		final Body body = this.modelObject.getB2DBodyHandler().getBody();
		if (body.isFixedRotation()) {
			body.setFixedRotation(false);
		}
		return damaged;
	}
}
