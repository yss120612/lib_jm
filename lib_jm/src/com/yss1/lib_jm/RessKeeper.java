/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yss1.lib_jm;

import com.jme3.animation.AnimEventListener;
import com.jme3.scene.Node;

/**
 *
 * @author ys
 */
public interface RessKeeper {
    public RessBase getRess();
    public GameSound getSound();
    public Node getRootNode();
    public ButtonListener getButtonListener(String s);
    public CardAnimatorBase getAnimator();
    public AnimClipListener getGameAnimClipListener();
}
