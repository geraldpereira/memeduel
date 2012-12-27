package fr.byob.game.memeduel.core.model.handler.cannon;

import pythagoras.f.Vector;
import fr.byob.game.box2d.dynamics.Body;
import fr.byob.game.memeduel.core.GamePool;
import fr.byob.game.memeduel.core.MathUtils;
import fr.byob.game.memeduel.core.god.b2d.BoxDefinition;
import fr.byob.game.memeduel.core.god.cannon.CannonBallGOD;
import fr.byob.game.memeduel.core.model.AbstractModel;
import fr.byob.game.memeduel.core.model.cannon.CannonBallModelObject;
import fr.byob.game.memeduel.core.model.object.ModelObject;

public class FragCannonBallHandler extends AbstractCannonBallHandler {


	private final static double ANGLE_RANGE = Math.PI * 3 / 4;

	@Override
	protected CannonBallModelObject applyClickBonus(final AbstractModel model, final float distance) {
		final Body body = this.modelObject.getB2DBodyHandler().getBody();
		int shotNumber = 0;
		int firstShotPosition = 0;


		if (distance < 0.5f){
			shotNumber = 7;
			firstShotPosition = 3;
		}else if (distance < 1.0f){
			shotNumber = 5;
			firstShotPosition = 2;
		}else if (distance < 2.0f){
			shotNumber = 3;
			firstShotPosition = 1;
		} else {
			shotNumber = 1;
			firstShotPosition = 0;
		}

		// Calculer le nombre de plombs
		final float inShapeDistance = Math.max(this.modelObject.getGOD().getShapeDefition().getSize().width, this.modelObject.getGOD().getShapeDefition().getSize().height);
		if (distance < inShapeDistance) {
			// Click direct sur le boulet, super bonus !
			shotNumber = 7;
		} else {
			// Sinon un bonus entre 2 et 4
			shotNumber = (int) (5 / (distance / 3));
			if (shotNumber > 4) {
				shotNumber = 4;
			} else if (shotNumber < 3) {
				shotNumber = 3;
			}
		}

		// Ajouter les plombs devant la cartouche (répartis sur un angle de 140
		// degres)
		final double angleOffset = ANGLE_RANGE / (shotNumber + 1);
		final Vector shotBasePosition = GamePool.instance().popVector();
		shotBasePosition.set(body.getLocalCenter()).addLocal(0.2f + ((BoxDefinition) this.modelObject.getGOD().getShapeDefition()).getSize().width / 2, 0f);
		CannonBallModelObject firstShot = null;

		final Vector linearImpulse = GamePool.instance().popVector();
		final Vector linearVelocity = GamePool.instance().popVector();
		final Vector shotPosition = GamePool.instance().popVector();

		for (int i = 0; i < shotNumber; i++) {
			final float curAngle = (float) ((i + 1) * angleOffset - ANGLE_RANGE / 2);
			MathUtils.rotatePointToOut(curAngle, shotBasePosition, shotPosition);
			final CannonBallGOD shotGOD = CannonBallGOD.cannonBallBuilder().fromType(model.getGodLoader(), "CANNON_BALL_FRAG_SHOT").position(body.getWorldPoint(shotPosition)).build();
			final ModelObject modelObject = model.addObject(shotGOD);
			if (modelObject != null){
				final CannonBallModelObject cannonBallModelObject = (CannonBallModelObject) modelObject;
				final Body shotBody = cannonBallModelObject.getB2DBodyHandler().getBody();
				// On applique la vitesse de la cartouche aux plombs * leur
				// multiplicateur
				final float fireSpeed = shotGOD.getCannonBallDefinition().getFireSpeed();
				linearImpulse.set(body.getLinearVelocity()).addLocal(shotPosition.x, 6 * shotPosition.y).scaleLocal(fireSpeed);
				shotBody.applyLinearImpulse(linearImpulse, shotBody.getWorldCenter());
				shotBody.applyAngularImpulse(body.getAngularVelocity() * fireSpeed);
				if (i == firstShotPosition) {
					firstShot = cannonBallModelObject;
				}
			}
		}

		linearVelocity.set(body.getLinearVelocity()).negateLocal().scaleLocal(0.5f);
		// Faire reculer la cartouche
		body.setLinearVelocity(linearVelocity);
		body.setAngularVelocity((float) (-2 * Math.PI));
		// Endommager la cartouche
		this.modelObject.getDamageHandler().damage(10);

		GamePool.instance().pushVector(4);

		return firstShot;
	}
}
