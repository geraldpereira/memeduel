package fr.byob.game.memeduel.core.god.damage;

import playn.core.Json.Object;
import fr.byob.game.memeduel.core.god.AllGODLoader;
import fr.byob.game.memeduel.core.model.ModelObjectExploder.ExplodeType;
import fr.byob.game.memeduel.core.model.handler.damage.DurationDamageHandler;


public class DurationDefinition extends DamageDefinition {

	private final static Builder builder = new Builder();

	public static Builder builder() {
		builder.reset();
		return builder;
	}

	public static class Builder extends DamageDefinition.Builder<DurationDefinition> {

		private float duration;

		private Builder() {
		}

		@Override
		public void reset() {
			super.reset();
			duration = 0;
		}

		public Builder duration(final float duration) {
			this.duration = duration;
			return this;
		}

		@Override
		public Builder fromJSON(final AllGODLoader godLoader, final Object jsonEntity) {
			super.fromJSON(godLoader, jsonEntity);
			return this.duration(jsonEntity.getInt("duration"));
		}

		@Override
		public Builder fromOther(final DurationDefinition other) {
			return this.duration(other.getDuration());
		}

		@Override
		public DurationDefinition build() {
			return new DurationDefinition(this.duration, explodable, explodeBlastFactor, explodeMotionFactor);
		}
	}

	private final float duration;

	protected DurationDefinition(final float duration, final ExplodeType explodable, final float explodeBlastFactor, final float explodeMotionFactor) {
		super(explodable, explodeBlastFactor, explodeMotionFactor);
		this.duration = duration;
	}

	public float getDuration() {
		return this.duration;
	}

	@Override
	public DurationDamageHandler newModelHandler() {
		return new DurationDamageHandler();
	}
}
