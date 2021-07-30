/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yss1.lib_jm;

import java.util.Map;

/**
 *
 * @author ys
 */
public interface QnetListiner {
public void gameLeaved();
public void gamePaused();

    public void sendPacket(NetPacket np);

    public void dataReceived(Map<String,Object> pID);

//public void send2All(NetPacket np);
//public void send2One(String partID, NetPacket np);
public void sendAllAttemptsError(String partID);
public void writeAndroidLog(String txt);
}
