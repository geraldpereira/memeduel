package fr.byob.game.memeduel.core.model.object;

import fr.byob.game.memeduel.core.god.DynamicGameObjectDefinition;
import fr.byob.game.memeduel.core.model.AbstractModel;
import fr.byob.game.memeduel.core.model.handler.damage.DamageHandler;
import fr.byob.game.memeduel.core.model.handler.update.UpdateHandler;

public class DefaultModelObject implements ModelObject {

	private final String id;
	protected boolean valid = true;

	// protected final AbstractModel model;
	protected final UpdateHandler updateHandler;
	protected final DamageHandler damageHandler;
	private final DynamicGameObjectDefinition god;

	public DefaultModelObject(final String id, final DynamicGameObjectDefinition god, final UpdateHandler updateHandler) {
		this(id, god, updateHandler, null);
	}

	public DefaultModelObject(final String id, final DynamicGameObjectDefinition god, final UpdateHandler updateHandler, final DamageHandler damageHandler) {
		// this.model = model;
		this.updateHandler = updateHandler;
		this.damageHandler = damageHandler;
		this.god = god;
		this.id = id;
	}

	@Override
	public void init(final AbstractModel model) {
		if (this.isUpdatable()) {
			this.updateHandler.setModelObject(this);
			this.updateHandler.init(model.getGodLoader());
		}

		if (this.isDamageable()) {
			this.damageHandler.setModelObject(this);
			this.damageHandler.init();
		}
	}

	@Override
	public void clear(final AbstractModel model) {
	}

	@Override
	public DynamicGameObjectDefinition getGOD() {
		return this.god;
	}

	@Override
	public boolean isDamageable() {
		return this.damageHandler != null;
	}

	@Override
	public boolean isUpdatable() {
		return this.updateHandler != null;
	}

	@Override
	public UpdateHandler getUpdateHandler() {
		return this.updateHandler;
	}

	@Override
	public DamageHandler getDamageHandler() {
		return this.damageHandler;
	}

	@Override
	public boolean update(final float delta) {
		if (this.isUpdatable()) {
			this.updateHandler.update(delta);
		}
		if (this.isDamageable()) {
			return this.damageHandler.update(delta);
		}
		return false;
	}

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public String getTypeId() {
		return "UNDEFINED";
	}

	/**
	 * Should be repainted
	 */
	@Override
	public void invalidate() {
		this.valid = false;
	}

	/**
	 * No more need for a repaint
	 */
	@Override
	public void validate() {
		this.valid = true;
	}

	/**
	 * When marked as damaged, should be repainted
	 * 
	 * @return
	 */
	@Override
	public boolean isValid() {
		return this.valid;
	}

}
