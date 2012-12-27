package fr.byob.game.memeduel.core.view;

import static playn.core.PlayN.graphics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import playn.core.Canvas;
import playn.core.CanvasImage;
import playn.core.GroupLayer;
import playn.core.ImageLayer;
import playn.core.PlayN;
import pythagoras.f.Vector;
import fr.byob.game.box2d.Disposable;
import fr.byob.game.box2d.callbacks.DebugDraw;
import fr.byob.game.memeduel.core.GameContext;
import fr.byob.game.memeduel.core.GamePool;
import fr.byob.game.memeduel.core.MathUtils;
import fr.byob.game.memeduel.core.MemeDuel;
import fr.byob.game.memeduel.core.Paintable;
import fr.byob.game.memeduel.core.ViewUtils;
import fr.byob.game.memeduel.core.controller.PointerPosition;
import fr.byob.game.memeduel.core.god.AllGODLoader;
import fr.byob.game.memeduel.core.god.ColoredFillerGOD;
import fr.byob.game.memeduel.core.god.LandscapeGOD;
import fr.byob.game.memeduel.core.god.layer.LayerDefinition;
import fr.byob.game.memeduel.core.model.AbstractModel;
import fr.byob.game.memeduel.core.model.Border;
import fr.byob.game.memeduel.core.model.ModelListener;
import fr.byob.game.memeduel.core.model.handler.update.PerspectiveUpdateHandler;
import fr.byob.game.memeduel.core.model.object.ModelObject;
import fr.byob.game.memeduel.core.view.object.DynamicViewObject;
import fr.byob.game.memeduel.core.view.object.StaticViewObject;

public abstract class AbstractView implements ModelListener, Paintable, View, Disposable {

	// scroll values in meters
	private final Vector currentScroll = new Vector ();
	// private Vector scrollInertia = null;
	private final Vector maxScroll = new Vector ();
	private final Vector scrollTarget = new Vector ();
	// If 0, go directly to the scroll / drawscale target at next paint()
	// if 1 will go very fast
	// if 10 will go very slowly
	private float scrollInvSpeed = 0;
	private float drawscaleInvSpeed = 0;

	private float drawScaleTarget = ViewUtils.initDrawScale;
	private float currentDrawScale = this.drawScaleTarget;

	private boolean hasEnvChanged = true;

	protected final AbstractModel model;

	protected final Map<String, DynamicViewObject> dynamicViewObjects = new HashMap<String, DynamicViewObject>(0);
	protected final List<DynamicViewObject> perspectiveViewObjects = new ArrayList<DynamicViewObject>(0);
	protected final List<StaticViewObject> cannonBallTrace = new ArrayList<StaticViewObject>(0);
	protected final List<StaticViewObject> staticViewEnvObjects = new ArrayList<StaticViewObject>(0);

	// main layer that holds the world. note: this gets scaled to world space
	private GroupLayer worldLayer;
	private GroupLayer staticLayerBack;
	private GroupLayer dynamicLayer;
	private GroupLayer staticLayerFront;

	// DEBUG
	private DebugDraw debugDraw;
	private ImageLayer debugLayer;

	// Coefficient directeur et décalage : voir
	// http://dundee.pagesperso-orange.fr/maths/cours/droite3.htm
	float maxScalePos; // Position Y pour laquelle le scale sera max
	float minScalePos; // Position Y pour laquelle le scale sera min
	float m;
	float p;

	public AbstractView(final AbstractModel model) {
		this.model = model;
		model.addListener(this);
		model.getGodLoader();
	}

	private void initLayers(final AllGODLoader godLoader) {
		final int width = graphics().width();
		final int height = graphics().height();
		final CanvasImage bgImage = graphics().createImage(width, height);
		final Canvas canvas = bgImage.canvas();
		if (MemeDuel.showTextures) {
			canvas.setFillColor(0xFFC3E9FC);
		} else {
			canvas.setFillColor(0xFFFFFFFF);
		}
		canvas.fillRect(0, 0, width, height);
		final ImageLayer bg = graphics().createImageLayer(bgImage);
		bg.setDepth(0);

		// create our world layer (scaled to "world space")
		this.worldLayer = graphics().createGroupLayer();
		this.worldLayer.setDepth(1);
		this.worldLayer.setScale(ViewUtils.getInitPhysUnitPerScreenUnit());
		this.staticLayerBack = graphics().createGroupLayer();
		staticLayerBack.add(bg);
		this.dynamicLayer = graphics().createGroupLayer();
		this.staticLayerFront = graphics().createGroupLayer();

		if (MemeDuel.showTextures) {
			this.worldLayer.add(this.staticLayerBack);
			this.worldLayer.add(this.dynamicLayer);
			this.worldLayer.add(this.staticLayerFront);
		}
	}

