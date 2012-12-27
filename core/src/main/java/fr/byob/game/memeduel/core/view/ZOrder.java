package fr.byob.game.memeduel.core.view;

import playn.core.Layer;

public enum ZOrder {
	FRONT {
		@Override
		public void addToView(final Layer layer, final View view) {
			view.getStaticLayerFront().add(layer);
		}

		@Override
		public void removeFromView(final Layer layer, final View view) {
			try {
				view.getStaticLayerFront().remove(layer);
			} catch (final UnsupportedOperationException e) {
			}
		}
	},
	DYNAMIC {
		@Override
		public void addToView(final Layer layer, final View view) {
			view.getDynamicLayer().add(layer);
		}

		@Override
		public void removeFromView(final Layer layer, final View view) {
			try {
				view.getDynamicLayer().remove(layer);
			} catch (final UnsupportedOperationException e) {
			}
		}
	},
	BACK {
		@Override
		public void addToView(final Layer layer, final View view) {
			view.getStaticLayerBack().add(layer);
		}

		@Override
		public void removeFromView(final Layer layer, final View view) {
			try {
				view.getStaticLayerBack().remove(layer);
			} catch (final UnsupportedOperationException e) {
			}
		}
	};

	public abstract void addToView(final Layer layer, final View view);

	public abstract void removeFromView(final Layer layer, final View view);
}
