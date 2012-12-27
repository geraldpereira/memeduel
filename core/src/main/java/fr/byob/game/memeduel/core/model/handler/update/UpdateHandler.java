package fr.byob.game.memeduel.core.model.handler.update;

import pythagoras.f.Vector;
import fr.byob.game.memeduel.core.god.AllGODLoader;
import fr.byob.game.memeduel.core.model.handler.ModelHandler;

public interface UpdateHandler extends ModelHandler {

	public void init(AllGODLoader godLoader);

	public void update(float delta);

	public void getPreviousPositionToOut(final Vector out);

	public float getPreviousRotation();

	public void getCurrentPositionToOut(final Vector out);

	public float getCurrentRotation();

	public float getCurrentTransparency();
}
