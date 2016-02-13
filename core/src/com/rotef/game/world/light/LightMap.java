package com.rotef.game.world.light;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class LightMap {

	private int width;
	private int height;
	private FrameBuffer fb;
	private Mesh lightMapMesh;

	public LightMap(int width, int height) {
		init(width, height);
	}

	public void init(int width, int height) {
		if (fb != null) {
			fb.dispose();
		}
		if (lightMapMesh != null) {
			lightMapMesh.dispose();
		}

		this.width = width;
		this.height = height;

		this.fb = new FrameBuffer(Format.RGB888, width, height, false);
		fb.getColorBufferTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		fb.getColorBufferTexture().setWrap(TextureWrap.ClampToEdge, TextureWrap.ClampToEdge);

		this.lightMapMesh = createLightMapMesh();
	}

	public void render(ShaderProgram shader, int downScale, float xMap, float yMap, float wMap, float hMap, Color ambientLight, LightArray lights, float sunIntensity) {
		fb.begin();
		shader.begin();

		Gdx.gl.glClearColor(ambientLight.r, ambientLight.g, ambientLight.b, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_ONE, GL20.GL_ONE);

		shader.setUniformi("u_downScale", downScale);
		shader.setUniformf("u_mapCoord", xMap, yMap);
		shader.setUniformf("u_resolution", wMap, hMap);
		shader.setUniformf("u_sunIntensity", sunIntensity);

		for (int i = 0; i < lights.size; i++) {
			Light light = lights.get(i);

			shader.setUniformf("u_lightPosition", light.getX(), light.getY());
			shader.setUniformf("u_lightDist", light.getDistance());
			shader.setUniformf("u_lightColor", light.getColor());

			ShadowMap shadowMap = light.getShadowMap();
			shadowMap.getShadowMapFB().getColorBufferTexture().bind(0);
			shader.setUniformi("u_shadowMap", 0);

			lightMapMesh.render(shader, GL20.GL_TRIANGLE_FAN);
		}

		shader.end();
		fb.end();
	}

	public void dispose() {
		if (fb != null) {
			fb.dispose();
		}
		if (lightMapMesh != null) {
			lightMapMesh.dispose();
		}
	}

	public FrameBuffer getFrameBuffer() {
		return fb;
	}

	public Texture getTexture() {
		return fb.getColorBufferTexture();
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	private Mesh createLightMapMesh() {
		float[] verts = new float[VERT_SIZE];
		// vertex coord
		verts[X1] = -1;
		verts[Y1] = -1;

		verts[X2] = 1;
		verts[Y2] = -1;

		verts[X3] = 1;
		verts[Y3] = 1;

		verts[X4] = -1;
		verts[Y4] = 1;

		// tex coords
		verts[U1] = 0f;
		verts[V1] = 0f;

		verts[U2] = 1f;
		verts[V2] = 0f;

		verts[U3] = 1f;
		verts[V3] = 1f;

		verts[U4] = 0f;
		verts[V4] = 1f;

		Mesh tmpMesh = new Mesh(true, 4, 0, new VertexAttribute(Usage.Position, 2, "a_position"), new VertexAttribute(Usage.TextureCoordinates, 2, "a_texCoord0"));

		tmpMesh.setVertices(verts);
		return tmpMesh;

	}

	static public final int VERT_SIZE = 16;
	static public final int X1 = 0;
	static public final int Y1 = 1;
	static public final int U1 = 2;
	static public final int V1 = 3;
	static public final int X2 = 4;
	static public final int Y2 = 5;
	static public final int U2 = 6;
	static public final int V2 = 7;
	static public final int X3 = 8;
	static public final int Y3 = 9;
	static public final int U3 = 10;
	static public final int V3 = 11;
	static public final int X4 = 12;
	static public final int Y4 = 13;
	static public final int U4 = 14;
	static public final int V4 = 15;

}
