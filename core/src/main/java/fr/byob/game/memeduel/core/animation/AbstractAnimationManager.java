package fr.byob.game.memeduel.core.animation;

import pythagoras.f.Dimension;
import pythagoras.f.Vector;
import fr.byob.game.memeduel.core.GameImage;
import fr.byob.game.memeduel.core.Updatable;
import fr.byob.game.memeduel.core.god.AnimationGOD;
import fr.byob.game.memeduel.core.god.damage.DurationDefinition;
import fr.byob.game.memeduel.core.god.image.AnimationImageDefinition;
import fr.byob.game.memeduel.core.god.layer.LayerDefinition;
import fr.byob.game.memeduel.core.god.update.PositionAngleUpdateDefinition;
import fr.byob.game.memeduel.core.model.AbstractModel;
import fr.byob.game.memeduel.core.model.ModelListener;
import fr.byob.game.memeduel.core.model.handler.update.AnimationUpdateHandler;
import fr.byob.game.memeduel.core.model.object.DefaultModelObject;
import fr.byob.game.memeduel.core.model.object.ModelObject;
import fr.byob.game.memeduel.core.view.View;
import fr.byob.game.memeduel.core.view.ZOrder;
import fr.byob.game.memeduel.core.view.handler.layer.InterpolateLayerHandler;
import fr.byob.game.memeduel.core.view.object.DynamicViewObject;
import fr.byob.game.memeduel.core.view.object.ViewObject;

public abstract class AbstractAnimationManager implements ModelListener, Updatable {
	protected AnimationGOD newMovingAnimationGOD(final Vector position, final float angle, final int frameCount, final int fps, final Dimension size, final GameImage image, final Vector positionUpdate, final float angleUpdate) {
		// Nombre d'images par seconde
		final AnimationImageDefinition animationDefinition = AnimationImageDefinition.animBuilder().imageSize(size).frameCount(frameCount).fps(fps).image(image).build();
		final LayerDefinition layerDefinition = LayerDefinition.builder().layerSize(size).depth(90).zOrder(ZOrder.DYNAMIC).build();
		final DurationDefinition damageDefinition = DurationDefinition.builder().duration((float) frameCount / fps).build();
		final PositionAngleUpdateDefinition updateDefinition = PositionAngleUpdateDefinition.builder().positionOffset(positionUpdate).angleOffset(angleUpdate).build();
		final AnimationGOD god = new AnimationGOD(position, angle, layerDefinition, animationDefinition, updateDefinition, damageDefinition) {
			@Override
			public DefaultModelObject newModelObject(final AbstractModel model) {
				final DefaultModelObject modelObject = new DefaultModelObject(model.newId(), this, new AnimationUpdateHandler(), this.damageDefinition.newModelHandler());
				modelObject.init(model);
				return modelObject;
			}

			@Override
			public ViewObject newViewObject(final View view, final ModelObject modelObject) {
				final DynamicViewObject viewObject = new DynamicViewObject(modelObject, this.imageDefinition.newViewHandler(), new InterpolateLayerHandler());
				viewObject.addToView(view);
				return viewObject;
			}
		};
		return god;
	}

	protected AnimationGOD newStaticAnimationGOD(final Vector position, final float angle, final int frameCount, final int fps, final Dimension size, final Vector layerOrigin, final GameImage image) {
		// Nombre d'images par seconde
		final AnimationImageDefinition animationDefinition = AnimationImageDefinition.animBuilder().imageSize(size).frameCount(frameCount).fps(fps).image(image).build();
		final LayerDefinition layerDefinition = LayerDefinition.builder().layerSize(size).layerOrigin(layerOrigin).depth(90).zOrder(ZOrder.DYNAMIC).build();
		final DurationDefinition damageDefinition = DurationDefinition.builder().duration((float) frameCount / fps).build();
		final PositionAngleUpdateDefinition updateDefinition = PositionAngleUpdateDefinition.builder().build();
		final AnimationGOD god = new AnimationGOD(position, angle, layerDefinition, animationDefinition, updateDefinition, damageDefinition) {
			@Override
			public DefaultModelObject newModelObject(final AbstractModel model) {
				final DefaultModelObject modelObject = new DefaultModelObject(model.newId(), this, new AnimationUpdateHandler(), this.damageDefinition.newModelHandler());
				modelObject.init(model);
				return modelObject;
			}

			@Override
			public ViewObject newViewObject(final View view, final ModelObject modelObject) {
				final DynamicViewObject viewObject = new DynamicViewObject(modelObject, this.imageDefinition.newViewHandler(), this.layerDefinition.newViewHandler());
				viewObject.addToView(view);
				return viewObject;
			}
		};
		return god;
	}

	public void clear() {
	};
}
