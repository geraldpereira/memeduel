package fr.byob.game.memeduel.core.god.update;

import playn.core.Json;
import playn.core.Json.Object;
import pythagoras.f.Vector;
import fr.byob.game.memeduel.core.god.AllGODLoader;
import fr.byob.game.memeduel.core.god.builder.GODBuilder;
import fr.byob.game.memeduel.core.model.handler.update.PerspectiveUpdateHandler;
import fr.byob.game.memeduel.core.model.handler.update.UpdateHandler;

public class PerspectiveUpdateDefinition implements UpdateDefinition {

	private final static Builder builder = new Builder();

	public static Builder builder() {
		builder.reset();
		return builder;
	}

	public static class Builder implements GODBuilder<PerspectiveUpdateDefinition> {

		protected final Vector scrollFactor = new Vector();

		private Builder() {
		}

		@Override
		public void reset() {
			scrollFactor.set(0, 0);
		}

		public Builder scrollFactor(final Vector scrollFactor) {
			this.scrollFactor.set(scrollFactor);
			return this;
		}

		public Builder scrollFactor(final float factorX, final float factorY) {
			this.scrollFactor.set(factorX, factorY);
			return this;
		}

		@Override
		public PerspectiveUpdateDefinition build() {
			return new PerspectiveUpdateDefinition(this.scrollFactor);
		}

		@Override
		public Builder fromJSON(final AllGODLoader godLoader, final Object jsonEntity) {
			final Json.Object scrollFactorJSON = jsonEntity.getObject("scrollFactor");
			final float xFactor = scrollFactorJSON.getNumber("x");
			final float yFactor = scrollFactorJSON.getNumber("y");

			return this.scrollFactor(xFactor, yFactor);
		}

		@Override
		public Builder fromOther(final PerspectiveUpdateDefinition other) {
			return this.scrollFactor(other.getScrollFactor());
		}

	}

	private final Vector scrollFactor;

	private PerspectiveUpdateDefinition(final Vector scrollFactor) {
		super();
		this.scrollFactor = new Vector(scrollFactor);
	}

	public Vector getScrollFactor() {
		return this.scrollFactor;
	}

	@Override
	public UpdateHandler newModelHandler() {
		return new PerspectiveUpdateHandler();
	}

}
