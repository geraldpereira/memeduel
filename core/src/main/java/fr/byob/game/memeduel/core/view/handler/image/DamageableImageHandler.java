package fr.byob.game.memeduel.core.view.handler.image;

import playn.core.Image;
import fr.byob.game.memeduel.core.GameImage;
import fr.byob.game.memeduel.core.model.handler.damage.LifespanDamageHandler;
import fr.byob.game.memeduel.core.model.object.ModelObject;
import fr.byob.game.memeduel.core.view.handler.AbstractViewHandler;
import fr.byob.game.memeduel.core.view.object.DynamicViewObject;

public class DamageableImageHandler extends AbstractViewHandler<DynamicViewObject> implements ImageHandler {

	@Override
	public Image createImage() {
		final ModelObject modelObject = this.viewObject.getModelObject();
		if (modelObject.isDamageable() && modelObject.getDamageHandler() instanceof LifespanDamageHandler) {
			final LifespanDamageHandler damageHandler = (LifespanDamageHandler) modelObject.getDamageHandler();
			final GameImage damageImage = damageHandler.getDamageMDImage();
			if (damageImage != null){
				return damageImage.getImage();
			}
		}
		return this.viewObject.getGOD().getImageDefinition().getImage().getImage();
	}
}