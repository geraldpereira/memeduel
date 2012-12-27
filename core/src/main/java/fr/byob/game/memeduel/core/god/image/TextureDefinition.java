package fr.byob.game.memeduel.core.god.image;

import playn.core.Json.Object;
import fr.byob.game.memeduel.core.GameImage;
import fr.byob.game.memeduel.core.god.AllGODLoader;
import fr.byob.game.memeduel.core.view.Color;
import fr.byob.game.memeduel.core.view.handler.image.ImageHandler;

public class TextureDefinition extends ImageDefinition {

	private final static Builder builder = new Builder();

	public static Builder textureBuilder() {
		builder.reset();
		return builder;
	}

	public static class Builder extends ImageDefinition.Builder<TextureDefinition> {

		private float strokeWidth;
		private Color strokeColor;

		private Builder() {
			super();
		}

		@Override
		public void reset() {
			super.reset();
			strokeWidth = 0;
			strokeColor = null;
		}

		public Builder strokeWidth(final float strokeWidth) {
			this.strokeWidth = strokeWidth;
			return this;
		}


		public Builder strokeColor(final Color strokeColor) {
			this.strokeColor = strokeColor;
			return this;
		}

		@Override
		public TextureDefinition build() {
			return new TextureDefinition(this.image, this.strokeColor, this.strokeWidth);
		}

		@Override
		public Builder fromJSON(final AllGODLoader godLoader, final Object jsonEntity) {
			super.fromJSON(godLoader, jsonEntity);
			return this.strokeWidth(jsonEntity.getNumber("strokeWidth")).strokeColor(new Color(jsonEntity.getString("strokeColor")));

		}

		@Override
		public Builder fromOther(final TextureDefinition other) {
			super.fromOther(other);
			return this.strokeWidth(other.getStrokeWidth()).strokeColor(other.getStrokeColor());
		}
	}

	private final float strokeWidth;
	private final Color strokeColor;

	protected TextureDefinition(final GameImage image, final Color strokeColor, final float strokeWidth) {
		super(image);
		this.strokeColor = strokeColor;
		this.strokeWidth = strokeWidth;
	}

	public float getStrokeWidth() {
		return this.strokeWidth;
	}

	public Color getStrokeColor() {
		return this.strokeColor;
	}

	@Override
	public ImageHandler newViewHandler() {
		// Géré par le shapeDefinition
		return null;
	}

}
