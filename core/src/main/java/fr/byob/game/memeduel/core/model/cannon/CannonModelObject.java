package fr.byob.game.memeduel.core.model.cannon;

import java.util.ArrayList;
import java.util.List;

import pythagoras.f.Dimension;
import pythagoras.f.Vector;
import fr.byob.game.box2d.Box2D;
import fr.byob.game.box2d.collision.shapes.BoxShape;
import fr.byob.game.box2d.collision.shapes.CircleShape;
import fr.byob.game.box2d.dynamics.Body;
import fr.byob.game.box2d.dynamics.BodyDef;
import fr.byob.game.box2d.dynamics.BodyType;
import fr.byob.game.box2d.dynamics.FixtureDef;
import fr.byob.game.box2d.dynamics.World;
import fr.byob.game.box2d.dynamics.joints.DistanceJoint;
import fr.byob.game.box2d.dynamics.joints.DistanceJointDef;
import fr.byob.game.box2d.dynamics.joints.FrictionJoint;
import fr.byob.game.box2d.dynamics.joints.FrictionJointDef;
import fr.byob.game.box2d.dynamics.joints.MouseJoint;
import fr.byob.game.box2d.dynamics.joints.MouseJointDef;
import fr.byob.game.box2d.dynamics.joints.PrismaticJoint;
import fr.byob.game.box2d.dynamics.joints.PrismaticJointDef;
import fr.byob.game.box2d.dynamics.joints.RevoluteJoint;
import fr.byob.game.box2d.dynamics.joints.RevoluteJointDef;
import fr.byob.game.memeduel.core.B2DUtils;
import fr.byob.game.memeduel.core.GameContext;
import fr.byob.game.memeduel.core.GameImage;
import fr.byob.game.memeduel.core.GamePool;
import fr.byob.game.memeduel.core.MathUtils;
import fr.byob.game.memeduel.core.MemeDuelLoader;
import fr.byob.game.memeduel.core.ViewUtils;
import fr.byob.game.memeduel.core.controller.PointerPosition;
import fr.byob.game.memeduel.core.editor.CannonBallGODProvider;
import fr.byob.game.memeduel.core.god.AllGODLoader;
import fr.byob.game.memeduel.core.god.ProvidedBodyGOD;
import fr.byob.game.memeduel.core.god.cannon.CannonBallGOD;
import fr.byob.game.memeduel.core.god.cannon.CannonGOD;
import fr.byob.game.memeduel.core.god.helper.CannonBallLoadHelper;
import fr.byob.game.memeduel.core.god.image.ImageDefinition;
import fr.byob.game.memeduel.core.god.layer.LayerDefinition;
import fr.byob.game.memeduel.core.god.update.B2DUpdateDefinition;
import fr.byob.game.memeduel.core.model.AbstractModel;
import fr.byob.game.memeduel.core.model.object.B2DModelObject;
import fr.byob.game.memeduel.core.model.object.ModelObject;

public class CannonModelObject {
	private final AbstractModel model;

	private CannonBallGODProvider cannonBallGODProvider;
	private final CannonGOD god;

	// Used to handle object selection
	private MouseJoint mouseJoint;
	private boolean fireCannon;

	private CannonBallModelObject currentCannonBall;

	// Cannon parts
	private final Body cannon;
	private final Body wheel;
	private final Body base;
	private final Body bolt;

	private final ModelObject cannonModelObject;
	private final ModelObject wheelModelObject;
	private final ModelObject baseModelObject;
	private final ModelObject boltModelObject;

	private final List<CannonListener> listeners = new ArrayList<CannonListener>();

	private Vector applyClickBonusPosition;

	private int updateCount = 0;

