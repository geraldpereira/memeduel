package fr.byob.game.memeduel.core.view;

import playn.core.GroupLayer;
import pythagoras.f.Dimension;
import pythagoras.f.Vector;
import tripleplay.ui.Element;
import fr.byob.game.memeduel.core.ViewUtils;
import fr.byob.game.memeduel.core.god.AllGODLoader;
import fr.byob.game.memeduel.core.god.DynamicGameObjectDefinition;
import fr.byob.game.memeduel.core.model.AbstractModel;
import fr.byob.game.memeduel.core.model.handler.damage.DamageHandler;
import fr.byob.game.memeduel.core.model.handler.update.UpdateHandler;
import fr.byob.game.memeduel.core.model.object.DefaultModelObject;
import fr.byob.game.memeduel.core.model.object.ModelObject;
import fr.byob.game.memeduel.core.view.object.ViewObject;


public class StandAloneView extends Element<StandAloneView> implements View {


	private final AllGODLoader godLoader;

	private float hintX;
	private float hintY;

	public StandAloneView(final AllGODLoader godLoader) {
		this.godLoader = godLoader;
	}

	@Override
	public void loadViewObjects(final AllGODLoader godLoader) {
		initLayers(godLoader);

		for (final DynamicGameObjectDefinition god : godLoader.getLevelGOD().getGameObjects()) {
			final ViewObject vo = god.newViewObject(this, new DummyModelObject(god));
			vo.paint(1);
		}

	}

	private void initLayers(final AllGODLoader godLoader) {
		this.layer.setDepth(1);

		// Margins
		final float width = hintX * 0.8f;
		final float height = hintY * 0.8f;
		final float windowModelWidth = ViewUtils.toInitModel(width);
		final float windowModelHeight = ViewUtils.toInitModel(height);

		final float levelWidth = godLoader.getLevelGOD().getSize().width;
		final float levelheight = godLoader.getLevelGOD().getSize().height;

		final float drawScale = Math.min(1, Math.min(windowModelWidth / levelWidth, windowModelHeight / levelheight));

		this.layer.setScale(drawScale / ViewUtils.physUnitPerScreenUnit);
		_size = new Dimension(levelWidth * drawScale / ViewUtils.physUnitPerScreenUnit, levelheight * drawScale / ViewUtils.physUnitPerScreenUnit);
		_preferredSize = _size;
	}


	@Override
	public GroupLayer getStaticLayerBack() {
		return this.layer;
	}

	@Override
	public GroupLayer getStaticLayerFront() {
		return this.layer;
	}

	@Override
	public GroupLayer getDynamicLayer() {
		return this.layer;
	}

	@Override
	public GroupLayer getWorldLayer() {
		return this.layer;
	}

	@Override
	protected tripleplay.ui.Element<StandAloneView>.LayoutData createLayoutData(final float hintX, final float hintY) {
		this.hintX = hintX;
		this.hintY = hintY;

		loadViewObjects(godLoader);

		return new LayoutData() {

			@Override
			public Dimension computeSize(final float hintX, final float hintY) {
				return _size;
			}
		};
	}

	@Override
	protected void wasRemoved() {
		super.wasRemoved();
		if (this.layer == null) {
			return;
		}

		this.layer.clear();
	}

	/**
	 * TODO Pas top ca ... découpler les GOD des objets du modèle et de la vue !
	 * 
	 * @author Kojiro
	 * 
	 */
	private static class DummyModelObject implements ModelObject {

		private final DynamicGameObjectDefinition god;

		public DummyModelObject(final DynamicGameObjectDefinition god) {
			this.god = god;
		}

		@Override
		public void init(final AbstractModel model) {
		}

		@Override
		public void clear(final AbstractModel model) {
		}

		@Override
		public DynamicGameObjectDefinition getGOD() {
			return god;
		}

		@Override
		public boolean isDamageable() {
			return false;
		}

		@Override
		public boolean isUpdatable() {
			return false;
		}

		@Override
		public UpdateHandler getUpdateHandler() {
			return new UpdateHandler() {

				@Override
				public void setModelObject(final DefaultModelObject modelObject) {
				}

				@Override
				public void update(final float delta) {
				}

				@Override
				public void init(final AllGODLoader godLoader) {
				}

				@Override
				public float getPreviousRotation() {
					return god.getAngle();
				}


				@Override
				public void getPreviousPositionToOut(final Vector out) {
					out.set(god.getPosition());
				}

				@Override
				public void getCurrentPositionToOut(final Vector out) {
					out.set(god.getPosition());
				}

				@Override
				public float getCurrentTransparency() {
					return 1f;
				}

				@Override
				public float getCurrentRotation() {
					return god.getAngle();
				}
			};
		}

		@Override
		public DamageHandler getDamageHandler() {
			return null;
		}

		@Override
		public boolean update(final float delta) {
			return false;
		}

		@Override
		public String getId() {
			return null;
		}

		@Override
		public String getTypeId() {
			return null;
		}

		@Override
		public void invalidate() {
		}

		@Override
		public void validate() {
		}

		@Override
		public boolean isValid() {
			return true;
		}

	}

}
