package com.rotef.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.rotef.game.Game;
import com.rotef.game.input.WorldInput;
import com.rotef.game.renderer.BatchTechnique;
import com.rotef.game.renderer.WorldRenderTechnique;
import com.rotef.game.renderer.WorldRenderer;
import com.rotef.game.renderer.WorldViewport;
import com.rotef.game.util.Debug;
import com.rotef.game.world.World;
import com.rotef.game.world.entity.Player;
import com.rotef.game.world.physics.PhysicsManager;

public class WorldScreen extends BaseScreen implements WorldRenderer {

	private OrthographicCamera worldCamera;
	private WorldRenderTechnique renderTechnique;

	private World world;
	private WorldViewport viewport = new WorldViewport();
	private WorldInput input;

	public WorldScreen(World world) {
		this.world = world;
	}

	@Override
	protected void makeContent() {
	}

	@Override
	public void show() {
		super.show();

		this.renderTechnique = chooseRenderTechnique();

		this.input = new WorldInput(this);

		InputMultiplexer multiplexer = new InputMultiplexer(this.ui, new GestureDetector(input), input);
		Gdx.input.setInputProcessor(multiplexer);

		this.worldCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	@Override
	public void hide() {
		super.hide();

		disposeWorld();
	}

	@Override
	public void pause() {
		super.pause();

		saveWorld();
	}

	@Override
	public void dispose() {
		super.dispose();

		disposeWorld();

		renderTechnique.dispose();
	}

	private void disposeWorld() {
		if (world != null) {
			world.dispose(false);

			world = null;
		}
	}

	private void saveWorld() {
		if (world != null) {
			world.requestSave();
		}
	}

	@Override
	public void renderWorld(float delta) {
		updateWorldCamera();

		if (world != null) {
			world.update(delta);

			renderWorld();

			world.updateAfterRender(delta);
		}
	}

	@Override
	public void renderAdditionalUI(float delta) {
		if (Game.config.isDebug()) {
			Debug.renderDebug(batch, world);
		}
		input.render(delta, this);

	}

	private void renderWorld() {
		renderTechnique.setView(worldCamera);

		renderTechnique.renderWorld(world, viewport);

		// world.getPhysicsManager().renderDebug(worldCamera);
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);

		worldCamera.setToOrtho(false, width, height);
	}

	private void updateWorldCamera() {
		Player player = world.getPlayer();
		worldCamera.position.x = player.getX() * PhysicsManager.PPM;
		worldCamera.position.y = player.getY() * PhysicsManager.PPM;

		worldCamera.update();
	}

	private WorldRenderTechnique chooseRenderTechnique() {
		return new BatchTechnique();
	}

	@Override
	public OrthographicCamera getWorldCamera() {
		return worldCamera;
	}

	private void updateViewport() {
		float width = worldCamera.viewportWidth * worldCamera.zoom;
		float height = worldCamera.viewportHeight * worldCamera.zoom;
		viewport.set(worldCamera.position.x - width / 2, worldCamera.position.y - height / 2, width, height);
	}

	@Override
	public WorldViewport getViewport() {
		updateViewport();

		return viewport;
	}

	@Override
	public WorldInput getInput() {
		return input;
	}

}
