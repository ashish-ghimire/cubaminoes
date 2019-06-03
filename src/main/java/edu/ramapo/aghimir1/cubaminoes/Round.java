package edu.ramapo.aghimir1.cubaminoes;

import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Formatter;

import static edu.ramapo.aghimir1.cubaminoes.Player.PLACE_TILE;

public class Round implements Serializable {


    private static int m_roundCount = 0;

    /* *********************************************
    Class member variables
    ********************************************* */
    private Tile m_engine;
    private Player m_humanPlayer;
    private Player m_computerPlayer;
    private Stock m_stock;
    private Table m_table;
    private boolean m_turn; // If m_turn is 0, human is playing. Else computer is playing
    private Player m_winner;
    private String m_winnerString;
    private int m_passAttemptCount;
    private boolean m_needsInitialSetUp;
    private int m_humanTournamentScore;
    private int m_computerTournamentScore;
    private String m_moveSummary;


    /* *********************************************
    Constructors
    ********************************************* */
    Round( int a_humanTournamentScore, int a_computerTournamentScore ){
        m_roundCount++;
        int engine = computeEngine();
        m_engine = new Tile(engine, engine);
        m_humanPlayer = new Human();
        m_computerPlayer = new Computer();
        m_stock = new Stock();
        m_table = new Table();
        m_winner = null;
        m_needsInitialSetUp = true;
        m_humanTournamentScore = a_humanTournamentScore;
        m_computerTournamentScore = a_computerTournamentScore;
        m_moveSummary = "";
    }

    Round( int a_roundCount, Tile a_engine, Human a_human, Computer a_computer, Stock a_stock, Table a_table, String a_nextPlayer, int a_humanTournamentScore, int a_computerTournamentScore ){
        m_roundCount = a_roundCount;
        m_humanPlayer = a_human;
        m_computerPlayer = a_computer;

        // Handle engine
        if( a_engine != null) {
            m_engine = a_engine;
            m_needsInitialSetUp = false;
            m_turn = a_nextPlayer.equalsIgnoreCase( m_humanPlayer.getPlayerString() )? false: true;
        }
        else{
            int engine = computeEngine();
            m_engine = new Tile( engine, engine );
            m_needsInitialSetUp = true;
        }

        m_stock = a_stock;
        m_table = a_table;
        m_humanTournamentScore = a_humanTournamentScore;
        m_computerTournamentScore = a_computerTournamentScore;
        m_moveSummary = "";
    }

    /* *********************************************
    Selectors
    ********************************************* */

    public Player getWinner(){
        return m_winner;
    }

    public Tile getEngine() { return  m_engine; }

    public boolean needsInitialSetUp(){
        return m_needsInitialSetUp;
    }

    public Player getHuman(){
        return m_humanPlayer;
    }

    public Player getComputer(){
        return m_computerPlayer;
    }

    public Table getTable() {
        return m_table;
    }

    public Stock getStock(){
        return m_stock;
    }

    public int getHumanTournamentScore() { return  m_humanTournamentScore; }

    public int getComputerTournamentScore() { return  m_computerTournamentScore; }

    public String getMoveSummary(){
        return m_moveSummary;
    }

    public String getWinnerString(){
        return m_winnerString;
    }

    /**/
    /*
    computeEngine

    NAME

        computeEngine - compute the engine tile for the round based on the round number

    SYNOPSIS

        private int computeEngine()

    DESCRIPTION

        compute the engine tile for the round based on the round number and Tile.MAX_VALUE_OF_TILE which
        is usally, 6

    RETURNS

        None

    AUTHOR

        Ashish Ghimire

    DATE

        4/25/2019

    */
    private int computeEngine(){
        int temp = m_roundCount % (Tile.MAX_VALUE_OF_A_STONE + 1);
        if(temp == 0)
            return temp;

        return Tile.MAX_VALUE_OF_A_STONE - temp + 1;
    }

    /**/
    /*
    setUpRound

    NAME

        setUpRound - to set up the start of a round

    SYNOPSIS

        public String getPlayerString();

    DESCRIPTION

        At the start of each round, this function calls distributeTile to distribute 8 tiles to
        each player

    RETURNS

        None

    AUTHOR

        Ashish Ghimire

    DATE

        4/25/2019

    */
    public void setUpRound(){
        distributeTiles(8, m_computerPlayer);
        distributeTiles(8, m_humanPlayer);
    }

