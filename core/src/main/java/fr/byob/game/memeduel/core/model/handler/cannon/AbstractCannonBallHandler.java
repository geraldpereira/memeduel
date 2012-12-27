package fr.byob.game.memeduel.core.model.handler.cannon;

import pythagoras.f.Vector;
import fr.byob.game.memeduel.core.GamePool;
import fr.byob.game.memeduel.core.model.AbstractModel;
import fr.byob.game.memeduel.core.model.cannon.CannonBallModelObject;
import fr.byob.game.memeduel.core.model.handler.AbstractModelHandler;

public abstract class AbstractCannonBallHandler extends AbstractModelHandler<CannonBallModelObject> implements CannonBallHandler {
	private boolean bonusApplied = false;

	/**
	 * Applique un bonus au boulet de cannon
	 * 
	 * @param models
	 * @param clickPosition
	 *            position du click souris
	 */
	@Override
	public CannonBallModelObject applyClickBonus(final AbstractModel model, final Vector clickPosition) {
		if (this.bonusApplied) {
			// On ne peut appliquer les bonus qu'une fois par boulet
			return null;
		}
		final Vector bodyPosition = GamePool.instance().popVector();
		this.modelObject.getUpdateHandler().getPreviousPositionToOut(bodyPosition);
		final float distance = bodyPosition.distance(clickPosition);
		GamePool.instance().pushVector(1);
		final CannonBallModelObject cannonBallModelObject = this.applyClickBonus(model, distance);
		this.bonusApplied = true;
		return cannonBallModelObject;
	}

	protected abstract CannonBallModelObject applyClickBonus(final AbstractModel model, final float distance);
}
