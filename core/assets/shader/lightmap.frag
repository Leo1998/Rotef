#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP
#endif

#define PI 3.14


uniform int u_downScale;
uniform vec2 u_mapCoord;
uniform vec2 u_resolution;

uniform float u_sunIntensity;
uniform float u_sunMapX;
uniform float u_sunMapW;
uniform float u_worldHeight;
uniform sampler2D u_sunMap;

uniform vec2 u_lightPosition;
uniform float u_lightDist;
uniform LOWP vec4 u_lightColor;

uniform sampler2D u_shadowMap;

varying vec2 v_transPos;
varying vec2 v_texCoords;

float calcShadowDist(vec2 norm) {
    float theta = atan(norm.y, norm.x);
    float r = length(norm); 
    float coord = (theta + PI) / (2.0 * PI);

    vec2 tc = vec2(coord, 0.0);

	float dist = texture2D(u_shadowMap, tc).r;

	return dist;
}

void main() {
	vec2 pos = ((v_transPos + 1.0) / 2.0);
	vec2 coord = u_mapCoord.xy + vec2(pos.xy * u_resolution.xy);

	vec4 color = vec4(0, 0, 0, 1);

	if (u_sunIntensity != -1) {
		float sunDist = 1.5;
	
		float x = (coord.x - u_sunMapX) / u_sunMapW;

		vec2 tc = vec2(x, 0.0);
		float invHeight = texture2D(u_sunMap, tc).r;
		float height = invHeight * u_worldHeight;
		
		float dif = height - coord.y;
		float intensity = 1f;
		if (dif >= 0) {
			intensity = (sunDist - dif) / sunDist;
		}

		color = vec4(vec3(u_sunIntensity * intensity), 1.0);
	} else {
		float dist = u_lightDist;
		float dist2 = dist / 3;

		vec2 v = coord.xy - u_lightPosition.xy;
		float l = length(v);
		float intensity = (dist - max(l, 0.01)) / dist;

		if (intensity > 0.03) {
			vec2 norm = vec2(v.x, -v.y);

			float shadowDist = dist * calcShadowDist(norm);

			float d = max(0.01, l - shadowDist);
			float shadowFactor = (dist2 - d) / dist2;

			color = u_lightColor * intensity * shadowFactor;
		}
	}

	gl_FragColor = color;
	gl_FragColor.a = 1.0;
}