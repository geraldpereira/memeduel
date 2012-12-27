package fr.byob.game.memeduel.core.god.image;

import fr.byob.game.memeduel.core.GameImage;
import fr.byob.game.memeduel.core.view.handler.image.ImageHandler;
import fr.byob.game.memeduel.core.view.handler.image.ScaledImageHandler;

public class ScaledImageDefinition extends ImageDefinition {

	private final static Builder builder = new Builder();

	public static Builder scaledBuilder() {
		builder.reset();
		return builder;
	}

	public static class Builder extends ImageDefinition.Builder<ScaledImageDefinition> {
		private Builder() {
			super();
		}

		@Override
		public ScaledImageDefinition build() {
			return new ScaledImageDefinition(this.image);
		}
	}

	protected ScaledImageDefinition(final GameImage image) {
		super(image);
	}

	@Override
	public ImageHandler newViewHandler() {
		return new ScaledImageHandler();
	}

}
