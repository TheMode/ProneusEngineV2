#version 330 core
layout (location = 0) in vec4 data;

out vec2 TexCoord;

uniform mat4 mvp;

void main()
{
    gl_Position = mvp * vec4(data.xy, 0.0, 1.0);
    TexCoord = data.zw;
}