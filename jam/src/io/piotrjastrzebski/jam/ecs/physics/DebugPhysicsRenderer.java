package io.piotrjastrzebski.jam.ecs.physics;

import com.artemis.BaseSystem;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import io.piotrjastrzebski.jam.ecs.Globals;

/**
 * Created by EvilEntity on 18/01/2016.
 */
public class DebugPhysicsRenderer extends BaseSystem {
	private @Wire ShapeRenderer shapes;
	private @Wire(name = Globals.WIRE_GAME_CAM) OrthographicCamera camera;
	private @Wire Physics physics;

	public DebugPhysicsRenderer () {
		super();
	}

	private Box2DDebugRenderer renderer;
	@Override protected void initialize () {
		renderer = new Box2DDebugRenderer();
	}

	@Override protected void processSystem () {
		renderer.render(physics.b2d, camera.combined);
	}
}
