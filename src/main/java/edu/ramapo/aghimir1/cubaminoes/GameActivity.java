package edu.ramapo.aghimir1.cubaminoes;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;

import android.graphics.Typeface;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

import static edu.ramapo.aghimir1.cubaminoes.UserInput.DRAW_TILE;
import static edu.ramapo.aghimir1.cubaminoes.UserInput.GAME_STATE;
import static edu.ramapo.aghimir1.cubaminoes.UserInput.PASS;
import static edu.ramapo.aghimir1.cubaminoes.UserInput.PLACE_TILE;

public class GameActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    /* *********************************************
    Symbolic constants
    ********************************************* */

    public static final String HUMAN_FINAL_TOURNAMENT_SCORE = "edu.ramapo.aghimir1.casino.HumanFinalTournamentScore";
    public static final String COMPUTER_FINAL_TOURNAMENT_SCORE = "edu.ramapo.aghimir1.casino.ComputerFinalTournamentScore";
    public static final String TOURNAMENT_WINNER = "edu.ramapo.aghimir1.casino.TournamentWinner";

    private static final int INVALID_INDEX = Tile.UNDEFINED_STONE;

    private static final String DRAW_TILES_PROMPT = "No player has the engine for the round. Press the button below to draw more tiles. One tile will be distributed to each player";
    private static final String DRAW_TILES_BUTTON_TEXT = "Draw Cards";

    private static final String PLACE_ENGINE_PROMPT = "One of the players has the engine. Press the button below to place the engine and start the round";
    private static final String PLACE_ENGINE_BUTTON_TEXT = "Place Engine";

    /* *********************************************
    Class member variables
    ********************************************* */
    private Tournament m_tournament;
    private Round m_round;
    private Player m_currentPlayer;
    private boolean m_enginePlaced = false;
    private String m_inputSide;
    private int m_inputhandTileIndex;


    /**/
    /*
    onCreate

    NAME

        onCreate(Bundle savedInstanceState) - Called when this activity is created

    SYNOPSIS

        protected void onCreate(Bundle savedInstanceState)

    DESCRIPTION

        Gets called when this activity is created. View for this activity is activity_intro_page.xml

    RETURNS

        None

    AUTHOR

        Ashish Ghimire

    DATE

        4/28/2019

    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        initializeTournament();
    }


    /**/
    /*
    initializeTournament

    NAME

        initializeTournament - Get the newly initialized tournament and set up the round so that
            the game can start

    SYNOPSIS

        private void initializeTournament()

    DESCRIPTION

        Get the Tournament object that was passed as extra by the activity that started GameActivity.
        Initialize the new round in that tournament. Set up the round's engine if need be

    RETURNS

        None

    AUTHOR

        Ashish Ghimire

    DATE

        4/28/2019

    */
    private void initializeTournament(){
        // To retrieve Tournament object passed by the Activity that started this activity
        m_tournament = (Tournament) getIntent().getSerializableExtra( IntroPageActivity.NEW_TOURNAMENT );
        m_round = m_tournament.getCurrentRound();


        if( m_round.needsInitialSetUp() ){
            m_round.setUpRound();
            setUpEngine();
        }
        refreshView();
    }


    /**/
    /*
    startNewRound

    NAME

        startNewRound - Start a new round if the tournament is not over

    SYNOPSIS

        private void startNewRound()

    DESCRIPTION

        Start a new round if the tournament is not over. Set up the engine if need be. Refresh the entire
        view as new round's first view should be different from old round's last view

    RETURNS

        None

    AUTHOR

        Ashish Ghimire

    DATE

        4/28/2019

    */
    private void startNewRound(){
        // Then start a new round through tournament class
        if( !m_tournament.startNewRound() ){
            endTournament();
        }
        else{
            m_round = m_tournament.getCurrentRound();
            m_round.setUpRound();
            setUpEngine();
            refreshView();
        }
    }


    /**/
    /*
    endTournament

    NAME

        endTournament - Start a new activity to signal the end of the tournament

    SYNOPSIS

        private void endTournament()

    DESCRIPTION

        When calling an acitivity to end the tournament, the function puts key information of the
        players that the final activity may need as intent extras that can be accessed by keys that
        are public symbolic constants of this clas

    RETURNS

        None

    AUTHOR

        Ashish Ghimire

    DATE

        4/28/2019

    */
    private void endTournament(){
        Intent intent = new Intent(this, GameOverActivity.class);
        intent.putExtra( HUMAN_FINAL_TOURNAMENT_SCORE, m_tournament.gethumanPlayer().getScore() + "" );
        intent.putExtra( COMPUTER_FINAL_TOURNAMENT_SCORE, m_tournament.getComputerPlayer().getScore() + "" );
        intent.putExtra( TOURNAMENT_WINNER, m_tournament.getResult() );
        startActivity(intent);
    }


    /**/
    /*
    refreshView

    NAME

        refreshView - Refresh the entire main view of the round

    SYNOPSIS

        private void refreshView()

    DESCRIPTION

        Adds information relevant for the player to make moves

    RETURNS

        None

    AUTHOR

        Ashish Ghimire

    DATE

        4/28/2019

    */
    private void refreshView(){
        m_round = m_tournament.getCurrentRound();
        resetInputs();

        addEngine();
        setUpCurrentPlayer();
        setUpTextBoxes();
        setUpHand();
        setUpTable();
        setUpTurnInfo();
    }


    /**/
    /*
    resetInputs

    NAME

        resetInputs - Reset the side and the hand tile the player chose to play

    SYNOPSIS

        private void resetInputs()

    DESCRIPTION

        Resetting may be necessary when turn changes. We don't want the new player to have the same
        input as the old player.

    RETURNS

        None

    AUTHOR

        Ashish Ghimire

    DATE

        4/28/2019

    */
    private void resetInputs(){
        m_inputSide = "";
        m_inputhandTileIndex = INVALID_INDEX;
    }


    /**/
    /*
    setUpCurrentPlayer

    NAME

        setUpCurrentPlayer - Get the current player from the game

    SYNOPSIS

        private void setUpCurrentPlayer()

    DESCRIPTION

        If it is the human player's turn, this function will get the reference to the human player.
        If it is the computer player's turn, this function will get the reference to the computer
        player. If it is neither, it will get a reference to a null object.

    RETURNS

        None

    AUTHOR

        Ashish Ghimire

    DATE

        4/28/2019

    */
    private void setUpCurrentPlayer(){
        m_currentPlayer = m_round.getWhoIsPlaying();
    }


    /**/
    /*
    setUpTextBoxes

    NAME

        setUpTextBoxes - Set up most of the text-boxes of the view

    SYNOPSIS

        private void setUpTextBoxes()

    DESCRIPTION

        Set up the following text boxes:
            - Human and computer tournament scores
            - The value of the engine
            - The text box that shows the description of the current player's hand

    RETURNS

        None

    AUTHOR

        Ashish Ghimire

    DATE

        4/28/2019

    */
    private void setUpTextBoxes(){
        int humanScore = m_round.getHumanTournamentScore();
        int computerScore = m_round.getComputerTournamentScore();
        String engineText = m_round.getEngine().toString();
        String currentPlayer = m_round.getWhoIsPlaying().getPlayerString();

        fillUpTextView( (TextView) findViewById(R.id.humanTournamentScore), "Human tournament score: " + humanScore );
        fillUpTextView( (TextView) findViewById(R.id.computerTournamentScore), "Computer tournament score " + computerScore );
        fillUpTextView( (TextView) findViewById(R.id.engineTopTextView), "Engine: " + engineText );
        fillUpTextView( (TextView) findViewById(R.id.textViewHandText), currentPlayer + " player's hand:" );
    }


    /**/
    /*
    fillUpTextView

    NAME

        fillUpTextView( TextView a_textView, String a_str ) - Set text and style the textboxes

    SYNOPSIS

        private void fillUpTextView( TextView a_textView, String a_str )

    DESCRIPTION

        fillUpTextView( TextView a_textView, String a_str ) - Set text and style the textboxes

    RETURNS

        None

    AUTHOR

        Ashish Ghimire

    DATE

        4/28/2019

    */
    private void fillUpTextView( TextView a_textView, String a_str ){
        a_textView.setTypeface( Typeface.DEFAULT_BOLD );
        a_textView.setText( a_str );
    }


    /**/
    /*
    setUpTurnInfo

    NAME

        setUpTurnInfo - Set up all the textboxes that contain information about a player's turn

    SYNOPSIS

        private void setUpTurnInfo()

    DESCRIPTION

        Set up the following textboxes:
            - the textbox that says whose turn it is to make moves
            - the textbox that says the open stone on the human side
            - the textbox that says the open stone on the computer side

    RETURNS

        None

    AUTHOR

        Ashish Ghimire

    DATE

        5/28/2019

    */
    private void setUpTurnInfo(){
        String currentPlayer = m_round.getWhoIsPlaying().getPlayerString();
        Table gameTable = m_round.getTable();
        int humanSideOpenStone = gameTable.getLeftSide().getOpenStone();
        int computerSideOpenStone = gameTable.getRightSide().getOpenStone();

        String turnText = getResources().getString(R.string.turn_info_turn);
        String humanSideOpenStoneText = getResources().getString(R.string.turn_info_human_open);
        String comptuterSideOpenStoneText = getResources().getString(R.string.turn_info_computer_open_stone);

        fillUpTextView( (TextView) findViewById(R.id.turnInfoTurn), turnText + " " + currentPlayer );
        fillUpTextView( (TextView) findViewById(R.id.turnInfoHumanOpenStone), humanSideOpenStoneText + " " + humanSideOpenStone );
        fillUpTextView( (TextView) findViewById(R.id.turnInfoComputerOpenStone), comptuterSideOpenStoneText + " " + computerSideOpenStone );
    }


    /**/
    /*
    setUpHand

    NAME

        setUpHand - View the tiles a player needs to make to make moves

    SYNOPSIS

        private void setUpHand()

    DESCRIPTION

        Display the tiles as images the player can click to choose the tile to place it on a valid
        table side

    RETURNS

        None

    AUTHOR

        Ashish Ghimire

    DATE

        5/28/2019

    */
    private void setUpHand(){

        ArrayList<Tile> handTiles = m_currentPlayer.getHandForDisplay();

        LinearLayout handLayout = findViewById(R.id.linearLayoutHand);
        handLayout.removeAllViews();

        int buttonId = 0;
        for( Tile i: handTiles ){
            // Create Buttons representing each tiles
            ImageButton button = new ImageButton(this);
            String tileString = i.toString();
            tileString = tileString.replace("-", "");
            String dominoResourceName = "t" + tileString;
            int resId = getResources().getIdentifier( dominoResourceName, "drawable", getPackageName());
            button.setBackgroundResource(resId);

            button.setId( buttonId );
            handLayout.addView(button);

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) button.getLayoutParams();
            params.setMargins(0, 0, 20, 0); //bottom margin is 25 here (change it as u wish)
            button.setLayoutParams(params);

            // Make sure to add on click listeners to buttons that do appropriate task
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // Define on click functionality here
                    recordInputHandTileIndex( v );
                }
            });

            buttonId++;
        }
    }


    /**/
    /*
    recordInputHandTileIndex

    NAME

        recordInputHandTileIndex - Record the tile the user wants to select

    SYNOPSIS

        void recordInputHandTileIndex(View view)
            view - the exact button that triggered this function when pressed

    DESCRIPTION

        Called when one of the tiles from a player's hand is selected

    RETURNS

        None

    AUTHOR

        Ashish Ghimire

    DATE

        5/28/2019

    */
    void recordInputHandTileIndex(View view){
        m_inputhandTileIndex = view.getId();
    }


    /**/
    /*
    setUpTable

    NAME

        setUpTable - View the tiles on the left (human's) and the right (computer's) side of the table

    SYNOPSIS

        private void setUpTable()

    DESCRIPTION

        The tiles containing open stones are the most important tiles a player needs to see
        because a player should make his move based on the open stones. This function
        scrolls the view for the respective table sides so that the open stones can be viewed
        clearly

    RETURNS

        None

    AUTHOR

        Ashish Ghimire

    DATE

        5/28/2019

    */
    private void setUpTable(){
        // Set up left side
        Table table = m_round.getTable();

        ArrayList<Tile> m_leftSideTiles = table.getLeftSide().getAllTiles();
        Collections.reverse(m_leftSideTiles);

        LinearLayout layoutLeft = findViewById(R.id.linearLayoutHumanSide);
        addTileToLayout( layoutLeft, m_leftSideTiles );

        // Set up right side
        ArrayList<Tile> m_rightSideTiles = table.getRightSide().getAllTiles();
        LinearLayout layoutRight = findViewById(R.id.linearLayoutComputerSide);
        addTileToLayout( layoutRight, m_rightSideTiles );

        // The 6 lines below set the default position of the scroll to the left most end
        // Idea taken from the accepted answer in
        // https://stackoverflow.com/questions/4720469/horizontalscrollview-auto-scroll-to-end-when-new-views-are-added
        final HorizontalScrollView scrollViewLeft = findViewById(R.id.scrollViewHumanSide);
        scrollViewLeft.post(new Runnable() {
            public void run() {
                scrollViewLeft.fullScroll(HorizontalScrollView.FOCUS_LEFT);
            }
        });

        // The 6 lines below set the default position of the scroll to the right most end
        // Idea taken from the accepted answer in
        // https://stackoverflow.com/questions/4720469/horizontalscrollview-auto-scroll-to-end-when-new-views-are-added
        final HorizontalScrollView scrollViewRight = findViewById(R.id.scrollViewComputerSide);
        scrollViewRight.post(new Runnable() {
            public void run() {
                scrollViewRight.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
            }
        });

        setOnTouchListener( findViewById(R.id.linearLayoutHumanSide));
        setOnTouchListener( findViewById(R.id.linearLayoutComputerSide));
    }


    /**/
    /*
    setOnTouchListener

    NAME

        setOnTouchListener( View a_view ) - Set on click/touch listener for a view

    SYNOPSIS

        private void setOnTouchListener( View a_view )
            a_view - the exact view object that triggered this function when clicked

    DESCRIPTION

        Triggered when the player clicks on the tiles already displayed on either the left side
        or the right side of the table

    RETURNS

        None

    AUTHOR

        Ashish Ghimire

    DATE

        5/28/2019

    */
    private void setOnTouchListener( View a_view ){
        a_view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                setInputSide( v );
            }
        });
    }


    /**/
    /*
    setInputSide

    NAME

        setInputSide( View view ) - Record the side the user wants to place a tile in

    SYNOPSIS

        public void setInputSide( View view )
            view - the exact view object that triggered this function when clicked

    DESCRIPTION

        A player must select a valid side before he places tile on the table. This function is triggered
        on either of the following conditions:
            - When the user presses a button labeled "Human side" or the player presses one of the
            tiles already displayed on the left side (Human side) of the table. When the player does either
            of the things discussed above, the function records that the user wants to place a tile
            on the left side of the table
            - When the user presses a button labeled "Computer side" or the player presses one of the
            tiles already displayed on the right side (Computer side) of the table. When the player does either
            of the things discussed above, the function records that the user wants to place a tile
            on the right side of the table

    RETURNS

        None

    AUTHOR

        Ashish Ghimire

    DATE

        5/28/2019

    */
    public void setInputSide( View view ){
        int viewId = view.getId();

        System.out.println("Inside setInput Side");

        if( viewId == R.id.buttonHumanSide || viewId == R.id.linearLayoutHumanSide ){
            m_inputSide = Player.LEFT;
        }
        else if (viewId == R.id.buttonComputerSide || viewId == R.id.linearLayoutComputerSide ){
            m_inputSide = Player.RIGHT;
        }
    }


    /**/
    /*
    addTileToLayout

    NAME

        addTileToLayout( LinearLayout a_layout, ArrayList<Tile> a_tiles) - Add tiles to a linear layout

    SYNOPSIS

        private void addTileToLayout( LinearLayout a_layout, ArrayList<Tile> a_tiles)
            a_layout - The layout to which tiles from the list should be added
            a_list - a list of tiles that should be added to the layout

    DESCRIPTION

        First remove the tiles that were previously on the layout. This has the same effect as refreshing
        the layout's view. Then, add the tiles to the layout

    RETURNS

        None

    AUTHOR

        Ashish Ghimire

    DATE

        5/28/2019

    */
    private void addTileToLayout( LinearLayout a_layout, ArrayList<Tile> a_tiles){
        a_layout.removeAllViews();
        for(Tile i: a_tiles){
            // Create Buttons representing each tiles
            ImageButton button = new ImageButton(this);

            String tileString = i.toString();
            tileString = tileString.replace("-", "");
            String dominoResourceName = "t" + tileString;
            int resId = getResources().getIdentifier( dominoResourceName, "drawable", getPackageName());
            button.setBackgroundResource(resId);

            button.setClickable(false);
            button.setFocusable(false);

            a_layout.addView( button );

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) button.getLayoutParams();
            params.setMargins(0, 0, 20, 0); //bottom margin is 25 here (change it as u wish)
            button.setLayoutParams(params);
        }
    }


    /**/
    /*
    setUpEngine

    NAME

        setUpEngine - Make sure the engine tile is placed on the table

    SYNOPSIS

        private void setUpEngine()

    DESCRIPTION

        A round can't start without the player having the engine tile in his hand placing the engine
        tile on the table. If none of the players have the engine tile on their hand, each player
        draws a tile from the hand. This continues until one of the players has the engine tile in
        their hand. The function makes it poissible to place the engine tile from the player's hand
        on the table.

    RETURNS

        None

    AUTHOR

        Ashish Ghimire

    DATE

        5/28/2019

    */
    private void setUpEngine(){
        /**
         * Displays a dialog box that allows the human player to press a dynamicaaly aletered button
         * to select two types of options. The dialog box won't close unless the player chooses right option
         */
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.layout_place_engine);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        TextView prompt = dialog.findViewById(R.id.placeEnginePrompt);
        Button actionButton = dialog.findViewById(R.id.placeEngineButton);

        if( m_round.aPlayerHasEngine() ){
            prompt.setText(PLACE_ENGINE_PROMPT);
            actionButton.setText(PLACE_ENGINE_BUTTON_TEXT);
            actionButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // Define on click functionality here
                    m_round.placeEngine();
                    refreshView();
                    dialog.dismiss();
                }
            });
        }
        else{
            prompt.setText(DRAW_TILES_PROMPT);
            actionButton.setText(DRAW_TILES_BUTTON_TEXT);
            actionButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // Define on click functionality here
                    m_round.drawATileEach();
                    dialog.dismiss();
                    setUpEngine();
                }
            });
        }

        dialog.show();
    }


    /**/
    /*
    addEngine

    NAME

        addEngine - Set up the view for the engine tile in the middle of the table

    SYNOPSIS

        private void addEngine()

    DESCRIPTION

        Set up the view for the engine tile in the middle of the table. Before the view for the
        tile is added, the previous view is removed from the layout containing the view

    RETURNS

        None

    AUTHOR

        Ashish Ghimire

    DATE

        5/28/2019

    */
    private void addEngine(){
        Tile engineTile = m_round.getEngine();
        String engineText = engineTile.toString();

        LinearLayout layout = this.findViewById(R.id.linearLayoutEngine);
        layout.removeAllViews();

        ImageButton button = new ImageButton(this);
        engineText = engineText.replace("-", "");
        String dominoResourceName = "t" + engineText;
        int resId = getResources().getIdentifier( dominoResourceName, "drawable", getPackageName());
        button.setBackgroundResource(resId);

        layout.addView(button);
    }


    /**/
    /*
    showPopup

    NAME

        showPopup(View v) - Display a pop up menu that will contain the move options a player
            can choose in his turn

    SYNOPSIS

        public void showPopup(View v)
            v - the exact view that cause this function to execute when on view touch

    DESCRIPTION

        Called when user clicks floating action button. Shows pop up menu with move options
        for the player making the move

    RETURNS

        None

    AUTHOR

        Ashish Ghimire

    DATE

        5/28/2019

    */
    public void showPopup(View v){
        PopupMenu menu = new PopupMenu(this, v);
        menu.setOnMenuItemClickListener(this);
        menu.inflate(R.menu.popup_menu);
        menu.show();
    }


    /**/
    /*
    onMenuItemClick

    NAME

        onMenuItemClick(MenuItem a_item) - Called when user clicks one of the options in the popup
        menu to make a move

    SYNOPSIS

        public boolean onMenuItemClick(MenuItem a_item)

    DESCRIPTION

        Called when user clicks one of the options in the popup menu. The buttons
        in the menu are used to make moves

    RETURNS

        true if one of the options in the menu is selected. false otherwise

    AUTHOR

        Ashish Ghimire

    DATE

        5/28/2019

    */
    @Override
    public boolean onMenuItemClick(MenuItem a_item){
        switch (a_item.getItemId() ){
            case R.id.menuOptionPlaceTile:
                makeMove( PLACE_TILE );
                return true;
            case R.id.menuOptionDrawFromStock:
                makeMove( DRAW_TILE );
                return true;
            case R.id.menuOptionPass:
                makeMove( PASS );
                return true;
            case R.id.menuOptionGameState:
                makeMove( GAME_STATE );
                return true;
        }
        return false;
    }


    /**/
    /*
    makeMove

    NAME

        makeMove( int a_moveOption ) - Respond according to the user's move choice.

    SYNOPSIS

        private void makeMove( int a_moveOption )
            a_moveOption - one of the move options the user selected out of PLACE_TILE, PASS,
            DRAW_MORE_TILES and GAME_STATE to make move

    DESCRIPTION

        Let the user know if he tired to make an invalid move. Else respond according to is valid
        move choice and refresh the view once a valid move is made.

    RETURNS

        None

    AUTHOR

        Ashish Ghimire

    DATE

        4/23/2019

    */
    private void makeMove( int a_moveOption ){
        if( a_moveOption == GAME_STATE ) {
            // Open a dialog box to show game state
            showGameStats();
            return;
        }
        else{
            UserInput input = new UserInput(m_inputhandTileIndex, m_inputSide, a_moveOption);
            m_currentPlayer.setInput(input);

            boolean validMove = m_round.makeMove( m_currentPlayer );
            if( !validMove ){
                // Show invalid move toast
                showToast("Invalid move");
            }
            else{
                // Display a snackbar with move summary
                String moveSummary = m_round.getMoveSummary();
                showSnackbar( moveSummary );

                if( m_round.endRound() ){
                    // Show a dialog box with end of round stats
                    showEndOfRoundDialog();
                }

                refreshView();
            }
        }
    }


    /**/
    /*
    saveGame

    NAME

        saveGame(View view) - To save the statistics of a game so that the game can be reloaded later

    SYNOPSIS

        public void saveGame(View view)
            view - The exact button that when pressed, triggered this function

    DESCRIPTION

        Triggered when the imagebutton that has a save symbol is pressed

    RETURNS

        None

    AUTHOR

        Ashish Ghimire

    DATE

        4/23/2019

    */
    public void saveGame(View view){
        m_round.saveGame( m_tournament.getTournamentScore() );

    }


    /**/
    /*
    showGameStats

    NAME

        showGameStats - Display the key game statistics to test if the game is working properly.

    SYNOPSIS

        private void showGameStats()

    DESCRIPTION

        Designed primarily for testing purposes, this function opens up a dialog box that contains
        the following information:
            - the tiles in each players' hand
            - the tiles yet to be drawn from the stock

    RETURNS

        None

    AUTHOR

        Ashish Ghimire

    DATE

        5/03/2019

    */
    private void showGameStats(){
        //Dialog
        final Dialog dialog = new Dialog(GameActivity.this);
        dialog.setTitle("Game Stats ");
        dialog.setContentView(R.layout.layout_game_stats);

        showHandsAndStock( dialog );

        Button okay = dialog.findViewById(R.id.okayButton);
        //on click listener to OK button
        okay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //close the dialog
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    /**/
    /*
    showToast

    NAME

        showToast - Display a toast to let the user know of his move

    SYNOPSIS

        private void showToast(CharSequence a_text)

    DESCRIPTION

        Display a toast to let the user know of his move

    RETURNS

        None

    AUTHOR

        Ashish Ghimire

    DATE

        5/03/2019

    */
    private void showToast(CharSequence a_text){
        Context context = getApplicationContext();

        Toast toast = Toast.makeText(context, a_text, Toast.LENGTH_SHORT);
        toast.show();
    }


    /**/
    /*
    autoMove

    NAME

        autoMove( View view ) - "Automatically" make move for the computer player

    SYNOPSIS

        public void autoMove( View view )
            view- The exact view that when pressed, triggered this function

    DESCRIPTION

        The move is "automatic" because the human player does not have to manually select tile or
        the side to make move for the computer player. He can simply press the image that shows
        a small computer icon on the top left corner of the main view to insure that the computer
        player makes his move

    RETURNS

        None

    AUTHOR

        Ashish Ghimire

    DATE

        5/03/2019

    */
    public void autoMove( View view ){
        resetInputs();
        makeMove( INVALID_INDEX );
    }


    /**/
    /*
    help

    NAME

        help(View view) - Get help for the human player

    SYNOPSIS

        public void help(View view)
            view- The exact view that when pressed, triggered this function

    DESCRIPTION

        Display a string that contains information about the optimal move the player should make in
        his turn.

    RETURNS

        None

    AUTHOR

        Ashish Ghimire

    DATE

        5/23/2019

    */
    public void help(View view){
        String helpStr = m_round.help();

        showSnackbar( helpStr );
    }


    /**/
    /*
    showSnackbar

    NAME

        showSnackbar( String a_text ) - Display a snackbar widget that contains certain text

    SYNOPSIS

        private void showSnackbar( String a_text )
            a_text - The text that should be displayed on the snakabar widget

    DESCRIPTION

        Display a snackbar widget that contains certain text

    RETURNS

        None

    AUTHOR

        Ashish Ghimire

    DATE

        5/23/2019

    */
    private void showSnackbar( String a_text ) {
        // Took a look at the following page to learn how to use snackbar
        // https://material.io/develop/android/components/snackbar/
        final Snackbar snackbar = Snackbar.make(findViewById( R.id.mainPageConstraintLayout ), a_text, Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("Okay", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });

        // Learned how to set max lines on snackbar through the following answer on
        // Stackoverflow https://stackoverflow.com/questions/30705607/android-multiline-snackbar
        View snackbarView = snackbar.getView();
        TextView textView= snackbarView.findViewById(android.support.design.R.id.snackbar_text); //
        textView.setMaxLines(2);

        snackbar.show();
    }


    /**/
    /*
    showHandsAndStock

    NAME

        showHandsAndStock( Dialog a_dialog ) - Display the players' hand tiles and the tiles yet to
            be drawn from the stock

    SYNOPSIS

        private void showHandsAndStock( Dialog a_dialog )
            a_dialog - Reference to the dialog to which the items should be added to

    DESCRIPTION

        Given a dialog as input, the function will add the views that enable the users to see
        the players' hand tiles and the tiles yet to be drawn from the stock

    RETURNS

        None

    AUTHOR

        Ashish Ghimire

    DATE

        5/26/2019

    */
    private void showHandsAndStock( Dialog a_dialog ){
        // Add tiles to computer hand linear layout in the dialog
        addTileToLayout( (LinearLayout) a_dialog.findViewById(R.id.endOfRoundComputerHand), m_round.getComputer().getHand() );

        // Add tiles to human hand linear layout in the dialog
        addTileToLayout( (LinearLayout) a_dialog.findViewById(R.id.endOfRoundHumanHand), m_round.getHuman().getHand() );

        addTileToLayout((LinearLayout) a_dialog.findViewById(R.id.endOfRoundStock), m_round.getStock().getTiles() );
    }


    /**/
    /*
    showEndOfRoundDialog - Display a dialog box at the end of a round showing the vital
        statistics of the round

    NAME

        showEndOfRoundDialog

    SYNOPSIS

        private void showEndOfRoundDialog()

    DESCRIPTION

        The following information is displayed on the dialog:
            - both players' hand tiles
            - the tiles yet to be drawn from the stock
            - the winner of the round
            - points each player got in this round

    RETURNS

        None

    AUTHOR

        Ashish Ghimire

    DATE

        5/26/2019

    */
    private void showEndOfRoundDialog(){
        //Dialog
        final Dialog dialog = new Dialog(GameActivity.this);
        dialog.setTitle("Round Over: ");
        dialog.setContentView(R.layout.layout_end_of_round);
        dialog.setCanceledOnTouchOutside(false);

        showHandsAndStock( dialog );

        TextView roundWinner = dialog.findViewById(R.id.endofRoundRoundWinner);
        roundWinner.setText( m_round.getWinnerString() );

        TextView computerScore = dialog.findViewById(R.id.endOfRoundComputerPoints);
        computerScore.setText( "The computer player got " + m_round.getComputer().getScore() + " points");

        TextView humanScore = dialog.findViewById(R.id.endOfRoundHumanPoints);
        humanScore.setText( "The human player got " + m_round.getHuman().getScore() + " points");

        Button okay = dialog.findViewById(R.id.okayButton);
        //on click listener to OK button
        okay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //close the dialog
                startNewRound();
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
