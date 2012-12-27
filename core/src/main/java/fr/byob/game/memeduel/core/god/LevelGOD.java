package fr.byob.game.memeduel.core.god;

import java.util.List;

import playn.core.Json.Writer;
import pythagoras.f.Dimension;
import pythagoras.f.Vector;
import fr.byob.game.box2d.dynamics.Body;
import fr.byob.game.memeduel.core.GamePool;
import fr.byob.game.memeduel.core.model.AbstractModel;
import fr.byob.game.memeduel.core.model.object.B2DModelObject;
import fr.byob.game.memeduel.core.model.object.ModelObject;

public abstract class LevelGOD extends GODLoadable<LevelGOD> implements GameObjectDefinition {

	protected Dimension size;

	protected List<DynamicGameObjectDefinition> gameObjects;

	protected LevelGOD() {
		super();
	}

	public List<DynamicGameObjectDefinition> getGameObjects() {
		return this.gameObjects;
	}

	public Dimension getSize() {
		return size;
	}

	public final void save(final Writer writer) {
		writer.object();

		saveCustom(writer);

		final Vector minPosition = GamePool.instance().popVector();
		minPosition.set(Float.MAX_VALUE, Float.MAX_VALUE);

		for (final DynamicGameObjectDefinition god : gameObjects) {
			if (god instanceof B2DGameObjectDefiniton) {
				minPosition.x = Math.min(minPosition.x, god.getPosition().x);
				minPosition.y = Math.min(minPosition.y, god.getPosition().y);
			}
		}

		writer.object("size");
		writer.value("width", size.width);
		writer.value("height", size.height);
		writer.end();

		writer.array("objects");

		for (final DynamicGameObjectDefinition god : gameObjects) {
			if (god instanceof B2DGameObjectDefiniton) {
				((B2DGameObjectDefiniton) god).save(writer, minPosition);
			}
		}

		writer.end();
		writer.end();

		GamePool.instance().pushVector(1);
	}

	public final boolean saveFomModel(final AbstractModel model, final Writer writer) {
		if (!model.isSleeping()) {
			return false;
		}
		writer.object();

		saveFromModelCustom(model, writer);

		final Vector minPosition = GamePool.instance().popVector().set(Float.MAX_VALUE, Float.MAX_VALUE);
		final Vector maxPosition = GamePool.instance().popVector().set(0, 0);

		final Vector position = GamePool.instance().popVector();

		for (final ModelObject modelObject : model.getModelObjects()) {
			if (modelObject instanceof B2DModelObject) {
				final B2DModelObject b2DModelObject = (B2DModelObject) modelObject;
				b2DModelObject.getUpdateHandler().getCurrentPositionToOut(position);
				minPosition.x = Math.min(minPosition.x, position.x);
				minPosition.y = Math.min(minPosition.y, position.y);

				maxPosition.x = Math.max(maxPosition.x, position.x);
				maxPosition.y = Math.max(maxPosition.y, position.y);
			}
		}

		final float width = maxPosition.x - minPosition.x;
		final float height = model.getGodLoader().getGameGOD().getWorld().getGroundYPosition() - minPosition.y;

		writer.object("size");
		writer.value("width", width);
		writer.value("height", height);
		writer.end();

		writer.array("objects");

		for (final Body curB2Body : model.getB2World().getBodyList()) {
			final Object userData = curB2Body.getUserData();
			if (userData != null && userData instanceof B2DModelObject) {
				final B2DModelObject modelObject = (B2DModelObject) userData;
				modelObject.getGOD().saveFromModel(modelObject, minPosition, writer);
			}
		}

		writer.end();
		writer.end();

		GamePool.instance().pushVector(3);
		return true;
	}

	protected abstract void saveCustom(final Writer writer);

	protected abstract void saveFromModelCustom(final AbstractModel model, final Writer writer);

}
