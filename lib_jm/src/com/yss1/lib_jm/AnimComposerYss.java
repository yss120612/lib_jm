package com.yss1.lib_jm;

import com.jme3.anim.AnimClip;
import com.jme3.anim.AnimComposer;
import com.jme3.anim.tween.action.Action;

public class AnimComposerYss extends AnimComposer {
    public AnimComposerYss(){
        super();
    }

    //Ловит событие окончания анимации
    protected AnimClipListener animClipListener;
    public AnimClipListener getAnimClipListener() {
        return animClipListener;
    }
    public void setAnimClipListener(AnimClipListener animClipListener) {
        this.animClipListener = animClipListener;
    }


    // Create a new ClipAction with specified clip name.
    @Override
    public Action makeAction(String name) {
        ActionClipYss action;
        AnimClip clip =  getAnimClip(name);
        if (clip == null) {
            throw new IllegalArgumentException("Cannot find clip named " + name);
        }
        action =new ActionClipYss(clip);
        action.setAnimClipListener(animClipListener);
        action.setAnimComposer(this);
        return action;

    }
}
