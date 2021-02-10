/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yss1.lib_jm;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 *
 * @author ys
 */
public class NetPacket {

    public static enum PType {

        EMPTY('_'),
        RASKLAD('R'),
        TORG_NEED('T'),
        TORG_MAKE('O'),
        MOVE_NEED('N'),
        MOVE_MAKE('M'),
        PARTY_FINISHED('P'),
        GAME_FINISHED('G'),
        NEXT_PARTY('S'),
        NEXT_GAME('E'),
        DESK_CLEARED('X'),
        IM_LEAVE('L'),
        IM_PAUSED('Q'),
        IM_ALIVE('A'),//AFTER PAUSE
        INFO_ALIVE('I'),//INFO about alived users
        PARTY_NEEDREPLY('Y'),
        RASKLAD_RESULT('W'),
        PRIKUP_RESULT('F');
        private char myLetter;

        PType(char mc) {
            myLetter = mc;
        }

        public char getLetter() {
            return myLetter;
        }

        public static PType getPT(char c) {
            switch (c) {
                case 'R':
                    return RASKLAD;
                case 'N':
                    return MOVE_NEED;
                case 'M':
                    return MOVE_MAKE;
                case 'P':
                    return PARTY_FINISHED;
                case 'G':
                    return GAME_FINISHED;
                case 'S':
                    return NEXT_PARTY;
                case 'E':
                    return NEXT_GAME;
                case 'X':
                    return DESK_CLEARED;
                case 'L':
                    return IM_LEAVE;
                case 'Q':
                    return IM_PAUSED;
                case 'A':
                    return IM_ALIVE;
                case 'I':
                    return INFO_ALIVE;
                case 'T':
                    return TORG_NEED;
                case 'O':
                    return TORG_MAKE;
                case 'Y':
                    return PARTY_NEEDREPLY;    
                case 'W':
                    return RASKLAD_RESULT;        
                 case 'F':
                    return PRIKUP_RESULT;           

            }
            return EMPTY;
        }
    }
    private PType packetType;
    private StringBuilder buffer;
    private final Set<String> contragents = new LinkedHashSet<>();
    private boolean multicast;

    public NetPacket() {
        buffer = new StringBuilder();
        packetType = PType.EMPTY;
    }

    public boolean isMulticast() {
        return multicast;
    }

    public void setMulticast(boolean multicast) {
        this.multicast = multicast;
    }

    public PType getPacketType() {
        return packetType;
    }

    public void setPacketType(PType packetType) {
        this.packetType = packetType;
    }

    public void setData(String s) {
        buffer.setLength(0);
        buffer.append(s);
    }

    public void setData(StringBuilder s) {
        buffer = s;
    }

    public String getContragent() {
        synchronized (contragents) {
            if (!contragents.isEmpty()) {
                Iterator<String> itr = contragents.iterator();
                return itr.next();
            }
        }
        return "";
    }

    public void clearContragents() {
        synchronized (contragents) {
            contragents.clear();
        }
    }

    public void addContragent(String rcp) {
        synchronized (contragents) {
            contragents.add(rcp);
        }
    }

    public void removeContragent(String rcp) {
        synchronized (contragents) {
            contragents.remove(rcp);
        }
    }

    public boolean haveContragents() {
        synchronized (contragents) {
            return !contragents.isEmpty();
        }
    }

    public String getContent() {
        return buffer.toString();
    }

    /**
     * pack NetPacket data in RAW [] bytes for send
     *
     * @return
     */
    public byte[] getData() {
        return (packetType.getLetter() + buffer.toString()).getBytes();
    }

    /**
     * populate NetPacket when received RAW [] bytes
     *
     * @param bf received data
     * @return
     */
    public String setData(byte[] bf) {
        buffer.setLength(0);
        buffer.append(new String(bf));
        if (buffer.length() < 1) {
            packetType = PType.EMPTY;
            return "";
        }
        packetType = PType.getPT(buffer.charAt(0));
        buffer.deleteCharAt(0);
        return buffer.toString();
    }
}
