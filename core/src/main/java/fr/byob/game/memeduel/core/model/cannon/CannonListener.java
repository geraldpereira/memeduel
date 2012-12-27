package fr.byob.game.memeduel.core.model.cannon;

import pythagoras.f.Vector;

public interface CannonListener {
	public void cannonBallDamaged();

	public void cannonBallFired(Vector position, float angle);

	public void cannonBallMoved(Vector position, float angle);

	public void cannonBallBonusActivated(Vector position, float angle);
}
