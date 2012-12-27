package fr.byob.game.memeduel.core;


import pythagoras.f.Vector;
import fr.byob.game.box2d.callbacks.QueryCallback;
import fr.byob.game.box2d.collision.AABB;
import fr.byob.game.box2d.dynamics.Body;
import fr.byob.game.box2d.dynamics.BodyType;
import fr.byob.game.box2d.dynamics.Fixture;
import fr.byob.game.box2d.dynamics.World;
import fr.byob.game.memeduel.core.controller.PointerPosition;
import fr.byob.game.memeduel.core.model.object.B2DModelObject;

public class B2DUtils {

	private final static AABB isBodyAtMouseAABB = new AABB();

	public static boolean isBodyAtMouse(final World world, final Body body, final PointerPosition pointerPosition) {
		final Vector vec2 = pointerPosition.getModelPosition();

		isBodyAtMouseAABB.lowerBound.set(vec2.x - 0.001f, vec2.y - 0.001f);
		isBodyAtMouseAABB.upperBound.set(vec2.x + 0.001f, vec2.y + 0.001f);

		// Query the world for overlapping shapes.
		final IsBodyAtMouseCallback callback = new IsBodyAtMouseCallback(vec2, body);
		world.queryAABB(callback, isBodyAtMouseAABB);
		return callback.isAtMouse();
	}

	private static class IsBodyAtMouseCallback implements QueryCallback {

		private final Vector vec2;
		private final Body body;
		private boolean atMouse = false;

		public IsBodyAtMouseCallback(final Vector vec2, final Body body) {
			this.vec2 = vec2;
			this.body = body;
		}

		@Override
		public boolean reportFixture(final Fixture fixture) {
			if (fixture.testPoint(this.vec2) && body == fixture.getBody()) {
				this.atMouse = true;
				return false;
			}
			return true;
		}

		public boolean isAtMouse() {
			return atMouse;
		}
	};

	private final static AABB getBodyAtMouseAABB = new AABB();

	public static Body getBodyAtMouse(final World world, final PointerPosition pointerPosition) {

		final Vector vec2 = pointerPosition.getModelPosition();
		getBodyAtMouseAABB.lowerBound.set(vec2.x - 0.001f, vec2.y - 0.001f);
		getBodyAtMouseAABB.upperBound.set(vec2.x + 0.001f, vec2.y + 0.001f);

		// Query the world for overlapping shapes.
		final GetBodyAtMouseCallback callback = new GetBodyAtMouseCallback(vec2);
		world.queryAABB(callback, getBodyAtMouseAABB);
		return callback.getSelectedBody();
	}

	private static class GetBodyAtMouseCallback implements QueryCallback {

		private final Vector vec2;
		private Body selectedBody = null;

		public GetBodyAtMouseCallback(final Vector vec2) {
			this.vec2 = vec2;
		}

		@Override
		public boolean reportFixture(final Fixture fixture) {
			// if (fixture.getBody().getType() != BodyType.STATIC) {
			if (fixture.testPoint(this.vec2)) {
				this.selectedBody = fixture.getBody();
				return false;
			}
			// }
			return true;
		}

		public Body getSelectedBody() {
			return this.selectedBody;
		}
	};

	public static void applyRandomForceAndRotation(final B2DModelObject modelObject, final float factor) {
		if (modelObject.getGOD().getB2DBodyDefition().getBodyType() == BodyType.STATIC) {
			return;
		}

		final Body b2dBody = modelObject.getB2DBodyHandler().getBody();

		final Vector linearVelocity = GamePool.instance().popVector();
		linearVelocity.set(factor * (float) (1 - 2 * Math.random()), factor * (float) (1 - 2 * Math.random()));

		b2dBody.setLinearVelocity(linearVelocity);
		if (!b2dBody.isFixedRotation()) {
			b2dBody.setAngularVelocity(factor * (float) (1 - 2 * Math.random()));
		}

		GamePool.instance().pushVector(1);
	}
}
