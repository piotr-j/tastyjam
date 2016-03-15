package io.piotrjastrzebski.jam.ecs.components.rendering;

import com.artemis.PooledComponent;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import io.piotrjastrzebski.jam.ecs.components.Transform;

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

		@Override public void render (ShapeRenderer renderer, Transform transform) {
			tmp.set(renderer.getColor());
			renderer.setColor(color);
			// TODO rotation?
			renderer.rect(
				transform.x + (centre?-width/2f:0) + ox,
				transform.y + (centre?-height/2f:0) + oy,
				width, height
			);
			renderer.setColor(tmp);
		}


		@Override public void reset () {
			super.reset();
			width = 0.5f;
			height = 0.5f;
			centre = false;
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
			centre = false;
		}

		@Override public void free () {
			pool.free(this);
		}
	}
}
