package fr.byob.game.memeduel.core.view.object;

import fr.byob.game.memeduel.core.god.StaticGameObjectDefinition;
import fr.byob.game.memeduel.core.view.View;
import fr.byob.game.memeduel.core.view.handler.image.ImageHandler;
import fr.byob.game.memeduel.core.view.handler.layer.LayerHandler;

public interface ViewObject {

	public void addToView(final View view);

	public void removeFromView(final View view);

	public ImageHandler getImageHandler();

	public LayerHandler getLayerHandler();

	public void paint(final float alpha);

	public StaticGameObjectDefinition getGOD();
}
