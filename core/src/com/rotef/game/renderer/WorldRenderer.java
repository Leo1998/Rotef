package com.rotef.game.renderer;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.rotef.game.input.WorldInput;

public interface WorldRenderer {

	OrthographicCamera getWorldCamera();

	WorldInput getInput();

	WorldViewport getViewport();

}
