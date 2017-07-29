package io.piotrjastrzebski.jam.ecs;

import com.badlogic.gdx.math.Vector2;

/**
 * Various global settings used in a lot of places
 *
 * Created by EvilEntity on 20/01/2016.
 */
public class Globals {
	private Globals (){}

	/**
	 * Initialize target scale and dimensions of ideal screen
	 * Should be called before any Screen is instantiated
	 */
	public static void init(float scale, int targetWidth, int targetHeight) {
		Globals.SCALE = scale;
		Globals.INV_SCALE = 1f/scale;
		Globals.WIDTH = targetWidth * INV_SCALE;
		Globals.HEIGHT = targetHeight * INV_SCALE;
	}

	public static float SCALE = 48f;
	public static float INV_SCALE = 1.f/ SCALE;
	public static float WIDTH = 1280 * INV_SCALE;
	public static float HEIGHT = 720 * INV_SCALE;

	public static final String WIRE_GAME_CAM = "WIRE_GAME_CAM";
	public static final String WIRE_DEBUG_GAME_CAM = "WIRE_DEBUG_GAME_CAM";
	public static final String WIRE_GAME_VP = "WIRE_GAME_VP";
	public static final String WIRE_DEBUG_GAME_VP = "WIRE_DEBUG_GAME_VP";
	public static final String WIRE_GUI_CAM = "WIRE_GUI_CAM";
	public static final String WIRE_GUI_VP = "WIRE_GUI_VP";

	public static final Vector2 GRAVITY = new Vector2();
}
