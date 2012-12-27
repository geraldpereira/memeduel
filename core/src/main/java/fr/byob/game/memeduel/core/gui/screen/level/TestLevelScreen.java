package fr.byob.game.memeduel.core.gui.screen.level;

import java.util.Iterator;

import playn.core.Json.Writer;
import playn.core.PlayN;
import pythagoras.f.Vector;
import react.Slot;
import tripleplay.ui.Button;
import tripleplay.ui.Group;
import tripleplay.ui.Label;
import tripleplay.ui.Style;
import tripleplay.ui.layout.AxisLayout;
import fr.byob.game.memeduel.core.GameContext;
import fr.byob.game.memeduel.core.GameStack;
import fr.byob.game.memeduel.core.GameStyles;
import fr.byob.game.memeduel.core.MemeDuel;
import fr.byob.game.memeduel.core.MemeDuelLoader;
import fr.byob.game.memeduel.core.controller.AbstractController;
import fr.byob.game.memeduel.core.editor.TestCannonBallGODProvider;
import fr.byob.game.memeduel.core.god.AllGODLoader;
import fr.byob.game.memeduel.core.god.MemeDuelGODLoader;
import fr.byob.game.memeduel.core.god.MemeDuelLevelGOD;
import fr.byob.game.memeduel.core.gui.builders.ButtonBuilder;
import fr.byob.game.memeduel.core.gui.builders.LabelBuilder;
import fr.byob.game.memeduel.core.gui.screen.load.TestLevelLoadingScreen;
import fr.byob.game.memeduel.core.model.PlayModel.GameState;
import fr.byob.game.memeduel.core.model.PlayModelStateListener;
import fr.byob.game.memeduel.core.model.cannon.CannonListener;
import fr.byob.game.memeduel.domain.Level;

public class TestLevelScreen extends AbstractLevelScreen implements PlayModelStateListener, CannonListener {

	private CannonBallIterator cannonBallIterator;

	private Group selectCannonBallsGroup;
	private Group firedCannonBallsGroup;

	private Button cannonBallButton;
	private TestCannonBallGODProvider cannonBallProvider;

	public TestLevelScreen(final String groupID, final AbstractController controller) {
		super(groupID, controller);
		cannonBallProvider = getController().getCannon().getCannonBallGODProvider();
	}

	@Override
	protected Group northGroup() {
		final Group menu = new Group(AxisLayout.horizontal().gap(GameStyles.getInstance().getGap()));
		final Button retry = ButtonBuilder.instance().icon(MemeDuelLoader.RETRY).slot(newRetryButtonSlot()).build();

		final MemeDuelGODLoader loader = getController().getModel().getGodLoader();
		cannonBallIterator = new CannonBallIterator(loader, CannonBall.CANNON_BALL_SMALL);

		this.cannonBallButton = ButtonBuilder.instance().styles(GameStyles.getInstance().getIconButtonStyles()).slot(new Slot<Button>() {
			@Override
			public void onEmit(final Button event) {
				setCurrentCannonBall(cannonBallIterator.next());
			}
		}).build();
		selectCannonBallsGroup = new Group(AxisLayout.horizontal(), Style.HALIGN.left).add(cannonBallButton);

		firedCannonBallsGroup = new Group(AxisLayout.horizontal(), Style.HALIGN.left);

		setCurrentCannonBall(this.cannonBallIterator.current());

		menu.add(retry, selectCannonBallsGroup, firedCannonBallsGroup);
		return menu;
	}

	@Override
	protected Button back() {
		final Button edit = ButtonBuilder.instance().icon(MemeDuelLoader.BACK).slot(newEditButtonSlot()).build();
		return edit;
	}

	private Slot<Button> newEditButtonSlot() {
		return new Slot<Button>() {
			@Override
			public void onEmit(final Button event) {
				GameStack.instance().remove(TestLevelScreen.this);
			}
		};
	}

	private Slot<Button> newRetryButtonSlot() {
		return new Slot<Button>() {
			@Override
			public void onEmit(final Button event) {
				final TestLevelLoadingScreen loadingScreeen = new TestLevelLoadingScreen(getGroupID());
				final GameStack stack = GameStack.instance();
				stack.replace(loadingScreeen, stack.slide());
				loadingScreeen.load();
			}
		};
	}

	private void setCurrentCannonBall(final CannonBall cannonBall) {
		cannonBallButton.icon.update(cannonBall.getIcon().getImage());
		cannonBallProvider.setType(cannonBall.name());
	}

	@Override
	public void cannonBallFired(final Vector position, final float angle) {
		final CannonBall cannonBall = cannonBallIterator.current();
		final Label label = LabelBuilder.instance().icon(cannonBall.getIcon()).build();
		firedCannonBallsGroup.add(label);
	}

	@Override
	public void gameStateChanged(final GameState gameState) {
		if (gameState == GameState.WON) {
			final Level level = GameContext.instance().getValue(MemeDuel.LEVEL);

			final MemeDuelLevelGOD god = new MemeDuelLevelGOD(getController().getModel().getGodLoader());
			god.loadFromContent(level.getContent());
			god.setAvailableCannonBalls(cannonBallProvider.getFiredCannonBalls());
			final Writer writer = PlayN.json().newWriter();
			god.save(writer);
			level.setContent(writer.write());


			// final Json.Object levelJSON = PlayN.json().parse(levelContent);
			// final Json.Array cannonBalls = PlayN.json().createArray();
			// for (final String cannonBall :
			// cannonBallProvider.getFiredCannonBalls()) {
			// final Json.Object cannonBallJSON = PlayN.json().createObject();
			// cannonBallJSON.put("type", cannonBall);
			// cannonBalls.add(cannonBallJSON);
			// }
			// levelJSON.put("cannonBalls", cannonBalls);
			//
			// final Writer writer = PlayN.json().newWriter();
			//
			// // Bug en HTML5
			// writer.object(levelJSON);
			// level.setContent(writer.write());

			GameStack.instance().replace(new SaveLevelScreen(getGroupID(), level));
		} else if (gameState == GameState.LOST) {
			// TODO faudra ajouter un ecran
			GameStack.instance().remove(TestLevelScreen.this);
		}
	}

	private static class CannonBallIterator implements Iterator<CannonBall> {

		private final AllGODLoader loader;
		private CannonBall current;

		public CannonBallIterator(final AllGODLoader loader, final CannonBall current) {
			this.current = current;
			this.loader = loader;
		}

		@Override
		public boolean hasNext() {
			return true;
		}

		@Override
		public CannonBall next() {
			int index = current.ordinal() + 1;
			if (index >= CannonBall.values().length) {
				index = 0;
			}
			current = CannonBall.values()[index];
			if (loader.getGameOptionsGOD().getLoadHelpers().containsKey(current.name())) {
				return current;
			}
			return next();
		}

		public CannonBall current() {
			return current;
		}

		@Override
		public void remove() {
		}
	}

}

