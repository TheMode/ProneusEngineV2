#version 330 core
in vec2 TexCoord;
out vec4 FragColor;

uniform sampler2D text;
uniform vec4 color;

void main(){
    vec4 sampled = vec4(1.0, 1.0, 1.0, texture(text, TexCoord).r);
    vec4 finalColor = sampled * color;

    // Alpha test
    if (finalColor.a == 0.0){
        discard;
    }
    // End test

    FragColor = finalColor;
}