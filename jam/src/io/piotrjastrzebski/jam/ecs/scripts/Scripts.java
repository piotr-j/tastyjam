package io.piotrjastrzebski.jam.ecs.scripts;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.PooledComponent;
import com.artemis.World;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

/**
 * Component based "scripts"
 *
 * Created by PiotrJ on 10/06/16.
 */
public class Scripts extends IteratingSystem {
	protected ComponentMapper<ScriptComponent> mScript;

	public Scripts () {
		super(Aspect.all(ScriptComponent.class));
	}

	@Override protected void inserted (int entityId) {
		ScriptComponent sc = mScript.get(entityId);
		sc.scripts.addAll(sc.add);
		sc.scripts.removeAll(sc.remove, true);
		for (Script script : sc.scripts) {
			world.inject(script, false);
			script.world = world;
			script.inserted(entityId);
		}
	}

	@Override protected void process (int entityId) {
		ScriptComponent sc = mScript.get(entityId);
		// add pending scripts
		for (Script script : sc.add) {
			world.inject(script, false);
			script.world = world;
			script.inserted(entityId);
			sc.scripts.add(script);
		}
		// removed pending scripts
		sc.add.clear();
		for (Script script : sc.remove) {
			if (sc.scripts.removeValue(script, true)) {
				script.removed(entityId);
			}
		}
		sc.remove.clear();
		// process active scripts
		Iterator<Script> it = sc.scripts.iterator();
		while (it.hasNext()) {
			Script next = it.next();
			next.process(entityId);
			if (next.remove) {
				it.remove();
				next.removed(entityId);
			}
		}
	}

	@Override protected void removed (int entityId) {
		ScriptComponent sc = mScript.get(entityId);
		for (Script script : sc.scripts) {
			script.removed(entityId);
		}
		sc.scripts.clear();
	}

	public static class ScriptComponent extends PooledComponent {
		// active scripts
		public Array<Script> scripts = new Array<>();
		// scripts that will be added on next process
		public Array<Script> add = new Array<>();
		// scripts that will be removed on next process
		public Array<Script> remove = new Array<>();

		public ScriptComponent add(Script script) {
			add.add(script);
			return this;
		}

		public ScriptComponent remove(Script script) {
			remove.add(script);
			return this;
		}

		@Override protected void reset () {
			scripts.clear();
			add.clear();
			remove.clear();
		}
	}

	public static class Script {
		protected World world;
		// set this flag to true, to remove after next process
		protected boolean remove;
		public void inserted(int entityId) {

		}

		public void process(int entityId) {

		}

		public void removed(int entityId) {

		}
	}
}
