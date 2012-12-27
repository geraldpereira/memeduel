package fr.byob.game.memeduel.core.god.meme;

import playn.core.Json.Object;
import pythagoras.f.Vector;
import fr.byob.game.memeduel.core.god.AllGODLoader;
import fr.byob.game.memeduel.core.god.B2DGameObjectDefiniton;
import fr.byob.game.memeduel.core.god.b2d.B2DBodyDefinition;
import fr.byob.game.memeduel.core.god.b2d.ShapeDefinition;
import fr.byob.game.memeduel.core.god.damage.DamageDefinition;
import fr.byob.game.memeduel.core.god.helper.MemeLoadHelper;
import fr.byob.game.memeduel.core.god.image.ImageDefinition;
import fr.byob.game.memeduel.core.god.layer.LayerDefinition;
import fr.byob.game.memeduel.core.god.update.UpdateDefinition;
import fr.byob.game.memeduel.core.model.AbstractModel;
import fr.byob.game.memeduel.core.model.handler.b2d.B2DBodyHandler;
import fr.byob.game.memeduel.core.model.handler.damage.DamageHandler;
import fr.byob.game.memeduel.core.model.handler.selection.DefaultSelectionHandler;
import fr.byob.game.memeduel.core.model.handler.selection.SelectionHandler;
import fr.byob.game.memeduel.core.model.handler.update.B2DUpdateHandler;
import fr.byob.game.memeduel.core.model.handler.update.UpdateHandler;
import fr.byob.game.memeduel.core.model.object.B2DModelObject;
import fr.byob.game.memeduel.core.model.object.ModelObject;
import fr.byob.game.memeduel.core.view.View;
import fr.byob.game.memeduel.core.view.handler.image.ImageHandler;
import fr.byob.game.memeduel.core.view.handler.image.MemeTextureHandler;
import fr.byob.game.memeduel.core.view.handler.layer.InterpolateLayerHandler;
import fr.byob.game.memeduel.core.view.handler.layer.LayerHandler;
import fr.byob.game.memeduel.core.view.object.DynamicViewObject;
import fr.byob.game.memeduel.core.view.object.ViewObject;

public class MemeGOD extends B2DGameObjectDefiniton {

	private final static Builder builder = new Builder();

	public static Builder memeBuilder() {
		builder.reset();
		return builder;
	}

	public static class Builder extends B2DGameObjectDefiniton.Builder<MemeGOD> {

		private Builder() {
		}

		@Override
		public Builder fromJSON(final AllGODLoader godLoader, final Object jsonEntity) {
			super.loadPosAndAngle(godLoader, jsonEntity);
			final String type = jsonEntity.getString("type");
			return fromType(godLoader, type);
		}

		public Builder fromType(final AllGODLoader godLoader, final String type) {
			final MemeLoadHelper memeLoadHelper = godLoader.getGameOptionsGOD().getLoadHelper(type);

			this.shapeDefinition(memeLoadHelper.getShapeDefinition());
			this.b2dBodyDefinition(memeLoadHelper.getB2DBodyDefinition());
			this.damageDefinition(memeLoadHelper.getDamageDefinition());
			this.imageDefinition(memeLoadHelper.getImageDefinition());
			this.selectable(memeLoadHelper.isSelectable());
			// final Dimension viewSize =
			// ViewUtils.toInitView(this.shapeDefinition.getSize());
			// this.layerDefinition(new
			// LayerDefinition.Builder().layerSize(viewSize).layerOrigin(ViewUtils.toInitView(this.shapeDefinition.getCenterPosition())).build());
			this.layerDefinition(LayerDefinition.builder().fromShapeDefinition(this.shapeDefinition).build());
			return this;
		}

		@Override
		public MemeGOD build() {
			return new MemeGOD(this.position, this.angle, this.layerDefinition, this.imageDefinition, this.updateDefinition, this.damageDefinition, this.b2dBodyDefinition, this.shapeDefinition, this.selectable);
		}

	}

	public MemeGOD(final Vector position, final float angle, final LayerDefinition layerDefinition, final ImageDefinition imageDefinition, final UpdateDefinition updateDefinition, final DamageDefinition damageDefinition, final B2DBodyDefinition b2dBodyDefinition,
			final ShapeDefinition shapeDefinition, final boolean selectable) {
		super(position, angle, layerDefinition, imageDefinition, updateDefinition, damageDefinition, b2dBodyDefinition, shapeDefinition, selectable);
	}

	@Override
	public B2DModelObject newModelObject(final AbstractModel model) {

		final UpdateHandler updateHandler = new B2DUpdateHandler();

		final B2DBodyHandler b2dBodyHandler = this.shapeDefinition.newModelHandler();
		DamageHandler damageHandler = null;
		if (this.damageDefinition != null) {
			damageHandler = damageDefinition.newModelHandler();
		}

		SelectionHandler selectionHandler = null;
		if (this.isSelectable()) {
			selectionHandler = new DefaultSelectionHandler();
		}

		final B2DModelObject gameObject = new B2DModelObject(model.newId(), this, updateHandler, damageHandler, selectionHandler, b2dBodyHandler);
		gameObject.init(model);
		return gameObject;
	}

	@Override
	public ViewObject newViewObject(final View view, final ModelObject modelObject) {
		final ImageHandler imageHandler = new MemeTextureHandler();
		final LayerHandler layerHandler = new InterpolateLayerHandler();

		final DynamicViewObject viewObject = new DynamicViewObject(modelObject, imageHandler, layerHandler);
		viewObject.addToView(view);
		return viewObject;
	}

}
