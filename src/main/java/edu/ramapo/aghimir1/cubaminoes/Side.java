package edu.ramapo.aghimir1.cubaminoes;

import java.io.Serializable;
import java.util.ArrayList;

public class Side implements Serializable {
    /* *********************************************
    Class member variables
    ********************************************* */
    private ArrayList<Tile> m_tile;
    private int m_openStone;
    private String m_sideString; // Side string can be either left or right

    /* *********************************************
    Constructor
    ********************************************* */
    Side(String a_sideString){
        m_tile = new ArrayList<>();
        m_openStone = Tile.UNDEFINED_STONE;
        m_sideString = a_sideString;
    }

    /* *********************************************
    Mutators
    ********************************************* */
    public void setOpenStone(int stone){
        if(stone == Tile.UNDEFINED_STONE)
            return;

        m_openStone = stone;
    }

    public void setTile( ArrayList<Tile> a_hand ){

        m_tile = (ArrayList) a_hand.clone();
    }

    /* *********************************************
    Accessors
    ********************************************* */
    public int getOpenStone(){

        return m_openStone;
    }

    public ArrayList<Tile> getAllTiles(){

        return (ArrayList) m_tile.clone();
    }

    /* *********************************************
    Utility functions
    ********************************************* */

    /**/
    /*
    addTile

    NAME

        addTile - Add a tile to the side

    SYNOPSIS

        public boolean addTile(Tile a_tile)

    DESCRIPTION

        Legitimately add a tile to the side

    RETURNS

        true if a tile has been legitimately added to the tile list. false otherwise

    AUTHOR

        Ashish Ghimire

    DATE

        4/21/2019

    */
    public boolean addTile(Tile a_tile){
        if(a_tile == null || (a_tile.getLeftStone() != m_openStone && a_tile.getRightStone() != m_openStone) )
            return false;

        if( m_sideString.equals(Player.RIGHT) ){
            if(a_tile.getRightStone() == m_openStone )
                // Swap left and right stone of the a_tile
                a_tile.swapStone();

            m_openStone = a_tile.getRightStone();
            m_tile.add(a_tile);
            return true;
        }

        if ( m_sideString.equals(Player.LEFT) ){
            if(a_tile.getLeftStone() == m_openStone)
                // Swap left and right stone of the a_tile
                a_tile.swapStone();

            m_openStone = a_tile.getLeftStone();
            m_tile.add(a_tile);
            return true;
        }

        return false;
    }

    public static void main(String [] args){

    }
}
