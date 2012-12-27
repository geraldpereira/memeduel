package fr.byob.game.memeduel.core.god;

import pythagoras.f.Vector;
import fr.byob.game.memeduel.core.god.image.ImageDefinition;
import fr.byob.game.memeduel.core.god.layer.LayerDefinition;
import fr.byob.game.memeduel.core.model.object.ModelObject;
import fr.byob.game.memeduel.core.view.View;
import fr.byob.game.memeduel.core.view.handler.image.ColoredFillerHandler;
import fr.byob.game.memeduel.core.view.handler.layer.StaticLayerHandler;
import fr.byob.game.memeduel.core.view.object.StaticViewObject;

public class ColoredFillerGOD extends StaticGameObjectDefinition {

	private final static Builder builder = new Builder();

	public static Builder fillerBuilder() {
		builder.reset();
		return builder;
	}

	public static class Builder extends StaticGameObjectDefinition.Builder<ColoredFillerGOD> {

		private int fillColor = 0xFF000000;

		private Builder() {
		}

		@Override
		public void reset() {
			super.reset();
			fillColor = 0xFF000000;
		}

		public Builder fillColor(final int fillColor) {
			this.fillColor = fillColor;
			return this;
		}

		@Override
		public ColoredFillerGOD build() {
			return new ColoredFillerGOD(this.position, this.angle, this.layerDefinition, this.imageDefinition, this.fillColor);
		}

	}

	private final int fillColor;

	protected ColoredFillerGOD(final Vector position, final float angle, final LayerDefinition layerDefinition, final ImageDefinition imageDefinition, final int fillColor) {
		super(position, angle, layerDefinition, imageDefinition);
		this.fillColor = fillColor;
	}

	@Override
	public StaticViewObject newViewObject(final View view, final ModelObject modelObject) {
		final StaticViewObject viewObject = new StaticViewObject(this, new ColoredFillerHandler(), new StaticLayerHandler());
		viewObject.addToView(view);
		return viewObject;
	}

	public int getFillColor() {
		return this.fillColor;
	}

}
