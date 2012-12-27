package fr.byob.game.memeduel.core.view.handler.image;

import static playn.core.PlayN.graphics;
import playn.core.Canvas.LineCap;
import playn.core.Canvas.LineJoin;
import playn.core.CanvasImage;
import playn.core.Image;
import playn.core.Path;
import playn.core.Pattern;
import pythagoras.f.Vector;
import fr.byob.game.memeduel.core.GameImage;
import fr.byob.game.memeduel.core.GamePool;
import fr.byob.game.memeduel.core.MathUtils;
import fr.byob.game.memeduel.core.MemeDuel;
import fr.byob.game.memeduel.core.ViewUtils;
import fr.byob.game.memeduel.core.god.b2d.PolygonDefinition;
import fr.byob.game.memeduel.core.view.object.ViewObject;

public class PolygonImageHandler extends ShapedImageHandler {

	protected PolygonDefinition polygonDefinition;

	@Override
	public void setViewObject(final ViewObject viewObject) {
		super.setViewObject(viewObject);
		this.polygonDefinition = (PolygonDefinition) getShapeDefinition();
	}

	@Override
	public Image createImage() {
		float strokeWidth = 0;
		float halfStrokeWidth = 0;
		if (this.isTextured) {
			strokeWidth = ViewUtils.toInitView(this.getTextureDefinition().getStrokeWidth());
			halfStrokeWidth = strokeWidth / 2;
		}

		final int count = this.polygonDefinition.getPolygonShape().getVertexCount();
		final GamePool pool = GamePool.instance();
		final Vector[] viewPoints = pool.popVectors(count);
		final Vector centerOffset = pool.popVector().set(ViewUtils.toInitView(this.polygonDefinition.getCenterPosition().x), ViewUtils.toInitView(this.polygonDefinition.getCenterPosition().y));
		final Vector mdModelXY = pool.popVector();
		// Calcul des positions des points en prenant en compte les épaisseurs
		// de lignes
		for (int i = 0; i < count; i++) {
			this.polygonDefinition.getPolygonShape().getVertex(i, mdModelXY);
			final Vector mdXY = viewPoints[i];
			mdXY.set(ViewUtils.toInitView(mdModelXY.x), ViewUtils.toInitView(mdModelXY.y));
			if (mdXY.x > 0) {
				mdXY.x -= halfStrokeWidth;
			} else if (mdXY.x < 0) {
				mdXY.x += halfStrokeWidth;
			}

			if (mdXY.y > 0) {
				mdXY.y -= halfStrokeWidth;
			} else if (mdXY.y < 0) {
				mdXY.y += halfStrokeWidth;
			}
			mdXY.x += centerOffset.x;
			mdXY.y += centerOffset.y;
		}

		final Pattern texture = this.viewObject.getGOD().getImageDefinition().getImage().getImage().toPattern();
		final float viewWidth = ViewUtils.toInitView(this.polygonDefinition.getSize().width);
		final float viewHeight = ViewUtils.toInitView(this.polygonDefinition.getSize().height);
		final CanvasImage image = graphics().createImage(MathUtils.ceil(viewWidth), MathUtils.ceil(viewHeight));

		final Path path = image.canvas().createPath();
		path.moveTo(viewPoints[0].x, viewPoints[0].y);
		for (int i = 1; i < count; i++) {
			path.lineTo(viewPoints[i].x, viewPoints[i].y);
		}
		path.close();

		image.canvas().save();
		if (MemeDuel.debug) {
			image.canvas().setFillColor(0xFF00FF00);
			image.canvas().fillRect(0, 0, image.canvas().width(), image.canvas().height());
		}
		image.canvas().setLineCap(LineCap.ROUND);
		image.canvas().setLineJoin(LineJoin.ROUND);
		image.canvas().setFillPattern(texture);
		image.canvas().fillPath(path);

		if (this.isTextured) {
			image.canvas().setStrokeColor(this.getStrokeColor());
			image.canvas().setStrokeWidth(strokeWidth);
			image.canvas().strokePath(path);
		}

		if (this.viewObject.getModelObject().isDamageable()) {
			final GameImage damageImage = this.viewObject.getModelObject().getDamageHandler().getDamageMDImage();
			if (damageImage != null) {
				final Pattern dmgTexture = damageImage.getImage().toPattern();
				image.canvas().setFillPattern(dmgTexture);
				image.canvas().fillPath(path);
			}
		}

		if (MemeDuel.debug) {
			image.canvas().setFillColor(0xFFFF0000);
			image.canvas().drawText("" + this.polygonDefinition.getCenterPosition().x, viewWidth / 2, viewHeight / 2);
			image.canvas().drawText("" + this.polygonDefinition.getCenterPosition().y, viewWidth / 2, viewHeight / 2 + 20);
		}

		image.canvas().restore();

		pool.pushVector(count + 2);
		return image;
	}
}
