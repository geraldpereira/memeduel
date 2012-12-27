package fr.byob.game.memeduel.core.god.image;

import playn.core.Json.Object;
import fr.byob.game.memeduel.core.GameImage;
import fr.byob.game.memeduel.core.god.AllGODLoader;
import fr.byob.game.memeduel.core.god.builder.GODBuilder;
import fr.byob.game.memeduel.core.god.builder.ViewHandlerFactory;
import fr.byob.game.memeduel.core.view.handler.image.DefaultImageHandler;
import fr.byob.game.memeduel.core.view.handler.image.ImageHandler;

public class ImageDefinition implements ViewHandlerFactory<ImageHandler> {

	private final static Builder<ImageDefinition> builder = new Builder<ImageDefinition>();

	public static Builder<ImageDefinition> imageBuilder() {
		builder.reset();
		return builder;
	}

	public static class Builder<T extends ImageDefinition> implements GODBuilder<T> {

		protected GameImage image;

		protected Builder() {
		}

		@Override
		public void reset() {
			image = null;
		}

		public Builder<T> image(final GameImage image) {
			this.image = image;
			return this;
		}

		@SuppressWarnings("unchecked")
		@Override
		public T build() {
			return (T) new ImageDefinition(this.image);
		}

		@Override
		public Builder<T> fromJSON(final AllGODLoader godLoader, final Object jsonEntity) {

			if (jsonEntity.containsKey("image")) {
				final String imageStr = jsonEntity.getString("image");
				this.image(GameImage.valueOf(imageStr));
			}

			return this;
		}

		@Override
		public Builder<T> fromOther(final T other) {
			return this.image(other.getImage());
		}

	}

	protected final GameImage image;

	protected ImageDefinition(final GameImage image) {
		this.image = image;
	}

	public GameImage getImage() {
		return this.image;
	}

	@Override
	public ImageHandler newViewHandler() {
		return new DefaultImageHandler();
	}

}
