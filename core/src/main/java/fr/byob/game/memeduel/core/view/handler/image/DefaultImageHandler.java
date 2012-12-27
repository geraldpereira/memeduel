package fr.byob.game.memeduel.core.view.handler.image;

import playn.core.Image;
import fr.byob.game.memeduel.core.view.handler.AbstractViewHandler;
import fr.byob.game.memeduel.core.view.object.ViewObject;

public class DefaultImageHandler extends AbstractViewHandler<ViewObject> implements ImageHandler {

	@Override
	public Image createImage() {
		return this.viewObject.getGOD().getImageDefinition().getImage().getImage();
	}
}
