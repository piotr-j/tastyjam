package io.piotrjastrzebski.jam.ecs.physics;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.ReflectionPool;
import io.piotrjastrzebski.jam.ecs.GlobalSettings;
import io.piotrjastrzebski.jam.ecs.components.*;
import io.piotrjastrzebski.jam.ecs.physics.components.*;

/**
 * We support only aabb stuff
 *
 * Created by EvilEntity on 21/01/2016.
 */
public class Physics extends BaseEntitySystem {
	private ComponentMapper<Transform> mTransform;
	private ComponentMapper<BodyDef> mBodyDef;
	private ComponentMapper<DynamicBody> mDynamicBody;
	private ComponentMapper<KinematicBody> mKinematicBody;
	private ComponentMapper<StaticBody> mStaticBody;
	private ComponentMapper<PhysicsBody> mPhysicsBody;

	public World b2d;
	public float stepTime = 1/60f;
	public int velocityIters = 6;
	public int positionIters = 4;

	public Physics () {
		super(Aspect.all(Transform.class, BodyDef.class));
	}

	@Override protected void initialize () {
		b2d = new World(GlobalSettings.GRAVITY, true);
	}

	private FixtureDef fixtureDef = new FixtureDef();

	@Override protected void inserted (int entityId) {
		BodyDef bodyDef = mBodyDef.get(entityId);

		Transform trans = mTransform.get(entityId);
		bodyDef.position(trans.cx, trans.cy);
		bodyDef.rotation(trans.rotation);
		// NOTE do we want this separation?
		IBody iBody;
		switch (bodyDef.def.type) {
		case StaticBody:
			iBody = mStaticBody.create(entityId);
			break;
		case KinematicBody:
			iBody = mKinematicBody.create(entityId);
			break;
		case DynamicBody:
			iBody = mDynamicBody.create(entityId);
			break;
		default:
			throw new IllegalStateException("Invalid body type!");
		}

		Body body = b2d.createBody(bodyDef.def);
		iBody.setBody(body);

		PhysicsBody physicsBody = mPhysicsBody.create(entityId);
		physicsBody.body = body;

		fixtureDef.restitution = bodyDef.restitution;
		fixtureDef.friction = bodyDef.friction;
		fixtureDef.density = bodyDef.density;
		fixtureDef.filter.categoryBits = bodyDef.categoryBits;
		fixtureDef.filter.maskBits = bodyDef.maskBits;
		fixtureDef.filter.groupIndex = bodyDef.groupIndex;

		UserData userData = bodyDef.userData != null?bodyDef.userData: UserData.obtain(entityId);
		for (FixtureDef def : bodyDef.fixtureDefs) {
			Fixture fixture = body.createFixture(def);
			// TODO per fixture user data?
			fixture.setUserData(userData);
			def.shape.dispose();
		}
		body.setUserData(userData);
	}

	@Override protected void processSystem () {
		// TODO proper fixed time step
		b2d.step(stepTime, velocityIters, positionIters);
	}

	@Override protected void removed (int entityId) {
		if (mDynamicBody.has(entityId)) {
			Body body = mDynamicBody.get(entityId).body;
			UserData.free((UserData)body.getUserData());
			b2d.destroyBody(body);
		}
		if (mKinematicBody.has(entityId)) {
			Body body = mKinematicBody.get(entityId).body;
			UserData.free((UserData)body.getUserData());
			b2d.destroyBody(body);
		}
		if (mStaticBody.has(entityId)) {
			Body body = mStaticBody.get(entityId).body;
			UserData.free((UserData)body.getUserData());
			b2d.destroyBody(body);
		}
	}

	@Override protected void dispose () {
		super.dispose();
		b2d.dispose();
	}

	public static class UserData implements Pool.Poolable {
		protected static Pool<UserData> pool = new ReflectionPool<UserData>(UserData.class);

		public static UserData obtain (int entityId) {
			return pool.obtain().init(entityId);
		}

		public static void free (UserData userData) {
			if (userData != null){
				pool.free(userData);
			}
		}

		private int entityId;

		public Object object;

		public UserData init (int entityId) {
			this.entityId = entityId;

			return this;
		}

		@Override public void reset () {
			entityId = -1;
		}
	}
}
