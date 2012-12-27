package fr.byob.game.memeduel.core.editor;

import fr.byob.game.memeduel.core.god.cannon.CannonBallGOD;

public interface CannonBallGODProvider extends B2DGODProvider<CannonBallGOD> {

	public boolean isEmpty();

	public String getCurrentType();

	public int getRemainingCannonBallCount();
}
