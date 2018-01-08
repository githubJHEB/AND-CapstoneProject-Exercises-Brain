package com.google.android.gms.example.exercisesforthebrain;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Handler;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

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


public class TestColorActivity extends AppCompatActivity {
    private static final String STATE_COLORA = "01";
    private static final String STATE_COLORB = "02";
    private static final String STATE_COLORC = "03";
    private static final String STATE_TEXTTITLE = "04";
    private static final String STATE_COLORTITLE = "05";
    private static final String STATE_TIMER = "0";
    private static final String STATE_ITEMSANSWERED = "06";
    private static final String STATE_BOOLEANSTART = "07";
    private static final String STATE_ANSWEREASERTED = "08";
    @BindView(R.id.fabOption1TestColorId)
    FloatingActionButton fabOption1;
    @BindView(R.id.fabOption2TestColorId)
    FloatingActionButton fabOption2;
    @BindView(R.id.fabOption3TestColorId)
    FloatingActionButton fabOption3;
    @BindView(R.id.buttonStartId)
    Button startChro;
    @BindView(R.id.buttonPerformanceColorId)
    Button perform;
    @BindView(R.id.textViewSubtitleId)
    TextView subtitleApp;
    @BindView(R.id.textViewDescriptionExerId)
    TextView descriptionExercise;
    @BindView(R.id.textViewMathQuestionId)
    TextView textViewWordColorId;
    private Long baseTime;
    private Long referencetime;
    private boolean restore = false;
    private boolean startButtonboolean = false;
    private boolean clickPerformance = false;
    private Chronometer crono;
    private ActionBar ab;
    private int itemForTestColor = 1;
    private String textTitleForColorExercise;
    private int intColorTitle;
    private int colorA;
    private int colorB;
    private int colorC;
    private List<Integer> ListPosibleposibleSelectedColorsTitle;
    private List<Integer> ListPosibleSelectedColorText;
    private List<String> ListText;
    private Explode mExplode;
    private ViewGroup mRootView;
    private String sharedText;
    private int correctAnswer = 0;
    private String substrSharedText;
    private Context context;
    private String[] textColorArray = {"PURPLE", "YELLOW", "ORANGE", "GREEN", "BLUE", "RED", "GREY", "BLACK", "BROWN"};
    private Integer[] posibleSelectedColorsText = {0, 1, 2};
    private Integer[] posibleSelectedColorsTitle = {0, 1, 2};

    protected void generatorValues() {
        String textColorSelection;
        Collections.shuffle(ListText);
        Collections.shuffle(ListPosibleposibleSelectedColorsTitle);
        Collections.shuffle(ListPosibleSelectedColorText);
        //Select any of the first three options on the array over all colors available after shuffe
        textTitleForColorExercise = ListText.get(ListPosibleposibleSelectedColorsTitle.get(0));
        //Selec the first option after shuffe the three first text colors
        textColorSelection = ListText.get(ListPosibleSelectedColorText.get(0));
        //Extract the int value of the text Color
        intColorTitle = getColorFromText(textColorSelection);
        colorA = getColorFromText(ListText.get(0));
        colorB = getColorFromText(ListText.get(1));
        colorC = getColorFromText(ListText.get(2));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_color);
        ButterKnife.bind(this);
        context = getApplicationContext();
        Intent intent = getIntent(); //Required to know the day of activity and for store statistic information
        sharedText = intent.getStringExtra("dayTest");
        substrSharedText = sharedText.substring(4);


        // Get the root view and create a transition
        mRootView = findViewById(R.id.activityTextLayout);
        mExplode = new Explode();
        mExplode.setDuration(300);


        ListPosibleposibleSelectedColorsTitle = Arrays.asList(posibleSelectedColorsTitle);
        ListPosibleSelectedColorText = Arrays.asList(posibleSelectedColorsText);
        ListText = Arrays.asList(textColorArray);
        generatorValues();

        startChro = findViewById(R.id.buttonStartId);
        crono = findViewById(R.id.chronometerId);

        Transition enterTrans = new Explode();
        enterTrans.setDuration(300);
        getWindow().setEnterTransition(enterTrans);

        Transition returnTrans = new Explode();
        returnTrans.setDuration(300);


