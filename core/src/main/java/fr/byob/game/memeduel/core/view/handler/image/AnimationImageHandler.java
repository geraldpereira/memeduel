package fr.byob.game.memeduel.core.view.handler.image;

import static playn.core.PlayN.graphics;

import java.util.HashMap;
import java.util.Map;

import playn.core.CanvasImage;
import playn.core.Image;
import fr.byob.game.memeduel.core.GameImage;
import fr.byob.game.memeduel.core.MathUtils;
import fr.byob.game.memeduel.core.god.image.AnimationImageDefinition;
import fr.byob.game.memeduel.core.view.handler.AbstractViewHandler;
import fr.byob.game.memeduel.core.view.object.DynamicViewObject;
import fr.byob.game.memeduel.core.view.object.ViewObject;

public class AnimationImageHandler extends AbstractViewHandler<DynamicViewObject> implements ImageHandler {

	private final static Map<String, CanvasImage> images = new HashMap<String, CanvasImage>();

	// private final static Map<AnimationFrameId, CanvasImage> images = new
	// HashMap<AnimationFrameId, CanvasImage>();
	//
	// private static class AnimationFrameId {
	// private final String imageName;
	// private final int currentFrame;
	//
	// public AnimationFrameId(final String imageName, final int currentFrame) {
	// super();
	// this.imageName = imageName;
	// this.currentFrame = currentFrame;
	// }
	//
	// @Override
	// public int hashCode() {
	// final int prime = 31;
	// int result = 1;
	// result = prime * result + this.currentFrame;
	// result = prime * result + (this.imageName == null ? 0 :
	// this.imageName.hashCode());
	// return result;
	// }
	//
	// @Override
	// public boolean equals(final Object obj) {
	// if (this == obj) {
	// return true;
	// }
	// if (obj == null) {
	// return false;
	// }
	// if (this.getClass() != obj.getClass()) {
	// return false;
	// }
	// final AnimationFrameId other = (AnimationFrameId) obj;
	// if (this.currentFrame != other.currentFrame) {
	// return false;
	// }
	// if (this.imageName == null) {
	// if (other.imageName != null) {
	// return false;
	// }
	// } else if (!this.imageName.equals(other.imageName)) {
	// return false;
	// }
	// return true;
	// }
	//
	// }

	private AnimationImageDefinition animDefinition;
	private int currentFrame = 0;

	@Override
	public void setViewObject(final ViewObject viewObject) {
		super.setViewObject(viewObject);
		this.animDefinition = (AnimationImageDefinition) this.viewObject.getModelObject().getGOD().getImageDefinition();
	}

	@Override
	public Image createImage() {
		final Image image = this.createFrame(this.currentFrame);
		this.currentFrame++;
		this.currentFrame %= this.animDefinition.getFrameCount();
		return image;
	}

	public Image createFrame(final int frame) {
		final GameImage mdImage = this.animDefinition.getImage();
		// final AnimationFrameId id = new AnimationFrameId(mdImage.name(),
		// frame);
		final String id = mdImage.name() + frame;

		CanvasImage canvasImage = images.get(id);
		if (canvasImage == null) {
			final Image image = mdImage.getImage();
			canvasImage = graphics().createImage(MathUtils.ceil(this.animDefinition.getImageSize().width), MathUtils.ceil(this.animDefinition.getImageSize().height));
			canvasImage.canvas().save();
			canvasImage.canvas().drawImage(image, 0, 0, this.animDefinition.getImageSize().width, this.animDefinition.getImageSize().height, frame * this.animDefinition.getImageSize().width, 0, this.animDefinition.getImageSize().width, this.animDefinition.getImageSize().height);
			images.put(id, canvasImage);
		}

		return canvasImage;

	}

}
