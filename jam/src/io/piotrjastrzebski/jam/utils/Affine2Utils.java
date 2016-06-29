package io.piotrjastrzebski.jam.utils;

import com.badlogic.gdx.math.Affine2;

/**
 * Created by PiotrJ on 28/06/16.
 */
public class Affine2Utils {

	public final static int X1 = 0;
	public final static int Y1 = 1;
	public final static int X2 = 2;
	public final static int Y2 = 3;
	public final static int X3 = 4;
	public final static int Y3 = 5;
	public final static int X4 = 6;
	public final static int Y4 = 7;
	public static float[] rectangle (float width, float height, Affine2 transform, float[] out) {
		if (out.length != 8) throw new AssertionError("out.length != 8! "  + out.length);
		out[X1] = transform.m02;
		out[Y1] = transform.m12;
		out[X2] = transform.m01 * height + transform.m02;
		out[Y2] = transform.m11 * height + transform.m12;
		out[X3] = transform.m00 * width + transform.m01 * height + transform.m02;
		out[Y3] = transform.m10 * width + transform.m11 * height + transform.m12;
		out[X4] = transform.m00 * width + transform.m02;
		out[Y4] = transform.m10 * width + transform.m12;
		return out;
	}
}
