precision mediump float;

uniform vec2 lightLocation;
uniform vec2 resolution;
uniform vec4 lightColor;

// Type
uniform int lightType;

// Ambient
uniform float ambientColor;
uniform float ambientIntensity;
uniform float ambientDecrease;
uniform float ambientShowRange;

void main() {
	if (lightType == 0) {
		vec2 position = lightLocation / resolution;
		vec2 distance = position - (gl_FragCoord.xy / resolution);
		float length = length(distance);
		float red = ambientColor - length * ambientDecrease;
		float green = ambientColor - length * ambientDecrease;
		float blue = ambientColor - length * ambientDecrease;
		gl_FragColor = vec4(red * ambientIntensity, green * ambientIntensity,
				blue * ambientIntensity, length*ambientShowRange) * lightColor;
	}
}
