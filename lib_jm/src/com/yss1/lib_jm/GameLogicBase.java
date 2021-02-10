/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yss1.lib_jm;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author ys
 */
public class GameLogicBase {
    protected HashMap<Character, CardSetBase> csets;
    protected ArrayList<Character> winners;
    protected ArrayList<Card> legalMovs;
    
    public ArrayList<Card> getLegalMovs() {
        return legalMovs;
    }
    public GameLogicBase(HashMap<Character, CardSetBase> csMAP) {
        csets = csMAP;
        winners = new ArrayList<>();
        legalMovs = new ArrayList<>();
    }

    
    
    public boolean partyIsOver() {
        return false;
    }

    public boolean gameIsOver() {
        return false;
    }

    public void initWinners() {
        
    }
    
    public ArrayList<Character> getWinners() {
        return winners;
    }

    public ArrayList<Card> refreshLegalMovies(Character Who) {
        return legalMovs;
    }

    public void clearLegalMovies() {
        legalMovs.clear();
    }
    
    public Card getMove(Character le) {
        return null;
    }

    public int getMyPoints() {
        if (csets.containsKey('D')) {
            return csets.get('D').getPoints();
        }
        return 0;
    }

   

    public int getWinnerPoints() {
        if (winners.isEmpty()) {
            return 0;
        }
        Character CH = winners.get(0);
        if (csets.containsKey(CH)) {
            return csets.get(CH).getPoints();
        }
        return 0;
    }
    
    public void newGame()
    {
        
    }
    
    public void newParty()
    {
        
    }
    
    
}
