package edu.ramapo.aghimir1.cubaminoes;

import java.io.Serializable;
import java.util.ArrayList;

public class Human extends Player implements Serializable {

    private static String HUMAN_STRING = "Human";

    /* *********************************************
    Constructors
    ********************************************* */
    Human(){
        super();
        m_side = LEFT;
    }

    Human(ArrayList<Tile> a_hand ){
        super(a_hand);
        m_side = LEFT;
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
    public String getPlayerString( ){
        return HUMAN_STRING;
    }



    public int selectTile( Table a_table, boolean opponentHasPassed ){
        return m_input.getTile();
    }


    /*
    getMoveOption

    NAME

        getMoveOption - Get the move option the human player decided to make out of UserInput.PLACE_TILE,
        UserInput.PASS and UserInput.DRAW_TILE

    SYNOPSIS

        public int getMoveOption( Table a_table, String a_playersSide, boolean a_opponentPassed, boolean a_stockEmpty )
            a_table - reference to the game table
            a_playersSide - a String denoting a player's side. Side can be either Player.LEFT or
                    Player.RIGHT
            a_opponentHasPassed - This variable tells if the opponent player passed in his previous turn
            a_stockEmpty - This variable tells if the round stock is empty


    DESCRIPTION

        Get the move option the human player decided to make out of UserInput.PLACE_TILE, UserInput.PASS
        and UserInput.DRAW_TILE

    RETURNS

        an integer that represents one of the moves a player wants to make in his turn

    AUTHOR

        Ashish Ghimire

    DATE

        5/3/2019

    */
    public int getMoveOption( Table a_table, String a_playersSide, boolean a_opponentPassed, boolean a_stockEmpty ){
        return m_input.getMoveOption();
    }



    /**/
    /*
    selectTile

    NAME

        selectTile( Table a_table, boolean a_opponentHasPassed ) - The function gets the tile the human
        player chose to play

    SYNOPSIS

        public int selectTile( Table a_table, boolean a_opponentHasPassed )
            a_table - reference to the game table
            a_opponentHasPassed - This variable tells if the opponent player passed in his previous turn


    DESCRIPTION

        - The function returns the index of tile the human player wants to play. If the hand consists
        of tiles 6-6 (index 0), 5-6 (index 1) and 2-3 (index 2) and the human player chose to play 2-3,
        this function would return 2.

    RETURNS

        an index of a tile from the player's hand

    AUTHOR

        Ashish Ghimire

    DATE

        4/21/2019

    */
    public String selectSide( Table a_table, String playersSide, Tile tileChosen, boolean opponentHasPassed ){
        return m_input.getSide();
    }


    /**/
    /*
    getHelp

    NAME

        getHelp( Table a_table, boolean a_opponentPassed, boolean a_stockEmpty ) - Help the human player
        make "optimum" move in his turn

    SYNOPSIS

        public String getHelp( Table a_table, boolean a_opponentPassed, boolean a_stockEmpty )
            a_table - reference to the game table
            a_opponentHasPassed - This variable tells if the opponent player passed in his previous turn
            a_stockEmpty - This variable tells if the round stock is empty


    DESCRIPTION

        If the player can place tiles in his turn this function will suggest him to place "optimum" tile
        on an "optimum" side. Please look at getOptimalTile in the Player class for more description about
        the "optimum" tile and "optimum" is computed.

        IF the player can't place any tiles from his hand legitimately on the table, if the stock is
        not empty and if he has not drawn any tiles from the stock in this turn, he is suggested to
        draw additional tile from the stock

        IF the options above are not satisfied, he is suggested to pass

    RETURNS

        A string that indicates the "optimum" move for the human player in his turn

    AUTHOR

        Ashish Ghimire

    DATE

        5/22/2019

    */
    public String getHelp( Table a_table, boolean a_opponentPassed, boolean a_stockEmpty ){
        String helpStr = "";
        String moveRationale = "";

        /*
            Help string should be based on the optimal move option the user can exercise at a given instance.
            Move options can either be UserInput.DRAW_TILE, or UserInput.DRAW_TILE or UserInput.PASS
         */

        int optimalMoveOption = getOptimalMoveOption( a_table, Player.LEFT, a_opponentPassed, a_stockEmpty);

        switch (optimalMoveOption){
            case UserInput.DRAW_TILE:
                helpStr = "You should draw additional tile from the stock ";
                moveRationale = "because you don't have any tiles on your hand that you can legitimately place on either side";
                break;

            case UserInput.PASS:
                helpStr = "You should pass ";
                moveRationale = "because you don't have any tiles on your hand that you can legitimately place on either side";
                break;

            case UserInput.PLACE_TILE:
                int optimalTile = getOptimalTile(a_table, a_opponentPassed, Player.LEFT);
                Tile optimal = m_hand.get(optimalTile);

                String optimalSide = getOptimaSide();
                helpStr = "You should place tile, " + optimal.toString() + " on " + optimalSide + " side ";
                moveRationale = "to get rid of the highest valued tile in your hand";
                break;
        }

        return helpStr + moveRationale;
    }


    /**/
    /*
    getHandForDisplay

    NAME

        getHandForDisplay - This function returns the tiles the caller can
            view

    SYNOPSIS

        public ArrayList<Tile> getHandForDisplay()

    DESCRIPTION

        Players can't view each other's hand tiles but this function is only implemented when the
        human player is playing. Since players can view their own tiles, the function returns
        the copy of tiles already in the human player's hand


    RETURNS

        An arraylist. A copy of the tiles the human player has in their hand

    AUTHOR

        Ashish Ghimire

    DATE

        5/25/2019

    */
    public ArrayList<Tile> getHandForDisplay(){
        return getHand();
    }

    public static void main( String [] args){

    }

}