package com.rotef.game;

import com.badlogic.gdx.graphics.GL20;

public class GPUInfo {

	final String gpu_gl_version;
	final String gpu_vendor;
	final String gpu_renderer;

	public GPUInfo(GL20 gl) {
		gpu_gl_version = gl.glGetString(GL20.GL_VERSION);
		gpu_vendor = gl.glGetString(GL20.GL_VENDOR);
		gpu_renderer = gl.glGetString(GL20.GL_RENDERER);
	}

	@Override
	public String toString() {
		return "GPUInfo [gpu_gl_version=" + gpu_gl_version + ", gpu_vendor=" + gpu_vendor + ", gpu_renderer=" + gpu_renderer + "]";
	}

}