	@Override
	public void loadViewObjects(final AllGODLoader godLoader) {
		this.initLayers(godLoader);

		final float worldWidth = godLoader.getGameGOD().getWorld().getWidth();
		final float displayableWorldWidth = ViewUtils.toInitModel(PlayN.graphics().width() / godLoader.getGameGOD().getMinDrawScale());
		final float maxWorldWidth = Math.max(worldWidth, displayableWorldWidth);
		final float minWorldWidth = Math.min(worldWidth, displayableWorldWidth);
		// 2 de marge de securité
		final float halfWidthDiff = 2 + (maxWorldWidth - minWorldWidth) / 2;

		final float worldHeight = godLoader.getGameGOD().getWorld().getHeight();
		final float displayableWorldHeight = ViewUtils.toInitModel(PlayN.graphics().height() / godLoader.getGameGOD().getMinDrawScale());
		final float maxWorldHeight = Math.max(worldHeight, displayableWorldHeight);
		final float minWorldHeight = Math.min(worldHeight, displayableWorldHeight);
		// 2 de marge de securité
		final float halfHeightDiff = 2 + (maxWorldHeight - minWorldHeight) / 2;

		final StaticViewObject groundViewObject = (StaticViewObject) godLoader.getGameGOD().getWorld().newViewObject(this, null);
		this.staticViewEnvObjects.add(groundViewObject);

		final ColoredFillerGOD blackFillerLeft = this.newFiller(-halfWidthDiff, 0, halfWidthDiff, worldHeight);
		this.staticViewEnvObjects.add(blackFillerLeft.newViewObject(this, null));

		final ColoredFillerGOD blackFillerRight = this.newFiller(worldWidth, 0, halfWidthDiff, worldHeight);
		this.staticViewEnvObjects.add(blackFillerRight.newViewObject(this, null));

		final ColoredFillerGOD blackFillerBottom = this.newFiller(-halfWidthDiff, worldHeight, worldWidth + halfWidthDiff * 2, halfHeightDiff);
		this.staticViewEnvObjects.add(blackFillerBottom.newViewObject(this, null));

		final ColoredFillerGOD blackFillerTop = this.newFiller(-halfWidthDiff, -halfHeightDiff, worldWidth + halfWidthDiff * 2, halfHeightDiff);
		this.staticViewEnvObjects.add(blackFillerTop.newViewObject(this, null));

		this.initDrawScaleCurve(godLoader);
	}

	protected ColoredFillerGOD newFiller(final float x, final float y, final float width, final float height) {
		return ColoredFillerGOD.fillerBuilder().fillColor(0xFF000000).layerDefinition(LayerDefinition.builder().layerSize(ViewUtils.toInitView(width), ViewUtils.toInitView(height)).depth(100).zOrder(ZOrder.FRONT).build()).imageDefinition(null).position(x, y).angle(0).build();
	}

	private void initDrawScaleCurve(final AllGODLoader godLoader) {
		final float groundHeight = godLoader.getGameGOD().getWorld().getGroundHeight();
		final float worldHeight = godLoader.getGameGOD().getWorld().getHeight();
		final float minAutofocusDrawScale = godLoader.getGameGOD().getMinAutofocusDrawScale();
		final float maxAutofocusDrawScale = godLoader.getGameGOD().getMaxAutofocusDrawScale();
		this.maxScalePos = worldHeight - groundHeight - 1;
		this.minScalePos = worldHeight / 4;
		this.m = (minAutofocusDrawScale - maxAutofocusDrawScale) / (this.minScalePos - this.maxScalePos);
		this.p = minAutofocusDrawScale - this.m * this.minScalePos;
	}

	public void modelLoaded() {
		final float worldHeight = this.model.getGodLoader().getGameGOD().getWorld().getHeight();
		final float worldWidth = this.model.getGodLoader().getGameGOD().getWorld().getWidth();

		if (MemeDuel.showDebugDraw) {
			final CanvasImage debugImage = graphics().createImage(MathUtils.ceil(this.toView(worldWidth)), MathUtils.ceil(this.toView(worldHeight)));
			debugLayer = graphics().createImageLayer(debugImage);
			debugLayer.setDepth(100);
			graphics().rootLayer().add(debugLayer);

			this.debugDraw = GameContext.instance().getBox2D().newDebugDraw(debugImage);
			debugDraw.setCamera(0, 0, this.getPhysUnitPerScreenUnit());
			this.model.getB2World().setDebugDraw(debugDraw);
		}

		final Vector focus = GamePool.instance().popVector().set(0, worldHeight);
		this.focusOn(focus, 0, 0.8f, 0);
		GamePool.instance().pushVector(1);

		final AllGODLoader godLoader = this.model.getGodLoader();
		final List<LandscapeGOD> landscapeGODs = godLoader.getGameGOD().getLandscapes();
		// Charger les landscapeGODs dans des landscape entities
		for (final LandscapeGOD landscapeGOD : landscapeGODs) {
			final ModelObject modelObject = landscapeGOD.newModelObject(this.model);
			this.perspectiveViewObjects.add(landscapeGOD.newViewObject(this, modelObject));
		}

		this.staticViewEnvObjects.add((StaticViewObject) godLoader.getGameGOD().getSun().newViewObject(this, null));

		// final List<CloudGOD> cloudGODs = godLoader.getClouds();
		// // Charger les landscapeGODs dans des landscape entities
		// for (final CloudGOD cloudGOD : cloudGODs) {
		// final ModelObject modelObject = cloudGOD.newModelObject(this.model);
		// this.dymanicEnvViewObjects.add(cloudGOD.newViewObject(this,
		// modelObject));
		// }
		//
		// final ModelObject modelObject =
		// godLoader.getSunGlow().newModelObject(this.model);
		// this.dymanicEnvViewObjects.add(godLoader.getSunGlow().newViewObject(this,
		// modelObject));
	}

