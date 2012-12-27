package fr.byob.game.memeduel.core.model.handler.update;

import fr.byob.game.memeduel.core.god.AllGODLoader;
import fr.byob.game.memeduel.core.god.image.AnimationImageDefinition;


public class AnimationUpdateHandler extends PositionAngleUpdateHandler {

	private float timeBetweenFrames;
	private float timeSinceLastFrame;

	@Override
	public void init(final AllGODLoader godLoader) {
		super.init(godLoader);
		this.timeBetweenFrames = 1 / ((AnimationImageDefinition) this.modelObject.getGOD().getImageDefinition()).getFps();
		this.timeSinceLastFrame = this.timeBetweenFrames;
	}

	@Override
	public void update(final float delta) {
		super.update(delta);
		final float deltaTime = 1 / delta;

		this.timeSinceLastFrame -= deltaTime;
		if (this.timeSinceLastFrame <= 0) {
			this.timeSinceLastFrame = this.timeBetweenFrames;
			this.modelObject.invalidate();
		}

	}
}
