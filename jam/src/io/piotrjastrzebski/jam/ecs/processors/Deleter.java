package io.piotrjastrzebski.jam.ecs.processors;

import com.artemis.*;
import com.artemis.annotations.PooledWeaver;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.utils.IntSet;

/**
 * Created by PiotrJ on 24/06/16.
 */
public class Deleter extends IteratingSystem {
	protected ComponentMapper<DeleteAfter> mDeleteAfter;

	protected IntSet deleted = new IntSet();
	@SuppressWarnings("unchecked")
	public Deleter () {
		super(Aspect.all(DeleteAfter.class));
	}

	@Override
	protected void begin() {
		deleted.clear();
	}

	@Override
	protected void process(int entityId) {
		DeleteAfter after = mDeleteAfter.get(entityId);
		if (after.type == DeleteAfter.TIME) {
			if (after.time <= 0) {
				delete(entityId);
			}
			after.time -= world.delta;
		} else if (after.type == DeleteAfter.FRAMES) {
			if (after.frames <= 0) {
				delete(entityId);
			}
			after.frames -= 1;
		} else {
			throw new IllegalArgumentException("Invalid DeleteAfter#type!");
		}
	}

	public boolean isDeleted (int entityId) {
		return deleted.contains(entityId) || !world.getEntityManager().isActive(entityId);
	}

	public boolean delete (int entityId) {
		if (!isDeleted(entityId)) {
			deleted.add(entityId);
			world.delete(entityId);
			return true;
		}
		return false;
	}

	public void deleteAfterSeconds (int entityId, float delay) {
		if (!isDeleted(entityId)) {
			mDeleteAfter.create(entityId).time(delay);
		}
	}

	public void deleteAfterFrames (int entityId, int frames) {
		if (!isDeleted(entityId)) {
			mDeleteAfter.create(entityId).frames(frames);
		}
	}
	@PooledWeaver
	public static class DeleteAfter extends Component {
		public static final int TIME = 0;
		public static final int FRAMES = 1;
		public int type = TIME;
		public float time;
		public int frames;

		public DeleteAfter time (float delay) {
			this.time = delay;
			type = TIME;
			return this;
		}

		public DeleteAfter frames (int frames) {
			this.frames = frames;
			type = FRAMES;
			return this;
		}

		public DeleteAfter nextFrame() {
			frames = 1;
			type = FRAMES;
			return this;
		}
	}
}
