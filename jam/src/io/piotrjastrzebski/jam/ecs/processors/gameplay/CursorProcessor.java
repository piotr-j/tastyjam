package io.piotrjastrzebski.jam.ecs.processors.gameplay;

import com.artemis.annotations.Wire;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import io.piotrjastrzebski.jam.ecs.GlobalSettings;

/**
 * Contains current state of main pointer
 *
 * Created by EvilEntity on 24/01/2016.
 */
public class CursorProcessor extends BaseInputSystem {
	private @Wire(name = GlobalSettings.WIRE_GAME_CAM) OrthographicCamera camera;
	public float x;
	public float y;
	public boolean touched;
	public boolean touchedLeft;
	public boolean touchedMid;
	public boolean touchedRight;
	public boolean justTouched;
	public boolean justTouchedLeft;
	public boolean justTouchedMid;
	public boolean justTouchedRight;

	private Vector3 tmp = new Vector3();
	@Override protected void processSystem () {
		camera.unproject(tmp.set(Gdx.input.getX(), Gdx.input.getY(), 0));
		x = tmp.x;
		y = tmp.y;
		touched = Gdx.input.isTouched();
		justTouched = Gdx.input.justTouched();
		justTouchedLeft = justTouchedMid = justTouchedRight = false;
	}

	@Override public boolean touchDown (int screenX, int screenY, int pointer, int button) {
		switch (button) {
		case Input.Buttons.LEFT:
			touchedLeft = true;
			justTouchedLeft = true;
			break;
		case Input.Buttons.MIDDLE:
			touchedMid = true;
			justTouchedMid = true;
			break;
		case Input.Buttons.RIGHT:
			touchedRight = true;
			justTouchedRight = true;
			break;
		}
		return super.touchDown(screenX, screenY, pointer, button);
	}

	@Override public boolean touchUp (int screenX, int screenY, int pointer, int button) {
		switch (button) {
		case Input.Buttons.LEFT:
			touchedLeft = false;
			break;
		case Input.Buttons.MIDDLE:
			touchedMid = false;
			break;
		case Input.Buttons.RIGHT:
			touchedRight = false;
			break;
		}
		return super.touchUp(screenX, screenY, pointer, button);
	}

	@Override public InputProcessor getProcessor () {
		return this;
	}

	@Override public int getPriority () {
		return 0;
	}
}
