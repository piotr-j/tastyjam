package io.piotrjastrzebski.jam.ecs.processors.rendering;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import io.piotrjastrzebski.jam.ecs.Globals;
import io.piotrjastrzebski.jam.ecs.components.Transform;
import io.piotrjastrzebski.jam.ecs.components.rendering.DebugShape;
import io.piotrjastrzebski.jam.utils.Affine2Utils;

/**
 * Simple renderer
 *
 * Created by EvilEntity on 28/01/2016.
 */
public class DebugTransformRendererAffine2 extends IteratingSystem {
	private static final String TAG = DebugTransformRendererAffine2.class.getSimpleName();
	private @Wire(name = Globals.WIRE_GAME_CAM) OrthographicCamera camera;
	private @Wire ShapeRenderer renderer;
	private ComponentMapper<Transform> mTransform;

	public DebugTransformRendererAffine2 () {
		this(Aspect.all(Transform.class, DebugShape.class));
	}

	public DebugTransformRendererAffine2 (Aspect.Builder aspect) {
		super(aspect);
	}

	@Override protected void begin () {
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		renderer.setProjectionMatrix(camera.combined);
		renderer.begin(ShapeRenderer.ShapeType.Line);
	}

	public float crossSize = .25f;
	Vector2 tmp = new Vector2();
	float[] tmpRect = new float[8];
	@Override protected void process (int entityId) {

		Transform tm = mTransform.get(entityId);

		// cyan
		renderer.setColor(0, 1, 1, .5f);
		Affine2Utils.rectangle(tm.width, tm.height, tm.affine2, tmpRect);
		renderer.polygon(tmpRect);

		// magenta - position
		renderer.setColor(1, 0, 1, .75f);
		tmp.set(0, 0);
		tm.affine2.applyTo(tmp);
		renderer.line(tmp.x - crossSize, tmp.y, tmp.x + crossSize, tmp.y);
		renderer.line(tmp.x, tmp.y - crossSize, tmp.x, tmp.y + crossSize);
		// yellow - centre position
		renderer.setColor(1, 1, 0, .75f);
		tmp.set(tm.width/2, tm.height/2);
		tm.affine2.applyTo(tmp);
		renderer.line(tmp.x - crossSize, tmp.y, tmp.x + crossSize, tmp.y);
		renderer.line(tmp.x, tmp.y - crossSize, tmp.x, tmp.y + crossSize);
		// blue - origin
		renderer.setColor(0, 0, 1, .75f);
		tmp.set(tm.originX, tm.originY);
		tm.affine2.applyTo(tmp);
		renderer.line(tmp.x - crossSize, tmp.y, tmp.x + crossSize, tmp.y);
		renderer.line(tmp.x, tmp.y - crossSize, tmp.x, tmp.y + crossSize);;
	}

	@Override public void end () {
		renderer.end();
	}
}
