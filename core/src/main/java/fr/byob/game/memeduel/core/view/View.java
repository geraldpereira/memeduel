package fr.byob.game.memeduel.core.view;

import playn.core.GroupLayer;
import fr.byob.game.memeduel.core.god.AllGODLoader;

public interface View {

	public void loadViewObjects(final AllGODLoader godLoader);

	public GroupLayer getStaticLayerBack();

	public GroupLayer getStaticLayerFront();

	public GroupLayer getDynamicLayer();

	public GroupLayer getWorldLayer();

}
