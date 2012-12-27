package fr.byob.game.memeduel.core.model.handler.b2d;

import fr.byob.game.box2d.dynamics.Body;
import fr.byob.game.box2d.dynamics.World;
import fr.byob.game.memeduel.core.model.handler.ModelHandler;

public interface B2DBodyHandler extends ModelHandler {

	public void addBody(World world);

	public void removeBody(World world);

	public Body getBody();

}
