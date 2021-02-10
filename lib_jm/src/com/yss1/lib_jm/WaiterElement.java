/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yss1.lib_jm;

//import com.yss1.lib_jm.WaiterObject.WAITERTYPE;

/**
 *
 * @author ys
 */
public class WaiterElement {
 public static enum WAITERTYPE{
        EMPTY_TYPE,
        START_NET_GAME,
        DATA_RECEIVED,
        SUCCESS_SEND,ERROR_SEND,
        AD_LOADED,
        AD_CLOSED,
        AD_NEED,
        BANNER_VISIBLE,
        STOP_FLASH,
        SEND_ALIVE,
        CLOSE_WAIT,
        CLICK_BUTTON1,
        CLICK_BUTTON2,
        PICTURE_WAIT,
        SERVER_WAIT,
        MIX_FINISHED,
        TORG_STEP,
        CHECK_RASKLAD,
        RASKLAD_OK,
        CHECK_PRIKUP,
        PRIKUP_OK,
        GET_PRIKUP
    }
 
    private float time;
    private boolean timed;

    public boolean isTimed() {
        return timed;
    }
    private boolean wait;
    private IExecutor executor;
    //private final WaiterObject parametr = new WaiterObject();
    private String info;
    private WAITERTYPE type;
    
     private void initPar(WAITERTYPE type, String info) {
        this.info = info;
        this.type = type;
    }

    public WAITERTYPE getType() {
        return type;
    }

    public String getInfo() {
        return info;
    }
        
    public WaiterElement(IExecutor ex) {
        executor = ex;
    }

    public WaiterElement() {
        executor = null;
    }

    public boolean isWait() {
        return wait;
    }

//    public WaiterElement setTime(float time) {
//        this.time = time;
//        wait = true;
//        timed = true;
//        return this;
//    }

    public WaiterElement setExecutor(IExecutor executor) {
        this.executor = executor;
        return this;
    }

    public IExecutor getExecutor() {
        return executor;
    }

    public WaiterElement init(WAITERTYPE t, String s) {
        wait = true;
        timed = false;
        initPar(t, s);
        return this;
    }

    public WaiterElement init(WAITERTYPE tp, String s, float tm) {
        wait = true;
        timed = true;
        time = tm;
        initPar(tp, s);
        return this;
    }
    
    /**
     * call execute when time is<=0
     * @param t
     * @return 
     */
    public boolean action(float t) {
        if (!timed || !wait) {
            return false;
        }
        time -= t;
        if (time <= 0) {
            wait = false;
            executor.execute(this);
            return true;
        }
        return false;
    }

    /**
     * call execute when first call this action
     * @return 
     */
    public boolean action() {
        if (timed || !wait) {
            return false;
        }
        wait = false;
        executor.execute(this);
        return true;
    }
    
    public boolean reset(WAITERTYPE wt) {
        if (wait && wt==type) {
            wait = false;
            return true;
        }
        return false;
    }
    
    
}
