/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yss1.lib_jm;

import java.util.List;
import java.util.Map;
import static com.jme3.font.BitmapFont.*;
import com.jme3.font.Rectangle;
import com.jme3.math.ColorRGBA;

/**
 *
 * @author ys
 */
public class InfoPlaneBuilder {
 private Map<Character,InfoPlane>  container;
 private RessKeeper rKeep;
 private List<FaceState> fsL;
 private float x,y,z;
 private float dx,dy;
 private int fontNo;
 private float fontSize;
 private float fontY;
 private float fontX;
 private Align fAlign;
 private ColorRGBA fColor;
 private Rectangle textRect;
 private Button3d target;
 
 private boolean makePict;
 private float pX,pY,pL,pH;
 private boolean pictTransp;
 private boolean pictSelected;
 private float pictPlaneH,pictPlaneW;
 private String pictMaterial;
 
 
 public InfoPlaneBuilder(RessKeeper rk)
 {
     rKeep=rk;
     fontX=0f;
     fontNo=0;
     dx=-1;
     textRect=null;
     fAlign=Align.Center;
     target=null;
     fsL=null;
     makePict=false;
 }
 
   public InfoPlaneBuilder setPlace(float x,float y,float z)
    {
        this.x=x;
        this.y=y;
        this.z=z;
        
        return this;
    }
 
   public InfoPlaneBuilder setDim(float x,float y)
    {
        this.dx=x;
        this.dy=y;
        return this;
    }
   
   public InfoPlaneBuilder setFaces(List<FaceState> fsL)
    {
        this.fsL=fsL;
        return this;
    }
   
   public InfoPlaneBuilder setTarget(Button3d target)
    {
        this.target=target;
        return this;
    }
   
   public InfoPlaneBuilder setFontRectangle(Rectangle textRect)
    {
        this.textRect=textRect;
        return this;
    }
   
   public InfoPlaneBuilder setFontNo(int fontNo)
    {
        this.fontNo=fontNo;
        return this;
    }
   
   public InfoPlaneBuilder setPictureCoord(float X, float Y, float L, float H)
    {
        pX=X;
        pY=Y;
        pL=L;
        pH=H;
        return this;
    }
   
   public InfoPlaneBuilder setPictureBool(boolean sel, boolean trans)
    {
        pictSelected=sel;
        pictTransp=trans;
        return this;
    }
   
   public InfoPlaneBuilder setPictureMaterial(String pm)
    {
        pictMaterial=pm;
        return this;
    }
   
   public InfoPlaneBuilder setPicturePlane(float ppW,float ppH)
    {
        pictPlaneW=ppW;
        pictPlaneH=ppH;
        return this;
    }
   
   
   
   public InfoPlaneBuilder setMakePict(boolean mp)
    {
        makePict=mp;
        return this;
    }
   
   
   
   public InfoPlaneBuilder setFontColor(ColorRGBA fColor)
    {
        this.fColor=fColor;
        return this;
    }
   
   public InfoPlaneBuilder setFontAlign(Align fAlign)
    {
        this.fAlign=fAlign;
        return this;
    }
   
   public InfoPlaneBuilder setFontSize(float fontSize)
    {
        this.fontSize=fontSize;
        return this;
    }
   
   public InfoPlaneBuilder setFontY(float fontY)
    {
        this.fontY=fontY;
        return this;
    }
   
   public InfoPlaneBuilder setFontX(float fontX)
    {
        this.fontX=fontX;
        return this;
    }
   
   public InfoPlaneBuilder setDimX(float x)
    {
        this.dx=x;
        return this;
    }
      public InfoPlaneBuilder setDimY(float y)
    {
        this.dy=y;
        dx=-1;
        return this;
    }
       
  public InfoPlaneBuilder setContainer(Map<Character,InfoPlane> container)
    {
        this.container=container;
        return this;
    }
 
    public InfoPlane build(Character ch, String n) {
        if (fsL == null || fsL.isEmpty()) {
            throw new RuntimeException("PlaneBuilder.build Not enouth arguments");
        }
        InfoPlane ip;
        if (dx < 0) {
            ip = new InfoPlane(rKeep, n, dy, fsL);
        } else {
            ip = new InfoPlane(rKeep, n, dy, dx, fsL);
        }

        if (container != null) {
            container.put(ch, ip);
        }
        ip.setMyPlace(x, y, z);

        if (fontNo != 0) {
            if (textRect == null) {
                ip.initText1(fontNo, fColor, fontSize, fontY, fontX, fAlign);
            } else {
                ip.reinitTxt(1, fontNo, textRect, fColor, fAlign, fontSize, fontX, fontY);
            }
        }

        if (target != null) {
            ip.setTarget(target);
        }
        if (makePict)
        {
            if (pictMaterial!=null && !pictMaterial.isEmpty())
            {
              ip.setPictFromALL(pictPlaneW, pictPlaneH, pX, pY, pL, pH,pictMaterial);  
            }
            else
            {
            ip.setPictFromALL(pX, pY, pL, pH, pictSelected,pictTransp);
            }
        }
        return ip;
    }
}
