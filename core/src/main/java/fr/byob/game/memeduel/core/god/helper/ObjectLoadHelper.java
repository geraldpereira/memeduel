package fr.byob.game.memeduel.core.god.helper;

import java.util.ArrayList;
import java.util.List;

import playn.core.Json.Object;
import fr.byob.game.memeduel.core.GameImage;
import fr.byob.game.memeduel.core.god.AllGODLoader;
import fr.byob.game.memeduel.core.god.GameObjectDefinition;
import fr.byob.game.memeduel.core.god.damage.LifespanDefinition;
import fr.byob.game.memeduel.core.god.damage.LifespanDefinition.DamageSlice;
import fr.byob.game.memeduel.core.god.damage.TrashableDefinition;
import fr.byob.game.memeduel.core.god.image.TextureDefinition;

public class ObjectLoadHelper extends LoadHelper implements GameObjectDefinition {

	protected final static String DMG25 = "_25";
	protected final static String DMG50 = "_50";
	protected final static String DMG75 = "_75";

	private boolean selectable;

	public ObjectLoadHelper() {
	}

	@Override
	public void load(final AllGODLoader loader, final Object jsonEntity) {
		super.load(loader, jsonEntity);

		imageDefinition = TextureDefinition.textureBuilder().fromJSON(loader, jsonEntity).build();

		selectable = jsonEntity.getBoolean("selectable");
		final boolean damageable = jsonEntity.getBoolean("damageable");

		if (damageable) {
			if (jsonEntity.containsKey("lifespanFactor")) {
				final String imageName = imageDefinition.getImage().name();
				final List<DamageSlice> damageSlices = new ArrayList<LifespanDefinition.DamageSlice>();
				damageSlices.add(new DamageSlice(25, GameImage.valueOf(imageName + DMG25)));
				damageSlices.add(new DamageSlice(50, GameImage.valueOf(imageName + DMG50)));
				damageSlices.add(new DamageSlice(75, GameImage.valueOf(imageName + DMG75)));
				damageDefinition = LifespanDefinition.lifespanBuilder().fromJSON(loader, jsonEntity).damageSlices(damageSlices).build();
			} else {
				damageDefinition = TrashableDefinition.builder().build();
			}
		}
	}

	@Override
	public TextureDefinition getImageDefinition() {
		return (TextureDefinition) imageDefinition;
	}

	public boolean isSelectable() {
		return selectable;
	}

}
