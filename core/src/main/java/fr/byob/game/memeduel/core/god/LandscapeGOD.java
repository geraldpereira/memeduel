package fr.byob.game.memeduel.core.god;

import playn.core.Json.Object;
import playn.core.PlayN;
import pythagoras.f.Vector;
import fr.byob.game.memeduel.core.ViewUtils;
import fr.byob.game.memeduel.core.god.damage.DamageDefinition;
import fr.byob.game.memeduel.core.god.image.ImageDefinition;
import fr.byob.game.memeduel.core.god.layer.LayerDefinition;
import fr.byob.game.memeduel.core.god.update.PerspectiveUpdateDefinition;
import fr.byob.game.memeduel.core.god.update.UpdateDefinition;
import fr.byob.game.memeduel.core.model.object.ModelObject;
import fr.byob.game.memeduel.core.view.View;
import fr.byob.game.memeduel.core.view.ZOrder;
import fr.byob.game.memeduel.core.view.handler.layer.PerspectiveLayerHandler;
import fr.byob.game.memeduel.core.view.object.DynamicViewObject;

public class LandscapeGOD extends DynamicGameObjectDefinition {

	public static class Builder extends DynamicGameObjectDefinition.Builder<LandscapeGOD> {

		@Override
		public Builder fromJSON(final AllGODLoader godLoader, final Object jsonEntity) {
			final float yOffset = jsonEntity.getNumber("yOffset");

			this.position(0, yOffset);

			final float depth = jsonEntity.getNumber("depth");

			this.imageDefinition(ImageDefinition.imageBuilder().fromJSON(godLoader, jsonEntity).build());

			final float displayableWorldWidth = ViewUtils.toInitModel(PlayN.graphics().width() / godLoader.getGameGOD().getMinDrawScale());
			final float maxWorldWidth = Math.max(godLoader.getGameGOD().getWorld().getWidth(), displayableWorldWidth);

			this.layerDefinition(LayerDefinition.builder().layerSize(ViewUtils.toInitView(maxWorldWidth), this.imageDefinition.getImage().getImage().height()).depth(depth).zOrder(ZOrder.BACK).repeatX(true).build());
			this.updateDefinition(PerspectiveUpdateDefinition.builder().fromJSON(godLoader, jsonEntity).build());
			return this;
		}

		@Override
		public LandscapeGOD build() {
			return new LandscapeGOD(this.position, this.angle, this.layerDefinition, this.imageDefinition, this.updateDefinition, this.damageDefinition);
		}

	}

	protected LandscapeGOD(final Vector position, final float angle, final LayerDefinition layerDefinition, final ImageDefinition imageDefinition, final UpdateDefinition updateDefinition, final DamageDefinition damageDefinition) {
		super(position, angle, layerDefinition, imageDefinition, updateDefinition, damageDefinition);
	}

	@Override
	public PerspectiveUpdateDefinition getUpdateDefinition() {
		return (PerspectiveUpdateDefinition) super.getUpdateDefinition();
	}

	@Override
	public DynamicViewObject newViewObject(final View view, final ModelObject modelObject) {
		final DynamicViewObject viewObject = new DynamicViewObject(modelObject, this.imageDefinition.newViewHandler(), new PerspectiveLayerHandler());
		viewObject.addToView(view);
		return viewObject;
	}

}
