/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yss1.lib_jm;

import java.util.ArrayList;
import java.util.Map;

/**
 *
 * @author ys
 */
public interface AppIF {

    public void setAndroidIF(AndroidIF aif);

    public void recvData(String field, Map<String,Object> value);

    public void onConnected();

    public void stringFromDialog(int dt, String s);

    public void adLoaded();

    public void adClosed();

    public void afterClearOnWrongVersion();

    public void prepareClearOnWrongVersion();

    public void startNetworkGame();

    public void initUsers(ArrayList<UserBase> uBL, String serverID);

    //public void dataReceived(String pID, byte[] dt);

    public void errorSend(String pID);

    public void successSend(String pID);
    
    public void appStop();

}
