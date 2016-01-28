package io.piotrjastrzebski.jam.ecs.components.physics;

import com.badlogic.gdx.physics.box2d.Body;

/**
 * Created by EvilEntity on 22/01/2016.
 */
public interface IBody {
	void setBody (Body body);
	Body getBody ();
}
