package fr.byob.game.memeduel.core.editor;

import pythagoras.f.Vector;
import fr.byob.game.memeduel.core.god.AllGODLoader;
import fr.byob.game.memeduel.core.god.meme.MemeGOD;

public class MemeGODProvider implements B2DGODProvider<MemeGOD> {

	private final AllGODLoader loader;
	private String type;

	public MemeGODProvider(final AllGODLoader loader, final String type) {
		super();
		this.loader = loader;
		this.type = type;
	}

	@Override
	public MemeGOD getGOD(final Vector position, final float angle) {
		return MemeGOD.memeBuilder().fromType(loader, type).position(position).angle(angle).build();
	}

	public void setType(final String type) {
		this.type = type;
	}
}
