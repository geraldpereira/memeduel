package fr.byob.game.memeduel.core.god.damage;

import playn.core.Json.Object;
import fr.byob.game.memeduel.core.god.AllGODLoader;
import fr.byob.game.memeduel.core.god.builder.GODBuilder;
import fr.byob.game.memeduel.core.god.builder.ModelHandlerFactory;
import fr.byob.game.memeduel.core.model.ModelObjectExploder.ExplodeType;
import fr.byob.game.memeduel.core.model.handler.damage.DamageHandler;

public abstract class DamageDefinition implements ModelHandlerFactory<DamageHandler> {

	public abstract static class Builder<T extends DamageDefinition> implements GODBuilder<T> {

		protected ExplodeType explodable;
		protected float explodeBlastFactor;
		protected float explodeMotionFactor;

		protected Builder() {
		}

		@Override
		public void reset() {
			explodable = ExplodeType.NONE;
			explodeBlastFactor = 0;
			explodeMotionFactor = 1.6f;

		}

		public Builder<T> explodable(final ExplodeType explodable) {
			this.explodable = explodable;
			return this;
		}

		public Builder<T> explodeBlastFactor(final float explodeBlastFactor) {
			this.explodeBlastFactor = explodeBlastFactor;
			return this;
		}

		public Builder<T> explodeMotionFactor(final float explodeMotionFactor) {
			this.explodeMotionFactor = explodeMotionFactor;
			return this;
		}

		@Override
		public Builder<T> fromJSON(final AllGODLoader godLoader, final Object jsonEntity) {
			if (jsonEntity.containsKey("explodable")) {
				this.explodable(ExplodeType.valueOf(jsonEntity.getString("explodable")));

				if (jsonEntity.containsKey("explodeBlastFactor")) {
					this.explodeBlastFactor(jsonEntity.getNumber("explodeBlastFactor"));
				}

				if (jsonEntity.containsKey("explodeMotionFactor")) {
					this.explodeMotionFactor(jsonEntity.getNumber("explodeMotionFactor"));
				}
			}
			return this;
		}

		@Override
		public Builder<T> fromOther(final DamageDefinition other) {
			return explodable(other.getExplodeType())
					.explodeBlastFactor(other.getExplodeBlastFactor()).explodeMotionFactor(other.getExplodeMotionFactor());
		}

	}

	private final ExplodeType explodable;
	private final float explodeBlastFactor;
	private final float explodeMotionFactor;

	protected DamageDefinition(final ExplodeType explodable, final float explodeBlastFactor, final float explodeMotionFactor) {
		super();
		this.explodable = explodable;
		this.explodeBlastFactor = explodeBlastFactor;
		this.explodeMotionFactor = explodeMotionFactor;
	}

	public ExplodeType getExplodeType() {
		return explodable;
	}

	public boolean isExplodable() {
		return this.explodable != null && this.explodable != ExplodeType.NONE;
	}

	public float getExplodeBlastFactor() {
		return this.explodeBlastFactor;
	}

	public float getExplodeMotionFactor() {
		return this.explodeMotionFactor;
	}
}
