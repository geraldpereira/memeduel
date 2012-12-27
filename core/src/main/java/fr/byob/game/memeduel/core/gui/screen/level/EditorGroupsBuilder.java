package fr.byob.game.memeduel.core.gui.screen.level;

import static playn.core.PlayN.graphics;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import playn.core.Platform;
import playn.core.PlayN;
import playn.core.Pointer;
import playn.core.Pointer.Event;
import pythagoras.f.Dimension;
import pythagoras.f.Rectangle;
import pythagoras.f.Vector;
import react.Slot;
import tripleplay.ui.Background;
import tripleplay.ui.Button;
import tripleplay.ui.Group;
import tripleplay.ui.Style;
import tripleplay.ui.Styles;
import tripleplay.ui.layout.AbsoluteLayout;
import tripleplay.ui.layout.AxisLayout;
import fr.byob.game.memeduel.core.GameImage;
import fr.byob.game.memeduel.core.GamePool;
import fr.byob.game.memeduel.core.GameStyles;
import fr.byob.game.memeduel.core.MemeDuelLoader;
import fr.byob.game.memeduel.core.MemeDuelUtils;
import fr.byob.game.memeduel.core.controller.EditLevelController;
import fr.byob.game.memeduel.core.controller.SelectionsListener;
import fr.byob.game.memeduel.core.editor.B2DGODFactory;
import fr.byob.game.memeduel.core.editor.B2DGODProvider;
import fr.byob.game.memeduel.core.editor.BoxGODFactory;
import fr.byob.game.memeduel.core.editor.CircleGODFactory;
import fr.byob.game.memeduel.core.editor.MemeGODProvider;
import fr.byob.game.memeduel.core.editor.PolygonGODFactory;
import fr.byob.game.memeduel.core.editor.TriangleGODFactory;
import fr.byob.game.memeduel.core.god.AllGODLoader;
import fr.byob.game.memeduel.core.god.B2DGameObjectDefiniton;
import fr.byob.game.memeduel.core.god.MemeDuelGODLoader;
import fr.byob.game.memeduel.core.gui.CanvasElement;
import fr.byob.game.memeduel.core.gui.builders.ButtonBuilder;

public class EditorGroupsBuilder implements SelectionsListener, B2DGODProvider<B2DGameObjectDefiniton> {

	private final TypeIterator typeIterator;
	private final MemeSizeIterator memeIterator;
	private final ObjectShapeIterator shapeIterator;
	private final EditorModeIterator editorModeIterator;

	// Editor
	private final Group editorButtonsLeftGroup;
	private final Group editorGroup;
	private final CanvasElement editorCanvas;

	private final Button trashButton;
	private final Button editorButton;
	private final Button shapeButton;
	private final Button typeButton;

	private B2DGODProvider<? extends B2DGameObjectDefiniton> currentProvider;

	private final MemeGODProvider memeProvider;
	private final BoxGODFactory boxFactory;
	private final CircleGODFactory circleFactory;
	private final PolygonGODFactory polygonFactory;
	private final PolygonGODFactory triangleFactory;

