#version 330 core
in vec2 TexCoord;

out vec4 FragColor;

uniform vec4 color;
uniform float color_intensity;

uniform sampler2D tex;


void main() {
    vec4 basecolor = texture(tex, TexCoord);

    // Alpha test
    if (basecolor.a == 0.0){
        discard;
    }
    // End alpha test

    basecolor += (color * color_intensity);

	FragColor = basecolor;
}
