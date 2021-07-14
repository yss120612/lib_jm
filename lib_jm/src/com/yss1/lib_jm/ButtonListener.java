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
public interface ButtonListener {
    public void clickButton(String name);
    public void selectRadio(String name);
    public void changeCheckbox(String name, boolean state);
    public Node getParentNode(String name);
    AnimClipListener getAnimClipListener();
}