    /**/
    /*
    distributeTiles( int a_numTiles, Player a_player )

    NAME

        distributeTiles( int a_numTiles, Player a_player ) - distribute certian number of tiles to a player

    SYNOPSIS

        private void distributeTiles( int a_numTiles, Player a_player )
            a_numTiles - number of tiles to distribute to the player
            a_player - player to which the tile should be distributed

    DESCRIPTION

        Draws a_numTiles number of tiles from the stock and adds the newly extracted tile from
        the stock to a player's hand

    RETURNS

        None

    AUTHOR

        Ashish Ghimire

    DATE

        4/25/2019

    */
    private void distributeTiles( int a_numTiles, Player a_player ){
        for(int i = 0; i < a_numTiles; i++){
            Tile drawnFromStock = m_stock.getNextTile();
            a_player.addToHand(drawnFromStock);
        }
    }

    /**/
    /*
    placeEngine

    NAME

        placeEngine - find out which player has the round engine, remove the engine from the player's
        hand and add it to the table

    SYNOPSIS

        public void placeEngine()

    DESCRIPTION

        finds out which player has the engine in his hand, removes the engine from the player's hand and
        adds it to the table

    RETURNS

        None

    AUTHOR

        Ashish Ghimire

    DATE

        4/25/2019

    */
    public void placeEngine(){
        Player whoHasEngine = computeFirstPlayer();

        if( whoHasEngine != null){
            // The player who has the engine should place the engine on the table
            whoHasEngine.removeFromHand(m_engine);
            m_table.setLeftSideValue( m_engine.getLeftStone() );
            m_table.setRightSideValue( m_engine.getRightStone() );
            m_turn = !m_turn;
        }
    }


    /**/
    /*
    computeFirstPlayer

    NAME

        computeFirstPlayer - find out which player has the round engine

    SYNOPSIS

        private Player computeFirstPlayer()

    DESCRIPTION

        checks which player has the engine in his hand

    RETURNS

        a player object- a human if the human player has the engine tile in his hand.
        a computer if the computer player has the engine tile in his hand.

    AUTHOR

        Ashish Ghimire

    DATE

        4/25/2019

    */
    private Player computeFirstPlayer(){
        Player firstPlayer = null;

        if( m_humanPlayer.hasTile(m_engine) ) {
            firstPlayer = m_humanPlayer;
            m_turn = false;
        }
        else if( m_computerPlayer.hasTile(m_engine) ) {
            firstPlayer = m_computerPlayer;
            m_turn = true;
        }

        return firstPlayer;
    }

    /**/
    /*
    getWhoIsPlaying

    NAME

        getWhoIsPlaying - find out the player who is supposed to be making moves

    SYNOPSIS

        public Player getWhoIsPlaying()

    DESCRIPTION

        checks the class variable, m_turn for its value. If m_turn is false, returns the
        human player. Returns the computer player otherwise.

    RETURNS

        a player object- a human if it is human's turn to make the move.
       The computer player object if it is computer's turn to make the move.

    AUTHOR

        Ashish Ghimire

    DATE

        4/25/2019

    */
    public Player getWhoIsPlaying(){
        if(!m_turn)
            return m_humanPlayer;

        return m_computerPlayer;
    }


    /**/
    /*
    aPlayerHasEngine

    NAME

        aPlayerHasEngine - find out if any player has the engine tile in their hand

    SYNOPSIS

        public boolean aPlayerHasEngine()

    DESCRIPTION

        checks if the computer player or the human player have the engine tile in their hand

    RETURNS

        true if one of the players has the engine tile in their hand. false otherwise

    AUTHOR

        Ashish Ghimire

    DATE

        4/26/2019

    */
    public boolean aPlayerHasEngine(){
        if( m_humanPlayer.hasTile(m_engine) || m_computerPlayer.hasTile(m_engine) )
            return true;

        return false;
    }

    /**/
    /*
    drawATileEach

    NAME

        drawATileEach - to draw a tile each for the human and the computer player

    SYNOPSIS

        public void drawATileEach()

    DESCRIPTION

        distributes one tile each to both players

    RETURNS

        None

    AUTHOR

        Ashish Ghimire

    DATE

        4/26/2019

    */
    public void drawATileEach(){
        distributeTiles(1, m_humanPlayer);
        distributeTiles(1, m_computerPlayer);
    }

