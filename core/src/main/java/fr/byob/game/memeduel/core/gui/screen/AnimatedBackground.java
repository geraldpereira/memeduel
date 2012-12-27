package fr.byob.game.memeduel.core.gui.screen;

import static playn.core.PlayN.graphics;
import playn.core.Canvas;
import playn.core.CanvasImage;
import playn.core.Gradient;
import playn.core.GroupLayer;
import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.Layer;
import playn.core.PlayN;
import pythagoras.f.Vector;
import tripleplay.anim.Animation;
import tripleplay.anim.Animator;
import fr.byob.game.memeduel.core.GameLoop;
import fr.byob.game.memeduel.core.GamePool;
import fr.byob.game.memeduel.core.MathUtils;
import fr.byob.game.memeduel.core.MemeDuelLoader;
import fr.byob.game.memeduel.core.Paintable;
import fr.byob.game.memeduel.core.Updatable;
import fr.byob.game.memeduel.core.god.layer.LayerDefinition;
import fr.byob.game.memeduel.core.view.handler.image.SunGlowImageHandler;

public class AnimatedBackground implements Paintable, Updatable {

	private float _elapsed;
	private final Animator anim = Animator.create();
	private final GroupLayer layer;

	public AnimatedBackground() {
		final float worldWidth = PlayN.graphics().width();
		final float worldHeight = PlayN.graphics().height();
		this.layer = PlayN.graphics().createGroupLayer(worldWidth, worldHeight);
		this.layer.setDepth(-1);
		PlayN.graphics().rootLayer().add(this.layer);

		// The gradiant background
		final CanvasImage bgImage = graphics().createImage(worldWidth, worldHeight);
		final Canvas canvas = bgImage.canvas();
		final Gradient gradient = graphics().createLinearGradient(0, 0, 0, worldHeight, new int[] { 0xFFC3E9FC, 0xFFFEC763 }, new float[] { 0.2f, 0.9f });
		canvas.setFillGradient(gradient);
		canvas.fillRect(0, 0, worldWidth, worldHeight);
		final ImageLayer bg = graphics().createImageLayer(bgImage);
		bg.setDepth(-10);

		layer.add(bg);

		// The sun
		final float sunDuration = 120000;
		final ImageLayer sunLayer = PlayN.graphics().createImageLayer(MemeDuelLoader.SUN.getImage());
		final float sunImageWidth = MemeDuelLoader.SUN.getImage().width();
		final float sunImageHeight = MemeDuelLoader.SUN.getImage().height();
		sunLayer.setDepth(-5);
		final float sunHorizonY = worldHeight * 0.3f;

		final Vector left = GamePool.instance().popVector().set(-sunImageWidth * 2, sunHorizonY);
		final Vector right = GamePool.instance().popVector().set(worldWidth + sunImageWidth, sunHorizonY);
		final Vector sunRotationCenter = MathUtils.circleCenter(left, GamePool.instance().popVector().set(worldWidth / 2, 0), right);
		final float radius = MathUtils.distance(left, sunRotationCenter);
		final float leftAngle = (float) MathUtils.computeAngleRadians(sunRotationCenter, left);
		final float rightAngle = (float) MathUtils.computeAngleRadians(sunRotationCenter, right);
		final SunAnimation sunAnim = new SunAnimation(sunLayer, sunRotationCenter, radius, 0, sunHorizonY);
		anim.addAt(this.layer, sunLayer, -sunLayer.width(), 0).then().repeat(sunLayer).tween(sunAnim).from(rightAngle).to(leftAngle).in(sunDuration).easeOut();

		GamePool.instance().pushVector(3);

		// The sunGlow
		final SunGlowImageHandler handler = new SunGlowImageHandler();
		handler.setLayerDefinition(LayerDefinition.builder().layerSize(sunImageWidth * 2, sunImageHeight * 2).build());
		final Image image = handler.createImage();
		final ImageLayer sunGlowLayer = PlayN.graphics().createImageLayer(image);
		sunGlowLayer.setDepth(-6);
		sunGlowLayer.setOrigin(sunImageWidth / 2, sunImageHeight / 2);
		final SunAnimation sunGlowAnim = new SunAnimation(sunGlowLayer, sunRotationCenter, radius, 0, sunHorizonY);
		anim.addAt(this.layer, sunGlowLayer, -sunGlowLayer.width(), 0).then().repeat(sunGlowLayer).tween(sunGlowAnim).from(rightAngle).to(leftAngle).in(sunDuration).easeOut();

		// The clouds
		final float cloudAnimDuration = 70000;
		final ImageLayer cloudLayer = PlayN.graphics().createImageLayer(MemeDuelLoader.CLOUD_I_LIED.getImage());
		anim.addAt(this.layer, cloudLayer, -cloudLayer.width(), worldHeight * 0.12f).then().repeat(cloudLayer).tween(new CloudAnimation(cloudLayer)).to(worldWidth).in(cloudAnimDuration);

		final float cloud2AnimDuration = 90000;
		final ImageLayer cloud2Layer = PlayN.graphics().createImageLayer(MemeDuelLoader.CLOUD_SWEET.getImage());
		anim.addAt(this.layer, cloud2Layer, worldWidth, worldHeight * 0.1f).then().repeat(cloud2Layer).tween(new CloudAnimation(cloud2Layer)).to(-cloud2Layer.width()).in(cloud2AnimDuration);

		// Ground
		addStaticImageLayer(MemeDuelLoader.GROUND.getImage(), -2, 0);

		// Foregroud
		addStaticImageLayer(MemeDuelLoader.FOREGROUND.getImage(), -3, 110);

		// Middleground
		addStaticImageLayer(MemeDuelLoader.MIDDLEGROUND.getImage(), -4, 60);

	}

