package fr.byob.game.memeduel.core.view.handler.image;

import static playn.core.PlayN.graphics;
import playn.core.Canvas.LineCap;
import playn.core.Canvas.LineJoin;
import playn.core.CanvasImage;
import playn.core.Image;
import playn.core.Pattern;
import fr.byob.game.memeduel.core.GameImage;
import fr.byob.game.memeduel.core.MathUtils;
import fr.byob.game.memeduel.core.MemeDuel;
import fr.byob.game.memeduel.core.ViewUtils;
import fr.byob.game.memeduel.core.god.b2d.BoxDefinition;
import fr.byob.game.memeduel.core.model.handler.damage.DamageHandler;
import fr.byob.game.memeduel.core.model.handler.damage.LifespanDamageHandler;
import fr.byob.game.memeduel.core.view.object.ViewObject;

public class BoxImageHandler extends ShapedImageHandler {

	protected BoxDefinition boxDefinition;

	@Override
	public void setViewObject(final ViewObject viewObject) {
		super.setViewObject(viewObject);
		this.boxDefinition = (BoxDefinition) getShapeDefinition();
	}

	@Override
	public Image createImage() {

		final Pattern texture = this.viewObject.getGOD().getImageDefinition().getImage().getImage().toPattern();
		final float viewWidth = ViewUtils.toInitView(this.boxDefinition.getSize().width);
		final float viewHeight = ViewUtils.toInitView(this.boxDefinition.getSize().height);
		final CanvasImage image = graphics().createImage(MathUtils.ceil(viewWidth), MathUtils.ceil(viewHeight));


		image.canvas().save();
		image.canvas().setLineCap(LineCap.ROUND);
		image.canvas().setLineJoin(LineJoin.ROUND);
		image.canvas().setFillPattern(texture);
		image.canvas().fillRect(0, 0, viewWidth, viewHeight);

		if (this.isTextured) {
			final float strokeWidth = ViewUtils.toInitView(this.getTextureDefinition().getStrokeWidth());
			final float halfStrokeWidth = strokeWidth / 2;
			image.canvas().setStrokeColor(this.getStrokeColor());
			image.canvas().setStrokeWidth(strokeWidth);
			image.canvas().strokeRect(halfStrokeWidth, halfStrokeWidth, viewWidth - strokeWidth, viewHeight - strokeWidth);
		}

		if (this.viewObject.getModelObject().isDamageable()) {
			final GameImage damageImage = this.viewObject.getModelObject().getDamageHandler().getDamageMDImage();
			if (damageImage != null) {
				final Pattern dmgTexture = damageImage.getImage().toPattern();
				image.canvas().setFillPattern(dmgTexture);
				image.canvas().fillRect(0, 0, viewWidth, viewHeight);
			}
		}

		if (MemeDuel.debug) {
			final DamageHandler handler = viewObject.getModelObject().getDamageHandler();
			if (handler != null && handler instanceof LifespanDamageHandler) {
				image.canvas().setFillColor(0xFF00FF00);
				image.canvas().drawText("" + ((LifespanDamageHandler) handler).getInitialLifespan(), viewWidth / 2, viewHeight / 2);
			}
		}

		image.canvas().restore();
		return image;
	}

}
