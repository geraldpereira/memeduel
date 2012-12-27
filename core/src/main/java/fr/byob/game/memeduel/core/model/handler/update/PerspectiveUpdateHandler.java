package fr.byob.game.memeduel.core.model.handler.update;

import playn.core.PlayN;
import pythagoras.f.Vector;
import fr.byob.game.memeduel.core.ViewUtils;
import fr.byob.game.memeduel.core.god.LandscapeGOD;
import fr.byob.game.memeduel.core.god.update.PerspectiveUpdateDefinition;
import fr.byob.game.memeduel.core.model.object.DefaultModelObject;

public class PerspectiveUpdateHandler extends AbstractUpdateHandler<DefaultModelObject> {

	private final Vector scroll = new Vector();

	public void setScroll(final Vector scroll) {
		this.scroll.set(scroll);
	}

	@Override
	public void update(final float delta) {
		final float heightModel = ViewUtils.toInitModel(PlayN.graphics().height());
		final LandscapeGOD god = (LandscapeGOD) this.modelObject.getGOD();
		final PerspectiveUpdateDefinition perspectiveUpdateDefinition = god.getUpdateDefinition();

		final float posX = this.initialPosition.x + this.scroll.x * perspectiveUpdateDefinition.getScrollFactor().x;
		final float posY = this.initialPosition.y + Math.max(0, this.scroll.y - heightModel) * perspectiveUpdateDefinition.getScrollFactor().y;
		this.currentPosition.x = posX;
		this.currentPosition.y = posY;
	}
}
