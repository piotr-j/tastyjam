package io.piotrjastrzebski.jam.ecs.components.rendering;

import com.artemis.PooledComponent;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Affine2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import io.piotrjastrzebski.jam.ecs.components.Transform;
import io.piotrjastrzebski.jam.utils.Affine2Utils;

import static io.piotrjastrzebski.jam.utils.Affine2Utils.X1;
import static io.piotrjastrzebski.jam.utils.Affine2Utils.Y1;
import static io.piotrjastrzebski.jam.utils.Affine2Utils.X2;
import static io.piotrjastrzebski.jam.utils.Affine2Utils.Y2;
import static io.piotrjastrzebski.jam.utils.Affine2Utils.X3;
import static io.piotrjastrzebski.jam.utils.Affine2Utils.Y3;
import static io.piotrjastrzebski.jam.utils.Affine2Utils.X4;
import static io.piotrjastrzebski.jam.utils.Affine2Utils.Y4;

/**
 * Tint for a drawable thing
 *
 * Created by EvilEntity on 18/01/2016.
 */
public class DebugShape extends PooledComponent {
	public Array<Shape> shapes = new Array<>();

	public CircleShape addCircle(ShapeRenderer.ShapeType type, float radius) {
		CircleShape shape = CircleShape.pool.obtain().init(type, radius);
		shapes.add(shape);
		return shape;
	}

	public RectShape addRectangle(ShapeRenderer.ShapeType type, float width, float height) {
		RectShape shape = RectShape.pool.obtain().init(type, width, height);
		shapes.add(shape);
		return shape;
	}

	@Override protected void reset () {
		for (Shape shape : shapes) {
			shape.free();
		}
		shapes.clear();
	}

	@SuppressWarnings("unchecked") public static abstract class Shape<T extends Shape> implements Pool.Poolable {
		private ShapeRenderer.ShapeType type = ShapeRenderer.ShapeType.Line;
		protected static Color tmp = new Color(Color.WHITE);
		public Color color = new Color(Color.WHITE);
		public float ox;
		public float oy;
		public Affine2 affine2 = new Affine2();
		protected Shape init(ShapeRenderer.ShapeType type) {
			this.type = type;
			return this;
		}

		public ShapeRenderer.ShapeType getType () {
			return type;
		}

		public T color(Color color) {
			this.color.set(color);
			return (T)this;
		}

		public T color(float r, float g, float b, float a) {
			this.color.set(r, g, b, a);
			return (T)this;
		}

		public T oxy(float ox, float oy) {
			this.ox = ox;
			this.oy = oy;
			return (T)this;
		}

		public T type(ShapeRenderer.ShapeType type) {
			this.type = type;
			return (T)this;
		}

		public abstract void render (ShapeRenderer renderer, Transform transform);

		public abstract void free ();
		@Override public void reset () {
			type = ShapeRenderer.ShapeType.Line;
			color.set(Color.WHITE);
			ox = oy = 0;
		}

		public abstract void renderAffine2 (ShapeRenderer renderer, Transform transform);
	}

	public static class RectShape extends Shape<RectShape> {
		protected final static Pool<RectShape> pool = new Pool<RectShape>() {
			@Override protected RectShape newObject () {
				return new RectShape();
			}
		};
		public float width = .5f;
		public float height = .5f;
		public boolean centre = false;
		public float originX = .25f;
		public float originY = .25f;

		private RectShape() {}

		public RectShape init (ShapeRenderer.ShapeType type, float width, float height) {
			super.init(type);
			return size(width, height);
		}

		public RectShape centre (boolean centre) {
			this.centre = centre;
			return this;
		}

		public RectShape size (float width, float height) {
			this.width = width;
			this.height = height;
			return this;
		}

		public RectShape origin (float originX, float originY) {
			this.originX = originX;
			this.originY = originY;
			return this;
		}

		@Override public void render (ShapeRenderer renderer, Transform transform) {
			tmp.set(renderer.getColor());
			renderer.setColor(color);
			// TODO rotation?
			float x = ox + (centre?-width/2f:0);
			float y = oy + (centre?-height/2f:0);
			renderer.rect(
				transform.x + x, transform.y + y,
				x + originX, y + originY,
				width, height, 1, 1, transform.rotation
			);
			renderer.setColor(tmp);
		}


		@Override public void reset () {
			super.reset();
			width = 0.5f;
			height = 0.5f;
			originX = .25f;
			originY = .25f;
			centre = false;
		}

		private final float[] rect = new float[8];
		@Override public void renderAffine2 (ShapeRenderer renderer, Transform transform) {
			tmp.set(renderer.getColor());
			renderer.setColor(color);
			affine2.set(transform.affine2);

			float x = ox + (centre?-width/2f:0);
			float y = oy + (centre?-height/2f:0);
			affine2.translate(x, y);

			Affine2Utils.rectangle(width, height, affine2, rect);
			if (renderer.getCurrentType() == ShapeRenderer.ShapeType.Filled) {
				// cant do polygon :/
				renderer.triangle(rect[X1], rect[Y1], rect[X2], rect[Y2], rect[X3], rect[Y3]);
				renderer.triangle(rect[X1], rect[Y1], rect[X3], rect[Y3], rect[X4], rect[Y4]);
			} else {
				renderer.polygon(rect);
			}
			renderer.setColor(tmp);
		}

		@Override public void free () {
			pool.free(this);
		}
	}

	public static class CircleShape extends Shape<CircleShape> {
		protected final static Pool<CircleShape> pool = new Pool<CircleShape>() {
			@Override protected CircleShape newObject () {
				return new CircleShape();
			}
		};
		public float radius = .5f;
		public boolean centre = true;
		public int segments = 8;

		private CircleShape() {}

		public CircleShape init (ShapeRenderer.ShapeType type, float diameter) {
			super.init(type);
			this.radius = diameter/2;
			return this;
		}

		public CircleShape radius (float radius) {
			this.radius = radius;
			return this;
		}

		public CircleShape diameter (float diameter) {
			this.radius = diameter/2;
			return this;
		}

		public CircleShape centre (boolean centre) {
			this.centre = centre;
			return this;
		}

		public CircleShape segments (int segments) {
			this.segments = segments;
			return this;
		}

		@Override public void render (ShapeRenderer renderer, Transform transform) {
			tmp.set(renderer.getColor());
			renderer.setColor(color);
			renderer.circle(
				transform.x + (centre?0:+radius) + ox,
				transform.y + (centre?0:+radius) + oy,
				radius, segments
			);
			renderer.setColor(tmp);
		}

		@Override public void reset () {
			super.reset();
			radius = 0.5f;
			segments = 8;
			centre = true;
		}

		private static Vector2 tmpV2 = new Vector2();
		@Override public void renderAffine2 (ShapeRenderer renderer, Transform transform) {
			tmp.set(renderer.getColor());
			renderer.setColor(color);

			affine2.set(transform.affine2);
			affine2.translate(ox, oy);
			if (!centre) {
				affine2.translate(radius, radius);
			}
			tmpV2.set(0, 0);
			affine2.applyTo(tmpV2);
			renderer.circle(tmpV2.x, tmpV2.y, radius, segments);
			renderer.setColor(tmp);
		}

		@Override public void free () {
			pool.free(this);
		}
	}
}
