package fr.byob.game.memeduel.core.model.handler.selection;

import fr.byob.game.memeduel.core.controller.SelectionType;
import fr.byob.game.memeduel.core.model.handler.ModelHandler;

public interface SelectionHandler extends ModelHandler {

	public void setSelectionType(final SelectionType selectionType);

	public SelectionType getSelectionType();

}
