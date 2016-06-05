package io.piotrjastrzebski.jam.utils;

import com.artemis.EntitySubscription;
import com.artemis.utils.IntBag;

/**
 * Created by PiotrJ on 05/06/16.
 */
public abstract class SubscriptionAdapter implements EntitySubscription.SubscriptionListener {
	@Override public final void inserted (IntBag entities) {
		if (!checkProcessing()) return;
		int[] data = entities.getData();
		int size = entities.size();
		for (int i = 0; i < size; i++) {
			inserted(data[i]);
		}
	}

	@Override public final void removed (IntBag entities) {
		if (!checkProcessing()) return;
		int[] data = entities.getData();
		int size = entities.size();
		for (int i = 0; i < size; i++) {
			removed(data[i]);
		}
	}

	public void inserted (int entityId) {

	}

	public void removed (int entityId) {

	}

	public boolean checkProcessing () {
		return true;
	}
}
