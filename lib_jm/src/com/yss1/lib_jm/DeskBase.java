/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yss1.lib_jm;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.LoopMode;
import com.jme3.math.Transform;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 *
 * @author ys
 */
public class DeskBase {
    
    protected AppBase app;
    protected Geometry coin;
    protected Geometry ge;
    protected Geometry trPlane;
    protected Node myNode;
    protected AnimControl acontrol;
    protected AnimChannel achannel;
    
    protected char [] main_pannels;
    protected char [] more_pannels;
    
    public DeskBase(AppBase a)
    {
        app=a;
        myNode=new Node("pBase");
    }
    
    public void init()
    {
       myNode.detachAllChildren();
       Vector2f v=app.getRess().getTexDim4Material("Стол");
       float xrepeat = SettBase.scrW/v.x;
       float yrepeat = xrepeat*SettBase.scrH/SettBase.scrW*v.x/v.y;
       
       Mesh ms=ToolsBase.makeSimplePlane(SettBase.deskHw,SettBase.deskHh, ToolsBase.singleTex);
       ms.scaleTextureCoordinates(new Vector2f(xrepeat,yrepeat));
       
       ge=new Geometry("Desk",ms);
       ge.setMaterial(app.getRess().getMaterial("Стол"));
       ge.setQueueBucket(RenderQueue.Bucket.Opaque);
       myNode.attachChild(ge);
       
       ms=ToolsBase.makePlane(SettBase.deskHw*2f,SettBase.deskHh*2f, null,null);
       trPlane = new Geometry("move_plane", ms);
       trPlane.setCullHint(Spatial.CullHint.Always);
       initPlanesLetters();
    }
    
    protected void initPlanesLetters()
    {
    main_pannels = new char[]{};
    more_pannels = new char[]{};
    }
    
    public Geometry getTrPlane() {
        return trPlane;
    }
    
     public Node getMyNode() {
        return myNode;
    }
    
     
     public void idleCoin()
    {  
     achannel.setAnim("Idle");
     achannel.setLoopMode(LoopMode.DontLoop);
     myNode.detachChild(coin);
    }
   
      public void showCoin(Vector3f v)
    {
        myNode.attachChild(coin);
        coin.setLocalTransform(Transform.IDENTITY);
        coin.setLocalTranslation(v);
    }
     
    public void initCoin(Transform T)
    {
     myNode.attachChild(coin);
     coin.setLocalTransform(T);
    }
    
   

    protected void enablePannels(char [] ac, boolean attach)
    {
        InfoPlane ip;
        for (char ch: ac)
        {
            ip=app.getUIM().getPlane(ch);
            if (ip!=null) 
                if (attach)
                {
                myNode.attachChild(ip.getNodePlane());
                }
            else
                {
                myNode.detachChild(ip.getNodePlane());
                }
        }
    }
    
    
}
