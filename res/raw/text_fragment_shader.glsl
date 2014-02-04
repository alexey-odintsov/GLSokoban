precision mediump float;
varying vec2 v_texCoord;
varying vec4 v_Color;
uniform sampler2D s_texture;

void main()
{
  gl_FragColor = (v_Color * texture2D( s_texture, v_texCoord ));
//  gl_FragColor = (texture2D( s_texture, v_texCoord ));
}