package fr.byob.game.memeduel.core.model.handler.update;

import pythagoras.f.Vector;
import fr.byob.game.box2d.dynamics.Body;
import fr.byob.game.memeduel.core.GamePool;
import fr.byob.game.memeduel.core.MathUtils;

/**
 * Met à jour la position et l'angle du handler en fonction de celles du Box2D
 * Body
 * 
 * @author gpereira
 * 
 */
public class RocketUpdateHandler extends B2DUpdateHandler {

	@Override
	public void update(final float delta) {
		super.update(delta);
		// Tant que la rocket n'est pas endommagée, sa rotation suis l'axe de sa
		// trajectoire
		if (!this.modelObject.getDamageHandler().isDamaged()) {
			final Body body = this.modelObject.getB2DBodyHandler().getBody();
			final UpdateHandler updateHandler = this.modelObject.getUpdateHandler();
			final Vector current = GamePool.instance().popVector();
			updateHandler.getCurrentPositionToOut(current);
			final Vector previous = GamePool.instance().popVector();
			updateHandler.getPreviousPositionToOut(previous);
			final float angle = (float) MathUtils.computeAngleRadians(current, previous);
			GamePool.instance().pushVector(2);
			body.setTransform(body.getPosition(), angle);
			// Un body avec un fixed rotation donne un angle de 0, il faut donc
			// le surcharger !
			this.currentAngle = angle;
		}
	}

}
