package fr.byob.game.memeduel.core.gui.builders;

import playn.core.Keyboard.TextType;
import tripleplay.ui.Field;
import tripleplay.ui.Layout.Constraint;
import tripleplay.ui.Styles;
import tripleplay.ui.layout.AxisLayout;

public class FieldBuilder {

	private final static FieldBuilder builder = new FieldBuilder();

	public static FieldBuilder instance() {
		builder.reset();
		return builder;
	}

	protected String initialText;
	protected String label;
	protected TextType textType;
	protected Constraint constraint = AxisLayout.stretched();
	protected Styles styles;// = GameStyles.getInstance().getFieldStyles();

	private FieldBuilder() {
	}

	private void reset(){
		initialText = null;
		label = null;
		textType = null;
		constraint = AxisLayout.stretched();
		styles = null;
	}

	public FieldBuilder initialText(final String text) {
		this.initialText = text;
		return this;
	}

	public FieldBuilder label(final String label) {
		this.label = label;
		return this;
	}

	public FieldBuilder textType(final TextType textType) {
		this.textType = textType;
		return this;
	}

	public FieldBuilder constraint(final Constraint constraint) {
		this.constraint = constraint;
		return this;
	}

	public FieldBuilder styles(final Styles styles) {
		this.styles = styles;
		return this;
	}

	public Field build() {
		final Field field = new Field(initialText);
		if (styles != null) {
			field.addStyles(styles);
		}
		if (constraint != null) {
			field.setConstraint(constraint);
		}

		return field;
	}
}
