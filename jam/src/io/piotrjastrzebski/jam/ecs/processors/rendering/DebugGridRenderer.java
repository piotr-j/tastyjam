package io.piotrjastrzebski.jam.ecs.processors.rendering;

import com.artemis.BaseSystem;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import io.piotrjastrzebski.jam.ecs.GlobalSettings;

/**
 * Draws a grid centered around GlobalSettings.WIRE_GAME_CAM with a given size
 * Assumes that camera.position is the centre of the grid, change offset if it is not
 *
 * GlobalSettings.WIRE_GAME_CAM OrthographicCamera and ShapeRenderer are @Wired in, so they must be available
 * Blending is changed in processSsytem
 * <code>
 *    Gdx.gl.glEnable(GL20.GL_BLEND);
 *		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
 *	</code>
 * Created by PiotrJ on 10/03/16.
 */
public class DebugGridRenderer extends BaseSystem {
	private static final String TAG = DebugGridRenderer.class.getSimpleName();
	private @Wire(name = GlobalSettings.WIRE_GAME_CAM) OrthographicCamera camera;
	private @Wire ShapeRenderer renderer;

	/**
	 * Grid with 1 unit spacing and semi transparent cyan color
	 */
	public DebugGridRenderer () {
		this(1);
	}

	public DebugGridRenderer (float size) {
		this(size, 0, 1, 1, .25f);
	}

	private float size = 1f;
	private Color color = new Color();
	private Vector2 offset = new Vector2();

	public DebugGridRenderer (float size, float r, float g, float b, float a) {
		this.size = size;
		this.color.set(r, g, b, a);
	}

	@Override protected void processSystem () {
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		renderer.setProjectionMatrix(camera.combined);
		renderer.begin(ShapeRenderer.ShapeType.Line);
		renderer.setColor(color);
		float cx = camera.position.x - camera.viewportWidth / 2 + offset.x;
		float cy = camera.position.y - camera.viewportHeight / 2 + offset.y;
		int width = (int)(camera.viewportWidth / size) + 2;
		int height = (int)(camera.viewportHeight / size) + 2;
		for (int x = -1; x <= width; x++) {
			renderer.line(x * size, cy - size, x * size, cy + height);
		}
		for (int y = -1; y <= height; y++) {
			renderer.line(cx - size, y * size, cx + width, y * size);
		}
		renderer.end();
	}

	public void setColor (Color color) {
		this.color.set(color);
	}

	public void setColor (float r, float g, float b, float a) {
		color.set(r, g, b, a);
	}

	public void setSize (float size) {
		this.size = size;
	}

	public void setOffset (float x, float y) {
		offset.set(x, y);
	}
}
