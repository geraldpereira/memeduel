package fr.byob.game.memeduel.core.gui.screen.level;

import pythagoras.f.Vector;
import react.Slot;
import tripleplay.ui.Button;
import tripleplay.ui.Group;
import tripleplay.ui.Style;
import tripleplay.ui.layout.AxisLayout;
import fr.byob.game.memeduel.core.GameContext;
import fr.byob.game.memeduel.core.GameImage;
import fr.byob.game.memeduel.core.GameMusic;
import fr.byob.game.memeduel.core.GameStack;
import fr.byob.game.memeduel.core.GameStyles;
import fr.byob.game.memeduel.core.MemeDuel;
import fr.byob.game.memeduel.core.MemeDuelLoader;
import fr.byob.game.memeduel.core.controller.EditLevelController;
import fr.byob.game.memeduel.core.controller.SelectionsHandler;
import fr.byob.game.memeduel.core.controller.SelectionsListener;
import fr.byob.game.memeduel.core.editor.B2DGODProvider;
import fr.byob.game.memeduel.core.god.B2DGameObjectDefiniton;
import fr.byob.game.memeduel.core.gui.ProgressElement;
import fr.byob.game.memeduel.core.gui.ProgressElement.Mode;
import fr.byob.game.memeduel.core.gui.builders.ButtonBuilder;
import fr.byob.game.memeduel.core.gui.screen.load.TestLevelLoadingScreen;
import fr.byob.game.memeduel.core.model.EditModel;
import fr.byob.game.memeduel.core.model.EditModelStateListener;
import fr.byob.game.memeduel.domain.Level;

/**
 * 
 * @author gpereira
 * 
 */
public class EditLevelScreen extends InGameScreen implements SelectionsListener, EditModelStateListener, B2DGODProvider<B2DGameObjectDefiniton> {

	private enum TestButtonState {
		UNLOCK(MemeDuelLoader.UNLOCK) {
			@Override
			public void onClick(final EditLevelScreen screen) {
				screen.selectionHandler.unlock();
			}
		},
		UNSELECT(MemeDuelLoader.UNLOCK) {
			@Override
			public void onClick(final EditLevelScreen screen) {
				screen.selectionHandler.unselectAll();
			}
		},
		DISABLED(MemeDuelLoader.TEST_DISABLED) {
			@Override
			public void onClick(final EditLevelScreen screen) {
				// Plus qu'a attendre que le camion soit en place ... et que le
				// world soit sleeping
			}
		},
		TEST(MemeDuelLoader.TEST) {
			@Override
			public void onClick(final EditLevelScreen screen) {
				final GameStack stack = GameStack.instance();
				// Store the level (So we can retry it)
				final String levelJSON = screen.controller.getLevelContent();
				final Level level = GameContext.instance().getValue(MemeDuel.LEVEL);
				level.setContent(levelJSON);
				// Test it
				final TestLevelLoadingScreen loading = new TestLevelLoadingScreen(screen.getGroupID());
				stack.push(loading, stack.slide());
				// context.remove(screen);
				loading.load();
				// TODO Handle null json level => error screen
			}
		};

		private final GameImage icon;

		private TestButtonState(final GameImage icon) {
			this.icon = icon;
		}

		public abstract void onClick(final EditLevelScreen screen);

		public GameImage getIcon() {
			return this.icon;
		}
	}

	private EditorGroupsBuilder editorBuilder;

	// Gauge
	private ProgressElement percentageGauge;
	private Button testLevelButton;

	private SelectionsHandler selectionHandler;

	private TestButtonState testButtonState;

	public EditLevelScreen(final String groupID, final EditLevelController controller) {
		super(groupID, controller);
		this.testButtonState = TestButtonState.TEST;
	}

	@Override
	public void wasAdded() {
		editorBuilder = new EditorGroupsBuilder(getController());

		super.wasAdded();

		final EditModel model = getController().getModel();
		this.selectionHandler = getController().getSelectionHandler();
		this.selectionHandler.addListener(this);
		this.selectionHandler.addListener(editorBuilder);
		this.percentageChanged(model.getPercentage());
		this.sleepingStateChanged(model.isSleeping());
		model.addStateListener(this);

	}

	@Override
	public void wasRemoved() {
		super.wasRemoved();

		this.selectionHandler.removeListener(this);
		getController().getModel().removeStateListener(this);
	}

	private EditLevelController getController() {
		return (EditLevelController) controller;
	}

	@Override
	protected Group northGroup() {
		final Group menu = new Group(AxisLayout.horizontal().gap(GameStyles.getInstance().getGap()));

		final Group editorButtonsLeftGroup = editorBuilder.getButtonsGroup();

		// Right buttons group : percentage of shapes dropped / launch test
		final Group rightGroup = new Group(AxisLayout.horizontal(), Style.HALIGN.right);

		final Button lockLevelButton = ButtonBuilder.instance().icon(MemeDuelLoader.LOCK).slot(newLockButtonSlot()).build();
		rightGroup.add(lockLevelButton);

		this.testLevelButton = ButtonBuilder.instance().icon(testButtonState.getIcon()).slot(newTestButtonSlot()).build();
		rightGroup.add(this.testLevelButton);

		this.percentageGauge = new ProgressElement(Mode.GAUGE, 100, 32);
		final Group gaugeGroup = new Group(AxisLayout.horizontal(), GameStyles.getInstance().getIconButtonStyles()).add(percentageGauge);

		rightGroup.add(gaugeGroup);

		menu.add(editorButtonsLeftGroup, rightGroup);
		menu.setConstraint(AxisLayout.stretched());
		return menu;
	}

	@Override
	protected Group westGroup() {
		return editorBuilder.getEditorGroup();
	}

	private Slot<Button> newTestButtonSlot() {
		return new Slot<Button>() {
			@Override
			public void onEmit(final Button event) {
				testButtonState.onClick(EditLevelScreen.this);
			}
		};
	}

	private Slot<Button> newLockButtonSlot() {
		return new Slot<Button>() {
			@Override
			public void onEmit(final Button event) {
				selectionHandler.lock();
			}
		};
	}

	@Override
	public B2DGameObjectDefiniton getGOD(final Vector position, final float angle) {
		return this.editorBuilder.getGOD(position, angle);
	}

	@Override
	public void selectionChanged(final boolean isEmpty, final boolean isLocked) {
		this.stateChanged(getController().getModel().isSleeping(), isEmpty, isLocked);
	}

	@Override
	public void sleepingStateChanged(final boolean sleeping) {
		this.stateChanged(sleeping, this.selectionHandler.isEmpty(), this.selectionHandler.isLocked());
	}

	private void stateChanged(final boolean sleeping, final boolean selectionEmpty, final boolean worldLocked) {
		if (worldLocked) {
			this.testButtonState = TestButtonState.UNLOCK;
		} else if (!selectionEmpty) {
			this.testButtonState = TestButtonState.UNSELECT;
		} else if (!sleeping || getController().getModel().getMemeCount() == 0) {
			this.testButtonState = TestButtonState.DISABLED;
		} else {
			this.testButtonState = TestButtonState.TEST;
		}
		this.testLevelButton.icon.update(this.testButtonState.getIcon().getImage());
	}

	@Override
	public void percentageChanged(final float percentage) {
		this.percentageGauge.setPercentage(percentage);
	}

	@Override
	public GameMusic getMusic() {
		return MemeDuelLoader.MUSIC_EDIT;
	}

}
