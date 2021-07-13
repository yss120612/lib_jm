/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yss1.lib_jm;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Stack;

/**
 *
 * @author ys
 */
public class Stock {

    private final List<Card> cards;
    private Stack<Card> mixed_stock;
    private int background;
    private long rasklad;
    public RessKeeper ap;
    private int stockCount;

    
    
public Stock(RessKeeper a, int n, int bk, boolean has_jockers) {//n==2,6,7,9
    this(a, n, bk,has_jockers, 1);
}
    public Stock(RessKeeper a, int n, int bk, boolean has_jockers, int nKol) {//n==2,6,7,9
        if (bk < 1 || bk > 4) {
            background = 1;
        } else {
            background = bk;
        }
        ap = a;
        cards = new ArrayList<>((15 - n) * 4 * nKol+ (has_jockers ? 2 : 0));
        mixed_stock = new Stack<>();
        Card Cr;

        ratio = (float) SettBase.CARD_H / (float) SettBase.CARD_W;
        SettBase.cardH = SettBase.cardW() * ratio;
        for (int k = 0; k < nKol; k++) {
            for (int i = n; i < 15; i++) {

                for (int j = 1; j < 5; j++) {
                    Cr = new Card(ap,k,i,j);
                    Cr.initGeometry(ap.getGameAnimClipListener(), background);
                    Cr.getGe().setMaterial(ap.getRess().getMaterial("Общий"));
                    cards.add(Cr);
                }
            }
        }

        if (has_jockers) {
            Cr = new Card(ap, "RC");
            //Cr.initGeometry(ap.getGameAEL(),  background);
            Cr.initGeometry(ap.getGameAnimClipListener(),  background);
            Cr.getGe().setMaterial(ap.getRess().getMaterial("Общий"));
            cards.add(Cr);
            Cr = new Card(ap, "RD");
            Cr.initGeometry(ap.getGameAnimClipListener(), background);
            Cr.getGe().setMaterial(ap.getRess().getMaterial("Общий"));
            cards.add(Cr);
        }


        rasklad = -1;
    }

    public int getStockCount() {
        return stockCount;
    }
    
    public void setRasklad(long rasklad) {
        this.rasklad = rasklad;
    }

    public List<Card> getCards() {
        return cards;
    }

    public ArrayList<String> getNames() {
        ArrayList<String> res = new ArrayList<>();
        for (Card c : cards) {
            res.add(c.getName());
        }
        return res;
    }
    private float ratio;

    public float getRatio() {
        return ratio;
    }

    public int getBackground() {
        return background;
    }

    public void setBackground(int background) {
        if (this.background == background) {
            return;
        }
        this.background = background;
        for (int i = 0; i < cards.size(); i++) {
            cards.get(i).setBackground(background);
        }
    }

    public long getRasklad() {
        return rasklad;
    }

    public Card getCard(int n) {
        if (cards == null || n < 0 || n >= cards.size()) {
            return null;
        }
        return cards.get(n);
    }

    public Card getCard(String n) {
        for (Card C : cards) {
            if (C.getName().equals(n)) {
                return C;
            }
        }

        return null;
    }

    public void idleAllResetWeight() {
        for (Card C : cards) {
            C.setIdleAnim();
            C.setWeight(0);
        }
    }

    public void mix(long raskl) {
        mixed_stock.clear();
        int i;
        if (cards.size() < 3) {
            return;
        }
        mixed_stock.addAll(cards);

        if (raskl == -1) {
            rasklad = (new Date()).getTime() - 40L * 365L * 24L * 60L * 60L * 1000L;//-2010-01-01 где то
        } else {
            rasklad = raskl;
        }

        Random rnd = new Random(rasklad);
        for (i = 0; i < 121; i++) {
            mixed_stock.push(mixed_stock.remove(rnd.nextInt(mixed_stock.size() - 1)));
        }
    }

    public void mix() {
        mix(-1);
    }

    public Card pop() {
        if (mixed_stock.empty()) {
            return null;
        }
        return mixed_stock.pop();
    }

    public int workSize() {
        return mixed_stock.size();
    }


}
