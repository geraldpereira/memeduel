package fr.byob.game.memeduel.core.god.damage;

import playn.core.Json.Object;
import fr.byob.game.memeduel.core.god.AllGODLoader;
import fr.byob.game.memeduel.core.god.builder.GODBuilder;
import fr.byob.game.memeduel.core.model.ModelObjectExploder.ExplodeType;
import fr.byob.game.memeduel.core.model.handler.damage.TrashableDamageHandler;

public class TrashableDefinition extends DamageDefinition {

	private final static Builder builder = new Builder();

	public static Builder builder() {
		builder.reset();
		return builder;
	}

	public static class Builder implements GODBuilder<TrashableDefinition> {

		private Builder() {
		}

		@Override
		public void reset() {
		}

		@Override
		public Builder fromJSON(final AllGODLoader godLoader, final Object jsonEntity) {
			return this;
		}

		@Override
		public Builder fromOther(final TrashableDefinition other) {
			return this;
		}

		@Override
		public TrashableDefinition build() {
			return new TrashableDefinition();
		}
	}

	private TrashableDefinition() {
		super(ExplodeType.NONE, 0, 0);
	}

	@Override
	public TrashableDamageHandler newModelHandler() {
		return new TrashableDamageHandler();
	}

}
