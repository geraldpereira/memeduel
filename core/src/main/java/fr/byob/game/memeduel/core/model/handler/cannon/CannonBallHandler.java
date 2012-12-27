package fr.byob.game.memeduel.core.model.handler.cannon;

import pythagoras.f.Vector;
import fr.byob.game.memeduel.core.model.AbstractModel;
import fr.byob.game.memeduel.core.model.cannon.CannonBallModelObject;
import fr.byob.game.memeduel.core.model.handler.ModelHandler;

public interface CannonBallHandler extends ModelHandler {

	/**
	 * Applique un bonus au boulet de cannon
	 * 
	 * @param model
	 * @param clickPosition
	 *            position du click souris
	 */
	CannonBallModelObject applyClickBonus(final AbstractModel model, final Vector clickPosition);

}
