/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yss1.lib_jm;

import com.jme3.material.Material;
import com.jme3.math.Vector2f;
import com.jme3.scene.Geometry;
import com.jme3.scene.VertexBuffer;
import java.nio.FloatBuffer;

/**
 *                                                                         D-X1Y2____C-X2Y2
 * @author yss                                                                  |   |
 * Содержит текстурные координаты (на большой карте в пикселях  A,B,C,D  A-X1Y1|___|B-X2Y1) и материал
 */
public class FaceState {
private RessKeeper rk;
String matKey;
Material material;
float [] texCoord;
Vector2f scaleTx;

public FaceState(RessKeeper rk)
{
 this.rk=rk;
 scaleTx=new Vector2f(1f,1f);
}

public FaceState init(String matKey, float... vTex)
{
    if (vTex.length!=8 && vTex.length!=16) throw new RuntimeException("FaceState.init incorrect Texture buffer size. Must be 8 or 16");
    this.matKey=matKey;
    texCoord=vTex.clone();
    material=rk.getRess().getMaterial(matKey);
    if (material==null) throw new RuntimeException("FaceState.init Material "+matKey+" not found.");
    return this;
}

public FaceState scaleX(float sx)
{
    scaleTx.x=sx;
    scaleTx.y=1f;
    return this;
}

public FaceState scaleY(float sy)
{
    scaleTx.x=1f;
    scaleTx.y=sy;
    return this;
}

public FaceState scale(float sx,float sy)
{
    scaleTx.x=sx;
    scaleTx.y=sy;
    return this;
}

public void applyTo(Geometry ge)
{
    if (texCoord==null || material==null || ge==null) throw new RuntimeException("FaceState.applyTo Coordinates or material not prepared");  
    ge.setMaterial(material);
    FloatBuffer fbuff = ge.getMesh().getFloatBuffer(VertexBuffer.Type.TexCoord);
    fbuff.put(texCoord);
    ge.getMesh().scaleTextureCoordinates(scaleTx);
}

public void applyMatTo(Geometry ge)
{
    if (texCoord==null || material==null || ge==null) throw new RuntimeException("FaceState.applyTo Coordinates or material not prepared");  
    ge.setMaterial(material);
    ge.getMesh().updateBound();
}


public float calcWidthForHeight(float h)
{
 float res;
 Vector2f v=rk.getRess().getTexDim4Material(matKey);
 return h * (texCoord[2] - texCoord[0]) /(texCoord[5] - texCoord[1])*v.x/v.y;    
}

}
