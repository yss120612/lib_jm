/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yss1.lib_jm;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *
 * @author ys
 */
public class Waiters {

    //private final LinkedList<WaiterElement> wElementsNS = new LinkedList<>();
    //private final SynchronizedList<WaiterElement> wElementsNS = new LinkedList<>();
    //private final List <WaiterElement> wElements = Collections.synchronizedList(new LinkedList<WaiterElement>());
    private final ConcurrentLinkedQueue<WaiterElement> wElements = new ConcurrentLinkedQueue<>();
    //private final List<WaiterElement> wTempBuff = new LinkedList<>();
    //private final int MAX_ELEMENTS = 10;
        
//    public LinkedList<WaiterElement> getElements() {
//        return wElements;
//    }
    public Waiters() {
    }
    /** non timed waiter
     * it timed action
     *
     * @param ex object with implements executor interface
     * @param tp type of action
     * @param inf any infornation for executer (String)
     */
    public void initWaiter(IExecutor ex, WaiterElement.WAITERTYPE tp, String inf) {
        WaiterElement we = ToolsBase.vPool.getWElement().init(tp, inf).setExecutor(ex);
        wElements.add(we);
    }

    /** timed waiter
     * init timed action
     *
     * @param ex object with implements executor interface
     * @param tp type of action
     * @param inf any infornation for executer (String)
     * @param tm time to wain in seconds
     */
    public void initWaiter(IExecutor ex, WaiterElement.WAITERTYPE tp, String inf, float tm) {
        WaiterElement we = ToolsBase.vPool.getWElement().init(tp, inf, tm).setExecutor(ex);
        wElements.add(we);
    }

    public boolean checkActions(IExecutor eX, float tm) {
        boolean result = false;
        boolean one_action;
        synchronized (wElements) {
            for (WaiterElement wE : wElements) {
                if (wE.getExecutor() == eX) {
                    one_action = (wE.isTimed()) ? wE.action(tm) : wE.action();
                    result = result || one_action;
                }
            }
        }
        if (result) {
            fresh();
        }
        return result;
    }

    /**
     * remove waiter without execute
     *
     * @param eX - executor of this waiter
     * @param tp - waiter type
     * @return
     */
    public boolean resetWaiter(IExecutor eX, WaiterElement.WAITERTYPE tp) {
        boolean result = false;
        boolean one_action;
        synchronized (wElements) {
            for (WaiterElement wE : wElements) {
                if (wE.getExecutor() == eX) {
                    one_action = wE.reset(tp);
                    result = result || one_action;
                }
            }
        }
        if (result) {
            fresh();
        }
        return result;
    }

    public boolean checkActions(IExecutor eX) {
        return checkActions(eX, -1f);
    }

    private void fresh() {
        synchronized (wElements) {
            if (wElements.size() < 10) {
                return;
            }

            List<WaiterElement> wel = new ArrayList<>();
            for (WaiterElement wE : wElements) {
                if (!wE.isWait()) {
                    wel.add(wE);
                }
            }

            for (WaiterElement wE : wel) {
                wElements.remove(wE);
                ToolsBase.vPool.freeWElement(wE);
            }
        }
    }


}
