package fr.byob.game.memeduel.core.model.handler.damage;

import fr.byob.game.memeduel.core.god.damage.DecayDefinition;

public class DecayDamageHandler extends LifespanDamageHandler {

	@Override
	public boolean update(final float delta) {
		final DecayDefinition decayDefiniton = (DecayDefinition) this.modelObject.getGOD().getDamageDefinition();
		if (decayDefiniton.getImmediateOrOnDamage() || this.isDamaged()) {
			final float damageAmount = this.initialLifespan * decayDefiniton.getDecayPercentage() * delta;
			return this.damage(damageAmount);
		}
		return false;
	};

}
