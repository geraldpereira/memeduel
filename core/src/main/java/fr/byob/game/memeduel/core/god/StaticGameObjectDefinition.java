package fr.byob.game.memeduel.core.god;

import playn.core.Json;
import playn.core.Json.Object;
import pythagoras.f.Vector;
import fr.byob.game.memeduel.core.god.builder.GODBuilder;
import fr.byob.game.memeduel.core.god.image.ImageDefinition;
import fr.byob.game.memeduel.core.god.layer.LayerDefinition;
import fr.byob.game.memeduel.core.model.object.ModelObject;
import fr.byob.game.memeduel.core.view.View;
import fr.byob.game.memeduel.core.view.object.StaticViewObject;
import fr.byob.game.memeduel.core.view.object.ViewObject;

public class StaticGameObjectDefinition implements GameObjectDefinition {

	private final static Builder<StaticGameObjectDefinition> builder = new Builder<StaticGameObjectDefinition>();

	public static Builder<StaticGameObjectDefinition> staticBuilder() {
		builder.reset();
		return builder;
	}

	public static class Builder<T extends StaticGameObjectDefinition> implements GODBuilder<T> {

		protected LayerDefinition layerDefinition;
		protected ImageDefinition imageDefinition;
		protected final Vector position = new Vector();
		protected float angle = 0;

		protected Builder() {
		}

		@Override
		public void reset() {
			layerDefinition = null;
			imageDefinition = null;
			position.set(0, 0);
			angle = 0;
		}

		public Builder<T> layerDefinition(final LayerDefinition layerDefinition) {
			this.layerDefinition = layerDefinition;
			return this;
		}

		public Builder<T> imageDefinition(final ImageDefinition imageDefinition) {
			this.imageDefinition = imageDefinition;
			return this;
		}

		public Builder<T> position(final Vector position) {
			this.position.set(position);
			return this;
		}

		public Builder<T> position(final float x, final float y) {
			this.position.set(x, y);
			return this;
		}

		public Builder<T> angle(final float angle) {
			this.angle = angle;
			return this;
		}

		@Override
		public Builder<T> fromOther(final T other) {
			return this.position(other.getPosition()).angle(other.getAngle()).imageDefinition(other.getImageDefinition()).layerDefinition(other.getLayerDefinition());
		}

		@Override
		public Builder<T> fromJSON(final AllGODLoader godLoader, final Object jsonEntity) {
			loadPosAndAngle(godLoader, jsonEntity);
			return this;
		}

		protected void loadPosAndAngle(final AllGODLoader godLoader, final Object jsonEntity) {
			final Json.Object positionJSON = jsonEntity.getObject("position");
			final float x = positionJSON.getNumber("x");
			final float y = positionJSON.getNumber("y");
			this.position.set(x, y);

			if (jsonEntity.containsKey("angle")) {
				this.angle(jsonEntity.getNumber("angle"));
			}
		}

		@SuppressWarnings("unchecked")
		@Override
		public T build() {
			return (T) new StaticGameObjectDefinition(this.position, this.angle, this.layerDefinition, this.imageDefinition);
		}

	}

	// tous les objets sont affichés
	protected final LayerDefinition layerDefinition;
	protected final ImageDefinition imageDefinition;
	protected final Vector position = new Vector();
	protected final float angle;

	protected StaticGameObjectDefinition(final Vector position, final float angle, final LayerDefinition layerDefinition, final ImageDefinition imageDefinition) {
		super();
		this.position.set(position);
		this.angle = angle;
		this.layerDefinition = layerDefinition;
		this.imageDefinition = imageDefinition;
	}

	public Vector getPosition() {
		return this.position;
	}

	public float getAngle() {
		return this.angle;
	}

	public LayerDefinition getLayerDefinition() {
		return this.layerDefinition;
	}

	public ImageDefinition getImageDefinition() {
		return this.imageDefinition;
	}

	public ViewObject newViewObject(final View view, final ModelObject modelObject) {
		final StaticViewObject viewObject = new StaticViewObject(this, this.imageDefinition.newViewHandler(), this.layerDefinition.newViewHandler());
		viewObject.addToView(view);
		return viewObject;
	}
}
