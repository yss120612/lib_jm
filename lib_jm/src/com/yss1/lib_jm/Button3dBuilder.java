/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yss1.lib_jm;

import com.jme3.math.Vector2f;
import static com.yss1.lib_jm.Button3d.*;
import java.util.List;

/**
 *
 * @author ys
 */
public class Button3dBuilder {
    private BTYPE btype;
    private ROT rotate;
    private BSIZE bsize;
    private BEHAVIOR behav; 
    private int rgroup;
    private List<Button3d> b3DL;
    private ButtonListener bi;
    private RessKeeper rk;
    private float imgH;//полувысота имиджа
    private int imgN;//номер имиджа в коллекции
    private List<FaceState> faces;
    private List<FaceState> images;
   //private Vector2f[] v2;
    private float x,y,z;
    private int sect;
    boolean checked;
    //boolean transparent;
    
    public Button3dBuilder(RessKeeper r)
    {
        rk=r;
        btype=BTYPE.PRESS;
        behav=BEHAVIOR.UPDOWN;
        rotate=ROT.Y;
        bsize=BSIZE.LARGE;
        faces=null;
        images=null;
        imgH=0;
        imgN=0;
        //v2=null;
        b3DL=null;
        rgroup=0;
        checked=false;
       // transparent=true;
    }
    
    public Button3dBuilder setAndInitRadio(int grp, boolean checked)
    {
        this.btype=BTYPE.RADIO;
        this.rgroup=grp;
        this.checked=checked;
        return this;
    }
    
    public Button3dBuilder setBtnList(List<Button3d> b3list)
    {
        b3DL=b3list;
        return this;
    }
    
    
    public Button3dBuilder setIf(ButtonListener bi)
    {
        this.bi=bi;
        return this;
    }
    
//    public Button3dBuilder setTransp(boolean transparent)
//    {
//        this.transparent=transparent;
//        return this;
//    }
    
    public Button3dBuilder setType(BTYPE bt)
    {
        this.btype=bt;
        if (bt!=BTYPE.RADIO) rgroup=0;
        return this;
    }
    
    public Button3dBuilder setSize(BSIZE bs)
    {
        this.bsize=bs;
        return this;
    }
    
    public Button3dBuilder setRot(ROT br)
    {
        this.rotate=br;
        return this;
    }
    
    public Button3dBuilder setFaces(List<FaceState> faces)
    {
        this.faces=faces;
        return this;
    }
    
    public Button3dBuilder setImages(List<FaceState> images,float  imgH)
    {
        this.images=images;
        this.imgH=imgH;
        return this;
    }
    
    
    public Button3dBuilder setBehav(BEHAVIOR behav)
    {
        this.behav=behav;
        return this;
    }
    
    public Button3dBuilder setPlace(float x,float y,float z)
    {
        this.x=x;
        this.y=y;
        this.z=z;
        return this;
    }
    
    public Button3dBuilder setSection(int sect)
    {
        this.sect=sect;
        return this;
    }
    
    
    public Button3d build(String name) {
        Button3d b3 = new Button3d(rk, name, bi, btype, bsize, faces);
        //System.out.println("Bi="+bi);
        if (b3DL != null) {
            b3DL.add(b3);
        }
        b3.setRotate(rotate);
        if (behav == BEHAVIOR.NONE) {
            b3.setBehav(behav);
        }
        b3.setMyPlace(x, y, z);
        b3.setSection(sect);
        if (rgroup > 0 && b3DL != null) {
            b3.initRadio(b3DL, rgroup, checked);
        }
        if (images != null) {
            b3.setImage(images, imgH);
            if (imgN != 0 && images.size() - 1 >= imgN) {
                b3.applyImage(imgN);
            }
        }
        return b3;
    }
    
}
