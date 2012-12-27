package fr.byob.game.memeduel.core.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import playn.core.Image;
import pythagoras.f.Dimension;
import pythagoras.f.Vector;
import fr.byob.game.box2d.callbacks.RayCastCallback;
import fr.byob.game.box2d.collision.WorldManifold;
import fr.byob.game.box2d.collision.shapes.PolygonShape;
import fr.byob.game.box2d.common.Settings;
import fr.byob.game.box2d.dynamics.Body;
import fr.byob.game.box2d.dynamics.BodyType;
import fr.byob.game.box2d.dynamics.Fixture;
import fr.byob.game.box2d.dynamics.contacts.ContactEdge;
import fr.byob.game.memeduel.core.GameContext;
import fr.byob.game.memeduel.core.GameImage;
import fr.byob.game.memeduel.core.GamePool;
import fr.byob.game.memeduel.core.MathUtils;
import fr.byob.game.memeduel.core.ViewUtils;
import fr.byob.game.memeduel.core.god.B2DGameObjectDefiniton;
import fr.byob.game.memeduel.core.god.b2d.B2DBodyDefinition;
import fr.byob.game.memeduel.core.god.b2d.CircleDefinition;
import fr.byob.game.memeduel.core.god.b2d.PolygonDefinition;
import fr.byob.game.memeduel.core.god.b2d.ShapeDefinition;
import fr.byob.game.memeduel.core.god.damage.DamageDefinition;
import fr.byob.game.memeduel.core.god.damage.DecayDefinition;
import fr.byob.game.memeduel.core.god.damage.DurationDefinition;
import fr.byob.game.memeduel.core.god.damage.LifespanDefinition;
import fr.byob.game.memeduel.core.god.image.ImageDefinition;
import fr.byob.game.memeduel.core.god.image.TextureDefinition;
import fr.byob.game.memeduel.core.god.layer.LayerDefinition;
import fr.byob.game.memeduel.core.god.update.B2DUpdateDefinition;
import fr.byob.game.memeduel.core.model.handler.damage.LifespanDamageHandler;
import fr.byob.game.memeduel.core.model.object.B2DModelObject;
import fr.byob.game.memeduel.core.model.object.ModelObject;

public class ModelObjectExploder implements RayCastCallback{

	private final AbstractModel model;

	private final Map<String, Vector> enterPoints;

	private final Map<String, Body> explodingBodies;

	private B2DModelObject modelObject;

	public enum ExplodeType {
		// Explodes around the center of the shape
		CENTRAL {
			@Override
			public int getExplosionCutsToOut(final B2DModelObject modelObject, final Vector[] out, final int outSize) {
				final Body bodyToExplode = modelObject.getB2DBodyHandler().getBody();
				final Vector bodyToExplodePosition = bodyToExplode.getPosition();
				final float diameter = getDiameterFromModelOBject(modelObject);
				final int explosionCount = getExplosionCutsSize(outSize, bodyToExplode);

				for (int i = 0; i < explosionCount - 1; i += 2) {
					setRandomVectorsAroundPoint(diameter, bodyToExplodePosition, out[i], out[i + 1]);
				}

				return explosionCount;
			}
		},
		// Explodes around the impact points
		IMPACT {
			@Override
			public int getExplosionCutsToOut(final B2DModelObject modelObject, final Vector[] out, final int outSize) {
				final Body bodyToExplode = modelObject.getB2DBodyHandler().getBody();
				final float diameter = getDiameterFromModelOBject(modelObject);
				final int explosionCount = getExplosionCutsSize(outSize, bodyToExplode);

				ContactEdge contacts = bodyToExplode.getContactList();
				int currentExplosionCount = 0;

				if (contacts != null) {
					do {
						contacts.getContact().getWorldManifoldToOut(getWorldManifold());
						for (final Vector impact : getWorldManifold().points) {
							setRandomVectorsAroundPoint(diameter, impact, out[currentExplosionCount], out[currentExplosionCount + 1]);
							currentExplosionCount += 2;

							if (currentExplosionCount >= explosionCount) {
								return currentExplosionCount;
							}
						}
						contacts = contacts.getNext();
						if (contacts == null) {
							// Restart from the beginning of the contact list
							contacts = bodyToExplode.getContactList();
						}
					} while (currentExplosionCount < explosionCount);
				} else {
					// No contact => No impact => explode from center
					return CENTRAL.getExplosionCutsToOut(modelObject, out, outSize);
				}

				return explosionCount;
			}
		},

