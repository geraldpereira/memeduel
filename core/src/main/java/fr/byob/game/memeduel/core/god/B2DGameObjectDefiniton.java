package fr.byob.game.memeduel.core.god;

import playn.core.Json.Object;
import playn.core.Json.Writer;
import pythagoras.f.Vector;
import fr.byob.game.memeduel.core.GamePool;
import fr.byob.game.memeduel.core.god.b2d.B2DBodyDefinition;
import fr.byob.game.memeduel.core.god.b2d.ShapeDefinition;
import fr.byob.game.memeduel.core.god.b2d.ShapeDefinition.Shape;
import fr.byob.game.memeduel.core.god.damage.DamageDefinition;
import fr.byob.game.memeduel.core.god.helper.ObjectLoadHelper;
import fr.byob.game.memeduel.core.god.image.ImageDefinition;
import fr.byob.game.memeduel.core.god.layer.LayerDefinition;
import fr.byob.game.memeduel.core.god.update.B2DUpdateDefinition;
import fr.byob.game.memeduel.core.god.update.UpdateDefinition;
import fr.byob.game.memeduel.core.model.AbstractModel;
import fr.byob.game.memeduel.core.model.handler.b2d.B2DBodyHandler;
import fr.byob.game.memeduel.core.model.handler.damage.DamageHandler;
import fr.byob.game.memeduel.core.model.handler.selection.DefaultSelectionHandler;
import fr.byob.game.memeduel.core.model.handler.selection.SelectionHandler;
import fr.byob.game.memeduel.core.model.handler.update.UpdateHandler;
import fr.byob.game.memeduel.core.model.object.B2DModelObject;
import fr.byob.game.memeduel.core.model.object.ModelObject;
import fr.byob.game.memeduel.core.view.View;
import fr.byob.game.memeduel.core.view.handler.image.ImageHandler;
import fr.byob.game.memeduel.core.view.handler.layer.InterpolateLayerHandler;
import fr.byob.game.memeduel.core.view.handler.layer.LayerHandler;
import fr.byob.game.memeduel.core.view.object.DynamicViewObject;
import fr.byob.game.memeduel.core.view.object.ViewObject;


public class B2DGameObjectDefiniton extends DynamicGameObjectDefinition {

	private final static Builder<B2DGameObjectDefiniton> builder = new Builder<B2DGameObjectDefiniton>();

	public static Builder<B2DGameObjectDefiniton> b2dBuilder() {
		builder.reset();
		return builder;
	}

	public static class Builder<T extends B2DGameObjectDefiniton> extends DynamicGameObjectDefinition.Builder<T> {

		protected B2DBodyDefinition b2dBodyDefinition;
		protected ShapeDefinition shapeDefinition;
		protected boolean selectable;

		protected Builder(){
		}

		@Override
		public void reset() {
			super.reset();
			b2dBodyDefinition = null;
			shapeDefinition = null;
			selectable = true;
		}

		public Builder<T> b2dBodyDefinition(final B2DBodyDefinition b2dBodyDefinition) {
			this.b2dBodyDefinition = b2dBodyDefinition;
			return this;
		}

		public Builder<T> shapeDefinition(final ShapeDefinition shapeDefinition) {
			this.shapeDefinition = shapeDefinition;
			return this;
		}

		public Builder<T> selectable(final boolean selectable) {
			this.selectable = selectable;
			return this;
		}

		@Override
		public Builder<T> fromJSON(final AllGODLoader godLoader, final Object jsonEntity) {
			super.fromJSON(godLoader, jsonEntity);
			this.updateDefinition(B2DUpdateDefinition.builder().build());
			final String shapeStr = jsonEntity.getString("shape");
			final Shape shape = Shape.valueOf(shapeStr);
			final String type = jsonEntity.getString("type");
			final ObjectLoadHelper helper = godLoader.getGameOptionsGOD().getLoadHelper(type);
			this.damageDefinition(helper.getDamageDefinition());
			this.shapeDefinition(shape.loadShapeDefinition(jsonEntity));
			this.b2dBodyDefinition(helper.getB2DBodyDefinition());
			this.imageDefinition(helper.getImageDefinition());
			this.selectable(helper.isSelectable());
			this.layerDefinition(LayerDefinition.builder().fromTexturedShapeDefinition(this.shapeDefinition).build());
			return this;
		}