        Transition exitTrans = new Explode();
        exitTrans.setDuration(300);

        Transition reenterTrans = new Explode();
        reenterTrans.setDuration(800);

        Toolbar toolbar = findViewById(R.id.toolbarTestStartAlphaId);
        setSupportActionBar(toolbar);
        // Get a support ActionBar corresponding to this toolbar
        ab = getSupportActionBar();

        // Enable the Up button
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        setValuesColorActivity();
        fabOption1.setEnabled(false);
        fabOption2.setEnabled(false);
        fabOption3.setEnabled(false);


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

            savedInstanceState.putInt(STATE_COLORA, colorA);
            savedInstanceState.putInt(STATE_COLORB, colorB);
            savedInstanceState.putInt(STATE_COLORC, colorC);
            savedInstanceState.putInt(STATE_COLORTITLE, intColorTitle);
            savedInstanceState.putString(STATE_TEXTTITLE, textTitleForColorExercise);
            savedInstanceState.putInt(STATE_ITEMSANSWERED, itemForTestColor);
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
        colorA = savedInstanceState.getInt(STATE_COLORA);
        colorB = savedInstanceState.getInt(STATE_COLORB);
        colorC = savedInstanceState.getInt(STATE_COLORC);
        intColorTitle = savedInstanceState.getInt(STATE_COLORTITLE);
        textTitleForColorExercise = savedInstanceState.getString(STATE_TEXTTITLE);
        itemForTestColor = savedInstanceState.getInt(STATE_ITEMSANSWERED);
        baseTime = savedInstanceState.getLong(STATE_TIMER);
        startButtonboolean = savedInstanceState.getBoolean(STATE_BOOLEANSTART);
        correctAnswer = savedInstanceState.getInt(STATE_ANSWEREASERTED);

