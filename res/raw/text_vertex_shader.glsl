uniform mat4   u_mvpMatrix;
uniform vec4 a_Color;

attribute vec4 a_position;
attribute vec2 a_texCoord;

varying vec4 v_Color;
varying vec2 v_texCoord;

void main()
{
	gl_Position = u_mvpMatrix * a_position;
	v_texCoord = a_texCoord;
	v_Color = a_Color;
}                                                 