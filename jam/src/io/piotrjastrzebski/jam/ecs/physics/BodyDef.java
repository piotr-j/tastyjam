package io.piotrjastrzebski.jam.ecs.physics;

import com.artemis.PooledComponent;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;

/**
 * Created by EvilEntity on 22/01/2016.
 */
public class BodyDef extends PooledComponent {
	public com.badlogic.gdx.physics.box2d.BodyDef def = new com.badlogic.gdx.physics.box2d.BodyDef();
	public float restitution;
	public float friction;
	public float density;
	public short categoryBits;
	public short groupIndex;
	public short maskBits;
	public Physics.UserData userData;
	public Array<FixtureDef> fixtureDefs = new Array<FixtureDef>();

	public BodyDef () {reset();}


	public BodyDef type(com.badlogic.gdx.physics.box2d.BodyDef.BodyType type) {
		def.type = type;
		return this;
	}

	public BodyDef position(float x, float y) {
		def.position.set(x, y);
		return this;
	}

	public BodyDef position(Vector2 position) {
		def.position.set(position);
		return this;
	}

	public BodyDef rotation(float degrees) {
		def.angle = degrees * MathUtils.degreesToRadians;
		return this;
	}

	public BodyDef angle(float radians) {
		def.angle = radians;
		return this;
	}

	@Override protected void reset () {
		def.type = com.badlogic.gdx.physics.box2d.BodyDef.BodyType.StaticBody;
		def.position.setZero();
		def.angle = 0;
		def.linearVelocity.setZero();
		def.angularVelocity = 0;
		def.linearDamping = 0;
		def.angularDamping = 0;
		def.bullet = false;
		def.fixedRotation = false;
		def.allowSleep = true;
		def.active = true;
		def.awake = true;
		def.gravityScale = 1;

		categoryBits = 0x0001;
		groupIndex = 0;
		maskBits = -1;

		restitution = 0;
		friction = 0;
		density = 1;

		userData = null;

		fixtureDefs.clear();
	}
}