    /**/
    /*
    getOpponentPlayer(Player a_player)

    NAME

        getOpponentPlayer(Player a_player) - to return the opponent player for a given player

    SYNOPSIS

        private Player getOpponentPlayer(Player a_player)
            a_player - player for which the function needs to compute the opponent

    DESCRIPTION

        For the human player, the opponent is computer player. The vice versa is also true

    RETURNS

        Player object representing the human player if a_player represents a human. Else,
        a computer player object

    AUTHOR

        Ashish Ghimire

    DATE

        4/26/2019

    */
    private Player getOpponentPlayer(Player a_player){
        if(a_player == m_computerPlayer)
            return m_humanPlayer;

        return m_computerPlayer;
    }

    /**/
    /*
    makeMove( Player a_player )

    NAME

        makeMove( Player a_player ) - to make a move for a player in his turn

    SYNOPSIS

        public boolean makeMove( Player a_player )
            a_player - player trying to make a move

    DESCRIPTION

        Tries to make a move for a player based on his/ her choice

    RETURNS

        true if the player made a valid move. false otherwise

    AUTHOR

        Ashish Ghimire

    DATE

        5/05/2019

    */
    public boolean makeMove( Player a_player ){
        m_moveSummary = "";

        Player opponentPlayer = getOpponentPlayer(a_player);

        int move = a_player.getMoveOption( m_table, a_player.getSide(), opponentPlayer.hasPassed(), m_stock.isEmpty() );

        return verifyMove( move, a_player);
    }

    /**/
    /*
    verifyMove( int a_move, Player a_player )

    NAME

        verifyMove( int a_move, Player a_player ) - to check the validity of a move

    SYNOPSIS

        private boolean verifyMove( int a_move, Player a_player )
            a_move - move option representing type of move option the player wants to make
            a_player - player trying to ake the move

    DESCRIPTION

        checks if a player legitimately made a move based on move options

    RETURNS

        true if the player made a valid move. false otherwise

    AUTHOR

        Ashish Ghimire

    DATE

        5/019/2019

    */
    private boolean verifyMove( int a_move, Player a_player ){
        boolean validTilePlacement = false;
        boolean validPass = false;
        boolean validTileDrawn = false;
        m_moveSummary += "The " + a_player.getPlayerString() + " chose to ";

        switch (a_move){
            case UserInput.PLACE_TILE:
                validTilePlacement = placeTile( a_player );
                break;

            case UserInput.PASS:
                validPass = pass( a_player );
                m_moveSummary += "pass.";
                break;

            case UserInput.DRAW_TILE:
                validTileDrawn = drawTile( a_player );
                m_moveSummary += "draw additional tile from the stock";
                break;
        }


        if(validPass || validTilePlacement) {
            m_turn = !m_turn;
        }

        return validTilePlacement || validPass || validTileDrawn;
    }

    /**/
    /*
    placeTile( Player a_player )

    NAME

        placeTile( Player a_player ) - to check the validity of a PLACE_TILE move

    SYNOPSIS

        private boolean placeTile( Player a_player )
            a_player - player trying to place the tile

    DESCRIPTION

        checks if a player legitimately placed a valid tie on a valid side of the table

    RETURNS

        true if the player validly placed a tile on a side he is permitted to do so. false otherwise

    AUTHOR

        Ashish Ghimire

    DATE

        5/19/2019

    */
    private boolean placeTile( Player a_player ){
        // When placing a tile, a player needs to select two things:
        // 1) tile to place
        // 2) side to place the tile on
        boolean opponentPassed = getOpponentPlayer(a_player).hasPassed();
        int tileIndex = a_player.selectTile(m_table, opponentPassed );

        ArrayList<Tile> hand = a_player.getHand();

        if( tileIndex < 0 ||  tileIndex > hand.size() )
            return false;

        Tile chosenTile = hand.get(tileIndex);
        m_moveSummary += "place the tile, " + chosenTile.toString();

        String side = a_player.selectSide( m_table, a_player.getSide(), chosenTile, opponentPassed );

        if( !verifySide(side, chosenTile, a_player) )
            return false;

        m_moveSummary += " on the " + side + " side";

        boolean validPlacement = m_table.addToSide( chosenTile, side );

        if( validPlacement ) {
            a_player.setPass(false);
            // We need to remove the tile's reference from the player's hand
            a_player.removeFromHand(chosenTile);
            a_player.getInput().resetTileDrawn();
        }

        return validPlacement;
    }

