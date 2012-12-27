package fr.byob.game.memeduel.core.model.handler.selection;

import fr.byob.game.memeduel.core.controller.SelectionType;
import fr.byob.game.memeduel.core.model.handler.AbstractModelHandler;
import fr.byob.game.memeduel.core.model.object.B2DModelObject;

public class DefaultSelectionHandler extends AbstractModelHandler<B2DModelObject> implements SelectionHandler {

	private SelectionType selectionType = SelectionType.NONE;

	@Override
	public void setSelectionType(final SelectionType selectionType) {
		this.selectionType = selectionType;
		this.modelObject.invalidate();
	}

	@Override
	public SelectionType getSelectionType() {
		return this.selectionType;
	}

}
