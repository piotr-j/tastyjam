package io.piotrjastrzebski.jam.ecs.processors.gameplay;

import com.artemis.annotations.Wire;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import io.piotrjastrzebski.jam.ecs.Globals;

/**
 * Contains current state of main pointer
 *
 * Created by EvilEntity on 24/01/2016.
 */
public class CursorProcessor extends BaseInputSystem {
	private @Wire(name = Globals.WIRE_GAME_CAM) OrthographicCamera camera;
	public int justTouchedFrameDelay = 2;
	public float x;
	public float y;
	public boolean touched;
	public boolean touchedLeft;
	public boolean touchedMid;
	public boolean touchedRight;
	public boolean justTouched;
	protected int justTouchedFrames;
	public boolean justTouchedLeft;
	protected int justTouchedLeftFrames;
	public boolean justTouchedMid;
	protected int justTouchedMidFrames;
	public boolean justTouchedRight;
	protected int justTouchedRightFrames;

	private Vector3 tmp = new Vector3();
	@Override protected void processSystem () {
		camera.unproject(tmp.set(Gdx.input.getX(), Gdx.input.getY(), 0));
		x = tmp.x;
		y = tmp.y;
		touched = Gdx.input.isTouched();
		justTouchedFrames--;
		if (justTouchedFrames < 0) {
			justTouched = false;
		}
		justTouchedLeftFrames--;
		if (justTouchedLeftFrames < 0) {
			justTouchedLeft = false;
		}
		justTouchedMidFrames--;
		if (justTouchedMidFrames < 0) {
			justTouchedMid = false;
		}
		justTouchedRightFrames--;
		if (justTouchedRightFrames < 0) {
			justTouchedRight = false;
		}
	}

	@Override public boolean touchDown (int screenX, int screenY, int pointer, int button) {
		justTouched = true;
		justTouchedFrames = justTouchedFrameDelay;
		switch (button) {
		case Input.Buttons.LEFT:
			touchedLeft = true;
			justTouchedLeft = true;
			justTouchedLeftFrames = justTouchedFrameDelay;
			break;
		case Input.Buttons.MIDDLE:
			touchedMid = true;
			justTouchedMid = true;
			justTouchedMidFrames = justTouchedFrameDelay;
			break;
		case Input.Buttons.RIGHT:
			touchedRight = true;
			justTouchedRight = true;
			justTouchedRightFrames = justTouchedFrameDelay;
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

	/**
	 * Check if cursor is contained in given rectangle
	 */
	public boolean isContained (float x, float y, float width, float height) {
		return x <= this.x && x + width >= this.x && y <= this.y && y + height >= this.y;
	}

	/**
	 * Check if cursor is contained in given rectangle specified by two corners
	 */
	public boolean isContainedCorners (float x1, float y1, float x2, float y2) {
		return x1 <= this.x && x2 >= this.x && y1 <= this.y && y2 >= this.y;
	}

	@Override public InputProcessor getProcessor () {
		return this;
	}

	@Override public int getPriority () {
		return 0;
	}
}
