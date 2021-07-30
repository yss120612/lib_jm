/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yss1.lib_jm;

import com.yss1.lib_jm.NetPacket.PType;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *
 * @author ys
 */
public class Qnet {
    private final ConcurrentLinkedQueue<NetPacket> forSnd = new  ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<NetPacket> forRcv =  new  ConcurrentLinkedQueue<>();
    private boolean succSend;
    private int resendCounter;
    QnetListiner ap;

    public Qnet(QnetListiner a) {
        succSend = true;
        ap = a;
    }

    public void reset() {
        NetPacket NP = forSnd.poll();
        while (NP != null) {
            freePacket(NP);
            NP = forSnd.poll();
        }
        NP = forRcv.poll();
        while (NP != null) {
            freePacket(NP);
            NP = forRcv.poll();
        }
        succSend = true;
    }

    private NetPacket getPacket() {
        return ToolsBase.vPool.getNPacket();
    }

    private void freePacket(NetPacket np) {
        ToolsBase.vPool.freeNPacket(np);
    }

    public void packetSend(PType pt, String D) {
        try
        {
        NetPacket NP = getPacket();
        NP.set_send_to(NetPacket.AddressType.ALLNOTME);
        NP.setContentType(pt);
        NP.setContent(D);
        processSendPacket(NP);
        }
        catch (Exception Ex)
    {
        ap.writeAndroidLog("In packetSend1 "+Ex.getMessage());
    }
    }

    public void packetSend(PType pt, StringBuilder D) {
        try
        {
        NetPacket NP = getPacket();
        NP.set_send_to(NetPacket.AddressType.ALLNOTME);
        NP.setContentType(pt);
        NP.setContent(D);
        processSendPacket(NP);
        }
        catch (Exception Ex)
    {
        ap.writeAndroidLog("In packetSend2 "+Ex.getMessage());
    }
    }
//
//    public void packetOneSend(PType pt, String D, String rcp) {
//        try
//        {
//        NetPacket NP = getPacket();
//        NP.setMulticast(false);
//        NP.clearContragents();
//        NP.addContragent(rcp);
//        NP.setPacketType(pt);
//        NP.setData(D);
//        processSendPacket(NP);
//        }
//    catch (Exception Ex)
//    {
//        ap.writeAndroidLog("In packetOneSend "+Ex.getMessage());
//    }
//    }
//
    
    private void processSendPacket(NetPacket NP) {
        forSnd.offer(NP);
        send();
    }


    private void send() {
        try {
            if (!succSend) {
                return;
            }
            NetPacket NP = forSnd.peek();
            if (NP == null) {
                return;
            }
            
            succSend = false;
            resendCounter = 0;

//            if (NP.isMulticast()) {
//                NP.clearContragents();
//                ap.send2All(NP);
//            } else {
//                ap.send2One(NP.getContragent(), NP);
//            }
            ap.sendPacket(NP);
        } catch (Exception Ex) {
            ap.writeAndroidLog("In send " + Ex.getMessage());
        }
    }
    
    //обрабатываем ошибку отправки. пересылаем пакет еще раз
    public void reSend(String id) {
        try {
            if (++resendCounter > 2) {//три попытки и хорош
                ap.sendAllAttemptsError(id);
                return;
            }
            
            NetPacket NP=forSnd.peek();

            if (NP != null) {
                //ap.send2One(id, NP);
            }
        } catch (Exception Ex) {
            ap.writeAndroidLog("In reSend " + Ex.getMessage());
        }
    }
     
    //вызывается когда пришло уведомление о вручении от всех партнера
    public void sucessSend(String pID) {
        
        try {
            NetPacket NP;
            synchronized (forSnd) {
                NP = forSnd.peek();
                if (NP == null) {
                    return;
                }
//                NP.removeContragent(pID);
//                if (NP.haveContragents()) {
//                    return;
//                }
                forSnd.remove(NP);
            }
            PType pt=NP.getContentType();
            freePacket(NP);
            succSend = true;
            if (pt == PType.IM_LEAVE) {//I leave game and succ send to other
                ap.gameLeaved();
            } else if (pt == PType.IM_PAUSED) {//I switch to other app or quit
                ap.gamePaused();
            }
            send();
        } catch (Exception Ex) {
            ap.writeAndroidLog("In successSend " + Ex.getMessage());
        }
    }

    //вызывается когда приходит пакет
    public void receivePacket(String pID) {
        NetPacket NP = getPacket();
        NP.makePacket(pID);
        //NP.setData(dt);
        //NP.clearContragents();
        //NP.addContragent(pID);
        forRcv.offer(NP);
    }

    //вызывается когда дошли ркуи обработать очередь пакетов
    public NetPacket beginRead() {
        NetPacket NP = forRcv.poll();
        return NP;
    }

    public void finishRead(NetPacket NP) {
        freePacket(NP);
    }
    
    public boolean hasReceived()
    {
        return !forRcv.isEmpty();
    }
    
    @Override
    public String toString() {
        return "Qnet{" + "Snd length=" + forSnd + ", Rcv length=" + forRcv + '}';
    }
    
}
