package io.piotrjastrzebski.jam.ecs.processors.rendering;

import com.artemis.BaseSystem;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import io.piotrjastrzebski.jam.ecs.processors.rendering.ViewBoundsProcessor.ViewBounds;

/**
 * Renders current view bounds with debug camera
 *
 * Created by EvilEntity on 24/01/2016.
 */
public class ViewBoundsRenderer extends BaseSystem {
	private @Wire ViewBoundsProcessor vbp;
	private @Wire DebugShapeRenderer shapeRenderer;
	private final Color color = new Color(Color.RED);

	@Override protected void processSystem () {
		final com.badlogic.gdx.graphics.glutils.ShapeRenderer renderer = shapeRenderer.renderer;
		renderer.begin(ShapeType.Line);
		ViewBounds vb = vbp.vb;
		renderer.rect(vb.x, vb.y, vb.width, vb.height);
		renderer.end();
	}

	public void setColor (Color color) {
		this.color.set(color);
	}

	public void setColor (float r, float g, float b, float a) {
		color.set(r, g, b, a);
	}
}
