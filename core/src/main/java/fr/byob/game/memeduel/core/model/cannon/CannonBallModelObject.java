package fr.byob.game.memeduel.core.model.cannon;

import pythagoras.f.Vector;
import fr.byob.game.memeduel.core.god.cannon.CannonBallGOD;
import fr.byob.game.memeduel.core.model.AbstractModel;
import fr.byob.game.memeduel.core.model.handler.b2d.B2DBodyHandler;
import fr.byob.game.memeduel.core.model.handler.cannon.CannonBallHandler;
import fr.byob.game.memeduel.core.model.handler.damage.DamageHandler;
import fr.byob.game.memeduel.core.model.handler.update.UpdateHandler;
import fr.byob.game.memeduel.core.model.object.B2DModelObject;

public class CannonBallModelObject extends B2DModelObject {

	protected final CannonBallHandler cannonBallHandler;

	public CannonBallModelObject(final String id, final CannonBallGOD god, final UpdateHandler updateHandler, final DamageHandler damageHandler,
			final B2DBodyHandler b2dHandler,
			final CannonBallHandler cannonBallHandler) {
		super(id, god, updateHandler, damageHandler, null, b2dHandler);
		this.cannonBallHandler = cannonBallHandler;
	}

	@Override
	public void init(final AbstractModel model) {
		super.init(model);
		if (this.cannonBallHandler != null) {
			this.cannonBallHandler.setModelObject(this);
		}
	}

	public CannonBallHandler getCannonBallHandler() {
		return this.cannonBallHandler;
	}

	@Override
	public CannonBallGOD getGOD() {
		return (CannonBallGOD) super.getGOD();
	}

	public void fire(final Vector fireVector, final float angularVelocity) {
		this.b2dBodyHandler.getBody().applyLinearImpulse(fireVector, this.b2dBodyHandler.getBody().getWorldCenter());
		this.b2dBodyHandler.getBody().applyTorque(angularVelocity);
		//		this.b2dBodyHandler.getBody().setLinearVelocity(fireVector);
		//		this.b2dBodyHandler.getBody().setAngularVelocity(angularVelocity);
	}

}
