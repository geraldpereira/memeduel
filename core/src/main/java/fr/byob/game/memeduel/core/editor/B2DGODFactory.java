package fr.byob.game.memeduel.core.editor;

import pythagoras.f.Rectangle;
import pythagoras.f.Vector;
import fr.byob.game.memeduel.core.god.B2DGameObjectDefiniton;

public interface B2DGODFactory<T extends B2DGameObjectDefiniton> extends B2DGODProvider<T> {

	public void setType(String type);

	public void handleClick(final Vector position);

	public void setCanvasViewport(final Rectangle canvasViewport);

	public void draw();
}
