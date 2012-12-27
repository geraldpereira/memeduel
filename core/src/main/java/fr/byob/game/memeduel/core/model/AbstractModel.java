package fr.byob.game.memeduel.core.model;

import java.util.ArrayList;
import java.util.List;

import playn.core.PlayN;
import pythagoras.f.Vector;
import fr.byob.game.box2d.Box2D;
import fr.byob.game.box2d.Disposable;
import fr.byob.game.box2d.callbacks.ContactImpulse;
import fr.byob.game.box2d.callbacks.ContactListener;
import fr.byob.game.box2d.collision.Manifold;
import fr.byob.game.box2d.collision.shapes.BoxShape;
import fr.byob.game.box2d.collision.shapes.EdgeShape;
import fr.byob.game.box2d.dynamics.Body;
import fr.byob.game.box2d.dynamics.BodyDef;
import fr.byob.game.box2d.dynamics.BodyType;
import fr.byob.game.box2d.dynamics.Contact;
import fr.byob.game.box2d.dynamics.FixtureDef;
import fr.byob.game.box2d.dynamics.World;
import fr.byob.game.memeduel.core.B2DUtils;
import fr.byob.game.memeduel.core.GameContext;
import fr.byob.game.memeduel.core.GamePool;
import fr.byob.game.memeduel.core.controller.PointerPosition;
import fr.byob.game.memeduel.core.god.AllGODLoader;
import fr.byob.game.memeduel.core.god.CloudGOD;
import fr.byob.game.memeduel.core.god.DynamicGameObjectDefinition;
import fr.byob.game.memeduel.core.god.WorldGOD;
import fr.byob.game.memeduel.core.model.object.B2DModelObject;
import fr.byob.game.memeduel.core.model.object.ModelObject;

public abstract class AbstractModel implements ContactListener, Disposable {

	/**
	 * Is the world sleeping ? the world is sleeping when no object moves.
	 */
	protected boolean sleeping;
	protected World world;

	protected AllGODLoader godLoader;

	private int currentId = 0;

	protected final List<ModelListener> listeners = new ArrayList<ModelListener>();

	protected Body groundBody;

	protected final List<ModelObject> modelObjects = new ArrayList<ModelObject>(0);

	private ModelObjectExploder modelObjectExploder;

	public AbstractModel() {
	}

	protected ModelObjectExploder newModelObjectExploder() {
		return new ModelObjectExploder(this);
	}

	public void loadModelObjects(final AllGODLoader godLoader) {
		final Box2D box2d = GameContext.instance().getBox2D();

		// Initialiser le loader
		this.godLoader = godLoader;

		// INitialiser l'exploder (il a besoin du loader!)
		this.modelObjectExploder = this.newModelObjectExploder();

		// create the physics world
		final Vector gravity = godLoader.getGameGOD().getGravity();
		this.world = box2d.newWorld(gravity, true);
		this.world.setWarmStarting(true);
		this.world.setAutoClearForces(true);

		final BodyDef groundBodyDef = new BodyDef();
		this.groundBody = this.world.createBody(groundBodyDef);

		final WorldGOD worldGOD = godLoader.getGameGOD().getWorld();

		// Tout en metres car le drawScale est variable
		final float worldWidth = worldGOD.getWidth();
		final float worldHeight = worldGOD.getHeight();
		final float groundHeight = worldGOD.getGroundHeight();

		final FixtureDef fixDef = new FixtureDef();
		// Densité des bloc (le poid est definit par la
		// surface * la densité)
		fixDef.density = 1.0f;
		// Quantité de frottement entre les blocs
		fixDef.friction = worldGOD.getFriction();
		// Definit les rebonds (plus la valeur est grande, plus les objects
		// rebondissent)
		fixDef.restitution = worldGOD.getRestitution();

		// create the ground
		final BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.STATIC;
		bodyDef.position.set(worldGOD.getWidth() / 2, worldGOD.getHeight() - 1);
		Body body = this.world.createBody(bodyDef);
		body.setUserData(Border.GROUND);
		fixDef.shape = box2d.newBoxShape();
		((BoxShape) fixDef.shape).setAsBox(worldWidth / 2, groundHeight / 2);
		body.createFixture(fixDef);

		// Le haut du monde
		// PosX = milieu du monde
		// PosY = 0.25m pour ratraper l'epaisseur du bas du restangle
		((BoxShape) fixDef.shape).setAsBox(worldWidth / 2, 0.25f);
		bodyDef.position.set(worldWidth / 2, -0.25f);
		body = this.world.createBody(bodyDef);
		body.setUserData(Border.TOP);
		body.createFixture(fixDef);

		// Les coté
		// 50cm de large et haut comme la hauteur du monde
		((BoxShape) fixDef.shape).setAsBox(godLoader.getGameGOD().getWorldWallWidth() / 2, worldHeight / 2);
		bodyDef.position.set(-godLoader.getGameGOD().getWorldWallWidth() / 2, worldHeight / 2);
		body = this.world.createBody(bodyDef);
		body.setUserData(Border.LEFT);
		body.createFixture(fixDef);
		bodyDef.position.set(worldWidth + godLoader.getGameGOD().getWorldWallWidth() / 2, worldHeight / 2);
		body = this.world.createBody(bodyDef);
		body.setUserData(Border.RIGHT);
		body.createFixture(fixDef);

		// create the walls
		final Body wallLeft = this.world.createBody(new BodyDef());
		final EdgeShape wallLeftShape = box2d.newEdgeShape();
		wallLeftShape.setAsEdge(GamePool.instance().popVector().set(0, 0), GamePool.instance().popVector().set(0, worldHeight));
		wallLeft.createFixture(wallLeftShape, 0.0f);
		final Body wallRight = this.world.createBody(new BodyDef());
		final EdgeShape wallRightShape = box2d.newEdgeShape();
		wallRightShape.setAsEdge(GamePool.instance().popVector().set(worldWidth, 0), GamePool.instance().popVector().set(worldWidth, worldHeight));
		wallRight.createFixture(wallRightShape, 0.0f);

		GamePool.instance().pushVector(1);

		final List<CloudGOD> cloudGODs = godLoader.getGameGOD().getClouds();
		// Charger les landscapeGODs dans des landscape entities
		for (final CloudGOD cloudGOD : cloudGODs) {
			this.addObject(cloudGOD);
		}

		this.addObject(godLoader.getGameGOD().getSunGlow());

		// Les objets dynamiques
		for (final DynamicGameObjectDefinition god : godLoader.getLevelGOD().getGameObjects()) {
			this.addObject(god);
		}
	}

