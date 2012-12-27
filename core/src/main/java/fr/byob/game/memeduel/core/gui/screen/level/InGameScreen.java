package fr.byob.game.memeduel.core.gui.screen.level;

import playn.core.Keyboard;
import playn.core.Layer;
import playn.core.Mouse;
import playn.core.Platform;
import playn.core.PlayN;
import playn.core.Touch;
import tripleplay.ui.Button;
import tripleplay.ui.Group;
import tripleplay.ui.Root;
import tripleplay.ui.Shim;
import tripleplay.ui.Style;
import tripleplay.ui.layout.AxisLayout;
import tripleplay.ui.layout.BorderLayout;
import fr.byob.game.memeduel.core.GameListeners;
import fr.byob.game.memeduel.core.GameStyles;
import fr.byob.game.memeduel.core.controller.AbstractController;
import fr.byob.game.memeduel.core.gui.screen.AbstractScreen;

public class InGameScreen extends AbstractScreen {

	protected final AbstractController controller;

	public InGameScreen(final String groupID, final AbstractController controller) {
		super(groupID);
		this.controller = controller;
	}

	public InGameScreen(final AbstractController controller) {
		this(null, controller);
	}

	private Root root;

	@Override
	public void wasAdded() {
		super.wasAdded();

		root = iface.createRoot(new BorderLayout(GameStyles.getInstance().getGap()), stylesheet(), layer);
		root.setSize(width(), height());

		final Group topGroup = new Group(AxisLayout.horizontal(), Style.HALIGN.left, GameStyles.getInstance().getBordersBackgroundStyle());
		final Button back = back();
		if (back != null) {
			topGroup.add(back);
			topGroup.add(new Shim(10, 0));
		}
		final Group northGroup = northGroup();
		if (northGroup != null) {
			topGroup.add(northGroup);
		}
		topGroup.setConstraint(BorderLayout.NORTH);
		root.add(topGroup);

		final Group southGroup = southGroup();
		if (southGroup != null) {
			root.add(southGroup.setConstraint(BorderLayout.SOUTH));
		}

		final Group eastGroup = eastGroup();
		if (eastGroup != null) {
			root.add(eastGroup.setConstraint(BorderLayout.EAST));
		}

		final Group westGroup = westGroup();
		if (westGroup != null) {
			root.add(westGroup.setConstraint(BorderLayout.WEST));
		}

		final Group centerGroup = centerGroup();
		if (centerGroup != null) {
			root.add(centerGroup.setConstraint(BorderLayout.CENTER));
		}

		// Set a very high depth to be in front of the game layers
		root.layer.setDepth(200);

		// hook up our pointer listener
		root.layer.addListener(controller);

		layer.add(gameLayer());
	}

	@Override
	public void wasRemoved() {
		super.wasRemoved();
		iface.destroyRoot(root);
		while (layer.size() > 0) {
			layer.get(0).destroy();
		}
		controller.dispose();
	}

	@Override
	public void wasShown() {
		super.wasShown();
		controller.play();
		if (PlayN.platformType() == Platform.Type.ANDROID) {
			GameListeners.instance().setListener((Touch.Listener) controller);
		} else {
			GameListeners.instance().setListener((Mouse.Listener) controller);
		}
		GameListeners.instance().setListener((Keyboard.Listener) controller);
	}

	@Override
	public void wasHidden() {
		super.wasHidden();
		controller.pause();
		// Game context redispatch les events !!!
		if (PlayN.platformType() == Platform.Type.ANDROID) {
			GameListeners.instance().removeListener((Touch.Listener) controller);
		} else {
			GameListeners.instance().removeListener((Mouse.Listener) controller);
		}
		GameListeners.instance().removeListener((Keyboard.Listener) controller);
	}

	@Override
	public boolean hasAnimatedBackground() {
		return false;
	}

	protected Group northGroup() {
		return null;
	}

	protected Group southGroup() {
		return null;
	}

	protected Group eastGroup() {
		return null;
	}

	protected Group westGroup() {
		return null;
	}

	protected Group centerGroup() {
		return null;
	}

	protected Layer gameLayer() {
		return controller.getView().getWorldLayer();
	}
}
