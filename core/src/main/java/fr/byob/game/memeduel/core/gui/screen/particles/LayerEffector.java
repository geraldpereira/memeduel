package fr.byob.game.memeduel.core.gui.screen.particles;

import static tripleplay.particle.ParticleBuffer.TX;
import static tripleplay.particle.ParticleBuffer.TY;
import playn.core.Layer;
import tripleplay.particle.Effector;

public class LayerEffector extends Effector {

	private final Layer layer;
	private float x, y;

	public LayerEffector(final Layer layer) {
		this.layer = layer;
		x = layer.transform().tx();
		y = layer.transform().ty();
	}

	@Override
	public void apply(final int index, final float[] data, final int start, final float now, final float dt) {
		// layer.transform().scale();
		if (x != layer.transform().tx()) {
			data[start + TX] = x;
		}
		if (y != layer.transform().ty()) {
			data[start + TY] = y;
		}
	}

}
