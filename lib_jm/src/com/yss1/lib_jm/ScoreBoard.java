/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yss1.lib_jm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author ys
 */
public class ScoreBoard {

    public static int MAX_PLAYERS = 3;
    private ArrayList<HighScore> HSL;
    private AppBase ap;
    private HighScore chemp;
    private Map<String, Object> savePack = new HashMap<>();

    public ScoreBoard(AppBase app) {
        ap = app;
        HSL = null;
        localRead();
    }

    public int regRecord(int sc) {
        int res = whatPlace(sc);
        if (res > 0) {
            chemp = new HighScore(sc, (new Date()).getTime(), SettBase.youName);
            HSL.add(chemp);
            if (HSL.size() > 1) {
                Collections.sort(HSL);
            }
            if (HSL.size() > MAX_PLAYERS) {
                HSL.remove(HSL.size() - 1);
            }
            localSave();
        }
        return res;
    }

    public int whatPlace(int sc) {//0 не попал в таблицу иначе место ще 1 до MAX_PLAYERS
        for (int i = 0; i < HSL.size(); i++) {
            if (sc > HSL.get(i).getScore()) {
                return i + 1;
            }
        }
        if (HSL.size() < MAX_PLAYERS) {
            return HSL.size() + 1;
        }
        return 0;
    }

    public void localRead() {
        if (HSL == null) {
            HSL = new ArrayList<>();
        } else {
            HSL.clear();
        }

        for (int i = 0; i < MAX_PLAYERS; i++) {
            localReadOne(String.format("HiSc%1$03d", i));
        }
        if (HSL.size() > 1) {
            Collections.sort(HSL);
        }
        chemp = null;
    }

    private void localReadOne(String prefix) {
        int sc;
        long dt;
        String pl;
        sc = ap.loadSettInt(prefix + "_SCORE", 0);
        dt = ap.loadSettLong(prefix + "_DATE", 0);
        pl = ap.loadSettStr(prefix + "_NAME", "NB");
        if (dt > 0) {
            HSL.add(new HighScore(sc, dt, pl));
        }
        chemp = null;
    }

    public void localSave() {
        String prefix;
        ArrayList<String> al = new ArrayList<>();
        for (int i = 0; i < MAX_PLAYERS; i++) {
            prefix = String.format("HiSc%1$03d", i);
            al.add(prefix + "_SCORE");
            al.add(prefix + "_DATE");
            al.add(prefix + "_NAME");
        }

        if (!al.isEmpty()) {
            ap.removeStrings(al);
        }
        savePack.clear();
        for (int i = 0; i < HSL.size(); i++) {
            prefix = String.format("HiSc%1$03d", i);
            savePack.put(prefix + "_SCORE", HSL.get(i).getScore());
            savePack.put(prefix + "_DATE", HSL.get(i).getScoreDate());
            savePack.put(prefix + "_NAME", HSL.get(i).getPlayer());
        }
        ap.saveSettingsMap(savePack);
    }

    public String getReg() {
        if (chemp != null) {
            return chemp.getString();
        }
        return null;
    }

    public String getTable() {
        String res = "";
        int i = 0;
        for (HighScore HS : HSL) {
            res += "\n";
            res += HS.getString();
            i++;
        }
        if (res.isEmpty()) res="NO records";
        return res;
    }
}
