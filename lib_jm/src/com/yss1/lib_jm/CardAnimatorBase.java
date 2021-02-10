/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yss1.lib_jm;

import com.yss1.lib_jm.Card.State;
import com.jme3.animation.Animation;
import com.jme3.animation.AnimationFactory;
import com.jme3.animation.LoopMode;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Transform;
import com.jme3.math.Vector3f;

/**
 *
 * @author ys
 */
public class CardAnimatorBase {
    
    public CardAnimatorBase()
    {
    }
    
    public void animate2endpointInv(Card CA, float aTime,int steps, String name)
    {
        animate2endpoint(CA,aTime,steps,name);
        CA.invertState();
    }
    
    public void animate2endpointPreUp(Card CA, float aTime,int steps, String name, float up)
    {
        CA.getGe().move(0, 0, up);
        animate2endpoint(CA,aTime,steps,name);
    }
    
    
    public void animate2endpoint(Card CA, float aTime,int steps, String name)
    {
        if (CA.getControl().getAnimationNames().contains(name)) CA.getControl().removeAnim(CA.getControl().getAnim(name));
        AnimationFactory af=new AnimationFactory(aTime,name,steps);
        af.addTimeTransform(0, CA.getGe().getLocalTransform());
        Vector3f tmpSv=ToolsBase.vPool.getV3(CA.getEndpointS(),CA.getEndpointS(),CA.getEndpointS());
        af.addTimeTransform(aTime, new Transform(CA.getEndpointM(),CA.getEndpointRq(),tmpSv));
        Animation an=af.buildAnimation();
        ToolsBase.vPool.freeV3(tmpSv);
       // if (CA.getControl().getAnim(name)!=null) CA.getControl().removeAnim(CA.getControl().getAnim(name));
        CA.getControl().addAnim(an);
        CA.getChannel().setAnim(name);
        CA.getChannel().setLoopMode(LoopMode.DontLoop);
    }
    
    public void animate3endpointInv(Card CA, float aTime,int steps, String name,float dx,float dy,float dz)//в средней точке отклонение
    {
        animate3endpoint(CA,aTime,steps,name,dx,dy,dz);//в средней точке отклонение
        CA.invertState();
    }
    
    public void animate3endpoint(Card CA, float aTime,int steps, String name,float dx,float dy,float dz)//в средней точке отклонение
    {
        if (CA.getControl().getAnimationNames().contains(name)) CA.getControl().removeAnim(CA.getControl().getAnim(name));
        AnimationFactory af=new AnimationFactory(aTime,name,steps);
        //StartPoint
        af.addTimeTransform(0, CA.getGe().getLocalTransform());
        //MiddlePoint
        Vector3f tmpSv=ToolsBase.vPool.getV3((CA.getGe().getLocalScale().x+CA.getEndpointS())/2f,(CA.getGe().getLocalScale().x+CA.getEndpointS())/2f,(CA.getGe().getLocalScale().x+CA.getEndpointS())/2f);
        Vector3f tmpMv=ToolsBase.vPool.getV3(CA.getGe().getLocalTranslation().interpolateLocal(CA.getEndpointM(), 0.5f));
        tmpMv.x+=dx;
        tmpMv.y+=dy;
        tmpMv.z+=dz;
        Quaternion Q1=ToolsBase.vPool.getQt(CA.getEndpointRq());
        Q1.slerp(CA.getGe().getLocalRotation(),0.5f);
        af.addTimeTransform(aTime/2f, new Transform(tmpMv,Q1,tmpSv));
        //EndPoint
        tmpSv.set(CA.getEndpointS(),CA.getEndpointS(),CA.getEndpointS());
        af.addTimeTransform(aTime, new Transform(CA.getEndpointM(),CA.getEndpointRq(),tmpSv));
        Animation an=af.buildAnimation();
        ToolsBase.vPool.freeV3(tmpSv);
        ToolsBase.vPool.freeV3(tmpMv);
        ToolsBase.vPool.freeQt(Q1);
        CA.getControl().addAnim(an);
        CA.getChannel().setAnim(name);
        CA.getChannel().setLoopMode(LoopMode.DontLoop);
    }
    
    public void moveOnDeskPreUp(Card CA, float aTime,int steps, String name, float h,float preh)
    {
    CA.getGe().move(0, 0, preh);
    moveOnDesk(CA,aTime,steps,name,h);
    }
    
    public void moveOnDesk(Card CA, float aTime,int steps, String name,float h)
    {
       float A;
       if (CA.getState()==State.BACK) {A=FastMath.HALF_PI;CA.invertState();} else A=FastMath.PI;
       if (CA.getControl().getAnimationNames().contains(name)) CA.getControl().removeAnim(CA.getControl().getAnim(name));
       AnimationFactory af=new AnimationFactory(aTime,name,steps);
       //StartPoint
       af.addTimeTransform(0, CA.getGe().getLocalTransform());
       //MiddlePoint
       Vector3f tmpRv=ToolsBase.vPool.getV3(1f,0,0);
       Vector3f tmpSv=ToolsBase.vPool.getV3((CA.getGe().getLocalScale().x+CA.getEndpointS())/2f,(CA.getGe().getLocalScale().x+CA.getEndpointS())/2f,(CA.getGe().getLocalScale().x+CA.getEndpointS())/2f);
       Vector3f tmpMv=ToolsBase.vPool.getV3(CA.getGe().getLocalTranslation());
       tmpMv=tmpMv.interpolateLocal(CA.getEndpointM(), 0.5f);
       tmpMv.z+=h;
       Quaternion Q1=ToolsBase.vPool.getQt().fromAngleNormalAxis(A, tmpRv);
       Q1=CA.getGe().getLocalRotation().mult(Q1);
       af.addTimeTransform(aTime/2f, new Transform(tmpMv,Q1,tmpSv));
       //EndPoint
       tmpSv.set(CA.getEndpointS(),CA.getEndpointS(),CA.getEndpointS());
       af.addTimeTransform(aTime, new Transform(CA.getEndpointM(),CA.getEndpointRq(),tmpSv));
       //and animate It
       Animation an=af.buildAnimation();
       ToolsBase.vPool.freeQt(Q1);
       ToolsBase.vPool.freeV3(tmpSv);
       ToolsBase.vPool.freeV3(tmpRv);
       CA.getControl().addAnim(an);
       CA.getChannel().setAnim(name);
       CA.getChannel().setLoopMode(LoopMode.DontLoop);
    }

    public void animateShift(Card CA, float aTime,int steps, float x, float y, float z) {
        if (CA.getControl().getAnimationNames().contains("Shift")) CA.getControl().removeAnim(CA.getControl().getAnim("Shift"));
        AnimationFactory af=new AnimationFactory(aTime,"Shift",steps);
        Transform tr=CA.getGe().getLocalTransform();
        af.addTimeTransform(0,tr);
        tr.getTranslation().x+=x;
        tr.getTranslation().y+=y;
        tr.getTranslation().z+=z;
        af.addTimeTransform(aTime, tr);
        Animation an=af.buildAnimation();
        CA.getControl().addAnim(an);
        CA.getChannel().setAnim("Shift");
        CA.getChannel().setLoopMode(LoopMode.DontLoop);
    }

    
    
}