	public CannonModelObject(final AbstractModel model,final CannonGOD god) {
		final Box2D box2d = GameContext.instance().getBox2D();

		this.model = model;
		this.god = god;

		final World world = this.model.getB2World();
		final AllGODLoader loader = model.getGodLoader();

		final float axleDiff = ViewUtils.toInitModel(this.god.getBaseDimension().height);
		final float baseLength = ViewUtils.toInitModel(this.god.getBaseDimension().width);
		final float baseThickness = ViewUtils.toInitModel(this.god.getBaseThickness());
		final float cannonLength = ViewUtils.toInitModel(this.god.getCannonDimension().width);
		final float cannonThickness = ViewUtils.toInitModel(this.god.getCannonDimension().height);
		final float wheelRadius = ViewUtils.toInitModel(this.god.getWheelRadius());
		final float boltLength = ViewUtils.toInitModel(this.god.getBoltDimension().width);
		final float boltThickness = ViewUtils.toInitModel(this.god.getBoltDimension().height);

		final float x = this.god.getXPosition();
		final float y = loader.getGameGOD().getWorld().getHeight() - (loader.getGameGOD().getWorld().getGroundHeight() + wheelRadius + baseThickness);

		final Vector wheelPosition = GamePool.instance().popVector().set(x, y + axleDiff / 2);
		final Vector cannonPosition = GamePool.instance().popVector().set(x + cannonLength / 4, y - axleDiff / 2);
		final Vector revoluteCannonPosition = GamePool.instance().popVector().set(x, y - axleDiff / 2);
		final Vector boltPosition = GamePool.instance().popVector().set(cannonPosition).add(-boltLength / 2, 0f);
		final Vector tmp = GamePool.instance().popVector();

		// The base
		final BoxShape axleShape = box2d.newBoxShape();
		axleShape.setAsBox(baseThickness / 2, axleDiff / 2);
		final FixtureDef axleFixture = new FixtureDef();
		axleFixture.density = 1;
		axleFixture.friction = 3;
		axleFixture.restitution = 0.3f;
		axleFixture.filter.groupIndex = -2;
		axleFixture.shape = axleShape;

		final BoxShape baseShape = box2d.newBoxShape();
		baseShape.setAsBox((baseLength - baseThickness) / 2, baseThickness / 2, tmp.set(-baseLength / 2, 0), 0);
		final FixtureDef baseFixture = new FixtureDef();
		baseFixture.density = 50;
		baseFixture.friction = 0.1f;
		baseFixture.restitution = 0.3f;
		baseFixture.filter.groupIndex = -2;
		baseFixture.shape = baseShape;

		final BoxShape massShape = box2d.newBoxShape();
		massShape.setAsBox(baseThickness / 2, baseThickness / 2, tmp.set(-baseLength, 0), 0);
		final FixtureDef massFixture = new FixtureDef();
		massFixture.density = 100;
		massFixture.friction = 0.1f;
		massFixture.restitution = 0.3f;
		massFixture.filter.groupIndex = -2;
		massFixture.shape = massShape;

		final BodyDef baseBodyDef = new BodyDef();
		baseBodyDef.type = BodyType.DYNAMIC;
		baseBodyDef.position.set(x, y);

		this.base = world.createBody(baseBodyDef);
		this.base.createFixture(baseFixture);
		this.base.createFixture(massFixture);
		this.base.createFixture(axleFixture);

		// The Wheel
		final CircleShape wheelShape = box2d.newCircleShape();
		final FixtureDef wheelFixture = new FixtureDef();
		wheelFixture.density = 2;
		wheelFixture.friction = 1;
		wheelFixture.restitution = 0.5f;
		wheelFixture.filter.groupIndex = -2;
		wheelFixture.shape = wheelShape;
		wheelShape.setRadius(wheelRadius);
		final BodyDef wheelBodyDef = new BodyDef();
		wheelBodyDef.type = BodyType.DYNAMIC;
		wheelBodyDef.position.set(wheelPosition);
		this.wheel = world.createBody(wheelBodyDef);
		this.wheel.createFixture(wheelFixture);

		// Constraint between the wheel and the base
		final RevoluteJointDef wheelRevoluteJointDef = new RevoluteJointDef();
		wheelRevoluteJointDef.initialize(this.wheel, this.base, this.wheel.getWorldCenter());
		wheelRevoluteJointDef.enableMotor = true;
		wheelRevoluteJointDef.maxMotorTorque = 10;
		final RevoluteJoint wheelRevoluteJoint = (RevoluteJoint) world.createJoint(wheelRevoluteJointDef);

		// The cannon
		final BoxShape cannonShape = box2d.newBoxShape();
		cannonShape.setAsBox(cannonLength / 2, cannonThickness / 2);
		final FixtureDef cannonFixture = new FixtureDef();
		cannonFixture.density = 1;
		cannonFixture.friction = 1;
		cannonFixture.restitution = 1f;
		cannonFixture.filter.groupIndex = -2;
		cannonFixture.shape = cannonShape;
		final BodyDef cannonBodyDef = new BodyDef();
		cannonBodyDef.type = BodyType.DYNAMIC;
		cannonBodyDef.position.set(cannonPosition);
		this.cannon = world.createBody(cannonBodyDef);
		this.cannon.createFixture(cannonFixture);

		// The bolt
		final BoxShape boltShape = box2d.newBoxShape();
		boltShape.setAsBox(boltLength / 2, boltThickness / 2);
		final FixtureDef boltFixture = new FixtureDef();
		boltFixture.density = 1;
		boltFixture.friction = 1;
		boltFixture.restitution = 1;
		boltFixture.shape = boltShape;
		boltFixture.filter.groupIndex = -2;
		final BodyDef boltBodyDef = new BodyDef();
		boltBodyDef.type = BodyType.DYNAMIC;
		boltBodyDef.position.set(boltPosition);
		this.bolt = world.createBody(boltBodyDef);
		this.bolt.createFixture(boltFixture);

		// Translation contraint of the bolt in the cannon
		final PrismaticJointDef boltPrismaticJointDef = new PrismaticJointDef();

		boltPrismaticJointDef.lowerTranslation = -boltLength + ViewUtils.toInitModel(10);
		boltPrismaticJointDef.upperTranslation = 0;
		boltPrismaticJointDef.enableLimit = true;
		boltPrismaticJointDef.enableMotor = true;
		boltPrismaticJointDef.maxMotorForce = 200;
		boltPrismaticJointDef.motorSpeed = 200;
		boltPrismaticJointDef.initialize(this.cannon, this.bolt, this.bolt.getWorldCenter(), tmp.set(1, 0));
		final PrismaticJoint boltPrismaticJoint = (PrismaticJoint) world.createJoint(boltPrismaticJointDef);

		// Angular constraint between the cannon and the base
		final RevoluteJointDef cannonRevoluteJointDef = new RevoluteJointDef();
		cannonRevoluteJointDef.initialize(this.cannon, this.base, revoluteCannonPosition);
		cannonRevoluteJointDef.enableMotor = false;
		cannonRevoluteJointDef.lowerAngle = -(float) (Math.PI / 4);
		cannonRevoluteJointDef.upperAngle = (float) (Math.PI / 3);
		cannonRevoluteJointDef.enableLimit = true;
		final RevoluteJoint cannonRevoluteJoint = (RevoluteJoint) world.createJoint(cannonRevoluteJointDef);

		// The cannon must NOT freely rotate over the base
		final FrictionJointDef cannonFrictionJointDef = new FrictionJointDef();
		cannonFrictionJointDef.initialize(this.cannon, this.base, revoluteCannonPosition);
		cannonFrictionJointDef.maxTorque = 60.0f;
		final FrictionJoint cannonFrictionJoint = (FrictionJoint) world.createJoint(cannonFrictionJointDef);

		// Attach the wheel to the static world
		final DistanceJointDef attatchToGroundJointDef = new DistanceJointDef();
		attatchToGroundJointDef.initialize(this.wheel, this.model.getGroundBody(), wheelPosition, wheelPosition);
		attatchToGroundJointDef.length = 0;
		attatchToGroundJointDef.frequencyHz = 2.0f;
		attatchToGroundJointDef.dampingRatio = 0.8f;
		// cannonDistanceJointDef.length = axleDiff;
		final DistanceJoint attatchToGroundJoint = (DistanceJoint) world.createJoint(attatchToGroundJointDef);

		// give the good final angle to final the base
		this.base.setTransform(this.base.getPosition(), (float) -(Math.PI / 10));

		final Vector wheelVect = GamePool.instance().popVector();
		final Vector baseVect = GamePool.instance().popVector();
		final Vector cannonVect = GamePool.instance().popVector();
		final Vector boltVect = GamePool.instance().popVector();

		this.wheelModelObject = this.initCannonPart(this.wheel, MemeDuelLoader.CANNON_WHEEL, MemeDuelLoader.CANNON_WHEEL.dimensionToOut(MathUtils.tmpDim()), MemeDuelLoader.CANNON_WHEEL.centerPositionToOut(wheelVect), god.getDepth());
		this.baseModelObject = this.initCannonPart(this.base, MemeDuelLoader.CANNON_BASE, MemeDuelLoader.CANNON_BASE.dimensionToOut(MathUtils.tmpDim()), baseVect.set(god.getBaseDimension().width + god.getBaseThickness() / 2, god.getBaseDimension().height / 2), god.getDepth() - 1);
		this.cannonModelObject = this.initCannonPart(this.cannon, MemeDuelLoader.CANNON, MemeDuelLoader.CANNON.dimensionToOut(MathUtils.tmpDim()), MemeDuelLoader.CANNON.centerPositionToOut(cannonVect), god.getDepth() - 2);
		this.boltModelObject = this.initCannonPart(this.bolt, MemeDuelLoader.CANNON_BOLT, MemeDuelLoader.CANNON_BOLT.dimensionToOut(MathUtils.tmpDim()), MemeDuelLoader.CANNON_BOLT.centerPositionToOut(boltVect).addLocal(-6, 0), god.getDepth() - 3);

		GamePool.instance().pushVector(9);

	}

