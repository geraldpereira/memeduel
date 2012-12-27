package fr.byob.game.memeduel.core.god.image;

import playn.core.Json;
import playn.core.Json.Object;
import pythagoras.f.Dimension;
import fr.byob.game.memeduel.core.GameImage;
import fr.byob.game.memeduel.core.god.AllGODLoader;
import fr.byob.game.memeduel.core.view.handler.image.AnimationImageHandler;

public class AnimationImageDefinition extends ImageDefinition {

	private final static Builder builder = new Builder();

	public static Builder animBuilder() {
		builder.reset();
		return builder;
	}

	public static class Builder extends ImageDefinition.Builder<AnimationImageDefinition> {

		private final Dimension imageSize = new Dimension();
		private float frameCount;
		private float fps;

		private Builder() {
			super();
		}

		@Override
		public void reset() {
			super.reset();
			imageSize.setSize(0, 0);
			frameCount = 0;
			fps = 0;
		}

		public Builder imageSize(final Dimension imageSize) {
			this.imageSize.setSize(imageSize);
			return this;
		}

		public Builder imageSize(final float width, final float height) {
			this.imageSize.setSize(width, height);
			return this;
		}

		public Builder frameCount(final float frameCount) {
			this.frameCount = frameCount;
			return this;
		}

		public Builder fps(final float fps) {
			this.fps = fps;
			return this;
		}

		@Override
		public AnimationImageDefinition build() {
			return new AnimationImageDefinition(this.image, this.imageSize, this.frameCount, this.fps);
		}

		@Override
		public Builder fromJSON(final AllGODLoader godLoader, final Object jsonEntity) {
			super.fromJSON(godLoader, jsonEntity);

			final Json.Object centerPositionJSON = jsonEntity.getObject("imageSize");
			final float width = centerPositionJSON.getNumber("width");
			final float height = centerPositionJSON.getNumber("height");
			this.imageSize(width, height);

			return this.frameCount(jsonEntity.getNumber("frameCount")).fps(jsonEntity.getNumber("fps"));

		}

		@Override
		public Builder fromOther(final AnimationImageDefinition other) {
			super.fromOther(other);
			return this.imageSize(other.getImageSize()).frameCount(other.getFrameCount()).fps(other.getFps());
		}
	}

	private final Dimension imageSize;
	private final float frameCount;
	private final float fps;

	protected AnimationImageDefinition(final GameImage image, final Dimension imageSize, final float frameCount, final float fps) {
		super(image);
		this.imageSize = new Dimension(imageSize);
		this.frameCount = frameCount;
		this.fps = fps;
	}

	public Dimension getImageSize() {
		return this.imageSize;
	}

	public float getFrameCount() {
		return this.frameCount;
	}

	public float getFps() {
		return this.fps;
	}

	@Override
	public AnimationImageHandler newViewHandler() {
		return new AnimationImageHandler();
	}


}
