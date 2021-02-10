/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yss1.lib_jm;

/**
 *
 * @author ys
 */
public class UserBase implements Comparable {

    public enum UserType {

        MAN, CPU, NET
    };
    private String name;
    private String uid;
    private UserType utype;
    private boolean server;
    private boolean IsI;
    private boolean active;
    private boolean newServer;

    @Override
    public int compareTo(Object o) {
        UserBase OTHER = (UserBase) o;
        return uid.compareTo(OTHER.getUid());
    }

    public UserBase(String nm, String id, boolean I) {
        name = nm;
        uid = id;
        utype = UserType.CPU;
        server = false;
        IsI = I;
        active = true;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isI() {
        return IsI;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setIsI(boolean IsI) {
        this.IsI = IsI;
    }

    public boolean isServer() {
        return server;
    }

    public void setServer(boolean server) {
        this.server = server;
    }

    public boolean isNewServer() {
        return newServer;
    }

    public void setNewServer(boolean newServer) {
        this.newServer = newServer;
    }

    public String getName() {
        return name;
    }

    public String getShortName() {
        String[] al = name.split("\\s+");
        String result;
        if (al.length == 1 || al[0].equals("Player") || al[0].equals("Игрок")) {
            result = name;
        } else {
            result = al[1] + " " + al[0].substring(0, 1) + ".";
        }
        return result;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserType getUtype() {
        return utype;
    }

    public void setUtype(UserType utype) {
        this.utype = utype;
    }

}