	private B2DModelObject initCannonPart(final Body curBody, final GameImage image, final Dimension layerSize, final Vector layerOrigin, final float depth) {
		final ProvidedBodyGOD cannonPartGOD = ProvidedBodyGOD.providedBuilder()
				.body(curBody)
				.updateDefinition(B2DUpdateDefinition.builder().build())
				.imageDefinition(ImageDefinition.imageBuilder().image(image).build())
				.layerDefinition(LayerDefinition.builder().layerSize(layerSize).layerOrigin(layerOrigin).depth(depth).build())
				.angle(curBody.getAngle())
				.position(curBody.getPosition()).build();
		return cannonPartGOD.newModelObject(this.model);
	}

	public ModelObject getCannonModelObject() {
		return this.cannonModelObject;
	}

	public ModelObject getBaseModelObject() {
		return this.baseModelObject;
	}

	public ModelObject getWheelModelObject() {
		return this.wheelModelObject;
	}

	public ModelObject getBoltModelObject() {
		return this.boltModelObject;
	}

	public boolean load(final PointerPosition pointerPosition) {
		if (!this.isFiring() && !cannonBallGODProvider.isEmpty()) {
			if (this.mouseJoint == null) {
				if (B2DUtils.isBodyAtMouse(this.model.getB2World(), this.bolt, pointerPosition)) {
					// Créer le b2dMouseJoint permettant de déplacer l'objet
					// sélectionner
					final MouseJointDef b2dMouseJointDef = new MouseJointDef();
					b2dMouseJointDef.bodyA = this.model.getGroundBody();// this.world.getGroundBody();
					b2dMouseJointDef.bodyB = this.bolt;
					b2dMouseJointDef.target.set(pointerPosition.getModelPosition());
					b2dMouseJointDef.collideConnected = true;
					b2dMouseJointDef.maxForce = 500.0f * this.bolt.getMass();
					this.mouseJoint = (MouseJoint) this.model.getB2World().createJoint(b2dMouseJointDef);
					this.bolt.setAwake(true);
					return true;
				} else {
					this.mouseJoint = null;
				}
			}
		}
		return false;
	}

