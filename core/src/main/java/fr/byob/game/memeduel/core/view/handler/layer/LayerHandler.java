package fr.byob.game.memeduel.core.view.handler.layer;

import playn.core.ImageLayer;
import fr.byob.game.memeduel.core.view.View;
import fr.byob.game.memeduel.core.view.handler.ViewHandler;

public interface LayerHandler extends ViewHandler {

	public void initLayer();

	public void repaint();

	public void addToView(final View view);

	public void removeFromView(final View view);

	public void paint(float alpha);

	public ImageLayer getLayer();

}
