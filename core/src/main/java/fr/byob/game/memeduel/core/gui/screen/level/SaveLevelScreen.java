package fr.byob.game.memeduel.core.gui.screen.level;

import playn.core.util.Callback;
import react.Slot;
import tripleplay.ui.Button;
import tripleplay.ui.Field;
import tripleplay.ui.Group;
import tripleplay.ui.Label;
import tripleplay.ui.layout.AxisLayout;
import fr.byob.game.memeduel.core.GameContext;
import fr.byob.game.memeduel.core.GameStack;
import fr.byob.game.memeduel.core.GameStyles;
import fr.byob.game.memeduel.core.MemeDuel;
import fr.byob.game.memeduel.core.gui.builders.ButtonBuilder;
import fr.byob.game.memeduel.core.gui.builders.FieldBuilder;
import fr.byob.game.memeduel.core.gui.builders.LabelBuilder;
import fr.byob.game.memeduel.core.gui.screen.MenuScreen;
import fr.byob.game.memeduel.core.net.helper.LevelNetHelper;
import fr.byob.game.memeduel.domain.Level;
import fr.byob.game.memeduel.domain.User;

public class SaveLevelScreen extends MenuScreen implements Callback<String> {

	private final Level level;

	protected SaveLevelScreen(final String groupID,final Level level) {
		super(groupID);
		this.level = level;
	}

	private Label infoMessage;
	private Field levelNameField;
	private Button save;

	@Override
	protected Label title() {
		return LabelBuilder.instance().text("SAVE LEVEL").styles(GameStyles.getInstance().getTitleStyles()).build();
	}

	@Override
	protected Group menu() {
		infoMessage = LabelBuilder.instance().text("Select level name").build();
		levelNameField = FieldBuilder.instance().initialText(level.getTitle()).build();
		final Group levelNameGroup = new Group(AxisLayout.horizontal().gap(GameStyles.getInstance().getGap())).add(levelNameField);
		final Group infoGroup = new Group(AxisLayout.horizontal().gap(GameStyles.getInstance().getGap())).add(infoMessage);
		final Group menu = new Group(AxisLayout.vertical().gap(GameStyles.getInstance().getGap())).add(infoGroup, levelNameGroup);
		return menu;
	}

	@Override
	protected Group buttons() {
		save = ButtonBuilder.instance().text("SAVE").slot(new Slot<Button>() {
			@Override
			public void onEmit(final Button event) {
				event.text.update("SAVING ...");
				event.setEnabled(false);
				back.setEnabled(false);
				final LevelNetHelper net = GameContext.instance().getValue(MemeDuel.LEVEL_NET_HELPER);
				final User user = GameContext.instance().getValue(MemeDuel.USER);
				level.setTitle(levelNameField.text.get());
				if (level.getId() != null){
					net.update(user, level, SaveLevelScreen.this);
				} else {
					net.add(user, level, SaveLevelScreen.this);
				}
			}
		}).build();

		final Group buttonsGroup = new Group(AxisLayout.vertical(), GameStyles.getInstance().getBordersBackgroundStyle()).add(save);
		return buttonsGroup;
	}

	@Override
	public void onSuccess(final String result) {
		GameStack.instance().removeGroup(getGroupID());
	}

	@Override
	public void onFailure(final Throwable cause) {
		save.text.update("SAVE");
		save.setEnabled(true);
		infoMessage.text.update(cause.getMessage());
	}
}