package fr.byob.game.memeduel.core.gui.builders;

import react.Slot;
import tripleplay.ui.Button;
import tripleplay.ui.Styles;
import fr.byob.game.memeduel.core.GameImage;
import fr.byob.game.memeduel.core.GameStyles;

public class ButtonBuilder {

	private final static ButtonBuilder builder = new ButtonBuilder();

	public static ButtonBuilder instance() {
		builder.reset();
		return builder;
	}

	protected String text;
	protected GameImage icon;
	protected Slot<Button> slot;
	protected Styles styles;// = GameStyles.getInstance().getButtonStyles();

	private ButtonBuilder() {
	}

	private void reset() {
		text = null;
		icon = null;
		slot = null;
		styles = null;
	}

	public ButtonBuilder text(final String text) {
		this.text = text;
		return this;
	}

	public ButtonBuilder icon(final GameImage icon) {
		this.icon = icon;
		return this;
	}

	public ButtonBuilder slot(final Slot<Button> slot) {
		this.slot = slot;
		return this;
	}

	public ButtonBuilder styles(final Styles styles) {
		this.styles = styles;
		return this;
	}

	public Button build() {
		final Button button = new Button(text);
		if (styles != null) {
			button.addStyles(styles);
		} else if (icon != null && text == null) {
			button.addStyles(GameStyles.getInstance().getIconButtonStyles());
		}
		if (icon != null) {
			button.icon.update(icon.getImage());
		}
		button.clicked().connect(slot);
		return button;
	}
}
