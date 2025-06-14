package com.yss1.lib_jm;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ys
 */
/*
JmeFragment наследуется от AndroidHarnes воплощает этот интерфейс.
находится в библиотеке Lib7
 */
public interface AndroidIF  {
  
  //сохранение параметров
  public void saveSettingInt(String k,int v);
  public int  loadSettingInt(String k,int dv);
  public void saveSettingLong(String k,long v);
  public long  loadSettingLong(String k,long dv);
  public void saveSettingBool(String k,boolean v);
  public boolean  loadSettingBool(String k,boolean dv);
  public void saveSettingStr(String k,String v);
  public String  loadSettingStr(String k,String dv);
  public void saveSettingStrSet(String k,Set<String> v);
  public Set<String>  loadSettingStrSet(String k);
  public void removeStrings(ArrayList<String> al);
  public void saveSettingsMap(Map<String,Object> M);
  public Map<String,?> readAll();
  //конец сохранения параметров

  public void displayInterstitial();  
  public boolean isInterstitialLoaded();
  public void loadAd();
  public void showBanner(boolean sb);
  public boolean isBannerVisible();

  //public void setIds();
  public String  getLanguage();
  public void setScreenOFF(boolean off);
  public void writeLog(String theme);
  public void getInput(int dt,String tit, String sinit);
  public void showMessage(String t, String m);
  public void showInfo(String t);
  public void rate(String what);
  public void sendEventGA(String app,String s);
  //public String getName();
  public void mp_signIn();
  public void mp_signOut(boolean signout_google);
  public boolean mp_isSignedIn();
  public void mp_writeDB(String field, Map<String,Object> value);
  public void mp_readDB(String field);
  public void mp_connectDataReceiverFor(String field);

  //public boolean gp_isSignedIn();
  //public boolean mp_connect();
  //public void mp_disconnect();
  //public void gp_ShowAchivements();
  //public void gp_ShowLeaderboard(String id);
  //public void gp_SubmitScore(String id, int sc);
  //public void gp_UnlockAchivement(String Aid);
  //public void gp_IncrementAchivement(String Aid,int sc);
  
  //public void mp_SelectOpponents(boolean allowAutomatch);
  //public void mp_QuickStartGame();
  //public void mp_StartWaitingRoom();
  //public void mp_InvitationInbox();
  //public int  mp_MaxMessageLength();
//  public void mp_Send2All(byte [] data);
//  public void mp_Send2One(String id, byte [] data);
//  public void mp_Sendll(byte [] data);
  //public void mp_ReSendTo(String id, byte [] data);
  //public void mp_SetSendCounter(int val);
  //public int mp_SendTo(String id, byte [] data);
  //public void mp_endNetworkGame();

}
