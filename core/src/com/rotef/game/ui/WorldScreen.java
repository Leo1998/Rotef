package com.rotef.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Array;
import com.rotef.game.Game;
import com.rotef.game.chat.ChatView;
import com.rotef.game.input.WorldInput;
import com.rotef.game.renderer.WorldRenderer;
import com.rotef.game.renderer.WorldViewport;
import com.rotef.game.world.World;
import com.rotef.game.world.entity.Player;
import com.rotef.game.world.physics.PhysicsManager;

public class WorldScreen extends BaseScreen {

	private OrthographicCamera worldCamera;
	private WorldRenderer renderer;

	private World world;
	private WorldViewport viewport = new WorldViewport();
	private WorldInput input;

	private Array<WorldView> views = new Array<>();

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

	public World getWorld() {
		return world;
	}

	public void disposeWorld() {
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
		update(delta);

		if (world != null) {
			world.update(delta);

			renderer.setView(worldCamera, viewport);

			renderer.renderWorld();

			if (Game.config.isDebug()) {
				world.getPhysicsManager().renderDebug(worldCamera);
			}

			world.updateAfterRender(delta);
		}
	}

	private void update(float delta) {
		updateWorldCamera();
		updateViewport();

		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
			Game.game.setScreen(new MainMenuScreen());
		}
	}

	@Override
	public void renderAdditionalUI(float delta) {
		// if (Game.config.isDebug() && world != null) {
		// Debug.renderDebug(batch, world);
		// }
		// TODO
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);

		worldCamera.setToOrtho(false, width, height);
		if (world != null) {
			world.getLightManager().init(width, height);
		}
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
		return viewport;
	}

	public WorldInput getInput() {
		return input;
	}

	private void showWorldView(WorldView view) {
		view.makeContent(this.ui);

		this.rootTable.addActor(view.getRootTable());
		view.getRootTable().getColor().a = 0.0f;
		view.getRootTable().addAction(Actions.fadeIn(0.4f));

		this.views.add(view);
	}

	public void showChatView() {
		this.showWorldView(new ChatView(this, world.getChatManager()));
	}

	public Array<WorldView> getViews() {
		return views;
	}

	public void closeView(WorldView worldView) {
		if (views.contains(worldView, true)) {
			views.removeValue(worldView, true);

			worldView.dispose();
			worldView.getRootTable().addAction(Actions.sequence(Actions.fadeOut(0.4f), Actions.removeActor()));
		}
	}

}
