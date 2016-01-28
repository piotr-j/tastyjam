package io.piotrjastrzebski.jam.ecs.processors.rendering;

import com.artemis.BaseSystem;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import io.piotrjastrzebski.jam.ecs.GlobalSettings;

/**
 * Contains current view bounds
 *
 * Created by EvilEntity on 24/01/2016.
 */
public class ViewBoundsProcessor extends BaseSystem {
	private @Wire(name = GlobalSettings.WIRE_GAME_CAM) OrthographicCamera camera;
	private @Wire(name = GlobalSettings.WIRE_GAME_VP) ExtendViewport vp;

	public ViewBounds vb = new ViewBounds();

	@Override protected void processSystem () {
		vb.set(camera.position.x - vp.getWorldWidth() / 2f, camera.position.y - vp.getWorldHeight() / 2f,
			vp.getWorldWidth() * camera.zoom, vp.getWorldHeight() * camera.zoom);
	}

	public static class ViewBounds extends Rectangle {
		public float sx = 0f;
		public float sy = 0f;
		public float ex = 0f;
		public float ey = 0f;
		public float cx = 0f;
		public float cy = 0f;

		@Override public ViewBounds set (Rectangle rect) {
			super.set(rect);
			update();
			return this;
		}

		@Override public ViewBounds set (float x, float y, float width, float height) {
			super.set(x, y, width, height);
			update();
			return this;
		}

		private void update () {
			sx = x;
			sy = y;
			ex = x + width;
			ey = y + height;
			cx = (sx + ex) / 2;
			cy = (sy + ey) / 2;
		}

		public boolean contains(float x, float y, float margin) {
			return sx - margin <= x && ex + margin >= x && sy - margin <= y && ey + margin >= y;
		}

		public boolean overlaps(float x, float y, float width, float height, float margin) {
			return sx - margin < x + width && ex + margin > x && sy - margin < y + height && ey + margin > y;
		}
	}
}