		// // Explodes around the impact points, faster thatn normal IMPACT
		// IMPACT_FAST {
		// @Override
		// public int getExplosionCutsToOut(final B2DModelObject modelObject,
		// final Vector[] out, final int outSize) {
		// final Body bodyToExplode = modelObject.getB2DBodyHandler().getBody();
		// final float diameter = getDiameterFromModelOBject(modelObject);
		// final int explosionCount = getExplosionCutsSize(outSize,
		// bodyToExplode);
		//
		// ContactEdge contacts = bodyToExplode.getContactList();
		// int currentExplosionCount = 0;
		//
		// if (contacts != null) {
		// do {
		//
		// Vector otherWorldCenter = contacts.getOther().getWorldCenter();
		// setRandomVectorsAroundPoint(diameter, impact,
		// out[currentExplosionCount], out[currentExplosionCount + 1]);
		// currentExplosionCount += 2;
		//
		// for (final Vector impact : getWorldManifold().points) {
		//
		//
		// if (currentExplosionCount >= explosionCount) {
		// return currentExplosionCount;
		// }
		// }
		// contacts = contacts.getNext();
		// if (contacts == null) {
		// // Restart from the beginning of the contact list
		// contacts = bodyToExplode.getContactList();
		// }
		// } while (currentExplosionCount < explosionCount);
		// } else {
		// // No contact => No impact => explode from center
		// return CENTRAL.getExplosionCutsToOut(modelObject, out, outSize);
		// }
		//
		// return explosionCount;
		// }
		// },
		// Does not explode
		NONE {
			@Override
			public int getExplosionCutsToOut(final B2DModelObject modelObject, final Vector[] out, final int outSize) {
				return 0;
			}

		};

		private static WorldManifold worldManifold;

		private final static WorldManifold getWorldManifold(){
			if(worldManifold == null){
				worldManifold = GameContext.instance().getBox2D().newWorldManifold();
			}
			return worldManifold;
		}

		/**
		 * The out vector array must be initialized
		 *  - each index must contain a vector
		 *  - size == 2 * GameGOD().getExplosionCuts()
		 * 
		 *  out[i] is the first point to use for the raycast
		 *  out[i+1] is the second point to use for the raycast
		 */
		public abstract int getExplosionCutsToOut(final B2DModelObject modelObject, final Vector[] out, final int outSize);

		private final static void setRandomVectorsAroundPoint(final float diameter, final Vector center, final Vector outA, final Vector outB) {
			final float angle = (float) (Math.random() * Math.PI * 2);
			final Vector currentA = outA.set(diameter, diameter);
			MathUtils.rotatePointToOut(angle, currentA, currentA);
			currentA.addLocal(center);

			final Vector currentB = outB.set(-diameter, -diameter);
			MathUtils.rotatePointToOut(angle, currentB, currentB);
			currentB.addLocal(center);
		}

		private final static float getDiameterFromModelOBject(final B2DModelObject modelObject) {
			final Vector dist = GamePool.instance().popVector();
			dist.set(modelObject.getGOD().getShapeDefition().getSize().width, modelObject.getGOD().getShapeDefition().getSize().height);
			final float diameter = dist.length();
			GamePool.instance().pushVector(1);
			return diameter;
		}

		private final static int getExplosionCutsSize(final int outSize, final Body bodyToExplode){
			return Math.min(outSize, 4 + 2 * (int) (bodyToExplode.getMass()));
		}
	}

	public ModelObjectExploder(final AbstractModel model) {
		this.model = model;
		this.enterPoints = new HashMap<String, Vector>();
		this.explodingBodies = new HashMap<String, Body>();
	}

	public List<ModelObject> explode(final B2DModelObject modelObject) {
		this.modelObject = modelObject;

		switch (modelObject.getGOD().getShapeDefition().getShapeType()) {
		case POLYGON:
		case BOX:
			// Polygon
			final int arraySize = 2 * model.getGodLoader().getGameGOD().getExplosionCuts();
			final Vector[] explosionPoints = GamePool.instance().popVectors(arraySize);
			final int explosionCount = modelObject.getGOD().getDamageDefinition().getExplodeType().getExplosionCutsToOut(modelObject, explosionPoints, arraySize);
			final List<ModelObject> fragments = explodePolygon(modelObject, explosionPoints, explosionCount);
			GamePool.instance().pushVector(arraySize);
			return fragments;
		case CIRCLE:
			// Circle
			return this.explodeCircle(modelObject);
		}

		return Collections.emptyList();
	};

