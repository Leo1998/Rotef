package com.rotef.game.world.light;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.rotef.game.world.physics.PhysicsManager;

public class ShadowMap {

	private Light light;

	private FrameBuffer occludersFB;
	private FrameBuffer shadowMapFB;

	private TextureRegion occludersDebug;
	private TextureRegion shadowMapDebug;

	private OrthographicCamera cam;

	public ShadowMap(Light light) {
		this.light = light;

		init();
	}

	private void init() {
		if (occludersFB != null) {
			occludersFB.dispose();
		}
		if (shadowMapFB != null) {
			shadowMapFB.dispose();
		}

		int d = (int) (light.getDistance() * 2 * PhysicsManager.PPM);
		this.occludersFB = new FrameBuffer(Format.RGBA8888, d, d, false);

		occludersDebug = new TextureRegion(occludersFB.getColorBufferTexture());
		occludersDebug.flip(false, true);

		this.shadowMapFB = new FrameBuffer(Format.RGBA8888, 360, 1, false);
		shadowMapFB.getColorBufferTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		shadowMapFB.getColorBufferTexture().setWrap(TextureWrap.Repeat, TextureWrap.Repeat);

		shadowMapDebug = new TextureRegion(shadowMapFB.getColorBufferTexture());
		shadowMapDebug.flip(false, true);

		this.cam = new OrthographicCamera();
	}

	public void begin() {
		cam.setToOrtho(false, occludersFB.getWidth(), occludersFB.getHeight());
		cam.translate((light.getX() - light.getDistance()) * PhysicsManager.PPM, (light.getY() - light.getDistance()) * PhysicsManager.PPM);
		cam.update();

		occludersFB.begin();

		Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}

	public void end(SpriteBatch batch, ShaderProgram shadowMapShader) {
		occludersFB.end();

		//STEP 2:
		
		shadowMapFB.begin();

		Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		cam.setToOrtho(false, shadowMapFB.getWidth(), shadowMapFB.getHeight());

		batch.setProjectionMatrix(cam.combined);
		batch.setShader(shadowMapShader);
		batch.begin();

		float d = light.getDistance() * 2 * PhysicsManager.PPM;
		shadowMapShader.setUniformf("u_resolution", d, d);

		batch.draw(occludersFB.getColorBufferTexture(), 0, 0, shadowMapFB.getWidth(), shadowMapFB.getHeight());

		batch.end();

		shadowMapFB.end();
	}

	public void dispose() {
		if (occludersFB != null) {
			occludersFB.dispose();
		}
		if (shadowMapFB != null) {
			shadowMapFB.dispose();
		}
	}

	public Light getLight() {
		return light;
	}

	public FrameBuffer getOccludersFB() {
		return occludersFB;
	}

	public FrameBuffer getShadowMapFB() {
		return shadowMapFB;
	}

	public TextureRegion getOccludersDebug() {
		return occludersDebug;
	}

	public TextureRegion getShadowMapDebug() {
		return shadowMapDebug;
	}

	public OrthographicCamera getCam() {
		return cam;
	}

}
