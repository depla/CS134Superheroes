package edu.miracosta.cs134;

import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
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
 * Activity that displays the game to the user and handles the gameplay. Lets the user choose
 * what quiz they want to take
 *
 * @author Dennis La
 * @version 1.0
 */
public class QuizActivity extends AppCompatActivity {

    private static final String TAG = "CS134 Super Hero Quiz";
    private static final String NAME_QUIZ = "NAME";
    private static final String POWER_QUIZ = "POWER";
    private static final String ONE_THING_QUIZ = "ONE_THING";

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
    private TextView mGuessHeroTextView;
    private ImageView mHeroImageView; // displays a hero
    private TextView mAnswerTextView; // displays correct answer

    private String quizTypeString = NAME_QUIZ;
    private MediaPlayer correctSound;
    private MediaPlayer incorrectSound;

    /**
     * Creates and inflates the layout of the quiz and also initializes the sounds
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        mQuizHeroesList = new ArrayList<>(HEROES_IN_QUIZ);
        rng = new SecureRandom();
        handler = new Handler();

        // DONE: Get references to GUI components (textviews and imageview)
        mQuestionNumberTextView = findViewById(R.id.questionNumberTextView);
        mHeroImageView = findViewById(R.id.heroImageView);
        mAnswerTextView = findViewById(R.id.answerTextView);
        mGuessHeroTextView = findViewById(R.id.guessHeroTextView);

        correctSound = MediaPlayer.create(this, R.raw.success);
        incorrectSound = MediaPlayer.create(this, R.raw.failed);

        // DONE: Put all 4 buttons in the array (mButtons)
        mButtons[0] = findViewById(R.id.button);
        mButtons[1] = findViewById(R.id.button2);
        mButtons[2] = findViewById(R.id.button3);
        mButtons[3] = findViewById(R.id.button4);

        // DONE: Set mQuestionNumberTextView's text to the appropriate strings.xml resource
        mQuestionNumberTextView.setText(getString(R.string.question, 1, HEROES_IN_QUIZ));

        // DONE: Load all the heroes from the JSON file using the JSONLoader
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

        // DONE: Randomly add HEROES_IN_QUIZ (10) heroes from the mAllHeroesList into the mQuizHeroesList
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

        // DONE: Start the quiz by calling loadNextHero
        loadNextHero();
    }

    /**
     * Method initiates the process of loading the next hero for the quiz, showing
     * the hero's image and then 4 buttons, one of which contains the correct answer.
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

            if(quizTypeString.equals(NAME_QUIZ))
            {
                mButtons[i].setText(mAllHeroesList.get(i).getName());
            }
            else if(quizTypeString.equals(POWER_QUIZ))
            {
                mButtons[i].setText(mAllHeroesList.get(i).getSuperPower());
            }
            else if(quizTypeString.equals(ONE_THING_QUIZ))
            {
                mButtons[i].setText(mAllHeroesList.get(i).getOneThing());
            }
        }

        // DONE: in the all heroes list


        // DONE: After the loop, randomly replace one of the 4 buttons with the name of the correct hero

        if(quizTypeString.equals(NAME_QUIZ))
        {
            mButtons[rng.nextInt(mButtons.length)].setText(mCorrectHero.getName());
        }
        else if(quizTypeString.equals(POWER_QUIZ))
        {
            mButtons[rng.nextInt(mButtons.length)].setText(mCorrectHero.getSuperPower());
        }
        else if(quizTypeString.equals(ONE_THING_QUIZ))
        {
            mButtons[rng.nextInt(mButtons.length)].setText(mCorrectHero.getOneThing());
        }

    }

    /**
     * Handles the click event of one of the 4 buttons indicating the guess of a hero's attribute
     * to match the hero image displayed.  If the guess is correct, the hero's name (in GREEN) will be shown,
     * followed by a slight delay of 2 seconds, then the next hero will be loaded.  Otherwise, the
     * word "Incorrect Guess" will be shown in RED and the button will be disabled.
     * @param v the button pressed
     */
    public void makeGuess(View v) {

        mTotalGuesses++;

        // DONE: Downcast the View v into a Button (since it's one of the 4 buttons)
        Button clickedButton = (Button) v;

        // DONE: Get the heroes's attribute from the text of the button
        String guessedItem = clickedButton.getText().toString();

        // DONE: If the guess matches the correct heroes's attribute, increment the number of correct guesses,
        String correctItem = "";

        if(quizTypeString.equals(NAME_QUIZ))
        {
            correctItem = mCorrectHero.getName();
        }
        else if(quizTypeString.equals(POWER_QUIZ))
        {
            correctItem = mCorrectHero.getSuperPower();
        }
        else if(quizTypeString.equals(ONE_THING_QUIZ))
        {
            correctItem = mCorrectHero.getOneThing();
        }

        // DONE: then display correct answer in green text.  Also, disable all 4 buttons (can't keep guessing once it's correct)
        // DONE: Nested in this decision, if the user has completed all 10 questions, show an AlertDialog
        // DONE: with the statistics and an option to Reset Quiz

        // DONE: Else, the answer is incorrect, so display "Incorrect Guess!" in red
        // DONE: and disable just the incorrect button.

        if(guessedItem.equalsIgnoreCase(correctItem))
        {
            correctSound.start();

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
                mAnswerTextView.setText(correctItem);
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
                correctSound.start();

                //change the answer text to correct answer
                //make the text green
                mAnswerTextView.setText(correctItem);
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
            incorrectSound.start();
            //disable the button
            clickedButton.setEnabled(false);
            mAnswerTextView.setText(getString(R.string.incorrect_answer));
            mAnswerTextView.setTextColor(getResources().getColor(R.color.incorrect_answer));
        }

    }


    /**
     * inflates settings_drop_down.xml
     *
     * @param menu the settings menu
     * @return a boolean
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //getMenuInflater().inflate(R.menu.menu_settings, menu); //for menu icon

        getMenuInflater().inflate(R.menu.settings_drop_down, menu);

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * sets the quiz type depending on item pressed
     *
     * @param item the setting MenuItem pressed
     * @return a boolean
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Intent settingsIntent = new Intent(this, SettingsActivity.class); //for settings fragment

        //startActivity(settingsIntent); //for settings fragment

        switch (item.getItemId())
        {
            case R.id.nameButton:
            {
                quizTypeString = NAME_QUIZ;
                mGuessHeroTextView.setText(R.string.guess_superhero);
                break;
            }
            case R.id.powerButton:
            {
                quizTypeString = POWER_QUIZ;
                mGuessHeroTextView.setText(R.string.guess_superpower);
                break;
            }
            case R.id.oneThingButton:
            {
                quizTypeString = ONE_THING_QUIZ;
                mGuessHeroTextView.setText(R.string.guess_the_one_thing);
                break;
            }

        }

        resetQuiz();

        return super.onOptionsItemSelected(item);
    }


    /**
     * unfinished OnSharedPreferenceChangeListener, couldn't get it to work :(
     */
    /*SharedPreferences.OnSharedPreferenceChangeListener
            mSharedPreferenceChangeListener =
            new SharedPreferences.OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                                      String key)
                {
                    if (key.equals("pref_typeOfQuiz"))
                    {
                        String quizType = sharedPreferences.getString("pref_typeOfQuiz", getString(R.string.default_quiz));


                        //updateQuiz(quizType);
                        resetQuiz();
                    }

                    Toast.makeText(QuizActivity.this, R.string.restarting_quiz, Toast.LENGTH_SHORT).show();
                } };
*/

}