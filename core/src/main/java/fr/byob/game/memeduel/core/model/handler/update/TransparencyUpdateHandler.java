package fr.byob.game.memeduel.core.model.handler.update;

import fr.byob.game.memeduel.core.god.AllGODLoader;
import fr.byob.game.memeduel.core.god.update.TransparencyUpdateDefinition;
import fr.byob.game.memeduel.core.model.object.DefaultModelObject;

public class TransparencyUpdateHandler extends AbstractUpdateHandler<DefaultModelObject> {

	private float currentAlpha;
	private boolean add;

	@Override
	public void init(final AllGODLoader godLoader) {
		super.init(godLoader);
		final TransparencyUpdateDefinition transparencyUpdateDefinition = this.getTransparencyUpdateDefinition();
		this.currentAlpha = transparencyUpdateDefinition.getLowerAlpha();
		this.add = true;
	}

	private TransparencyUpdateDefinition getTransparencyUpdateDefinition() {
		final TransparencyUpdateDefinition transparencyUpdateDefinition = (TransparencyUpdateDefinition) this.modelObject.getGOD().getUpdateDefinition();
		return transparencyUpdateDefinition;
	}

	@Override
	public void update(final float delta) {
		final TransparencyUpdateDefinition transparencyUpdateDefinition = this.getTransparencyUpdateDefinition();

		// Pas besoin d'appeller super.update(), la position ne change pas !
		final float deltaTime = 1 / delta;
		// layer.setAlpha(nPos);
		final float nToAdd = deltaTime * transparencyUpdateDefinition.getAlphaFactor();
		if (this.add) {
			this.currentAlpha += nToAdd;
		} else {
			this.currentAlpha -= nToAdd;
		}

		if (this.currentAlpha > transparencyUpdateDefinition.getUpperAlpha()) {
			this.currentAlpha = transparencyUpdateDefinition.getUpperAlpha();
			this.add = false;
		} else if (this.currentAlpha < transparencyUpdateDefinition.getLowerAlpha()) {
			this.currentAlpha = transparencyUpdateDefinition.getLowerAlpha();
			this.add = true;
		}
	}

	@Override
	public float getCurrentTransparency() {
		return this.currentAlpha;
	}
}
