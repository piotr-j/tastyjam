package io.piotrjastrzebski.jam.ecs.processors.gameplay;

import com.artemis.BaseSystem;
import com.badlogic.gdx.InputProcessor;
import io.piotrjastrzebski.jam.ecs.processors.gameplay.InputSystem;

/**
 * BaseSystem adapter for InputProcessor
 *
 * Created by EvilEntity on 28/01/2016.
 */
public abstract class BaseInputSystem extends BaseSystem implements InputProcessor, InputSystem {

	@Override public int getPriority () {
		return 0;
	}

	@Override public InputProcessor getProcessor () {
		return this;
	}

	@Override public boolean keyDown (int keycode) {
		return false;
	}

	@Override public boolean keyUp (int keycode) {
		return false;
	}

	@Override public boolean keyTyped (char character) {
		return false;
	}

	@Override public boolean touchDown (int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override public boolean touchUp (int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override public boolean touchDragged (int screenX, int screenY, int pointer) {
		return false;
	}

	@Override public boolean mouseMoved (int screenX, int screenY) {
		return false;
	}

	@Override public boolean scrolled (int amount) {
		return false;
	}
}
