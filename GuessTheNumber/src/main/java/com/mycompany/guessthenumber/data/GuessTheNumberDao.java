/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.guessthenumber.data;

import com.mycompany.guessthenumber.models.Game;
import com.mycompany.guessthenumber.models.Round;
import java.util.List;

/**
 *
 * @author Sweetlana Protsenko
 */
public interface GuessTheNumberDao {
    
    public Game addGame(Game game);
    public Game getGame (int gameID);
    public List<Game> getAllGames();
    void updateGame(Game game);
    void deleteGame(int gameID);
    
    public Round addRound (Round round);
    public List<Round> getRounds(int gameID);
    Round getRoundById(int roundID);
    void updateRound(Round round);
    void deleteRoundById(int roundID);
    public List<Round> readAll();
}