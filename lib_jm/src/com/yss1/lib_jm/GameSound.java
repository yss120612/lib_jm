/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yss1.lib_jm;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioData;
import com.jme3.audio.AudioNode;
import java.util.HashMap;
import java.util.Map.Entry;
//import skell.Main;
//import skell.Sett;

/**
 *
 * @author ys
 */
public class GameSound {
    private SimpleApplication ap;
    private AssetManager AM;
    private HashMap<String,AudioNode> AUDIO;
    //vjik, dist, piu, btndown,btnup;//moneyin,
    
    public GameSound(AssetManager a)
    {
        //ap=a;
        AM=a;
        AUDIO= new HashMap<String,AudioNode>();
        
    }
    
    public void init(HashMap<String,String> sn)
    {

        for (Entry<String,String> entry: sn.entrySet())
        {
            AUDIO.put(entry.getKey(), new AudioNode(AM, "Sounds/"+entry.getValue()+".ogg", AudioData.DataType.Buffer));
        }
    }




    public void playSound(String no) {
        if (!SettBase.sound_on || !AUDIO.containsKey(no)) {
            return;
        }
        AUDIO.get(no).playInstance();
    }
}
