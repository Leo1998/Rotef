#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP
#endif

#define PI 3.14


uniform int u_downScale;
uniform vec2 u_mapCoord;

uniform float u_sunIntensity;

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
	vec2 coord = u_mapCoord + (((v_transPos + 1.0) / 2.0) * 8.0);
	
	vec4 color = vec4(0, 0, 0, 1);
	
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

	gl_FragColor = color;
	gl_FragColor.a = 1.0;
}