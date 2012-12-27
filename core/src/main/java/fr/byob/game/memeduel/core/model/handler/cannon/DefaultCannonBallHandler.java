package fr.byob.game.memeduel.core.model.handler.cannon;

import pythagoras.f.Vector;
import fr.byob.game.box2d.dynamics.Body;
import fr.byob.game.memeduel.core.model.AbstractModel;
import fr.byob.game.memeduel.core.model.cannon.CannonBallModelObject;

public class DefaultCannonBallHandler extends AbstractCannonBallHandler {

	@Override
	protected CannonBallModelObject applyClickBonus(final AbstractModel model, final float distance) {
		final Body body = this.modelObject.getB2DBodyHandler().getBody();
		final Vector linearVelocity = body.getLinearVelocity();
		float multFactor = 1;

		final float inShapeDistance = Math.max(this.modelObject.getGOD().getShapeDefition().getSize().width, this.modelObject.getGOD().getShapeDefition().getSize().height);
		if (distance < inShapeDistance) {
			// Click direct sur le boulet, super bonus !
			multFactor = 3;
		} else {
			// Sinon un bonus entre 1.2 et 2
			multFactor = 2 / (distance / 3);
			if (multFactor > 2) {
				multFactor = 2;
			} else if (multFactor < 1.2) {
				multFactor = 1.2f;
			}
		}

		// TODO PAS bon !!! il faut faire un setLinearVelocity !
		linearVelocity.scaleLocal(multFactor);
		return this.modelObject;
	}

}