	public boolean fire(final PointerPosition position) {
		final boolean wasLoaded = this.isLoaded();
		if (wasLoaded) {
			this.fireCannon = true;
		}
		return wasLoaded;
	}

	public boolean isFiring() {
		return this.currentCannonBall != null;
	}

	/**
	 * 
	 * @return null si le canon n'est pas en train de tirer
	 */
	public CannonBallModelObject getCurrentCannonBall() {
		return this.currentCannonBall;
	}

	public boolean isLoaded() {
		return this.mouseJoint != null;
	}

	public void update(final PointerPosition pointerPosition, final float delta) {
		this.cannonModelObject.update(delta);
		this.wheelModelObject.update(delta);
		this.baseModelObject.update(delta);
		this.boltModelObject.update(delta);

		if (this.currentCannonBall != null) {
			if (this.applyClickBonusPosition != null && this.currentCannonBall.getCannonBallHandler() != null) {
				final CannonBallModelObject cannonBallModelObject = this.currentCannonBall.getCannonBallHandler().applyClickBonus(this.model, this.applyClickBonusPosition);
				if (cannonBallModelObject != null) {
					final Vector currentPos = GamePool.instance().popVector();
					this.currentCannonBall.getUpdateHandler().getCurrentPositionToOut(currentPos);
					this.fireCannonBallBonusActivated(currentPos, this.currentCannonBall.getUpdateHandler().getCurrentRotation());
					this.currentCannonBall = cannonBallModelObject;
					GamePool.instance().pushVector(1);
				}
				this.applyClickBonusPosition = null;
			} else if (this.currentCannonBall.isDamageable() && this.currentCannonBall.getDamageHandler().isDamaged()) {
				// Dès que le boulet touche un obstacle, sa vie commence à
				// diminuer. On ne le garde plus, l'utilisateur peut à nouveau
				// tirer
				this.currentCannonBall = null;
				this.fireCannonBallDamaged();
			} else {
				this.updateCount++;
				if (this.updateCount >= 2) {
					final Vector currentPos = GamePool.instance().popVector();
					this.currentCannonBall.getUpdateHandler().getCurrentPositionToOut(currentPos);
					this.fireCannonBallMoved(currentPos, this.currentCannonBall.getUpdateHandler().getCurrentRotation());
					this.updateCount = 0;
					GamePool.instance().pushVector(1);
				}
			}
		}

		if (this.isLoaded()) {
			if (this.fireCannon) {
				// the cannon is fired, add the cannon ball
				final String ballType = this.cannonBallGODProvider.getCurrentType();
				final float ballWidth = ((CannonBallLoadHelper) this.model.getGodLoader().getGameOptionsGOD().getLoadHelper(ballType)).getShapeDefinition().getSize().width;
				final float cannonLength = ViewUtils.toInitModel(this.god.getCannonDimension().width);

				// Il ne faut pas créer le boulet DANS le bout du cannon car ca
				// l'endomage

				final Vector cannonBallLocalPosition = GamePool.instance().popVector().set(ballWidth + cannonLength / 2, 0);
				final Vector cannonBallPosition = GamePool.instance().popVector();
				this.cannon.getWorldPointToOut(cannonBallLocalPosition, cannonBallPosition);
				// Par contre pour le fire on donne la position exacte du bout
				// du cannon, elle est utilisée pour afficher le muzzle flash
				final Vector cannonLocalPosition = GamePool.instance().popVector().set(cannonLength / 2, 0);
				final Vector cannonPosition = GamePool.instance().popVector();
				this.cannon.getWorldPointToOut(cannonLocalPosition, cannonPosition);

				final CannonBallGOD cannonBallGOD = cannonBallGODProvider.getGOD(cannonBallPosition, this.cannon.getAngle());
				this.currentCannonBall = (CannonBallModelObject) this.model.addObject(cannonBallGOD);

				final Vector fireVector = GamePool.instance().popVector();
				this.getFireVectorToOut(cannonBallGOD, fireVector);
				this.currentCannonBall.fire(fireVector, 0);
				this.cannon.setLinearVelocity(fireVector.negateLocal().scaleLocal(10));

				this.fireCannonBallFired(cannonPosition, this.cannon.getAngle());
				// Clean the cannon state
				this.model.getB2World().destroyJoint(this.mouseJoint);
				this.mouseJoint = null;
				this.fireCannon = false;
				this.updateCount = 0;

				GamePool.instance().pushVector(5);
			} else if (pointerPosition != null) {
				this.mouseJoint.setTarget(pointerPosition.getModelPosition());
			}
		}
	};

