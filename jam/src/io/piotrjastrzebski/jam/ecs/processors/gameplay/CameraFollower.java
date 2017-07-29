package io.piotrjastrzebski.jam.ecs.processors.gameplay;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import io.piotrjastrzebski.jam.ecs.Globals;
import io.piotrjastrzebski.jam.ecs.components.gameplay.CameraFollow;
import io.piotrjastrzebski.jam.ecs.components.Transform;

/**
 * Follows marked entity
 * TODO calculate midpoint between primary and secondary targets that includes weight
 * TODO smooth follow, not just instant snap to position
 *
 * Created by EvilEntity on 20/01/2016.
 */
public class CameraFollower extends IteratingSystem {
	private static final String TAG = CameraFollower.class.getSimpleName();
	private @Wire(name = Globals.WIRE_GAME_CAM) OrthographicCamera camera;
	private ComponentMapper<Transform> mTransform;
	private ComponentMapper<CameraFollow> mFollow;

	public CameraFollower () {
		super(Aspect.all(CameraFollow.class, Transform.class));
	}

	@Override protected void process (int entityId) {
		Transform t = mTransform.get(entityId);
		CameraFollow cf = mFollow.get(entityId);
		camera.position.set(t.cx, t.cy, 0f);
		camera.update();
	}
}
