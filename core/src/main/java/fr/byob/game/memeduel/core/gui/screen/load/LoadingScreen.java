package fr.byob.game.memeduel.core.gui.screen.load;

import playn.core.PlayN;
import playn.core.util.Callback;
import tripleplay.ui.Background;
import tripleplay.ui.Group;
import tripleplay.ui.Label;
import tripleplay.ui.Root;
import tripleplay.ui.Style;
import tripleplay.ui.Styles;
import tripleplay.ui.layout.AxisLayout;
import tripleplay.ui.layout.BorderLayout;
import fr.byob.game.memeduel.core.GameImage;
import fr.byob.game.memeduel.core.GameMusic;
import fr.byob.game.memeduel.core.GameSound;
import fr.byob.game.memeduel.core.GameStyles;
import fr.byob.game.memeduel.core.MemeDuelLoader;
import fr.byob.game.memeduel.core.gui.builders.LabelBuilder;
import fr.byob.game.memeduel.core.gui.screen.AbstractScreen;

public abstract class LoadingScreen<T> extends AbstractScreen implements Callback<T> {

	protected boolean painted = false;
	protected boolean startLoad = false;

	protected LoadingScreen() {
	}

	protected LoadingScreen(final String groupID) {
		super(groupID);
	}

	protected Root root;

	@Override
	public void wasAdded() {
		GameSound.mute();
		this.root = iface.createRoot(new BorderLayout(), stylesheet(), layer);
		root.setSize(width(), height());
		if (hasAnimatedBackground()) {
			root.addStyles(Styles.make(Style.BACKGROUND.is(Background.solid(0x88000000))));
		} else {
			root.addStyles(Styles.make(Style.BACKGROUND.is(Background.solid(0xFF000000))));
		}

		final Group labelGroup = new Group(AxisLayout.vertical(), Style.VALIGN.center, Style.HALIGN.center);
		final Label label = LabelBuilder.instance().text(text()).styles(GameStyles.getInstance().getTitleStyles()).build();
		labelGroup.add(label);

		if (icon() != null) {
			final Group iconGroup = new Group(AxisLayout.vertical(), Style.VALIGN.center, Style.HALIGN.center);
			final Label icon = LabelBuilder.instance().icon(icon()).build();
			iconGroup.add(icon);

			this.root.add(labelGroup.setConstraint(BorderLayout.NORTH));
			this.root.add(iconGroup.setConstraint(BorderLayout.CENTER));
		} else {
			this.root.add(labelGroup.setConstraint(BorderLayout.CENTER));
		}
	}

	@Override
	public void wasRemoved() {
		this.iface.destroyRoot(this.root);
		while (layer.size() > 0) {
			layer.get(0).destroy();
		}
		GameSound.unmute();
	}

	protected abstract String text();

	protected GameImage icon() {
		return null;
	}

	@Override
	public void showTransitionCompleted() {
		super.showTransitionCompleted();
		painted = true;
	}

	@Override
	public void update(final float delta) {
		super.update(delta);
		if (painted && startLoad) {
			PlayN.log().debug("Loading start");
			// Load only once !
			startLoad = false;
			doLoad();
			PlayN.log().debug("Loading end");
		}
	}

	protected abstract void doLoad();

	public final void load() {
		startLoad = true;
	}

	@Override
	public GameMusic getMusic() {
		return MemeDuelLoader.MUSIC_MENU;
	}

}
