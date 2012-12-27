package fr.byob.game.memeduel.core.god;

import java.util.ArrayList;
import java.util.List;

import playn.core.Json;
import playn.core.Json.Writer;
import pythagoras.f.Dimension;
import fr.byob.game.memeduel.core.god.meme.MemeGOD;
import fr.byob.game.memeduel.core.model.AbstractModel;
import fr.byob.game.memeduel.core.model.EditModel;

public class MemeDuelLevelGOD extends LevelGOD {


	private final MemeDuelGODLoader godLoader;

	private List<String> availableCannonBalls;
	private float maxLifespanSum;

	public MemeDuelLevelGOD(final MemeDuelGODLoader godLoader) {
		this.godLoader = godLoader;
	}

	public List<String> getAvailableCannonBalls() {
		return this.availableCannonBalls;
	}

	public void setAvailableCannonBalls(final List<String> availableCannonBalls) {
		this.availableCannonBalls = availableCannonBalls;
	}

	public float getMaxLifespanSum() {
		return this.maxLifespanSum;
	}

	@Override
	protected MemeDuelLevelGOD loadFromJSON(final Json.Object document) {

		maxLifespanSum = 0;
		if (document.containsKey("maxLifespanSum")) {
			maxLifespanSum = document.getNumber("maxLifespanSum");
		}

		size = new Dimension();
		if (document.containsKey("size")) {
			final Json.Object sizeJSON = document.getObject("size");
			size.setSize(sizeJSON.getNumber("width"), sizeJSON.getNumber("height"));
		}

		final Json.Array gameObjectsJSON = document.getArray("objects");
		gameObjects = new ArrayList<DynamicGameObjectDefinition>(gameObjectsJSON.length());
		for (int i = 0; i < gameObjectsJSON.length(); i++) {
			final Json.Object gameObjectJSON = gameObjectsJSON.getObject(i);
			final String type = gameObjectJSON.getString("type");
			B2DGameObjectDefiniton gameObjectGOD = null;
			if (type.startsWith("MEME")) {
				gameObjectGOD = MemeGOD.memeBuilder().fromJSON(godLoader, gameObjectJSON).build();
			} else if (type.startsWith("OBJECT")) {
				gameObjectGOD = new B2DGameObjectDefiniton.Builder<B2DGameObjectDefiniton>().fromJSON(godLoader, gameObjectJSON).build();
			}
			gameObjects.add(gameObjectGOD);

		}

		availableCannonBalls = new ArrayList<String>();

		if (document.containsKey("cannonBalls")) {
			final Json.Array cannonBallsJSON = document.getArray("cannonBalls");
			for (int i = 0; i < cannonBallsJSON.length(); i++) {
				final Json.Object cannonBallJSON = cannonBallsJSON.getObject(i);
				final String type = cannonBallJSON.getString("type");
				availableCannonBalls.add(type);
			}
		}

		return this;
	}

	@Override
	protected void saveCustom(final Writer writer) {
		writer.value("maxLifespanSum", this.maxLifespanSum);

		if (availableCannonBalls != null && !availableCannonBalls.isEmpty()) {
			writer.array("cannonBalls");
			for (final String cannonBallType : availableCannonBalls) {
				writer.object();
				writer.value("type", cannonBallType);
				writer.end();
			}
			writer.end();
		}
	}

	@Override
	protected void saveFromModelCustom(final AbstractModel model, final Writer writer) {
		if (model instanceof EditModel) {
			maxLifespanSum = ((EditModel) model).getMaxLifespanSum();
		}
		saveCustom(writer);
	}
}
