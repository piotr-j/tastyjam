package io.piotrjastrzebski.jam.ecs.processors.rendering;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.piotrjastrzebski.jam.ecs.Globals;
import io.piotrjastrzebski.jam.ecs.components.Transform;
import io.piotrjastrzebski.jam.ecs.components.rendering.AtlasAsset;
import io.piotrjastrzebski.jam.ecs.components.rendering.Tint;

/**
 * Simple renderer
 *
 * Created by EvilEntity on 28/01/2016.
 */
public class RendererAffine2 extends IteratingSystem {
	private static final String TAG = RendererAffine2.class.getSimpleName();
	private @Wire(name = Globals.WIRE_GAME_CAM) OrthographicCamera camera;
	private @Wire SpriteBatch batch;
	private ComponentMapper<Transform> mTransform;
	private ComponentMapper<AtlasAsset> mAtlasAsset;
	private ComponentMapper<Tint> mTint;

	public RendererAffine2 () {
		super(Aspect.all(Transform.class, AtlasAsset.class));
	}

	@Override protected void begin () {
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
	}

	@Override protected void process (int entityId) {
		Tint tint = mTint.get(entityId);
		if (tint != null) batch.setColor(tint.color);
		AtlasAsset asset = mAtlasAsset.get(entityId);
		Transform transform = mTransform.get(entityId);
		batch.draw(
			asset.region,
			transform.width, transform.height,
			transform.affine2
		);
		if (tint != null) batch.setColor(Color.WHITE);
	}

	@Override protected void end () {
		batch.end();
	}
}
