package com.yss1.lib_jm;

import com.jme3.anim.AnimClip;
import com.jme3.anim.AnimComposer;
import com.jme3.anim.tween.action.Action;
import com.jme3.anim.tween.action.ClipAction;



public class ActionClipYss extends ClipAction {
    public ActionClipYss(AnimClip clip){
        super(clip);
        loop=false;
    }

    protected AnimClipListener animClipListener;//Ловит событие окончания анимации
    public void setAnimClipListener(AnimClipListener animClipListener) {
        this.animClipListener = animClipListener;
    }
    public AnimClipListener getAnimClipListener()
    {
        return animClipListener;
    }


    protected AnimComposer animComposer;//компановщик анимации
    public void setAnimComposer(AnimComposer animComposer){
        this.animComposer=animComposer;
    }
    public AnimComposer getAnimComposer() {
        return animComposer;
    }

    protected boolean loop;//признак зацикливания
    public boolean isLoop() {
        return loop;
    }
    public void setLoop(boolean loop) {
        this.loop = loop;
    }

    //Перематывает анимацию на время Т, если больше, чем длительность клипа
    //анимация прекращается (листенер обрабатывает событие)
    @Override
    public boolean interpolate(double t) {
        boolean result=super.interpolate(t);
        if (!result) {
            if (!loop) animComposer.reset();
            animClipListener.onAnimCycleDone(this, animComposer, this.toString());
        }
        return result;
    }
}
