package fr.byob.game.memeduel.core.god.cannon;

import playn.core.Json.Object;
import fr.byob.game.memeduel.core.god.AllGODLoader;
import fr.byob.game.memeduel.core.god.GameObjectDefinition;
import fr.byob.game.memeduel.core.god.builder.GODBuilder;
import fr.byob.game.memeduel.core.model.handler.cannon.CannonBallHandler;
import fr.byob.game.memeduel.core.model.handler.cannon.DefaultCannonBallHandler;
import fr.byob.game.memeduel.core.model.handler.cannon.FragCannonBallHandler;

public class CannonBallDefinition implements GameObjectDefinition {

	private final static Builder builder = new Builder();

	public static Builder builder() {
		builder.reset();
		return builder;
	}

	public static class Builder implements GODBuilder<CannonBallDefinition> {
		private String cannonBallType;
		private float fireSpeed;
		// The min damage this cannonBall can do to other objets ON FIRST HIT !
		private float minOtherDamagePercentage = 0.0f;

		private Builder() {
		}

		@Override
		public void reset() {
			cannonBallType = null;
			fireSpeed = 0;
			minOtherDamagePercentage = 0;
		}


		public Builder cannonBallType(final String cannonBallType) {
			this.cannonBallType = cannonBallType;
			return this;
		}

		public Builder fireSpeed(final float fireSpeed) {
			this.fireSpeed = fireSpeed;
			return this;
		}

		public Builder minOtherDamagePercentage(final float minOtherDamagePercentage) {
			this.minOtherDamagePercentage = minOtherDamagePercentage;
			return this;
		}

		@Override
		public CannonBallDefinition build() {
			return new CannonBallDefinition(cannonBallType, fireSpeed, minOtherDamagePercentage);
		}

		@Override
		public GODBuilder<CannonBallDefinition> fromJSON(final AllGODLoader godLoader, final Object jsonEntity) {

			if (jsonEntity.containsKey("minOtherDamagePercentage")) {
				minOtherDamagePercentage(jsonEntity.getNumber("minOtherDamagePercentage"));
			}

			cannonBallType(jsonEntity.getString("type")).fireSpeed(jsonEntity.getNumber("fireSpeed"));
			return this;
		}

		@Override
		public GODBuilder<CannonBallDefinition> fromOther(final CannonBallDefinition other) {
			return cannonBallType(other.getCannonBallType()).fireSpeed(other.getFireSpeed()).minOtherDamagePercentage(other.getMinOtherDamagePercentage());
		}

	}

	private final String cannonBallType;
	private final float fireSpeed;
	private final float minOtherDamagePercentage;

	private CannonBallDefinition(final String cannonBallType, final float fireSpeed, final float minDamagePercentage) {
		super();
		this.cannonBallType = cannonBallType;
		this.fireSpeed = fireSpeed;
		this.minOtherDamagePercentage = minDamagePercentage;
	}

	public String getCannonBallType() {
		return this.cannonBallType;
	}

	public float getFireSpeed() {
		return this.fireSpeed;
	}

	public float getMinOtherDamagePercentage() {
		return minOtherDamagePercentage;
	}

	public CannonBallHandler newCannonBallHandler() {
		if ("CANNON_BALL_FRAG".equals(cannonBallType)) {
			return new FragCannonBallHandler();
		} else if ("CANNON_BALL_FRAG_SHOT".equals(cannonBallType)) {
			return null;
		}
		return new DefaultCannonBallHandler();
	}

}
