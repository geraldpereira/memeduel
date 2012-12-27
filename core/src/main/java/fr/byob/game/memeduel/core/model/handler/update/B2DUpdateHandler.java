package fr.byob.game.memeduel.core.model.handler.update;

import fr.byob.game.box2d.dynamics.Body;
import fr.byob.game.memeduel.core.model.object.B2DModelObject;

/**
 * Met à jour la position et l'angle du handler en fonction de celles du Box2D
 * Body
 * 
 * @author gpereira
 * 
 */
public class B2DUpdateHandler extends AbstractUpdateHandler<B2DModelObject> {

	@Override
	public void update(final float delta) {
		super.update(delta);
		final Body body = this.modelObject.getB2DBodyHandler().getBody();
		this.currentPosition.set(body.getPosition());
		this.currentAngle = body.getAngle();
	}

}
