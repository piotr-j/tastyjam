package io.piotrjastrzebski.jam.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * Wrapper for OrthographicCamera that shakes it
 *
 * Created by PiotrJ on 21/04/16.
 */
public class CameraShaker {
	public interface ShakeStrategy {
		Vector3 calculateOffset (float delta, float magnitude, Vector3 out);
	}
	private static ShakeStrategy simpleStrategy;
	private Vector3 camPos;
	private Vector3 out;
	private OrthographicCamera camera;
	private float duration;
	private float magnitude;
	private float maxMagnitude = 2;
	private float magnitudeTaperScale = 3f;
	private ShakeStrategy strategy;

	/**
	 * Create CameraShaker with RandomShaker strategy
	 */
	public CameraShaker () {
		this(null);
	}

	/**
	 * Create CameraShaker with given strategy
	 * @param strategy ShakeStrategy to use, if null default one will be created
	 */
	public CameraShaker (ShakeStrategy strategy) {
		this.strategy = strategy;
		if (strategy == null) {
			if (simpleStrategy == null) {
				simpleStrategy = new RandomShaker();
			}
			this.strategy = simpleStrategy;
		}
		camPos = new Vector3();
		out = new Vector3();
	}

	/**
	 * Shake the camera
	 *
	 * Why minimum time? The noise function used spits out 0 for integer values,
	 * this allows for the shake to end very near the original camera position, albeit not at exact time
	 * How fast that happens is dependant on #getMagnitudeTaperScale()
	 *
	 * multiple shakes will be added up to each other
	 *
	 * @param minTime minimum time the shaking will take place
	 * @param magnitude magnitude of the shape, in game units
	 */
	public void shake(float minTime, float magnitude) {
		duration += minTime;
		this.magnitude = Math.min(this.magnitude + magnitude, maxMagnitude);
	}

	/**
	 * Shakes given camera, changes its position
	 */
	public void begin(float delta, OrthographicCamera camera) {
		if (this.camera != null) throw new AssertionError("Call #end first!");
		this.camera = camera;
		camPos.set(camera.position);
		if (duration > 0) {
			duration -= delta;
			out.setZero();
			strategy.calculateOffset(delta, magnitude, out);
			camera.position.add(out);
			camera.update();
		} else if (magnitude > 0){
			// taper of magnitude quickly until we end up close enough to original camera position
			magnitude -= magnitudeTaperScale * delta;
			out.setZero();
			strategy.calculateOffset(delta, magnitude, out);
			if (!out.isZero(0.001f)) {
				camera.position.add(out);
				camera.update();
				duration -= delta;
			} else {
				magnitude = 0;
				int frames = (int)((-duration)/(1f/60f));
				Gdx.app.log("", "Overshoot = " + frames + "f, " + (-duration) + "ms");
				duration = 0;
			}
		}
	}

	/**
	 * Reverts previously used Camera to pre shake position
	 */
	public void end() {
		if (camera == null) throw new AssertionError("Call #begin first!");
		camera.position.set(camPos);
		camera.update();
		camera = null;
	}

	/**
	 * Stop shaking like it would normally after the set duration has elapse
	 */
	public void stop () {
		duration = 0;
	}

	/**
	 * Immediately cancel shaking
	 */
	public void cancel () {
		duration = 0;
		magnitude = 0;
	}

	public float getDuration () {
		return duration;
	}

	public float getMagnitude () {
		return magnitude;
	}

	public boolean isShaking () {
		return magnitude > 0;
	}

	public float getMaxMagnitude () {
		return maxMagnitude;
	}

	public CameraShaker setMaxMagnitude (float maxMagnitude) {
		this.maxMagnitude = maxMagnitude;
		return this;
	}

	public float getMagnitudeTaperScale () {
		return magnitudeTaperScale;
	}

	public CameraShaker setMagnitudeTaperScale (float magnitudeTaperScale) {
		this.magnitudeTaperScale = magnitudeTaperScale;
		return this;
	}

	public static class NoiseShaker implements ShakeStrategy {
		private float timer = 0;
		@Override public Vector3 calculateOffset (float delta, float magnitude, Vector3 out) {
			timer += delta;
			out.x = SimplexNoise.noise(timer * 10) * magnitude;
			out.y = SimplexNoise.noise(timer * 5) * magnitude;
			return out;
		}
	}

	public static class RandomShaker implements ShakeStrategy {
		@Override public Vector3 calculateOffset (float delta, float magnitude, Vector3 out) {
			out.x = MathUtils.random(-magnitude, magnitude);
			out.y = MathUtils.random(-magnitude, magnitude);
			return out;
		}
	}

	public static class RandomInterpolatedShaker implements ShakeStrategy {
		private float timer = 0;
		private float freq = 16;
		private Vector2 start = new Vector2();
		private Vector2 target = new Vector2();
		@Override public Vector3 calculateOffset (float delta, float magnitude, Vector3 out) {
			// note this isn't so great if magnitude changes
			if (timer <= 0) {
				start.set(target);
				target.set(MathUtils.random(-magnitude, magnitude), MathUtils.random(-magnitude, magnitude));
			}
			timer += delta;
			out.x = Interpolation.linear.apply(start.x, target.x, timer * freq);
			out.y = Interpolation.linear.apply(start.y, target.y, timer * freq);
			// pick target freq times a second
			if (timer >= 1f/freq) {
				timer = 0;
			}
			return out;
		}
	}
}
