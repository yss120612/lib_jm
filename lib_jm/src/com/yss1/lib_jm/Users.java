/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yss1.lib_jm;

import java.util.ArrayList;

/**
 * `
 *
 * @author ys
 */
public class Users {

    private ArrayList<Character> usersQueue;
    private ArrayList<UserBase> uList = new ArrayList<>();
    private int shift;
    private int ya;//мой индекс в массиве юзеров
    private int nUsers;

    public Users(ArrayList<Character> uq) {
        //uList = null;
        usersQueue = uq;
        ya = -1;
        nUsers = uq.size();
    }

    public void initLocalUsers(ArrayList<String> usl) {
        if (usl.size() != nUsers) {
            throw new RuntimeException("Incorrect Array size in Users.initLocalUsers");
        }
        ya = 0;
        shift = 0;

        UserBase ub;
        if (uList==null)
        {
            System.out.println("ulist is NULL!");
        }
        uList.clear();
        for (int i = 0; i < usersQueue.size(); i++) {
            ub = new UserBase(usl.get(i), usersQueue.get(i) + "", i == 0);
            ub.setUtype(i == 0 ? UserBase.UserType.MAN : UserBase.UserType.CPU);
            uList.add(ub);
        }
    }

    public boolean allActive() {
        for (UserBase u : uList) {
            if (!u.isActive()) {
                return false;
            }
        }
        return true;
    }

    public String getNAusers() {
        String res = "";
        for (UserBase u : uList) {
            if (!u.isActive()) {
                if (!res.isEmpty()) {
                    res += ",";
                }
                res += u.getUid();
            }
        }
        return res;
    }
    
     public boolean isServerActive() {
        for (UserBase u : uList) {
            if (u.isServer()) {
                return u.isActive();
            }
        }
        return false;
    }
   
     
    public boolean isImLowActive() {
        for (UserBase u : uList) {
            if (u.isActive()) {
                return u.isI();
                
            }
        }
        return false;
    }
     
    
    public ArrayList<UserBase> getuList() {
        return uList;
    }

    public void setuList(ArrayList<UserBase> uList) {
       
        if (uList==null || uList.size() != nUsers) {
            throw new RuntimeException("Incorrect Array size in Users.setuList");
        }
        this.uList = uList;
        reorderI();
        shift = getShiftFromServer();
        if (shift >= uList.size()) {
            shift = 0;
        }
    }

    public void setServer(String id) {
        for (UserBase u : uList) {
            u.setServer(u.getUid().equals(id));
        }
        shift = getShiftFromServer();
        if (shift >= uList.size()) {
            shift = 0;
        }
    }

    public void setNewServer(String id) {
        for (UserBase u : uList) {
            u.setNewServer(u.getUid().equals(id));
        }
    }
    
    private void reorderI() {
        if (uList == null || uList.isEmpty()) {
            return;
        }
        for (int j = 0; j < uList.size(); j++) {
            if (uList.get(j).isI()) {
                ya = j;
                break;
            }
        }
    }

    public UserBase getUser(String id) {
        for (UserBase u : uList) {
            if (u.getUid().equals(id)) {
                return u;
            }
        }
        return new UserBase("Anonimous", "0", false);
    }

    public int getUserIdx(String id) {
        
        for (int i=0;i<uList.size();i++) {
            if (uList.get(i).getUid().equals(id)) {
                return i;
            }
        }
        return -1;
    }
    
    public UserBase getUser(char W) {
        int idx = usersQueue.indexOf(W);
        if (idx < 0 || ya < 0) {
            throw new RuntimeException("User " + W + " not found at Users.getUser");
        }
        idx = (idx + ya) % uList.size();
        return uList.get(idx);
    }

    public char getCharFor(String id) {
        if (ya<0) return 'N';
        int idx=getUserIdx(id);
        if (idx<0) return 'N';
        System.out.println("id="+id+" idx="+idx+" ya="+ya+" found="+(idx+ya)%usersQueue.size()+" uQ="+usersQueue.toString());
        return usersQueue.get((usersQueue.size()+idx-ya)%usersQueue.size());
    }
    
    public char getS2I(char W) {
        return getLetter(W, true);
    }

    public char getI2S(char W) {
        return getLetter(W, false);
    }

    private char getLetter(char W, boolean plus) {
        int idx = usersQueue.indexOf(W);
        if (idx < 0) {
            return 'N';
        }
        idx += shift * (plus ? 1 : -1);
        int L = usersQueue.size();
        idx = (idx + L) % L;
        return usersQueue.get(idx);
    }

    private int getShiftFromServer() {
        int s = getServerIdx();
        
        if (s == 10 || ya<0) {
            return 10;
        }
        return s - ya;
    }

    private int getServerIdx()
    {
        int k=0;
        for (UserBase u : uList) {
            if (u.isServer()) {
                return k;
            }
            k++;
        }
        return 10;
    }
    
    public boolean isIServer() {
        return getI().isServer();
    }

    public boolean isINewServer() {
        return getI().isNewServer();
    }
    
    public UserBase getI() {
        if (ya < 0) {
            throw new RuntimeException("Not find myself in Users.getI");
        }
        return uList.get(ya);
    }

    @Override
    public String toString() {
        StringBuilder SB = new StringBuilder();
        SB.append("\n");
        for (UserBase u : uList) {
            SB.append("ID=");
            SB.append(u.getUid());
            SB.append(" NAME=");
            SB.append(u.getName());
            SB.append(" IS I=");
            SB.append(u.isI());
            SB.append(" SERVER=");
            SB.append(u.isServer());
            SB.append(" NEW SERVER=");
            SB.append(u.isNewServer());
            SB.append(" Active=");
            SB.append(u.isActive());
            SB.append(" ya=");
            SB.append(ya);
            SB.append(" srvIdx=");
            SB.append(getServerIdx());
            SB.append("\n");
        }
        return SB.toString();
    }

    public void setActivity(String list) {
        for (UserBase u : uList) {
            u.setActive(!list.contains(u.getUid()));
        }
    }
}
