/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.guessthenumber.servicelayer;

import com.mycompany.guessthenumber.models.Game;
import com.mycompany.guessthenumber.models.Round;
import java.util.List;

/**
 *
 * @author Sweetlana Protsenko
 */
public interface GuessTheNumberServiceLayer {
    
    
    int beginNewGame ();
    List<Game> getAllGames();
    List<Round> getRounds(int gameID) throws NoGameException;
    Round makeGuess(Round round)throws InvalidGuessException, NoGameException;
    Game getGame(int gameID) throws NoGameException;
    
    //String answerNumGenerator();
    String guessResult(String answer, String guess);
    //Game hideAnswer(Game game);
    
}
