#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP
#endif

varying vec4 v_transPos;
varying vec2 v_texCoords;

uniform sampler2D u_texture;

void main() {
	vec4 diffuseColor = texture2D(u_texture, v_texCoords);

	gl_FragColor = diffuseColor;
}