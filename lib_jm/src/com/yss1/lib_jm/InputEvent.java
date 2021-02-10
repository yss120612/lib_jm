/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yss1.lib_jm;

/**
 *
 * @author ys
 */
public class InputEvent {
    public enum EVENTTYPE{NOP,BUTTON_UP,BUTTON_DOWN,CARD_UP,CARD_DOWN,CARD_SHOW,CARD_END_SHOW, STOCK_DOWN, CLICK_DT, CLICK_COIN,CLICK_MULTIPANEL, CLICK_NAME,NEW_GAME,BACK,F10,MOUSE_DOWN,CARD_SECOND_SELECT,CARD_SECOND_DESELECT,CARD_MOVE,MIX_PRESSED,CLICK_MENU};
    private EVENTTYPE eType;
    private boolean boolParam;
    private Object eventObject;
    private String name;
    
    public InputEvent(EVENTTYPE et)
    {
        eType=et;
        boolParam=false;
        eventObject = null;
        name="";
    }
    
     public InputEvent init(EVENTTYPE et)
    {
        eType=et;
        boolParam=false;
        eventObject = null;
        name="";
        return this;
    }
             
//    public InputEvent(EVENTTYPE et, Object obj, boolean p)
//    {
//        eType=et;
//        boolParam=p;
//        eventObject = obj;
//        name="";
//    }
//    
//    public InputEvent(EVENTTYPE et, Object obj,String nm)
//    {
//        eType=et;
//        boolParam=false;
//        eventObject = obj;
//        name=nm;
//    }
//    
//    public InputEvent init(EVENTTYPE et,Object obj, boolean p)
//    {
//        eType=et;
//        boolParam=p;
//        eventObject = obj;
//        name="";
//        return this;
//    }
//    
//    public InputEvent init(EVENTTYPE et, Object obj, String nm)
//    {
//        eType=et;
//        boolParam=false;
//        eventObject = obj;
//        name=nm;
//        return this;
//    }

    public InputEvent setBoolParam(boolean boolParam) {
        this.boolParam = boolParam;
        return this;
    }

    public InputEvent setEventObject(Object eventObject) {
        this.eventObject = eventObject;
        return this;
                }

    public InputEvent setName(String name) {
        this.name = name;
        return this;
    }
    
    

    public EVENTTYPE geteType() {
        return eType;
    }

    public boolean isBoolParam() {
        return boolParam;
    }

    public String getName() {
        return name;
    }

    public Object getEventObject() {
        return eventObject;
    }
}
