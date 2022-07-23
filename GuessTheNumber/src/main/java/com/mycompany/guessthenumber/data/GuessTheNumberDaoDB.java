/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.guessthenumber.data;

import com.mycompany.guessthenumber.models.Game;
import com.mycompany.guessthenumber.models.Round;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

//import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Sweetlana Protsenko
 */
@Repository
public class GuessTheNumberDaoDB implements GuessTheNumberDao {
    
    private final JdbcTemplate jdbcTemplate;
    
    @Autowired
    JdbcTemplate jdbc;
    
    public  GuessTheNumberDaoDB (JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    } 
   
    public static final class GameMapper implements RowMapper<Game> {
        @Override
        public Game mapRow(ResultSet rs, int i) throws SQLException {
            Game game = new Game();
            game.setGameID(rs.getInt("gameID"));
            game.setAnswer(rs.getString("answer"));
            game.setIsFinished(rs.getBoolean("isFinished"));
            return game;
        }
    }

    final class RoundMapper implements RowMapper<Round> {
        @Override
        public Round mapRow(ResultSet rs, int i) throws SQLException {
            Round round = new Round();
            round.setRoundID(rs.getInt("roundID"));
            round.setGameID(rs.getInt("gameID"));
            round.setGuess(rs.getString("guess"));
            round.setTime(rs.getTimestamp("time").toLocalDateTime());
            round.setResult(rs.getString("result"));
            return round;
            
        }
    }
    
    @Override
    @Transactional
    public Round addRound(Round round) {
        final String sql = "INSERT INTO Round(gameID, guess, time, result) VALUES (?, ?, ?, ?);";
        jdbcTemplate.update(sql,
                round.getGameID(),
                round.getGuess(),
                Timestamp.valueOf(round.getTime()),//round.getTime(),   
                round.getResult());
        int roundID = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        round.setRoundID(roundID);
        return round;
    }    

    @Override
    @Transactional
    public Game addGame(Game game) {
        final String INSERT_GAME = "INSERT INTO Game(answer, isFinished) VALUES(?,?);";
        jdbcTemplate.update(INSERT_GAME, 
                game.getAnswer(), 
                game.isIsFinished());
        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        game.setGameID(newId);
        return game;
    }   

    @Override
    public Game getGame(int gameID) {
        try {
            final String sql = "SELECT gameID, answer, isFinished "
                    + "FROM game WHERE gameID = ?;";

            return jdbc.queryForObject(sql, new GameMapper(), gameID);
        } catch (DataAccessException ex) {
            return null;
        }
    }

    
    @Override
    public List<Round> getRounds(int gameID) {
        try{
            final String sql = "SELECT * FROM round WHERE gameID = ?;";
            List<Round> rounds = jdbc.query(sql, new RoundMapper(), gameID);   
            return rounds;
        } catch(DataAccessException ex) {
            return null;
        }
    }

    

    @Override
    public List<Game> getAllGames() {
        try {
            final String SELECT_ALL_GAMES = "SELECT * FROM game;";
            List<Game> games = jdbc.query(SELECT_ALL_GAMES, new GameMapper());
            games.forEach(game -> {
                game.setRounds(getRounds(game.getGameID()));
            });
            return games;
        }catch(DataAccessException ex) {
            return null;
        }
    }

    @Override
    public void updateGame(Game game) {
       final String UPDATE_GAME = "UPDATE Game SET answer = ?, isFinished = ? WHERE gameID = ?;";
        jdbc.update(UPDATE_GAME,
                game.getAnswer(),
                game.isIsFinished(),
                game.getGameID());
    }

    @Override
    @Transactional
    public void deleteGame(int gameID) {
        final String DELETE_ROUND = "DELETE FROM round " + "WHERE gameID = ?;";
        jdbc.update(DELETE_ROUND, gameID);
        
        final String DELETE_GAME = "DELETE FROM game WHERE gameID = ?;";
        jdbc.update(DELETE_GAME, gameID);
        
    }
    
    @Override
    public Round getRoundById(int roundID) {
        final String sql = "SELECT * FROM round WHERE roundID = ?;";
        return jdbcTemplate.queryForObject(sql, new RoundMapper(), roundID);
    }

    @Override
    public void updateRound(Round round) {
        final String sql = "UPDATE round SET "
                + "gameID = ?,"
                + "guess = ?,"
                + "guess = ?,"
                + "result = ?;";

        jdbcTemplate.update(sql,
                round.getGameID(),
                round.getGuess(),
                round.getTime(),
                round.getResult());
    }

    @Override
    public void deleteRoundById(int roundID) {
        final String sql = "DELETE FROM round WHERE roundID = ?;";
        jdbcTemplate.update(sql, roundID);
    }
     @Override
    @Transactional
    public List<Round> readAll() {
        final String sql = "SELECT * FROM round;";
        List<Round> rounds = jdbc.query(sql, new RoundMapper());
        
        return rounds;
    }
    
}
