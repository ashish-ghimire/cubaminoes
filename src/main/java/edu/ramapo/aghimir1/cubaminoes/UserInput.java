package edu.ramapo.aghimir1.cubaminoes;

import java.io.Serializable;

public class UserInput implements Serializable {
    public static final int INVALID_OPTION = 5000;

    /* *********************************************
    Symbolic constants that define the types of move options a player may exercise
    ********************************************* */
    public static final int PLACE_TILE = 1;
    public static final int DRAW_TILE = 2;
    public static final int PASS = 3;
    public static final int GAME_STATE = 4;

    /* *********************************************
    Member variables
    ********************************************* */
    private int m_tile;
    private String m_side;
    private int m_moveOption;
    private static int m_tileDrawn = 0;

    /* *********************************************
    Constructors
    ********************************************* */
    UserInput(){
        m_tile= Tile.UNDEFINED_STONE;
        m_side = "";
        m_moveOption = INVALID_OPTION;
    }

    UserInput(int a_tile, String a_side, int a_moveOption){
        m_tile = a_tile;
        m_side = a_side;
        m_moveOption = a_moveOption;
    }

    /* *********************************************
    Accessors/ Selectors
    ********************************************* */
    public int getTile() {
        return m_tile;
    }

    public String getSide() {
        return m_side;
    }

    public int getMoveOption(){
        return m_moveOption;
    }

    public int getTileDrawn(){
        return m_tileDrawn;
    }

    /* *********************************************
    Mutators
    ********************************************* */
    public void setTile(int a_tile) {
        this.m_tile = a_tile;
    }


    /**/
    /*
    incrementTileDrawn

    NAME

        incrementTileDrawn - Increment the value of member variable, m_tileDrawn that represents
        the number of tiles a player has drawn in his turn by 1

    SYNOPSIS

        public void incrementTileDrawn()

    DESCRIPTION

        Increment the value of member variable, m_tileDrawn by 1

    RETURNS

        None

    AUTHOR

        Ashish Ghimire

    DATE

        5/3/2019

    */
    public void incrementTileDrawn(){

        m_tileDrawn++;
    }

    /**/
    /*
    resetTileDrawn

    NAME

        resetTileDrawn - Re-initialize the value of member variable, m_tileDrawn that represents
        the number of tiles a player has drawn in his turn

    SYNOPSIS

        public void resetTileDrawn()

    DESCRIPTION

        Set the value of member variable, m_tileDrawn to 0

    RETURNS

        None

    AUTHOR

        Ashish Ghimire

    DATE

        5/3/2019

    */
    public void resetTileDrawn(){
        m_tileDrawn = 0;
    }
}
