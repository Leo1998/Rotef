package com.rotef.game.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class ShaderUtils {

	private ShaderUtils() {
	}

	public static void validateShader(ShaderProgram shader) {
		String log = shader.getLog();

		if (!shader.isCompiled()) {
			throw new RuntimeException("Shader not compiled successfully: " + log);
		}

		if (log != null) {
			Gdx.app.debug("ShaderUtils", "Shader compiled with warning: " + log);
		}
	}

}
