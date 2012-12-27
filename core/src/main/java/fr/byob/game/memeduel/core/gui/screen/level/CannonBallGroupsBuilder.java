package fr.byob.game.memeduel.core.gui.screen.level;

import java.util.Iterator;

import pythagoras.f.Vector;
import react.Slot;
import tripleplay.ui.Button;
import tripleplay.ui.Group;
import tripleplay.ui.Label;
import tripleplay.ui.Style;
import tripleplay.ui.layout.AxisLayout;
import fr.byob.game.memeduel.core.controller.PlayLevelController;
import fr.byob.game.memeduel.core.editor.B2DGODProvider;
import fr.byob.game.memeduel.core.editor.TestCannonBallGODProvider;
import fr.byob.game.memeduel.core.god.AllGODLoader;
import fr.byob.game.memeduel.core.god.MemeDuelGODLoader;
import fr.byob.game.memeduel.core.god.cannon.CannonBallGOD;
import fr.byob.game.memeduel.core.gui.builders.ButtonBuilder;
import fr.byob.game.memeduel.core.gui.builders.LabelBuilder;
import fr.byob.game.memeduel.core.model.cannon.CannonListener;

public class CannonBallGroupsBuilder implements B2DGODProvider<CannonBallGOD>, CannonListener {

	private final CannonBallIterator cannonBallIterator;

	private final Group selectCannonBallsGroup;
	private final Group firedCannonBallsGroup;

	private final Button cannonBallButton;
	private final TestCannonBallGODProvider cannonBallProvider;

	public CannonBallGroupsBuilder(final PlayLevelController controller) {
		final MemeDuelGODLoader loader = controller.getModel().getGodLoader();

		cannonBallProvider = controller.getCannon().getCannonBallGODProvider();
		cannonBallIterator = new CannonBallIterator(loader, CannonBall.CANNON_BALL_SMALL);

		this.cannonBallButton = ButtonBuilder.instance().slot(new Slot<Button>() {
			@Override
			public void onEmit(final Button event) {
				setCurrentCannonBall(cannonBallIterator.next());
			}
		}).build();
		selectCannonBallsGroup = new Group(AxisLayout.horizontal(), Style.HALIGN.left).add(cannonBallButton);

		firedCannonBallsGroup = new Group(AxisLayout.horizontal(), Style.HALIGN.left);

		setCurrentCannonBall(this.cannonBallIterator.current());
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

	public Group getSelectCannonBallsGroup() {
		return selectCannonBallsGroup;
	}

	public Group getFiredCannonBallsGroup() {
		return firedCannonBallsGroup;
	}

	@Override
	public CannonBallGOD getGOD(final Vector position, final float angle) {
		return cannonBallProvider.getGOD(position, angle);
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

	@Override
	public void cannonBallDamaged() {
	}

	@Override
	public void cannonBallMoved(final Vector position, final float angle) {
	}

	@Override
	public void cannonBallBonusActivated(final Vector position, final float angle) {
	}
}
