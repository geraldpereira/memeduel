package fr.byob.game.memeduel.core.gui.screen;

import react.Slot;
import tripleplay.game.UIScreen;
import tripleplay.ui.Button;
import tripleplay.ui.Stylesheet;
import fr.byob.game.memeduel.core.GameMusic;
import fr.byob.game.memeduel.core.GameStack;
import fr.byob.game.memeduel.core.GameStyles;
import fr.byob.game.memeduel.core.MemeDuelLoader;
import fr.byob.game.memeduel.core.gui.builders.ButtonBuilder;

public abstract class AbstractScreen extends UIScreen {

	private final String groupID;

	protected Button back;

	protected AbstractScreen(final String groupID) {
		super();
		this.groupID = groupID;
	}

	protected AbstractScreen() {
		this(null);
	}

	public String getGroupID() {
		return groupID;
	}

	public boolean hasAnimatedBackground() {
		return true;
	}

	protected Button back() {
		back = ButtonBuilder.instance().icon(MemeDuelLoader.BACK).slot(new Slot<Button>() {
			@Override
			public void onEmit(final Button event) {
				GameStack.instance().remove(AbstractScreen.this);
			}
		}).build();

		return back;
	}

	protected Stylesheet stylesheet() {
		return GameStyles.getInstance().getStylesheet();
	}

	public GameMusic getMusic() {
		return null;
	}
}
