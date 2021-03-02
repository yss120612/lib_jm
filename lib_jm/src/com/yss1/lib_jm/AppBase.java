/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yss1.lib_jm;

import com.jme3.animation.AnimEventListener;
import com.jme3.app.SimpleApplication;
import com.jme3.app.StatsAppState;
import com.jme3.app.state.AppState;
import com.jme3.effect.ParticleEmitter;
import com.jme3.font.BitmapFont;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;

import static com.yss1.lib_jm.WaiterElement.WAITERTYPE.*;

import java.util.ArrayList;
import java.util.HashSet;

import java.util.Map;
import java.util.Set;

/**
 * @author ys
 *
 */
public abstract class AppBase extends SimpleApplication
        implements
        QnetListiner,
        RessKeeper,
        IExecutor,
        AppIF {

    public AndroidIF androidIF = null;
    private boolean isAnd;
    public GameSound SOUND;
    public ScoreBoard HBOARD;
    public Qnet QNET;
    public Users USERS;
    public GameInputBase INPUT;
    public RessBase RES;
    public UImanagerBase UIM;
    protected String runOnConnect = "";
    public ParticleEmitter flash;
    protected CardAnimatorBase animator;


    @Override
    public void simpleInitApp() {

        isAnd = "Dalvik".equals(System.getProperty("java.vm.name"));
        SettBase.scrW = Math.max(getContext().getSettings().getWidth(), getContext().getSettings().getHeight());
        SettBase.scrH = Math.min(getContext().getSettings().getWidth(), getContext().getSettings().getHeight());

        flyCam.setEnabled(false);

        int il = 2;
        if (getDefaultLanguage().toUpperCase().equals("RU")) {
            il = 1;
        }
        SettBase.lang = loadSettInt("LANGUAGE", il);
        if (SettBase.lang == 2) {
            SettBase.youName = loadSettStr("PLAYER_NAME", SettBase.youName2);
        } else {
            SettBase.youName = loadSettStr("PLAYER_NAME", SettBase.youName1);
        }

        SettBase.sound_on = loadSettBool("SOUND_ON", true);
        SettBase.screen_on = loadSettBool("SCREEN_ON", false);

        SOUND = new GameSound(getAssetManager());
        if (HBOARD == null) HBOARD = new ScoreBoard(this);


        //Это бред, но без этого частицы на самсе не рулят
        //BitmapText BT=new BitmapText(guiFont);
        //guiNode.attachChild(BT);

        flash = null;
    }

    @Override
    protected BitmapFont loadGuiFont() {
        AppState as = getStateManager().getState(StatsAppState.class);
        if (as != null) {
            getStateManager().detach(as);
        }
        //return getAssetManager().loadFont("Interface/Fonts/Calibri.fnt");
        return null;
    }

    public void flash(ColorRGBA COL, Vector3f v) {

    }

    protected void initFlash() {
        flash = null;
    }

    protected void stopFlash() {
        //flash=null;
    }


    @Override
    public GameSound getSound() {
        return SOUND;
    }


    public boolean isAndroid() {
        return isAnd;
    }

    @Override
    public void setAndroidIF(AndroidIF aif) {
        this.androidIF = aif;
    }

    public void showInterstitial() {
        if (androidIF != null) {
            androidIF.displayInterstitial();
        }
    }

    public void loadAd(){
        if (androidIF != null) {
            androidIF.loadAd();
        }
    }

    public boolean isBannerVisible(){
        if (androidIF != null) {
            return androidIF.isBannerVisible();
        }
        return false;
    }

    public void showBanner(boolean sh) {
        if (androidIF != null) {
            androidIF.showBanner(sh);
        }
    }

    public boolean isInterstitialLoadedLoaded() {
        return androidIF != null && androidIF.isInterstitialLoaded();
    }

    public boolean isSignedIn() {
        return (androidIF != null && androidIF.gp_isSignedIn());
    }

    public void connect() {
        if (androidIF != null && !androidIF.gp_isSignedIn()) androidIF.gp_Connect();
    }

    public void disconnect() {
        if (androidIF != null) androidIF.gp_Disconnect();
    }

    @Override
    public void onConnected() {
//        if (getUIM()==null || getUIM().getPlane('P') ==null)
//        {
//          //setMatForGpPlane("GP_Active");
//          getUIM().setGPmaterial(true);
//        }   
//        else
//        {
//          getUIM().getPlane('P').setMaterial("GP_Active");
//        }

        if (getUIM() != null) {
            getUIM().setGPmaterial(true);
        }

        if (!runOnConnect.isEmpty()) {
            if (runOnConnect.contains("Achivements")) {
                showAcivements();
            } else if (runOnConnect.contains("Multiplayer")) {
                androidIF.mp_InvitationInbox();
            } else {
                showLeaderboard(runOnConnect);
            }
            runOnConnect = "";
        }

    }

    protected void gp_Menu(float X, String LB) {//Joystick+Achivements+Leaderboard
        if (androidIF == null) {
            return;
        }
        boolean conn = isSignedIn();
        if (X < -0.1f) {
            if (conn) {

                disconnect();
                getUIM().setGPmaterial(false);
            } else {
                runOnConnect = "";
                connect();
            }
            return;
        }
        if (X < 0.33f) {
            if (!conn) {
                runOnConnect = "Achivements";
                connect();
            } else {
                showAcivements();
            }
        } else {
            if (!conn) {
                runOnConnect = LB;
                connect();
            } else {
                showLeaderboard(LB);
            }
        }
    }

    public void showAcivements() {
        if (androidIF != null) androidIF.gp_ShowAchivements();
    }

    public void showLeaderboard(String Lid) {
        if (androidIF != null) androidIF.gp_ShowLeaderboard(Lid);
    }

    public void unlockAchivment(String Lid) {
        if (androidIF != null) androidIF.gp_UnlockAchivement(Lid);
    }

    public void incrementAchivment(String Lid, int sc) {
        if (androidIF != null) androidIF.gp_IncrementAchivement(Lid, sc);
    }

    public void submitScore(String Lid, int sc) {
        if (androidIF != null) androidIF.gp_SubmitScore(Lid, sc);
    }

    public String getDefaultLanguage() {
        if (androidIF != null) {
            return androidIF.getLanguage();
        }
        return "";
    }

    public void getInput(int dt, String tit, String sinit) {
        if (androidIF != null) {
            androidIF.getInput(dt, tit, sinit);
        }
    }

    public void showAndroidMessage(String t, String m) {
        if (androidIF != null) {
            androidIF.showMessage(t, m);
        }
    }

    public void showAndroidInfo(String t) {
        if (androidIF != null) {
            androidIF.showInfo(t);
        }
    }


    //Load && Save
    public void saveSettInt(String k, int v) {
        if (androidIF != null) {
            androidIF.saveSettingInt(k, v);
        }
    }

    public int loadSettInt(String k, int v) {
        if (androidIF != null) {
            return androidIF.loadSettingInt(k, v);
        }
        return v;
    }

    public void saveSettLong(String k, long v) {
        if (androidIF != null) {
            androidIF.saveSettingLong(k, v);
        }
    }

    public long loadSettLong(String k, long v) {
        if (androidIF != null) {
            return androidIF.loadSettingLong(k, v);
        }
        return v;
    }

    public void saveSettBool(String k, boolean v) {
        if (androidIF != null) {
            androidIF.saveSettingBool(k, v);
        }
    }

    public boolean loadSettBool(String k, boolean v) {
        if (androidIF != null) {
            return androidIF.loadSettingBool(k, v);
        }
        return v;
    }

    public String loadSettStr(String k, String v) {
        if (androidIF != null) {
            return androidIF.loadSettingStr(k, v);
        }
        return v;
    }

    public void saveSettStr(String k, String v) {
        if (androidIF != null) {
            androidIF.saveSettingStr(k, v);
        }
    }

    public Set<String> loadSettStrSet(String k) {
        if (androidIF != null) {
            return androidIF.loadSettingStrSet(k);
        }
        return new HashSet<>();
    }

    public void saveSettStrSet(String k, Set<String> v) {
        if (androidIF != null) {
            androidIF.saveSettingStrSet(k, v);
        }
    }

    public void saveSettingsMap(Map<String, Object> M) {
        if (androidIF != null) {
            androidIF.saveSettingsMap(M);
        }
    }

    public void removeStrings(ArrayList<String> al) {
        if (androidIF != null) {
            androidIF.removeStrings(al);
        }
    }
    //end of Load && Save 

    @Override
    public void writeAndroidLog(String t) {
        if (androidIF != null) {
            androidIF.writeLog(t);
        }
    }

    @Override
    public abstract void stringFromDialog(int dt, String s);

    public void setScrOFF(boolean b) {
        if (androidIF != null) {
            androidIF.setScreenOFF(b);
        }
    }

    public void rate(String wht) {
        if (androidIF != null) {
            androidIF.rate(wht);
        }
    }

    public void sendEventGA(String s) {

    }

    @Override
    public RessBase getRess() {
        return RES;
    }

    @Override
    public AnimEventListener getGameAEL() {
        return null;
    }

    @Override
    public CardAnimatorBase getAnimator() {
        return animator;
    }

    public UImanagerBase getUIM() {
        return UIM;
    }

    @Override
    public void prepareClearOnWrongVersion() {
        if (HBOARD == null) {
            HBOARD = new ScoreBoard(this);
        } else {
            HBOARD.localRead();
        }
    }

    @Override
    public void afterClearOnWrongVersion() {
        HBOARD.localSave();
    }

    public void selectOpponents(boolean am) {
        if (androidIF != null) {
            androidIF.mp_SelectOpponents(am);
        }
    }

    public void quickStart() {
        if (androidIF != null) {
            androidIF.mp_QuickStartGame();
        }
    }

    public void invitationInbox() {
        if (androidIF != null) {
            androidIF.mp_InvitationInbox();
        }
    }

    @Override
    public void startNetworkGame() {
        ToolsBase.waiters.initWaiter(this, START_NET_GAME, "Start server", 0.5f);
    }


    @Override
    public void dataReceived(String pID, byte[] dt) {
        if (QNET != null) {
            QNET.receivePacket(pID, dt);
            ToolsBase.waiters.initWaiter(this, DATA_RECEIVED, pID, 0.5f);
        }
    }

    @Override
    public void successSend(String pID) {
        ToolsBase.waiters.initWaiter(this, SUCCESS_SEND, pID, 0.5f);
    }

    @Override
    public void errorSend(String pID) {
        ToolsBase.waiters.initWaiter(this, ERROR_SEND, pID, 0.8f);
    }


    @Override
    public void adLoaded() {
        ToolsBase.waiters.initWaiter(this, AD_LOADED, "AD loaded", 0.5f);
    }

    @Override
    public void adClosed() {
        ToolsBase.waiters.initWaiter(this, AD_CLOSED, "AD closed", 0.5f);
    }

    public int maxMessageLength() {
        if (androidIF != null) {
            return androidIF.mp_MaxMessageLength();
        }
        return 0;
    }

    @Override
    public void send2All(NetPacket np) {
        //только используется qnet
        if (androidIF == null) {
            return;
        }
        int res;
        //showAndroidMessage(np.getPacketType()+"",np.getContent());

        for (UserBase ub : USERS.getuList()) {
            if (!ub.isI()) {
                res = androidIF.mp_SendTo(ub.getUid(), np.getData());
                if (res > 0) {
                    np.addContragent(ub.getUid());
                }
            }
        }
    }

    @Override
    public void send2One(String id, NetPacket np) {
        if (androidIF != null) {
            if (androidIF.mp_SendTo(id, np.getData()) > 0) {
                // np.addContragent(id);
                //contragent уже есть
            }
        }
    }

    @Override
    public void sendAllAttemptsError(String id) {
        showAndroidMessage("Send error", "error send to " + id);
    }

    @Override
    public void gameLeaved() {
        if (androidIF != null) {
            //writeAndroidLog("Leave");
            androidIF.mp_endNetworkGame();
        }
    }

    @Override
    public void initUsers(ArrayList<UserBase> uBL, String serverID) {
        //if (USERS==null) USERS=new Users();
        USERS.setuList(uBL);
        USERS.setServer(serverID);
    }

    @Override
    public void execute(WaiterElement param) {

        throw new RuntimeException("Please call my child only 'execute'");
    }

    @Override
    public ButtonListener getButtonListener(String s) {
        throw new RuntimeException("Please call my child only 'getButtonInterface'");
    }

    @Override
    public void gamePaused() {
        if (androidIF != null) {
            //  disconnect();
        }
    }

    @Override
    public void appStop() {

    }

    //read all config an wtite it to log
    public void readAll() {
        if (androidIF == null) {
            return;
        }
        for (Map.Entry<String, ?> en : androidIF.readAll().entrySet()) {
            writeAndroidLog(en.getKey() + "\t= " + en.getValue().toString());
        }
    }

}
