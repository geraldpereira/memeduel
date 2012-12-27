package fr.byob.game.memeduel.core.god.cannon;

import pythagoras.f.Vector;
import fr.byob.game.memeduel.core.god.AllGODLoader;
import fr.byob.game.memeduel.core.god.B2DGameObjectDefiniton;
import fr.byob.game.memeduel.core.god.b2d.B2DBodyDefinition;
import fr.byob.game.memeduel.core.god.b2d.ShapeDefinition;
import fr.byob.game.memeduel.core.god.b2d.ShapeDefinition.Shape;
import fr.byob.game.memeduel.core.god.damage.DamageDefinition;
import fr.byob.game.memeduel.core.god.helper.CannonBallLoadHelper;
import fr.byob.game.memeduel.core.god.image.ImageDefinition;
import fr.byob.game.memeduel.core.god.layer.LayerDefinition;
import fr.byob.game.memeduel.core.god.update.UpdateDefinition;
import fr.byob.game.memeduel.core.model.AbstractModel;
import fr.byob.game.memeduel.core.model.cannon.CannonBallModelObject;
import fr.byob.game.memeduel.core.model.handler.b2d.B2DBodyHandler;
import fr.byob.game.memeduel.core.model.handler.cannon.CannonBallHandler;
import fr.byob.game.memeduel.core.model.handler.damage.DamageHandler;
import fr.byob.game.memeduel.core.model.handler.damage.DecayDamageHandler;
import fr.byob.game.memeduel.core.model.handler.damage.RocketDamageHandler;
import fr.byob.game.memeduel.core.model.handler.update.B2DUpdateHandler;
import fr.byob.game.memeduel.core.model.handler.update.RocketUpdateHandler;
import fr.byob.game.memeduel.core.model.handler.update.UpdateHandler;
import fr.byob.game.memeduel.core.model.object.ModelObject;
import fr.byob.game.memeduel.core.view.View;
import fr.byob.game.memeduel.core.view.handler.image.DamageableImageHandler;
import fr.byob.game.memeduel.core.view.handler.image.ImageHandler;
import fr.byob.game.memeduel.core.view.handler.layer.ImmediateLayerHandler;
import fr.byob.game.memeduel.core.view.handler.layer.LayerHandler;
import fr.byob.game.memeduel.core.view.object.DynamicViewObject;
import fr.byob.game.memeduel.core.view.object.ViewObject;

public class CannonBallGOD extends B2DGameObjectDefiniton {

	private final static Builder builder = new Builder();

	public static Builder cannonBallBuilder() {
		builder.reset();
		return builder;
	}

	public static class Builder extends B2DGameObjectDefiniton.Builder<CannonBallGOD> {

		private CannonBallDefinition cannonBallDefinition;

		private Builder() {
		}

		@Override
		public void reset() {
			super.reset();
			cannonBallDefinition = null;
		}

		public Builder cannonBallDefinition(final CannonBallDefinition cannonBallDefinition) {
			this.cannonBallDefinition = cannonBallDefinition;
			return this;
		}

		public Builder fromType(final AllGODLoader loader, final String type) {
			final CannonBallLoadHelper helper = loader.getGameOptionsGOD().getLoadHelper(type);
			this.b2dBodyDefinition(helper.getB2DBodyDefinition());
			this.shapeDefinition(helper.getShapeDefinition());
			this.damageDefinition(helper.getDamageDefinition());
			this.imageDefinition(helper.getImageDefinition());
			this.cannonBallDefinition(helper.getCannonBallDefinition());
			this.layerDefinition(LayerDefinition.builder().layerSize(this.imageDefinition.getImage()).layerOrigin(this.imageDefinition.getImage()).depth(51).build());
			return this;
		}

		@Override
		public CannonBallGOD build() {
			return new CannonBallGOD(this.position, this.angle, this.layerDefinition, this.imageDefinition, this.updateDefinition, this.damageDefinition, this.b2dBodyDefinition, this.shapeDefinition, this.selectable, this.cannonBallDefinition);
		}

	}

	private final CannonBallDefinition cannonBallDefinition;

	private CannonBallGOD(final Vector position, final float angle, final LayerDefinition layerDefinition, final ImageDefinition imageDefinition, final UpdateDefinition updateDefinition, final DamageDefinition damageDefinition, final B2DBodyDefinition b2dBodyDefinition,
			final ShapeDefinition shapeDefinition, final boolean selectable, final CannonBallDefinition cannonBallDefinition) {
		super(position, angle, layerDefinition, imageDefinition, updateDefinition, damageDefinition, b2dBodyDefinition, shapeDefinition, selectable);
		this.cannonBallDefinition = cannonBallDefinition;
	}

	public CannonBallDefinition getCannonBallDefinition() {
		return this.cannonBallDefinition;
	}


	@Override
	public CannonBallModelObject newModelObject(final AbstractModel model) {

		final B2DBodyHandler b2dBodyHandler = this.shapeDefinition.newModelHandler();

		UpdateHandler updateHandler = null;
		DamageHandler damageHandler = null;
		if (this.shapeDefinition.getShapeType() == Shape.CIRCLE) {
			// C'est un boulet
			updateHandler = new B2DUpdateHandler();
			damageHandler = new DecayDamageHandler();
		} else {
			// C'est une roquette
			updateHandler = new RocketUpdateHandler();
			damageHandler = new RocketDamageHandler();
		}

		final CannonBallHandler cannonBallHandler = this.cannonBallDefinition.newCannonBallHandler();

		final CannonBallModelObject modelObject = new CannonBallModelObject(model.newId(), this, updateHandler, damageHandler, b2dBodyHandler, cannonBallHandler);
		modelObject.init(model);
		return modelObject;
	}

	@Override
	public ViewObject newViewObject(final View view, final ModelObject modelObject) {
		final ImageHandler imageHandler = new DamageableImageHandler();
		final LayerHandler layerHandler = new ImmediateLayerHandler();

		final DynamicViewObject viewObject = new DynamicViewObject(modelObject, imageHandler, layerHandler);
		viewObject.addToView(view);
		return viewObject;
	}
}

