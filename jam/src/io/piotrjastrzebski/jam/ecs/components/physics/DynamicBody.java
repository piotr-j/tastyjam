package io.piotrjastrzebski.jam.ecs.components.physics;

import com.artemis.PooledComponent;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * Created by EvilEntity on 22/01/2016.
 */
public class DynamicBody extends PooledComponent implements IBody {
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
