package edu.ramapo.aghimir1.cubaminoes;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class Player implements Serializable {

    /* *********************************************
    Symbolic constants
    ********************************************* */
    protected static final String LEFT = "Left";
    protected static final String RIGHT = "Right";

    protected static final int PLACE_TILE = 1;
    protected static final int PASS = 2;

    /* *********************************************
    Class member variables that are protected because the derived classes will
    be able to use this as well
    ********************************************* */
    protected ArrayList<Tile> m_hand;
    protected boolean m_pass;
    protected int m_score;
    protected String m_side;
    protected UserInput m_input;

    // Other member variable
    private String m_optimalSideToPlaceTileOn;

    /* *********************************************
    Constructors
    ********************************************* */
    Player(){
        m_hand = new ArrayList<>();
        m_pass = false;
        m_score = 0;
        m_side = "";
        m_input = new UserInput();
        resetOptimalSide();
    }

    Player(ArrayList<Tile> a_hand ){
        m_hand = (ArrayList) a_hand.clone();
        m_score = 0;
        m_pass = false;
        m_side = "";
        m_input = new UserInput();
        resetOptimalSide();
    }

    /* *********************************************
    Accessors
    ********************************************* */
    public int getScore() {
        return m_score;
    }

    public String getSide() {
        return m_side;
    }

    public UserInput getInput(){
        return m_input;
    }

    public boolean hasPassed() {
        return m_pass;
    }

    /**
     A selector that returns a collection of all the tiles that constitute a player's hand
     @return an ArrayList of all tiles in a player's hand
     */
    ArrayList <Tile> getHand() { return (ArrayList) m_hand.clone(); }


    /* *********************************************
    Mutators
    ********************************************* */
    public void setInput( UserInput input){
        m_input = input;
    }

    public void setPass(boolean m_pass) {
        this.m_pass = m_pass;
    }

    public void updateScore(int score) {
        this.m_score += score;
    }

    private void resetOptimalSide(){
        m_optimalSideToPlaceTileOn = "";
    }


    /* *********************************************
    Abstract classes. Look at the derived classes for more description about the implementation of
    these classes
    ********************************************* */

    /**/
    /*
    getMoveOption

    NAME

        getMoveOption - Get the optimal move option out of UserInput.PLACE_TILE, UserInput.PASS and
        UserInput.DRAW_TILE

    SYNOPSIS

        public abstract int getMoveOption( Table a_table, String a_playersSide, boolean a_opponentPassed, boolean a_stockEmpty )
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

        4/25/2019

    */
    public abstract int getMoveOption( Table a_table, String a_playersSide, boolean a_opponentPassed, boolean a_stockEmpty );


    /**/
    /*
    selectTile

    NAME

        selectTile( Table a_table, boolean a_opponentHasPassed ) - The implementation of this
        function allows the players to select a tile from their hand

    SYNOPSIS

        public abstract int selectTile( Table a_table, boolean a_opponentHasPassed )
            a_table - reference to the game table
            a_opponentHasPassed -This variable tells if the opponent player passed in his previous turn

    DESCRIPTION

        The implementation of this function allows the players to select a tile from their hand

    RETURNS

        an index of a tile from the player's hand

    AUTHOR

        Ashish Ghimire

    DATE

        4/25/2019

    */
    public abstract int selectTile( Table a_table, boolean a_opponentHasPassed );


    /**/
    /*
    selectSide

    NAME

        String selectSide( Table a_table, String a_playersSide, Tile a_tileChosen, boolean a_opponentHasPassed )
            - The implementation of this function allows the players to select a side on the table

    SYNOPSIS

        public abstract String selectSide( Table a_table, String a_playersSide, Tile a_tileChosen, boolean a_opponentHasPassed )
            a_table - reference to the game table
            a_playersSide - a String denoting a player's side. Side can be either Player.LEFT or
                    Player.RIGHT
            a_opponentHasPassed - This variable tells if the opponent player passed in his previous turn
            a_tileChosen - Tile the player chose in his turn

    DESCRIPTION

        The implementation of this function allows the players to select a side on the table

    RETURNS

        A string that tells the side the player chose to place a_chosenTile in his turn

    AUTHOR

        Ashish Ghimire

    DATE

        4/25/2019

    */
    public abstract String selectSide( Table a_table, String a_playersSide, Tile a_tileChosen, boolean a_opponentHasPassed );


    /**/
    /*
    getPlayerString

    NAME

        getPlayerString
            - The implementation of this function returns the string representation of the players

    SYNOPSIS

        public abstract String getPlayerString()

    DESCRIPTION

        The function helps the caller know what the players are called

    RETURNS

        A string that tells the caller what type of player the player is

    AUTHOR

        Ashish Ghimire

    DATE

        4/25/2019

    */
    public abstract String getPlayerString();


    /**/
    /*
    getHandForDisplay

    NAME

        getHandForDisplay - The implementation of this function returns the tiles the caller can
            view

    SYNOPSIS

        public abstract ArrayList<Tile> getHandForDisplay()

    DESCRIPTION

        Players can't view each other's hand tiles. This function helps make sure that does not happen


    RETURNS

        An arraylist of Tile objects that represent the tiles the caller can view

    AUTHOR

        Ashish Ghimire

    DATE

        5/25/2019

    */
    public abstract ArrayList<Tile> getHandForDisplay();


    /**/
    /*
    getHandValue

    NAME

        getHandValue - Get the sum of numeric value of all tiles in a player's hand

    SYNOPSIS

        public int getHandValue()

    DESCRIPTION

        The numeric value of a tie is the sum of the value of its stones. For instance, the tile,
        3-6 would have a numeric value of 9. So, a hand with tiles, 3-5 4-6 and 2-3 would be
        8 + 10 + 5 = 23

    RETURNS

        an integer value denoting the sum of numeric values of all tiles in a player's hand

    AUTHOR

        Ashish Ghimire

    DATE

        4/25/2019

    */
    public int getHandValue(){
        int sum = 0;

        for(Tile i: m_hand )
            sum += i.getNumericValue();

        return sum;
    }

    /**/
    /*
    addToHand

    NAME

        addToHand - Add a tile to a player's hand

    SYNOPSIS

        public void addToHand(Tile a_tile)
            tile - a Tile object that needs to be added to the player's hand

    DESCRIPTION

        Add a non null tile to a player's hand

    RETURNS

        None

    AUTHOR

        Ashish Ghimire

    DATE

        4/23/2019

    */
    public void addToHand(Tile a_tile){
        if( a_tile != null)
            m_hand.add( a_tile );
    }


    /**/
    /*
    removeFromHand

    NAME

        removeFromHand(Tile a_tile) - Remove a tile from the player's hand if the tile to remove is
            valid

    SYNOPSIS

        public void removeFromHand(Tile a_tile)
            a_tile - the reference of the Tile object that needs to be removed from the Player's hand

    DESCRIPTION

        Remove a non null tile from the player's hand

    RETURNS

        None

    AUTHOR

        Ashish Ghimire

    DATE

        4/23/2019

    */
    public void removeFromHand(Tile a_tile){
        if(a_tile != null)
            m_hand.remove(a_tile);
    }


    /**/
    /*
    hasTile

    NAME

        hasTile( Tile a_tile ) - Check if the player has a tile specified by the parameter in his hand

    SYNOPSIS

        public boolean hasTile( Tile a_tile )
            a_tile - the reference of the Tile object that needs to be checked in the Player's hand

    DESCRIPTION

        A player has a certain tile if the a_tile equals one of the tiles already in the player's hand

    RETURNS

        true if the player has a tile that equals the tile specified in a_tile. false otherwise

    AUTHOR

        Ashish Ghimire

    DATE

        4/23/2019

    */
    public boolean hasTile( Tile a_tile ){
        for(Tile t: m_hand){
            if( t.equals(a_tile) )
                return true;
        }

        return false;
    }


    /**/
    /*
    canPlaceTiles

    NAME

        canPlaceTiles( Table a_table, String a_playersSide, boolean a_opponentHasPassed ) - Check if
            the player can make legitimate moves

    SYNOPSIS

        protected boolean canPlaceTiles( Table a_table, String a_playersSide, boolean a_opponentHasPassed )
            a_table - reference to the game table
            a_playersSide - a String denoting a player's side. Side can be either Player.LEFT or
                    Player.RIGHT
            a_opponentHasPassed - This variable tells if the opponent player passed in his previous turn

    DESCRIPTION

        A player can make a valid move in any one of the following situations:
            a) If the player has a double tile, the player can place the double tile on the opponent's
            side or his own side if the double tile's stone value equals to the value of the open stone
            on the respective side.
            b) If the player's opponent has passed, the player can place a tile on the opponent's
            side or his own side if the tile's stone value equals to the value of the open stone
            on the respective side.
            c) IF the opponent has not passed and the player does not have a duble tile in his hand,
            he can place a tile only on his side if the tile's stone value equals to the value of the
            open stone on the respective side.

    RETURNS

        true if the player has can make at least one valid move. false otherwise

    AUTHOR

        Ashish Ghimire

    DATE

        4/23/2019

    */
    protected boolean canPlaceTiles( Table a_table, String a_playersSide, boolean a_opponentHasPassed ){
        Side leftSide = a_table.getLeftSide();
        Side rightSide = a_table.getRightSide();

        for(Tile i: m_hand){
            if( i.isDoubleTile() ){
                if( i.getLeftStone() == leftSide.getOpenStone() || i.getLeftStone() == rightSide.getOpenStone() )
                    return true;
            }
            else if( a_opponentHasPassed ){
                // If the other player just passed on her/his turn, the player may place a non-double on either side.
                boolean checkLeftSide = leftSide.getOpenStone() == i.getLeftStone() || leftSide.getOpenStone() == i.getRightStone();
                boolean checkRightSide = rightSide.getOpenStone() == i.getLeftStone() || rightSide.getOpenStone() == i.getRightStone();

                if( checkLeftSide || checkRightSide )
                    return true;
            }
            else{ //Not a double tile. Can put such tile only on the "player's side"
                if( a_playersSide.equals(Player.RIGHT) ){
                    if( rightSide.getOpenStone() == i.getLeftStone() || rightSide.getOpenStone() == i.getRightStone() )
                        return true;
                }
                else if( a_playersSide.equals(Player.LEFT) ){
                    if(leftSide.getOpenStone() == i.getLeftStone() || leftSide.getOpenStone() == i.getRightStone() )
                        return true;
                }
            }
        }

        return false;
    }


    /**/
    /*
    getOneDoubleTile

    NAME

        getOneDoubleTile( int a_numericValue ) - Get the tile whose numeric value of its stones equals
         to the value specified by the parammeter

    SYNOPSIS

        protected Tile getOneDoubleTile( int a_numericValue )
            a_numericValue - the numeric value that should be looked for in player's hand tiles

    DESCRIPTION

        Get the tile whose numeric value of its stones equals to the value specified by the parammeter

    RETURNS

        null if the player does not have a double tile whose left stone's value equals the
        a_numericValue. Else, return the tile whose left and right stone's value equals the
        value specified by the parameter.

    AUTHOR

        Ashish Ghimire

    DATE

        4/23/2019

    */
    protected Tile getOneDoubleTile( int a_numericValue ){
        Tile res = null;

        for(Tile i: m_hand){
            if( i.isDoubleTile() && i.getRightStone() == a_numericValue )
                return i;
        }

        return res;
    }


    /**/
    /*
    getIndexOfTile

    NAME

        getIndexOfTile( Tile a_tile ) - Get the arraylist index (m_hand) of a tile in player's hand
            if the tile loosely equals the tile specfied in the parameter

    SYNOPSIS

        protected int getIndexOfTile( Tile a_tile )
            a_tile - Tile to compare with other tiles in the player's hand

    DESCRIPTION

        Tile A is loosely equal to tile B as long as it has equal number of stones of a given value
        regardless of the order of stones. For instance, tiles "6-3" and "3-6" are equal according to
        this definition. Tiles "6-3" and "6-3" are also equal

    RETURNS

        null if the player does not have a double tile whose left stone's value equals the
        a_numericValue. Else, return the tile whose left and right stone's value equals the
        value specified by the parameter.

    AUTHOR

        Ashish Ghimire

    DATE

        4/23/2019

    */
    protected int getIndexOfTile( Tile a_tile ){
        int tileIndex = Tile.UNDEFINED_STONE;

        for(Tile i: m_hand){
            if( i.looseEquals(a_tile) )
                return m_hand.indexOf(i);
        }

        return tileIndex;
    }


    /**/
    /*
    getHighestNumericValuedTile

    NAME

        getHighestNumericValuedTile( ArrayList<Tile> a_list ) - Return the numerically highest valued
            tile from a list

    SYNOPSIS

        protected int getHighestNumericValuedTile( ArrayList<Tile> a_list )
            a_list - an arraylist that the function should look at. The function will return the
                numerically highest valued tile from this list

    DESCRIPTION

        The numeric value of a tie is the sum of the value of its stones. For instance, the tile,
        3-6 would have a numeric value of 9. If the list only has tiles, 3-6 and 4-6, the function would
        return 4-6 whose numeric value (10) is greater than the numeric value (9) of the 3-6 tile


    RETURNS

        null if the list is empty. Else, the tile whose numeric value is highest among the tiles in
        the list specified in the parameter

    AUTHOR

        Ashish Ghimire

    DATE

        4/23/2019

    */
    protected int getHighestNumericValuedTile( ArrayList<Tile> a_list ){
        int res = Tile.UNDEFINED_STONE;
        int highestVal = Integer.MIN_VALUE;

        for( Tile i: a_list ){
            if( i.getNumericValue() > highestVal ){
                highestVal = i.getNumericValue();
                res = getIndexOfTile(i);
            }
        }

        return res;
    }


    /**/
    /*
    getOptimalMoveOption

    NAME

        getOptimalMoveOption( Table a_table, String a_playersSide, boolean a_opponentPassed, boolean a_stockEmpty )
            - This function gives the move option that is optimal based on game conditions specified by the
            parameters

    SYNOPSIS

        protected int getOptimalMoveOption( Table a_table, String a_playersSide, boolean a_opponentPassed, boolean a_stockEmpty )
            a_table - reference to the game table
            a_playersSide - a String denoting a player's side. Side can be either Player.LEFT or
                    Player.RIGHT
            a_opponentHasPassed - This variable tells if the opponent player passed in his previous turn
            a_stockEmpty - This variable tells if the round stock is empty

    DESCRIPTION

        The move options can be UserInput.PLACE_TILE, UserInput.PASS or UserInput.DRAW_TILE


    RETURNS

        One of the integer move options from UserInput.PLACE_TILE, UserInput.PASS and UserInput.DRAW_TILE


    AUTHOR

        Ashish Ghimire

    DATE

        5/23/2019

    */
    protected int getOptimalMoveOption( Table a_table, String a_playersSide, boolean a_opponentPassed, boolean a_stockEmpty ){
        if( canPlaceTiles( a_table, a_playersSide, a_opponentPassed ) )
            return UserInput.PLACE_TILE;

        if( m_input.getTileDrawn() == 0 && !a_stockEmpty)
            return UserInput.DRAW_TILE;

        return UserInput.PASS;
    }


    /**/
    /*
    getOptimalTile

    NAME

        getOptimalTile( Table a_table, boolean opponentHasPassed, String a_currentPlayersSide )
            - Get the tile that is optimal for a turn

    SYNOPSIS

        protected int getOptimalTile( Table a_table, boolean opponentHasPassed, String a_currentPlayersSide )
            a_table - reference to the game table
            a_currentPlayersSide - a String denoting a player's side. Side can be either Player.LEFT or
                    Player.RIGHT
            a_opponentHasPassed - This variable tells if the opponent player passed in his previous turn

    DESCRIPTION

        - See if you can place a double tile on the opponent's side. If you can, place a tile on the opponent's
        side and return.
        - If you can't place a double stone in opponent's side, place the best alternative stone in your side
        and return. This makes sense because if you don't have a double stone that you can legitimately
        place on the opponent's side and if the opponent has not passed, this is the only option you
        can exercise. Even if the opponent has passed, this makes sense. Usually, the opponent passes
        because he can't place a tile on his side. You wan't this to continue and the opponent to
        continue passing. So, don't clear his side by placing tiles in his side. Place tiles on your
        side.
        - If the above two options can't be exercised, if the opponent has passed, you can place a tile
        on his hand given you have a tile whose left or right stone matches the value of the open stone
        on the opponent's side

        If there are multiple tiles that fit one of the criterias above, return the tile with the highest
        numeric value since at the end of the game, if you tiles with higher numeric values remaining
        in yur hand, your opponent will get a higher score


    RETURNS

        A tile object

    AUTHOR

        Ashish Ghimire

    DATE

        5/23/2019

    */
    protected int getOptimalTile( Table a_table, boolean a_opponentHasPassed, String a_currentPlayersSide ){
        int openStoneOpponentsSide = a_table.getOpponentsSideOpenStone( a_currentPlayersSide );
        int openStonePlayersSide = a_table.getPlayersSideOpenStone( a_currentPlayersSide );

        String ownSide = a_currentPlayersSide;     // for human, it is player.left, for computer, Player.RIGHT
        String opponentsSide = a_currentPlayersSide.equalsIgnoreCase(Player.LEFT)? Player.RIGHT: Player.LEFT;

        // Look if you can place a double tile on the opponent's side first
        Tile doubleTile = getOneDoubleTile( openStoneOpponentsSide );
        if( doubleTile != null){
            m_optimalSideToPlaceTileOn = opponentsSide;
            return getIndexOfTile(doubleTile);
        }

        // If you can't place a double stone in opponent's side, place the best alternative stone in your side
        ArrayList<Tile> candidatesOwnSide = findCandidateTile( openStonePlayersSide, ownSide );
        int indexOfHighestValuedNumericTile = getHighestNumericValuedTile(candidatesOwnSide);

        if( indexOfHighestValuedNumericTile != Tile.UNDEFINED_STONE ) {
            m_optimalSideToPlaceTileOn = ownSide;
            return indexOfHighestValuedNumericTile;
        }

        // Try to place a non double on opponent's side. If the opponent has passed, you can do so
        ArrayList<Tile> candidatesOppSide = findCandidateTile( openStoneOpponentsSide, opponentsSide ); //Player.Right if doing this for huma

        m_optimalSideToPlaceTileOn = opponentsSide;
        return getHighestNumericValuedTile(candidatesOppSide);
    }



    /**/
    /*
    findCandidateTile

    NAME

        findCandidateTile(int a_openEnd, String a_side ) - Find tiles in the player's hand that have
        at least one stone whose value is equal to an "open stone" in the table

    SYNOPSIS

        private ArrayList<Tile> findCandidateTile(int a_openEnd, String a_side )
            a_openEnd - an integer whose values denotes the value of the open stone
                on either on left side or the right side of the table
            a_side - a string that can be either Player.LEFT or Player.RIGHT

    DESCRIPTION

        candidateTiles are the tiles in the player's hand that have at least one stone whose value
        is equal to a_openEnd

    RETURNS

        An arraylist of tile objects that have at least one stone whose value
        is equal to a_openEnd

    AUTHOR

        Ashish Ghimire

    DATE

        4/29/2019

    */
    private ArrayList<Tile> findCandidateTile(int a_openEnd, String a_side ){
        ArrayList<Tile> candidates = new ArrayList<>();
        ArrayList<Tile> handCopy = getHand();

        for(Tile i: handCopy){

            if( a_side.equals(Player.RIGHT) ){
                if( i.getLeftStone() == a_openEnd ){
                    candidates.add(i);
                }
                else if( i.getRightStone() == a_openEnd ){
                    Tile flipped = new Tile( i.getRightStone(), i.getLeftStone() );
                    candidates.add(flipped);
                }
            }
            else if( a_side.equals(Player.LEFT) ){
                if( i.getRightStone()== a_openEnd){
                    candidates.add(i);
                }
                else if( i.getLeftStone() == a_openEnd ){
                    Tile flipped = new Tile(i.getRightStone(), i.getLeftStone());
                    candidates.add(flipped);
                }
            }
        }

        return candidates;
    }


    /**/
    /*
    getOptimaSide

    NAME

        getOptimaSide - Get the optimal side a player should place a tile in his turn

    SYNOPSIS

        protected String getOptimaSide()

    DESCRIPTION

        Get the optimal side a player should place a tile in his turn. Don't call this function
        before getOptimalMove is called

    RETURNS

        A String variable that denotes the side the player should place a tile one. If the function
        is called before getOptimalMove is called, this function will return an empty string

    AUTHOR

        Ashish Ghimire

    DATE

        4/29/2019

    */
    protected String getOptimaSide(){
        return m_optimalSideToPlaceTileOn;
    }

    public static void main(String [] args){

    }
}