	private List<ModelObject> explodeCircle(final B2DModelObject circleModelObject) {
		final Body bodyToExplode = circleModelObject.getB2DBodyHandler().getBody();

		// Calculer les points de contact AVANT de remplacer le cercle
		// par un polygone sinon ca pete en NPE

		final int arraySize = 2 * model.getGodLoader().getGameGOD().getExplosionCuts();
		final Vector[] explosionPoints = GamePool.instance().popVectors(arraySize);
		final int explosionCount = ExplodeType.CENTRAL.getExplosionCutsToOut(circleModelObject, explosionPoints, arraySize);

		// Supprimer le cercle et sa représentation dans la vue
		// modelObject.getDamageHandler().destroy();
		this.model.getB2World().destroyBody(bodyToExplode);
		// this.model.fireModelObjectRemoved(modelObject);
		if (((CircleDefinition) circleModelObject.getGOD().getShapeDefition()).getRadius() > this.model.getGodLoader().getGameGOD().getSizeMinCircleFragment()) {
			// Si le cercle à détruire est trop petit, on le supprime juste

			// En créer un nouveau sous forme de polygone
			final B2DModelObject polygonModelObject = this.newPolygonFromCircle(circleModelObject);

			// On ne fait pas de fireAdded ! Cet objet va être découpé donc pas
			// la peine de s'encombrer d'un affichage
			final List<ModelObject> fragments = explodePolygon(polygonModelObject, explosionPoints, explosionCount);
			GamePool.instance().pushVector(arraySize);
			return fragments;
		}
		GamePool.instance().pushVector(arraySize);
		return Collections.emptyList();
	}

	private List<ModelObject> explodePolygon(final B2DModelObject modelObject, final Vector[] explosionPoints, final int explosionCount) {
		final Body bodyToExplode = modelObject.getB2DBodyHandler().getBody();
		this.explodingBodies.put(modelObject.getId(), bodyToExplode);

		// the explosion begins!
		for (int i = 0; i < explosionCount - 1; i += 2) {
			final Vector p1 = explosionPoints[i];
			final Vector p2 = explosionPoints[i + 1];

			this.model.getB2World().raycast(this, p1, p2);
			this.model.getB2World().raycast(this, p2, p1);
			this.enterPoints.clear();
		}

		final List<ModelObject> fragments = new ArrayList<ModelObject>();
		// parcourir aExplodingBodies et faire des fire à la vue
		for (final Body b2dBody : this.explodingBodies.values()) {
			fragments.add((ModelObject) b2dBody.getUserData());
		}
		this.explodingBodies.clear();

		return fragments;
	};

	@Override
	public float reportFixture(final Fixture fixture, final Vector point, final Vector normal, final float fraction){
		final Object userData = fixture.getBody().getUserData();
		ModelObject mdModelObject = null;
		String id = null;
		boolean isExplodingBody = false;
		if (userData instanceof ModelObject) {
			mdModelObject = (ModelObject) userData;
			id = mdModelObject.getId();
			if (this.explodingBodies.containsKey(id)) {
				isExplodingBody = true;
			}
		}

		if (isExplodingBody) {
			// Throughout this whole code I use only one global vector, and that is enterPointsVec. Why do I need it you ask?
			// Well, the problem is that the world.RayCast() method calls this function only when it sees that a given line gets into the body - it doesnt see when the line gets out of it.
			// I must have 2 intersection points with a body so that it can be sliced, thats why I use world.RayCast() again, but this time from B to A - that way the point, at which BA enters the body is the point at which AB leaves it!
			// For that reason, I use a vector enterPointsVec, where I store the points, at which AB enters the body. And later on, if I see that BA enters a body, which has been entered already by AB, I fire the splitObj() function!
			// I need a unique ID for each body, in order to know where its corresponding enter point is - I store that id in the userData of each body.
			if (this.enterPoints.containsKey(id)) {
				// If this body has already had an intersection point, then it
				// now has two intersection points, thus it must be split in two
				// - thats where the splitObj() method comes in.
				this.splitBody(fixture.getBody(), this.enterPoints.get(id), point.clone(), id);
				// Retourner 0 => dit a B2D d'arrêter les events
				// d'intersection
				return 0;
			} else {
				this.enterPoints.put(id, point.clone());
			}
		}
		return 1;
	}

