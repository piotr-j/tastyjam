package io.piotrjastrzebski.jam.ecs.components.gameplay;

import com.artemis.PooledComponent;

/**
 * Tags entity with camera stickiness
 *
 * Created by EvilEntity on 18/01/2016.
 */
public class CameraFollow extends PooledComponent {
	/**
	 * Marks entity as primary, will always be visible regardless of weight
	 * Only one entity should be marked as primary
	 */
	public boolean primary;
	/**
	 * Bias for this entity, the midpoint between two entities will be closer to the one with larger weight
	 */
	public int weight;

	@Override protected void reset () {
		primary = false;
		weight = 0;
	}
}