    /**/
    /*
    verifySide( String a_side, Tile a_tileChosen, Player a_player )

    NAME

        verifySide( String a_side, Tile a_tileChosen, Player a_player ) - to make sure that the player places a tile only on the side he
                                        is allowed to do so

    SYNOPSIS

        verifySide( String a_side, Tile a_tileChosen, Player a_player )
            a_player - player trying to place the tile

    DESCRIPTION

        Makes sure that the player can't place a tile on an invalid side

    RETURNS

        true if the player validly placed a tile on a side he is permitted to do so. false otherwise

    AUTHOR

        Ashish Ghimire

    DATE

        5/19/2019

    */
    private boolean verifySide( String a_side, Tile a_tileChosen, Player a_player ){
        if( !(a_side.equalsIgnoreCase(Player.RIGHT) || a_side.equalsIgnoreCase(Player.LEFT) ) )
            return false;

        String playersSide = a_player.getSide();

        if( ! (a_tileChosen.isDoubleTile() || getOpponentPlayer(a_player).hasPassed() ) ){
            boolean validLeft = a_side.equalsIgnoreCase(Player.LEFT) && a_player.getSide().equals(Player.LEFT);
            boolean validRight = a_side.equalsIgnoreCase(Player.RIGHT) && playersSide.equals(Player.RIGHT);
            return validLeft || validRight;
        }

        return true;
    }

    /**/
    /*
    pass( Player a_player )

    NAME

        pass( Player a_player )- to make sure that the player passes only when he has run out of valid
                                moves to make

    SYNOPSIS

        private boolean pass( Player a_player )
            a_player - player trying to pass

    DESCRIPTION

        Makes sure that the player chooses to pass only when he is allowed to do so. The player can't
        choose to pass when he has possible valid moves

    RETURNS

        true if the player successfully passed. false otherwise

    AUTHOR

        Ashish Ghimire

    DATE

        5/09/2019

    */
    private boolean pass( Player a_player ){
        Player opponentPlayer = getOpponentPlayer(a_player);

        if( a_player.canPlaceTiles( m_table, a_player.getSide(), opponentPlayer.hasPassed() ) ){
            System.out.println("You can't pass because you still have valid move you can make");
            return false;
        }

        // The player must pass
        a_player.setPass(true);

        UserInput usrInput = a_player.getInput();
        usrInput.resetTileDrawn();

        return true;
    }

    /**/
    /*
    drawTile( Player a_player )

    NAME

        boolean drawTile( Player a_player )- to make sure that the player draws a stock tile only when
            he is allowed to do so

    SYNOPSIS

        private boolean drawTile( Player a_player )
            a_player - player trying to draw a tile from the stock

    DESCRIPTION

        Makes sure that the player draws a tile from the stock when he is allowed to do so. The player can't
        choose to draw tiles when he has possible valid moves. The player can't draw more than
        one tile per turn

    RETURNS

        true if the player successfully drew a tile from the stock. false otherwise

    AUTHOR

        Ashish Ghimire

    DATE

        5/09/2019

    */
    private boolean drawTile( Player a_player ){
        boolean opponentPassed = getOpponentPlayer(a_player).hasPassed();

        if( a_player.canPlaceTiles(m_table, a_player.getSide(), opponentPassed) ){
            return false;
        }

        if( m_stock.isEmpty() )
            return false;

        Tile tile = m_stock.getNextTile();
        a_player.addToHand( tile );

        UserInput usrInput = a_player.getInput();
        usrInput.incrementTileDrawn();

        return true;
    }

    /**/
    /*
    roundOver

    NAME

        roundOver- check if the round is over

    SYNOPSIS

        private boolean roundOver()

    DESCRIPTION

         The round ends when one of the following conditions is met:
        -One of the players empties her/his hand.
        -After the stock has emptied out (and not counting the pass that constitutes the last
        tile drawn from the stock), both the players pass.

    RETURNS

        true if the round is over. false otherwise

    AUTHOR

        Ashish Ghimire

    DATE

        5/09/2019

    */
    private boolean roundOver(){
        ArrayList<Tile> humanHand = m_humanPlayer.getHand();
        ArrayList<Tile> computerHand = m_computerPlayer.getHand();

        if( humanHand.isEmpty() || computerHand.isEmpty() )
            return true;

        if( m_stock.isEmpty() ){ // If the stock is empty and both the players pass
            if( m_humanPlayer.hasPassed() && m_computerPlayer.hasPassed() )
                return true;
        }

        return false;
    }



