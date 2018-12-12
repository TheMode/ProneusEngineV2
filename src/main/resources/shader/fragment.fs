#version 330 core
in vec2 TexCoord;

out vec4 FragColor;

uniform vec4 color;
uniform sampler2D tex;


void main() {
    vec4 basecolor = texture(tex, TexCoord) * color;

    // Alpha test
    if (basecolor.a == 0.0){
        discard;
    }
    // End test

	FragColor = basecolor;
}
