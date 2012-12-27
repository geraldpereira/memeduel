package fr.byob.game.memeduel.core;

import playn.core.Font;
import playn.core.PlayN;
import tripleplay.ui.Background;
import tripleplay.ui.Button;
import tripleplay.ui.CheckBox;
import tripleplay.ui.Field;
import tripleplay.ui.Label;
import tripleplay.ui.Style;
import tripleplay.ui.Style.TextEffect;
import tripleplay.ui.Styles;
import tripleplay.ui.Stylesheet;
import tripleplay.ui.ToggleButton;

public class GameStyles {

	private static GameStyles instance;

	private final static int buttonBGColor = 0xFFBAC161;
	private final static int buttonBorderColor = 0xFF616D30;
	private final static int buttonSelBorderColor = 0xFF40200E;
	private final static int buttonDisBGColor = 0xFFDDD396;
	private final static int buttonDisBorderColor = 0xFFBC9A5E;
	private final static Background buttonBG = Background.roundRect(buttonBGColor, 5, buttonBorderColor, 2).inset(5);
	private final static Background buttonSelectedBG = Background.roundRect(buttonBGColor, 5, buttonSelBorderColor, 2).inset(5);
	private final static Background buttonDisabledBG = Background.roundRect(buttonDisBGColor, 5, buttonDisBorderColor, 2).inset(5);
	private final static Background iconButtonBG = Background.roundRect(buttonBGColor, 5, buttonBorderColor, 2).inset(10);
	private final static Background iconButtonSelectedBG = Background.roundRect(buttonBGColor, 5, buttonSelBorderColor, 2).inset(10);
	private final static Background iconButtonDisabledBG = Background.roundRect(buttonDisBGColor, 5, buttonDisBorderColor, 2).inset(10);

	private final static int fieldBGColor = 0xFFFFFFFF;
	private final static int fieldBorderColor = 0xFF000000;
	private final static int fieldDisBGColor = 0xFFA0A0A0;
	private final static int fieldDisBorderColor = 0xFF000000;
	private final static Background fieldBG = Background.roundRect(fieldBGColor, 5, fieldBorderColor, 2).inset(5);
	private final static Background fieldDisabledBG = Background.roundRect(fieldDisBGColor, 5, fieldDisBorderColor, 2).inset(5);


	public static class Builder {
		private Styles buttonStyles = Styles.none().add(Style.COLOR.is(0xFFFFFFFF)).add(Style.HIGHLIGHT.is(0xFF000000)).add(Style.TEXT_EFFECT.is(TextEffect.VECTOR_OUTLINE)).add(Style.FONT.is(PlayN.graphics().createFont("Meme", Font.Style.BOLD, 32))).add(Style.BACKGROUND.is(buttonBG))
				.addDisabled(Style.COLOR.is(0xFFAAAAAA)).addDisabled(Style.BACKGROUND.is(buttonDisabledBG)).addSelected(Style.BACKGROUND.is(buttonSelectedBG));

		private Styles iconButtonStyles = Styles.none().add(Style.COLOR.is(0xFFFFFFFF)).add(Style.HIGHLIGHT.is(0xFF000000)).add(Style.TEXT_EFFECT.is(TextEffect.VECTOR_OUTLINE)).add(Style.FONT.is(PlayN.graphics().createFont("Meme", Font.Style.BOLD, 32))).add(Style.BACKGROUND.is(iconButtonBG))
				.addDisabled(Style.COLOR.is(0xFFAAAAAA)).addDisabled(Style.BACKGROUND.is(iconButtonDisabledBG)).addSelected(Style.BACKGROUND.is(iconButtonSelectedBG));

		private Styles fieldStyles = Styles.none().add(Style.COLOR.is(buttonBGColor)).add(Style.HIGHLIGHT.is(buttonSelBorderColor)).add(Style.TEXT_EFFECT.is(TextEffect.VECTOR_OUTLINE)).add(Style.FONT.is(PlayN.graphics().createFont("Meme", Font.Style.BOLD, 28))).add(Style.BACKGROUND.is(fieldBG))
				.addDisabled(Style.BACKGROUND.is(fieldDisabledBG));

