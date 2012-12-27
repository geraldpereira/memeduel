package fr.byob.game.memeduel.core.god.damage;

import java.util.List;

import playn.core.Json.Object;
import fr.byob.game.memeduel.core.GameImage;
import fr.byob.game.memeduel.core.god.AllGODLoader;
import fr.byob.game.memeduel.core.model.ModelObjectExploder.ExplodeType;
import fr.byob.game.memeduel.core.model.handler.damage.LifespanDamageHandler;

public class LifespanDefinition extends DamageDefinition {

	private final static Builder<LifespanDefinition> builder = new Builder<LifespanDefinition>();

	public static Builder<LifespanDefinition> lifespanBuilder() {
		builder.reset();
		return builder;
	}

	public static class Builder<T extends LifespanDefinition> extends DamageDefinition.Builder<T> {

		// Quantity of life
		protected float lifespanFactor;
		// Min damage persentage before they are applied to this object
		protected float minDamagePercentage = 0.0f;
		// How this object hurts the others
		protected float otherDamageFactor = 1.0f;
		protected List<DamageSlice> damageSlices;

		protected Builder() {
		}

		@Override
		public void reset() {
			super.reset();
			lifespanFactor = 0;
			minDamagePercentage = 0;
			otherDamageFactor = 1;
			damageSlices = null;
		}

		public Builder<T> lifespanFactor(final float lifespanFactor) {
			this.lifespanFactor = lifespanFactor;
			return this;
		}

		public Builder<T> minDamagePercentage(final float minDamagePercentage) {
			this.minDamagePercentage = minDamagePercentage;
			return this;
		}

		public Builder<T> otherDamageFactor(final float otherDamageFactor) {
			this.otherDamageFactor = otherDamageFactor;
			return this;
		}

		public Builder<T> damageSlices(final List<DamageSlice> damageSlices) {
			this.damageSlices = damageSlices;
			return this;
		}

		@Override
		public Builder<T> fromJSON(final AllGODLoader godLoader, final Object jsonEntity) {
			super.fromJSON(godLoader, jsonEntity);
			if (jsonEntity.containsKey("otherDamageFactor")) {
				otherDamageFactor(jsonEntity.getNumber("otherDamageFactor"));
			}
			if (jsonEntity.containsKey("minDamagePercentage")) {
				minDamagePercentage(jsonEntity.getNumber("minDamagePercentage"));
			}
			return this.lifespanFactor(jsonEntity.getNumber("lifespanFactor"));
		}

		@Override
		public Builder<T> fromOther(final LifespanDefinition other) {
			super.fromOther(other);
			return this.lifespanFactor(other.getLifespanFactor()).damageSlices(other.getDamageSlices());
		}

		@SuppressWarnings("unchecked")
		@Override
		public T build() {
			return (T) new LifespanDefinition(lifespanFactor, minDamagePercentage, otherDamageFactor, damageSlices, explodable, explodeBlastFactor, explodeMotionFactor);
		}

	}

	private final float lifespanFactor;
	private final float minDamagePercentage;
	private final float otherDamageFactor;
	private final List<DamageSlice> damageSlices;

	protected LifespanDefinition(final float lifespanFactor, final float minDamagePercentage, final float otherDamageFactor, final List<DamageSlice> damageSlices, final ExplodeType explodable, final float explodeBlastFactor, final float explodeMotionFactor) {
		super(explodable, explodeBlastFactor, explodeMotionFactor);
		this.lifespanFactor = lifespanFactor;
		this.minDamagePercentage = minDamagePercentage;
		this.otherDamageFactor = otherDamageFactor;
		this.damageSlices = damageSlices;
	}

	public float getLifespanFactor() {
		return lifespanFactor;
	}

	public float getMinDamagePercentage() {
		return minDamagePercentage;
	}

	public float getOtherDamageFactor() {
		return otherDamageFactor;
	}

	public List<DamageSlice> getDamageSlices() {
		return damageSlices;
	}


	public static class DamageSlice {
		private final float maxDamagePercentage;
		private final GameImage image;

		public DamageSlice(final float maxDamagePercentage, final GameImage image) {
			super();
			this.maxDamagePercentage = maxDamagePercentage;
			this.image = image;
		}

		public GameImage getImage(final float lifespan) {
			if (this.maxDamagePercentage >= lifespan) {
				return this.image;
			}
			return null;
		}

	}

	@Override
	public LifespanDamageHandler newModelHandler() {
		return new LifespanDamageHandler();
	}
}
