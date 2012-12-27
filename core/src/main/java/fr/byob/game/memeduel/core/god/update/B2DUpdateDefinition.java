package fr.byob.game.memeduel.core.god.update;

import playn.core.Json.Object;
import fr.byob.game.memeduel.core.god.AllGODLoader;
import fr.byob.game.memeduel.core.god.builder.GODBuilder;
import fr.byob.game.memeduel.core.model.handler.update.B2DUpdateHandler;
import fr.byob.game.memeduel.core.model.handler.update.UpdateHandler;

public class B2DUpdateDefinition implements UpdateDefinition {

	private final static Builder builder = new Builder();

	public static Builder builder() {
		builder.reset();
		return builder;
	}

	public static class Builder implements GODBuilder<B2DUpdateDefinition> {

		private Builder() {
		}

		@Override
		public B2DUpdateDefinition build() {
			return new B2DUpdateDefinition();
		}

		@Override
		public void reset() {
		}

		@Override
		public Builder fromJSON(final AllGODLoader godLoader, final Object jsonEntity) {
			return this;
		}

		@Override
		public Builder fromOther(final B2DUpdateDefinition other) {
			return this;
		}

	}

	private B2DUpdateDefinition() {

	}

	@Override
	public UpdateHandler newModelHandler() {
		return new B2DUpdateHandler();
	}

}