	public EditorGroupsBuilder(final EditLevelController controller) {
		final MemeDuelGODLoader loader = controller.getModel().getGodLoader();

		typeIterator = new TypeIterator(loader, Type.MEME);
		memeIterator = new MemeSizeIterator(loader, MemeSize.MEME_MEDIUM);
		shapeIterator = new ObjectShapeIterator(ObjectShape.BOX);
		editorModeIterator = new EditorModeIterator(EditorMode.HIDDEN);

		// The shape editor
		editorGroup = new Group(new AbsoluteLayout());
		final Group innerGroup = new Group(AxisLayout.vertical().gap(10));
		innerGroup.addStyles(Styles.none().add(Style.BACKGROUND.is(Background.beveled(0xFFFFFFFF, 0xFF000000, 0xFF000000))).add(Style.BACKGROUND.is(Background.beveled(0xFFFFFFFF, 0xFF000000, 0xFF000000))));

		final Dimension dim = GamePool.instance().popDimension();
		editorModeIterator.current().getCanvasDimensionToOut(dim);
		this.editorCanvas = new CanvasElement(dim);
		GamePool.instance().pushDimension(1);

		this.editorCanvas.getImageLayer().addListener(new Pointer.Listener() {
			final Vector position = new Vector();

			@Override
			public void onPointerStart(final Event event) {
			}

			@Override
			public void onPointerEnd(final Event event) {
				position.set(event.localX(), event.localY());
				((B2DGODFactory<?>) currentProvider).handleClick(position);
			}

			@Override
			public void onPointerDrag(final Event event) {
			}

			public void onPointerCancel(final Event event) {
			}
		});
		innerGroup.add(this.editorCanvas);
		// innerGroup.setVisible(editorModeIterator.current().isVisible());
		AbsoluteLayout.at(innerGroup, 5, 0);
		editorGroup.add(innerGroup);

		final Rectangle canvasViewport = editorModeIterator.current().getCanvasViewportRectangle();
		this.boxFactory = new BoxGODFactory(this.editorCanvas.getCanvas(), loader, MemeDuelUtils.OBJECT_WOOD, canvasViewport);
		this.circleFactory = new CircleGODFactory(this.editorCanvas.getCanvas(), loader, MemeDuelUtils.OBJECT_WOOD, canvasViewport);
		this.polygonFactory = new PolygonGODFactory(this.editorCanvas.getCanvas(), loader, MemeDuelUtils.OBJECT_WOOD, canvasViewport);
		this.triangleFactory = new TriangleGODFactory(this.editorCanvas.getCanvas(), loader, MemeDuelUtils.OBJECT_WOOD, canvasViewport);
		this.memeProvider = new MemeGODProvider(loader, memeIterator.current().name());

		this.currentProvider = this.memeProvider;

		shapeButton = ButtonBuilder.instance().slot(newShapeButtonSlot()).styles(GameStyles.getInstance().getIconButtonStyles()).build();
		typeButton = ButtonBuilder.instance().slot(newTypeButtonSlot()).styles(GameStyles.getInstance().getIconButtonStyles()).build();
		editorButton = ButtonBuilder.instance().icon(MemeDuelLoader.EDITOR_DISABLED).slot(new Slot<Button>() {
			@Override
			public void onEmit(final Button event) {
				setCurrentEditorMode(editorModeIterator.next());
			}
		}).build();
		this.trashButton = ButtonBuilder.instance().icon(MemeDuelLoader.TRASH_DISABLED).slot(new Slot<Button>() {
			@Override
			public void onEmit(final Button event) {
				controller.trashSelection();
			}
		}).build();
		// The left buttons : trash / open editor / shape / texture
		editorButtonsLeftGroup = new Group(AxisLayout.horizontal(), Style.HALIGN.left).add(typeButton, shapeButton, editorButton, trashButton).setConstraint(AxisLayout.stretched());

		this.setCurrentShape(this.shapeIterator.current());
		this.setCurrentType(this.typeIterator.current());
		this.setCurrentMemeSize(this.memeIterator.current());
		// No need
		// this.setCurrentEditorMode(editorModeIterator.current());
	}

	public Group getButtonsGroup() {
		return editorButtonsLeftGroup;
	}

	public Group getEditorGroup() {
		return editorGroup;
	}

	@Override
	public B2DGameObjectDefiniton getGOD(final Vector position, final float angle) {
		return currentProvider.getGOD(position, angle);
	}

	private Slot<Button> newShapeButtonSlot() {
		return new Slot<Button>() {
			@Override
			public void onEmit(final Button event) {
				if (typeIterator.current().isCustom()) {
					setCurrentMemeSize(memeIterator.next());
				} else {
					setCurrentShape(shapeIterator.next());
				}
			}
		};
	}

	private Slot<Button> newTypeButtonSlot() {
		return new Slot<Button>() {
			@Override
			public void onEmit(final Button event) {
				final Type prevType = typeIterator.current();
				final Type type = typeIterator.next();
				if (!prevType.isCustom() && type.isCustom()) {
					setCurrentMemeSize(memeIterator.current());
				} else if (prevType.isCustom() && !type.isCustom()) {
					setCurrentShape(shapeIterator.current());
				}
				setCurrentType(type);
			}
		};
	}

	private void setCurrentEditorMode(final EditorMode mode) {
		if (mode.isVisible()) {
			editorGroup.setVisible(true);
			final Dimension dim = GamePool.instance().popDimension();
			mode.getCanvasDimensionToOut(dim);
			editorCanvas.setSize(dim);
			GamePool.instance().pushDimension(1);
			final Rectangle canvasViewport = mode.getCanvasViewportRectangle();
			boxFactory.setCanvasViewport(canvasViewport);
			circleFactory.setCanvasViewport(canvasViewport);
			polygonFactory.setCanvasViewport(canvasViewport);
			triangleFactory.setCanvasViewport(canvasViewport);
			((B2DGODFactory<?>) currentProvider).draw();
		} else {
			editorGroup.setVisible(false);
		}
	}