	private void getFireVectorToOut(final CannonBallGOD god, final Vector out) {
		final Vector cannonCenter = this.cannon.getWorldCenter();
		final Vector boltCenter = this.bolt.getWorldCenter();
		// Do not create a vec2 each time the cannon fires
		out.set(cannonCenter.x - boltCenter.x, cannonCenter.y - boltCenter.y).scaleLocal(god.getCannonBallDefinition().getFireSpeed());
	}

	public void applyClickBonus(final Vector clickPosition) {
		if (this.currentCannonBall != null) {
			this.applyClickBonusPosition = clickPosition;
		}
	}

	public void addListener(final CannonListener listener) {
		this.listeners.add(listener);
	}

	public void removeListener(final CannonListener listener) {
		this.listeners.remove(listener);
	}

	private void fireCannonBallDamaged() {
		for (final CannonListener listener : this.listeners) {
			listener.cannonBallDamaged();
		}
	}

	private void fireCannonBallFired(final Vector position, final float angle) {
		for (final CannonListener listener : this.listeners) {
			listener.cannonBallFired(position, angle);
		}
	}

	private void fireCannonBallMoved(final Vector position, final float angle) {
		for (final CannonListener listener : this.listeners) {
			listener.cannonBallMoved(position, angle);
		}
	}

	private void fireCannonBallBonusActivated(final Vector position, final float angle) {
		for (final CannonListener listener : this.listeners) {
			listener.cannonBallBonusActivated(position, angle);
		}
	}

	@SuppressWarnings("unchecked")
	public <T extends CannonBallGODProvider> T getCannonBallGODProvider() {
		return (T) cannonBallGODProvider;
	}

	public void setCannonBallGODProvider(final CannonBallGODProvider cannonBallGODProvider) {
		this.cannonBallGODProvider = cannonBallGODProvider;
	}

}
