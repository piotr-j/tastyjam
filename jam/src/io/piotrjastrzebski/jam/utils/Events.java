package io.piotrjastrzebski.jam.utils;

import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.ai.msg.MessageDispatcher;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegraph;

/**
 * Created by PiotrJ on 06/06/16.
 */
public class Events {
	private static MessageDispatcher dispatcher = MessageManager.getInstance();

	public static void addListener(Telegraph listener, int msg) {
		dispatcher.addListener(listener, msg);
	}

	public static void addListeners(Telegraph listener, int... msgs) {
		dispatcher.addListeners(listener, msgs);
	}

	public static void dispatch(int msg) {
		dispatcher.dispatchMessage(msg);
	}

	public static void dispatch(int msg, Object extraInfo) {
		dispatcher.dispatchMessage(msg, extraInfo);
	}

	public static void dispatch(Telegraph sender, int msg) {
		dispatcher.dispatchMessage(sender, msg);
	}

	public static void dispatch(Telegraph sender, int msg, Object extraInfo) {
		dispatcher.dispatchMessage(sender, msg, extraInfo);
	}

	public static void dispatch(float delay, int msg) {
		dispatcher.dispatchMessage(delay, msg);
	}

	public static void dispatch(float delay, int msg, Object extraInfo) {
		dispatcher.dispatchMessage(delay, msg, extraInfo);
	}

	public static void update(float delta) {
		GdxAI.getTimepiece().update(delta);
		dispatcher.update();
	}

	public static void clear() {
		dispatcher.clear();
	}
}
