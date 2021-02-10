/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yss1.lib_jm;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *
 * @author ys
 */
public class InputEvents {
    //private final List<InputEvent> events =  Collections.synchronizedList(new LinkedList<InputEvent>());
    private final ConcurrentLinkedQueue<InputEvent> events = new ConcurrentLinkedQueue<>();
    //private final LinkedList<InputEvent> events=new LinkedList<>();
    public void putEvent(InputEvent.EVENTTYPE et)
    {
        InputEvent iE=ToolsBase.vPool.getEvent(et);
        events.offer(iE);
    }
    
    public void putEvent(InputEvent.EVENTTYPE et, Object obj)
    {
        InputEvent iE=ToolsBase.vPool.getEvent(et).setEventObject(obj);
         events.offer(iE);
    }
    
    public void putEvent(InputEvent.EVENTTYPE et, Object obj, String nm)
    {
        InputEvent iE=ToolsBase.vPool.getEvent(et).setEventObject(obj).setName(nm);
         events.offer(iE);
    }
    
    public void putEvent(InputEvent.EVENTTYPE et, Object obj, boolean succ)
    {
        InputEvent iE=ToolsBase.vPool.getEvent(et).setEventObject(obj).setBoolParam(succ);
        events.offer(iE);
        
                }
    
    public InputEvent getEvent()
    {
        InputEvent iE=events.poll();
        if (iE!=null)
        {
         ToolsBase.vPool.freeEvent(iE);
        }
        return iE;
    }
}
