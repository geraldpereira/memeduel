package fr.byob.game.memeduel.core.editor;

import java.util.List;

import pythagoras.f.Vector;
import fr.byob.game.memeduel.core.god.AllGODLoader;
import fr.byob.game.memeduel.core.god.cannon.CannonBallGOD;

public class PlayCannonBallGODProvider implements CannonBallGODProvider {

	private final AllGODLoader loader;
	private List<String> availableTypes;

	public PlayCannonBallGODProvider(final AllGODLoader loader, final List<String> availableTypes) {
		super();
		this.loader = loader;
		this.availableTypes = availableTypes;
	}

	@Override
	public CannonBallGOD getGOD(final Vector position, final float angle) {
		final String type = availableTypes.remove(0);
		return CannonBallGOD.cannonBallBuilder().fromType(loader, type).position(position).angle(angle).build();
	}

	@Override
	public String getCurrentType() {
		return availableTypes.get(0);
	}


	@Override
	public boolean isEmpty() {
		return availableTypes.isEmpty();
	}

	@Override
	public int getRemainingCannonBallCount() {
		return availableTypes.size();
	}
}
