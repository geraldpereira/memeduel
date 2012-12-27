package fr.byob.game.memeduel.core.god;

import pythagoras.f.Vector;
import fr.byob.game.memeduel.core.god.damage.DamageDefinition;
import fr.byob.game.memeduel.core.god.image.ImageDefinition;
import fr.byob.game.memeduel.core.god.layer.LayerDefinition;
import fr.byob.game.memeduel.core.god.update.UpdateDefinition;

public abstract class AnimationGOD extends DynamicGameObjectDefinition {

	public AnimationGOD(final Vector position, final float angle, final LayerDefinition layerDefinition, final ImageDefinition imageDefinition, final UpdateDefinition updateDefinition, final DamageDefinition damageDefinition) {
		super(position, angle, layerDefinition, imageDefinition, updateDefinition, damageDefinition);
	}

}
