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
 * Blending is changed in processSystem
 * <code>
 *    Gdx.gl.glEnable(GL20.GL_BLEND);
 *		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
 *	</code>
 * Created by PiotrJ on 10/03/16.
 */
public class DebugGridRenderer2 extends BaseSystem {
	private static final String TAG = DebugGridRenderer2.class.getSimpleName();
	private @Wire(name = GlobalSettings.WIRE_GAME_CAM) OrthographicCamera camera;
	private @Wire ShapeRenderer renderer;
	private @Wire ViewBounds vb;


	/**
	 * Grid with 1 unit spacing and semi transparent cyan gridColor
	 */
	public DebugGridRenderer2 () {
		this(1);
	}

	public DebugGridRenderer2 (float size) {
		this(size, 0, 1, 1, .25f);
	}

	private float size = 1f;
	private float overlapSize = .1f;
	private Color gridColor = new Color();
	private Color overlapColor = new Color();
	private Vector2 offset = new Vector2();
	private boolean fancy = true;

	public DebugGridRenderer2 (float size, float r, float g, float b, float a) {
		this.size = size;
		this.gridColor.set(r, g, b, a);
		overlapColor.set(gridColor);
	}

	@Override protected void processSystem () {
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		renderer.setProjectionMatrix(camera.combined);
		renderer.begin(ShapeRenderer.ShapeType.Line);
		renderer.setColor(gridColor);
		float sx = (int)vb.sx + offset.x;
		float sy = (int)vb.sy + offset.y;
		int width = (int)(vb.width / size) + 2;
		int height = (int)(vb.height / size) + 2;
		// major grid lines
		for (int x = -1; x <= width; x++) {
			renderer.line(sx + x * size, sy - 1, sx + x * size, sy + height);
		}
		for (int y = -1; y <= height; y++) {
			renderer.line(sx - 1, sy + y * size, sx + width, sy + y * size);
		}

		if (fancy) {
			renderer.setColor(overlapColor);
			// redo line crosses to make them more pronounced
			for (int x = -1; x <= width; x++) {
				for (int y = -1; y <= height; y++) {
					renderer.line(sx + x * size - size * overlapSize, sy + y * size, sx + x * size + size * overlapSize, sy + y * size);
					renderer.line(sx + x * size, sy + y * size - size * overlapSize, sx + x * size, sy + y * size + size * overlapSize);
				}
			}
		}
		renderer.end();
	}

	public void setFancy (boolean fancy) {
		this.fancy = fancy;
	}

	public void setGridColor (Color gridColor) {
		this.gridColor.set(gridColor);
	}

	public void setOverlapColor (Color gridColor) {
		this.gridColor.set(gridColor);
	}

	public void setOverlapSize (float overlapSize) {
		this.overlapSize = overlapSize;
	}

	public void setColor (float r, float g, float b, float a) {
		gridColor.set(r, g, b, a);
	}

	public void setSize (float size) {
		this.size = size;
	}

	public void setOffset (float x, float y) {
		offset.set(x, y);
	}
}
