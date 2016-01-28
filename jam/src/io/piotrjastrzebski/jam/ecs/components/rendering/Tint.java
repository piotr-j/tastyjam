package io.piotrjastrzebski.jam.ecs.components.rendering;

import com.artemis.PooledComponent;
import com.badlogic.gdx.graphics.Color;

/**
 * Tint for a drawable thing
 *
 * Created by EvilEntity on 18/01/2016.
 */
public class Tint extends PooledComponent {
	public Color color = new Color();

	public Tint tint (Color color) {
		this.color.set(color);
		return this;
	}

	@Override protected void reset () {
		color.set(Color.WHITE);
	}
}
