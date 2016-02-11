package io.piotrjastrzebski.jam.ecs.components.rendering;

import com.artemis.PooledComponent;

/**
 * Tint for a drawable thing
 *
 * Created by EvilEntity on 18/01/2016.
 */
public class Actor extends PooledComponent {
	public com.badlogic.gdx.scenes.scene2d.Actor actual;

	@Override protected void reset () {
		actual = null;
	}
}