        restore = true;
        if (startButtonboolean) {
            String questionColorAnswered = getString(R.string.questionAnswered) + " " + itemForTestColor + getString(R.string.questionSing);
            descriptionExercise.setText(questionColorAnswered);
            startprocedure();
        } else {
            baseTime = 0L;
            crono.stop();
            setValuesColorActivity();
        }
    }

    @OnFocusChange({
            R.id.buttonStartId,
            R.id.buttonPerformanceColorId
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
            case R.id.buttonPerformanceColorId:
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
                    textViewWordColorId.setBackgroundColor(getColor(R.color.focus_button));
                } else {
                    textViewWordColorId.setBackgroundColor(Color.TRANSPARENT);
                }
                break;
        }
    }

    private void startprocedure() {
        ab.setDisplayHomeAsUpEnabled(false);
        crono.setBase(SystemClock.elapsedRealtime());
        crono.start();
        if (restore) {
            referencetime = SystemClock.elapsedRealtime() - baseTime;
            crono.setBase(referencetime);
        }
        setValuesColorActivity();
        fabOption1.setEnabled(true);
        fabOption2.setEnabled(true);
        fabOption3.setEnabled(true);
        // Start recording changes to the view hierarchy
        TransitionManager.beginDelayedTransition(mRootView, mExplode);

        // Add the new TextView to the view hierarchy
        mRootView.removeView(startChro);
        mRootView.removeView(perform);
    }

    @OnClick({
            R.id.buttonStartId,
            R.id.buttonPerformanceColorId
    })
    void buttons(Button button) {
        switch (button.getId()) {
            case R.id.buttonStartId:
                startButtonboolean = true;
                generatorValues();
                startprocedure();
                break;
            case R.id.buttonPerformanceColorId:
                clickPerformance = true;
                Intent mathPerformance = new Intent(TestColorActivity.this, PerformanceAcivity.class);
                mathPerformance.putExtra("data", "color");
                startActivity(mathPerformance);
                break;
        }
    }

    @OnClick({
            R.id.fabOption1TestColorId,
            R.id.fabOption2TestColorId,
            R.id.fabOption3TestColorId,
    })
    void floatingbuttons(FloatingActionButton buttonpresed) {
        switch (buttonpresed.getId()) {
            case R.id.fabOption1TestColorId:
                wramUpTestColorActivity(intColorTitle, 1, colorA, colorB, colorC);
                itemForTestColor++;
                break;
            case R.id.fabOption2TestColorId:
                wramUpTestColorActivity(intColorTitle, 2, colorA, colorB, colorC);
                itemForTestColor++;
                break;
            case R.id.fabOption3TestColorId:
                wramUpTestColorActivity(intColorTitle, 3, colorA, colorB, colorC);
                itemForTestColor++;
                break;

        }
    }

    protected int getColorFromText(String textColor) {
        switch (textColor) {
            case "PURPLE":
                return getColor(R.color.colorPurple);
            case "YELLOW":
                return getColor(R.color.colorYellow);
            case "ORANGE":
                return getColor(R.color.colorOrange);
            case "GREEN":
                return getColor(R.color.colorGeen);
            case "BLUE":
                return getColor(R.color.colorBlue);
            case "RED":
                return getColor(R.color.colorRed);
            case "GREY":
                return getColor(R.color.colorGrey);
            case "BLACK":
                return getColor(R.color.colorBlack);
            case "BROWN":
                return getColor(R.color.colorBrown);
        }
        return 0;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finishAfterTransition();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void setValuesColorActivity() {
        if (!restore) {
            generatorValues();
        }
        textViewWordColorId.setText(textTitleForColorExercise);
        textViewWordColorId.setTextColor(intColorTitle);
        fabOption1.setBackgroundTintList(ColorStateList.valueOf(colorA));
        fabOption1.setContentDescription(ListText.get(0));
        fabOption2.setBackgroundTintList(ColorStateList.valueOf(colorB));
        fabOption2.setContentDescription(ListText.get(1));
        fabOption3.setBackgroundTintList(ColorStateList.valueOf(colorC));
        fabOption3.setContentDescription(ListText.get(2));
    }

    protected void wramUpTestColorActivity(final int colorint, int value, final int colorA, final int colorB, final int colorC) {

        if (value == 1) {
            if (colorint - colorA == 0) {
                fabOption1.setImageResource((R.drawable.ic_check_white_48dp));
                correctAnswer++;
            } else {
                fabOption1.setImageResource((R.drawable.ic_clear_white_48dp));
            }
        } else if (value == 2) {
            if (colorint - colorB == 0) {
                fabOption2.setImageResource((R.drawable.ic_check_white_48dp));
                correctAnswer++;
            } else {
                fabOption2.setImageResource((R.drawable.ic_clear_white_48dp));
            }

        } else if (value == 3) {
            if (colorint - colorC == 0) {
                fabOption3.setImageResource((R.drawable.ic_check_white_48dp));
                correctAnswer++;
            } else {
                fabOption3.setImageResource((R.drawable.ic_clear_white_48dp));
            }
        }
        fabOption1.setEnabled(false);
        fabOption2.setEnabled(false);
        fabOption3.setEnabled(false);
        delayResultextColor();
    }

    protected void delayResultextColor() { //Delay to show result, and generate new value for the next question.
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                fabOption1.setImageResource(android.R.color.transparent);
                fabOption2.setImageResource(android.R.color.transparent);
                fabOption3.setImageResource(android.R.color.transparent);
                TransitionManager.beginDelayedTransition(mRootView, mExplode);
                generatorValues();
                setValuesColorActivity();
                fabOption1.setEnabled(true);
                fabOption2.setEnabled(true);
                fabOption3.setEnabled(true);
                String questionColorAnswered = getString(R.string.questionAnswered) + " " + itemForTestColor + getString(R.string.questionSing);
                descriptionExercise.setText(questionColorAnswered);
            }
        }, 500);
        if (itemForTestColor == 30) {
            String timeElapsed = Timer.showElapsedTime(crono);
            ContentValues quoteCVA = new ContentValues();
            quoteCVA.put(Contract.Entry.COLUMN_COLOR, String.valueOf(correctAnswer + "," + timeElapsed));
            GeneralUtilities.requestToLocalDataBase(context, substrSharedText, quoteCVA);

            Intent intent = new Intent(TestColorActivity.this, DayActivity.class);
            intent.putExtra("identity", sharedText);
            //Close "Day activity" and the current Activity.
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }
}