    /**/
    /*
    endRound

    NAME

        endRound - Successfully end a round by checking to see who won the round

    SYNOPSIS

        public boolean endRound()

    DESCRIPTION

        A round can end when of the two conditions is satisfied:
            - When a player empties his hand
            - When the stock is empty and both player's pass
        This function ends the round regardless of the condition and computes the round's winner

    RETURNS

        None

    AUTHOR

        Ashish Ghimire

    DATE

        5/14/2019

    */
    public boolean endRound(){
        if( !roundOver() )
            return false;

        ArrayList<Tile> humanHand = m_humanPlayer.getHand();
        int humanHandValue = m_humanPlayer.getHandValue();

        ArrayList<Tile> computerHand = m_computerPlayer.getHand();
        int computerHandValue = m_computerPlayer.getHandValue();

        endRoundHandEmpty(humanHand, computerHand, humanHandValue, computerHandValue);
        endRoundBothPlayersPass( humanHandValue, computerHandValue );
        setWinnerString();
        return true;
    }

    /**/
    /*
    endRoundHandEmpty

    NAME

        endRoundHandEmpty - Successfully end a round if one of the players has emptied his hand

    SYNOPSIS

        private void endRoundHandEmpty( ArrayList<Tile> humanHand, ArrayList<Tile> computerHand, int humanHandValue, int computerHandValue )

    DESCRIPTION

        A round can end when of the two conditions is satisfied:
            - When a player empties his hand
            - When the stock is empty and both players pass
        This function ends the round if one of the players has emptied her hand. The winner gets points equal
        to the number of sum of stones of all tiles in the opponent's hand. In case of a tie, no player
        gets any point

    RETURNS

        None

    AUTHOR

        Ashish Ghimire

    DATE

        5/14/2019

    */
    private void endRoundHandEmpty( ArrayList<Tile> humanHand, ArrayList<Tile> computerHand, int humanHandValue, int computerHandValue ){
        // If one player empties his/her hand, the player wins. The player gets points equal to the total stones on all the tiles still in the opponent's hand.
        if( humanHand.isEmpty() ){
            System.out.println("Human player's hand is empty. Human won");
            m_winner = m_humanPlayer;
            m_humanPlayer.updateScore( computerHandValue );
        }
        else if( computerHand.isEmpty() ){
            System.out.println("Computer player's hand is empty. Computer won");
            m_winner = m_computerPlayer;
            m_computerPlayer.updateScore( humanHandValue );
        }
    }


    /**/
    /*
    endRoundBothPlayersPass

    NAME

        endRoundBothPlayersPass - Successfully end a round if the stock is empty and both players
        pass. This means both players have run out of moves

    SYNOPSIS

        private void endRoundBothPlayersPass( int humanHandValue, int computerHandValue )

    DESCRIPTION

        A round can end when of the two conditions is satisfied:
            - When a player empties his hand
            - When the stock is empty and both players pass
        This function ends the round when the second condition is satisfied . Both players' hand values
        are compared. The person with the lesser hand value wins and gets points equal to the hand
        value of the opponent player. In case of a tie, no player gets any point

    RETURNS

        None

    AUTHOR

        Ashish Ghimire

    DATE

        5/14/2019

    */
    private void endRoundBothPlayersPass( int humanHandValue, int computerHandValue ){
        if( humanHandValue < computerHandValue ){
            m_winner = m_humanPlayer;
            m_humanPlayer.updateScore(computerHandValue);
        }
        else if(computerHandValue < humanHandValue ){
            m_winner = m_computerPlayer;
            m_computerPlayer.updateScore(humanHandValue);
        }
    }

    /**/
    /*
    setWinnerString

    NAME

        setWinnerString - Initialize a value of a string member variable, m_winnerString
        to a text that tells you the result of a round

    SYNOPSIS

        private void setWinnerString()

    DESCRIPTION

         m_winnerString is "Human player" if human won the round.
                            - "Computer player" if computer won the round
                            - "The round ended in a tie" if the round ended in a tie

    RETURNS

        None

    AUTHOR

        Ashish Ghimire

    DATE

        5/14/2019

    */
    private void setWinnerString(){
        if(m_winner == m_humanPlayer){
            m_winnerString = "Human player";
        }else if(m_winner == m_computerPlayer){
            m_winnerString = "Computer player";
        }
        else{
            m_winnerString = "The round ended in a tie";
        }
    }

