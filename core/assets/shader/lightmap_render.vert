attribute vec4 a_position;
attribute vec2 a_texCoord0;

uniform mat4 u_projTrans;

varying vec4 v_transPos;
varying vec2 v_texCoords;

void main() {
	v_transPos = u_projTrans * a_position;
	v_texCoords = a_texCoord0;

	gl_Position =  v_transPos;
}