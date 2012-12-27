package fr.byob.game.memeduel.core.gui.screen.particles;

import static playn.core.PlayN.graphics;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import playn.core.CanvasImage;
import playn.core.Font;
import playn.core.Mouse;
import playn.core.Mouse.ButtonEvent;
import playn.core.Mouse.MotionEvent;
import playn.core.Mouse.WheelEvent;
import playn.core.PlayN;
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
import tripleplay.ui.Background;
import tripleplay.ui.Button;
import tripleplay.ui.Group;
import tripleplay.ui.Label;
import tripleplay.ui.Root;
import tripleplay.ui.SimpleStyles;
import tripleplay.ui.Style;
import tripleplay.ui.Stylesheet;
import tripleplay.ui.layout.AxisLayout;
import tripleplay.util.Interpolator;
import tripleplay.util.Randoms;
import fr.byob.game.memeduel.core.GameLoop;
import fr.byob.game.memeduel.core.Updatable;
import fr.byob.game.memeduel.core.gui.screen.AbstractScreen;

public class ParticlesScreen extends AbstractScreen implements Updatable, Mouse.Listener {

	// Particles dans le GameContext

	public ParticlesScreen() {
		GameLoop.instance().register(this);
		PlayN.mouse().setListener(this);
	}

	@Override
	public void showTransitionCompleted() {
		super.showTransitionCompleted();
		if (graphics().ctx() != null) {
			createParticles(_parts, _rando);
		}
	}

	@Override
	public void hideTransitionStarted() {
		super.hideTransitionStarted();
		for (final Emitter emitter : _emitters) {
			emitter.destroy();
		}
		_emitters.clear();
	}

	@Override
	public void update(final float delta) {
		super.update(delta);
		_parts.update(delta);
	}

	protected Group createIface() {
		final Group group = new Group(AxisLayout.vertical(), Style.BACKGROUND.is(Background.solid(0xFF000000)));
		if (graphics().ctx() == null) {
			group.add(new Label("Particles are only supported with GL/WebGL.").addStyles(Style.COLOR.is(0xFFFFFFFF)));
		}
		return group;
	}

	protected String name() {
		return "Fireworks";
	}

	protected String title() {
		return "Particles: Fireworks";
	}

	protected void createParticles(final Particles parts, final Randoms rando) {
		final CanvasImage image = graphics().createImage(2, 2);
		image.canvas().setFillColor(0xFFFFFFFF);
		image.canvas().fillRect(0, 0, 4, 2);

		final Emitter explode1 = createEmitter(parts, rando, image, 0xFFFFCD82, 0.975f);
		final Emitter explode2 = createEmitter(parts, rando, image, 0xFFF06969, 0.95f);
		note(explode1);
		note(explode2);

		final float tx = 100 + rando.getFloat(graphics().width() - 200);
		final float ty = 100 + rando.getFloat(graphics().height() - 200);
		explode1.layer.setTranslation(tx, ty);
		explode2.layer.setTranslation(tx, ty);

		explode1.generator = Generator.impulse(200);
		explode2.generator = Generator.impulse(200);

		explode1.onEmpty.connect(new UnitSlot() {
			@Override
			public void onEmit() {
				final float tx = 100 + rando.getFloat(graphics().width() - 200);
				final float ty = 100 + rando.getFloat(graphics().height() - 200);
				explode1.layer.setTranslation(tx, ty);
				explode2.layer.setTranslation(tx, ty);
				explode1.generator = Generator.impulse(200);
				explode2.generator = Generator.impulse(200);
			}
		});
	}

	protected Emitter createEmitter(final Particles parts, final Randoms rando, final CanvasImage image, final int color, final float drag) {
		final Emitter explode = parts.createEmitter(200, image);
		explode.generator = Generator.impulse(200);
		explode.initters.add(Lifespan.random(rando, 1, 1.5f));
		explode.initters.add(tripleplay.particle.init.Color.constant(color));
		// explode.initters.add(Color.constant(0xFFF06969));
		explode.initters.add(tripleplay.particle.init.Transform.layer(explode.layer));
		explode.initters.add(Velocity.randomNormal(rando, 0, 70));
		explode.initters.add(Velocity.increment(0, 10));
		explode.effectors.add(Alpha.byAge(Interpolator.EASE_IN));
		explode.effectors.add(new Gravity(30));
		// explode.effectors.add(new Drag(0.95f));
		explode.effectors.add(new Drag(drag));
		explode.effectors.add(new Move());
		return explode;
	}

	@Override
	public boolean hasAnimatedBackground() {
		return false;
	}

	protected void note(final Emitter emitter) {
		emitter.layer.setDepth(1);
		_emitters.add(emitter);
	}

	public static final Font TITLE_FONT = graphics().createFont("Helvetica", Font.Style.PLAIN, 24);

	public Button back;

	@Override
	public void wasAdded() {
		super.wasAdded();
		_root = iface.createRoot(AxisLayout.vertical().gap(0).offStretch(), stylesheet(), layer);
		_root.addStyles(Style.BACKGROUND.is(background()), Style.VALIGN.top);
		_root.setSize(width(), height());
		final Background bg = Background.solid(0xFFCC99FF).inset(0, 0, 5, 0);
		_root.add(new Group(AxisLayout.horizontal(), Style.HALIGN.left, Style.BACKGROUND.is(bg)).add(this.back = new Button("Back"), new Label(title()).addStyles(Style.FONT.is(TITLE_FONT), Style.HALIGN.center).setConstraint(AxisLayout.stretched())));
		if (subtitle() != null) {
			_root.add(new Label(subtitle()));
		}
		final Group iface = createIface();
		if (iface != null) {
			_root.add(iface.setConstraint(AxisLayout.stretched()));
		}
	}

	@Override
	public void wasRemoved() {
		super.wasRemoved();
		iface.destroyRoot(_root);
		while (layer.size() > 0) {
			layer.get(0).destroy();
		}
	}

	/** Returns an explanatory subtitle for this demo, or null. */
	protected String subtitle() {
		return null;
	}


	/** Returns the stylesheet to use for this screen. */
	@Override
	protected Stylesheet stylesheet() {
		return SimpleStyles.newSheet();
	}

	/** Returns the background to use for this screen. */
	protected Background background() {
		return Background.bordered(0xFFCCCCCC, 0xFFCC99FF, 5).inset(5);
	}

	protected float updateRate() {
		return 16;
	}

	protected Root _root;

	protected final Randoms _rando = Randoms.with(new Random());
	protected final Particles _parts = new Particles();

	protected int _testIdx;
	protected List<Emitter> _emitters = new ArrayList<Emitter>();

	@Override
	public void onMouseDown(final ButtonEvent event) {
	}

	@Override
	public void onMouseUp(final ButtonEvent event) {
	}

	@Override
	public void onMouseMove(final MotionEvent event) {
	}

	float drawScale = 1.0f;

	@Override
	public void onMouseWheelScroll(final WheelEvent event) {
		final float velocity = event.velocity();

		drawScale -= velocity / 200;
		if (drawScale > 3f) {
			drawScale = 3f;
		} else if (drawScale < 1f) {
			drawScale = 1f;
		}
		PlayN.graphics().rootLayer().setScale(drawScale);
		if (_emitters != null && !_emitters.isEmpty()) {
			for (final Emitter emitter : _emitters) {
				emitter.layer.setScale(drawScale);
			}
		}
	}
}
