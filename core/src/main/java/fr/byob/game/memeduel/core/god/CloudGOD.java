package fr.byob.game.memeduel.core.god;

import playn.core.Json.Object;
import pythagoras.f.Vector;
import fr.byob.game.memeduel.core.MathUtils;
import fr.byob.game.memeduel.core.god.damage.DamageDefinition;
import fr.byob.game.memeduel.core.god.image.ImageDefinition;
import fr.byob.game.memeduel.core.god.layer.LayerDefinition;
import fr.byob.game.memeduel.core.god.update.PositionAngleUpdateDefinition;
import fr.byob.game.memeduel.core.god.update.UpdateDefinition;
import fr.byob.game.memeduel.core.model.AbstractModel;
import fr.byob.game.memeduel.core.model.handler.update.CloudUpdateHandler;
import fr.byob.game.memeduel.core.model.object.DefaultModelObject;
import fr.byob.game.memeduel.core.model.object.ModelObject;
import fr.byob.game.memeduel.core.view.View;
import fr.byob.game.memeduel.core.view.ZOrder;
import fr.byob.game.memeduel.core.view.handler.layer.ImmediateLayerHandler;
import fr.byob.game.memeduel.core.view.object.DynamicViewObject;

public class CloudGOD extends DynamicGameObjectDefinition {


	public static class Builder extends DynamicGameObjectDefinition.Builder<CloudGOD> {

		@Override
		public Builder fromJSON(final AllGODLoader godLoader, final Object jsonEntity) {
			super.fromJSON(godLoader, jsonEntity);
			final float depth = jsonEntity.getInt("depth");
			this.imageDefinition(ImageDefinition.imageBuilder().fromJSON(godLoader, jsonEntity).build());
			this.layerDefinition(LayerDefinition.builder().layerSize(this.imageDefinition.getImage().dimensionToOut(MathUtils.tmpDim())).depth(depth).zOrder(ZOrder.DYNAMIC).build());
			this.updateDefinition(PositionAngleUpdateDefinition.builder().fromJSON(godLoader, jsonEntity).build());
			return this;
		}

		@Override
		public CloudGOD build() {
			return new CloudGOD(this.position, this.angle, this.layerDefinition, this.imageDefinition, this.updateDefinition, this.damageDefinition);
		}

	}

	protected CloudGOD(final Vector position, final float angle, final LayerDefinition layerDefinition, final ImageDefinition imageDefinition, final UpdateDefinition updateDefinition, final DamageDefinition damageDefinition) {
		super(position, angle, layerDefinition, imageDefinition, updateDefinition, damageDefinition);
	}

	@Override
	public PositionAngleUpdateDefinition getUpdateDefinition() {
		return (PositionAngleUpdateDefinition) super.getUpdateDefinition();
	}

	@Override
	public DefaultModelObject newModelObject(final AbstractModel model) {
		final DefaultModelObject modelObject = new DefaultModelObject(model.newId(), this, new CloudUpdateHandler());
		modelObject.init(model);
		return modelObject;
	}

	@Override
	public DynamicViewObject newViewObject(final View view, final ModelObject modelObject) {
		final DynamicViewObject viewObject = new DynamicViewObject(modelObject, this.imageDefinition.newViewHandler(), new ImmediateLayerHandler());
		viewObject.addToView(view);
		return viewObject;
	}

}
