/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.guessthenumber.controller;

import com.mycompany.guessthenumber.models.Game;
import com.mycompany.guessthenumber.models.Round;
import com.mycompany.guessthenumber.servicelayer.GuessTheNumberServiceLayer;
import com.mycompany.guessthenumber.servicelayer.InvalidGuessException;
import com.mycompany.guessthenumber.servicelayer.NoGameException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Sweetlana Protsenko
 */

@RestController
@RequestMapping("/api/guessthenumber")
public class GuessTheNumberController {
    
    private final GuessTheNumberServiceLayer service;
    
    public GuessTheNumberController(GuessTheNumberServiceLayer service) {
        this.service = service;
    }
     
        
    //Start game
    @PostMapping("/begin")       
    @ResponseStatus(HttpStatus.CREATED)
    public int begin() {
        return service.beginNewGame();
    }
   
    @PostMapping("/guess")
    public ResponseEntity<Round> makeGuess(@RequestBody Round round) throws InvalidGuessException {
        round.setTime(LocalDateTime.now());
        try {
            round = service.makeGuess(round);
        } catch (NoGameException ex) {
            Logger.getLogger(GuessTheNumberController.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(round == null){
            return new ResponseEntity(null, HttpStatus.NO_CONTENT);    
        }
        return ResponseEntity.ok(round);
    }
     
    
    @GetMapping("/game")
    public List<Game> getAllGames() {
        return service.getAllGames();
    }
    
    @GetMapping("/game/{gameID}")
    public ResponseEntity<Game> getGame(@PathVariable int gameID) throws NoGameException {
        Game game = service.getGame(gameID);
        if (game == null){        
        return new ResponseEntity (null,HttpStatus.NOT_FOUND);
        }
        if(!game.isIsFinished()) {
            game.setAnswer("Still not guessed");
        }
        return ResponseEntity.ok(game);
    }
    
        
    @GetMapping("/rounds/{gameID}")
    public List<Round> getRoundsForGame(@PathVariable("gameID") int gameID) throws NoGameException {
        return service.getRounds(gameID);
    }
   
}
