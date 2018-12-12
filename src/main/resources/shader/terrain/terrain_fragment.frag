#version 330 core

in vec3 color;

out vec4 fragColor;

void main() {

	fragColor = vec4(0.2, 0.1, 0.6, 1);
	//fragColor = vec4(color, 1);
}
