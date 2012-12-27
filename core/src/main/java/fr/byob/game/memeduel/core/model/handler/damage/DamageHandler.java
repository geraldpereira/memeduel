package fr.byob.game.memeduel.core.model.handler.damage;

import fr.byob.game.memeduel.core.GameImage;
import fr.byob.game.memeduel.core.model.handler.ModelHandler;

public interface DamageHandler extends ModelHandler {

	public void init();

	public boolean update(float delta);

	public boolean isDestroyed();

	public boolean isDamaged();

	public void destroy();

	public boolean damage(final float damageAmount);

	public GameImage getDamageMDImage();

}
