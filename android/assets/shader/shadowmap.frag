#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP 
#endif

#define PI 3.14

varying vec2 v_texCoords;

uniform sampler2D u_texture;
uniform vec2 u_resolution;

const float THRESHOLD = 0.75;

void main() {
	float distance = 1.0;

	for (float y = 0.0; y < u_resolution.y; y += 1.0) {		
        vec2 norm = vec2(v_texCoords.s, y / u_resolution.y) * 2.0 - 1.0;
		
        float theta = PI*1.5 + norm.x * PI; 
        float r = (1.0 + norm.y) * 0.5;

        vec2 coord = vec2(-r * sin(theta), -r * cos(theta)) / 2.0 + 0.5;

        vec4 data = texture2D(u_texture, coord);

        float dst = y / u_resolution.y;

        float caster = data.a;
        if (caster > THRESHOLD) {
            distance = min(distance, dst);
        }
	}

	gl_FragColor = vec4(vec3(distance), 1.0);
}