	private void splitBody(final Body slicedBody, final Vector worldA, final Vector worldB, final String id) {
		final Fixture originFixture = slicedBody.getFixtureList().iterator().next();
		final PolygonShape originPolygonShape = (PolygonShape) originFixture.getShape();
		final int verticesCount = originPolygonShape.getVertexCount();

		// First, I destroy the original body and remove its Sprite representation from the childlist.
		this.model.getB2World().destroyBody(slicedBody);

		// The world.RayCast() method returns points in world coordinates, so I use the b2Body.GetLocalPoint() to convert them to local coordinates.;
		final Vector localA = GamePool.instance().popVector();
		slicedBody.getLocalPointToOut(worldA, localA);
		final Vector localB = GamePool.instance().popVector();
		slicedBody.getLocalPointToOut(worldB, localB);

		final int polygonSize = verticesCount + 2;
		// Ajouter tous les points (points de coupe compris) dans un tableau
		final Vector[] polygon = GamePool.instance().popVectors(polygonSize);
		for (int i = 0; i < verticesCount; i++) {
			originPolygonShape.getVertex(i, polygon[i]);
		}
		polygon[verticesCount].set(localA);
		polygon[verticesCount + 1].set(localB);

		final Vector localCenter = slicedBody.getLocalCenter();
		// Trier ce tableau de points dans le sens des aiguilles d'une montre
		MathUtils.arrangeClockwise(polygon, polygonSize, localCenter);

		// On va découper la forme en deux, les points seront tous déjà dans le
		// bon ordre
		final Vector[] shape1Vertices = GamePool.instance().popVectors(Settings.maxPolygonVertices);
		final Vector[] shape2Vertices = GamePool.instance().popVectors(Settings.maxPolygonVertices);

		// On commence par remplir le shape1
		boolean fillShape1 = true;

		int shape1Index = 0;
		int shape2Index = 0;
		for (final Vector currentVertex : polygon) {
			// True si ce point est un point de coupe
			final boolean isCutVertex = currentVertex.x == localA.x && currentVertex.y == localA.y || currentVertex.x == localB.x && currentVertex.y == localB.y;

			if (isCutVertex == true) {
				// On ajoute les points de coupe aux deux formes créées
				shape1Vertices[shape1Index].set(currentVertex);
				shape1Index++;
				shape2Vertices[shape2Index].set(currentVertex);
				shape2Index++;
				// On vient de franchir la ligne de coupe => On ajoutera les
				// points suivant à l'autre forme
				fillShape1 = !fillShape1;
			}else{
				if (fillShape1) {
					shape1Vertices[shape1Index].set(currentVertex);
					shape1Index++;
				}else{
					shape2Vertices[shape2Index].set(currentVertex);
					shape2Index++;
				}
			}

		}

		if (this.addFragment(slicedBody, shape1Vertices, shape1Index, this.model.newId()) == true) {
			this.enterPoints.remove(id);
		}

		if (this.addFragment(slicedBody, shape2Vertices, shape2Index, this.model.newId()) == true) {
			this.enterPoints.remove(id);
		}

		this.explodingBodies.remove(id);

		GamePool.instance().pushVector(verticesCount + 4 + (Settings.maxPolygonVertices * 2));
	}

