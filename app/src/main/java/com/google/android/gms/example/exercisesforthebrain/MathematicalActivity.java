package com.google.android.gms.example.exercisesforthebrain;

import android.content.ContentValues;
import android.content.Intent;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.example.exercisesforthebrain.data.Contract;
import com.google.android.gms.example.exercisesforthebrain.utilities.GeneralUtilities;
import com.google.android.gms.example.exercisesforthebrain.utilities.Timer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;

public class MathematicalActivity extends AppCompatActivity {
    private static final String STATE_ANSWER1 = "01";
    private static final String STATE_ANSWER2 = "02";
    private static final String STATE_ANSWER3 = "03";
    private static final String STATE_ANSWER4 = "04";
    private static final String STATE_QUESTION = "05";
    private static final String STATE_TIMER = "0";
    private static final String STATE_ITEMSANSWERED = "06";
    private static final String STATE_BOOLEANSTART = "07";
    private static final String STATE_ANSWEREASERTED = "08";
    @BindView(R.id.floatingActionButton1)
    FloatingActionButton buttonAnswer1;
    @BindView(R.id.floatingActionButton2)
    FloatingActionButton buttonAnswer2;
    @BindView(R.id.floatingActionButton3)
    FloatingActionButton buttonAnswer3;
    @BindView(R.id.floatingActionButton4)
    FloatingActionButton buttonAnswer4;
    @BindView(R.id.buttonStartId)
    Button startChro;
    @BindView(R.id.buttonPerformanceMathId)
    Button perform;
    @BindView(R.id.textFabButton1Id)
    TextView textFabButton1;
    @BindView(R.id.textFabButton2Id)
    TextView textFabButton2;
    @BindView(R.id.textFabButton3Id)
    TextView textFabButton3;
    @BindView(R.id.textFabButton4Id)
    TextView textFabButton4;
    @BindView(R.id.textViewMathQuestionId)
    TextView textViewMathQuestion;
    @BindView(R.id.textViewSubtitleId)
    TextView subtitleApp;
    @BindView(R.id.textViewDescriptionExerId)
    TextView descriptionExercise;
    private Long baseTime;
    private Long referencetime;
    private boolean restore = false;
    private boolean startButtonboolean = false;
    private boolean clickPerformance = false;
    private Chronometer crono;
    private Integer[] posibleSelectedMathAnswer = {0, 1, 2, 3};
    private List<Integer> ListPosibleSelectedMath;
    private List<String> ListMathOperations;
    private String textMathFxercise;
    private String answer1;
    private String answer2;
    private String answer3;
    private String answer4;
    private int itemForTestMath = 1;
    private String sharedText;
    private String substrSharedText;
    private int correctAnswer = 0;
    private String unitType;
    private InterstitialAd mInterstitialAd;
    private ViewGroup mRootView;
    private Explode mExplode;

    protected void generatorValues() {
        //Select any of the first three options on the array over all colors available after shuffe
        Collections.shuffle(ListMathOperations);
        Collections.shuffle(ListPosibleSelectedMath);
        textMathFxercise = ListMathOperations.get(0);
        answer1 = ListMathOperations.get(ListPosibleSelectedMath.get(0));
        answer2 = ListMathOperations.get(ListPosibleSelectedMath.get(1));
        answer3 = ListMathOperations.get(ListPosibleSelectedMath.get(2));
        answer4 = ListMathOperations.get(ListPosibleSelectedMath.get(3));
    }