		@Override
		protected void loadPosAndAngle(final AllGODLoader godLoader, final Object jsonEntity) {
			super.loadPosAndAngle(godLoader, jsonEntity);
			position.addLocal(godLoader.getB2dObjectsOffset().x, godLoader.getB2dObjectsOffset().y);
		}

		@Override
		public Builder<T> fromOther(final T other) {
			super.fromOther(other);
			return this.b2dBodyDefinition(other.getB2DBodyDefition()).shapeDefinition(other.getShapeDefition()).selectable(other.isSelectable());

		}


		@SuppressWarnings("unchecked")
		@Override
		public T build() {
			return (T) new B2DGameObjectDefiniton(this.position, this.angle, this.layerDefinition, this.imageDefinition, this.updateDefinition, this.damageDefinition, this.b2dBodyDefinition, this.shapeDefinition, this.selectable);
		}

	}

	public String test;

	protected final B2DBodyDefinition b2dBodyDefinition;
	protected final ShapeDefinition shapeDefinition;
	protected final boolean selectable;

	protected B2DGameObjectDefiniton(final Vector position, final float angle, final LayerDefinition layerDefinition, final ImageDefinition imageDefinition, final UpdateDefinition updateDefinition, final DamageDefinition damageDefinition, final B2DBodyDefinition b2dBodyDefinition,
			final ShapeDefinition shapeDefinition, final boolean selectable) {
		super(position, angle, layerDefinition, imageDefinition, updateDefinition, damageDefinition);
		this.b2dBodyDefinition = b2dBodyDefinition;
		this.shapeDefinition = shapeDefinition;
		this.selectable = selectable;
	}

	public boolean isSelectable() {
		return this.selectable;
	}

	public B2DBodyDefinition getB2DBodyDefition() {
		return this.b2dBodyDefinition;
	}

	public ShapeDefinition getShapeDefition() {
		return this.shapeDefinition;
	}


	public void saveFromModel(final B2DModelObject modelObject, final Vector offset, final Writer writer) {
		final Vector currentPosition = GamePool.instance().popVector();
		modelObject.getUpdateHandler().getCurrentPositionToOut(currentPosition);
		writer.object();
		// Write the position
		writer.object("position");
		writer.value("x", currentPosition.x - offset.x);
		writer.value("y", currentPosition.y - offset.y);
		writer.end();
		// Write the angle
		writer.value("angle", modelObject.getUpdateHandler().getCurrentRotation());

		writer.value("type", this.getB2DBodyDefition().getId());

		this.getShapeDefition().save(writer);

		// Everything else can be deducted from the above
		writer.end();

		GamePool.instance().pushVector(1);
	}

	public void save(final Writer writer, final Vector offset) {
		writer.object();
		// Write the position
		writer.object("position");
		writer.value("x", position.x - offset.x);
		writer.value("y", position.y - offset.y);
		writer.end();
		// Write the angle
		writer.value("angle", angle);

		writer.value("type", this.getB2DBodyDefition().getId());

		this.getShapeDefition().save(writer);

		// Everything else can be deducted from the above
		writer.end();
	}

	@Override
	public B2DModelObject newModelObject(final AbstractModel model) {
		final UpdateHandler updateHandler = this.updateDefinition.newModelHandler();
		final B2DBodyHandler b2dBodyHandler = this.shapeDefinition.newModelHandler();
		DamageHandler damageHandler = null;
		if (this.damageDefinition != null) {
			damageHandler = this.damageDefinition.newModelHandler();
		}

		SelectionHandler selectionHandler = null;
		if (this.selectable) {
			selectionHandler = new DefaultSelectionHandler();
		}

		final B2DModelObject modelObject = new B2DModelObject(model.newId(), this, updateHandler, damageHandler, selectionHandler, b2dBodyHandler);
		modelObject.init(model);
		return modelObject;
	}

	@Override
	public ViewObject newViewObject(final View view, final ModelObject modelObject) {
		final ImageHandler imageHandler = this.shapeDefinition.newViewHandler();
		final LayerHandler layerHandler = new InterpolateLayerHandler();

		final DynamicViewObject viewObject = new DynamicViewObject(modelObject, imageHandler, layerHandler);
		viewObject.addToView(view);

		return viewObject;
	}

}