	public boolean addFragment(final Body slicedBody, final Vector[] shapeVertices, final int verticesCount, final String sId) {
		final B2DModelObject originModelObject = (B2DModelObject) slicedBody.getUserData();
		// Attention oCenterPos est passé en paramètre de newFragmentGOD et y
		// est modifié ! C'est la position du centre du fragment par rapport à
		// la position du centre de l'objet découpé.
		final Vector centerPos = GamePool.instance().popVector().set(0, 0);
		final B2DGameObjectDefiniton god = this.newFragmentObjectDefiniton(originModelObject, shapeVertices, verticesCount, centerPos);
		if (god != null){
			// Calcul de la position du nouvel objet dans le b2World
			// On prend en compte la position de son centre par rapport a la
			// position de l'objet exploded que l'on tranforme via l'angle de ce
			// dernier
			final float angle = slicedBody.getAngle();
			final Vector rotatedCenterPos = GamePool.instance().popVector();
			MathUtils.rotatePointToOut(angle, centerPos, rotatedCenterPos);
			final Vector currentPos = GamePool.instance().popVector().set(slicedBody.getPosition());
			currentPos.addLocal(rotatedCenterPos);
			god.getPosition().set(currentPos);
			GamePool.instance().pushVector(2);

			// Ajuster la vie du fragment, de 20 à 40% de son max
			final B2DModelObject fragmentModelObject = god.newModelObject(this.model);
			if (fragmentModelObject.getDamageHandler() instanceof LifespanDamageHandler){
				((LifespanDamageHandler) fragmentModelObject.getDamageHandler()).setLifespanPercentage((float) (20 + 20 * Math.random()));
			}


			// Ajuster les mouvements du fragment
			final Body fragmentBody = fragmentModelObject.getB2DBodyHandler().getBody();

			float explodeMotionFactor = god.getDamageDefinition().getExplodeMotionFactor();
			final float explodeBlastFactor = god.getDamageDefinition().getExplodeBlastFactor();

			final Vector explosionVector = GamePool.instance().popVector();
			final Vector velocity = GamePool.instance().popVector();

			final Body bodyToExplode = modelObject.getB2DBodyHandler().getBody();

			fragmentBody.setAngularDamping(bodyToExplode.getAngularDamping());
			fragmentBody.setAngularVelocity(bodyToExplode.getAngularVelocity());
			fragmentBody.setLinearDamping(bodyToExplode.getLinearDamping());

			// Pour les objets explosifs
			explosionVector.set(fragmentBody.getWorldCenter()).subtractLocal(bodyToExplode.getPosition()).scaleLocal(explodeBlastFactor);

			// Transmission de la quantité de mouvement : les objets plus petits
			// vont partir plus vite !
			explodeMotionFactor *= 1 - (fragmentBody.getMass() / bodyToExplode.getMass());
			System.out.println("ModelObjectExploder.addFragment() " + fragmentBody.getMass() + " " + explodeMotionFactor);
			velocity.set(bodyToExplode.getLinearVelocity()).scaleLocal(explodeMotionFactor).addLocal(explosionVector);

			fragmentBody.setLinearVelocity(velocity);

			GamePool.instance().pushVector(2);

			// the shape will be also part of the explosion and can explode too
			this.explodingBodies.put(fragmentModelObject.getId(), fragmentBody);
		}

		GamePool.instance().pushVector(1);
		return god != null;
	};

