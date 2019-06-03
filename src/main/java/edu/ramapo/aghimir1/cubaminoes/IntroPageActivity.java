package edu.ramapo.aghimir1.cubaminoes;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class IntroPageActivity extends AppCompatActivity {

    public static final String NEW_TOURNAMENT = "edu.ramapo.aghimir1.casino.NewTournamentObject";
    public static Tournament m_newTournament;

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

        4/21/2019

    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_page);
    }


    /**/
    /*
    showDialogNewGame

    NAME

        showDialogNewGame(View a_view )

    SYNOPSIS

        public void showDialogNewGame(View a_view )
            a_view - This View objects lets the function know the view from which the function
            was called

    DESCRIPTION

        Show a dialog box when "New Game" button is pressed. The dialog asks the user to input
        a tournament threshold score. This score determines when the tournament is over. Once a
        player gets points equal to or greater than the tournament score, the tournament is over

    RETURNS

        None

    AUTHOR

        Ashish Ghimire

    DATE

        4/24/2019

    */
    public void showDialogNewGame(View a_view ){
        //Dialog
        final Dialog dialog = new Dialog(this);
        dialog.setTitle("Enter max tournament score: ");
        dialog.setContentView(R.layout.layout_tournament_score_dialog);

        Button start = dialog.findViewById(R.id.button_start_new_game);

        //on click listener to OK button
        start.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //close the dialog
                if( startNewTournament(dialog) )
                    dialog.dismiss();
            }
        });

        dialog.show();
    }


    /**/
    /*
    startNewTournament

    NAME

        startNewTournament(Dialog a_dialog) - Initialize reference to a Tournament object and start
        a new activity (GameActivity). Pass the reference to a Tournament object to GameActivity
        as an intentExtra

    SYNOPSIS

        private boolean startNewTournament(Dialog a_dialog)
            a_dialog - Reference to a Dialog object

    DESCRIPTION

        Initialize reference to a Tournament object to form a link between the model classes
        and the view classes

    RETURNS

        False if the user did not enter valid integer input. True otherwise

    AUTHOR

        Ashish Ghimire

    DATE

        4/24/2019

    */
    private boolean startNewTournament(Dialog a_dialog){
        EditText editText = a_dialog.findViewById(R.id.editTextMaxTournamentScore);
        String text = editText.getText().toString();

        if( !(text.isEmpty() || Integer.parseInt(text) == 0) ){
            m_newTournament = new Tournament( Integer.parseInt(text) );
            Intent intent = new Intent(this, GameActivity.class);
            intent.putExtra(NEW_TOURNAMENT, m_newTournament);
            startActivity(intent);
            return true;
        }

        return false;
    }


    /**/
    /*
    loadNewTournament

    NAME

        loadNewTournament(Dialog a_dialog) - Load a new tournament by calling Tournament class'
                custom constructor

    SYNOPSIS

        public void loadNewTournament( View view )
            view - Reference to a View object

    DESCRIPTION
        Open up an alertbox that asks the user to select a previously saved game. Previously saved
        games are all textfiles. The user should select a valid text file for the game to load
        Initialize reference to a Tournament object and start a new activity (GameActivity). Pass the
        reference to a Tournament object to GameActivity as an intentExtra to form a link between the
        model classes and the view classes

    RETURNS

        None

    AUTHOR

        Ashish Ghimire

    DATE

        4/29/2019

    */
    public void loadNewTournament( View view ){

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Choose a game");

        final String fileDir = Environment.getExternalStorageDirectory().getAbsolutePath();
        final File[] files = new File(fileDir).listFiles();
        List<String> nameList = new ArrayList<>();

        for (File oneFile : files ){
            String name = oneFile.getName();
            if (name.endsWith(".txt")){
                nameList.add(name);
            }
        }

        String [] items = new String[nameList.size()];
        items = nameList.toArray(items);

        alert.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int these) {
                ListView select = ((AlertDialog)dialog).getListView();
                String fileName = (String) select.getAdapter().getItem(these);
                fileName = fileDir  + "/" +  fileName;

                m_newTournament = new Tournament( fileName );
                Intent intent = new Intent(IntroPageActivity.this, GameActivity.class);
                intent.putExtra(NEW_TOURNAMENT, m_newTournament);
                startActivity(intent);
            }
        });
        alert.show();

    }
}