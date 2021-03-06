package io.piotrjastrzebski.jam.ecs.components;

import com.artemis.PooledComponent;
import com.badlogic.gdx.math.Affine2;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 * Contains data about entities position, rotation and dimensions and modifiers for them
 *
 * Created by EvilEntity on 28/01/2016.
 */
public class Transform extends PooledComponent {
	public float x, y, cx, cy, z;
	public float width, height;
	public float scaleX;
	public float scaleY;
	public float originX, originY;
	public float rotation;
	// updated by Transformer.class
	public Affine2 affine2 = new Affine2();

	public int lastTick;

	public Transform () {
		reset();
	}

	public Transform xy (float x, float y) {
		this.x = x;
		this.y = y;
		cx = x + width / 2;
		cy = y + height / 2;
		return this;
	}

	public Transform xy (Vector2 position) {
		this.x = position.x;
		this.y = position.y;
		cx = x + width / 2;
		cy = y + height / 2;
		return this;
	}

	public Transform cxy (float cx, float cy) {
		this.x = cx - width / 2;
		this.y = cy - height / 2;
		this.cx = cx;
		this.cy = cy;
		return this;
	}

	public Transform cxy (Vector2 centre) {
		this.x = centre.x - width / 2;
		this.y = centre.y - height / 2;
		cx = centre.x;
		cy = centre.y;
		return this;
	}

	public Transform rotation (float rotation) {
		this.rotation = rotation % 360;
		return this;
	}

	public Transform size (float width, float height) {
		this.width = width;
		this.height = height;
		origin(width/2, height/2);
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
		x = cx = cy = y = z = 0;
		width = height = 1;
		originX = originY = .5f;
		scaleX = scaleY = 1;
		rotation = 0;
		affine2.setToTrnRotRadScl(0, 0, 0, 0, 0);
	}
}
