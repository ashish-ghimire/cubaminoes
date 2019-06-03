package edu.ramapo.aghimir1.cubaminoes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class GameOverActivity extends AppCompatActivity {

    /**/
    /*
    onCreate

    NAME

        onCreate(Bundle savedInstanceState) - Called when this activity is created

    SYNOPSIS

        protected void onCreate(Bundle savedInstanceState)

    DESCRIPTION

        Gets called when this activity is created

    RETURNS

        None

    AUTHOR

        Ashish Ghimire

    DATE

        4/21/2019

    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        Intent intent = getIntent();
        setUpText( intent );
    }


    /**/
    /*
    setUpText

    NAME

        setUpText( Intent intent ) - Set the value of all almost all textboxes used in this activity

    SYNOPSIS

        private void setUpText( Intent intent )
            intent - an Intent object that is used to extract values for the textboxes

    DESCRIPTION

        Set the value of all almost all textboxes used in this activity

    RETURNS

        None

    AUTHOR

        Ashish Ghimire

    DATE

        4/21/2019

    */
    private void setUpText( Intent intent ){
        TextView textView = (TextView) findViewById( R.id.gameOverHumanTournament);
        String text = textView.getText() + intent.getStringExtra(GameActivity.HUMAN_FINAL_TOURNAMENT_SCORE );
        textView.setText(text);

        textView = (TextView) findViewById( R.id.gameOverComputerTournament );
        text = textView.getText() + intent.getStringExtra( GameActivity.COMPUTER_FINAL_TOURNAMENT_SCORE );
        textView.setText(text);

        textView = (TextView) findViewById( R.id.gameOverWinner );
        text = textView.getText() + intent.getStringExtra( GameActivity.TOURNAMENT_WINNER);
        textView.setText(text);
    }


    /**/
    /*
    endApplication

    NAME

        endApplication( View view ) - End the activity and the entire game

    SYNOPSIS

        public void endApplication( View view )
            view, a View variable - the GameOVerActivity's main view


    DESCRIPTION

     End the activity and the entire game. After execution, this method
     causes the app to exit and return view to the device's home screen

    RETURNS

        None

    AUTHOR

        Ashish Ghimire

    DATE

        4/21/2019

    */
    public void endApplication( View view ) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }
}
