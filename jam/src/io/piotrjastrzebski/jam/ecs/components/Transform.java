package io.piotrjastrzebski.jam.ecs.components;

import com.artemis.PooledComponent;
import com.badlogic.gdx.math.Vector2;

/**
 * Contains data about entities position, rotation and dimensions and modifiers for them
 *
 * Created by EvilEntity on 28/01/2016.
 */
public class Transform extends PooledComponent {
	public float x, y, z;
	public float width, height;
	public float scaleX;
	public float scaleY;
	public float originX, originY;
	public float rotation;

	public Transform () {
		reset();
	}

	public Transform xy (float x, float y) {
		this.x = x;
		this.y = y;
		return this;
	}

	public Transform xy (Vector2 position) {
		this.x = position.x;
		this.y = position.y;
		return this;
	}

	public Transform centerXY (Vector2 position) {
		this.x = position.x - width/2;
		this.y = position.y - height/2;
		return this;
	}

	public Transform rotation (float rotation) {
		this.rotation = rotation;
		return this;
	}

	public Transform size (float width, float height) {
		this.width = width;
		this.height = height;
		return this;
	}

	public Transform origin (float originX, float originY) {
		this.originX = originX;
		this.originY = originY;
		return this;
	}

	public Transform scale (float scaleX, float scaleY) {
		this.scaleX = scaleX;
		this.scaleY = scaleY;
		return this;
	}

	@Override protected void reset () {
		x = y = z = 0;
		width = height = 1;
		originX = originY = .5f;
		scaleX = scaleY = 1;
		rotation = 0;
	}

	public float centerX () {
		return x + width/2;
	}

	public float centerY () {
		return y + height/2;
	}
}
