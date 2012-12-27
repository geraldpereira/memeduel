package fr.byob.game.memeduel.core.model.handler.update;

import pythagoras.f.Vector;
import fr.byob.game.memeduel.core.ViewUtils;
import fr.byob.game.memeduel.core.god.AllGODLoader;
import fr.byob.game.memeduel.core.god.CloudGOD;
import fr.byob.game.memeduel.core.model.object.DefaultModelObject;

public class CloudUpdateHandler extends AbstractUpdateHandler<DefaultModelObject> {

	private float worldWidth;

	@Override
	public void init(final AllGODLoader godLoader) {
		super.init(godLoader);
		this.worldWidth = godLoader.getGameGOD().getWorld().getWidth();
	}

	@Override
	public void update(final float delta) {
		super.update(delta);
		final CloudGOD god = (CloudGOD) this.modelObject.getGOD();
		final Vector positionOffset = god.getUpdateDefinition().getPositionOffset();
		final float layerWidth = ViewUtils.toInitModel(god.getLayerDefinition().getLayerSize().width);

		final float deltaTime = 1 / delta;

		this.currentPosition.x += positionOffset.x * deltaTime;
		if (this.currentPosition.x > this.worldWidth) {
			this.currentPosition.x = -layerWidth;
		} else if (this.currentPosition.x < -layerWidth) {
			this.currentPosition.x = this.worldWidth;
		}
		this.currentPosition.y = (float) (this.initialPosition.y + positionOffset.y * Math.sin(this.currentPosition.x / 2) * deltaTime);
	}
}
