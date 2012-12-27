package fr.byob.game.memeduel.core.view.handler.layer;

import pythagoras.f.Vector;
import fr.byob.game.memeduel.core.GamePool;
import fr.byob.game.memeduel.core.model.handler.update.UpdateHandler;
import fr.byob.game.memeduel.core.view.object.DynamicViewObject;

public class ImmediateLayerHandler extends AbstractLayerHandler<DynamicViewObject> {


	@Override
	public void paint(final float alpha) {
		final UpdateHandler updateHandler = this.viewObject.getModelObject().getUpdateHandler();
		final Vector position = GamePool.instance().popVector();
		updateHandler.getCurrentPositionToOut(position);
		final Vector previousPosition = GamePool.instance().popVector();
		updateHandler.getPreviousPositionToOut(previousPosition);
		final float angle = updateHandler.getCurrentRotation();
		final float previousAngle = updateHandler.getPreviousRotation();
		final float transparency = updateHandler.getCurrentTransparency();

		// Watch out ! l'interpolation ne fonctionne correctement que pour les
		// objets avec une faible vélocité
		// Ici on repart à chaque fois de zero pour éviter les erreurs
		// d'appreciation, cela évite que l'affichage du boulet soit saccadé.
		this.layer.setTranslation(-previousPosition.x, -previousPosition.y);
		this.layer.setTranslation(position.x, position.y);
		this.layer.setRotation(-previousAngle);
		this.layer.setRotation(angle);
		this.layer.setAlpha(transparency);

		GamePool.instance().pushVector(2);
	}

}
