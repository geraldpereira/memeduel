package fr.byob.game.memeduel.core.model.handler.b2d;

import fr.byob.game.box2d.Box2D;
import fr.byob.game.box2d.collision.shapes.CircleShape;
import fr.byob.game.box2d.dynamics.Body;
import fr.byob.game.box2d.dynamics.BodyDef;
import fr.byob.game.box2d.dynamics.FixtureDef;
import fr.byob.game.box2d.dynamics.World;
import fr.byob.game.memeduel.core.GameContext;
import fr.byob.game.memeduel.core.god.b2d.B2DBodyDefinition;
import fr.byob.game.memeduel.core.god.b2d.CircleDefinition;

public class CircleBodyHandler extends AbstractBodyHandler {

	@Override
	public void addBody(final World world) {
		final Box2D box2d = GameContext.instance().getBox2D();

		final CircleDefinition circleDefinition = (CircleDefinition) this.modelObject.getGOD().getShapeDefition();
		final B2DBodyDefinition bodyDefinition = this.modelObject.getGOD().getB2DBodyDefition();

		final BodyDef bodyDef = new BodyDef();
		bodyDef.type = bodyDefinition.getBodyType();
		bodyDef.position.set(this.modelObject.getGOD().getPosition().x, this.modelObject.getGOD().getPosition().y);
		bodyDef.angle = this.modelObject.getGOD().getAngle();
		final Body circleBody = world.createBody(bodyDef);

		final FixtureDef fixDef = new FixtureDef();
		fixDef.shape = box2d.newCircleShape();
		((CircleShape) fixDef.shape).setRadius(circleDefinition.getRadius());
		fixDef.density = bodyDefinition.getDensity();
		fixDef.friction = bodyDefinition.getFriction();
		fixDef.restitution = bodyDefinition.getRestitution();

		circleBody.createFixture(fixDef);

		circleBody.setUserData(this.modelObject);
		circleBody.setBullet(bodyDefinition.isBullet());
		circleBody.setFixedRotation(bodyDefinition.isFixedRotation());
		this.body = circleBody;
	}

}
