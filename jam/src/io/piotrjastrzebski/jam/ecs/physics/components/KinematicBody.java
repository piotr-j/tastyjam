package io.piotrjastrzebski.jam.ecs.physics.components;

import com.artemis.PooledComponent;
import com.badlogic.gdx.physics.box2d.Body;
import io.piotrjastrzebski.jam.ecs.physics.IBody;

/**
 * Created by EvilEntity on 22/01/2016.
 */
public class KinematicBody extends PooledComponent implements IBody {
	public Body body;

	@Override public void setBody (Body body) {
		this.body = body;
	}

	@Override public Body getBody () {
		return body;
	}

	@Override protected void reset () {
		body = null;
	}
}
