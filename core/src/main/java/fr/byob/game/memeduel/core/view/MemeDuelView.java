package fr.byob.game.memeduel.core.view;

import java.util.ArrayList;
import java.util.List;

import pythagoras.f.Vector;
import fr.byob.game.memeduel.core.GameImage;
import fr.byob.game.memeduel.core.MemeDuelLoader;
import fr.byob.game.memeduel.core.god.ImageGOD;
import fr.byob.game.memeduel.core.god.MemeDuelGODLoader;
import fr.byob.game.memeduel.core.model.AbstractModel;
import fr.byob.game.memeduel.core.model.PlayModel;
import fr.byob.game.memeduel.core.model.cannon.CannonListener;
import fr.byob.game.memeduel.core.model.cannon.CannonModelObject;
import fr.byob.game.memeduel.core.model.object.ModelObject;
import fr.byob.game.memeduel.core.view.object.DynamicViewObject;
import fr.byob.game.memeduel.core.view.object.StaticViewObject;
import fr.byob.game.memeduel.core.view.object.ViewObject;

public class MemeDuelView extends AbstractView implements CannonListener {

	private final List<StaticViewObject> cannonBallTrace = new ArrayList<StaticViewObject>(0);

	public MemeDuelView(final AbstractModel model) {
		super(model);
	}

	@Override
	public void modelLoaded() {
		super.modelLoaded();

		final MemeDuelGODLoader godLoader = (MemeDuelGODLoader) this.model.getGodLoader();

		if (godLoader.getGameOptionsGOD().getCannonGOD() != null) {
			final CannonModelObject cannon = ((PlayModel) this.model).getCannon();
			final ModelObject cannonModelObject = cannon.getCannonModelObject();
			this.dynamicViewObjects.put(cannonModelObject.getId(), (DynamicViewObject) cannonModelObject.getGOD().newViewObject(this, cannonModelObject));

			final ModelObject boltModelObject = cannon.getBoltModelObject();
			this.dynamicViewObjects.put(boltModelObject.getId(), (DynamicViewObject) boltModelObject.getGOD().newViewObject(this, boltModelObject));

			final ModelObject whellModelObject = cannon.getWheelModelObject();
			this.dynamicViewObjects.put(whellModelObject.getId(), (DynamicViewObject) whellModelObject.getGOD().newViewObject(this, whellModelObject));

			final ModelObject baseModelObject = cannon.getBaseModelObject();
			this.dynamicViewObjects.put(baseModelObject.getId(), (DynamicViewObject) baseModelObject.getGOD().newViewObject(this, baseModelObject));
		}
	}

	@Override
	public void cannonBallDamaged() {
	}

	@Override
	public void cannonBallFired(final Vector position, final float angle) {
		for (final ViewObject vo : this.cannonBallTrace) {
			vo.removeFromView(this);
		}

		this.cannonBallTrace.clear();
	}

	private int imgNumber = 0;

	@Override
	public void cannonBallMoved(final Vector position, final float angle) {
		// Dessiner des petits rond de fumée
		final ImageGOD god = new ImageGOD(position, angle, GameImage.valueOf("CANNON_BALL_TRACE_" + this.imgNumber));
		this.cannonBallTrace.add((StaticViewObject) god.newViewObject(this, null));
		this.imgNumber++;
		if (this.imgNumber > 3) {
			this.imgNumber = 0;
		}
	}

	@Override
	public void cannonBallBonusActivated(final Vector position, final float angle) {
		// Dessiner un gros rond de fumée
		final ImageGOD god = new ImageGOD(position, angle, MemeDuelLoader.CANNON_BALL_TRACE_BONUS);
		this.cannonBallTrace.add((StaticViewObject) god.newViewObject(this, null));
	}
}
