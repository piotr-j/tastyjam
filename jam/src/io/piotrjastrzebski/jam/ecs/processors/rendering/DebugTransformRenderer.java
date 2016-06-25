package io.piotrjastrzebski.jam.ecs.processors.rendering;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import io.piotrjastrzebski.jam.ecs.GlobalSettings;
import io.piotrjastrzebski.jam.ecs.components.Transform;
import io.piotrjastrzebski.jam.ecs.components.rendering.DebugShape;
import io.piotrjastrzebski.jam.ecs.components.rendering.Tint;

/**
 * Simple renderer
 *
 * Created by EvilEntity on 28/01/2016.
 */
public class DebugTransformRenderer extends IteratingSystem {
	private static final String TAG = DebugTransformRenderer.class.getSimpleName();
	private @Wire(name = GlobalSettings.WIRE_GAME_CAM) OrthographicCamera camera;
	public @Wire ShapeRenderer renderer;
	private ComponentMapper<Transform> mTransform;

	public DebugTransformRenderer () {
		this(Aspect.all(Transform.class, DebugShape.class));
	}

	public DebugTransformRenderer (Aspect.Builder aspect) {
		super(aspect);
	}

	@Override protected void begin () {
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		renderer.setProjectionMatrix(camera.combined);
		renderer.begin(ShapeRenderer.ShapeType.Line);
	}

	float xs = .25f;
	@Override protected void process (int entityId) {

		Transform tm = mTransform.get(entityId);
		float lw = tm.width * xs/2;
		float lh = tm.height * xs/2;
		renderer.setColor(0, 1, 1, .75f);
		renderer.rect(tm.x, tm.y, tm.width, tm.height);
		renderer.setColor(1, 0, 1, .75f);
		renderer.line(tm.x - lw, tm.y, tm.x + lw, tm.y);
		renderer.line(tm.x, tm.y - lh, tm.x, tm.y + lh);
		renderer.setColor(1, 1, 0, .75f);
		renderer.line(tm.cx - lw, tm.cy, tm.cx + lw, tm.cy);
		renderer.line(tm.cx, tm.cy - lh, tm.cx, tm.cy + lh);
		renderer.setColor(0, 0, 1, .75f);
		renderer.line(tm.x + tm.originX - lw, tm.y + tm.originY, tm.x + tm.originX + lw, tm.y + tm.originY);
		renderer.line(tm.x + tm.originX, tm.y + tm.originY - lh, tm.x + tm.originX , tm.y + tm.originY + lh);
	}

	@Override public void end () {
		renderer.end();
	}
}
