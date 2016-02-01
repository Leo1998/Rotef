attribute vec4 a_position;

varying vec2 v_transPos;

void main() {
	v_transPos = a_position.xy;

	gl_Position = a_position;
}