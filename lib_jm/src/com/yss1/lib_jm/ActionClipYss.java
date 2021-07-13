package com.yss1.lib_jm;

import com.jme3.anim.AnimClip;
import com.jme3.anim.AnimComposer;
import com.jme3.anim.tween.action.Action;
import com.jme3.anim.tween.action.ClipAction;



public class ActionClipYss extends ClipAction {
    protected AnimClipListener animClipListener;
    protected AnimComposer animComposer;
    public ActionClipYss(AnimClip clip){
        super(clip);
    }

    public void setAnimComposer(AnimComposer animComposer){
    this.animComposer=animComposer;
    }

    public AnimComposer getAnimComposer() {
        return animComposer;
    }

    public void setAnimClipListener(AnimClipListener animClipListener) {
        this.animClipListener = animClipListener;
    }

    public AnimClipListener getAnimClipListener()
    {
        return animClipListener;
    }

    @Override
    public boolean interpolate(double t) {
        boolean result=super.interpolate(t);
        if (!result) animClipListener.onAnimCycleDone(this,animComposer, this.toString());
        return result;
    }


}