    /**/
    /*
    saveGame( int a_tournamentScore )

    NAME

        saveGame( int a_tournamentScore )- to save the key statistics of the tournament in a text file

    SYNOPSIS

        public void saveGame( int a_tournamentScore )
            a_tournamentScore - a parameter that represents the maximum score a player can achieve in
            a tournament. IF any player gets score more than or equal to a_tournamentScore, he wins the
            tournament

    DESCRIPTION

         Writes information about the players' hands and tournament scores, the table, the stock,
         the current player, if the previous player passed, the engine and the round number

    RETURNS

        None

    AUTHOR

        Ashish Ghimire

    DATE

        5/26/2019

    */
    public void saveGame( int a_tournamentScore ){
        String fileName = "savedGame.txt";
        fileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + fileName;

        try{
            Formatter formatter = new Formatter( fileName );

            formatter.format("%s %s %d%s", "Tournament", "Score:", a_tournamentScore, "\n" );
            formatter.format("%s %s %d%s", "Round", "No:", m_roundCount, "\n");
            savePlayerInfo( m_computerPlayer, formatter, m_computerTournamentScore );
            savePlayerInfo( m_humanPlayer, formatter, m_humanTournamentScore );
            saveEngineInfo( formatter );
            saveTable( formatter );
            saveStock( formatter );
            saveOtherInfo( formatter );
            formatter.close();

        } catch (Exception e){
            System.out.println("Failure to save the game");
        }
    }

    /**/
    /*
    private void savePlayerInfo( Player a_player, Formatter a_formatter, int a_tournamentScore )

    NAME

        savePlayerInfo( Player a_player, Formatter a_formatter, int a_tournamentScore ) - Write information
        about a player to a file

    SYNOPSIS

        private void savePlayerInfo( Player a_player, Formatter a_formatter, int a_tournamentScore )
            a_player - a Player object whose information should be saved
            a_formatter - a Formatter object that indicates where on the file this function should write
            a_tournamentScore - an integer representing tournament score fo a player

    DESCRIPTION

         Writes information about the players' hands and tournament scores into a text file indicated
         by a formatter object

    RETURNS

        None

    AUTHOR

        Ashish Ghimire

    DATE

        5/26/2019

    */
    private void savePlayerInfo( Player a_player, Formatter a_formatter, int a_tournamentScore ){
        a_formatter.format("%s%s", "\n", a_player.getPlayerString() + ":\n" );
        a_formatter.format("%s", "  Hand:");

        ArrayList<Tile> playersHand = a_player.getHand();
        for( Tile i: playersHand){
            String tileStr = i.toString();
            a_formatter.format( " %s", tileStr );
        }

        a_formatter.format("%s", "\n");

        // Write player's score to the file
        a_formatter.format("%s %d%s", "  Score:", a_tournamentScore, "\n" );
    }

    /**/
    /*
    saveEngineInfo( Formatter a_formatter)

    NAME

        private void saveEngineInfo( Formatter a_formatter) - Save information about the round's engine
        to a file

    SYNOPSIS

        private void savePlayerInfo( Player a_player, Formatter a_formatter, int a_tournamentScore )
            a_player - a Player object whose information should be saved
            a_formatter - a Formatter object that indicates where on the file this function should write
            a_tournamentScore - an integer representing tournament score fo a player

    DESCRIPTION

         Writes information about the round's engine into a text file indicated
         by a formatter object

    RETURNS

        None

    AUTHOR

        Ashish Ghimire

    DATE

        5/26/2019

    */
    private void saveEngineInfo( Formatter a_formatter){
        a_formatter.format("%s%s %s%s", "\n", "Engine:", m_engine.toString(), "\n");
    }

