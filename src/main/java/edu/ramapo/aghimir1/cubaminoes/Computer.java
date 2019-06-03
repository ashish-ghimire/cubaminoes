package edu.ramapo.aghimir1.cubaminoes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Scanner;

public class Computer extends Player implements Serializable {

    private static String COMPUTER_STRING = "Computer";

    /* *********************************************
    Constructors - initialize any Computer object
    ********************************************* */
    Computer(){
        super();
        m_side = RIGHT;
    }

    Computer(ArrayList<Tile> a_hand){
        super(a_hand );
        m_side = RIGHT;
    }


    /**/
    /*
    getPlayerString

    NAME

        getPlayerString - to return a string value that tells the user what kind of player the player is

    SYNOPSIS

        public String getPlayerString()

    DESCRIPTION

        Return a string value that tells the user what kind of player the player is

    RETURNS

        A string that tells the caller what type of player the player is

    AUTHOR

        Ashish Ghimire

    DATE

        4/21/2019

    */

    public String getPlayerString(){
        return COMPUTER_STRING;
    }


    /**/
    /*
    getMoveOption

    NAME

        getMoveOption - Get the optimal move option out of UserInput.PLACE_TILE, UserInput.PASS and
        UserInput.DRAW_TILE

    SYNOPSIS

        public int getMoveOption( Table a_table, String a_playersSide, boolean a_opponentPassed, boolean a_stockEmpty )
            a_table - reference to the game table
            a_playersSide - a String denoting a player's side. Side can be either Player.LEFT or
                    Player.RIGHT
            a_opponentHasPassed - This variable tells if the opponent player passed in his previous turn
            a_stockEmpty - This variable tells if the round stock is empty


    DESCRIPTION

        Get the optimal move option out of UserInput.PLACE_TILE, UserInput.PASS and
        UserInput.DRAW_TILE

    RETURNS

        an integer that represents one of the moves a player can make in his turn

    AUTHOR

        Ashish Ghimire

    DATE

        4/21/2019

    */
    public int getMoveOption( Table a_table, String a_playersSide, boolean a_opponentPassed, boolean a_stockEmpty ){
        return getOptimalMoveOption( a_table, a_playersSide, a_opponentPassed, a_stockEmpty );
    }


    /**/
    /*
    selectTile

    NAME

        selectTile( Table a_table, boolean a_opponentHasPassed ) The function allows the player to
        select a tile from their hand

    SYNOPSIS

        public int selectTile( Table a_table, boolean a_opponentHasPassed )
            a_table - reference to the game table
            a_opponentHasPassed - This variable tells if the opponent player passed in his previous turn


    DESCRIPTION

        This function allows the player to select a tile from their hand

    RETURNS

        an index of a tile from the player's hand

    AUTHOR

        Ashish Ghimire

    DATE

        4/21/2019

    */
    public int selectTile( Table a_table, boolean a_opponentHasPassed ){
        return getOptimalTile( a_table, a_opponentHasPassed, Player.RIGHT );
    }


    /**/
    /*
    selectSide

    NAME

    selectSide( Table a_table, String a_playersSide, Tile a_tileChosen, boolean a_opponentHasPassed )
        - This function calls a function from the player class to get optimum side the player should
        place his tile on if he is allowed to do so

    SYNOPSIS

        public String selectSide( Table a_table, String a_playersSide, Tile a_tileChosen, boolean a_opponentHasPassed )
            a_table - reference to the game table
            a_playersSide - a String denoting a player's side. Side can be either Player.LEFT or
                    Player.RIGHT
            a_opponentHasPassed - This variable tells if the opponent player passed in his previous turn
            a_tileChosen - Tile the player chose in his turn


    DESCRIPTION

        This function calls a function from the player class to get optimum side the player should
        place his tile on if he is allowed to do so


    RETURNS

        A string that tells the side the player chose to place a_chosenTile in his turn

    AUTHOR

        Ashish Ghimire

    DATE

        4/21/2019

    */
    public String selectSide( Table a_table, String playersSide, Tile tileChosen, boolean opponentHasPassed ){
        return getOptimaSide();
    }


    /**/
    /*
    getHandForDisplay

    NAME

        getHandForDisplay - The implementation of this function returns the tiles the caller can
            view

    SYNOPSIS

        public abstract ArrayList<Tile> getHandForDisplay()

    DESCRIPTION

        Players can't view each other's hand tiles. This function should only called by the Activities the
        human user can see. Human player is not supposed to see any tiles of the computer player. The
        vice-versa is also true. Therefore, the function returns an empty list as the viewer (human user)
        is not supposed to see any of tiles in the computer player's hand


    RETURNS

        An empty arraylist

    AUTHOR

        Ashish Ghimire

    DATE

        5/25/2019

    */
    public ArrayList<Tile> getHandForDisplay(){
        return new ArrayList<>();
    }

    public static void main( String [] args){

    }
}
