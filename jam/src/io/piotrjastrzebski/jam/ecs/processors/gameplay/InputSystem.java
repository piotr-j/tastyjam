package io.piotrjastrzebski.jam.ecs.processors.gameplay;

import com.badlogic.gdx.InputProcessor;

import java.util.Comparator;

/**
 * Marker interface for stuff that needs to handle input
 *
 * Created by EvilEntity on 28/01/2016.
 */
public interface InputSystem {
	Comparator<InputSystem> sorter = new Comparator<InputSystem>() {
		@Override public int compare (InputSystem o1, InputSystem o2) {
			return o1.getPriority() - o2.getPriority();
		}
	};
	InputProcessor getProcessor ();
	int getPriority ();
}
