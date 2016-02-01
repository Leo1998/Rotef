#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP
#endif

#define MAX_LIGHTS 64



uniform int u_resolution;
uniform vec2 u_mapCoord;

uniform LOWP vec4 u_ambientLight;
uniform float u_sunIntensity;

uniform vec2 u_lightPosition[MAX_LIGHTS];
uniform float u_lightDist[MAX_LIGHTS];
uniform LOWP vec4 u_lightColor[MAX_LIGHTS];
uniform int u_lightCount;


varying vec2 v_transPos;

void main() {
	vec2 coord = u_mapCoord + (((v_transPos + 1) / 2) * 8.0);
	
	vec4 color = u_ambientLight;
	
	for (int i = 0; i < u_lightCount; i++) {
		float dist = u_lightDist[i];
		
		float l = length(coord.xy - u_lightPosition[i]);
		float intensity = (dist - max(l, 0.01f)) / dist;

		if (intensity > 0.03) {
			color += u_lightColor[i] * intensity;
		}
	}

	gl_FragColor = color;
	gl_FragColor.a = 1.0;
}