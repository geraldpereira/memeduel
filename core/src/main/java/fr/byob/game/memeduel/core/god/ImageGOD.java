package fr.byob.game.memeduel.core.god;

import pythagoras.f.Vector;
import fr.byob.game.memeduel.core.GameImage;
import fr.byob.game.memeduel.core.god.image.ImageDefinition;
import fr.byob.game.memeduel.core.god.layer.LayerDefinition;
import fr.byob.game.memeduel.core.view.ZOrder;

public class ImageGOD extends StaticGameObjectDefinition {

	public ImageGOD(final Vector position, final float angle, final GameImage image) {
		super(position, angle, LayerDefinition.builder().layerSize(image).zOrder(ZOrder.BACK).depth(100).build(), ImageDefinition.imageBuilder().image(image).build());
	}

}
