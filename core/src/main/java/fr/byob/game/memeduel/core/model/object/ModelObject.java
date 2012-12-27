package fr.byob.game.memeduel.core.model.object;

import fr.byob.game.memeduel.core.god.DynamicGameObjectDefinition;
import fr.byob.game.memeduel.core.model.AbstractModel;
import fr.byob.game.memeduel.core.model.handler.damage.DamageHandler;
import fr.byob.game.memeduel.core.model.handler.update.UpdateHandler;

public interface ModelObject {
	public void init(final AbstractModel model);

	public void clear(final AbstractModel model);

	public DynamicGameObjectDefinition getGOD();

	public boolean isDamageable();

	public boolean isUpdatable();

	public UpdateHandler getUpdateHandler();

	public DamageHandler getDamageHandler();

	public boolean update(final float delta);

	public String getId();

	public String getTypeId();

	/**
	 * Should be repainted
	 */
	public void invalidate();

	/**
	 * No more need for a repaint
	 */
	public void validate();

	/**
	 * When marked as damaged, should be repainted
	 * 
	 * @return
	 */
	public boolean isValid();
}
