package com.google.android.gms.example.exercisesforthebrain;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.SystemClock;
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

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;


public class TestAlphabetActivity extends AppCompatActivity {
    private static final String STATE_TIMER = "0";
    private static final String STATE_BOOLEANSTART = "01";
    @BindView(R.id.buttonStartId)
    Button startChro;
    @BindView(R.id.buttonPerformanceAlphaId)
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
    private Chronometer crono;
    private boolean startButton = true;
    private boolean startButtonboolean = false;
    private boolean clickPerformance = false;
    private String sharedText;
    private String substrSharedText;
    private Context context;
    private Explode mExplode;
    private ViewGroup mRootView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_alphabet);

        ButterKnife.bind(this);
        ActionBar ab;
        context = getApplicationContext();
        //Required to know the day of activity and for store statistic information
        Intent intent = getIntent();
        sharedText = intent.getStringExtra("dayTest");
        substrSharedText = sharedText.substring(4);
        // Get the root view and create a transition
        mRootView = findViewById(R.id.activityAlphaLayout);
        mExplode = new Explode();
        mExplode.setDuration(300);


        Transition enterTrans = new Explode();
        enterTrans.setDuration(300);
        getWindow().setEnterTransition(enterTrans);

        Transition returnTrans = new Explode();
        returnTrans.setDuration(300);

        Transition exitTrans = new Explode();
        exitTrans.setDuration(300);

        Transition reenterTrans = new Explode();
        reenterTrans.setDuration(800);

        crono = findViewById(R.id.chronometerId);

        Toolbar toolbar = findViewById(R.id.toolbarTestStartAlphaId);
        setSupportActionBar(toolbar);

        // Get a support ActionBar corresponding to this toolbar
        ab = getSupportActionBar();

        // Enable the Up button
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

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

            savedInstanceState.putLong(STATE_TIMER, SystemClock.elapsedRealtime() - crono.getBase());
            savedInstanceState.putBoolean(STATE_BOOLEANSTART, startButtonboolean);
            super.onSaveInstanceState(savedInstanceState);
        }

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);
        // Restore state members from saved instance
        baseTime = savedInstanceState.getLong(STATE_TIMER);
        startButtonboolean = savedInstanceState.getBoolean(STATE_BOOLEANSTART);

        restore = true;
        if (startButtonboolean) {
            startprocedure();
        } else {
            baseTime = 0L;
            crono.stop();
        }

    }

    //Used for d-pad
    @OnFocusChange({
            R.id.buttonStartId,
            R.id.buttonPerformanceAlphaId
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
            case R.id.buttonPerformanceAlphaId:
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
        if (startButton) {
            crono.setBase(SystemClock.elapsedRealtime());
            crono.start();
            if (restore) {
                if (!startButtonboolean) {
                    crono.setBase(SystemClock.elapsedRealtime());
                    crono.start();
                } else {
                    referencetime = SystemClock.elapsedRealtime() - baseTime;
                    crono.setBase(referencetime);
                }

            }
            startButton = false;
            startChro.setText(getString(R.string.alphaStop));
            // Start recording changes to the view hierarchy
            TransitionManager.beginDelayedTransition(mRootView, mExplode);

            // Add the new TextView to the view hierarchy
            mRootView.removeView(perform);
        } else {
            crono.stop();
            startChro.setEnabled(false);
            String timeElapsed = showElapsedTime();
            delayResultextColor(timeElapsed);
        }
    }

    @OnClick({
            R.id.buttonStartId,
            R.id.buttonPerformanceAlphaId
    })
    void buttons(Button button) {
        switch (button.getId()) {
            case R.id.buttonStartId:
                startButtonboolean = true;
                startprocedure();
                break;
            case R.id.buttonPerformanceAlphaId:
                clickPerformance = true;
                Intent mathPerformance = new Intent(TestAlphabetActivity.this, PerformanceAcivity.class);
                mathPerformance.putExtra("data", "alpha");
                startActivity(mathPerformance);
                break;
        }
    }

    protected void delayResultextColor(final String time) { //Delay to show result, and generate new value for the next question.
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ContentValues quoteCVA = new ContentValues();
                quoteCVA.put(Contract.Entry.COLUMN_ALPHABET, time);
                GeneralUtilities.requestToLocalDataBase(context, substrSharedText, quoteCVA);
                Intent intent = new Intent(TestAlphabetActivity.this, DayActivity.class);
                intent.putExtra("identity", sharedText);
                //Close "Day activity" and the current Activity.
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }, 1000);
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

    private String showElapsedTime() {
        String twoDigitNum;
        DecimalFormat precision = new DecimalFormat("0.0");
        long elapsedMillis = (SystemClock.elapsedRealtime() - crono.getBase()) / 1000;
        twoDigitNum = precision.format(elapsedMillis);
        return twoDigitNum;
    }
}
