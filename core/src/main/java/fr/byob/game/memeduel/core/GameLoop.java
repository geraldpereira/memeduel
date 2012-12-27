package fr.byob.game.memeduel.core;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import playn.core.PlayN;

public class GameLoop implements Updatable, Paintable {

	private final static GameLoop instance = new GameLoop();

	protected final Set<Updatable> updatables;
	protected final List<Updatable> updatablesToRegister;
	protected final List<Updatable> updatablesToDeregister;
	protected final Set<Paintable> paintables;
	protected final List<Paintable> paintablesToRegister;
	protected final List<Paintable> paintablesToDeregister;

	private GameLoop() {
		updatables = new LinkedHashSet<Updatable>(16);
		updatablesToRegister = new ArrayList<Updatable>(4);
		updatablesToDeregister = new ArrayList<Updatable>(4);
		paintables = new LinkedHashSet<Paintable>(16);
		paintablesToRegister = new ArrayList<Paintable>(4);
		paintablesToDeregister = new ArrayList<Paintable>(4);
	}

	public static GameLoop instance() {
		return instance;
	}

	@Override
	public void update(final float delta) {
		for (final Updatable updatable : updatablesToRegister) {
			updatables.add(updatable);
		}
		updatablesToRegister.clear();

		for (final Updatable updatable : updatablesToDeregister) {
			updatables.remove(updatable);
		}
		updatablesToDeregister.clear();

		for (final Updatable updatable : updatables) {
			updatable.update(delta);
		}
	}

	@Override
	public void paint(final float alpha) {
		for (final Paintable paintable : paintablesToRegister) {
			paintables.add(paintable);
		}
		paintablesToRegister.clear();

		for (final Paintable paintable : paintablesToDeregister) {
			paintables.remove(paintable);
		}
		paintablesToDeregister.clear();

		for (final Paintable paintable : paintables) {
			paintable.paint(alpha);
		}
	}


	public void register(final Updatable updatable) {
		if (updatable == null) {
			throw new IllegalArgumentException("Updatable cannot be null");
		}
		PlayN.log().info("Registered Updatable " + updatable);
		updatablesToRegister.add(updatable);
	}

	public void deregister(final Updatable updatable) {
		if (updatable == null) {
			throw new IllegalArgumentException("Updatable cannot be null");
		}
		PlayN.log().info("Deregistered Updatable " + updatable);
		updatablesToDeregister.add(updatable);
	}

	public void register(final Paintable paintable) {
		if (paintable == null) {
			throw new IllegalArgumentException("Paintable cannot be null");
		}
		PlayN.log().info("Registered Paintable " + paintable);
		paintablesToRegister.add(paintable);
	}

	public void deregister(final Paintable paintable) {
		if (paintable == null) {
			throw new IllegalArgumentException("Paintable cannot be null");
		}
		PlayN.log().info("Deregistered Paintable " + paintable);
		paintablesToDeregister.add(paintable);
	}
}
