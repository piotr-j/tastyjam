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
public class DebugShapeRenderer extends IteratingSystem {
	private static final String TAG = DebugShapeRenderer.class.getSimpleName();
	private @Wire(name = GlobalSettings.WIRE_GAME_CAM) OrthographicCamera camera;
	public @Wire ShapeRenderer renderer;
	private ComponentMapper<Transform> mTransform;
	private ComponentMapper<DebugShape> mDebugShape;
	private ComponentMapper<Tint> mTint;

	public DebugShapeRenderer () {
		super(Aspect.all(Transform.class, DebugShape.class));
	}

	@Override protected void begin () {
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		renderer.setProjectionMatrix(camera.combined);
		renderer.begin(ShapeRenderer.ShapeType.Line);
	}

	@Override protected void process (int entityId) {
		Tint tint = mTint.getSafe(entityId);
		if (tint != null) renderer.setColor(tint.color);
		Transform transform = mTransform.get(entityId);
		DebugShape debugShape = mDebugShape.get(entityId);
		for (DebugShape.Shape shape : debugShape.shapes) {
			if (renderer.getCurrentType() != shape.getType()) {
				renderer.end();
				renderer.begin(shape.getType());
			}
			shape.render(renderer, transform);
		}

		if (tint != null) renderer.setColor(Color.WHITE);
	}

	@Override public void end () {
		renderer.end();
	}
}
