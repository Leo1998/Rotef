package com.rotef.game.renderer;

import static com.badlogic.gdx.graphics.g2d.Batch.C1;
import static com.badlogic.gdx.graphics.g2d.Batch.C2;
import static com.badlogic.gdx.graphics.g2d.Batch.C3;
import static com.badlogic.gdx.graphics.g2d.Batch.C4;
import static com.badlogic.gdx.graphics.g2d.Batch.U1;
import static com.badlogic.gdx.graphics.g2d.Batch.U2;
import static com.badlogic.gdx.graphics.g2d.Batch.U3;
import static com.badlogic.gdx.graphics.g2d.Batch.U4;
import static com.badlogic.gdx.graphics.g2d.Batch.V1;
import static com.badlogic.gdx.graphics.g2d.Batch.V2;
import static com.badlogic.gdx.graphics.g2d.Batch.V3;
import static com.badlogic.gdx.graphics.g2d.Batch.V4;
import static com.badlogic.gdx.graphics.g2d.Batch.X1;
import static com.badlogic.gdx.graphics.g2d.Batch.X2;
import static com.badlogic.gdx.graphics.g2d.Batch.X3;
import static com.badlogic.gdx.graphics.g2d.Batch.X4;
import static com.badlogic.gdx.graphics.g2d.Batch.Y1;
import static com.badlogic.gdx.graphics.g2d.Batch.Y2;
import static com.badlogic.gdx.graphics.g2d.Batch.Y3;
import static com.badlogic.gdx.graphics.g2d.Batch.Y4;

import java.util.Collection;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.rotef.game.assets.Sprite;
import com.rotef.game.util.ShaderUtils;
import com.rotef.game.world.World;
import com.rotef.game.world.entity.Entity;
import com.rotef.game.world.entity.skin.EntitySkin;
import com.rotef.game.world.light.LightManager;
import com.rotef.game.world.physics.PhysicsManager;
import com.rotef.game.world.tile.Tile;

public class BatchTechnique implements WorldRenderTechnique {

	private static final int NUM_VERTICES = 20;

	private ShaderProgram objectShader;
	private ShaderProgram lightMapRenderShader;
	private SpriteBatch batch;

	private Rectangle viewBounds = new Rectangle();
	private Rectangle imageBounds = new Rectangle();

	private float vertices[] = new float[NUM_VERTICES];

	public BatchTechnique() {
		this.batch = new SpriteBatch();

		this.objectShader = new ShaderProgram(Gdx.files.internal("shader/object.vert"), Gdx.files.internal("shader/object.frag"));
		ShaderUtils.validateShader(objectShader);
		this.lightMapRenderShader = new ShaderProgram(Gdx.files.internal("shader/lightmap_draw.vert"), Gdx.files.internal("shader/lightmap_draw.frag"));
		ShaderUtils.validateShader(lightMapRenderShader);
	}

	@Override
	public void setView(OrthographicCamera camera) {
		batch.setProjectionMatrix(camera.combined);
		float width = camera.viewportWidth * camera.zoom;
		float height = camera.viewportHeight * camera.zoom;
		viewBounds.set(camera.position.x - width / 2, camera.position.y - height / 2, width, height);
	}

	@Override
	public void setView(Matrix4 projection, float x, float y, float width, float height) {
		batch.setProjectionMatrix(projection);
		viewBounds.set(x, y, width, height);
	}

	public SpriteBatch getBatch() {
		return batch;
	}

	public Rectangle getViewBounds() {
		return viewBounds;
	}

	private void renderObject(Sprite sprite, float x, float y, float width, float height) {
		final Color batchColor = batch.getColor();
		final float color = Color.toFloatBits(batchColor.r, batchColor.g, batchColor.b, batchColor.a);

		final float[] vertices = this.vertices;

		TextureRegion region = sprite.getTex();

		final float x1 = x;
		final float y1 = y;
		final float x2 = x1 + width;
		final float y2 = y1 + height;

		imageBounds.set(x1, y1, x2 - x1, y2 - y1);

		if (viewBounds.contains(imageBounds) || viewBounds.overlaps(imageBounds)) {
			final float u1 = region.getU();
			final float v1 = region.getV2();
			final float u2 = region.getU2();
			final float v2 = region.getV();

			vertices[X1] = x1;
			vertices[Y1] = y1;
			vertices[C1] = color;
			vertices[U1] = u1;
			vertices[V1] = v1;

			vertices[X2] = x1;
			vertices[Y2] = y2;
			vertices[C2] = color;
			vertices[U2] = u1;
			vertices[V2] = v2;

			vertices[X3] = x2;
			vertices[Y3] = y2;
			vertices[C3] = color;
			vertices[U3] = u2;
			vertices[V3] = v2;

			vertices[X4] = x2;
			vertices[Y4] = y1;
			vertices[C4] = color;
			vertices[U4] = u2;
			vertices[V4] = v1;

			batch.setColor(sprite.getTint());

			batch.draw(region.getTexture(), vertices, 0, NUM_VERTICES);
		}
	}

	@Override
	public void renderWorld(World world, WorldViewport viewport) {
		{
			batch.setShader(objectShader);
			batch.begin();
			batch.enableBlending();
			batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

			float x0 = viewport.getX();
			float y0 = viewport.getY();
			float x1 = x0 + viewport.getWidth();
			float y1 = y0 + viewport.getHeight();
			int tileX0 = (int) (x0 / Tile.TILE_SIZE);
			int tileY0 = (int) (y0 / Tile.TILE_SIZE);
			int tileX1 = (int) (x1 / Tile.TILE_SIZE) + 1;
			int tileY1 = (int) (y1 / Tile.TILE_SIZE) + 1;

			for (int xt = tileX0; xt < tileX1; xt++) {
				for (int yt = tileY0; yt < tileY1; yt++) {
					Tile tile = world.getTile(xt, yt, true);

					if (tile != null) {
						if (tile.hasSprite()) {
							int x = xt * Tile.TILE_SIZE;
							int y = yt * Tile.TILE_SIZE;
							renderObject(tile.getSprite().getSprite(), x, y, Tile.TILE_SIZE, Tile.TILE_SIZE);
						}
					}
				}
			}
			Collection<Entity> entities = world.getEntityManager().getEntities();
			for (Entity e : entities) {

				EntitySkin skin = e.getSkin();
				if (skin != null) {
					Sprite sprite = skin.getSprite();

					float x = (e.getX() - e.getWidth() / 2) * PhysicsManager.PPM;
					float y = (e.getY() - e.getHeight() / 2) * PhysicsManager.PPM;
					float w = e.getWidth() * PhysicsManager.PPM;
					float h = e.getHeight() * PhysicsManager.PPM;

					if (skin.mirrorX()) {
						x += w;
						w *= -1;
					}
					if (skin.mirrorY()) {
						y += h;
						h *= -1;
					}

					renderObject(sprite, x, y, w, h);
				}
			}

			batch.end();
		}

		{
			batch.setShader(lightMapRenderShader);
			batch.begin();
			batch.enableBlending();
			batch.setBlendFunction(GL20.GL_DST_COLOR, GL20.GL_ZERO);

			LightManager lightManager = world.getLightManager();
			lightManager.renderLightMaps(viewport, batch);

			batch.end();
		}
	}

	@Override
	public void dispose() {
		batch.dispose();
	}

}
