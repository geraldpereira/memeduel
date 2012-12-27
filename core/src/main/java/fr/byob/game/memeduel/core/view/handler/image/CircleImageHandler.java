package fr.byob.game.memeduel.core.view.handler.image;

import static playn.core.PlayN.graphics;
import playn.core.CanvasImage;
import playn.core.Image;
import playn.core.Pattern;
import fr.byob.game.memeduel.core.GameImage;
import fr.byob.game.memeduel.core.MathUtils;
import fr.byob.game.memeduel.core.ViewUtils;
import fr.byob.game.memeduel.core.god.b2d.CircleDefinition;
import fr.byob.game.memeduel.core.view.object.ViewObject;

public class CircleImageHandler extends ShapedImageHandler {


	protected CircleDefinition circleDefinition;

	@Override
	public void setViewObject(final ViewObject viewObject) {
		super.setViewObject(viewObject);
		this.circleDefinition = (CircleDefinition) getShapeDefinition();
	}

	@Override
	public Image createImage() {
		final float modelRadius = this.circleDefinition.getRadius();
		final int viewDiameter = MathUtils.ceil(ViewUtils.toInitView(2 * modelRadius));
		final float viewRadius = ViewUtils.toInitView(modelRadius);

		final Pattern texture = this.viewObject.getGOD().getImageDefinition().getImage().getImage().toPattern();
		final CanvasImage image = graphics().createImage(viewDiameter, viewDiameter);



		image.canvas().save();
		image.canvas().setFillPattern(texture);
		image.canvas().fillCircle(viewRadius, viewRadius, viewRadius);

		if (this.isTextured) {
			final float strokeWidth = ViewUtils.toInitView(this.getTextureDefinition().getStrokeWidth());
			image.canvas().setStrokeColor(this.getStrokeColor());
			image.canvas().setStrokeWidth(strokeWidth);
			image.canvas().strokeCircle(viewRadius, viewRadius, viewRadius - strokeWidth / 2);
		}

		if (this.viewObject.getModelObject().isDamageable()) {
			final GameImage damageImage = this.viewObject.getModelObject().getDamageHandler().getDamageMDImage();
			if (damageImage != null) {
				final Pattern dmgTexture = damageImage.getImage().toPattern();
				image.canvas().setFillPattern(dmgTexture);
				image.canvas().fillCircle(viewRadius, viewRadius, viewRadius);
			}
		}

		image.canvas().restore();

		return image;
	}
}
