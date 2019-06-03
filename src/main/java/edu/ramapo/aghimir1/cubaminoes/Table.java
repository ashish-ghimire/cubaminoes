package edu.ramapo.aghimir1.cubaminoes;

import java.io.Serializable;

public class Table implements Serializable {
    /* *********************************************
    Class member variables
    ********************************************* */
    Side m_leftSide;
    Side m_rightSide;

    /* *********************************************
    Constructors
    ********************************************* */
    Table(){
        m_leftSide = new Side(Player.LEFT);
        m_rightSide = new Side(Player.RIGHT);
    }

    Table( Side a_leftSide, Side a_rightSide ){
        m_leftSide = a_leftSide;
        m_rightSide = a_rightSide;
    }

    /* *********************************************
    Accessors
    ********************************************* */
    Side getLeftSide(){
        return m_leftSide;
    }

    Side getRightSide(){
        return m_rightSide;
    }


    /* *********************************************
    Mutators
    ********************************************* */
    public void setLeftSideValue(int val){
        m_leftSide.setOpenStone(val);
    }

    public void setRightSideValue(int val){
        m_rightSide.setOpenStone(val);
    }

    /**/
    /*
    addToSide

    NAME

        addToSide - Add a tile to either a table's left side or right side

    SYNOPSIS

        public boolean addToSide(Tile a_tile, String a_side)
            a_tile - a tile object
            a_side - a side object

    DESCRIPTION

        Add a non null tile to either a table's left side or right side

    RETURNS

        true if a_tile was successfully put into a_side. false otherwise

    AUTHOR

        Ashish Ghimire

    DATE

        5/15/2019

    */
    public boolean addToSide(Tile a_tile, String a_side){
        if( a_side == null )
            return false;

        if (a_side.equals(Player.LEFT) )
            return m_leftSide.addTile(a_tile);
        else if(a_side.equals(Player.RIGHT) )
            return m_rightSide.addTile(a_tile);

        return false;
    }

    /**/
    /*
    getPlayersSideOpenStone

    NAME

        getPlayersSideOpenStone - Return the open stone of either the left side or the
        right side of the table

    SYNOPSIS

        public int getPlayersSideOpenStone( String a_currentPlayersSide )
            a_currentPlayersSide - string whose value can either be PLAYER.LEFT
            or PLAYER.RIGHT

    DESCRIPTION

        Return the open stone of either the left side or the
        right side of the table

    RETURNS

        An integer representing the open stone of either the left side or the
        right side of the table

    AUTHOR

        Ashish Ghimire

    DATE

        4/21/2019

    */
    public int getPlayersSideOpenStone( String a_currentPlayersSide ){
        if( a_currentPlayersSide.equals(Player.RIGHT) )
            return m_rightSide.getOpenStone();

        return m_leftSide.getOpenStone();
    }


    /**/
    /*
    getOpponentsSideOpenStone

    NAME

        getOpponentsSideOpenStone - Return the open stone of the opponent's side of the table

    SYNOPSIS

        public int getOpponentsSideOpenStone(String a_currentPlayersSide)
            a_currentPlayersSide - string whose value can either be PLAYER.LEFT
            or PLAYER.RIGHT

    DESCRIPTION

        Return the open stone of either the left side or the
        right side of the table, depending upon the side of the current player

    RETURNS

        An integer representing the open stone of either the left side or the
        right side of the table

    AUTHOR

        Ashish Ghimire

    DATE

        4/21/2019

    */
    public int getOpponentsSideOpenStone(String a_currentPlayersSide){
        if( a_currentPlayersSide.equals(Player.LEFT) )
            return m_rightSide.getOpenStone();

        return m_leftSide.getOpenStone();
    }

    public static void main( String [] args ){

    }
}
