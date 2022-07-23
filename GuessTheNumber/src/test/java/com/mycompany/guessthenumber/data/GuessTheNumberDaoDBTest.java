/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.guessthenumber.data;

//import com.mycompany.guessthenumber.data.GuessTheNumberDaoDB.RoundMapper;
import com.mycompany.guessthenumber.models.Game;
import com.mycompany.guessthenumber.models.Round;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * @author Sweetlana Protsenko
 */
//@RunWith(SpringRunner.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class GuessTheNumberDaoDBTest {
    
    @Autowired
    private GuessTheNumberDaoDB dao;
    
    public GuessTheNumberDaoDBTest() {
    }

    @BeforeAll
    public static void setUpClass() throws Exception {
    }

    @AfterAll
    public static void tearDownClass() throws Exception {
    }

    @BeforeEach
    public void setUp() throws Exception {
        
        List<Game> games = dao.getAllGames();
        System.out.println(games.size());
        for (Game game : games) {
            dao.deleteGame(game.getGameID());
        }
        
        List<Round> rounds = dao.readAll();
        for (Round round : rounds) {
            dao.deleteRoundById(round.getRoundID());
        }

    }

    @AfterEach
    public void tearDown() throws Exception {
    }
    
    /**
     * Test of addRound method, of class GuessTheNumberDaoDB.
     */
    @Test
    public void testAddRound() {
        Game game = new Game();
        game.setAnswer("1234");
        game.setIsFinished(false);
        game=dao.addGame(game);

        Round round = new Round();
        round.setGameID(game.getGameID());
        round.setGuess("1234");
        round.setTime(LocalDateTime.now().withNano(0));
        round.setResult("e:4:p:0");
        round = dao.addRound(round);

        Round fromDAO = dao.getRoundById(round.getRoundID());

        assertEquals(round, fromDAO);
    }

    /**
     * Test of addGame method, of class GuessTheNumberDaoDB.
     */
    @Test
    public void testAddGetGame() {
        Game game = new Game();
        game.setAnswer("1234");
        game.setIsFinished(false);
        game = dao.addGame(game);

        Game fromDAO = dao.getGame(game.getGameID());

        assertEquals(game, fromDAO);
    }
    

    /**
     * Test of getGame method, of class GuessTheNumberDaoDB.
     */
    @Test
    public void testReadByIDNoGame() {
        Game game = new Game();
        game.setAnswer("1234");
        game.setIsFinished(false);

        Game fromDAO = dao.getGame(game.getGameID());
        assertNull(fromDAO);
    }


    /**
     * Test of getRounds method, of class GuessTheNumberDaoDB.
     */
    @Test
    public void testGetRounds() {
        Game game1 = new Game();
        game1.setAnswer("1234");
        game1.setIsFinished(false);
        Game testGame1 = dao.addGame(game1);

        Round round = new Round();
        round.setTime(LocalDateTime.now().withNano(0));
        round.setGameID(testGame1.getGameID());
        round.setGuess("4321");
        round.setResult("e:0;p:4");
        Round testRound = dao.addRound(round);
        Round daoRound = dao.getRoundById(testRound.getRoundID());


        Round round2 = new Round();
        round2.setTime(LocalDateTime.now().withNano(0));
        round2.setGameID(testGame1.getGameID());
        round2.setGuess("1235");
        round2.setResult("e:1;p:3");
        Round testRound2 = dao.addRound(round2);
        Round daoRound2 = dao.getRoundById(testRound2.getRoundID());


        List<Round> roundList = dao.getRounds(testGame1.getGameID());
        assertEquals(2, roundList.size());
        assertTrue(roundList.contains(round));
        assertTrue(roundList.contains(round2));
    }

    /**
     * Test of getAllGames method, of class GuessTheNumberDaoDB.
     */
    @Test
    public void testGetAllGames() {
        Game game = new Game();
        game.setAnswer("1234");
        game.setIsFinished(false);
        dao.addGame(game);

        Game game2 = new Game();
        game2.setAnswer("5678");
        game2.setIsFinished(false);
        dao.addGame(game2);

        List<Game> games = dao.getAllGames();

        assertEquals(2, games.size());
        assertTrue(games.contains(game));
        assertTrue(games.contains(game2));
    }
    

    /**
     * Test of updateGame method, of class GuessTheNumberDaoDB.
     */
    @Test
    public void testUpdateGame() {
        Game game = new Game();
        game.setAnswer("1234");
        game.setIsFinished(false);
        dao.addGame(game);

        Game fromDAO = dao.getGame(game.getGameID());

        assertEquals(game, fromDAO);

        game.setIsFinished(true);

        dao.updateGame(game);

        assertNotEquals(game, fromDAO);

        fromDAO = dao.getGame(game.getGameID());

        assertEquals(game, fromDAO);
    }

    /**
     * Test of deleteGame method, of class GuessTheNumberDaoDB.
     */
    @Test
    public void testDeleteGame() {
        Game game = new Game();
        game.setAnswer("1234");
        game.setIsFinished(false);
        dao.addGame(game);

        dao.deleteGame(game.getGameID());

        Game fromDAO = dao.getGame(game.getGameID());

        assertNull(fromDAO);
    }
    @Test
    void getRoundById() {
        Game game1 = new Game();
        game1.setAnswer("1234");
        game1.setIsFinished(false);
        Game testGame1 = dao.addGame(game1);
        
        Round round = new Round();
        round.setTime(LocalDateTime.now().withNano(0));
        round.setGameID(testGame1.getGameID());
        round.setGuess("4321");
        round.setResult("e:0;p:4");
        Round testRound = dao.addRound(round);
        Round daoRound = dao.getRoundById(testRound.getRoundID());

        Round round2 = new Round();
        round2.setTime(LocalDateTime.now().withNano(0));
        round2.setGameID(testGame1.getGameID());
        round2.setGuess("1235");
        round2.setResult("e:1;p:3");
        Round testRound2 = dao.addRound(round2);
        Round daoRound2 = dao.getRoundById(testRound2.getRoundID());

        assertEquals(testRound, daoRound);
        assertEquals(testRound2, daoRound2);
    }
    
}
