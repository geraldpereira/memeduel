package fr.byob.game.memeduel.core.animation;

import static playn.core.PlayN.graphics;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import playn.core.CanvasImage;
import pythagoras.f.Dimension;
import pythagoras.f.Vector;
import react.UnitSlot;
import tripleplay.particle.Emitter;
import tripleplay.particle.Generator;
import tripleplay.particle.Particles;
import tripleplay.particle.effect.Alpha;
import tripleplay.particle.effect.Drag;
import tripleplay.particle.effect.Gravity;
import tripleplay.particle.effect.Move;
import tripleplay.particle.init.Lifespan;
import tripleplay.particle.init.Velocity;
import tripleplay.util.Interpolator;
import tripleplay.util.Randoms;
import fr.byob.game.memeduel.core.GamePool;
import fr.byob.game.memeduel.core.MathUtils;
import fr.byob.game.memeduel.core.MemeDuelLoader;
import fr.byob.game.memeduel.core.MemeDuelUtils;
import fr.byob.game.memeduel.core.ViewUtils;
import fr.byob.game.memeduel.core.god.AnimationGOD;
import fr.byob.game.memeduel.core.god.image.TextureDefinition;
import fr.byob.game.memeduel.core.god.layer.LayerDefinition;
import fr.byob.game.memeduel.core.model.AbstractModel;
import fr.byob.game.memeduel.core.model.object.B2DModelObject;
import fr.byob.game.memeduel.core.model.object.ModelObject;
import fr.byob.game.memeduel.core.view.Color;
import fr.byob.game.memeduel.core.view.View;

public abstract class MemeDuelAnimationManager extends AbstractAnimationManager {

	protected final Randoms rando = Randoms.with(new Random());
	protected final Particles parts;
	protected List<Emitter> emitters = new ArrayList<Emitter>();

	protected final AbstractModel model;
	protected final View view;

	public MemeDuelAnimationManager(final AbstractModel model, final View view) {
		this.model = model;
		this.view = view;

		parts = null;

		// new Particles() {
		// @Override
		// public Emitter createEmitter(final int maxParticles, final Image
		// image) {
		// final Emitter emitter = super.createEmitter(maxParticles, image);
		// PlayN.graphics().rootLayer().remove(emitter.layer);
		// view.getDynamicLayer().add(emitter.layer);
		// return emitter;
		// }
		// };
	}

	@Override
	public void update(final float delta) {
		// parts.update(delta);
	}

	private static final Dimension particleSize = new Dimension(4, 4);

	protected void createParticles(final ModelObject modelObject) {
		if (graphics().ctx() == null) {
			return;
		}

		Color color1 = null;
		Color color2 = null;
		if (MemeDuelUtils.isObjectNotFragment(modelObject)) {
			final Color color = ((TextureDefinition) modelObject.getGOD().getImageDefinition()).getStrokeColor();
			color1 = color;
			color2 = color.brighter();
		} else if (MemeDuelUtils.isMeme(modelObject)) {
			// Yellow !
			color1 = new Color("FFEFC438");
			color2 = color1.brighter();
		} else {
			return;
		}

		final B2DModelObject b2dModelObject = (B2DModelObject) modelObject;

		final Vector position = GamePool.instance().popVector();
		b2dModelObject.getUpdateHandler().getCurrentPositionToOut(position);
		final Dimension shapeSize = ViewUtils.modelToInitView(b2dModelObject.getGOD().getShapeDefition().getSize(), MathUtils.tmpDim());
		final int particlesAmount = (int) Math.ceil((shapeSize.width / particleSize.width) * (shapeSize.height / particleSize.height)) / 2;

		// TODO utiliser des images de particules
		// TODO dispatcher les particules sur l'ensemble de la forme
		final CanvasImage image = graphics().createImage(particleSize.width, particleSize.height);
		image.canvas().setFillColor(0xFFFFFFFF);
		image.canvas().fillRect(0, 0, particleSize.width, particleSize.height);

		final Emitter explode1 = createEmitter(image, color1.asInt(), 0.975f, particlesAmount);
		final Emitter explode2 = createEmitter(image, color2.asInt(), 0.95f, particlesAmount);
		note(explode1);
		note(explode2);

		final float tx = position.x;
		final float ty = position.y;

		GamePool.instance().popVector();

		explode1.layer.setScale(LayerDefinition.DEFAULT_LAYER_SCALE.x, LayerDefinition.DEFAULT_LAYER_SCALE.y);
		explode2.layer.setScale(LayerDefinition.DEFAULT_LAYER_SCALE.x, LayerDefinition.DEFAULT_LAYER_SCALE.y);
		explode1.layer.setTranslation(tx, ty);
		explode2.layer.setTranslation(tx, ty);


		explode1.onEmpty.connect(new UnitSlot() {
			@Override
			public void onEmit() {
				emitters.remove(explode1);
				emitters.remove(explode2);
			}
		});
	}

