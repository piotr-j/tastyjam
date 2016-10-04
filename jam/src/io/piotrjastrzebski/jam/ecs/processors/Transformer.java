package io.piotrjastrzebski.jam.ecs.processors;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.link.EntityLinkManager;
import com.artemis.link.LinkListener;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.IntSet;
import io.piotrjastrzebski.jam.ecs.components.InheritTransform;
import io.piotrjastrzebski.jam.ecs.components.Transform;

/**
 * Created by PiotrJ on 28/06/16.
 */
public class Transformer extends IteratingSystem {
	private final static String TAG = Transformer.class.getSimpleName();

	protected ComponentMapper<Transform> mTransform;
	protected ComponentMapper<InheritTransform> mInheritTransform;

	public Transformer () {
		super(Aspect.all(Transform.class));
	}

	@Override protected void initialize () {
		EntityLinkManager elm = world.getSystem(EntityLinkManager.class);
		if (elm == null) {
			Gdx.app.error(TAG, "World doesnt have EntityLinkManager!");
			return;
		}
		elm.register(InheritTransform.class, new LinkListener() {
			@Override public void onLinkEstablished (int sourceId, int targetId) {
				System.out.println("InheritTransform.onLinkEstablished s="+sourceId+", t="+targetId);
			}

			@Override public void onLinkKilled (int sourceId, int targetId) {
				System.out.println("InheritTransform.onLinkKilled s="+sourceId+", t="+targetId);
			}

			@Override public void onTargetDead (int sourceId, int deadTargetId) {
				System.out.println("InheritTransform.onTargetDead s="+sourceId+", dt="+deadTargetId);
			}

			@Override public void onTargetChanged (int sourceId, int targetId, int oldTargetId) {
				System.out.println("InheritTransform.onTargetChanged s="+sourceId+", t="+targetId+", ot="+oldTargetId);
			}
		});
	}

	int tick;
	@Override protected void begin () {
		tick++;
	}

	@Override protected void process (int entityId) {
		Transform tf = mTransform.get(entityId);
		if (tf.lastTick >= tick) return;
		tf.lastTick = tick;
		apply(tf);

		// update if we have a parent/children
		InheritTransform it = mInheritTransform.get(entityId);
		if (it != null) {
			// if we have a parent, we need its transform
			if (it.from >= 0) {
				// make sure it is processed
				process(it.from);
				Transform ftf = mTransform.get(it.from);
				tf.affine2.preMul(ftf.affine2);
			}
			// if we have children, we need to process them as well
			int[] ids = it.children.getData();
			int size = it.children.size();
			for (int i = 0; i < size; i++) {
				process(ids[i]);
			}
		}
	}

	public void inheritTransform (int parent, int child) {
		mInheritTransform.create(parent).children.add(child);
		mInheritTransform.create(child).from = parent;
	}

	public Transform apply(Transform tf) {
		// set up our affine2 matrix
		tf.affine2.setToTrnRotScl(tf.x + tf.originX, tf.y + tf.originY, tf.rotation, tf.scaleX, tf.scaleY);
		if (tf.originX != 0 || tf.originY != 0) {
			tf.affine2.translate(-tf.originX, -tf.originY);
		}
		return tf;
	}
}
