package fr.byob.game.memeduel.core.gui.screen.load;

import playn.core.Font;
import playn.core.PlayN;
import tripleplay.ui.Background;
import tripleplay.ui.Group;
import tripleplay.ui.Label;
import tripleplay.ui.Root;
import tripleplay.ui.Style;
import tripleplay.ui.Styles;
import tripleplay.ui.layout.AxisLayout;
import fr.byob.game.memeduel.core.GameContext;
import fr.byob.game.memeduel.core.GameStack;
import fr.byob.game.memeduel.core.GameStyles;
import fr.byob.game.memeduel.core.MemeDuel;
import fr.byob.game.memeduel.core.MemeDuelLoader;
import fr.byob.game.memeduel.core.gui.ProgressElement;
import fr.byob.game.memeduel.core.gui.ProgressElement.Mode;
import fr.byob.game.memeduel.core.gui.builders.LabelBuilder;
import fr.byob.game.memeduel.core.gui.screen.AbstractScreen;
import fr.byob.game.memeduel.core.gui.screen.AnimatedBackground;
import fr.byob.game.memeduel.core.gui.screen.IntroScreen;
import fr.byob.game.memeduel.core.gui.screen.MessageScreen;
import fr.byob.game.memeduel.core.patch.AssetWatcher;

public class ResourcesLoadingScreen extends AbstractScreen implements AssetWatcher.Listener {

	protected boolean painted = false;
	protected boolean startLoad = false;

	private Root root;
	private ProgressElement gauge;

	@Override
	public void wasAdded() {
		this.root = iface.createRoot(AxisLayout.vertical().gap(GameStyles.getInstance().getGap()), stylesheet(), layer);
		root.setSize(width(), height());
		root.addStyles(Styles.make(Style.BACKGROUND.is(Background.solid(0xFF000000))));

		final Group labelGroup = new Group(AxisLayout.horizontal());
		final Label label = LabelBuilder.instance().text("LOADING ...").styles(GameStyles.getInstance().getTitleStyles().add(Style.FONT.is(PlayN.graphics().createFont("Impact", Font.Style.BOLD, 44)))).build();
		labelGroup.add(label);

		gauge = new ProgressElement(Mode.PROGRESS, 320, 40);
		final Group gaugeGroup = new Group(AxisLayout.horizontal(), GameStyles.getInstance().getButtonStyles()).add(gauge);

		root.add(labelGroup, gaugeGroup);

	}

	@Override
	public void wasRemoved() {
		this.iface.destroyRoot(this.root);
		while (layer.size() > 0) {
			layer.get(0).destroy();
		}
	}

	public void load() {
		startLoad = true;
	}
	@Override
	public void paint(final float alpha) {
		super.paint(alpha);
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

	private void doLoad() {
		final MemeDuelLoader loader = new MemeDuelLoader(this);
		loader.loadResources();
	}


	@Override
	public boolean hasAnimatedBackground() {
		return false;
	}

	@Override
	public void done() {
		gauge.setPercentage(1f);
		GameContext.instance().setValue(MemeDuel.ANIMATED_BACKGROUND, new AnimatedBackground());
		GameStack.instance().replace(new IntroScreen());

	}

	@Override
	public void progress(final float percentage) {
		gauge.setPercentage(percentage);
	}

	@Override
	public void error(final Throwable e) {
		PlayN.log().debug(e.getMessage());
		GameStack.instance().replace(new MessageScreen("FAILED TO LOAD RESOURCES", e.getMessage()));
	}

}
