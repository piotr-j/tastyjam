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
		renderer.setColor(0, 1, 1, .5f);
		renderer.rect(tm.x, tm.y, tm.width, tm.height);
		renderer.setColor(1, 0, 1, .5f);
		renderer.line(tm.x - tm.width * xs, tm.y, tm.x + tm.width * xs, tm.y);
		renderer.line(tm.x, tm.y - tm.height * xs, tm.x, tm.y + tm.height * xs);
		renderer.setColor(1, 1, 0, .5f);
		renderer.line(tm.cx - tm.width * xs, tm.cy, tm.cx + tm.width * xs, tm.cy);
		renderer.line(tm.cx, tm.cy - tm.height * xs, tm.cx, tm.cy + tm.height * xs);
	}

	@Override public void end () {
		renderer.end();
	}
}
