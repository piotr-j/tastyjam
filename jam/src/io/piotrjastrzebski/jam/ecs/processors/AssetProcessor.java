package io.piotrjastrzebski.jam.ecs.processors;

import com.artemis.*;
import com.artemis.annotations.Wire;
import com.artemis.systems.IteratingSystem;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import io.piotrjastrzebski.jam.ecs.components.rendering.AtlasAsset;
import io.piotrjastrzebski.jam.ecs.components.rendering.AtlasAssetDef;
import io.piotrjastrzebski.jam.utils.Assets;

/**
 * Adds/removes assets
 *
 * Created by EvilEntity on 28/01/2016.
 */
public class AssetProcessor extends BaseEntitySystem {
	private static final String TAG = AssetProcessor.class.getSimpleName();

	private ComponentMapper<AtlasAsset> mAtlasAsset;
	private ComponentMapper<AtlasAssetDef> mAtlasAssetDef;
	private @Wire Assets assets;

	public AssetProcessor () {
		super(Aspect.all(AtlasAssetDef.class));
	}

	@Override protected void initialize () {
		super.initialize();
		setEnabled(false);
	}

	@Override protected void inserted (int entityId) {
		AtlasAssetDef assetDef = mAtlasAssetDef.get(entityId);
		Assets.TextureAtlasCache cache = assets.getAtlasCache(assetDef.atlas);
		if (cache == null) {
			Gdx.app.error(TAG, "Invalid atlas name " + assetDef.atlas + " in entity " + entityId + "!");
			return;
		}
		TextureAtlas.AtlasRegion region = cache.getAtlasRegion(assetDef.path);
		if (region == null) {
			Gdx.app.error(TAG, "Invalid asset name " + assetDef.path + " in entity " + entityId + "!");
			return;
		}
		AtlasAsset asset = mAtlasAsset.create(entityId);
		asset.region = region;
	}

	@Override protected void removed (int entityId) {
		AtlasAsset asset = mAtlasAsset.get(entityId);
		// do whatever is needed
	}

	@Override protected void processSystem () {}
}