	protected void note(final Emitter emitter) {
		emitter.layer.setDepth(101);
		emitters.add(emitter);
	}

	protected Emitter createEmitter(final CanvasImage image, final int color, final float drag, final int amount) {
		final Emitter explode = parts.createEmitter(amount, image);
		explode.generator = Generator.impulse(amount);
		explode.initters.add(Lifespan.random(rando, 1, 1.5f));
		explode.initters.add(tripleplay.particle.init.Color.constant(color));
		explode.initters.add(tripleplay.particle.init.Transform.layer(explode.layer));
		// explode.initters.add(tripleplay.particle.init.Transform.randomPos(rando,
		// 0, 0, 100, 100));
		explode.initters.add(Velocity.randomNormal(rando, 0, 70));
		explode.initters.add(Velocity.increment(0, 10));
		explode.effectors.add(Alpha.byAge(Interpolator.EASE_IN));
		explode.effectors.add(new Gravity(60));
		explode.effectors.add(new Drag(drag));
		explode.effectors.add(new Drag(drag));
		// explode.effectors.add(new LayerEffector(view.getWorldLayer()));
		explode.effectors.add(new Move());
		return explode;
	}

	protected AnimationGOD newSandParticleGOD(final Vector position) {
		final Vector delta = GamePool.instance().popVector();
		// un peu de random dans les delta des particules
		delta.set((float) (0.2 - 0.4 * Math.random()), (float) -(0.2 + 0.4 * Math.random()));
		final Dimension dim = GamePool.instance().popDimension();
		dim.setSize(25, 25);

		final AnimationGOD anim = this.newMovingAnimationGOD(position, 0, 12, 15, dim, MemeDuelLoader.SAND_PARTICLES, delta, 0);

		GamePool.instance().pushVector(1);
		GamePool.instance().pushDimension(1);
		return anim;
	}

	protected AnimationGOD newSmokeExplosionParticleGOD(final Vector position) {
		final Vector origin = GamePool.instance().popVector();
		origin.set(50, 50);
		final Dimension dim = GamePool.instance().popDimension();
		dim.setSize(100, 100);
		final AnimationGOD anim = this.newStaticAnimationGOD(position, 0, 21, 24, dim, origin, MemeDuelLoader.SMOKE_RING);
		GamePool.instance().pushVector(1);
		GamePool.instance().pushDimension(1);
		return anim;
	}

	protected AnimationGOD newCannonMuzzleFlashGOD(final Vector position, final float angle) {

		final Vector origin = GamePool.instance().popVector();
		origin.set(30, 50);
		final Dimension dim = GamePool.instance().popDimension();
		dim.setSize(150, 100);
		final AnimationGOD anim = this.newStaticAnimationGOD(position, angle, 14, 32, dim, origin, MemeDuelLoader.CANNON_MUZZLE_FLASH);
		GamePool.instance().pushVector(1);
		GamePool.instance().pushDimension(1);
		return anim;

	}


}