	// Box2d's begin contact
	@Override
	public void beginContact(final Contact contact) {
	}

	// Box2d's end contact
	@Override
	public void endContact(final Contact contact) {
	}

	// Box2d's pre solve
	@Override
	public void preSolve(final Contact contact, final Manifold oldManifold) {
	}

	@Override
	public void postSolve(final Contact contact, final ContactImpulse impulse) {
		float b2dNormalImpulse = 0;
		for (final float val : impulse.getNormalImpulses()) {
			b2dNormalImpulse += val;
		}
		if (b2dNormalImpulse > 0 && contact.getFixtureA() != null && contact.getFixtureB() != null) {
			final Body bodyA = contact.getFixtureA().getBody();
			final Body bodyB = contact.getFixtureB().getBody();
			if (b2dNormalImpulse > this.getGodLoader().getGameGOD().getMinNormalImpulseSmoke()) {

				// Afficher la fumée au sol
				if (bodyA.getUserData() instanceof Border) {
					final Vector b2dLocalPoint = contact.getManifold().getLocalPoint();
					final Vector position = bodyB.getWorldPoint(b2dLocalPoint);
					this.fireObjectHitBorder((ModelObject) bodyB.getUserData(), (Border) bodyA.getUserData(), position);
				} else if (bodyB.getUserData() instanceof Border) {
					final Vector b2dLocalPoint = contact.getManifold().getLocalPoint();
					final Vector position = bodyA.getWorldPoint(b2dLocalPoint);
					this.fireObjectHitBorder((ModelObject) bodyA.getUserData(), (Border) bodyB.getUserData(), position);
				}
			}

			this.damageObject(bodyA.getLinearVelocity().length() * b2dNormalImpulse, bodyA, bodyB);
			this.damageObject(bodyB.getLinearVelocity().length() * b2dNormalImpulse, bodyB, bodyA);
		}
	}

	protected abstract void damageObject(final float damageAmount, final Body b2dBody, final Body b2dOtherBody);

	public void addListener(final ModelListener listener) {
		this.listeners.add(listener);
	}

	protected final void fireModelObjectAdded(final ModelObject modelObject) {
		for (int i = 0; i < this.listeners.size(); i++) {
			final ModelListener listener = this.listeners.get(i);
			listener.objectAdded(modelObject);
		}
	};