	private void addStaticImageLayer(final Image image, final int depth, final float yOffset) {
		final float worldWidth = PlayN.graphics().width();
		final float worldHeight = PlayN.graphics().height();
		final ImageLayer staticLayer = PlayN.graphics().createImageLayer(image);
		final float staticLayerHeight = image.height();
		final float staticLayerWidth = image.width();
		if (worldWidth > staticLayerWidth) {
			staticLayer.setRepeatX(true);
			staticLayer.setWidth(worldWidth);
		} else {
			staticLayer.setWidth(staticLayerWidth);
		}
		staticLayer.setHeight(staticLayerHeight);
		staticLayer.setTranslation(0, worldHeight - staticLayerHeight - yOffset);
		staticLayer.setDepth(depth);
		layer.add(staticLayer);

	}

	private static class SunAnimation implements Animation.Value {
		private final ImageLayer layer;
		private final Vector sunRotationCenter;
		private final float radius;
		private final float maxScalePos; // Position Y pour laquelle le scale
		// sera max
		private final float minScalePos; // Position Y pour laquelle le scale
		// sera min
		private final float minDrawScale = 1.0f;
		private final float maxDrawScale = 0.5f;
		private final float m;
		private final float p;


		public SunAnimation(final ImageLayer layer, final Vector sunRotationCenter, final float radius, final float minScalePos, final float maxScalePos) {
			this.layer = layer;
			this.sunRotationCenter = sunRotationCenter;
			this.radius = radius;
			this.maxScalePos = maxScalePos;
			this.minScalePos = minScalePos;

			this.m = (minDrawScale - maxDrawScale) / (this.minScalePos - this.maxScalePos);
			this.p = minDrawScale - this.m * this.minScalePos;
		}

		@Override
		public float initial() {
			return layer.transform().rotation();
		}

		@Override
		public void set(final float value) {

			final float x = sunRotationCenter.x + (float) (Math.cos(value) * radius);
			final float y = sunRotationCenter.y - (float) (Math.sin(value) * radius);

			layer.transform().setTx(x);
			layer.transform().setTy(y);

			// scale max si le point.y est >= groundPos -1
			// scale min si le point.y est <= worldHeight /2
			float drawScale = 1;
			if (y >= this.maxScalePos) {
				drawScale = maxDrawScale;
			} else if (y <= this.minScalePos) {
				drawScale = minDrawScale;
			} else {
				drawScale = this.m * y + this.p;
			}
			layer.setScale(drawScale);
		}

	}


	private static class CloudAnimation implements Animation.Value {

		private final Layer layer;
		private float yOffset;

		public CloudAnimation(final Layer layer) {
			this.layer = layer;
		}

		@Override
		public float initial() {
			this.yOffset = layer.transform().ty();
			return layer.transform().tx();
		}

		@Override
		public void set(final float value) {
			layer.transform().setTx(value);
			layer.transform().setTy(yOffset + 10 * (float) Math.sin(value / 50));
		}

	}

	public void start() {
		GameLoop.instance().register((Paintable) this);
		GameLoop.instance().register((Updatable) this);
	}

	public void stop() {
		GameLoop.instance().deregister((Paintable) this);
		GameLoop.instance().deregister((Updatable) this);
	}

	@Override
	public void update(final float delta) {
		_elapsed += delta;
	}

	@Override
	public void paint(final float alpha) {
		anim.update(_elapsed + alpha * 32);
	}

}