    protected int makeOperation(String textOperation) {
        int result = 0;

        if (unitType.equals("mediumlevel")) {
            String operator1 = textOperation.substring(0, 1);
            String operator2 = textOperation.substring(4);
            String operator = textOperation.substring(2, 3);
            int op1 = Integer.parseInt(operator1);
            int op2 = Integer.parseInt(operator2);

            switch (operator) { //mathematical operations process, basic level.
                case "+":
                    result = op1 + op2;
                    break;
                case "-":
                    result = op1 - op2;
                    break;
                case "x":
                    result = op1 * op2;
                    break;
                case "/":
                    result = op1 / op2;
                    break;
                default:
                    break;
            }

        } else if (unitType.equals("highlevel")) {
            String operator1 = textOperation.substring(0, 1);
            String operatora = textOperation.substring(2, 3);
            String operator3 = textOperation.substring(8);
            String operator2 = textOperation.substring(4, 5);
            String operatorb = textOperation.substring(6, 7);
            int op1 = Integer.parseInt(operator1);
            int op2 = Integer.parseInt(operator2);
            int op3 = Integer.parseInt(operator3);

            switch (operatora) { //Mathematical operations process, medium level.
                case "+":
                    result = op1 + op2;
                    break;
                case "-":
                    result = op1 - op2;
                    break;
                case "x":
                    result = op1 * op2;
                    break;
                case "/":
                    result = op1 / op2;
                    break;
            }

            switch (operatorb) {
                case "+":
                    result = result + op3;
                    break;
                case "-":
                    result = result - op3;
                    break;
                case "x":
                    result = result * op3;
                    break;
                case "/":
                    result = result / op3;
                    break;
            }

        }
        return result;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mathematical);

