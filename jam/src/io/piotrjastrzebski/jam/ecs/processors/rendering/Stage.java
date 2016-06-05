package io.piotrjastrzebski.jam.ecs.processors.rendering;

import com.artemis.*;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.piotrjastrzebski.jam.ecs.GlobalSettings;
import io.piotrjastrzebski.jam.ecs.components.rendering.Actor;
import io.piotrjastrzebski.jam.ecs.processors.gameplay.InputSystem;

/**
 * Simple renderer
 *
 * Created by EvilEntity on 28/01/2016.
 */
public class Stage extends BaseEntitySystem implements InputSystem {
	public static final int INPUT_PRIORITY = 100;
	private static final String TAG = Stage.class.getSimpleName();
	private @Wire(name = GlobalSettings.WIRE_GUI_VP) ScreenViewport vp;
	private @Wire SpriteBatch batch;
	private ComponentMapper<Actor> mActor;
	private Array<com.badlogic.gdx.scenes.scene2d.Actor> queue = new Array<>();
	private com.badlogic.gdx.scenes.scene2d.Stage stage;
	private Table root;

	public Stage () {
		super(Aspect.all(Actor.class));
	}

	@Override protected void initialize () {
		stage = new com.badlogic.gdx.scenes.scene2d.Stage(vp, batch);
		root = new Table();
		root.setFillParent(true);
		stage.addActor(root);

		for (com.badlogic.gdx.scenes.scene2d.Actor actor : queue) {
			stage.addActor(actor);
		}
		queue.clear();
	}

	@Override public void inserted (int entityId) {
		Actor actor = mActor.get(entityId);
		root.addActor(actor.actual);
	}

	@Override protected void processSystem () {
		stage.act(world.delta);
		stage.draw();
	}

	@Override public void removed (int entityId) {
		Actor actor = mActor.get(entityId);
		root.removeActor(actor.actual);
	}

	public Table getRoot () {
		return root;
	}

	public com.badlogic.gdx.scenes.scene2d.Stage getStage () {
		return stage;
	}

	/**
	 * Add actor to stage
	 * @param actor to add
	 */
	public void addActor (com.badlogic.gdx.scenes.scene2d.Actor actor) {
		if (stage == null) {
			queue.add(actor);
		} else {
			stage.addActor(actor);
		}
	}

	/**
	 * Remove actor from stage
	 * @param actor to remove
	 * @return if it was removed
	 */
	public boolean removeActor (com.badlogic.gdx.scenes.scene2d.Actor actor) {
		if (stage == null) {
			return queue.removeValue(actor, true);
		} else {
			return actor.remove();
		}
	}

	@Override public InputProcessor getProcessor () {
		return stage;
	}

	@Override public int getPriority () {
		return INPUT_PRIORITY;
	}
}
