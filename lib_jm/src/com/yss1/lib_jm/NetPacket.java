/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yss1.lib_jm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author ys
 */
public class NetPacket {

    private ArrayList<String> arrayList;

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

    public static enum AddressType {
        NOBODY('N'),    //никто
        ALLNOTME('A'),  //все кроме меня
        LEFT_USER('L'),
        UPPER_USER('U'),
        RIGHT_USER('R'),
        DOWN_USER('D');
        private char _letter;

        AddressType(char mc) {
            _letter = mc;
        }

        public char getLetter() {
            return _letter;
        }

        public static AddressType getAddressType(char c) {
            switch (c) {
                case 'A':
                    return ALLNOTME;
                case 'L':
                    return LEFT_USER;
                case 'U':
                    return UPPER_USER;
                case 'R':
                    return RIGHT_USER;
                case 'D':
                    return DOWN_USER;
            }
            return NOBODY;
        }
    }



    private PType contentType;
    private AddressType _sender;
    private AddressType _send_to;
    private StringBuilder data;

    public AddressType get_sender() {
        return _sender;
    }

    public AddressType get_send_to() {
        return _send_to;
    }


    public NetPacket() {
        data = new StringBuilder();
        contentType = PType.EMPTY;
        _send_to = AddressType.NOBODY;
        _sender = AddressType.NOBODY;
    }

    //public boolean isMulticast() {
    //   return _send_to==RecvType.ALLNOTME;
    //}

    //public void setMulticast(boolean multicast) {
    //    _send_to==RecvType.ALLNOTME
    //}

    public void set_sender(AddressType _sender) {
        this._sender = _sender;
    }

    public void set_send_to(AddressType _send_to) {
        this._send_to = _send_to;
    }

    public PType getContentType() {
        return contentType;
    }

    public void setContentType(PType contentType) {
        this.contentType = contentType;
    }

    public void setContent(String s) {
        data.setLength(0);
        data.append(s);
    }

    public void setContent(StringBuilder s) {
        data = s;
    }

    public void appendContent(String s) {
        data.append(s);
    }

    public String getContent() {
        return data.toString();
    }

    /**
     * pack NetPacket data in RAW [] bytes for send
     *
     * @return
     */
    public String prepare2sendPacket() {
        return "{CT=" + contentType.getLetter() + " ,RCPT=" + _send_to.getLetter() + " ,SENDER=" + _sender.getLetter() + " ,DATA=" + data.toString() + "}";
    }

    /**
     * populate NetPacket when received RAW [] bytes
     *
     * @return
     */
    public void makePacket(String s) {
        List<String> al = Stream.of(s.replaceAll("[\\{,\\}]", "").split("\\,=")).collect(Collectors.toList());
        al.forEach(String::trim);
        for (int i = 0; i < al.size(); i += 2) {
            if (al.get(i).equals("CT") && i + 1 < al.size())
                contentType = PType.getPT(al.get(i + 1).charAt(0));
            else if (al.get(i).equals("RCPT") && i + 1 < al.size())
                _send_to = AddressType.getAddressType(al.get(i + 1).charAt(0));
            else if (al.get(i).equals("SENDER") && i + 1 < al.size())
                _sender = AddressType.getAddressType(al.get(i + 1).charAt(0));
            else if (al.get(i).equals("DATA") && i + 1 < al.size()) setContent(al.get(i + 1));
        }

    }
}
