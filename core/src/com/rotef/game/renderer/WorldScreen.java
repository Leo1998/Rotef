package com.rotef.game.renderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.rotef.game.Game;
import com.rotef.game.input.WorldInput;
import com.rotef.game.ui.BaseScreen;
import com.rotef.game.util.Debug;
import com.rotef.game.world.World;
import com.rotef.game.world.entity.Player;
import com.rotef.game.world.physics.PhysicsManager;

public class WorldScreen extends BaseScreen {

	private OrthographicCamera worldCamera;
	private WorldRenderer renderer;

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

		this.renderer = new WorldRenderer(world);

		this.input = new WorldInput(this);

		InputMultiplexer multiplexer = new InputMultiplexer(this.ui, input);
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

		renderer.dispose();
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
	}

	private void renderWorld() {
		renderer.setView(worldCamera, viewport);

		renderer.renderWorld();
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

	public OrthographicCamera getWorldCamera() {
		return worldCamera;
	}

	private void updateViewport() {
		float width = worldCamera.viewportWidth * worldCamera.zoom;
		float height = worldCamera.viewportHeight * worldCamera.zoom;
		viewport.set(worldCamera.position.x - width / 2, worldCamera.position.y - height / 2, width, height);
	}

	public WorldViewport getViewport() {
		updateViewport();

		return viewport;
	}

	public WorldInput getInput() {
		return input;
	}

}