	/**
	 * Pour les fragments
	 * 
	 * @param type
	 * @param vertices
	 * @param factor
	 * @param centerPosParam
	 *            ATTENTION le centerPosParam est passé en paramètre pour
	 *            pouvoir récupèrer le centre lors de la fragementation d'objets
	 * @return
	 */
	private B2DGameObjectDefiniton newFragmentObjectDefiniton(final B2DModelObject sourceModelObject, final Vector[] vertices, final int verticesCount, final Vector worldCenterPos) {

		int numPoints = verticesCount;

		if (numPoints < 3) {
			return null;
		}

		numPoints = MathUtils.toConvexPolygon(vertices, verticesCount);

		final Vector minPos = GamePool.instance().popVector().set(Float.MAX_VALUE, Float.MAX_VALUE);
		final Vector maxPos = GamePool.instance().popVector().set(0, 0);
		for (int i = 0; i < numPoints; i++) {
			final Vector point = vertices[i];
			worldCenterPos.x += point.x;
			worldCenterPos.y += point.y;
			minPos.x = Math.min(minPos.x, point.x);
			minPos.y = Math.min(minPos.y, point.y);
			maxPos.x = Math.max(maxPos.x, point.x);
			maxPos.y = Math.max(maxPos.y, point.y);
		}
		worldCenterPos.x = worldCenterPos.x / numPoints;
		worldCenterPos.y = worldCenterPos.y / numPoints;

		for (int i = 0; i < numPoints; i++) {
			vertices[i].subtractLocal(worldCenterPos);
		}

		final float area = MathUtils.getArea(vertices, numPoints);
		boolean isFragment;
		if (area >= this.model.getGodLoader().getGameGOD().getSizeMaxFragment()) {
			isFragment = false;
		} else if (area >= this.model.getGodLoader().getGameGOD().getSizeMinFragment()) {
			isFragment = true;
		} else {
			return null;
		}

		// Position du centre du polygone dans son référentiel à elle !
		final Vector localCenterPos = new Vector(worldCenterPos.x - minPos.x, worldCenterPos.y - minPos.y);
		final float width = maxPos.x - minPos.x;
		final float height = maxPos.y - minPos.y;

		// La definition de la forme calculée
		final PolygonDefinition destShapeDefinition = PolygonDefinition.polygonBuilder().centerPosition(localCenterPos).vertices(vertices, numPoints).size(width, height).build();

		// La definition des dégats
		final DamageDefinition sourceDamageDefinition = sourceModelObject.getGOD().getDamageDefinition();
		DamageDefinition destDamageDefinition = null;
		if (sourceDamageDefinition instanceof LifespanDefinition) {
			final LifespanDefinition lifespanDefinition = (LifespanDefinition) sourceDamageDefinition;
			if (isFragment) {
				destDamageDefinition = DecayDefinition.decayBuilder().fromOther(lifespanDefinition).decayPercentage(this.model.getGodLoader().getGameGOD().getFragmentDecay()).immediateOrOnDamage(true).explodable(ExplodeType.NONE).build();
			} else {
				// Force central explode for fragments to avoid lag due to
				// getWorldManifold calls
				destDamageDefinition = LifespanDefinition.lifespanBuilder().fromOther(lifespanDefinition).explodable(ExplodeType.CENTRAL).build();
			}
		} else {
			destDamageDefinition = DurationDefinition.builder().duration(this.model.getGodLoader().getGameGOD().getFragmentDuration()).explodable(ExplodeType.NONE).build();
		}


		final ImageDefinition sourceImageDefinition = sourceModelObject.getGOD().getImageDefinition();
		ImageDefinition destImageDefinition;
		String test = "";
		if (!(sourceImageDefinition instanceof TextureDefinition)) {
			// Il s'agit d'une image et non pas d'une texture, il faut donc
			// découper cette image
			Image image = sourceImageDefinition.getImage().getImage();

			final ShapeDefinition sourceShapeDefinition = sourceModelObject.getGOD().getShapeDefition();
			final Dimension viewShapeSize = new Dimension();
			ViewUtils.modelToInitView(sourceShapeDefinition.getSize(), viewShapeSize);

			// TODO Cela ne doit certainement pas fonctionner pour les
			// scaledImages ...
			final float widthRatio = 1;// image.width() / viewShapeSize.width;
			final float heightRatio = 1;// image.height() /	// viewShapeSize.height;

			float imageX = MathUtils.floor(Math.max(0, (image.width() - viewShapeSize.width) / 2) + MathUtils.floor(ViewUtils.toInitView(sourceShapeDefinition.getCenterPosition().x + minPos.x) * widthRatio));
			float imageY = MathUtils.floor(Math.max(0, (image.height() - viewShapeSize.height) / 2) + MathUtils.floor(ViewUtils.toInitView(sourceShapeDefinition.getCenterPosition().y + minPos.y) * heightRatio));

			if (imageX < 0) {
				imageX = 0;
			}

			if (imageY < 0) {
				imageY = 0;
			}

			float imageW = MathUtils.floor(ViewUtils.toInitView(destShapeDefinition.getSize().width) * widthRatio);
			float imageH = MathUtils.floor(ViewUtils.toInitView(destShapeDefinition.getSize().height) * heightRatio);

			if (imageX + imageW > image.width()) {
				imageW = image.width() - imageX;
			}

			if (imageY + imageH > image.height()) {
				imageH = image.height() - imageY;
			}

			final String src = image.width() + " " + image.height();

			image = image.subImage(imageX, imageY, imageW, imageH);

			final GameImage gameImage = new GameImage(image);
			destImageDefinition = ImageDefinition.imageBuilder().image(gameImage).build();
			test = "[" + imageX + " " + imageY + " " + imageW + " " + imageH + "]  " + src;
		} else {
			destImageDefinition = sourceImageDefinition;
		}
		final LayerDefinition destLayerDefinition = LayerDefinition.builder().fromTexturedShapeDefinition(destShapeDefinition).build();

		final B2DBodyDefinition sourceBodyDefinition = sourceModelObject.getGOD().getB2DBodyDefition();
		final B2DBodyDefinition destBodyDefinition = B2DBodyDefinition.builder().fromOther(sourceBodyDefinition).bodyType(BodyType.DYNAMIC).fixedRotation(false).build();

		final Vector position = GamePool.instance().popVector();
		sourceModelObject.getUpdateHandler().getCurrentPositionToOut(position);

		final B2DGameObjectDefiniton definition = B2DGameObjectDefiniton.b2dBuilder()
				.selectable(sourceModelObject.getGOD().isSelectable())
				.b2dBodyDefinition(destBodyDefinition)
				.shapeDefinition(destShapeDefinition)
				.damageDefinition(destDamageDefinition)
				.updateDefinition(B2DUpdateDefinition.builder().build())
				.position(position)
				.angle(sourceModelObject.getUpdateHandler().getCurrentRotation())
				.layerDefinition(destLayerDefinition).imageDefinition(destImageDefinition)
				.build();
		definition.test = test;

		GamePool.instance().pushVector(3);

		return definition;
	}

