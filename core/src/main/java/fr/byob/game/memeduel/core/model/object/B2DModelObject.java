package fr.byob.game.memeduel.core.model.object;

import fr.byob.game.memeduel.core.god.B2DGameObjectDefiniton;
import fr.byob.game.memeduel.core.model.AbstractModel;
import fr.byob.game.memeduel.core.model.handler.b2d.B2DBodyHandler;
import fr.byob.game.memeduel.core.model.handler.damage.DamageHandler;
import fr.byob.game.memeduel.core.model.handler.selection.SelectionHandler;
import fr.byob.game.memeduel.core.model.handler.update.UpdateHandler;


public class B2DModelObject extends DefaultModelObject {

	protected final SelectionHandler selectionHandler;
	protected final B2DBodyHandler b2dBodyHandler;

	public B2DModelObject(final String id, final B2DGameObjectDefiniton god, final UpdateHandler updateHandler, final DamageHandler damageHandler, final SelectionHandler selectionHandler,
			final B2DBodyHandler b2dHandler) {
		super(id, god, updateHandler, damageHandler);
		this.selectionHandler = selectionHandler;
		this.b2dBodyHandler = b2dHandler;
	}

	@Override
	public void init(final AbstractModel model) {
		if (this.isSelectable()) {
			this.selectionHandler.setModelObject(this);
		}

		this.b2dBodyHandler.setModelObject(this);
		this.b2dBodyHandler.addBody(model.getB2World());

		super.init(model);
	}

	@Override
	public void clear(final AbstractModel model) {
		super.clear(model);
		this.b2dBodyHandler.removeBody(model.getB2World());
	}

	@Override
	public B2DGameObjectDefiniton getGOD() {
		return (B2DGameObjectDefiniton) super.getGOD();
	}


	public boolean isSelectable() {
		return this.selectionHandler != null;
	}

	public SelectionHandler getSelectionHandler() {
		return this.selectionHandler;
	}

	public B2DBodyHandler getB2DBodyHandler() {
		return this.b2dBodyHandler;
	}

	public boolean isExplodable() {
		return this.isDamageable() && this.getGOD().getDamageDefinition().isExplodable();
	}

	@Override
	public String getTypeId() {
		return this.getGOD().getB2DBodyDefition().getId();
	}
}