	private void setCurrentType(final Type type) {
		typeButton.icon.update(type.getIcon().getImage());
		if (type.isCustom()) {
			editorButton.setEnabled(false);
			editorButton.icon.update(MemeDuelLoader.EDITOR_DISABLED.getImage());
			// editorModeIterator.setCurrent(EditorMode.HIDDEN);
			setCurrentEditorMode(EditorMode.HIDDEN);
			this.currentProvider = memeProvider;
		} else {
			setCurrentEditorMode(editorModeIterator.current());
			editorButton.setEnabled(true);
			editorButton.icon.update(MemeDuelLoader.EDITOR.getImage());
			this.boxFactory.setType(type.name());
			this.circleFactory.setType(type.name());
			this.polygonFactory.setType(type.name());
			this.triangleFactory.setType(type.name());
			((B2DGODFactory<?>) currentProvider).draw();
		}
	}

	private void setCurrentShape(final ObjectShape shape) {
		shapeButton.icon.update(shape.getIcon().getImage());
		this.currentProvider = shape.getB2DFactory(this);
		// Draw the new current factory
		((B2DGODFactory<?>) currentProvider).draw();
	}

	private void setCurrentMemeSize(final MemeSize size) {
		shapeButton.icon.update(size.getIcon().getImage());
		memeProvider.setType(size.name());
	}


	@Override
	public void selectionChanged(final boolean isEmpty, final boolean isLocked) {
		if (isEmpty) {
			this.trashButton.icon.update(MemeDuelLoader.TRASH_DISABLED.getImage());
		} else {
			this.trashButton.icon.update(MemeDuelLoader.TRASH.getImage());
		}
	}

	public enum Type {
		OBJECT_WOOD(MemeDuelLoader.WOOD_ICON), OBJECT_STONE(MemeDuelLoader.STONE_ICON), OBJECT_GLASS(MemeDuelLoader.GLASS_ICON), OBJECT_MUD(MemeDuelLoader.MUD_ICON), MEME(MemeDuelLoader.TARGET_ICON, true);

		private final GameImage icon;
		private final boolean custom;

		private Type(final GameImage icon) {
			this(icon, false);
		}

		private Type(final GameImage icon, final boolean custom) {
			this.icon = icon;
			this.custom = custom;
		}

		public boolean isCustom() {
			return custom;
		}

		public GameImage getIcon() {
			return this.icon;
		}
	}

	private static class TypeIterator implements Iterator<Type> {

		private final AllGODLoader loader;
		private Type current;

		public TypeIterator(final AllGODLoader loader, final Type current) {
			this.current = current;
			this.loader = loader;
		}

		@Override
		public boolean hasNext() {
			return true;
		}

		@Override
		public Type next() {
			int index = current.ordinal() + 1;
			if (index >= Type.values().length) {
				index = 0;
			}
			current = Type.values()[index];
			// The MEME shape as no associated helper, but we can handle it
			// anyway (we have MEME_* helpers for each size)
			if (current.isCustom() || loader.getGameOptionsGOD().getLoadHelpers().containsKey(current.name())) {
				return current;
			}
			return next();
		}

		public Type current() {
			return current;
		}

		@Override
		public void remove() {
		}
	}

	private enum MemeSize {
		MEME_MEDIUM(MemeDuelLoader.MEME_MEDIUM_ICON), MEME_LARGE(MemeDuelLoader.MEME_LARGE_ICON);

		private final GameImage icon;

		private MemeSize(final GameImage icon) {
			this.icon = icon;
		}

		public GameImage getIcon() {
			return this.icon;
		}
	}

	private static class MemeSizeIterator implements Iterator<MemeSize> {

		private final AllGODLoader loader;
		private MemeSize current;

		public MemeSizeIterator(final AllGODLoader loader, final MemeSize current) {
			this.current = current;
			this.loader = loader;
		}

		@Override
		public boolean hasNext() {
			return true;
		}

		@Override
		public MemeSize next() {
			int index = current.ordinal() + 1;
			if (index >= MemeSize.values().length) {
				index = 0;
			}
			current = MemeSize.values()[index];
			if (loader.getGameOptionsGOD().getLoadHelpers().containsKey(current.name())) {
				return current;
			}
			return next();
		}

		public MemeSize current() {
			return current;
		}

		@Override
		public void remove() {
		}
	}

