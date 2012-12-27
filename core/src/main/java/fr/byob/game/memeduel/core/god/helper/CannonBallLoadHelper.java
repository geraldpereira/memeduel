package fr.byob.game.memeduel.core.god.helper;

import java.util.ArrayList;
import java.util.List;

import playn.core.Json.Object;
import fr.byob.game.memeduel.core.GameImage;
import fr.byob.game.memeduel.core.god.AllGODLoader;
import fr.byob.game.memeduel.core.god.GameObjectDefinition;
import fr.byob.game.memeduel.core.god.b2d.ShapeDefinition;
import fr.byob.game.memeduel.core.god.b2d.ShapeDefinition.Shape;
import fr.byob.game.memeduel.core.god.cannon.CannonBallDefinition;
import fr.byob.game.memeduel.core.god.damage.DecayDefinition;
import fr.byob.game.memeduel.core.god.damage.LifespanDefinition;
import fr.byob.game.memeduel.core.god.damage.LifespanDefinition.DamageSlice;
import fr.byob.game.memeduel.core.god.image.ImageDefinition;

public class CannonBallLoadHelper extends LoadHelper implements GameObjectDefinition {

	protected final static String DMG = "_DAMAGED";

	private ShapeDefinition shapeDefinition;
	private CannonBallDefinition cannonBallDefinition;

	public CannonBallLoadHelper() {
	}


	@Override
	public DecayDefinition getDamageDefinition() {
		return (DecayDefinition) damageDefinition;
	}

	public ShapeDefinition getShapeDefinition() {
		return shapeDefinition;
	}

	public CannonBallDefinition getCannonBallDefinition() {
		return cannonBallDefinition;
	}

	@Override
	public void load(final AllGODLoader loader, final Object jsonEntity) {
		super.load(loader, jsonEntity);

		imageDefinition = ImageDefinition.imageBuilder().fromJSON(loader, jsonEntity).build();

		final String imageName = imageDefinition.getImage().name();
		final List<DamageSlice> damageSlices = new ArrayList<LifespanDefinition.DamageSlice>();
		damageSlices.add(new DamageSlice(99f, GameImage.valueOf(imageName + DMG)));

		damageDefinition = DecayDefinition.decayBuilder().fromJSON(loader, jsonEntity).damageSlices(damageSlices).build();

		final String shapeStr = jsonEntity.getString("shape");
		final Shape shape = Shape.valueOf(shapeStr);
		shapeDefinition = shape.loadShapeDefinition(jsonEntity);

		cannonBallDefinition = CannonBallDefinition.builder().fromJSON(loader, jsonEntity).build();
	}

}
