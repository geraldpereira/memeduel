package fr.byob.game.memeduel.core.view.handler.layer;

import pythagoras.f.Vector;
import fr.byob.game.memeduel.core.GamePool;
import fr.byob.game.memeduel.core.model.handler.update.UpdateHandler;
import fr.byob.game.memeduel.core.view.object.DynamicViewObject;

public class InterpolateLayerHandler extends AbstractLayerHandler<DynamicViewObject> {


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

		// interpolate based on previous state
		final float x = position.x * alpha + previousPosition.x * (1f - alpha);
		final float y = position.y * alpha + previousPosition.y * (1f - alpha);
		final float a = angle * alpha + previousAngle * (1f - alpha);
		this.layer.setTranslation(x, y);
		this.layer.setRotation(a);
		this.layer.setAlpha(transparency);

		GamePool.instance().pushVector(2);
	}

}
