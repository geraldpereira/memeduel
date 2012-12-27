package fr.byob.game.memeduel.core.god.damage;

import java.util.List;

import playn.core.Json.Object;
import fr.byob.game.memeduel.core.god.AllGODLoader;
import fr.byob.game.memeduel.core.model.ModelObjectExploder.ExplodeType;
import fr.byob.game.memeduel.core.model.handler.damage.DecayDamageHandler;

public class DecayDefinition extends LifespanDefinition {

	private final static Builder builder = new Builder();

	public static Builder decayBuilder() {
		builder.reset();
		return builder;
	}

	public static class Builder extends LifespanDefinition.Builder<DecayDefinition> {

		private float decayPercentage;
		private boolean immediateOrOnDamage;

		private Builder() {
		}

		@Override
		public void reset() {
			super.reset();
			decayPercentage = 0;
			immediateOrOnDamage = true;
		}

		public Builder decayPercentage(final float decayPercentage) {
			this.decayPercentage = decayPercentage;
			return this;
		}

		public Builder immediateOrOnDamage(final boolean immediateOrOnDamage) {
			this.immediateOrOnDamage = immediateOrOnDamage;
			return this;
		}

		@Override
		public Builder fromJSON(final AllGODLoader godLoader, final Object jsonEntity) {
			super.fromJSON(godLoader, jsonEntity);
			return this.decayPercentage(jsonEntity.getInt("decayPercentage")).immediateOrOnDamage(jsonEntity.getBoolean("immediateOrOnDamage"));
		}

		// Passe pas sous GWT et n'est pas utilisé
		// @Override
		// public Builder fromOther(final DecayDefinition other) {
		// super.fromOther(other);
		// return
		// this.decayPercentage(other.getDecayPercentage()).immediateOrOnDamage(other.getImmediateOrOnDamage());
		// }

		@Override
		public Builder fromOther(final LifespanDefinition other) {
			super.fromOther(other);
			return this;
		}

		@Override
		public DecayDefinition build() {
			return new DecayDefinition(lifespanFactor, minDamagePercentage, otherDamageFactor, damageSlices, decayPercentage, immediateOrOnDamage, explodable, explodeBlastFactor, explodeMotionFactor);
		}
	}

	private final float decayPercentage;
	private final boolean immediateOrOnDamage;

	protected DecayDefinition(final float lifespanFactor, final float minImpulse, final float damageFactor, final List<DamageSlice> damageSlices, final float decayPercentage, final boolean immediateOrOnDamage, final ExplodeType explodable, final float explodeBlastFactor,
			final float explodeMotionFactor) {
		super(lifespanFactor, minImpulse, damageFactor, damageSlices, explodable, explodeBlastFactor, explodeMotionFactor);
		this.decayPercentage = decayPercentage;
		this.immediateOrOnDamage = immediateOrOnDamage;
	}


	public float getDecayPercentage() {
		return this.decayPercentage;
	}

	public boolean getImmediateOrOnDamage() {
		return this.immediateOrOnDamage;
	}

	@Override
	public DecayDamageHandler newModelHandler() {
		return new DecayDamageHandler();
	}
}
