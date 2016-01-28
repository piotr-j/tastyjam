package io.piotrjastrzebski.jam.ecs;

import com.artemis.BaseSystem;
import com.artemis.World;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.utils.Array;
import io.piotrjastrzebski.jam.ecs.processors.gameplay.InputSystem;

/**
 *
 *
 * Created by EvilEntity on 28/01/2016.
 */
public class ArtemisUtils {
	private ArtemisUtils (){}

	/**
	 * Get InputSystems from the world and add them to multiplexer
	 */
	public static InputMultiplexer registerInput(World world) {
		InputMultiplexer multiplexer = new InputMultiplexer();
		Array<InputSystem> inputSystems = new Array<>();
		for (BaseSystem system : world.getSystems()) {
			if (system instanceof InputSystem) {
				inputSystems.add((InputSystem)system);
			}
		}
		inputSystems.sort(InputSystem.sorter);
		for (InputSystem inputSystem : inputSystems) {
			multiplexer.addProcessor(inputSystem.getProcessor());
		}
		return multiplexer;
	}
}
