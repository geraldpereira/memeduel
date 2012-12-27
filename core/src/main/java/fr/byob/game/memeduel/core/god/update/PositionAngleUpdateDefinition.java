package fr.byob.game.memeduel.core.god.update;

import playn.core.Json;
import playn.core.Json.Object;
import pythagoras.f.Vector;
import fr.byob.game.memeduel.core.god.AllGODLoader;
import fr.byob.game.memeduel.core.god.builder.GODBuilder;
import fr.byob.game.memeduel.core.model.handler.update.PositionAngleUpdateHandler;
import fr.byob.game.memeduel.core.model.handler.update.UpdateHandler;

public class PositionAngleUpdateDefinition implements UpdateDefinition {

	private final static Builder builder = new Builder();

	public static Builder builder() {
		builder.reset();
		return builder;
	}

	public static class Builder implements GODBuilder<PositionAngleUpdateDefinition> {

		protected final Vector positionOffset = new Vector(0, 0);
		protected float angleOffset = 0;

		private Builder() {
		}

		@Override
		public void reset() {
			positionOffset.set(0, 0);
			angleOffset = 0;
		}

		public Builder positionOffset(final Vector positionOffset) {
			this.positionOffset.set(positionOffset);
			return this;
		}

		public Builder positionOffset(final float x, final float y) {
			this.positionOffset.set(x, y);
			return this;
		}

		public Builder angleOffset(final float angleOffset) {
			this.angleOffset = angleOffset;
			return this;
		}

		@Override
		public PositionAngleUpdateDefinition build() {
			return new PositionAngleUpdateDefinition(this.positionOffset,this.angleOffset);
		}

		@Override
		public Builder fromJSON(final AllGODLoader godLoader, final Object jsonEntity) {
			final Json.Object offsetJSON = jsonEntity.getObject("offset");
			final float xOffset = offsetJSON.getNumber("x");
			final float yOffset = offsetJSON.getNumber("y");
			return this.positionOffset(xOffset, yOffset);
		}

		@Override
		public Builder fromOther(final PositionAngleUpdateDefinition other) {
			return this.positionOffset(other.getPositionOffset())
					.angleOffset(other.getAngleOffset());
		}

	}

	private final Vector positionOffset;
	private final float angleOffset;

	private PositionAngleUpdateDefinition(final Vector positionOffset, final float angleOffset) {
		super();
		this.positionOffset = new Vector(positionOffset);
		this.angleOffset = angleOffset;
	}

	public Vector getPositionOffset() {
		return this.positionOffset;
	}

	public float getAngleOffset() {
		return this.angleOffset;
	}

	@Override
	public UpdateHandler newModelHandler() {
		return new PositionAngleUpdateHandler();
	}

}
