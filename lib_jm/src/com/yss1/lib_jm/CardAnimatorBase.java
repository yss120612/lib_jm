/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yss1.lib_jm;

import com.jme3.anim.AnimClip;
import com.jme3.anim.AnimComposer;
import com.jme3.anim.AnimFactory;
import com.yss1.lib_jm.Card.State;
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
        AnimComposer animComposer=CA.getGe().getControl(AnimComposer.class);
        if (animComposer==null){
            //throw ("Anim composer is NULL!!!");
            throw new NullPointerException("Cannot find Anim composer for " + name);
        }

        if (animComposer.getAction(name)!=null) animComposer.removeAction(name);
        AnimClip clip=animComposer.getAnimClip(name);
        if (clip!=null) animComposer.removeAnimClip(clip);

        AnimFactory animFactory = new AnimFactory(aTime, name, 25f);
       // AnimationFactory af=new AnimationFactory(aTime,name,steps);
        animFactory.addTimeTransform(0,CA.getGe().getLocalTransform());
//        animationFactory.addTimeTranslation(0,CA.getGe().getLocalTransform().getTranslation());
//        animationFactory.addTimeScale(0,CA.getGe().getLocalTransform().getScale());
//        animationFactory.addTimeRotation(0,CA.getGe().getLocalTransform().getRotation());
        //af.addTimeTransform(0, CA.getGe().getLocalTransform());
        Vector3f tmpSv=ToolsBase.vPool.getV3(CA.getEndpointS(),CA.getEndpointS(),CA.getEndpointS());
        //animationFactory.addTimeTranslation(aTime,CA.getEndpointM());
        //animationFactory.addTimeScale(aTime,tmpSv);
        //animationFactory.addTimeRotation(aTime,CA.getEndpointRq());

        animFactory.addTimeTransform(aTime, new Transform(CA.getEndpointM(),CA.getEndpointRq(),tmpSv));
        //MorphTrack mt =
        //AnimClip ac=af.buildAnimation();

        clip = animFactory.buildAnimation(CA.getGe());

        animComposer.addAnimClip(clip);

        animComposer.setCurrentAction(name);
        //((ActionClipYss)animComposer.getAction(name)).setLoop(true);


        //control.addAnimClip(clip);

        //model.addControl(control);

        //rootNode.attachChild(model);

        //run animation
        //control.setCurrentAction("anim");


        //Animation an=af.buildAnimation();
        ToolsBase.vPool.freeV3(tmpSv);
        //CA.getControl().addAnim(an);
        //CA.getChannel().setAnim(name);
        //CA.getChannel().setLoopMode(LoopMode.DontLoop);
    }
    
    public void animate3endpointInv(Card CA, float aTime,int steps, String name,float dx,float dy,float dz)//в средней точке отклонение
    {
        animate3endpoint(CA,aTime,steps,name,dx,dy,dz);//в средней точке отклонение
        CA.invertState();
    }
    
    public void animate3endpoint(Card CA, float aTime,int steps, String name,float dx,float dy,float dz)//в средней точке отклонение
    {
//        if (CA.getControl().getAnimationNames().contains(name)) CA.getControl().removeAnim(CA.getControl().getAnim(name));
//        AnimationFactory af=new AnimationFactory(aTime,name,steps);
//        //StartPoint
//        af.addTimeTransform(0, CA.getGe().getLocalTransform());
//        //MiddlePoint
//        Vector3f tmpSv=ToolsBase.vPool.getV3((CA.getGe().getLocalScale().x+CA.getEndpointS())/2f,(CA.getGe().getLocalScale().x+CA.getEndpointS())/2f,(CA.getGe().getLocalScale().x+CA.getEndpointS())/2f);
//        Vector3f tmpMv=ToolsBase.vPool.getV3(CA.getGe().getLocalTranslation().interpolateLocal(CA.getEndpointM(), 0.5f));
//        tmpMv.x+=dx;
//        tmpMv.y+=dy;
//        tmpMv.z+=dz;
//        Quaternion Q1=ToolsBase.vPool.getQt(CA.getEndpointRq());
//        Q1.slerp(CA.getGe().getLocalRotation(),0.5f);
//        af.addTimeTransform(aTime/2f, new Transform(tmpMv,Q1,tmpSv));
//        //EndPoint
//        tmpSv.set(CA.getEndpointS(),CA.getEndpointS(),CA.getEndpointS());
//        af.addTimeTransform(aTime, new Transform(CA.getEndpointM(),CA.getEndpointRq(),tmpSv));
//        Animation an=af.buildAnimation();
//        //AnimComposer ac;
//        ToolsBase.vPool.freeV3(tmpSv);
//        ToolsBase.vPool.freeV3(tmpMv);
//        ToolsBase.vPool.freeQt(Q1);
//        CA.getControl().addAnim(an);
//        CA.getChannel().setAnim(name);
//        CA.getChannel().setLoopMode(LoopMode.DontLoop);


        AnimComposer animComposer=CA.animComposerYss;
        if (animComposer==null){
            //throw ("Anim composer is NULL!!!");
            throw new NullPointerException("Cannot find Anim composer for " + name);
        }

        if (animComposer.getAction(name)!=null) animComposer.removeAction(name);
        AnimClip clip=animComposer.getAnimClip(name);
        if (clip!=null) animComposer.removeAnimClip(clip);

        AnimFactory animFactory = new AnimFactory(aTime, name, 25f);
        animFactory.addTimeTransform(0,CA.getGe().getLocalTransform());
        //MiddlePoint
        Vector3f tmpSv=ToolsBase.vPool.getV3((CA.getGe().getLocalScale().x+CA.getEndpointS())/2f,(CA.getGe().getLocalScale().x+CA.getEndpointS())/2f,(CA.getGe().getLocalScale().x+CA.getEndpointS())/2f);
        Vector3f tmpMv=ToolsBase.vPool.getV3(CA.getGe().getLocalTranslation().interpolateLocal(CA.getEndpointM(), 0.5f));
        tmpMv.x+=dx;
        tmpMv.y+=dy;
        tmpMv.z+=dz;
        Quaternion Q1=ToolsBase.vPool.getQt(CA.getEndpointRq());
        Q1.slerp(CA.getGe().getLocalRotation(),0.5f);
        animFactory.addTimeTransform(aTime/2f,new Transform(tmpMv,Q1,tmpSv));
        //EndPoint
        tmpSv.set(CA.getEndpointS(),CA.getEndpointS(),CA.getEndpointS());
        animFactory.addTimeTransform(aTime,new Transform(CA.getEndpointM(),CA.getEndpointRq(),tmpSv));

        clip = animFactory.buildAnimation(CA.getGe());

        animComposer.addAnimClip(clip);
        animComposer.setCurrentAction(name);

        ToolsBase.vPool.freeV3(tmpSv);
        ToolsBase.vPool.freeV3(tmpMv);
        ToolsBase.vPool.freeQt(Q1);
//        CA.getControl().addAnim(an);
//        CA.getChannel().setAnim(name);
//        CA.getChannel().setLoopMode(LoopMode.DontLoop);
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
//       if (CA.getControl().getAnimationNames().contains(name)) CA.getControl().removeAnim(CA.getControl().getAnim(name));
//       AnimationFactory af=new AnimationFactory(aTime,name,steps);
//       //StartPoint
//       af.addTimeTransform(0, CA.getGe().getLocalTransform());
//       //MiddlePoint
//       Vector3f tmpRv=ToolsBase.vPool.getV3(1f,0,0);
//       Vector3f tmpSv=ToolsBase.vPool.getV3((CA.getGe().getLocalScale().x+CA.getEndpointS())/2f,(CA.getGe().getLocalScale().x+CA.getEndpointS())/2f,(CA.getGe().getLocalScale().x+CA.getEndpointS())/2f);
//       Vector3f tmpMv=ToolsBase.vPool.getV3(CA.getGe().getLocalTranslation());
//       tmpMv=tmpMv.interpolateLocal(CA.getEndpointM(), 0.5f);
//       tmpMv.z+=h;
//       Quaternion Q1=ToolsBase.vPool.getQt().fromAngleNormalAxis(A, tmpRv);
//       Q1=CA.getGe().getLocalRotation().mult(Q1);
//       af.addTimeTransform(aTime/2f, new Transform(tmpMv,Q1,tmpSv));
//       //EndPoint
//       tmpSv.set(CA.getEndpointS(),CA.getEndpointS(),CA.getEndpointS());
//       af.addTimeTransform(aTime, new Transform(CA.getEndpointM(),CA.getEndpointRq(),tmpSv));
//       //and animate It
//       Animation an=af.buildAnimation();
//       ToolsBase.vPool.freeQt(Q1);
//       ToolsBase.vPool.freeV3(tmpSv);
//       ToolsBase.vPool.freeV3(tmpRv);
//       CA.getControl().addAnim(an);
//       CA.getChannel().setAnim(name);
//       CA.getChannel().setLoopMode(LoopMode.DontLoop);
        AnimComposer animComposer=CA.animComposerYss;
        if (animComposer==null){
            //throw ("Anim composer is NULL!!!");
            throw new NullPointerException("Cannot find Anim composer for " + name);
        }

        if (animComposer.getAction(name)!=null) animComposer.removeAction(name);
        AnimClip clip=animComposer.getAnimClip(name);
        if (clip!=null) animComposer.removeAnimClip(clip);

        AnimFactory animFactory = new AnimFactory(aTime, name, 25f);
        animFactory.addTimeTransform(0,CA.getGe().getLocalTransform());


        //MiddlePoint
        Vector3f tmpRv=ToolsBase.vPool.getV3(1f,0,0);
        Vector3f tmpSv=ToolsBase.vPool.getV3((CA.getGe().getLocalScale().x+CA.getEndpointS())/2f,(CA.getGe().getLocalScale().x+CA.getEndpointS())/2f,(CA.getGe().getLocalScale().x+CA.getEndpointS())/2f);
        Vector3f tmpMv=ToolsBase.vPool.getV3(CA.getGe().getLocalTranslation());
        tmpMv=tmpMv.interpolateLocal(CA.getEndpointM(), 0.5f);
        tmpMv.z+=h;
        Quaternion Q1=ToolsBase.vPool.getQt().fromAngleNormalAxis(A, tmpRv);
        Q1=CA.getGe().getLocalRotation().mult(Q1);
        animFactory.addTimeTransform(aTime/2f, new Transform(tmpMv,Q1,tmpSv));

        //EndPoint
        tmpSv.set(CA.getEndpointS(),CA.getEndpointS(),CA.getEndpointS());
        animFactory.addTimeTransform(aTime, new Transform(CA.getEndpointM(),CA.getEndpointRq(),tmpSv));

        //and animate It
        clip = animFactory.buildAnimation(CA.getGe());
        animComposer.addAnimClip(clip);
        animComposer.setCurrentAction(name);

        ToolsBase.vPool.freeQt(Q1);
        ToolsBase.vPool.freeV3(tmpSv);
        ToolsBase.vPool.freeV3(tmpRv);

    }

    public void animateShift(Card CA, float aTime,int steps, float x, float y, float z) {
//        if (CA.getControl().getAnimationNames().contains("Shift")) CA.getControl().removeAnim(CA.getControl().getAnim("Shift"));
//        AnimationFactory af=new AnimationFactory(aTime,"Shift",steps);
//        Transform tr=CA.getGe().getLocalTransform();
//        af.addTimeTransform(0,tr);
//        tr.getTranslation().x+=x;
//        tr.getTranslation().y+=y;
//        tr.getTranslation().z+=z;
//        af.addTimeTransform(aTime, tr);
//        Animation an=af.buildAnimation();
//        CA.getControl().addAnim(an);
//        CA.getChannel().setAnim("Shift");
//        CA.getChannel().setLoopMode(LoopMode.DontLoop);

        AnimComposer animComposer=CA.animComposerYss;
        if (animComposer==null){
            //throw ("Anim composer is NULL!!!");
            throw new NullPointerException("Cannot find Anim composer for Shift");
        }

        if (animComposer.getAction("Shift")!=null) animComposer.removeAction("Shift");
        AnimClip clip=animComposer.getAnimClip("Shift");
        if (clip!=null) animComposer.removeAnimClip(clip);

        AnimFactory animFactory = new AnimFactory(aTime, "Shift", 25f);
        Transform tr=CA.getGe().getLocalTransform();
        animFactory.addTimeTransform(0,tr);

        tr.getTranslation().x+=x;
        tr.getTranslation().y+=y;
        tr.getTranslation().z+=z;
        animFactory.addTimeTransform(aTime, tr);

        //and animate It
        clip = animFactory.buildAnimation(CA.getGe());
        animComposer.addAnimClip(clip);
        animComposer.setCurrentAction("Shift");

    }

    
    
}
