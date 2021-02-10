/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yss1.lib_jm;

import com.jme3.scene.Node;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ys
 */
public class CardSetBase {

    protected CardAnimatorBase animator;
    protected Node myNode;
    protected List<Card> cards = new LinkedList<>();
    protected int points;
    
    private int iAdd=0,iClear=0,iDel=0;
    
    public CardSetBase(RessKeeper a, Node n) {
        
        animator = a.getAnimator();
        myNode = n;
    }

   
    
    public Node getNode() {
        return myNode;
    }

    public List<Card> getCards() {
        return cards;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void addPoints(int dp) {
        this.points += dp;
    }

    public void rotate(boolean xy) {
        for (Card cr:cards) {
            //cards.get(i).setIdleAnim();
            //cr.rotate();
            cr.applyEndpoint();
            cr.setRotatedEndpoint(xy);
            animator.animate3endpointInv(cr, SettBase.animTime, 12, "Rotate", 0, 0, 1f);
            
            //cr.applyEndpoint();
            //cards.get(i).applyEndpoint();
        }
    }

    public Map<String, Object> forSaveCards(Map<String, Object> DCT, char IM) {
        if (DCT == null) {
            DCT = new HashMap<>();
        }
        for (Card cr : cards) {
            DCT.put(cr.getName(), cr.getStateStr(IM));
        }
        return DCT;
    }

    public StringBuilder forNetSend(StringBuilder S, char IM) {
        if (S == null) {
            S = new StringBuilder(700);
        }
        for (Card cr : cards) {
            if (S.length() > 0) {
                S.append(SettBase.netSeparator);
            }
            S.append(cr.getNetStr(IM));
        }
        return S;
    }

    public void add(Card c) {
        if (cards.add(c)) {
            iAdd++;
            myNode.attachChild(c.getGe());
        }
    }

    public Card find(int r, int s) {
        for (Card c : cards) {
            if (c.getSuit() == s && c.getRank() == r) {
                return c;
            }
        }
        return null;
    }

    public Card find(String nm) {
        for (Card c : cards) {
            if (c.getName().equals(nm)) {
                return c;
            }
        }
        return null;
    }

    public Card findIdle(String nm) {
        Card c = find(nm);
        if (c != null && c.isIdle()) {
            return c;
        }
        return null;
    }

    public boolean isMy(String nm) {
        for (Card c : cards) {
            if (c.getName().equals(nm)) {
                return true;
            }
        }
        return false;
    }

    public Card remove(int r, int s) {
        iDel++;
        return remove(find(r, s));
    }

    public Card remove(Card c) {
        iDel++;
        return remove(cards.indexOf(c));
    }

    public Card remove(int idx) {
        if (idx < 0 || idx >= cards.size()) {
            return null;
        }
        iDel++;
        Card c = cards.get(idx);
        myNode.detachChild(c.getGe());
        cards.remove(idx);
        return c;
    }

    public Card removeLast() {
        return remove(cards.size()-1);
    }
    
    
    public Card get(int idx) {
        if (idx < 0 || idx >= cards.size()) {
            return null;
        }

        return cards.get(idx);
    }

    public Card getLast() {
        if (cards.isEmpty()) {
            return null;
        }
        return cards.get(cards.size()-1);
    }
    
    public Card getFirst() {
        if (cards.isEmpty()) {
            return null;
        }
        return cards.get(0);
    }
    
    public int length() {
        return cards.size();
    }

    public void clear() {
        iClear++;
        while (cards.size()>0)
        {
            removeLast();
        }
        //cards.clear();
        //myNode.detachAllChildren();
    }

    protected void sort() {
        Collections.sort(cards);
    }

    public void sortByWeight() {
        Comparator<Card> comparator = new Comparator<Card>() {
            @Override
            public int compare(Card c1, Card c2) {
                return c1.getWeight() - c2.getWeight(); // use your logic
            }
        };
        Collections.sort(cards, comparator);
    }

    public void setWeightToN() {
        int i = 0;
        for (Card cr : cards) {
            cr.setWeight(i++);
        }
    }
    
    
    public String writeDiag() {
        StringBuilder sb = new StringBuilder();
            sb.append("[L:");
            sb.append(cards.size());
            sb.append("a:");
            sb.append(iAdd);
            sb.append("d:");
            sb.append(iDel);
            sb.append("c:");
            sb.append(iClear);
            sb.append("]");
        if (cards.isEmpty()) {
            sb.append("EMPTY\n");
        } else {
            
            for (int i = 0; i < cards.size(); i++) {
                sb.append(cards.get(i).getName());
                if (i < cards.size() - 1) {
                    sb.append(" ,");
                } else {
                    sb.append("\n");
                }

            }
        }
        return sb.toString();
    }
    
}
