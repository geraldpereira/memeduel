package fr.byob.game.memeduel.core.god.helper;

import playn.core.Json.Object;
import fr.byob.game.memeduel.core.god.AllGODLoader;
import fr.byob.game.memeduel.core.god.b2d.B2DBodyDefinition;
import fr.byob.game.memeduel.core.god.damage.DamageDefinition;
import fr.byob.game.memeduel.core.god.image.ImageDefinition;

public abstract class LoadHelper {

	protected String type;
	protected B2DBodyDefinition bodyDefinition;
	protected ImageDefinition imageDefinition;
	protected DamageDefinition damageDefinition;

	public void load(final AllGODLoader loader, final Object jsonEntity) {
		type = jsonEntity.getString("type");
		bodyDefinition = B2DBodyDefinition.builder().fromJSON(loader, jsonEntity).build();
	}

	public String getType() {
		return type;
	}

	public B2DBodyDefinition getB2DBodyDefinition() {
		return bodyDefinition;
	}

	public DamageDefinition getDamageDefinition() {
		return damageDefinition;
	}

	public ImageDefinition getImageDefinition() {
		return imageDefinition;
	}

	/**
	 * Does this object has a fiexed shape ? Or can it be customized ?
	 * 
	 * @return
	 */
	// public boolean fixedShape();

}
