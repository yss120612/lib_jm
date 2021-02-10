/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yss1.lib_jm;

/**
 *
 * @author ys
 */
public interface IGameRules {
public String getPacketRules();
public void applyPacketRules(String as);
public void backupRules();
public void restoreRules();
public void commit(boolean checkChanges);
}
