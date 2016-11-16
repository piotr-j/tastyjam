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
public class ViewBounds extends BaseSystem {
	private @Wire(name = GlobalSettings.WIRE_GAME_CAM) OrthographicCamera camera;
	private @Wire(name = GlobalSettings.WIRE_GAME_VP) ExtendViewport vp;
	public float x = 0f;
	public float y = 0f;
	public float sx = 0f;
	public float sy = 0f;
	public float ex = 0f;
	public float ey = 0f;
	public float cx = 0f;
	public float cy = 0f;
	public float width = 0f;
	public float height = 0f;


	@Override protected void processSystem () {
		width = vp.getWorldWidth() * camera.zoom;
		height = vp.getWorldHeight() * camera.zoom;
		x = sx = camera.position.x - width / 2f;
		y = sy = camera.position.y - height / 2f;

		ex = sx + width;
		ey = sy + height;
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
