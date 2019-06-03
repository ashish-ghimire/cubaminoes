package edu.ramapo.aghimir1.cubaminoes;

import java.io.Serializable;
import java.util.Objects;

public class Tile implements Serializable {

    /* *********************************************
    Symbolic constants
    ********************************************* */
    public static int MAX_VALUE_OF_A_STONE = 6;
    public final static int UNDEFINED_STONE = 5000;

    /* *********************************************
    Class member variables
    ********************************************* */
    private int m_leftStone;
    private int m_rightStone;

    /* *********************************************
    Constructor
    ********************************************* */
    Tile( int left, int right ){
        if( (left >= 0 && left <= 6) && (right >= 0 && right <=6 ) ) {
            m_leftStone = left;
            m_rightStone = right;
        }
        else{
            m_leftStone = UNDEFINED_STONE;
            m_rightStone = UNDEFINED_STONE;
        }
    }

    /* *********************************************
    Selectors
    ********************************************* */
    public int getLeftStone()
    {
        return m_leftStone;
    }

    public int getRightStone() {
        return m_rightStone;
    }

    public int getNumericValue(){

        return m_leftStone + m_rightStone;
    }

    /**/
    /*
    isDoubleTile

    NAME

        isDoubleTile - Determine if a tile has left and right stones of equal values

    SYNOPSIS

        public boolean isDoubleTile()

    DESCRIPTION

        Determine if a tile has left and right stones of equal values

    RETURNS

        true if the value of m_leftStone equals the value of m_rightStone. false otherwise

    AUTHOR

        Ashish Ghimire

    DATE

        4/25/2019

    */
    public boolean isDoubleTile(){
        if( m_rightStone == m_leftStone )
            return true;

        return false;
    }

    /**/
    /*
    swapStone

    NAME

        swapStone - Swap the values of left and right stones of a tile

    SYNOPSIS

        public void swapStone()

    DESCRIPTION

        Swap the values of left and right stones

    RETURNS

        None

    AUTHOR

        Ashish Ghimire

    DATE

        4/23/2019

    */
    public void swapStone(){
        int temp = m_leftStone;
        m_leftStone = m_rightStone;
        m_rightStone = temp;
    }

    /**/
    /*
    looseEquals

    NAME

        looseEquals - Defines a loose definition of equality of two tiles

    SYNOPSIS

        public boolean looseEquals( Tile a_tile )
            a_tile - a Tile object whose stones need to be compared with the stones
            of the current object (this)

    DESCRIPTION

        Tile A is loosely equal to tile B as long as it has equal number of stones of a given value
        regardless of the order of stones. For instance, tiles "6-3" and "3-6" are equal according to
        this definition. Tiles "6-3" and "6-3" are also equal

    RETURNS

        true if tile represented by a_tile is loosely equal to this object

    AUTHOR

        Ashish Ghimire

    DATE

        4/20/2019

    */
    public boolean looseEquals( Tile a_tile ){
        if (a_tile.getRightStone() == this.getLeftStone() && a_tile.getLeftStone() == this.getRightStone() )
            return true;

        return this.equals(a_tile);
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("");
        sb.append(m_leftStone);
        sb.append("-").append(m_rightStone);

        return sb.toString();
    }

    // Overiding the equals method to compare the equality of two Tile objects
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tile tile = (Tile) o;
        return m_leftStone == tile.m_leftStone &&
                m_rightStone == tile.m_rightStone;
    }

    public static void main( String [] args ){

    }
}
