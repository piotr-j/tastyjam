package io.piotrjastrzebski.jam.ecs.processors.rendering;

import com.artemis.*;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
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
	private static final String TAG = Stage.class.getSimpleName();
	private @Wire(name = GlobalSettings.WIRE_GUI_VP) ScreenViewport vp;
	private @Wire SpriteBatch batch;
	private ComponentMapper<Actor> mActor;
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

	@Override public InputProcessor getProcessor () {
		return stage;
	}

	@Override public int getPriority () {
		return 999;
	}
}
