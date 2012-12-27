package fr.byob.game.memeduel.core.editor;

import pythagoras.f.Vector;
import fr.byob.game.memeduel.core.god.B2DGameObjectDefiniton;

public interface B2DGODProvider<T extends B2DGameObjectDefiniton> {
	public T getGOD(Vector position, float angle);
}
