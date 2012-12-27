package fr.byob.game.memeduel.core.gui.screen.level;

import pythagoras.f.Vector;
import tripleplay.ui.Group;
import tripleplay.ui.Label;
import tripleplay.ui.Style;
import tripleplay.ui.layout.AxisLayout;
import fr.byob.game.memeduel.core.controller.AbstractController;
import fr.byob.game.memeduel.core.controller.PlayLevelController;
import fr.byob.game.memeduel.core.gui.builders.LabelBuilder;
import fr.byob.game.memeduel.core.model.PlayModelStateListener;
import fr.byob.game.memeduel.core.model.cannon.CannonListener;

public abstract class AbstractLevelScreen extends InGameScreen implements CannonListener, PlayModelStateListener {


	public AbstractLevelScreen(final String groupID, final AbstractController controller) {
		super(groupID, controller);
	}

	private Label scoreLabel;

	protected PlayLevelController getController() {
		return (PlayLevelController) this.controller;
	}

	@Override
	public void wasAdded() {
		super.wasAdded();
		getController().getCannon().addListener(this);
		getController().getModel().addStateListener(this);
	}

	@Override
	public void wasRemoved() {
		getController().getCannon().removeListener(this);
		getController().getModel().removeStateListener(this);
		super.wasRemoved();
	}

	@Override
	protected Group southGroup() {
		final Group group = new Group(AxisLayout.horizontal(), Style.HALIGN.center);
		scoreLabel = LabelBuilder.instance().text("0").build();
		getController().getModel().setScoreSlot(this.scoreLabel.text.slot());
		group.add(scoreLabel);
		return group;
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
