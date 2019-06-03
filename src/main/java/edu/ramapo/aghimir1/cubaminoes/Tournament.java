package edu.ramapo.aghimir1.cubaminoes;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Tournament implements Serializable {

    /* *********************************************
    Class member variables
    ********************************************* */
    private Computer  m_computerPlayer;
    private Human m_humanPlayer;
    private Round m_currentRound;
    private boolean m_isSerialized = false;
    private String m_tournamentResult;
    private int m_tournamentMaxScore;

    /* *********************************************
    Constructors
    ********************************************* */
    public Tournament(int a_tournamentMaxScore){
        m_computerPlayer = new Computer();
        m_humanPlayer = new Human();
        m_currentRound = new Round(0, 0);
        m_tournamentResult = "";
        m_tournamentMaxScore = a_tournamentMaxScore;
    }

    public Tournament( String fileName ){
        m_computerPlayer = new Computer();
        m_humanPlayer = new Human();
        if ( !loadTournament( fileName ) )
            System.out.println("Errorrrrrrrrrrr");

        m_isSerialized = true;
    }

    /**
     Selector for the Round object in this class
     @return An instance of the current round
     */
    public Round getCurrentRound(){
        return m_currentRound;
    }

    /**
     Selector for the human object in this class
     @return An instance of the tournament's human player
     */
    public Human gethumanPlayer(){
        return m_humanPlayer;
    }

    /**
     Selector for the human object in this class
     @return An instance of the tournament's human player
     */
    public Computer getComputerPlayer(){
        return m_computerPlayer;
    }

    /**
     Selector for knowing the tournament's winner
     @return "The tournament ended in a tie" if tie.
     "The human player won the tournament" if human player wins the tournament
     "The computer player won the tournament" if computer player wins the tournament
     */

    public String getResult() {
        return m_tournamentResult;
    }

    public int getTournamentScore(){
        return m_tournamentMaxScore;
    }

    /**/
    /*
    loadTournament

    NAME

        loadTournament - Load a saved game from a text file

    SYNOPSIS

        private boolean loadTournament ( String a_fileName )
            a_fileName - name of the text file that needs to be parsed

    DESCRIPTION

        Parse key information of the game like the tournament threshold score, round number,
        players' hands, players' scores, tiles in the stock and on the table to load a previously
        saved game

    RETURNS

        true if the game successfully loads. false otherwise

    AUTHOR

        Ashish Ghimire

    DATE

        5/15/2019

    */
    private boolean loadTournament ( String a_fileName ){
        int parsedTournamentScore = UserInput.INVALID_OPTION;
        int roundNumber = UserInput.INVALID_OPTION;
        ArrayList<String> computerHand = new ArrayList<>();
        ArrayList<String> humanHand = new ArrayList<>();
        int computerScore = UserInput.INVALID_OPTION;
        int humanScore = UserInput.INVALID_OPTION;
        ArrayList<String> leftSideTiles = new ArrayList<>();
        ArrayList<String> rightSideTiles = new ArrayList<>();
        String engineString = "";
        boolean previousPlayerPassed = false;
        String nextPlayer = "";
        ArrayList<String> stockTiles = new ArrayList<>();

        File file = new File( a_fileName );
        if( !file.isFile() ){
            System.out.println("File not found");
        }
        else{
            System.out.println("File exists");

            try {
                Scanner sc = new Scanner(file);

                while ( sc.hasNextLine() ){
                    String oneLine = sc.nextLine();

                    if(oneLine.contains("Tournament Score") ){
                        parsedTournamentScore = parseFirstNumber(oneLine);
                    }
                    else if( oneLine.contains("Round No") ){
                        roundNumber = parseFirstNumber( oneLine );
                    }
                    else if( oneLine.contains("Computer:") ){
                        computerScore = parsePlayer( computerHand, sc );
                    }
                    else if( oneLine.contains("Human:") ){
                        humanScore = parsePlayer( humanHand, sc);
                    }
                    else if( oneLine.contains("Engine") ){
                        engineString = parseWordAfterColon(oneLine);
                    }
                    else if(oneLine.contains("Layout") ){
                        parseTable( leftSideTiles, rightSideTiles, sc, engineString );
                    }
                    else if( oneLine.contains("Stock") ){
                        parseStock(stockTiles, sc);
                    }
                    else if(oneLine.contains("Previous Player") ){
                        previousPlayerPassed = parsePlayerPassed(oneLine);
                    }
                    else if( oneLine.contains("Next Player") ){
                        nextPlayer = parseWordAfterColon(oneLine);
                    }
                }

                sc.close();
            }
            catch ( FileNotFoundException e){
                System.out.println("Error");
                return false;
            }
        }

        Computer computerPlayer = createComputer(computerHand );
        Human humanPlayer = createHuman( humanHand );

        // Set previous player passed
        if( nextPlayer.equalsIgnoreCase( humanPlayer.getPlayerString() ) ) {
            computerPlayer.setPass(previousPlayerPassed);
        }
        else if( nextPlayer.equalsIgnoreCase(m_computerPlayer.getPlayerString() ) ) {
            humanPlayer.setPass( previousPlayerPassed );
        }

        Stock stock = createStock( stockTiles );

        // Create engines
        Tile engine = createEngine( engineString );

        Table table = createTable(  leftSideTiles, rightSideTiles, engine );

        m_currentRound = new Round( roundNumber, engine, humanPlayer, computerPlayer, stock, table, nextPlayer, humanScore, computerScore );
        m_tournamentMaxScore = parsedTournamentScore;
        m_humanPlayer.updateScore(humanScore);
        m_computerPlayer.updateScore(computerScore);

        return true;
    }


    /**/
    /*
    parseFirstNumber

    NAME

        parseFirstNumber(String a_line) - Load a saved game from a text file

    SYNOPSIS

        private int parseFirstNumber(String a_line)
           a_line - a string variable that may have a number that this function needs to parse

    DESCRIPTION

        Split the line based on colon. Extract the first number after the colon

    RETURNS

        a number denoting an invalid number if the text contains no number. Else an integer number

    AUTHOR

        Ashish Ghimire

    DATE

        5/15/2019

    */
    private int parseFirstNumber(String a_line){
        int parsedScore = UserInput.INVALID_OPTION;

        String [] splitOnColon = a_line.split(":");
        for(String i: splitOnColon){
            Scanner sc = new Scanner(i);
            if( sc.hasNextInt() )
                parsedScore = sc.nextInt();
        }

        return parsedScore;
    }


    /**/
    /*
    parsePlayer

    NAME

        parsePlayer( ArrayList<String> a_list, Scanner a_sc )

    SYNOPSIS

        private int parsePlayer( ArrayList<String> a_list, Scanner a_sc )
           a_list - a list that contains string representation of tiles. A tile with
                    left stone, 5 and right stone, 6 would be represented as "5-3"
           a_sc - a scanner object representing the text file that needs to be parsed

    DESCRIPTION

        Parse player's score. And create a list of Tile objects from a list of string representing
        tiles

    RETURNS

        a number denoting an invalid number if the text contains no number. Else an integer number
        reprsenting a player's score

    AUTHOR

        Ashish Ghimire

    DATE

        5/15/2019

    */
    private int parsePlayer( ArrayList<String> a_list, Scanner a_sc ){
        int playerScore = UserInput.INVALID_OPTION;

        String lineWithHandInfo = a_sc.nextLine();
        String [] splitOnColon = lineWithHandInfo.split(":");

        parseTiles(a_list, splitOnColon[1] );

        String lineWithScoreInfo = a_sc.nextLine();
        playerScore = parseFirstNumber( lineWithScoreInfo );

        return playerScore;
    }


    /**/
    /*
    parseTiles

    NAME

        parseTiles( ArrayList<String> a_list, String a_line )

    SYNOPSIS

        private void parseTiles( ArrayList<String> a_list, String a_line )
           a_list - a list that should contain string representation of tiles. A tile with
                    left stone, 5 and right stone, 6 would be represented as "5-3"
           a_line - a line of text from which the string tiles need to be extracted.
                    A line would look something like "6-5 4-5 1-2 3-4"

    DESCRIPTION

        Parse string representation of player's tiles from a line of text.

    RETURNS

        None

    AUTHOR

        Ashish Ghimire

    DATE

        5/15/2019

    */
    private void parseTiles( ArrayList<String> a_list, String a_line ){

        String [] splitOnSpace = a_line.split(" ");

        for(String i: splitOnSpace){
            if(i.length() > 1)
                a_list.add(i);
        }
    }

    /**/
    /*
    parseStock

    NAME

        parseStock( ArrayList<String> a_list, Scanner a_sc ) - parse information about a game's stock

    SYNOPSIS

        private void parseStock( ArrayList<String> a_list, Scanner a_sc )
           a_list - a list that should contain string representation of tiles after the execution of
           this function . A tile with left stone, 5 and right stone, 6 would be represented as "5-3"
           a_sc - a scanner object that represents a text file from which game data needs to be parsed

    DESCRIPTION

        Parse string representation of player's tiles from a line of text.

    RETURNS

        None

    AUTHOR

        Ashish Ghimire

    DATE

        5/15/2019

    */
    private void parseStock( ArrayList<String> a_list, Scanner a_sc ){
        String stockString = a_sc.nextLine();
        parseTiles(a_list, stockString);
    }


    /**/
    /*
    parsePlayerPassed

    NAME

        parsePlayerPassed( String a_line ) - Parse and find if the previous player passed on his turn

    SYNOPSIS

        private boolean parsePlayerPassed( String a_line )
           a_line - a string variable that will contain information if the previous player passed on
           his turn. Format of a_line would be something like "Previous player passed: Yes"

    DESCRIPTION

        Determine if the previous player passed on his turn

    RETURNS

        true if the previous player passed on his turn. false otherwise

    AUTHOR

        Ashish Ghimire

    DATE

        5/15/2019

    */
    private boolean parsePlayerPassed( String a_line ){
        boolean passed = false;

        String [] splitOnColon = a_line.split(":");
        String relevantString = splitOnColon[1];

        if( relevantString.contains("Yes") )
            return true;

        return passed;
    }


    /**/
    /*
    parseWordAfterColon

    NAME

        parseWordAfterColon(String a_line) - Return the first word after a colon

    SYNOPSIS

        private String parseWordAfterColon(String a_line)
           a_line - a string variable that will contain at least one colon

    DESCRIPTION

        Split the text based on colon and return the second element from the array containing split
        elements


    RETURNS

        The first word after the first colon in the input text. The returned string won't have any
        space character in it

    AUTHOR

        Ashish Ghimire

    DATE

        5/15/2019

    */
    private String parseWordAfterColon(String a_line){
        String [] splitOnColon = a_line.split(":");

        String relevantString = splitOnColon[1];

        return relevantString.replace(" ", "");
    }


    /**/
    /*
    parseTable

    NAME

        parseTable( ArrayList<String> a_leftSide, ArrayList<String> a_rightSide, Scanner a_sc, String a_engineString )

    SYNOPSIS

        private void parseTable( ArrayList<String> a_leftSide, ArrayList<String> a_rightSinde, Scanner a_sc, String a_engineString )
           a_leftSide - a list that will contain string reprsentation of tiles on the left side of the
                        table ater the execution of this function
           a_rightSide - a list that will contain string reprsentation of tiles on the right side of the
                        table ater the execution of this function
           a_sc - a scanner object that represents a text file from which game data needs to be parsed
           a_engineString - a string variable that resembles a string reprsentation of a tile

    DESCRIPTION

        parse the tiles in the left side and the right side of the table

    RETURNS

        None

    AUTHOR

        Ashish Ghimire

    DATE

        5/15/2019

    */
    private void parseTable( ArrayList<String> a_leftSide, ArrayList<String> a_rightSide, Scanner a_sc, String a_engineString ){
        String tableInfo = a_sc.nextLine();
        String [] splitOnEngine = tableInfo.split(a_engineString);

        if( splitOnEngine.length >= 2 ){
            parseSide( a_leftSide, splitOnEngine[0] );
            parseSide( a_rightSide, splitOnEngine[1] );
            Collections.reverse( a_leftSide );
        }
    }

    /**/
    /*
    parseSide

    NAME

        parseSide( ArrayList<String> a_list, String a_string )

    SYNOPSIS

        private void parseSide( ArrayList<String> a_list, String a_string )
           a_list - a list that contains string representation of tiles. A tile with left stone, 5 and
                    right stone, 6 would be reprsented as "5-6"
           a_string - a string variable denoting text from which required information needs to be
                      extracted

    DESCRIPTION

        parse the tiles in the left side and the right side of the table

    RETURNS

        None

    AUTHOR

        Ashish Ghimire

    DATE

        5/15/2019

    */
    private void parseSide( ArrayList<String> a_list, String a_string ){
        a_string = a_string.replaceAll("L|R", "");

        parseTiles(a_list, a_string);
    }


    /**/
    /*
    createHuman

    NAME

        createHuman( ArrayList<String> a_handString ) - create a human player and populate its hand

    SYNOPSIS

        private Human createHuman( ArrayList<String> a_handString )


    DESCRIPTION

        Populate a human player's hand by parsing a text that contains string reprsentation of the
        tiles in his hand


    RETURNS

        A human object representing a human player

    AUTHOR

        Ashish Ghimire

    DATE

        5/15/2019

    */
    private Human createHuman( ArrayList<String> a_handString ){
        ArrayList<Tile> handTiles = createTilesFromStrings( a_handString );

        return new Human( handTiles );
    }


    /**/
    /*
    createComputer

    NAME

        createComputer( ArrayList<String> a_handString ) - create a Computer player and populate its hand

    SYNOPSIS

        private Computer createComputer( ArrayList<String> a_handString )


    DESCRIPTION

        Populate a Computer player's hand by parsing a text that contains string reprsentation of the
        tiles in his hand


    RETURNS

        A Computer object representing a Computer player

    AUTHOR

        Ashish Ghimire

    DATE

        5/15/2019

    */
    private Computer createComputer( ArrayList<String> a_handString ){
        ArrayList<Tile> handTiles = createTilesFromStrings( a_handString );

        return new Computer( handTiles );
    }

    /**/
    /*
    createStock

    NAME

        createStock( ArrayList<String> a_handString ) - create a Stock populate its tiles

    SYNOPSIS

        private Stock createStock( ArrayList<String> a_stockString )
            a_stockString - a string variable denoting text from which required information needs to be
                      extracted

    DESCRIPTION

        Populate a Stock by parsing a text that contains string reprsentation of the
        tiles in the stock

    RETURNS

        A Stock object representing a Stock

    AUTHOR

        Ashish Ghimire

    DATE

        5/15/2019

    */
    private Stock createStock( ArrayList<String> a_stockString ){
        ArrayList<Tile> stockTiles = createTilesFromStrings( a_stockString );

        return new Stock( stockTiles );
    }

    /**/
    /*
    createEngine

    NAME

        createEngine( String a_engineString )- create an engine tile from a text representing a tile

    SYNOPSIS

        private Tile createEngine( String a_engineString )
            a_engineString - a string variable denoting text from which required information needs to be
                      extracted. Format is "6-6" where 6 is the left stone and 6 is the right stone
                      of the engine tile

    DESCRIPTION

        Create an engine tile from a text representing a tile

    RETURNS

        A tile object

    AUTHOR

        Ashish Ghimire

    DATE

        5/15/2019

    */
    private Tile createEngine( String a_engineString ){
        Tile engine = null;

        if( !a_engineString.equals("") ) {
            int leftStone = Integer.parseInt(a_engineString.charAt(0) + "");
            int rightStone = Integer.parseInt( a_engineString.charAt(2) + "");
            engine = new Tile( leftStone, rightStone );
        }

        return engine;
    }


    /**/
    /*
    createTable

    NAME

        createTable( ArrayList<String> a_leftSideTiles, ArrayList<String> a_rightSideTiles, Tile a_engine )

    SYNOPSIS

        private Table createTable( ArrayList<String> a_leftSideTiles, ArrayList<String> a_rightSideTiles, Tile a_engine )
            a_leftSideTiles - a list that contains string representation of tiles in table's left side.
             A tile with left stone, 5 and right stone, 6 would be represented as "5-3"
            a_rightSideTiles - a list that contains string representation of tiles in table's right side.
            a_engine - a Tile object that is the engine of a given round

    DESCRIPTION

        Create a table by parsing information from a text file. Information would include the tiles in
        the left and the right side of the string

    RETURNS

        A Table object

    AUTHOR

        Ashish Ghimire

    DATE

        5/15/2019

    */
    private Table createTable( ArrayList<String> a_leftSideTiles, ArrayList<String> a_rightSideTiles, Tile a_engine ){

        // Create left side
        Side leftSide = new Side(Player.LEFT);

        if( !a_leftSideTiles.isEmpty() )
            setUpSide(0, leftSide, a_leftSideTiles);

        // Create right side
        Side rightSide = new Side( Player.RIGHT );
        if( !a_rightSideTiles.isEmpty() )
            setUpSide(1, rightSide, a_rightSideTiles );

        if( a_engine == null )
            return new Table();

        if( a_leftSideTiles.isEmpty() )
            leftSide.setOpenStone( a_engine.getLeftStone() );

        if( a_rightSideTiles.isEmpty() )
            rightSide.setOpenStone( a_engine.getRightStone() );

        return new Table( leftSide, rightSide );
    }


    /**/
    /*
    setUpSide

    NAME

        setUpSide( int a_index, Side a_side, ArrayList<String> a_stringTiles )
        - Initialize a Side object by setting up its tiles and its open stone

    SYNOPSIS

        private void setUpSide( int a_index, Side a_side, ArrayList<String> a_stringTiles )

    DESCRIPTION

        Initialize a Side object by setting up its tiles and its open stone based on text

    RETURNS

        None

    AUTHOR

        Ashish Ghimire

    DATE

        5/16/2019

    */
    private void setUpSide( int a_index, Side a_side, ArrayList<String> a_stringTiles ){
        String possOpenStone = a_stringTiles.get(a_stringTiles.size() - 1);
        String [] splitOnDash = possOpenStone.split("-");

        int openStone = Integer.parseInt( splitOnDash[a_index] );
        a_side.setOpenStone(openStone);

        ArrayList<Tile> sideTiles = createTilesFromStrings( a_stringTiles );
        a_side.setTile( sideTiles );
    }


    /**/
    /*
    createTilesFromStrings

    NAME

        createTilesFromStrings( ArrayList<String> a_stringTiles ) - Create an arraylist of tiles from
              an arraylist of strings each representing a tile

    SYNOPSIS

        private ArrayList<Tile> createTilesFromStrings( ArrayList<String> a_stringTiles )
            a_stringTiles - a list that contains string representation of tiles in table's left side.
             A tile with left stone, 5 and right stone, 6 would be represented as "5-3"

    DESCRIPTION

        Create an arraylist of tiles from an arraylist of string representing tiles

    RETURNS

        An arraylist of Tile objects

    AUTHOR

        Ashish Ghimire

    DATE

        5/16/2019

    */
    private ArrayList<Tile> createTilesFromStrings( ArrayList<String> a_stringTiles ){
        ArrayList<Tile> tiles = new ArrayList<>();

        // String format of tile is like this: 6-6
        for(String oneStringTile: a_stringTiles){
            if(oneStringTile.length() > 1) {
                int leftStone = Integer.parseInt(oneStringTile.charAt(0) + "");
                int rightStone = Integer.parseInt( oneStringTile.charAt(2) + "");
                Tile oneTile = new Tile( leftStone, rightStone );
                tiles.add( oneTile );
            }
        }

        return tiles;
    }


    /**/
    /*
    startNewRound

    NAME

        startNewRound - start a new round if the tournament is not over. If the tournament is over,
                determine the winner of the tournament

    SYNOPSIS

        public boolean startNewRound()

    DESCRIPTION

        Update the players' tournament scores after each round

    RETURNS

        False if the torunament is over. true otherwise

    AUTHOR

        Ashish Ghimire

    DATE

        5/16/2019

    */
    public boolean startNewRound(){
        updatePlayerScores( m_currentRound.getHuman().getScore(), m_currentRound.getComputer().getScore() );

        if( tournamentOver() ){
            // Determine the winner
            determineTheWinner();
            return false;
        }

        m_currentRound = new Round(this.m_humanPlayer.getScore(), this.m_computerPlayer.getScore());
        return true;
    }


    /**/
    /*
    updatePlayerScores

    NAME

        updatePlayerScores( int a_humanScore, int a_computerScore) - Update players' scores in the
                tournament

    SYNOPSIS

        private void updatePlayerScores( int a_humanScore, int a_computerScore)

    DESCRIPTION

        Update the players' tournament scores after each round

    RETURNS

        None

    AUTHOR

        Ashish Ghimire

    DATE

        5/16/2019

    */
    private void updatePlayerScores( int a_humanScore, int a_computerScore){
        // After each round is over, update the player scores here
        m_humanPlayer.updateScore( a_humanScore );
        m_computerPlayer.updateScore( a_computerScore );
    }


    /**/
    /*
    tournamentOver

    NAME

        tournamentOver - Let the caller know if the tournament is over

    SYNOPSIS

        public boolean tournamentOver()

    DESCRIPTION

        The tournament is over if any one of the players gets score equal than or greater than
        the tournament threshold score

    RETURNS

        True if the tournament is over. False otherwise

    AUTHOR

        Ashish Ghimire

    DATE

        5/16/2019

    */
    public boolean tournamentOver(){
        if( m_humanPlayer.getScore() >= m_tournamentMaxScore || m_computerPlayer.getScore() >= m_tournamentMaxScore )
            return true;
        return false;
    }


    /**/
    /*
    determineTheWinner

    NAME

        determineTheWinner - Set the value of class member variable, m_winnerString that denotes the
                who won the tournament

    SYNOPSIS

        private void determineTheWinner()

    DESCRIPTION

        Sets the winner of the tournament by checking which player got the greater score.
        The tournament ends in a tie if both player have same score after tournament completion

    RETURNS

        None

    AUTHOR

        Ashish Ghimire

    DATE

        5/16/2019

    */
    private void determineTheWinner(){
        if(m_humanPlayer.getScore() == m_computerPlayer.getScore() )
            m_tournamentResult = "The tournament ended in a tie";
        else if(m_humanPlayer.getScore() > m_computerPlayer.getScore())
            m_tournamentResult = "The human player won the tournament";
        else
            m_tournamentResult =  "The computer player won the tournament";
    }

    public static void main( String [] args ){

    }
}
