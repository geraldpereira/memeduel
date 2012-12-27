package fr.byob.game.memeduel.core.gui.screen;

import tripleplay.ui.Background;
import tripleplay.ui.Button;
import tripleplay.ui.Group;
import tripleplay.ui.Label;
import tripleplay.ui.Root;
import tripleplay.ui.Shim;
import tripleplay.ui.Style;
import tripleplay.ui.Styles;
import tripleplay.ui.layout.AxisLayout;
import tripleplay.ui.layout.BorderLayout;
import fr.byob.game.memeduel.core.GameImage;
import fr.byob.game.memeduel.core.GameMusic;
import fr.byob.game.memeduel.core.GameStyles;
import fr.byob.game.memeduel.core.MemeDuelLoader;
import fr.byob.game.memeduel.core.gui.ImageElement;
import fr.byob.game.memeduel.core.gui.builders.LabelBuilder;

public class MessageScreen extends AbstractScreen {

	private final String title;
	private final String message;
	private final GameImage image;

	public MessageScreen(final String groupID, final String title, final GameImage image) {
		this(groupID, title, null, image);
	}

	public MessageScreen(final String groupID, final String title, final String message) {
		this(groupID, title, message, null);
	}

	public MessageScreen(final String title, final String message) {
		this(null, title, message, null);
	}

	public MessageScreen(final String groupID, final String title, final String message, final GameImage image) {
		super(groupID);
		this.title = title;
		this.message = message;
		this.image = image;
	}

	private Root root;

	@Override
	public void wasAdded() {
		super.wasAdded();

		root = iface.createRoot(new BorderLayout(GameStyles.getInstance().getGap()), stylesheet(), layer);
		root.setSize(width(), height());
		if (hasAnimatedBackground()) {
			root.addStyles(Styles.make(Style.BACKGROUND.is(Background.solid(0x88000000))));
		} else {
			root.addStyles(Styles.make(Style.BACKGROUND.is(Background.solid(0xFF000000))));
		}

		final Group topGroup = new Group(AxisLayout.horizontal().gap(GameStyles.getInstance().getGap()), Style.HALIGN.left, GameStyles.getInstance().getBordersBackgroundStyle());
		final Button back = back();
		if (back != null) {
			topGroup.add(back);
			topGroup.add(new Shim(10, 0));
		}
		topGroup.add(title().setConstraint(AxisLayout.stretched()));
		topGroup.setConstraint(BorderLayout.NORTH);
		root.add(topGroup);

		final Group messageGroup = new Group(AxisLayout.vertical().gap(GameStyles.getInstance().getGap()).offEqualize());

		if (message != null) {
			messageGroup.add(new Group(AxisLayout.horizontal()).add(message()));
		}

		if (image != null) {
			messageGroup.add(new Group(AxisLayout.horizontal()).add(new ImageElement(image)));
		}
		messageGroup.setConstraint(BorderLayout.CENTER);
		root.add(messageGroup);
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
		return LabelBuilder.instance().text(title).styles(GameStyles.getInstance().getTitleStyles()).build().setConstraint(AxisLayout.stretched());
	}

	protected Label message() {
		return LabelBuilder.instance().text(message).build();
	}

	@Override
	public GameMusic getMusic() {
		return MemeDuelLoader.MUSIC_MENU;
	}
}
