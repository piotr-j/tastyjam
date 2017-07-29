package io.piotrjastrzebski.jam.ecs.processors.rendering;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import io.piotrjastrzebski.jam.ecs.Globals;
import io.piotrjastrzebski.jam.ecs.components.Transform;
import io.piotrjastrzebski.jam.ecs.components.rendering.DebugShape;

/**
 * Simple renderer
 *
 * Created by EvilEntity on 28/01/2016.
 */
public class DebugTransformRenderer extends IteratingSystem {
	private static final String TAG = DebugTransformRenderer.class.getSimpleName();
	private @Wire(name = Globals.WIRE_GAME_CAM) OrthographicCamera camera;
	private @Wire ShapeRenderer renderer;
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

	private float xs = .25f;
	@Override protected void process (int entityId) {

		Transform tm = mTransform.get(entityId);
		float l = Math.min(tm.width * xs/2, tm.height * xs/2);
		// cyan
		renderer.setColor(0, 1, 1, .5f);
		renderer.rect(tm.x, tm.y, tm.width, tm.height);
		renderer.setColor(0, 1, 1, .75f);
		renderer.rect(tm.x, tm.y, tm.originX, tm.originY, tm.width, tm.height, 1, 1, tm.rotation);
		// magenta - position
		renderer.setColor(1, 0, 1, .75f);
		renderer.line(tm.x - l, tm.y, tm.x + l, tm.y);
		renderer.line(tm.x, tm.y - l, tm.x, tm.y + l);
		// yellow - centre position
		renderer.setColor(1, 1, 0, .75f);
		renderer.line(tm.cx - l, tm.cy, tm.cx + l, tm.cy);
		renderer.line(tm.cx, tm.cy - l, tm.cx, tm.cy + l);
		// blue - origin
		renderer.setColor(0, 0, 1, .75f);
		renderer.line(tm.x + tm.originX - l, tm.y + tm.originY, tm.x + tm.originX + l, tm.y + tm.originY);
		renderer.line(tm.x + tm.originX, tm.y + tm.originY - l, tm.x + tm.originX , tm.y + tm.originY + l);
	}

	@Override public void end () {
		renderer.end();
	}
}
