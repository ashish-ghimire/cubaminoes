package edu.ramapo.aghimir1.cubaminoes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class Stock implements Serializable {
    private static int MAX_TILES = 28;

    /* *********************************************
    Class member variables
    ********************************************* */
    private ArrayList<Tile> m_tiles;
    private int m_nextTile;

    /* *********************************************
    Constructors
    ********************************************* */
    Stock(){
        m_tiles = new ArrayList<>();
        createNewStock();
        shuffle();
        m_nextTile = 0;
    }

    public Stock(ArrayList<Tile> a_tiles){
        m_tiles = (ArrayList)a_tiles.clone();
        m_nextTile = 0;
    }

    /**/
    /*
    createNewStock

    NAME

        createNewStock - Create and initialize all the tiles in the game

    SYNOPSIS

        private void createNewStock()

    DESCRIPTION

        Create and initialize all the tiles in the game. Add all the created tile to m_tiles

    RETURNS

        None

    AUTHOR

        Ashish Ghimire

    DATE

        4/21/2019

    */
    private void createNewStock(){
        for(int left = 0; left <= Tile.MAX_VALUE_OF_A_STONE; left++ ){
            for(int right = left; right <= Tile.MAX_VALUE_OF_A_STONE; right++){
                Tile tile = new Tile(left, right);
                m_tiles.add(tile);
            }
        }
    }

    /**/
    /*
    shuffle

    NAME

        shuffle - Shuffle all the tiles in m_tiles

    SYNOPSIS

        private void shuffle()

    DESCRIPTION

        Shuffle all the tiles in m_tiles

    RETURNS

        None

    AUTHOR

        Ashish Ghimire

    DATE

        4/20/2019

    */
    private void shuffle(){
        Collections.shuffle(m_tiles);
    }

    /**/
    /*
    getNextTile

    NAME

        getNextTile - Return a tile from m_tiles arraylist

    SYNOPSIS

        public Tile getNextTile()

    DESCRIPTION

        getNextTile - Return a tile from m_tiles arraylist and increment the value of
        m_nextTile by 1

    RETURNS

        A tile object

    AUTHOR

        Ashish Ghimire

    DATE

        4/20/2019

    */
    public Tile getNextTile(){
        return m_tiles.get(m_nextTile++);
    }

    /**/
    /*
    isEmpty

    NAME

        isEmpty - Determine if there are any more tiles to distribute from the stock. If ther are no more
        tiles to distribute, the stock is empty.

    SYNOPSIS

        public boolean isEmpty()

    DESCRIPTION

        A stock is empty if the value of m_nextTile is greater than the size of m_tiles

    RETURNS

        True if the stock is empty. False otherwise

    AUTHOR

        Ashish Ghimire

    DATE

        4/20/2019

    */
    public boolean isEmpty(){
        if( m_nextTile >= m_tiles.size() )
            return true;

        return false;
    }

    /**/
    /*
    getTiles

    NAME

        getTiles - A selector that returns the set of tiles yet to be distributed

    SYNOPSIS

        public ArrayList<Tile> getTiles()

    DESCRIPTION

        A selector that returns the set of tiles yet to be distributed

    RETURNS

        An arraylist of tiles

    AUTHOR

        Ashish Ghimire

    DATE

        4/20/2019

    */
    public ArrayList<Tile> getTiles(){
        ArrayList<Tile> tiles = new ArrayList<>();
        tiles.addAll( m_tiles.subList(m_nextTile, m_tiles.size() ));
        return tiles;
    }

    public static void main( String [] args ) {

    }
}
