package io.piotrjastrzebski.jam.ecs.processors.gameplay;

import com.artemis.Aspect;
import com.artemis.Component;
import com.artemis.ComponentMapper;
import com.artemis.PooledComponent;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

import java.util.Iterator;

/**
 * Remove component after specified time
 *
 * Created by EvilEntity on 14/06/2016.
 */
public class ComponentRemover extends IteratingSystem {
	protected ComponentMapper<RemoveComponent> mRemoveAfter;
	public ComponentRemover () {
		super(Aspect.all(RemoveComponent.class));
	}

	private DeltaTimeSource defaultDts;
	private DeltaTimeSource dts;
	@Override protected void initialize () {
		super.initialize();
		defaultDts = new DeltaTimeSource() {
			@Override public float dt () {
				return world.delta;
			}
		};
		dts = defaultDts;
	}

	public void setDeltaTimeSource(DeltaTimeSource dts) {
		if (dts == null) {
			this.dts = defaultDts;
		} else {
			this.dts = dts;
		}
	}

	@Override protected void process (int entityId) {
		RemoveComponent ra = mRemoveAfter.get(entityId);
		Iterator<RemoveAfterEntry> it = ra.entries.iterator();
		while (it.hasNext()) {
			RemoveAfterEntry entry = it.next();
			entry.timer += dts.dt();
			if (entry.timer >= entry.delay) {
				world.getMapper(entry.componentClass).remove(entityId);
				it.remove();
			}
		}
		if (ra.entries.size == 0) {
			mRemoveAfter.remove(entityId);
		}
	}

	public static class RemoveComponent extends PooledComponent {
		private Array<RemoveAfterEntry> entries = new Array<RemoveAfterEntry>();

		@Override protected void reset () {
			RemoveAfterEntry.pool.freeAll(entries);
			entries.clear();
		}

		public RemoveComponent queue (Class<? extends Component> componentClass, float delay) {
			entries.add(RemoveAfterEntry.pool.obtain().init(componentClass, delay));
			return this;
		}
	}

	/**
	 * Public for reflection
	 */
	public static class RemoveAfterEntry implements Pool.Poolable {
		public static Pool<RemoveAfterEntry> pool = new Pool<RemoveAfterEntry>() {
			@Override protected RemoveAfterEntry newObject () {
				return new RemoveAfterEntry();
			}
		};
		public Class<? extends Component> componentClass;
		public float delay;
		public float timer;

		@Override public void reset () {
			componentClass = null;
			delay = 0;
			timer = 0;
		}

		public RemoveAfterEntry init (Class<? extends Component> componentClass, float delay) {
			this.componentClass = componentClass;
			this.delay = delay;
			return this;
		}
	}

	public interface DeltaTimeSource {
		float dt ();
	}
}