	/**
	 * Pour les cercles
	 * 
	 * @param modelObject
	 * @return
	 */
	private B2DModelObject newPolygonFromCircle(final B2DModelObject modelObject) {
		final float circleRadius = ((CircleDefinition) modelObject.getGOD().getShapeDefition()).getRadius();

		final int maxPolygonVertices = this.model.getGodLoader().getGameGOD().getMaxPolygonVertices();
		final List<Vector> circlePoints = new ArrayList<Vector>();
		int circleSteps = (int) (2 * maxPolygonVertices * circleRadius);
		if (circleSteps < 4) {
			circleSteps = 4;
		} else if (circleSteps > maxPolygonVertices) {
			circleSteps = maxPolygonVertices;
		}
		for (int i = 0; i < circleSteps; i++) {
			circlePoints.add(new Vector ((float) (circleRadius * Math.cos(2 * Math.PI / circleSteps * i)), (float) (circleRadius * Math.sin(2 * Math.PI / circleSteps * i))));
		}

		final Vector centerOffset = new Vector (circleRadius, circleRadius);

		final PolygonDefinition shapeDefinition = PolygonDefinition.polygonBuilder().centerPosition(centerOffset).vertices(circlePoints.toArray(new Vector[0])).size(2 * circleRadius, 2 * circleRadius).build();
		final ImageDefinition imageDefinition = modelObject.getGOD().getImageDefinition();
		final LayerDefinition layerDefinition = LayerDefinition.builder().fromTexturedShapeDefinition(shapeDefinition).build();

		final Vector position = GamePool.instance().popVector();
		modelObject.getUpdateHandler().getCurrentPositionToOut(position);

		final B2DGameObjectDefiniton definition = B2DGameObjectDefiniton.b2dBuilder()
				.b2dBodyDefinition(modelObject.getGOD().getB2DBodyDefition())
				.shapeDefinition(shapeDefinition)
				.selectable(modelObject.getGOD().isSelectable())
				.updateDefinition(B2DUpdateDefinition.builder().build())
				.damageDefinition(modelObject.getGOD().getDamageDefinition())
				.position(position)
				.angle(modelObject.getUpdateHandler().getCurrentRotation())
				.layerDefinition(layerDefinition)
				.imageDefinition(imageDefinition)
				.build();

		GamePool.instance().pushVector(1);

		return definition.newModelObject(this.model);

	}

	public static void main(final String[] args) {
		final Vector bodyToExplodePosition = new Vector(4, 5);

		final float diameter = 2f;

		// Math.random() *
		// choosing a random angle
		final float angleA = (float) (0.0 * Math.PI * 2);
		final Vector currentA = new Vector().set(diameter, diameter);
		MathUtils.rotatePointToOut(angleA, currentA, currentA);
		currentA.addLocal(bodyToExplodePosition);

		final Vector currentB = new Vector().set(diameter, diameter);
		final float angleB = (float) (angleA - Math.PI);
		MathUtils.rotatePointToOut(angleB, currentB, currentB);
		currentB.addLocal(bodyToExplodePosition);

		System.out.println(currentA);
		System.out.println(currentB);
	}
}