		private Styles labelStyles = Styles.none().add(Style.COLOR.is(0xFFFFFFFF)).add(Style.HIGHLIGHT.is(0xFF000000)).add(Style.TEXT_EFFECT.is(TextEffect.VECTOR_OUTLINE)).add(Style.FONT.is(PlayN.graphics().createFont("Meme", Font.Style.BOLD, 32)));
		private Styles titleStyles = Styles.none().add(Style.COLOR.is(0xFFFFFFFF)).add(Style.HIGHLIGHT.is(0xFF000000)).add(Style.TEXT_EFFECT.is(TextEffect.VECTOR_OUTLINE)).add(Style.FONT.is(PlayN.graphics().createFont("Meme", Font.Style.BOLD, 44)));

		private int gap = 10;

		private Background bordersBackground = Background.solid(0x00000000).inset(5);


		public Builder gap(final int gap) {
			this.gap = gap;
			return this;
		}

		public Builder bordersBackground(final Background bordersBackground) {
			this.bordersBackground = bordersBackground;
			return this;
		}

		public Builder buttonStyles(final Styles buttonStyles) {
			this.buttonStyles = buttonStyles;
			return this;
		}

		public Builder iconButtonStyles(final Styles iconButtonStyles) {
			this.iconButtonStyles = iconButtonStyles;
			return this;
		}

		public Builder labelStyles(final Styles labelStyles) {
			this.labelStyles = labelStyles;
			return this;
		}

		public Builder fieldStyles(final Styles fieldStyles) {
			this.fieldStyles = fieldStyles;
			return this;
		}

		public Builder titleStyles(final Styles titleStyles) {
			this.titleStyles = titleStyles;
			return this;
		}

		public GameStyles build() {
			if (instance != null) {
				return instance;
			}
			instance = new GameStyles(buttonStyles, iconButtonStyles, labelStyles, fieldStyles, gap, bordersBackground, titleStyles);
			return instance;
		}
	}

	private final Styles buttonStyles;
	private final Styles iconButtonStyles;
	private final Styles labelStyles;
	private final Styles titleStyles;
	private final Styles fieldStyles;
	private final Stylesheet stylesheet;
	private final int gap;
	private final Background bordersBackground;

	private GameStyles(final Styles buttonStyles, final Styles iconButtonStyles, final Styles labelStyles, final Styles fieldStyles, final int gap, final Background bordersBackground, final Styles titleStyles) {
		this.buttonStyles = buttonStyles;
		this.iconButtonStyles = iconButtonStyles;
		this.labelStyles = labelStyles;
		this.fieldStyles = fieldStyles;
		stylesheet = Stylesheet.builder().add(Button.class, buttonStyles).add(ToggleButton.class, buttonStyles).add(CheckBox.class, buttonStyles).add(Label.class, labelStyles).add(Field.class, fieldStyles).create();

		this.gap = gap;
		this.bordersBackground = bordersBackground;
		this.titleStyles = titleStyles;
	}

	public Styles getButtonStyles() {
		return buttonStyles;
	}

	public Styles getLabelStyles() {
		return labelStyles;
	}

	public Styles getFieldStyles() {
		return fieldStyles;
	}

	public Styles getIconButtonStyles() {
		return iconButtonStyles;
	}

	public Stylesheet getStylesheet() {
		return stylesheet;
	}

	public int getGap() {
		return gap;
	}

	public Style.Binding<Background> getBordersBackgroundStyle() {
		return Style.BACKGROUND.is(bordersBackground);
	}

	public Styles getTitleStyles() {
		return titleStyles;
	}

	public final static GameStyles getInstance() {
		return instance;
	}
}