        ActionBar ab;
        String[] textMathArrayH;
        String[] textMathArray;
        // Get the root view and create a transition
        mRootView = findViewById(R.id.activityTextLayout);
        mExplode = new Explode();
        mExplode.setDuration(300);
        crono = findViewById(R.id.chronometerId);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.AddId));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        textMathArray = MainActivity.getMyMathArray();
        textMathArrayH = MainActivity.getMyMathArrayH();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        unitType = prefs.getString(getString(R.string.pref_difficult_level_key), getString(R.string.pref_dificult_level_medium));

        ButterKnife.bind(this);

        Intent intent = getIntent(); //Necesary to know the day of activity and for store stadistical infomration
        sharedText = intent.getStringExtra("dayTest"); //Necesary to know the day of activity and for store stadistical infomration
        substrSharedText = sharedText.substring(4);

        Transition enterTrans = new Explode();
        enterTrans.setDuration(300);
        getWindow().setEnterTransition(enterTrans);

        Transition returnTrans = new Explode();
        returnTrans.setDuration(300);
        Transition exitTrans = new Explode();
        exitTrans.setDuration(300);

        Transition reenterTrans = new Explode();
        reenterTrans.setDuration(300);

        Toolbar toolbar = findViewById(R.id.toolbarTestStartAlphaId);
        setSupportActionBar(toolbar);
        // Get a support ActionBar corresponding to this toolbar
        ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        if (unitType.equals("mediumlevel")) {
            ListMathOperations = Arrays.asList(textMathArray);
        } else if (unitType.equals("highlevel")) {
            ListMathOperations = Arrays.asList(textMathArrayH);
        }

        ListPosibleSelectedMath = Arrays.asList(posibleSelectedMathAnswer);

        generatorValues();

        textViewMathQuestion.setText(textMathFxercise);
        textViewMathQuestion.setContentDescription(textMathFxercise);
        textFabButton1.setText(String.valueOf(makeOperation(answer1)));
        textFabButton1.setContentDescription(String.valueOf(makeOperation(answer1)));
        textFabButton2.setText(String.valueOf(makeOperation(answer2)));
        textFabButton2.setContentDescription(String.valueOf(makeOperation(answer2)));
        textFabButton3.setText(String.valueOf(makeOperation(answer3)));
        textFabButton3.setContentDescription(String.valueOf(makeOperation(answer3)));
        textFabButton4.setText(String.valueOf(makeOperation(answer4)));
        textFabButton4.setContentDescription(String.valueOf(makeOperation(answer4)));


        buttonAnswer1.setEnabled(false);
        buttonAnswer2.setEnabled(false);
        buttonAnswer3.setEnabled(false);
        buttonAnswer4.setEnabled(false);


        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Code to be executed when when the interstitial ad is closed.
                Log.i("Ads", "onAdClosed");
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
                Intent intent = new Intent(MathematicalActivity.this, MainActivity.class);
                intent.putExtra("identity", sharedText);
                //Close "Day activity" and the current Activity.
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });


        crono.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    crono.setBackgroundColor(getColor(R.color.focus_button));
                } else {
                    crono.setBackgroundColor(Color.TRANSPARENT);
                }

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (startButtonboolean) {
            if (referencetime != null) {
                referencetime = SystemClock.elapsedRealtime() - baseTime;
                crono.setBase(referencetime);
                crono.start();
            }
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        if (startButtonboolean) {
            if (!clickPerformance) {
                baseTime = SystemClock.elapsedRealtime() - crono.getBase();
                referencetime = baseTime;
                crono.stop();
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        if (!clickPerformance) {
            // Save UI state changes to the savedInstanceState.

            savedInstanceState.putString(STATE_ANSWER1, answer1);
            savedInstanceState.putString(STATE_ANSWER2, answer2);
            savedInstanceState.putString(STATE_ANSWER3, answer3);
            savedInstanceState.putString(STATE_ANSWER4, answer4);
            savedInstanceState.putString(STATE_QUESTION, textMathFxercise);
            savedInstanceState.putInt(STATE_ITEMSANSWERED, itemForTestMath);
            savedInstanceState.putLong(STATE_TIMER, SystemClock.elapsedRealtime() - crono.getBase());
            savedInstanceState.putBoolean(STATE_BOOLEANSTART, startButtonboolean);
            savedInstanceState.putInt(STATE_ANSWEREASERTED, correctAnswer);

            super.onSaveInstanceState(savedInstanceState);
        }
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);
        // Restore state members from saved instance
        answer1 = savedInstanceState.getString(STATE_ANSWER1);
        answer2 = savedInstanceState.getString(STATE_ANSWER2);
        answer3 = savedInstanceState.getString(STATE_ANSWER3);
        answer4 = savedInstanceState.getString(STATE_ANSWER4);
        textMathFxercise = savedInstanceState.getString(STATE_QUESTION);
        itemForTestMath = savedInstanceState.getInt(STATE_ITEMSANSWERED);
        baseTime = savedInstanceState.getLong(STATE_TIMER);
        startButtonboolean = savedInstanceState.getBoolean(STATE_BOOLEANSTART);
        correctAnswer = savedInstanceState.getInt(STATE_ANSWEREASERTED);

        restore = true;
        if (startButtonboolean) {
            String questionMathanswered = getString(R.string.questionAnswered) + " " + itemForTestMath + getString(R.string.questionSing);
            descriptionExercise.setText(questionMathanswered);
            startprocedure();
        } else {
            baseTime = 0L;
            crono.stop();
        }
    }

    //Used for d-pad
    @OnFocusChange({
            R.id.buttonStartId,
            R.id.buttonPerformanceMathId
    })
    void onfocusbutton(boolean hasFocus, Button button) {
        switch (button.getId()) {
            case R.id.buttonStartId:
                if (hasFocus) {
                    startChro.setBackgroundColor(getColor(R.color.colorfocusmathedayexe));
                    startChro.setTextColor(Color.WHITE);
                } else {
                    startChro.setBackgroundColor(getColor(R.color.colorfabbuttondayactivity));
                    startChro.setTextColor(Color.WHITE);

                }
                break;
            case R.id.buttonPerformanceMathId:
                if (hasFocus) {
                    perform.setBackgroundColor(getColor(R.color.colorfocusmathedayexe));
                    perform.setTextColor(Color.WHITE);
                } else {
                    perform.setBackgroundColor(getColor(R.color.colorfabbuttondayactivity));
                    perform.setTextColor(Color.WHITE);

                }
                break;
        }
    }

    @OnFocusChange({
            R.id.textViewSubtitleId,
            R.id.textViewDescriptionExerId,
            R.id.textViewMathQuestionId,
            R.id.chronometerId,

    })
    void onfocustext(boolean hasFocus, TextView text) {
        switch (text.getId()) {
            case R.id.textViewSubtitleId:
                if (hasFocus) {
                    subtitleApp.setBackgroundColor(Color.TRANSPARENT);
                    subtitleApp.setTextColor(Color.WHITE);
                } else {
                    subtitleApp.setBackgroundColor(Color.TRANSPARENT);
                    subtitleApp.setTextColor(Color.WHITE);
                }
                break;
            case R.id.textViewDescriptionExerId:
                if (hasFocus) {
                    descriptionExercise.setBackgroundColor(getColor(R.color.focus_button));
                } else {
                    descriptionExercise.setBackgroundColor(Color.TRANSPARENT);
                }
                break;
            case R.id.textViewMathQuestionId:
                if (hasFocus) {
                    textViewMathQuestion.setBackgroundColor(getColor(R.color.focus_button));
                } else {
                    textViewMathQuestion.setBackgroundColor(Color.TRANSPARENT);
                }
                break;
        }
    }

    @OnClick({
            R.id.floatingActionButton1,
            R.id.floatingActionButton2,
            R.id.floatingActionButton3,
            R.id.floatingActionButton4
    })
    void floatingbuttons(FloatingActionButton buttonpresed) {
        switch (buttonpresed.getId()) {
            case R.id.floatingActionButton1: //Call a procedure to process the user answer, whit the correct answer
                wramUpTestColorActivity(textMathFxercise, 1, answer1, answer2, answer3, answer4);
                itemForTestMath++;
                break;
            case R.id.floatingActionButton2:
                wramUpTestColorActivity(textMathFxercise, 2, answer1, answer2, answer3, answer4);
                itemForTestMath++;
                break;
            case R.id.floatingActionButton3:
                wramUpTestColorActivity(textMathFxercise, 3, answer1, answer2, answer3, answer4);
                itemForTestMath++;
                break;
            case R.id.floatingActionButton4:
                wramUpTestColorActivity(textMathFxercise, 4, answer1, answer2, answer3, answer4);
                itemForTestMath++;
                break;
        }
    }

    private void startprocedure() {
        crono.setBase(SystemClock.elapsedRealtime());
        crono.start();
        if (restore) {
            referencetime = SystemClock.elapsedRealtime() - baseTime;
            crono.setBase(referencetime);
        }

        crono.setContentDescription(getString(R.string.chronometer));
        buttonAnswer1.setEnabled(true);
        buttonAnswer2.setEnabled(true);
        buttonAnswer3.setEnabled(true);
        buttonAnswer4.setEnabled(true);
        setValuesMathActivity(); //Update mathematical question

        TransitionManager.beginDelayedTransition(mRootView, mExplode);

        mRootView.removeView(startChro);
        mRootView.removeView(perform);
    }


    @OnClick({
            R.id.buttonStartId,
            R.id.buttonPerformanceMathId
    })
    void buttons(Button button) {
        switch (button.getId()) {
            case R.id.buttonStartId:
                startButtonboolean = true;
                startprocedure();
                break;
            case R.id.buttonPerformanceMathId:
                clickPerformance = true;
                Intent mathPerformance = new Intent(MathematicalActivity.this, PerformanceAcivity.class);
                mathPerformance.putExtra("data", "math");
                startActivity(mathPerformance);
                break;
        }
    }

    //Evaluate the answer, and if it is correct keep counter of corrects answers, and a green color if it correct.
    protected void wramUpTestColorActivity(final String questionMath, int value, final String answer1, final String answer2, final String answer3, final String answer4) {
        if (value == 1) {
            if (questionMath.equals(answer1)) {
                buttonAnswer1.setBackgroundTintList(getResources().getColorStateList(R.color.colorGeen, null));
                correctAnswer++;
            } else {
                buttonAnswer1.setBackgroundTintList(getResources().getColorStateList(R.color.colorRed, null));
            }
        } else if (value == 2) {
            if (questionMath.equals(answer2)) {
                buttonAnswer2.setBackgroundTintList(getResources().getColorStateList(R.color.colorGeen, null));
                correctAnswer++;
            } else {
                buttonAnswer2.setBackgroundTintList(getResources().getColorStateList(R.color.colorRed, null));
            }

        } else if (value == 3) {
            if (questionMath.equals(answer3)) {
                buttonAnswer3.setBackgroundTintList(getResources().getColorStateList(R.color.colorGeen, null));
                correctAnswer++;
            } else {
                buttonAnswer3.setBackgroundTintList(getResources().getColorStateList(R.color.colorRed, null));
            }
        } else if (value == 4) {
            if (questionMath.equals(answer4)) {
                buttonAnswer4.setBackgroundTintList(getResources().getColorStateList(R.color.colorGeen, null));
                correctAnswer++;
            } else {
                buttonAnswer4.setBackgroundTintList(getResources().getColorStateList(R.color.colorRed, null));
            }
        }

        buttonAnswer1.setEnabled(false);
        buttonAnswer2.setEnabled(false);
        buttonAnswer3.setEnabled(false);
        buttonAnswer4.setEnabled(false);
        delayResultextColor();
    }

    protected void setValuesMathActivity() {//Set color of each fab, and de color of the text, and the text that the user can see.
        if (!restore) {
            generatorValues();
        }
        textViewMathQuestion.setText(textMathFxercise);
        textViewMathQuestion.setContentDescription(textMathFxercise);
        textFabButton1.setText(String.valueOf(makeOperation(answer1)));
        textFabButton1.setContentDescription(String.valueOf(makeOperation(answer1)));
        textFabButton2.setText(String.valueOf(makeOperation(answer2)));
        textFabButton2.setContentDescription(String.valueOf(makeOperation(answer2)));
        textFabButton3.setText(String.valueOf(makeOperation(answer3)));
        textFabButton3.setContentDescription(String.valueOf(makeOperation(answer3)));
        textFabButton4.setText(String.valueOf(makeOperation(answer4)));
        textFabButton4.setContentDescription(String.valueOf(makeOperation(answer4)));
    }

    protected void delayResultextColor() { //Delay to show result, and generate new value for the next question.
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                buttonAnswer1.setBackgroundTintList(getResources().getColorStateList(R.color.colorPurple, null));
                buttonAnswer2.setBackgroundTintList(getResources().getColorStateList(R.color.colorPurple, null));
                buttonAnswer3.setBackgroundTintList(getResources().getColorStateList(R.color.colorPurple, null));
                buttonAnswer4.setBackgroundTintList(getResources().getColorStateList(R.color.colorPurple, null));
                generatorValues();
                setValuesMathActivity();

                buttonAnswer1.setEnabled(true);
                buttonAnswer2.setEnabled(true);
                buttonAnswer3.setEnabled(true);
                buttonAnswer4.setEnabled(true);
                String questionMathanswered = getString(R.string.questionAnswered) + " " + itemForTestMath + getString(R.string.questionSing);
                descriptionExercise.setText(questionMathanswered);
            }
        }, 300);
        if (itemForTestMath == 30) { //After 30th question, save on local data base, show the interstitial if it is ready to be showed.
            String timeElapsed = Timer.showElapsedTime(crono);
            ContentValues quoteCVA = new ContentValues();
            //correctAnswer = correctAnswer+"\"\\n\" ;
            quoteCVA.put(Contract.Entry.COLUMN_MATHEM, String.valueOf(correctAnswer + "," + timeElapsed));
            GeneralUtilities.requestToLocalDataBase(MathematicalActivity.this, substrSharedText, quoteCVA);

            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
            } else {
                Intent intent = new Intent(MathematicalActivity.this, MainActivity.class);
                intent.putExtra("identity", sharedText);
                //Close "Day activity" and the current Activity.  | Intent.FLAG_ACTIVITY_SINGLE_TOP
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { // Used for go to home activity
        switch (item.getItemId()) {
            case android.R.id.home:
                finishAfterTransition();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