	private enum ObjectShape {
		CIRCLE(MemeDuelLoader.CIRCLE) {
			@Override
			public B2DGODFactory<B2DGameObjectDefiniton> getB2DFactory(final EditorGroupsBuilder editorBuilder) {
				return editorBuilder.circleFactory;
			}
		},
		BOX(MemeDuelLoader.BOX) {
			@Override
			public B2DGODFactory<B2DGameObjectDefiniton> getB2DFactory(final EditorGroupsBuilder editorBuilder) {
				return editorBuilder.boxFactory;
			}
		},
		POLYGON(MemeDuelLoader.POLYGON) {
			@Override
			public B2DGODFactory<B2DGameObjectDefiniton> getB2DFactory(final EditorGroupsBuilder editorBuilder) {
				return editorBuilder.polygonFactory;
			}
		},
		TRIANGLE(MemeDuelLoader.TRIANGLE) {
			@Override
			public B2DGODFactory<B2DGameObjectDefiniton> getB2DFactory(final EditorGroupsBuilder editorBuilder) {
				return editorBuilder.triangleFactory;
			}
		};

		private final GameImage icon;

		private ObjectShape(final GameImage icon) {
			this.icon = icon;
		}

		public GameImage getIcon() {
			return this.icon;
		}

		public abstract B2DGODFactory<B2DGameObjectDefiniton> getB2DFactory(EditorGroupsBuilder editorBuilder);
	}


	private static class ObjectShapeIterator implements Iterator<ObjectShape> {
		private ObjectShape current;

		public ObjectShapeIterator(final ObjectShape current) {
			this.current = current;
		}

		@Override
		public boolean hasNext() {
			return true;
		}

		@Override
		public ObjectShape next() {
			int index = current.ordinal() + 1;
			if (index >= ObjectShape.values().length) {
				index = 0;
			}
			current = ObjectShape.values()[index];
			return current;
		}

		public ObjectShape current() {
			return current;
		}

		@Override
		public void remove() {
		}
	}

	private enum EditorMode {
		HIDDEN(Platform.Type.HTML, Platform.Type.JAVA, Platform.Type.ANDROID) {
			@Override
			public boolean isVisible() {
				return false;
			}

			@Override
			public void getCanvasDimensionToOut(final Dimension out) {
				// If not initialized to the max size, the hud does not display
				// the edited shapes
				MEDIUM.getCanvasDimensionToOut(out);
			}
		},
		SMALL(Platform.Type.HTML, Platform.Type.JAVA, Platform.Type.ANDROID) {
			@Override
			public boolean isVisible() {
				return true;
			}

			@Override
			public void getCanvasDimensionToOut(final Dimension out) {
				final float side = (graphics().width() + graphics().height()) / 8;
				out.setSize(side, side);
			}
		},

		MEDIUM(Platform.Type.HTML, Platform.Type.JAVA, Platform.Type.ANDROID) {
			@Override
			public boolean isVisible() {
				return true;
			}

			@Override
			public void getCanvasDimensionToOut(final Dimension out) {
				final float side = (graphics().width() + graphics().height()) / 4;
				out.setSize(side, side);
			}
		};

		private final List<Platform.Type> platformTypes;

		private EditorMode(final Platform.Type... platformTypes) {
			this.platformTypes = Arrays.asList(platformTypes);
		}

		public abstract boolean isVisible();

		public abstract void getCanvasDimensionToOut(final Dimension out);

		public Rectangle getCanvasViewportRectangle() {
			final Dimension dim = GamePool.instance().popDimension();
			this.getCanvasDimensionToOut(dim);
			final float x = 10;
			final float y = 10;
			final float width = dim.width - 20;
			final float height = dim.height - 20;
			GamePool.instance().pushDimension(1);
			return new Rectangle(x, y, width, height);
		}

	}

	private static class EditorModeIterator implements Iterator<EditorMode> {

		private EditorMode current;

		public EditorModeIterator(final EditorMode current) {
			this.current = current;
		}

		@Override
		public boolean hasNext() {
			return true;
		}

		@Override
		public EditorMode next() {
			final Platform.Type currentType = PlayN.platformType();

			int index = current.ordinal() + 1;
			if (index >= EditorMode.values().length) {
				index = 0;
			}

			current = EditorMode.values()[index];

			if (current.platformTypes.contains(currentType)) {
				return current;
			}

			return next();
		}

		public EditorMode current() {
			return current;
		}

		@Override
		public void remove() {
		}
	}
}
