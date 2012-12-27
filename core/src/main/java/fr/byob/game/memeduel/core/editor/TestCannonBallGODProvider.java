package fr.byob.game.memeduel.core.editor;

import java.util.ArrayList;
import java.util.List;

import pythagoras.f.Vector;
import fr.byob.game.memeduel.core.god.AllGODLoader;
import fr.byob.game.memeduel.core.god.cannon.CannonBallGOD;

public class TestCannonBallGODProvider implements CannonBallGODProvider {

	private final AllGODLoader loader;
	private String type;
	private List<String> firedCannonBalls = new ArrayList<String>();

	public TestCannonBallGODProvider(final AllGODLoader loader, final String type) {
		super();
		this.loader = loader;
		this.type = type;
	}

	@Override
	public CannonBallGOD getGOD(final Vector position, final float angle) {
		firedCannonBalls.add(type);
		return CannonBallGOD.cannonBallBuilder().fromType(loader, type).position(position).angle(angle).build();
	}

	public void setType(final String type) {
		this.type = type;
	}

	@Override
	public String getCurrentType() {
		return type;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public int getRemainingCannonBallCount() {
		return 0;
	}

	public List<String> getFiredCannonBalls() {
		return firedCannonBalls;
	}
}
