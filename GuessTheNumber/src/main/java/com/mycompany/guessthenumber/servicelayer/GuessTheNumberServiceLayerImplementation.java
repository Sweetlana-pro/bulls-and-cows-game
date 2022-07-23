/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.guessthenumber.servicelayer;

import com.mycompany.guessthenumber.data.GuessTheNumberDao;
import com.mycompany.guessthenumber.models.Game;
import com.mycompany.guessthenumber.models.Round;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Sweetlana Protsenko
 */
@Component
public class GuessTheNumberServiceLayerImplementation implements GuessTheNumberServiceLayer {
    
    GuessTheNumberDao dao;
      
    Random rd;
    
    @Autowired
    public GuessTheNumberServiceLayerImplementation(GuessTheNumberDao dao){
        this.dao = dao;
        rd = new Random();
    }
    
        
    //create new game
    @Override
    public int beginNewGame() {
        Game game = new Game();
        game.setAnswer(answerNumGenerator());
        game.setIsFinished(false);
        return dao.addGame(game).getGameID();
    }
    
    //getting list of games by gameID
    @Override
    public List<Game> getAllGames() {
        List<Game> games = dao.getAllGames();
        games.stream().filter(game -> (! game.isIsFinished())).forEachOrdered(game -> {
            game.setAnswer("****");
        });
        return games;
        //return dao.getAllGames().stream().map((i)->hideAnswer(i)).collect(Collectors.toList());
    }
    
    //display all rounds of the game by gameID
    @Override
    public List<Round> getRounds(int gameID)throws NoGameException {
        Game game = dao.getGame(gameID);
        if (game == null){
            throw new NoGameException("Error: No game with such Id found.");
        }
        return dao.getRounds(gameID);
    }
    
    //getting a game ID
    @Override
    public Game getGame(int gameID) throws NoGameException {
        Game game = dao.getGame(gameID);
        if (game == null){
            throw new NoGameException("Error: No game with such Id found.");
        }
        if(!game.isIsFinished()) {
            game.setAnswer("****");
        }
        return game;
        
    }
        
    //randomly generate a 4 digits number
    public String answerNumGenerator() {
        Random rnd = new Random();
        int digit1 = rnd.nextInt(10);

        int digit2 = rnd.nextInt(10);
        while (digit2 == digit1) {
            digit2 = rnd.nextInt(10);
        }

        int digit3 = rnd.nextInt(10);
        while (digit3 == digit2 || digit3 == digit1) {
            digit3 = rnd.nextInt(10);
        }

        int digit4 = rnd.nextInt(10);
        while (digit4 == digit3 || digit4 == digit2 || digit4 == digit1) {
            digit4 = rnd.nextInt(10);
        }

        String answer = String.valueOf(digit1) + String.valueOf(digit2)
                + String.valueOf(digit3) + String.valueOf(digit4);

        return answer;
        /*Set<Integer> set = new HashSet<>();
        while (set.size() < 4) {
            set.add(rd.nextInt(10));
        }
        String ans = "";
        for(Integer x : set) {
            ans += x;
        }

        System.out.println("Generated Answer: " + ans);

        return ans;*/
        
    }

    // calculate a result String based on answer and guess
    @Override
    public String guessResult(String answer, String guess) {
        
        char[] guessChars = guess.toCharArray();
        char[] answerChars = answer.toCharArray();
        int exact = 0;
        int partial = 0;
        
        for (int i = 0; i < guessChars.length; i++) {
            if (answer.indexOf(guessChars[i]) > -1) {
                if (guessChars[i] == answerChars[i]) {
                    exact++;
                } else {
                    partial++;
                }
            }
        }
        
        String result = "e:" + exact + ":p:" + partial;
        
        return result;
    }   

        
//guessing, in case of the guess meets the answer, the status updates to finished
    @Override
    public Round makeGuess(Round round) throws InvalidGuessException, NoGameException{
        String answer = dao.getGame(round.getGameID()).getAnswer();
        String guess = round.getGuess();
        String result = guessResult(guess, answer);
        round.setResult(result);
        
        if (guess.equals(answer)) {
            Game game = getGame(round.getGameID());
            if (game == null) {
                throw new NoGameException("Error: No game with such Id found.");
            }
            game.setIsFinished(true);
            dao.updateGame(game);
        }
        
        
        //throwing an exception if a guess contains other characters (not digits)
        if (!containsOnlyFourDigits(guess)) {
            throw new InvalidGuessException("Error: your guess can contain only 4 digits!");
        }
        //throwing an exception is a guess has duplicate digits
        if (!isDigitUnique(guess)) {
            throw new InvalidGuessException("Error: your guess can't have duplicate digits!");                  
        }
        return dao.addRound(round);
    }
    
    //Checking if every digit in guess is unique
    private boolean isDigitUnique (String input) {
        
        for (int i = 0; i < input.length(); i++)
            for (int j = i + 1; j < input.length(); j++)
                if (input.charAt(i) == input.charAt(j))
                    return false;

        return true;
    }
    
    //checking if it contains 4 digits
    private boolean containsOnlyFourDigits(String guess) {
        if (guess.matches("[0-9]+") && guess.length() == 4) {
            return true;
        }
        return false;
    }
    
}
