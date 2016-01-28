package io.piotrjastrzebski.jam.ecs.components.rendering;

import com.artemis.PooledComponent;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class AtlasAssetDef extends PooledComponent {
	public String atlas;
	public String path;

	@Override protected void reset () {
		atlas = null;
		path = null;
	}
}
