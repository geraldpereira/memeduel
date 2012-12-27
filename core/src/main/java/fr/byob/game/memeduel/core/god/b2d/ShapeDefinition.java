package fr.byob.game.memeduel.core.god.b2d;

import playn.core.Json;
import playn.core.Json.Object;
import playn.core.Json.Writer;
import pythagoras.f.Dimension;
import pythagoras.f.Vector;
import fr.byob.game.memeduel.core.god.builder.ModelHandlerFactory;
import fr.byob.game.memeduel.core.god.builder.ViewHandlerFactory;
import fr.byob.game.memeduel.core.model.handler.b2d.B2DBodyHandler;
import fr.byob.game.memeduel.core.view.handler.image.ShapedImageHandler;

public interface ShapeDefinition extends ModelHandlerFactory<B2DBodyHandler>, ViewHandlerFactory<ShapedImageHandler> {
	public enum Shape {
		CIRCLE {
			@Override
			public CircleDefinition loadShapeDefinition(final Json.Object jsonEntity) {
				return CircleDefinition.circleBuilder().fromJSON(null, jsonEntity).build();
			}
		},
		BOX {
			@Override
			public BoxDefinition loadShapeDefinition(final Json.Object jsonEntity) {
				return BoxDefinition.boxBuilder().fromJSON(null, jsonEntity).build();
			}
		},
		POLYGON {
			@Override
			public PolygonDefinition loadShapeDefinition(final Object jsonEntity) {
				return PolygonDefinition.polygonBuilder().fromJSON(null, jsonEntity).build();
			}
		};
		public abstract ShapeDefinition loadShapeDefinition(final Json.Object jsonEntity);

	}

	public Shape getShapeType();

	public Dimension getSize();

	public Vector getCenterPosition();

	public void save(final Writer writer);
}
