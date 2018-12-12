#version 330 core

out vec4 fragColor;

uniform vec2 u_resolution;
uniform vec4 backgroundColor;


void main() {
    float ratio = u_resolution.x/u_resolution.y;

    vec2 st = gl_FragCoord.xy/u_resolution;
    st.x *= ratio;

    vec2 pos = vec2(0.5, 0.5);
    pos.x *= ratio;

    vec4 finalColor = backgroundColor;

    float dist = distance(pos, st);
    if(dist < 0.1){
        finalColor.w = 0;
    }else{
        //finalColor.w = dist;
    }

	fragColor = finalColor;
}
