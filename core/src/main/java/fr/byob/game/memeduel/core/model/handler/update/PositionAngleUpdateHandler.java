package fr.byob.game.memeduel.core.model.handler.update;

import pythagoras.f.Vector;
import fr.byob.game.memeduel.core.god.update.PositionAngleUpdateDefinition;
import fr.byob.game.memeduel.core.model.object.DefaultModelObject;

public class PositionAngleUpdateHandler extends AbstractUpdateHandler<DefaultModelObject> {

	@Override
	public void update(final float delta) {
		super.update(delta);
		final float deltaTime = 1 / delta;
		final PositionAngleUpdateDefinition positionAndAngleUpdateDefinition = (PositionAngleUpdateDefinition) this.modelObject.getGOD().getUpdateDefinition();
		final Vector positionOffset = positionAndAngleUpdateDefinition.getPositionOffset();
		final float angleOffset = positionAndAngleUpdateDefinition.getAngleOffset();
		this.currentPosition.x += positionOffset.x * deltaTime;
		this.currentPosition.y += positionOffset.y * deltaTime;
		this.currentAngle += angleOffset * deltaTime;
	}
}
