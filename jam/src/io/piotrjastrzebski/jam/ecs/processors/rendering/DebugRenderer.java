package io.piotrjastrzebski.jam.ecs.processors.rendering;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.PooledComponent;
import com.artemis.annotations.Wire;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import io.piotrjastrzebski.jam.ecs.Globals;

/**
 * Simple debug renderer for arbitrary
 *
 * Created by EvilEntity on 28/01/2016.
 */
public class DebugRenderer extends IteratingSystem {
	private static final String TAG = DebugRenderer.class.getSimpleName();
	private @Wire(name = Globals.WIRE_GAME_CAM) OrthographicCamera camera;
	public @Wire ShapeRenderer renderer;
	private ComponentMapper<DebugRenderable> mDebugShape;

	public DebugRenderer () {
		super(Aspect.all(DebugRenderable.class));
	}

	@Override protected void inserted (int entityId) {
		DebugRenderable renderable = mDebugShape.get(entityId);
		for (DebugRenderable.Render render : renderable.renders) {
			world.inject(render, false);
		}
	}

	@Override protected void begin () {
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		renderer.setProjectionMatrix(camera.combined);
		renderer.begin(ShapeRenderer.ShapeType.Line);
	}

	@Override protected void process (int entityId) {
		DebugRenderable renderable = mDebugShape.get(entityId);
		for (DebugRenderable.Render render : renderable.renders) {
			if (render.type() != renderer.getCurrentType()) {
				renderer.end();
				renderer.begin(render.type());
			}
			render.render(entityId, renderer);
		}
	}

	@Override public void end () {
		renderer.end();
	}

	public static class DebugRenderable extends PooledComponent {
		public Array<Render> renders = new Array<>();
		@Override protected void reset () {
			renders.clear();
		}

		public DebugRenderable add(Render render) {
			renders.add(render);
			return this;
		}

		public static abstract class Render {
			public abstract void render(int entityId, ShapeRenderer renderer);
			public ShapeRenderer.ShapeType type() {
				return ShapeRenderer.ShapeType.Line;
			}
		}
	}
}
