package io.piotrjastrzebski.jam.ecs.components;

import com.artemis.Component;
import com.artemis.annotations.EntityId;
import com.artemis.annotations.PooledWeaver;
import com.artemis.utils.IntBag;

/**
 * Created by PiotrJ on 28/06/16.
 */
@PooledWeaver
public class InheritTransform extends Component {
	@EntityId
	public int from = -1;
	@EntityId
	public IntBag children = new IntBag();

	public InheritTransform () {}
}