    /**/
    /*
    saveTable( Formatter a_formatter )

    NAME

        saveTable( Formatter a_formatter ) - Save information about the game's table
        to a file

    SYNOPSIS

        private void saveTable( Formatter a_formatter )
            a_formatter - a Formatter object that indicates where on the file this function should write

    DESCRIPTION

        Writes information about the game's table including the tiles currently placed on both sides
        of the table into a text file

    RETURNS

        None

    AUTHOR

        Ashish Ghimire

    DATE

        5/26/2019

    */
    private void saveTable( Formatter a_formatter ){
        a_formatter.format( "%s%s%s", "\n", "Layout:", "\n");

        // Save left side of the table
        a_formatter.format("  %s", "L");
        saveTiles( m_table.getLeftSide().getAllTiles(), a_formatter );

        // Save engine
        a_formatter.format(" %s", m_engine.toString() );

        // Save right side of the table
        saveTiles( m_table.getRightSide().getAllTiles(), a_formatter );
        a_formatter.format(" %s", "R");

        // Add a new line
        a_formatter.format("%s", "\n");
    }

    /**/
    /*
    saveStock( Formatter a_formatter )

    NAME

        saveStock( Formatter a_formatter ) - Save information about a round's stock to a text file

    SYNOPSIS

        private void saveStock( Formatter a_formatter )
            a_formatter - a Formatter object that indicates where on the file this function should write

    DESCRIPTION

        Write the tiles yet to be distributed from the round's stock

    RETURNS

        None

    AUTHOR

        Ashish Ghimire

    DATE

        5/26/2019

    */
    private void saveStock( Formatter a_formatter ){
        a_formatter.format( "%s%s%s", "\n", "Stock:", "\n");

        saveTiles( m_stock.getTiles(), a_formatter );

        // Add a new line
        a_formatter.format("%s", "\n");
    }

    /**/
    /*
    saveTiles( ArrayList<Tile> a_list, Formatter a_formatter )

    NAME

        saveTiles( ArrayList<Tile> a_list, Formatter a_formatter ) - Write string representation of a ll
        tiles in a list to a text file

    SYNOPSIS

        private void saveTiles( ArrayList<Tile> a_list, Formatter a_formatter )
            a_formatter - a Formatter object that indicates where on the file this function should write
            a_list - a list of all tiles whose string representation needs to be put into the text
            file indicated by the formatter object

    DESCRIPTION

        Write the string representation of a tile to a text file. A tile with left stone 6 and right
        stone 4 would have th following represenation in the text file, 6-4

    RETURNS

        None

    AUTHOR

        Ashish Ghimire

    DATE

        5/26/2019

    */
    private void saveTiles( ArrayList<Tile> a_list, Formatter a_formatter ){
        for( Tile i: a_list ){
            a_formatter.format(" %s", i.toString() );
        }
    }


    /**/
    /*
    saveOtherInfo( Formatter a_formatter )

    NAME

        saveOtherInfo( Formatter a_formatter ) - Save remaining game stats to a text file

    SYNOPSIS

        private void saveOtherInfo( Formatter a_formatter )
            a_formatter - a Formatter object that indicates where on the file this function should write

    DESCRIPTION

        This function saves the following information about the game in the following format
         Previous Player Passed: (didPassString)

         Next Player: (playerStr)

         (didPassString) can be "Yes" or "No" while (playerStr) can be "Human" or "Computer"

    RETURNS

        None

    AUTHOR

        Ashish Ghimire

    DATE

        5/26/2019

    */
    private void saveOtherInfo( Formatter a_formatter ){
        Player currentPlayer = getWhoIsPlaying();
        Player opponent = getOpponentPlayer( currentPlayer );
        boolean didOpponentPass = opponent.hasPassed();
        String didPassString = didOpponentPass? "Yes": "No";

        String playerStr = currentPlayer.getPlayerString();

        a_formatter.format("%s%s %s%s", "\n", "Previous Player Passed:", didPassString, "\n");

        a_formatter.format("%s%s %s", "\n", "Next Player:", playerStr );
    }

    /**/
    /*
    help

    NAME

        help- to get the optimum move suggestion for a player using the computer player's strategy

    SYNOPSIS

        public String help()

    DESCRIPTION

         get the optimum move suggestion for a player using the computer player's strategy

    RETURNS

        A string that contains move suggestions for the player

    AUTHOR

        Ashish Ghimire

    DATE

        5/26/2019

    */
    public String help(){
        if( m_turn )
            return "";

        boolean opponentPassed = getOpponentPlayer( m_humanPlayer).hasPassed();
        boolean stockIsEmpty = m_stock.isEmpty();

        Human tempHuman = (Human) m_humanPlayer;

        return tempHuman.getHelp( m_table, opponentPassed, stockIsEmpty );
    }

    public static void main( String [] args ){

    }
}
