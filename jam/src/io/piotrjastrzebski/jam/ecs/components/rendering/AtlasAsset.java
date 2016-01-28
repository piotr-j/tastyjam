package io.piotrjastrzebski.jam.ecs.components.rendering;

import com.artemis.PooledComponent;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class AtlasAsset extends PooledComponent {
	public TextureAtlas.AtlasRegion region;

	@Override protected void reset () {
		region = null;
	}
}
