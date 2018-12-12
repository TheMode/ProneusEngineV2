#version 330 core
layout (location = 0) in vec2 pos;

uniform float depth;

void main() {

    gl_Position = vec4(pos, depth, 1);
}
