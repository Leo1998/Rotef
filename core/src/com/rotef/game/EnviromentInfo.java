package com.rotef.game;

import com.badlogic.gdx.graphics.GL20;

public class EnviromentInfo {

	public final String userName;
	public final String osName;
	public final String gl_version;
	public final String gl_vendor;
	public final String gl_renderer;
	public final long maxMemory;
	public final int availableProcessors;

	public EnviromentInfo(GL20 gl) {
		this.userName = System.getProperty("user.name");
		this.osName = System.getProperty("os.name");
		this.gl_version = gl.glGetString(GL20.GL_VERSION);
		this.gl_vendor = gl.glGetString(GL20.GL_VENDOR);
		this.gl_renderer = gl.glGetString(GL20.GL_RENDERER);
		this.maxMemory = Runtime.getRuntime().maxMemory();
		this.availableProcessors = Runtime.getRuntime().availableProcessors();
	}

	@Override
	public String toString() {
		return "EnviromentInfo [userName=" + userName + "\n osName=" + osName + "\n gl_version=" + gl_version + "\n gl_vendor=" + gl_vendor + "\n gl_renderer=" + gl_renderer + "\n maxMemory=" + maxMemory + "\n availableProcessors=" + availableProcessors + "]";
	}

}
