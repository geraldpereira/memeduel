package fr.byob.game.memeduel.core.god;

import playn.core.Json.Object;
import pythagoras.f.Vector;
import fr.byob.game.memeduel.core.god.damage.DamageDefinition;
import fr.byob.game.memeduel.core.god.image.ImageDefinition;
import fr.byob.game.memeduel.core.god.layer.LayerDefinition;
import fr.byob.game.memeduel.core.god.update.TransparencyUpdateDefinition;
import fr.byob.game.memeduel.core.god.update.UpdateDefinition;
import fr.byob.game.memeduel.core.model.object.ModelObject;
import fr.byob.game.memeduel.core.view.View;
import fr.byob.game.memeduel.core.view.ZOrder;
import fr.byob.game.memeduel.core.view.handler.image.SunGlowImageHandler;
import fr.byob.game.memeduel.core.view.handler.layer.InterpolateLayerHandler;
import fr.byob.game.memeduel.core.view.object.DynamicViewObject;

public class SunGlowGOD extends DynamicGameObjectDefinition {

	public static class Builder extends DynamicGameObjectDefinition.Builder<SunGlowGOD> {

		@Override
		public Builder fromJSON(final AllGODLoader godLoader, final Object jsonEntity) {
			super.fromJSON(godLoader, jsonEntity);

			final int glowSize = jsonEntity.getInt("glowSize");
			this.updateDefinition(TransparencyUpdateDefinition.builder().alphaFactor(1f / 10f).upperAlpha(1.0f).lowerAlpha(0.2f).build());
			this.layerDefinition(LayerDefinition.builder().layerSize(glowSize, glowSize).layerOrigin(glowSize / 2, glowSize / 2).depth(9).zOrder(ZOrder.DYNAMIC).build());
			return this;
		}

		@Override
		public SunGlowGOD build() {
			return new SunGlowGOD(this.position, this.angle, this.layerDefinition, this.imageDefinition, this.updateDefinition, this.damageDefinition);
		}

	}

	protected SunGlowGOD(final Vector position, final float angle, final LayerDefinition layerDefinition, final ImageDefinition imageDefinition, final UpdateDefinition updateDefinition, final DamageDefinition damageDefinition) {
		super(position, angle, layerDefinition, imageDefinition, updateDefinition, damageDefinition);
	}

	@Override
	public DynamicViewObject newViewObject(final View view, final ModelObject modelObject) {
		final DynamicViewObject viewObject = new DynamicViewObject(modelObject, new SunGlowImageHandler(), new InterpolateLayerHandler());
		viewObject.addToView(view);
		return viewObject;
	}
}
