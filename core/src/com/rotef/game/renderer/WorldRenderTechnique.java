package com.rotef.game.renderer;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import com.rotef.game.world.World;

public interface WorldRenderTechnique {

	public void setView(OrthographicCamera camera);

	public void setView(Matrix4 projectionMatrix, float viewboundsX, float viewboundsY, float viewboundsWidth, float viewboundsHeight);

	public void renderWorld(World world, WorldViewport viewport);

	public void dispose();

}
