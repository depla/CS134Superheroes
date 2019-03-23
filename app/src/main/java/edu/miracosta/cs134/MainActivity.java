package edu.miracosta.cs134;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.miracosta.cs134.model.JSONLoader;
import edu.miracosta.cs134.model.SuperHero;

/**
 * Activity that displays the game to the user and handles the gameplay.
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "CS134 Super Hero Quiz";

    private static final int HEROES_IN_QUIZ = 10;

    private Button[] mButtons = new Button[4];
    private List<SuperHero> mAllHeroesList;  // all the heroes loaded from JSON
    private List<SuperHero> mQuizHeroesList; // heroes in current quiz (just 10 of them)
    private SuperHero mCorrectHero; // correct hero for the current question
    private int mTotalGuesses; // number of total guesses made
    private int mCorrectGuesses; // number of correct guesses
    private SecureRandom rng; // used to randomize the quiz
    private Handler handler; // used to delay loading next hero

    private TextView mQuestionNumberTextView; // shows current question #
    private ImageView mHeroImageView; // displays a hero
    private TextView mAnswerTextView; // displays correct answer

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mQuizHeroesList = new ArrayList<>(HEROES_IN_QUIZ);
        rng = new SecureRandom();
        handler = new Handler();

        // DONE: Get references to GUI components (textviews and imageview)
        mQuestionNumberTextView = findViewById(R.id.questionNumberTextView);
        mHeroImageView = findViewById(R.id.heroImageView);
        mAnswerTextView = findViewById(R.id.answerTextView);

        // DONE: Put all 4 buttons in the array (mButtons)
        mButtons[0] = findViewById(R.id.button);
        mButtons[1] = findViewById(R.id.button2);
        mButtons[2] = findViewById(R.id.button3);
        mButtons[3] = findViewById(R.id.button4);

        // DONE: Set mQuestionNumberTextView's text to the appropriate strings.xml resource
        mQuestionNumberTextView.setText(getString(R.string.question, 1, HEROES_IN_QUIZ));

        // DONE: Load all the countries from the JSON file using the JSONLoader
        try
        {
            mAllHeroesList = JSONLoader.loadJSONFromAsset(this);
        }
        catch (IOException e)
        {
            Log.e(TAG, e.getMessage());
        }

        // DONE: Call the method resetQuiz() to start the quiz.
        resetQuiz();

    }

    /**
     * Sets up and starts a new quiz.
     */
    public void resetQuiz() {

        // DONE: Reset the number of correct guesses made
        mCorrectGuesses = 0;

        // DONE: Reset the total number of guesses the user made
        mTotalGuesses = 0;

        // DONE: Clear list of quiz heroes (for prior games played)
        mQuizHeroesList.clear();

        // TODO: Randomly add FLAGS_IN_QUIZ (10) countries from the mAllCountriesList into the mQuizCountriesList
        int size = mAllHeroesList.size();
        int randomPosition;
        SuperHero randomHero;
        while(mQuizHeroesList.size() <= HEROES_IN_QUIZ)
        {
            randomPosition = rng.nextInt(size);
            randomHero = mAllHeroesList.get(randomPosition);

            // DONE: Ensure no duplicate heroes (e.g. don't add a hero if it's already in mQuizHeroesList)
            //check for duplicates
            //if quiz list DOESNT contain random hero, add it!
            if(!mQuizHeroesList.contains(randomHero))
            {
                mQuizHeroesList.add(randomHero);
            }
        }

        //Lets set the text of the 4 buttons to the first 4 hero names
        for(int i = 0; i < mButtons.length; i++)
        {
            mButtons[i].setText(mQuizHeroesList.get(i).getName());
        }



        // DONE: Start the quiz by calling loadNextHero
        loadNextHero();
    }

    /**
     * Method initiates the process of loading the next flag for the quiz, showing
     * the flag's image and then 4 buttons, one of which contains the correct answer.
     */
    private void loadNextHero() {
        // DONE: Initialize the mCorrectHero by removing the item at position 0 in the mQuizHeroesList
        mCorrectHero = mQuizHeroesList.get(0);
        mQuizHeroesList.remove(0);

        // DONE: Clear the mAnswerTextView so that it doesn't show text from the previous question
        mAnswerTextView.setText("");

        // DONE: Display current question number in the mQuestionNumberTextView
        mQuestionNumberTextView.setText(getString(R.string.question, mCorrectGuesses + 1, HEROES_IN_QUIZ));


        // DONE: Use AssetManager to load next image from assets folder
        AssetManager am = getAssets();

        try {
            InputStream stream = am.open(mCorrectHero.getImageName());
            Drawable image = Drawable.createFromStream(stream, mCorrectHero.getName());
            mHeroImageView.setImageDrawable(image);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }




        // DONE: Get an InputStream to the asset representing the next hero
        // DONE: and try to use the InputStream to create a Drawable
        // DONE: The file name can be retrieved from the correct hero's file name.
        // DONE: Set the image drawable to the correct hero.

        // DONE: Shuffle the order of all the heroes (use Collections.shuffle)

        do {
            Collections.shuffle(mAllHeroesList);

        }while(mAllHeroesList.subList(0, mButtons.length).contains(mCorrectHero));

        // DONE: Loop through all 4 buttons, enable them all and set them to the first 4 heroes
        for(int i = 0; i < mButtons.length; i++)
        {
            mButtons[i].setEnabled(true);
            mButtons[i].setText(mAllHeroesList.get(i).getName());
        }

        // DONE: in the all heroes list


        // DONE: After the loop, randomly replace one of the 4 buttons with the name of the correct hero
        mButtons[rng.nextInt(mButtons.length)].setText(mCorrectHero.getName());

    }

    /**
     * Handles the click event of one of the 4 buttons indicating the guess of a country's name
     * to match the flag image displayed.  If the guess is correct, the country's name (in GREEN) will be shown,
     * followed by a slight delay of 2 seconds, then the next flag will be loaded.  Otherwise, the
     * word "Incorrect Guess" will be shown in RED and the button will be disabled.
     * @param v
     */
    public void makeGuess(View v) {

        mTotalGuesses++;

        // TODO: Downcast the View v into a Button (since it's one of the 4 buttons)
        Button clickedButton = (Button) v;

        // TODO: Get the heroes's name from the text of the button
        String guessedName = clickedButton.getText().toString();

        // TODO: If the guess matches the correct heroes's name, increment the number of correct guesses,
        if(guessedName.equalsIgnoreCase(mCorrectHero.getName()))
        {
            mCorrectGuesses++;

            //game is not over yet! ( < 10 )
            if(mCorrectGuesses < HEROES_IN_QUIZ)
            {
                //disable all the buttons
                for(int i = 0; i < mButtons.length; i++)
                {
                    mButtons[i].setEnabled(false);
                }
                //change the answer text to correct answer
                //make the text green
                mAnswerTextView.setText(mCorrectHero.getName());
                mAnswerTextView.setTextColor(getResources().getColor(R.color.correct_answer));

                //call load next hero after pausing for 2 seconds == 2000 ms
                //use a handler to delay actions
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadNextHero();
                    }
                }, 2000);


            }
            else //game over
            {
                //change the answer text to correct answer
                //make the text green
                mAnswerTextView.setText(mCorrectHero.getName());
                mAnswerTextView.setTextColor(getResources().getColor(R.color.correct_answer));

                //create an alert dialog with some text and a button to reset the quiz (Start a new game)
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                double percentage = (double) mCorrectGuesses / mTotalGuesses * 100.0;

                builder.setMessage(getString(R.string.results, mTotalGuesses, percentage));

                builder.setPositiveButton(getString(R.string.reset_quiz), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        resetQuiz();
                    }
                });


                //disable the cancel operation (cant cancel dialog)
                builder.setCancelable(false);

                //create dialog
                builder.create();

                //show the dialog
                builder.show();
            }
        }
        else //incorrect guess
        {
            //disable the button
            clickedButton.setEnabled(false);
            mAnswerTextView.setText(getString(R.string.incorrect_answer));
            mAnswerTextView.setTextColor(getResources().getColor(R.color.incorrect_answer));
        }

        // TODO: then display correct answer in green text.  Also, disable all 4 buttons (can't keep guessing once it's correct)
        // TODO: Nested in this decision, if the user has completed all 10 questions, show an AlertDialog
        // TODO: with the statistics and an option to Reset Quiz

        // TODO: Else, the answer is incorrect, so display "Incorrect Guess!" in red
        // TODO: and disable just the incorrect button.



    }


    /**
     * inflates menu_settings.xml
     *
     * @param menu the settings menu
     * @return
     */
    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_settings, menu);

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * starts the SettingsActivity when the setting MenuItem is selected
     *
     * @param item the setting MenuItem pressed
     * @return
     */
    /*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent settingsIntent = new Intent(this, SettingsActivity.class);

        startActivity(settingsIntent);

        return super.onOptionsItemSelected(item);
    }
    */


    /*SharedPreferences.OnSharedPreferenceChangeListener
            mSharedPreferenceChangeListener =
            new SharedPreferences.OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                                      String key) {
                    if (key.equals(REGIONS)) {
                        String region = sharedPreferences.getString(REGIONS, getString(R.string.default_region));
                        updateRegion(region);
                        resetQuiz();
                    }
                    else if (key.equals(CHOICES)) {
                        mChoices = Integer.parseInt(sharedPreferences.getString(CHOICES, getString(R.string.default_choices)));
                        updateChoices();
                        resetQuiz();
                    }
                    Toast.makeText(MainActivity.this, R.string.restarting_quiz, Toast.LENGTH_SHORT).show();
                } };

                */
}