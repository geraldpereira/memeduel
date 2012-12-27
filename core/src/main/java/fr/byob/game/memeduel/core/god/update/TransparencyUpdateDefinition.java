package fr.byob.game.memeduel.core.god.update;

import playn.core.Json.Object;
import fr.byob.game.memeduel.core.god.AllGODLoader;
import fr.byob.game.memeduel.core.god.builder.GODBuilder;
import fr.byob.game.memeduel.core.model.handler.update.TransparencyUpdateHandler;
import fr.byob.game.memeduel.core.model.handler.update.UpdateHandler;

public class TransparencyUpdateDefinition implements UpdateDefinition {

	private final static Builder builder = new Builder();

	public static Builder builder() {
		builder.reset();
		return builder;
	}

	public static class Builder implements GODBuilder<TransparencyUpdateDefinition> {

		private float alphaFactor;
		private float upperAlpha;
		private float lowerAlpha;

		private Builder() {

		}

		@Override
		public void reset() {
			alphaFactor = 0;
			upperAlpha = 0;
			lowerAlpha = 0;
		}

		public Builder alphaFactor(final float alphaFactor) {
			this.alphaFactor = alphaFactor;
			return this;
		}

		public Builder upperAlpha(final float upperAlpha) {
			this.upperAlpha = upperAlpha;
			return this;
		}

		public Builder lowerAlpha(final float lowerAlpha) {
			this.lowerAlpha = lowerAlpha;
			return this;
		}

		@Override
		public TransparencyUpdateDefinition build() {
			return new TransparencyUpdateDefinition(this.alphaFactor, this.upperAlpha, this.lowerAlpha);
		}

		@Override
		public Builder fromJSON(final AllGODLoader godLoader, final Object jsonEntity) {
			return this;
		}

		@Override
		public Builder fromOther(final TransparencyUpdateDefinition other) {
			return this.alphaFactor(other.getAlphaFactor())
					.upperAlpha(other.getUpperAlpha())
					.lowerAlpha(other.getLowerAlpha());
		}

	}

	private final float alphaFactor;
	private final float upperAlpha;
	private final float lowerAlpha;

	private TransparencyUpdateDefinition(final float alphaFactor, final float upperAlpha, final float lowerAlpha) {
		super();
		this.alphaFactor = alphaFactor;
		this.upperAlpha = upperAlpha;
		this.lowerAlpha = lowerAlpha;
	}

	public float getAlphaFactor() {
		return this.alphaFactor;
	}

	public float getUpperAlpha() {
		return this.upperAlpha;
	}

	public float getLowerAlpha() {
		return this.lowerAlpha;
	}

	@Override
	public UpdateHandler newModelHandler() {
		return new TransparencyUpdateHandler();
	}

}
