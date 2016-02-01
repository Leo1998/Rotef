package com.rotef.game.world.light;

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
		init(false, width, height);
	}

	public void init(boolean resize, int width, int height) {
		if (fb != null) {
			fb.dispose();
		}

		this.width = width;
		this.height = height;

		this.fb = new FrameBuffer(Format.RGB888, width, height, false);
		fb.getColorBufferTexture().setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		fb.getColorBufferTexture().setWrap(TextureWrap.ClampToEdge, TextureWrap.ClampToEdge);

		this.lightMapMesh = createLightMapMesh();
	}

	public void render(ShaderProgram shader, int resolution, int mapX, int mapY, Color ambientLight, LightArray lights, float sunIntensity) {
		fb.begin();
		shader.begin();

		shader.setUniformi("u_resolution", resolution);
		shader.setUniformf("u_mapCoord", mapX, mapY);
		shader.setUniformf("u_ambientLight", ambientLight);
		shader.setUniformf("u_sunIntensity", sunIntensity);

		final int MAX_LIGHTS = 64;
		float[] positionBuffer = new float[MAX_LIGHTS * 2];
		float[] distBuffer = new float[MAX_LIGHTS * 1];
		float[] colorBuffer = new float[MAX_LIGHTS * 4];
		
		int i = 0;
		while (i < MAX_LIGHTS && i < lights.size) {
			Light light = lights.get(i);

			positionBuffer[i * 2 + 0] = light.getX();
			positionBuffer[i * 2 + 1] = light.getY();
			
			distBuffer[i] = light.getDistance();
			
			colorBuffer[i * 4 + 0] = light.getColor().r;
			colorBuffer[i * 4 + 1] = light.getColor().g;
			colorBuffer[i * 4 + 2] = light.getColor().b;
			colorBuffer[i * 4 + 3] = light.getColor().a;
			
			i++;
		}

		shader.setUniform2fv("u_lightPosition[0]", positionBuffer, 0, positionBuffer.length);
		shader.setUniform1fv("u_lightDist[0]", distBuffer, 0, distBuffer.length);
		shader.setUniform4fv("u_lightColor[0]", colorBuffer, 0, colorBuffer.length);
		
		shader.setUniformi("u_lightCount", i + 1);

		lightMapMesh.render(shader, GL20.GL_TRIANGLE_FAN);

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
		float[] verts = new float[8];

		verts[0] = -1;
		verts[1] = -1;

		verts[2] = 1;
		verts[3] = -1;

		verts[4] = 1;
		verts[5] = 1;

		verts[6] = -1;
		verts[7] = 1;

		Mesh tmpMesh = new Mesh(true, 4, 0, new VertexAttribute(Usage.Position, 2, "a_position"));

		tmpMesh.setVertices(verts);
		return tmpMesh;

	}

}
