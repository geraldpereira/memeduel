package fr.byob.game.memeduel.core.view.handler.layer;

import pythagoras.f.Vector;
import fr.byob.game.memeduel.core.GamePool;
import fr.byob.game.memeduel.core.model.handler.update.UpdateHandler;
import fr.byob.game.memeduel.core.view.object.DynamicViewObject;

/**
 * CElui la est très spécial car appellé depuis le update de la vue!
 * 
 * @author Kojiro
 * 
 */
public class PerspectiveLayerHandler extends AbstractLayerHandler<DynamicViewObject> {


	@Override
	public void paint(final float alpha) {
		final UpdateHandler updateHandler = this.viewObject.getModelObject().getUpdateHandler();
		final Vector position = GamePool.instance().popVector();
		updateHandler.getCurrentPositionToOut(position);
		this.layer.setTranslation(position.x, position.y);
		GamePool.instance().pushVector(1);
	}

}
