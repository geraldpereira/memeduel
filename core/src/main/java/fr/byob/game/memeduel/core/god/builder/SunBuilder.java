package fr.byob.game.memeduel.core.god.builder;

import playn.core.Json.Object;
import fr.byob.game.memeduel.core.GameImage;
import fr.byob.game.memeduel.core.god.AllGODLoader;
import fr.byob.game.memeduel.core.god.StaticGameObjectDefinition;
import fr.byob.game.memeduel.core.god.image.ImageDefinition;
import fr.byob.game.memeduel.core.god.layer.LayerDefinition;
import fr.byob.game.memeduel.core.view.ZOrder;

public class SunBuilder extends StaticGameObjectDefinition.Builder<StaticGameObjectDefinition> {

	@Override
	public SunBuilder fromJSON(final AllGODLoader godLoader, final Object jsonEntity) {
		super.fromJSON(godLoader, jsonEntity);
		this.imageDefinition(ImageDefinition.imageBuilder().fromJSON(godLoader, jsonEntity).build());
		final GameImage image = this.imageDefinition.getImage();
		this.layerDefinition(LayerDefinition.builder().layerSize(image).layerOrigin(image).depth(10).zOrder(ZOrder.DYNAMIC).build());
		return this;
	}

}