	public void update(final PointerPosition pointerPosition, final float delta) {

		if (this.hasEnvChanged) {
			if (this.currentScroll.x != this.scrollTarget.x || this.currentScroll.y != this.scrollTarget.y) {
				// Nous devons mettre à jour le scroll
				if (this.scrollInvSpeed == 0) {
					this.currentScroll.x = this.scrollTarget.x;
					this.currentScroll.y = this.scrollTarget.y;
				} else {
					this.currentScroll.x += (this.scrollTarget.x - this.currentScroll.x) / delta * this.scrollInvSpeed;
					this.currentScroll.y += (this.scrollTarget.y - this.currentScroll.y) / delta * this.scrollInvSpeed;
				}
			}

			if (this.currentDrawScale != this.drawScaleTarget) {
				if (this.drawscaleInvSpeed == 0) {
					this.currentDrawScale = this.drawScaleTarget;
				} else {
					this.currentDrawScale += (this.drawScaleTarget - this.currentDrawScale) / delta * this.drawscaleInvSpeed;
				}
			}

			this.worldLayer.setScale(this.getPhysUnitPerScreenUnit());
			this.worldLayer.setTranslation(this.toView(-this.currentScroll.x), this.toView(-this.currentScroll.y));

			for (final DynamicViewObject vo : this.perspectiveViewObjects) {
				((PerspectiveUpdateHandler) vo.getModelObject().getUpdateHandler()).setScroll(this.currentScroll);
				vo.getModelObject().update(delta);
				vo.paint(0);
			}
			if (this.currentScroll.x == this.scrollTarget.x && this.currentScroll.y == this.scrollTarget.y && this.currentDrawScale == this.drawScaleTarget) {
				this.hasEnvChanged = false;
				this.scrollInvSpeed = 0;
				this.drawscaleInvSpeed = 0;
			}
		}

	}

	@Override
	public GroupLayer getStaticLayerBack() {
		return this.staticLayerBack;
	}

	@Override
	public GroupLayer getStaticLayerFront() {
		return this.staticLayerFront;
	}

	@Override
	public GroupLayer getDynamicLayer() {
		return this.dynamicLayer;
	}

	@Override
	public GroupLayer getWorldLayer() {
		return this.worldLayer;
	}

	@Override
	public void objectAdded(final ModelObject modelObject) {
		if (this.dynamicViewObjects.containsKey(modelObject.getId())) {
			return;
		}
		final DynamicViewObject dynamicViewObject = (DynamicViewObject) modelObject.getGOD().newViewObject(this, modelObject);
		this.dynamicViewObjects.put(modelObject.getId(), dynamicViewObject);
	}

	@Override
	public void objectRemoved(final ModelObject modelObject) {
		final DynamicViewObject dynamicViewObject = this.dynamicViewObjects.remove(modelObject.getId());
		if (dynamicViewObject != null) {
			dynamicViewObject.removeFromView(this);
		}
	}

	@Override
	public void objectHitBorder(final ModelObject modelObject, final Border border, final Vector position) {
	}

	@Override
	public void objectDamaged(final ModelObject modelObject) {
	}

	private float getPhysUnitPerScreenUnit() {
		return this.currentDrawScale / ViewUtils.physUnitPerScreenUnit;
	}

	public float toModel(final float viewValue) {
		return viewValue / this.currentDrawScale * ViewUtils.physUnitPerScreenUnit;
	}

	private float toView(final float modelValue) {
		return modelValue * this.currentDrawScale / ViewUtils.physUnitPerScreenUnit;
	}

	public float getCurrentDrawScale() {
		return this.currentDrawScale;
	}

	public Vector getCurrentScroll() {
		return this.currentScroll;
	}

