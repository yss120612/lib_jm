package com.yss1.lib_jm;

import com.jme3.anim.AnimComposer;
import com.jme3.anim.tween.action.Action;

public interface AnimClipListener {

    public void onAnimCycleDone(Action action, AnimComposer animComposer, String animName);
}
