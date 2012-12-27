package fr.byob.game.memeduel.core.gui.screen.level;

import java.util.List;

import pythagoras.f.Vector;
import react.Slot;
import tripleplay.ui.Button;
import tripleplay.ui.Group;
import tripleplay.ui.Label;
import tripleplay.ui.Shim;
import tripleplay.ui.Style;
import tripleplay.ui.layout.AxisLayout;
import fr.byob.game.memeduel.core.GameImage;
import fr.byob.game.memeduel.core.GameStack;
import fr.byob.game.memeduel.core.GameStyles;
import fr.byob.game.memeduel.core.MemeDuelLoader;
import fr.byob.game.memeduel.core.controller.AbstractController;
import fr.byob.game.memeduel.core.god.MemeDuelGODLoader;
import fr.byob.game.memeduel.core.gui.builders.ButtonBuilder;
import fr.byob.game.memeduel.core.gui.builders.LabelBuilder;
import fr.byob.game.memeduel.core.gui.screen.MessageScreen;
import fr.byob.game.memeduel.core.gui.screen.load.PlayLevelLoadingScreen;
import fr.byob.game.memeduel.core.model.PlayModel.GameState;
import fr.byob.game.memeduel.core.model.PlayModelStateListener;
import fr.byob.game.memeduel.core.model.cannon.CannonListener;

public class PlayLevelScreen extends AbstractLevelScreen implements CannonListener, PlayModelStateListener {


	public PlayLevelScreen(final String groupID, final AbstractController controller) {
		super(groupID, controller);
	}

	// Editor
	private Group cannonBallsGroup;

	@Override
	protected Group northGroup() {
		final Group menu = new Group(AxisLayout.horizontal().gap(GameStyles.getInstance().getGap()));

		final Group leftGroup = new Group(AxisLayout.horizontal(), Style.HALIGN.left);
		final Button retry = ButtonBuilder.instance().icon(MemeDuelLoader.RETRY).slot(newRetryButtonSlot()).build();
		leftGroup.add(retry);

		final MemeDuelGODLoader loader = getController().getModel().getGodLoader();
		final List<String> cannonBallGODTypes = loader.getLevelGOD().getAvailableCannonBalls();
		cannonBallsGroup = new Group(AxisLayout.horizontal(), Style.HALIGN.left);

		for (final String type : cannonBallGODTypes) {
			final CannonBall cannonBall = CannonBall.valueOf(type);
			final Label label = LabelBuilder.instance().icon(cannonBall.getIcon()).build();
			cannonBallsGroup.add(label);
		}

		menu.add(leftGroup, cannonBallsGroup, new Shim(0, 0).setConstraint(AxisLayout.stretched()));
		menu.setConstraint(AxisLayout.stretched());
		return menu;
	}

	private Slot<Button> newRetryButtonSlot() {
		return new Slot<Button>() {
			@Override
			public void onEmit(final Button event) {
				final PlayLevelLoadingScreen loading = new PlayLevelLoadingScreen(getGroupID());
				final GameStack stack = GameStack.instance();
				stack.replace(loading, stack.slide().right());
				loading.load();
			}
		};
	}


	@Override
	public void cannonBallFired(final Vector position, final float angle) {
		this.cannonBallsGroup.removeAt(0);
	}

	@Override
	public void gameStateChanged(final GameState gameState) {
		if (gameState == GameState.WON) {
			final MessageScreen screen = new MessageScreen(getGroupID(), "YOU WON!", MemeDuelLoader.GAME_END_WIN);
			GameStack.instance().push(screen);
			GameStack.instance().remove(this);
		} else if (gameState == GameState.LOST) {
			GameImage image = null;
			if (getController().getModel().getRemainingTargets() == 1) {
				image = MemeDuelLoader.GAME_END_BATMAN;
			}else{
				image = MemeDuelLoader.GAME_END_WTF;
			}
			final MessageScreen screen = new MessageScreen(getGroupID(), "YOU LOST!", image) {
				@Override
				protected Button back() {
					back = ButtonBuilder.instance().icon(MemeDuelLoader.RETRY).slot(newRetryButtonSlot()).build();
					return back;
				}
			};
			GameStack.instance().push(screen);
			GameStack.instance().remove(this);
		}
	}
}
