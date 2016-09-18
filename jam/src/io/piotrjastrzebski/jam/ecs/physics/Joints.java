package io.piotrjastrzebski.jam.ecs.physics;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.artemis.PooledComponent;
import com.artemis.annotations.EntityId;
import com.artemis.annotations.Wire;
import com.artemis.systems.IteratingSystem;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.joints.WeldJoint;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.badlogic.gdx.utils.Array;
import io.piotrjastrzebski.jam.ecs.physics.components.PhysicsBody;

import java.util.Iterator;

/**
 * Created by PiotrJ on 15/09/16.
 */
public class Joints extends IteratingSystem {
	protected ComponentMapper<Weld> mWeld;
	protected ComponentMapper<Welded> mWelded;
	protected ComponentMapper<PhysicsBody> mPhysicsBody;
	@Wire Physics physics;

	public Joints () {
		super(Aspect.all(PhysicsBody.class, Weld.class));
	}

	private WeldJointDef wjd = new WeldJointDef();
	@Override protected void process (int entityId) {
		Weld weld = mWeld.get(entityId);
		PhysicsBody bodyA = mPhysicsBody.get(weld.bodyA);
		Iterator<Weld.WeldDef> it = weld.welds.iterator();
		Welded welded = mWelded.create(entityId);
		welded.bodyA = weld.bodyA;
		while (it.hasNext()) {
			Weld.WeldDef def = it.next();
			PhysicsBody bodyB = mPhysicsBody.getSafe(def.bodyB, null);
			if (bodyB == null)
				continue;
			wjd.bodyA = bodyA.body;
			wjd.bodyB = bodyB.body;
			if (def.useLocalAnchors) {
				wjd.localAnchorA.set(def.localAnchorA);
				wjd.localAnchorB.set(def.localAnchorB);
				wjd.referenceAngle = def.referenceAngle;
			} else if (def.useAnchor){
				wjd.initialize(bodyA.body, bodyB.body, def.anchor);
			} else {
				wjd.localAnchorA.set(0, 0);
				wjd.localAnchorB.set(bodyB.body.getPosition().sub(bodyA.body.getPosition()));
				wjd.referenceAngle = bodyB.body.getAngle() - bodyA.body.getAngle();
			}
			WeldJoint joint = (WeldJoint)physics.b2d.createJoint(wjd);
			welded.bodiesB.add(def.bodyB);
			welded.joints.add(joint);
			it.remove();
		}
		if (weld.welds.size == 0) {
			mWeld.remove(entityId);
		}
	}

	public void weld(int bodyA, float laAx, float laAy, int bodyB, float laBx, float laBy, float angleRad) {
		Weld weld = mWeld.create(bodyA);
		weld.bodyA = bodyA;
		Weld.WeldDef weldDef = new Weld.WeldDef();
		weldDef.localAnchorA.set(laAx, laAy);
		weldDef.bodyB = bodyB;
		weldDef.localAnchorB.set(laBx, laBy);
		weldDef.referenceAngle = angleRad;
		weldDef.useLocalAnchors = true;
		weld.welds.add(weldDef);
	}

	public void weld(int bodyA, int bodyB, float ax, float ay) {
		Weld weld = mWeld.create(bodyA);
		weld.bodyA = bodyA;
		Weld.WeldDef weldDef = new Weld.WeldDef();
		weldDef.bodyB = bodyB;
		weldDef.useAnchor = true;
		weldDef.anchor.set(ax, ay);
		weld.welds.add(weldDef);
	}

	public void weld(int bodyA, int bodyB) {
		Weld weld = mWeld.create(bodyA);
		weld.bodyA = bodyA;
		Weld.WeldDef weldDef = new Weld.WeldDef();
		weldDef.bodyB = bodyB;
		weld.welds.add(weldDef);
	}

	public static class Weld extends PooledComponent {
		@EntityId
		public int bodyA;
		public Array<WeldDef> welds = new Array<>();

		@Override protected void reset () {
			bodyA = -1;
			welds.clear();
		}

		public static class WeldDef {
			public Vector2 anchor = new Vector2();
			public boolean useLocalAnchors = false;
			public boolean useAnchor = false;
			public Vector2 localAnchorA = new Vector2();
			@EntityId
			public int bodyB;
			public Vector2 localAnchorB = new Vector2();
			// in radians
			public float referenceAngle;
		}
	}

	public static class Welded extends PooledComponent {
		@EntityId
		public int bodyA;
		@EntityId
		public IntBag bodiesB = new IntBag();
		public Array<WeldJoint> joints = new Array<>();

		@Override protected void reset () {
			bodyA = -1;
			bodiesB.clear();
			joints.clear();
		}
	}
}
