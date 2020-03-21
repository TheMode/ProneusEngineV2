#version 330 core
layout (location = 0) in vec2 pos;
layout (location = 1) in vec2 aTexCoord;

out vec2 TexCoord;

uniform vec2 camera;
uniform mat4 mvp;

void main() {

	TexCoord = aTexCoord;

    gl_Position = mvp * vec4(pos, 0, 1) + vec4(camera, 0, 0);
}
