/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yss1.lib_jm;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.TouchInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.input.controls.TouchListener;
import com.jme3.input.controls.TouchTrigger;
import com.jme3.input.controls.Trigger;
import com.jme3.input.event.TouchEvent;
import com.jme3.math.FastMath;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;

//import sun.rmi.runtime.Log;

/**
 * @author ys
 */
public class GameInputBase {
    protected AppBase ap;
    protected InputManager IM;
    private Card ACR;
    private Button3d BUTTON;
    private Wnd WND;
    
    //const
    //private final static Trigger TRIGGER_COLOR = new KeyTrigger(KeyInput.KEY_SPACE);
   // private final static Trigger TRIGGER_COLOR2 = new KeyTrigger(KeyInput.KEY_X);
    //private final static Trigger TRIGGER_YSSATY = new KeyTrigger(KeyInput.KEY_Y);
    //private final static Trigger TRIGGER_YSSATS = new KeyTrigger(KeyInput.KEY_U);
    private final static Trigger TRIGGER_STARTGAME = new KeyTrigger(KeyInput.KEY_F2);
    private final static Trigger TRIGGER_CLEAR = new KeyTrigger(KeyInput.KEY_F3);
    private final static Trigger TRIGGER_MENU = new KeyTrigger(KeyInput.KEY_BACK);
    private final static Trigger TRIGGER_RMBTN = new MouseButtonTrigger(MouseInput.BUTTON_RIGHT);
    private final static Trigger TRIGGER_LMBTN = new MouseButtonTrigger(MouseInput.BUTTON_LEFT);
    private final static Trigger TRIGGER_BACK = new TouchTrigger(TouchInput.KEYCODE_BACK);
    private final static Trigger TRIGGER_ANDROIDMENU = new TouchTrigger(TouchInput.KEYCODE_MENU);
    private final static Trigger TRIGGER_F10MENU = new KeyTrigger(KeyInput.KEY_F10);
    private final static Trigger TRIGGER_TUK = new TouchTrigger(TouchInput.ALL);
    //private final static String MAPPING_COLOR = "Toggle Color";
    private final static String MAPPING_LMBTN = "LeftButton";
    private final static String MAPPING_RMBTN = "RightButton";
    //private final static String MAPPING_YSSA = "YssAction";
    private final static String MAPPING_TUK = "YssTouchAction";
    private final static String MAPPING_MENU = "YssMainMenu";
    private final static String MAPPING_GAMEMENU = "YssGameMenu";//popup Android menu
    private final static String MAPPING_STARTGAME = "YssStartGame";
    private final static String MAPPING_CLEARGAME = "YssClearGame";
    
    public  GameInputBase(AppBase a)
    {
        ap=a;
        IM=ap.getInputManager();
    }
    
    public void init()
    {
        //IM.addMapping(MAPPING_COLOR, TRIGGER_COLOR, TRIGGER_COLOR2);
        IM.addMapping(MAPPING_LMBTN, TRIGGER_LMBTN);
        IM.addMapping(MAPPING_RMBTN, TRIGGER_RMBTN);
        //IM.addMapping(MAPPING_YSSA, TRIGGER_YSSATY, TRIGGER_YSSATS);
        IM.addMapping(MAPPING_TUK, TRIGGER_TUK);
        IM.addMapping(MAPPING_MENU, TRIGGER_MENU);
        IM.addMapping(MAPPING_MENU, TRIGGER_BACK);
        IM.addMapping(MAPPING_GAMEMENU, TRIGGER_ANDROIDMENU);
        IM.addMapping(MAPPING_GAMEMENU, TRIGGER_F10MENU);
        IM.addMapping(MAPPING_STARTGAME, TRIGGER_STARTGAME);
        IM.addMapping(MAPPING_CLEARGAME, TRIGGER_CLEAR);
        
        IM.addListener( actionListener, new String[]{MAPPING_MENU, MAPPING_STARTGAME, MAPPING_CLEARGAME, MAPPING_LMBTN, MAPPING_RMBTN, MAPPING_GAMEMENU});
        IM.addListener(analogListener, new String[]{MAPPING_LMBTN});
        IM.addListener(touchListener, new String[]{MAPPING_TUK, MAPPING_MENU, MAPPING_GAMEMENU});
    }
    
    public void removeListeners()
    {
        IM.removeListener(touchListener);
        IM.removeListener(analogListener);
        IM.removeListener(actionListener);
    }
    
