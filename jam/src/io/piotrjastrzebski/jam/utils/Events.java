package io.piotrjastrzebski.jam.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.ai.msg.MessageDispatcher;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Field;
import com.badlogic.gdx.utils.reflect.ReflectionException;

/**
 * Extend this class and add fields with event ids
 *
 * Created by PiotrJ on 06/06/16.
 */
public class Events {
	private final static String TAG = Events.class.getSimpleName();

	protected static MessageDispatcher dispatcher = MessageManager.getInstance();

	@Deprecated
	public static void addListener(Telegraph listener, int msg) {
		dispatcher.addListener(listener, msg);
	}
	@Deprecated
	public static void addListeners(Telegraph listener, int... msgs) {
		dispatcher.addListeners(listener, msgs);
	}
	@Deprecated
	public static void dispatch(int msg) {
		dispatcher.dispatchMessage(msg);
	}
	@Deprecated
	public static void dispatch(int msg, Object extraInfo) {
		dispatcher.dispatchMessage(msg, extraInfo);
	}
	@Deprecated
	public static void dispatch(Telegraph sender, int msg) {
		dispatcher.dispatchMessage(sender, msg);
	}
	@Deprecated
	public static void dispatch(Telegraph sender, int msg, Object extraInfo) {
		dispatcher.dispatchMessage(sender, msg, extraInfo);
	}
	@Deprecated
	public static void dispatch(float delay, int msg) {
		dispatcher.dispatchMessage(delay, msg);
	}
	@Deprecated
	public static void dispatch(float delay, int msg, Object extraInfo) {
		dispatcher.dispatchMessage(delay, msg, extraInfo);
	}

	public static void register(Telegraph listener, int msg) {
		dispatcher.addListener(listener, msg);
	}

	public static void register(Telegraph listener, int... msgs) {
		dispatcher.addListeners(listener, msgs);
	}

	public static void unregister(Telegraph listener, int msg) {
		dispatcher.removeListener(listener, msg);
	}

	public static void unregister(Telegraph listener, int... msgs) {
		dispatcher.removeListener(listener, msgs);
	}

	public static void send(int msg) {
		dispatcher.dispatchMessage(msg);
	}

	public static void send(int msg, Object extraInfo) {
		dispatcher.dispatchMessage(msg, extraInfo);
	}

	public static void send(Telegraph sender, int msg) {
		dispatcher.dispatchMessage(sender, msg);
	}

	public static void send(Telegraph sender, int msg, Object extraInfo) {
		dispatcher.dispatchMessage(sender, msg, extraInfo);
	}

	public static void send(float delay, int msg) {
		dispatcher.dispatchMessage(delay, msg);
	}

	public static void send(float delay, int msg, Object extraInfo) {
		dispatcher.dispatchMessage(delay, msg, extraInfo);
	}

	public static void update(float delta) {
		GdxAI.getTimepiece().update(delta);
		dispatcher.update();
	}

	public static void clear() {
		dispatcher.clear();
	}

	/**
	 * Check if all public final static int fields have unique values
	 *
	 * @return if all event ids are unique
	 */
	public static boolean validate(Class cls) {
		boolean valid = true;
		// check if we have made a typo and have duplicate event ids
		Field[] fields = ClassReflection.getDeclaredFields(cls);
		IntMap<String> eventIdName = new IntMap<>();

		for (Field field : fields) {
			if (!field.isStatic() || field.getType() != int.class)
				continue;
			try {
				// NOTE we could try messing with the fields to assign them sequential ids, but it is messy and might not work
				int id = (int)field.get(cls);
				if (eventIdName.containsKey(id)) {
					Gdx.app.error(TAG, "Event '" + eventIdName.get(id) + "' clashes with event '" + field.getName() + "', id = " + id);
					valid = false;
				} else {
					eventIdName.put(id, field.getName());
				}
			} catch (ReflectionException e) {
				e.printStackTrace();
			}
		}
		return valid;
	}
}
