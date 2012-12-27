package fr.byob.game.memeduel.core.gui.builders;

import tripleplay.ui.Label;
import tripleplay.ui.Layout.Constraint;
import tripleplay.ui.Styles;
import fr.byob.game.memeduel.core.GameImage;

public class LabelBuilder {

	private final static LabelBuilder builder = new LabelBuilder();

	public static LabelBuilder instance() {
		builder.reset();
		return builder;
	}

	protected String text;
	protected GameImage icon;
	protected Constraint constraint;
	protected Styles styles;

	private LabelBuilder() {
	}

	private void reset() {
		text = null;
		icon = null;
		constraint = null;
		styles = null;
	}

	public LabelBuilder text(final String text) {
		this.text = text;
		return this;
	}

	public LabelBuilder icon(final GameImage icon) {
		this.icon = icon;
		return this;
	}

	public LabelBuilder constraint(final Constraint constraint) {
		this.constraint = constraint;
		return this;
	}

	public LabelBuilder styles(final Styles styles) {
		this.styles = styles;
		return this;
	}

	public Label build() {
		final Label label = new Label(text);
		if (styles != null) {
			label.addStyles(styles);
		}

		if (icon != null) {
			label.icon.update(icon.getImage());
		}
		if (constraint != null) {
			label.setConstraint(constraint);
		}
		return label;
	}
}
