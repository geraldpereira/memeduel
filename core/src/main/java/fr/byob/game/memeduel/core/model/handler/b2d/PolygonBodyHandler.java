package fr.byob.game.memeduel.core.model.handler.b2d;

import fr.byob.game.box2d.dynamics.Body;
import fr.byob.game.box2d.dynamics.BodyDef;
import fr.byob.game.box2d.dynamics.FixtureDef;
import fr.byob.game.box2d.dynamics.World;
import fr.byob.game.memeduel.core.god.b2d.B2DBodyDefinition;
import fr.byob.game.memeduel.core.god.b2d.PolygonDefinition;

public class PolygonBodyHandler extends AbstractBodyHandler {

	@Override
	public void addBody(final World world) {

		final PolygonDefinition polygonDefinition = (PolygonDefinition) this.modelObject.getGOD().getShapeDefition();
		final B2DBodyDefinition bodyDefinition = this.modelObject.getGOD().getB2DBodyDefition();

		final BodyDef bodyDef = new BodyDef();
		bodyDef.type = bodyDefinition.getBodyType();
		bodyDef.position.set(this.modelObject.getGOD().getPosition().x, this.modelObject.getGOD().getPosition().y);
		bodyDef.angle = this.modelObject.getGOD().getAngle();
		final Body polygonBody = world.createBody(bodyDef);

		final FixtureDef fixDef = new FixtureDef();
		fixDef.shape = polygonDefinition.getPolygonShape();
		fixDef.density = bodyDefinition.getDensity();
		fixDef.friction = bodyDefinition.getFriction();
		fixDef.restitution = bodyDefinition.getRestitution();

		polygonBody.createFixture(fixDef);

		polygonBody.setUserData(this.modelObject);
		polygonBody.setBullet(bodyDefinition.isBullet());
		polygonBody.setFixedRotation(bodyDefinition.isFixedRotation());
		this.body = polygonBody;
	}

}