	protected final void fireModelObjectRemoved(final ModelObject modelObject) {
		for (int i = 0; i < this.listeners.size(); i++) {
			final ModelListener listener = this.listeners.get(i);
			listener.objectRemoved(modelObject);
		}
	};

	protected final void fireObjectHitBorder(final ModelObject modelObject, final Border border, final Vector position) {
		for (int i = 0; i < this.listeners.size(); i++) {
			final ModelListener oListener = this.listeners.get(i);
			oListener.objectHitBorder(modelObject, border, position);
		}
	};

	protected final void fireObjectDamaged(final ModelObject modelObject) {
		for (int i = 0; i < this.listeners.size(); i++) {
			final ModelListener oListener = this.listeners.get(i);
			oListener.objectDamaged(modelObject);
		}
	};

	public String newId() {
		final int id = this.currentId;
		this.currentId++;
		return "" + id;
	};

	public ModelObject addObject(final DynamicGameObjectDefinition god) {
		if (god == null) {
			PlayN.log().error("GOD is null !");
			return null;
		}

		final Vector position = god.getPosition();

		if (position.x >= 0.5f && position.x <= this.godLoader.getGameGOD().getWorld().getWidth() - 0.5f && position.y >= 0.5f && position.y <= this.godLoader.getGameGOD().getWorld().getHeight() - this.godLoader.getGameGOD().getWorld().getGroundHeight()) {
			final ModelObject modelObject = god.newModelObject(this);
			this.modelObjects.add(modelObject);
			this.fireModelObjectAdded(modelObject);
			return modelObject;
		}
		return null;
	}

	protected ModelObject removeObject(final int index) {
		final ModelObject modelObject = this.modelObjects.remove(index);
		if (modelObject != null) {
			if (modelObject instanceof B2DModelObject) {
				final B2DModelObject b2dModelObject = (B2DModelObject) modelObject;
				if (!b2dModelObject.isExplodable()) {
					b2dModelObject.getDamageHandler().destroy();
					this.world.destroyBody(b2dModelObject.getB2DBodyHandler().getBody());
					this.fireModelObjectRemoved(b2dModelObject);
				} else {
					final List<ModelObject> fragments = this.modelObjectExploder.explode(b2dModelObject);
					// Pas besoin de supprimer le B2D Body car le Exploder s'en
					// occupe
					this.fireModelObjectRemoved(b2dModelObject);
					for (final ModelObject fragment : fragments) {
						this.modelObjects.add(fragment);
						this.fireModelObjectAdded(fragment);
					}
				}
			} else {
				this.fireModelObjectRemoved(modelObject);
			}
			return modelObject;
		}
		return null;
	}

	public void update(final PointerPosition pointerPosition, final float delta) {
		// the step delta is fixed so box2d isn't affected by framerate
		this.world.step(0.033f, 10, 10);

		boolean sleeping = true;

		for (final Body curB2Body : this.world.getBodyList()) {
			if (curB2Body.getType() != BodyType.STATIC && curB2Body.isAwake()) {
				// One body is moving, the world is not sleeping
				sleeping = false;
			}
		}

		this.sleeping = sleeping;

		final List<Integer> destroyedObjectIndexes = new ArrayList<Integer>();
		for (int i = 0; i < this.modelObjects.size(); i++) {
			final ModelObject modelObject = this.modelObjects.get(i);
			if (modelObject.update(delta)) {
				this.fireObjectDamaged(modelObject);
			}
			if (modelObject.isDamageable() && modelObject.getDamageHandler().isDestroyed()) {
				destroyedObjectIndexes.add(i);
			}
		}

		for (int i = destroyedObjectIndexes.size() - 1; i >= 0; i--) {
			this.removeObject(destroyedObjectIndexes.get(i).intValue());
		}

	};

	public World getB2World() {
		return this.world;
	}

	public Body getGroundBody() {
		return this.groundBody;
	}

	public boolean isSleeping() {
		return this.sleeping;
	}

	public AllGODLoader getGodLoader() {
		return this.godLoader;
	}

	public B2DModelObject getModelObjectAtMouse(final PointerPosition pointerPosition) {
		// Récupérer l'objet sélectionné
		final Body selectedBody = B2DUtils.getBodyAtMouse(this.world, pointerPosition);
		if (selectedBody != null) {
			final Object userData = selectedBody.getUserData();
			if (userData instanceof B2DModelObject) {
				return (B2DModelObject) userData;
			}
		}
		return null;
	}

	public List<ModelObject> getModelObjects() {
		return this.modelObjects;
	}

	@Override
	public void dispose() {
		this.world.dispose();
	}
}
