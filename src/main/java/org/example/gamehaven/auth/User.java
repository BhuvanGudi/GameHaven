package org.example.gamehaven.auth;

public class User {
    private final String id;
    private final String username;
    private int gamesPlayed;
    private int wins;
    private int losses;

    // Game-specific stats
    private int tttWins;
    private int tttLosses;
    private int tttDraws;

    private int c4Wins;
    private int c4Losses;
    private int c4Draws;

    private int checkersWins;
    private int checkersLosses;
    private int checkersDraws;

    public User(String id, String username) {
        this.id = id;
        this.username = username;
    }

    // Getters and setters for all fields
    public String getId() { return id; }
    public String getUsername() { return username; }
    public int getGamesPlayed() { return gamesPlayed; }
    public int getWins() { return wins; }
    public int getLosses() { return losses; }
    public int getTttWins() { return tttWins; }
    public int getTttLosses() { return tttLosses; }
    public int getTttDraws() { return tttDraws; }
    public int getC4Wins() { return c4Wins; }
    public int getC4Losses() { return c4Losses; }
    public int getC4Draws() { return c4Draws; }
    public int getCheckersWins() { return checkersWins; }
    public int getCheckersLosses() { return checkersLosses; }
    public int getCheckersDraws() { return checkersDraws; }

    public void incrementWins() { wins++; gamesPlayed++; }
    public void incrementLosses() { losses++; gamesPlayed++; }
    public void incrementTttWins() { tttWins++; }
    public void incrementTttLosses() { tttLosses++; }
    public void incrementTttDraws() { tttDraws++; }
    public void incrementC4Wins() { c4Wins++; }
    public void incrementC4Losses() { c4Losses++; }
    public void incrementC4Draws() { c4Draws++; }
    public void incrementCheckersWins() { checkersWins++; }
    public void incrementCheckersLosses() { checkersLosses++; }
    public void incrementCheckersDraws() { checkersDraws++; }
}