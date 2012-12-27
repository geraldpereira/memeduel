package fr.byob.game.memeduel.core.model.handler.b2d;

import fr.byob.game.box2d.dynamics.Body;
import fr.byob.game.box2d.dynamics.World;
import fr.byob.game.memeduel.core.model.handler.AbstractModelHandler;
import fr.byob.game.memeduel.core.model.object.B2DModelObject;

public abstract class AbstractBodyHandler extends AbstractModelHandler<B2DModelObject> implements B2DBodyHandler {
	protected Body body;

	@Override
	public Body getBody() {
		return this.body;
	}

	@Override
	public void removeBody(final World world) {
		world.destroyBody(this.body);
	}

}
