package io.piotrjastrzebski.jam.ecs;

import com.badlogic.gdx.math.Vector2;

/**
 * Various global settings used in a lot of places
 *
 * Created by EvilEntity on 20/01/2016.
 */
public class GlobalSettings {
	private GlobalSettings (){}

	/**
	 * Initialize target scale and dimensions of ideal screen
	 * Should be called before any Screen is instantiated
	 */
	public static void init(float scale, int targetWidth, int targetHeight) {
		GlobalSettings.scale = scale;
		GlobalSettings.invScale = 1f/scale;
		GlobalSettings.vpWidth = targetWidth * invScale;
		GlobalSettings.vpHeight = targetHeight * invScale;
	}

	protected static float scale = 48f;
	protected static float invScale = 1.f/scale;
	protected static float vpWidth = 1280 * invScale;
	protected static float vpHeight = 720 * invScale;

	public static float scale () {
		return scale;
	}

	public static float invScale () {
		return invScale;
	}

	public static float vpWidth () {
		return vpWidth;
	}

	public static float vpHeight () {
		return vpHeight;
	}

	public static final String WIRE_GAME_CAM = "WIRE_GAME_CAM";
	public static final String WIRE_GAME_VP = "WIRE_GAME_VP";
	public static final String WIRE_GUI_CAM = "WIRE_GUI_CAM";
	public static final String WIRE_GUI_VP = "WIRE_GUI_VP";

	public static final Vector2 GRAVITY = new Vector2();
}
