package fr.byob.game.memeduel.core.model.handler.damage;

import fr.byob.game.memeduel.core.GameImage;
import fr.byob.game.memeduel.core.model.handler.AbstractModelHandler;
import fr.byob.game.memeduel.core.model.object.B2DModelObject;

/**
 * Un handler permettant de supprimer des objets en édition, meme s'ils ne sont
 * à la base pas damageable
 * 
 * @author Kojiro
 * 
 */
public class TrashableDamageHandler extends AbstractModelHandler<B2DModelObject> implements DamageHandler {

	private boolean destroyed;


	@Override
	public void init() {
		this.destroyed = false;
	}

	@Override
	public boolean update(final float delta) {
		return false;
	}

	@Override
	public boolean isDestroyed() {
		return this.destroyed;
	}

	@Override
	public boolean isDamaged() {
		return this.destroyed;
	}

	@Override
	public void destroy() {
		this.destroyed = true;
	}

	@Override
	public boolean damage(final float damageAmount) {
		return false;
	}

	@Override
	public GameImage getDamageMDImage() {
		return null;
	}
}
