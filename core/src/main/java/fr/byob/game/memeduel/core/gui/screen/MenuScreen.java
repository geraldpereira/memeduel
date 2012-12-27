package fr.byob.game.memeduel.core.gui.screen;

import tripleplay.ui.Background;
import tripleplay.ui.Button;
import tripleplay.ui.Group;
import tripleplay.ui.Label;
import tripleplay.ui.Root;
import tripleplay.ui.Shim;
import tripleplay.ui.Style;
import tripleplay.ui.layout.AxisLayout;
import tripleplay.ui.layout.BorderLayout;
import fr.byob.game.memeduel.core.GameMusic;
import fr.byob.game.memeduel.core.GameStyles;
import fr.byob.game.memeduel.core.MemeDuelLoader;

public class MenuScreen extends AbstractScreen {

	protected MenuScreen(final String groupID) {
		super(groupID);
	}

	protected MenuScreen() {
		super();
	}


	private Root root;

	@Override
	public void wasAdded() {
		super.wasAdded();

		root = iface.createRoot(new BorderLayout(GameStyles.getInstance().getGap()), stylesheet(), layer);
		root.setSize(width(), height());

		final Group topGroup = new Group(AxisLayout.horizontal().gap(GameStyles.getInstance().getGap()), Style.HALIGN.left, GameStyles.getInstance().getBordersBackgroundStyle());
		final Button back = back();
		if (back != null) {
			topGroup.add(back);
			topGroup.add(new Shim(10, 0));
		}
		topGroup.add(title().setConstraint(AxisLayout.stretched()));
		topGroup.setConstraint(BorderLayout.NORTH);
		root.add(topGroup);

		final Group buttons = buttons();
		if (buttons != null) {
			buttons.setConstraint(BorderLayout.SOUTH);
			root.add(buttons);
		}

		final Group west = west();
		if (west != null) {
			west.setConstraint(BorderLayout.WEST);
			root.add(west);
		}

		final Group east = east();
		if (east != null) {
			east.setConstraint(BorderLayout.EAST);
			root.add(east);
		}

		final Group menu = menu();
		if (menu != null) {
			menu.setConstraint(BorderLayout.CENTER);
			root.add(menu);
		}
	}

	@Override
	public void wasRemoved() {
		super.wasRemoved();
		iface.destroyRoot(root);
		while (layer.size() > 0) {
			layer.get(0).destroy();
		}
	}

	protected Label title() {
		return new Label("TITLE").setConstraint(AxisLayout.stretched());
	}

	protected Group buttons() {
		return null;
	}

	protected Group west() {
		return null;
	}

	protected Group east() {
		return null;
	}

	protected Group menu() {
		return null;
	}

	protected Background background() {
		return Background.bordered(0xFFCCCCCC, 0xFFCC99FF, 5).inset(5);
	}

	@Override
	public GameMusic getMusic() {
		return MemeDuelLoader.MUSIC_MENU;
	}

}