	/**
	 * Utilisé lorsque l'utilisateur se déplace dans la vue en ne modifiant que
	 * le scroll
	 * 
	 * @param offset
	 * @return
	 */
	public boolean updateScroll(final Vector offset) {
		boolean scrollUpdated = false;
		// Mettre à jour les scroll lors du DND de la souris
		if (offset.x != 0) {
			this.updateScrollX(this.scrollTarget.x + offset.x);
			scrollUpdated = true;
		}
		if (offset.y != 0) {
			this.updateScrollY(this.scrollTarget.y + offset.y);
			scrollUpdated = true;
		}
		if (scrollUpdated) {
			this.hasEnvChanged = true;
			this.scrollInvSpeed = 0;
			this.drawscaleInvSpeed = 0;
		}
		return scrollUpdated;
	}

	/**
	 * 
	 * @param point
	 *            null si on ne modifie que le drawscale, le point est
	 *            automatiquement calculé pour garder la vue centrée sur ce
	 *            qu'on voayait avant modification du drawscale
	 * @param drawScale
	 *            -1 to let the view compute it
	 */
	public void focusOn(final Vector point, final float scrollInvSpeed, float drawScale, final float drawscaleInvSpeed) {

		// Calcul de l'échelle
		if (drawScale == -1) {

			// scale max si le point.y est >= groundPos -1
			// scale min si le point.y est <= worldHeight /2
			if (point.y >= this.maxScalePos) {
				drawScale = this.model.getGodLoader().getGameGOD().getMaxAutofocusDrawScale();
			} else if (point.y <= this.minScalePos) {
				drawScale = this.model.getGodLoader().getGameGOD().getMinAutofocusDrawScale();
			} else {
				drawScale = this.m * point.y + this.p;
			}
		}

		// ne PAS utiliser toModel() car cette méthode se base sur le drawScale
		// courrant pas la cible
		final float windowModelWidth = PlayN.graphics().width() / drawScale * ViewUtils.physUnitPerScreenUnit;
		final float windowModelHeight = PlayN.graphics().height() / drawScale * ViewUtils.physUnitPerScreenUnit;
		this.maxScroll.x = this.model.getGodLoader().getGameGOD().getWorld().getWidth() - windowModelWidth;
		this.maxScroll.y = this.model.getGodLoader().getGameGOD().getWorld().getHeight() - windowModelHeight;

		float x;
		float y;
		if (point == null) {
			final float oldDrawScale = this.currentDrawScale;
			float scrollDiff = this.currentScroll.y * (drawScale - oldDrawScale);
			// float scrollDiff = (this.maxScroll.y - oldMaxScroll.y) / 2
			if (oldDrawScale < this.drawScaleTarget) {
				scrollDiff += 0.5f;
			}

			x = this.currentScroll.x;
			y = this.currentScroll.y + scrollDiff;
		} else {
			// pour le x, position du point - largeur de la fenetre /2
			// largeur de la fenetre /2 = toModel(graphics.width())/2
			x = point.x - windowModelWidth / 2;
			y = point.y - windowModelHeight / 2;
		}


		this.drawScaleTarget = drawScale;
		// Do NOT call this.updateScroll() as the updateScrollX(0) would not be
		// called
		this.updateScrollX(x);
		this.updateScrollY(y);
		this.hasEnvChanged = true;
		this.scrollInvSpeed = scrollInvSpeed;
		this.drawscaleInvSpeed = drawscaleInvSpeed;
	}

	private void updateScrollX(float scrollValue) {
		if (this.maxScroll.x < 0) {
			// Special case => the screen is larger than the world
			scrollValue = this.maxScroll.x / 2;
		} else if (scrollValue < 0) {
			scrollValue = 0;
		} else if (scrollValue > this.maxScroll.x) {
			scrollValue = this.maxScroll.x;
		}
		this.scrollTarget.x = scrollValue;
	};

	private void updateScrollY(float scrollValue) {
		if (this.maxScroll.y < 0) {
			// Special case => the screen is taller than the world
			scrollValue = this.maxScroll.y / 2;
		} else if (scrollValue < 0) {
			scrollValue = 0;
		} else if (scrollValue > this.maxScroll.y) {
			scrollValue = this.maxScroll.y;
		}
		this.scrollTarget.y = scrollValue;
	};

	@Override
	public void paint(final float alpha) {
		for (final DynamicViewObject dynamicViewObject : this.dynamicViewObjects.values()) {
			dynamicViewObject.paint(alpha);
		}

		if (MemeDuel.showDebugDraw) {
			debugDraw.setCamera(this.getCurrentScroll().x, this.getCurrentScroll().y, this.getPhysUnitPerScreenUnit());
			debugDraw.getCanvas().clear();
			this.model.getB2World().drawDebugData();
		}
	}

	@Override
	public void dispose() {
		if (MemeDuel.showDebugDraw) {
			graphics().rootLayer().remove(debugLayer);
		}
	}
}
