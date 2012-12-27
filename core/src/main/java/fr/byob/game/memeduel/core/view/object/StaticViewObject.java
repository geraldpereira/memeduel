package fr.byob.game.memeduel.core.view.object;

import fr.byob.game.memeduel.core.god.StaticGameObjectDefinition;
import fr.byob.game.memeduel.core.view.handler.image.ImageHandler;
import fr.byob.game.memeduel.core.view.handler.layer.LayerHandler;

public class StaticViewObject extends AbstractViewObject {

	private final StaticGameObjectDefinition god;

	public StaticViewObject(final StaticGameObjectDefinition god, final ImageHandler imageHandler, final LayerHandler layerHandler) {
		super(imageHandler, layerHandler);
		this.god = god;
	}


	@Override
	public StaticGameObjectDefinition getGOD() {
		return this.god;
	}

}