    private ActionListener actionListener = new ActionListener() {
        @Override
        public void onAction(String name, boolean isPressed, float tpf) {
//            if (name.equals(MAPPING_COLOR) && !isPressed) {
//                //ColorRGBA colr = ColorRGBA.randomColor();
//                //System.out.println("You color is: " + colr.toString());
//            }
            if (name.equals(MAPPING_GAMEMENU) && !isPressed) {
//                synchronized(ap.iEvents)
//                        {
//                            ap.iEvents.offerFirst(ToolsBase.vPool.getEvent(InputEvent.EVENTTYPE.F10));
//                        }
                ToolsBase.inputEvents.putEvent(InputEvent.EVENTTYPE.F10);
            }
            if (name.equals(MAPPING_STARTGAME) && !isPressed) {
//                synchronized(ap.iEvents)
//                        {
//                            ap.iEvents.offerFirst(ToolsBase.vPool.getEvent(InputEvent.EVENTTYPE.NEW_GAME));
//                        }
                ToolsBase.inputEvents.putEvent(InputEvent.EVENTTYPE.NEW_GAME);
            }
            if (name.equals(MAPPING_MENU) && !isPressed) {
//                synchronized(ap.iEvents)
//                        {
//                            ap.iEvents.offerFirst(ToolsBase.vPool.getEvent(InputEvent.EVENTTYPE.BACK));
//                        }
                ToolsBase.inputEvents.putEvent(InputEvent.EVENTTYPE.BACK);
            }
            if (name.equals(MAPPING_CLEARGAME) && !isPressed) {

            }

            if (name.equals(MAPPING_LMBTN)) {
                processTouches(IM.getCursorPosition().x,IM.getCursorPosition().y,isPressed?TouchEvent.Type.DOWN:TouchEvent.Type.UP);
            } 
            else if (name.equals(MAPPING_RMBTN) && !isPressed) {
            }
        }
    };
    
    private TouchListener touchListener = new TouchListener() {
        @Override
        public void onTouch(String name, TouchEvent event, float tpf) {
            
            if (name.equals(MAPPING_GAMEMENU) && event.getType() == TouchEvent.Type.KEY_UP) {
//                synchronized(ap.iEvents)
//                        {
//                            ap.iEvents.offerFirst(ToolsBase.vPool.getEvent(InputEvent.EVENTTYPE.F10));
//                        }
                ToolsBase.inputEvents.putEvent(InputEvent.EVENTTYPE.F10);
            } else if (name.equals(MAPPING_MENU) && event.getType() == TouchEvent.Type.KEY_UP) {
//                synchronized(ap.iEvents)
//                        {
//                            ap.iEvents.offerFirst(ToolsBase.vPool.getEvent(InputEvent.EVENTTYPE.BACK));
//                        }
                ToolsBase.inputEvents.putEvent(InputEvent.EVENTTYPE.BACK);
            } else if (name.equals(MAPPING_TUK)) {
                if (event.getPointerId() == 0 && (
                     event.getType() == TouchEvent.Type.DOWN ||
                     event.getType() == TouchEvent.Type.UP ||
                     event.getType() == TouchEvent.Type.MOVE
                    )) {
                    processTouches(event.getX(),event.getY(),event.getType());
                }//POINTER id==0
            }
        }
    };
    
    private AnalogListener analogListener = new AnalogListener() {
        @Override
        public void onAnalog(String name, float intensity, float tpf) {
            if (name.equals(MAPPING_LMBTN)) {
                    processTouches(IM.getCursorPosition().x,IM.getCursorPosition().y,TouchEvent.Type.MOVE);
            }
        }
    };
    
    protected void processTouches(float x,float y, TouchEvent.Type TT)
    {
     
       
    } 
    
  
    
    protected Ray getRay(float x, float y) {
         Camera cam = ap.getCamera();
        
        Vector2f click2d = ToolsBase.vPool.getV2(x, y);
        Vector3f click3d = ToolsBase.vPool.getV3(cam.getWorldCoordinates(click2d, 0f));
        Vector3f dir     = ToolsBase.vPool.getV3(cam.getWorldCoordinates(click2d, 1f).subtractLocal(click3d));



        //assert 1!=1:"length="+dir.length();
        //,"Len="+length);
        Ray ray = ToolsBase.vPool.getRay(click3d, dir);

        ToolsBase.vPool.freeV3(click3d);
        ToolsBase.vPool.freeV2(click2d);
        ToolsBase.vPool.freeV3(dir);
        return ray;
    }
}
