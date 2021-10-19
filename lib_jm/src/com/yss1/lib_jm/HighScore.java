/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yss1.lib_jm;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author ys
 */
public class HighScore implements Comparable {

    private int score;
    private Date scoreDate;
    private String player;

    public HighScore(int s, long dt, String who) {
        score = s;
        scoreDate = dt == 0 ? new Date() : new Date(dt);
        player = who;
    }

    @Override
    public int compareTo(Object o) {
        if (o == null) {
            return 1;
        }
        if (getClass() != o.getClass()) {
            return 1;
        }
        HighScore other = (HighScore) o;
        return -(Integer.valueOf(score).compareTo(other.getScore()));
        //чем больше тем выше по списку
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 11 * hash + this.score;
        hash = 11 * hash + (this.scoreDate != null ? this.scoreDate.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final HighScore other = (HighScore) obj;
        if (this.score != other.score) {
            return false;
        }
        return true;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public long getScoreDate() {
        return scoreDate.getTime();
    }

    public String getScoreDateString() {
        SimpleDateFormat ft = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return ft.format(scoreDate);
    }

    public void setScoreDate(long scoreDate) {
        this.scoreDate = new Date(scoreDate);
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public String getString() {
        return ToolsBase.getStrWithSpaces(player, 15) + ToolsBase.getStrWithSpaces(getScoreDateString(), 18) + ToolsBase.getStrWithSpacesLeft(score + "", 5);
    }
}
