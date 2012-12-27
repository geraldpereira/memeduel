package fr.byob.game.memeduel.core.god;

import playn.core.Json;
import fr.byob.game.memeduel.core.god.cannon.CannonGOD;
import fr.byob.game.memeduel.core.god.helper.CannonBallLoadHelper;
import fr.byob.game.memeduel.core.god.helper.LoadHelper;
import fr.byob.game.memeduel.core.god.helper.MemeLoadHelper;
import fr.byob.game.memeduel.core.god.helper.ObjectLoadHelper;

public class MemeDuelGameOptionsGOD extends GameOptionsGOD {

	private CannonGOD cannonGOD;

	private final MemeDuelGODLoader godLoader;

	public MemeDuelGameOptionsGOD(final MemeDuelGODLoader godLoader) {
		this.godLoader = godLoader;
	}

	@Override
	protected MemeDuelGameOptionsGOD loadFromJSON(final Json.Object jsonEntity) {
		xOffset = 0;
		if (jsonEntity.containsKey("xOffset")) {
			xOffset = jsonEntity.getNumber("xOffset");
		}

		cannonGOD = null;
		if (jsonEntity.containsKey("cannon")) {
			final Json.Object cannonEntity = jsonEntity.getObject("cannon");
			cannonGOD = CannonGOD.builder().fromJSON(godLoader, cannonEntity).build();
		}

		final Json.Array memeTypesJSON = jsonEntity.getArray("memeTypes");
		for (int i = 0; i < memeTypesJSON.length(); i++) {
			final LoadHelper helper = new MemeLoadHelper();
			helper.load(godLoader, memeTypesJSON.getObject(i));
			helpers.put(helper.getType(), helper);
		}

		final Json.Array objectTypesJSON = jsonEntity.getArray("objectTypes");
		for (int i = 0; i < objectTypesJSON.length(); i++) {
			final LoadHelper helper = new ObjectLoadHelper();
			helper.load(godLoader, objectTypesJSON.getObject(i));
			helpers.put(helper.getType(), helper);
		}

		if (jsonEntity.containsKey("cannonBallTypes")) {
			final Json.Array cannonBallTypesJSON = jsonEntity.getArray("cannonBallTypes");
			for (int i = 0; i < cannonBallTypesJSON.length(); i++) {
				final LoadHelper helper = new CannonBallLoadHelper();
				helper.load(godLoader, cannonBallTypesJSON.getObject(i));
				helpers.put(helper.getType(), helper);
			}
		}

		return this;
	}

	public CannonGOD getCannonGOD() {
		return this.cannonGOD;
	}

}
