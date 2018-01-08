package com.google.android.gms.example.exercisesforthebrain;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.Handler;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Transition;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.example.exercisesforthebrain.data.Contract;
import com.google.android.gms.example.exercisesforthebrain.data.ImageAdapter;
import com.google.android.gms.example.exercisesforthebrain.utilities.GeneralUtilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;


public class TestImagenActivity extends AppCompatActivity {
    private static final String DEBUG_TAG = "TRAK";
    private static final String STATE_TIMER = "0";
    private static final String STATE_BOOLEANSTART = "01";
    private static final String STATE_BOOLEANFINISH = "02";
    private static final String STATE_CORRECTANSWER = "03";
    private static final String STATE_CORRECTANSWERCLICK = "04";
    private static final String STATE_ITEMSANSWERED = "05";
    private static final String STATE_ANSWEREASERTED = "06";
    private boolean startButtonboolean = false;
    private boolean clickPerformance = false;

    private Long baseTime = 60000L;
    private Long baseTimeOnStop = 60000L;

    private int quatityImagesToSelect = 0;
    private CountDownTimer countDownTimer;
    private boolean finishCountown = false;
    String sharedText;
    String substrSharedText;
    private boolean startButton = true;
    private boolean gridViewImagens = false;

    // references to our images
    private int im1, im2, im3, im4, im5, im6, im7, im8, im9, im10, im11, im12, im13, im14, im15, im16, im17, im18, im19, im20, im21, im22, im23, im24, im25, im26, im27, im28, im29, im30;
    private Integer[] mThumbIdsExercise = {im1, im2, im3, im4, im5, im6, im7, im8, im9, im10, im11, im12, im13, im14, im15, im16, im17, im18, im19, im20, im21, im22, im23, im24, im25, im26, im27, im28, im29, im30};
    private Integer[] correctAnswer = {2, 3, 5, 6, 9, 10, 15, 18, 26, 29};
    private Integer[] correctAnswerClick = {55, 55, 55, 55, 55, 55, 55, 55, 55, 55};
    private ArrayList<Integer> correctArray;// Array to saveinstance of correct answer
    private ArrayList<Integer> correctClickArray;// Array to saveinstance of correct answer
    private  Integer[] inicialImageReferenceArray =
            {0, 1, 2, 3, 5, 6, 7, 8, 9,
                    10, 11, 12, 13, 14, 15, 16, 17, 18, 19,
                    20, 21, 22, 23, 24, 25, 26, 27, 28, 29};
    private int asertedAswer;
    private ImageAdapter adapter;
    private int z = 0;
    private ActionBar ab;
    private Context context;
    @BindView(R.id.textViewSubtitleId)
    TextView subtitleApp;
    @BindView(R.id.textViewDescriptionExerId)
    TextView descriptionExercise;
    @BindView(R.id.buttonStartId)
    Button startChro;
    @BindView(R.id.buttonPerformanceImageId)
    Button perform;

    private void generatorValues() {
        List<Integer> ListinicialImageReference;
        ListinicialImageReference = Arrays.asList(inicialImageReferenceArray);
        Collections.shuffle(ListinicialImageReference);
        for (int answer = 0; answer < 10; answer++) {
            correctAnswer[answer] = ListinicialImageReference.get(answer);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_imagen);
        Explode mExplode;
        correctArray = new ArrayList<>(10);
        correctClickArray = new ArrayList<>(10);

        generatorValues();
        ButterKnife.bind(this);
        context = getApplicationContext();
        //Necesary to know the day of activity and for store stadistical information
        Intent intent = getIntent();
        sharedText = intent.getStringExtra("dayTest");
        substrSharedText = sharedText.substring(4);

        asertedAswer = 0;
        Toolbar toolbar =  findViewById(R.id.toolbarTestStartAlphaId);


        mExplode = new Explode();
        mExplode.setDuration(300);
        mThumbIdsExercise = GeneralUtilities.inicialImageReference();

        Transition enterTrans = new Explode();
        enterTrans.setDuration(300);
        getWindow().setEnterTransition(enterTrans);

        Transition returnTrans = new Explode();
        returnTrans.setDuration(300);
        startChro =  findViewById(R.id.buttonStartId);

        setSupportActionBar(toolbar);
        // Get a support ActionBar corresponding to this toolbar
        ab = getSupportActionBar();

        // Enable the Up button
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        adapter = new ImageAdapter(this, mThumbIdsExercise);
        final GridView gridview =  findViewById(R.id.gridview);
        gridview.setAdapter(adapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                if (z < 10 && gridViewImagens) {
                    descriptionExercise.setText(getString(R.string.alphaSelectedImages) + " " + (z + 1));
                    quatityImagesToSelect = (z+1);
                    for (int ii = 0; ii < 10; ii++) {  //Store position  of correct click, set OK drawable
                        // en each position and store information for save instance
                        if (correctAnswer[ii] == position  && correctAnswerClick[ii] != position) {
                            correctAnswerClick[ii] = position;
                            Log.d(DEBUG_TAG,"ClickGridView " + correctAnswerClick[ii].toString());
                            if (correctAnswerClick[ii]==0){
                                correctClickArray.add(66);
                            } else {
                                correctClickArray.add(correctAnswerClick[ii]);
                            }
                                asertedAswer++;
                                mThumbIdsExercise[position] = R.drawable.ok;
                        }
                    }
                    adapter.notifyDataSetChanged();
                    z++;
                }
                if (z == 10) {
                    gridViewImagens = false;
                    Toast.makeText(TestImagenActivity.this, getString(R.string.alphaCorrectAnswer) +" " + asertedAswer,
                            Toast.LENGTH_SHORT).show();
                    delayResultextColor();
                    z = 11;
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (countDownTimer != null) {
            if (!finishCountown) { //Only in the time of image observation
                baseTime = baseTimeOnStop;
                countDownTimer.cancel();
                counterDownAndAction();
            }
        }
    }

    @Override
    public void onStop() { //Only in the time of image observation
        super.onStop();
        if (!clickPerformance) {
            if (!finishCountown) {
                baseTimeOnStop = baseTime;
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        if (!clickPerformance) {
            // Save UI state changes to the savedInstanceState.

            savedInstanceState.putLong(STATE_TIMER, baseTime);
            savedInstanceState.putBoolean(STATE_BOOLEANSTART, startButtonboolean);
            savedInstanceState.putBoolean(STATE_BOOLEANFINISH, finishCountown);
            savedInstanceState.putIntegerArrayList(STATE_CORRECTANSWER, correctArray);
            savedInstanceState.putIntegerArrayList(STATE_CORRECTANSWERCLICK, correctClickArray);
            savedInstanceState.putInt(STATE_ITEMSANSWERED, quatityImagesToSelect);
            savedInstanceState.putInt(STATE_ANSWEREASERTED, asertedAswer);

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
        finishCountown = savedInstanceState.getBoolean(STATE_BOOLEANFINISH);
        correctArray = savedInstanceState.getIntegerArrayList(STATE_CORRECTANSWER);
        correctClickArray = savedInstanceState.getIntegerArrayList(STATE_CORRECTANSWERCLICK);
        quatityImagesToSelect = savedInstanceState.getInt(STATE_ITEMSANSWERED);
        z = quatityImagesToSelect;
        asertedAswer = savedInstanceState.getInt(STATE_ANSWEREASERTED);
        baseTimeOnStop = baseTime;

        for (int i = 0; i<correctArray.size(); i++){
            Log.d(DEBUG_TAG,"restoreTestrespuestascorrectasSIZE " + correctArray.size());
            correctAnswer[i] = correctArray.get(i);
            Log.d(DEBUG_TAG,"restoreTestrespuestascorrectas " + correctAnswer[i].toString());

        }

        for (int i = 0; i<correctClickArray.size(); i++){
            if (0!=correctClickArray.get(i)){
                if (correctClickArray.get(i) == 66){
                    correctAnswerClick[i] = 0;
                } else {
                    correctAnswerClick[i] = correctClickArray.get(i);
                }
                Log.d(DEBUG_TAG,"Testclickrealizados " + correctAnswerClick[i].toString());
            }
        }


        if (startButtonboolean) {
            startprocedure();
        } else {
            baseTime = 60000L;
        }
    }
    @OnFocusChange({
            R.id.buttonStartId,
            R.id.buttonPerformanceImageId
    })
    void onfocusbutton(boolean hasFocus, Button button){
        switch (button.getId()) {
            case R.id.buttonStartId:
                if (hasFocus){
                    startChro.setBackgroundColor(getColor(R.color.colorfocusmathedayexe));
                    startChro.setTextColor(Color.WHITE);
                }else {
                    startChro.setBackgroundColor(getColor(R.color.colorfabbuttondayactivity));
                    startChro.setTextColor(Color.WHITE);

                }
                break;
            case R.id.buttonPerformanceImageId:
                if (hasFocus){
                    perform.setBackgroundColor(getColor(R.color.colorfocusmathedayexe));
                    perform.setTextColor(Color.WHITE);
                }else {
                    perform.setBackgroundColor(getColor(R.color.colorfabbuttondayactivity));
                    perform.setTextColor(Color.WHITE);

                }
                break;
        }
    }

    @OnFocusChange({
            R.id.textViewSubtitleId,
            R.id.textViewDescriptionExerId,
    })
    void onfocustext(boolean hasFocus, TextView text){
        switch (text.getId()){
            case R.id.textViewSubtitleId:
                if (hasFocus){
                    subtitleApp.setBackgroundColor(Color.TRANSPARENT);
                    subtitleApp.setTextColor(Color.WHITE);
                }else {
                    subtitleApp.setBackgroundColor(Color.TRANSPARENT);
                    subtitleApp.setTextColor(Color.WHITE);
                }
                break;
            case R.id.textViewDescriptionExerId:
                if (hasFocus){
                    descriptionExercise.setBackgroundColor(getColor(R.color.focus_button));
                }else {
                    descriptionExercise.setBackgroundColor(Color.TRANSPARENT);
                }
                break;
        }
    }

    private void counterDownAndAction() {
        countDownTimer = new CountDownTimer(baseTime, 1000) {

            public void onTick(long millisUntilFinished) {
                //countDown.setText(millisUntilFinished / 1000 + getString(R.string.alphaSeconds));
                descriptionExercise.setText(millisUntilFinished / 1000 + " " + getString(R.string.alphaSeconds));
                baseTime = millisUntilFinished;
            }

            public void onFinish() {
                // Start recording changes to the view hierarchy
                //descriptionExercise.setText(getString(R.string.alphaImageToRemember));
                finishCountown = true;
                descriptionExercise.setText(getString(R.string.alphaImageToRememberTen));
                gridViewImagens = true;
                mThumbIdsExercise = GeneralUtilities.toSelectImageReference(correctAnswer);

                correctArray.clear();

                for (int i = 0; i<10; i++){
                    correctArray.add(0);
                }
                for (int i = 0; i<correctAnswer.length; i++){
                    correctArray.set(i, correctAnswer[i]);
                    Log.d(DEBUG_TAG, "correctArraySaveinstence" + correctArray.get(i).toString());
                }
                adapter.notifyDataSetChanged();
            }
        }.start();
    }

    private void startprocedure() {

        if (startButtonboolean) {
            startChro.setVisibility(View.GONE);
            perform.setVisibility(View.GONE);
        }
        ab.setDisplayHomeAsUpEnabled(false);
        if (startButton) {


            if (!finishCountown) {
                mThumbIdsExercise = GeneralUtilities.rememberImageReference();
                adapter.notifyDataSetChanged();
                counterDownAndAction();
            } else {
                //finishCountown = true;
                descriptionExercise.setText(getString(R.string.alphaSelectedImages) + " " + quatityImagesToSelect);
                gridViewImagens = true;
                for (int aa = 0; aa< 10; aa++){
                    Log.d(DEBUG_TAG,"Test " + correctAnswer[aa].toString());
                }
                mThumbIdsExercise = GeneralUtilities.toSelectImageReference(correctAnswer);

                int aaa = 0;
                for (Integer valueitem : correctAnswerClick){
                    if (valueitem!=55) {
                        mThumbIdsExercise[correctAnswerClick[aaa]] = R.drawable.ok; //mThumbIdsExercise
                        Log.d(DEBUG_TAG, "Testclickrealizadosooooo " + correctAnswerClick[aaa].toString());
                    aaa++;
                    }
                }
                adapter.notifyDataSetChanged();
            }
        }
    }
    @OnClick({
            R.id.buttonStartId,
            R.id.buttonPerformanceImageId
    })
    void buttons(Button button) {

        switch (button.getId()) {
            case R.id.buttonStartId:
                startButtonboolean = true;
                startprocedure();
                break;
            case R.id.buttonPerformanceImageId:
                clickPerformance = true;
                Intent mathPerformance = new Intent(TestImagenActivity.this, PerformanceAcivity.class);
                mathPerformance.putExtra("data", "image");
                startActivity(mathPerformance);
                break;
        }
    }


    //Delay to show result, and generate new value for the next question.
    protected void delayResultextColor() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ContentValues quoteCVA = new ContentValues();
                quoteCVA.put(Contract.Entry.COLUMN_IMAGENS, String.valueOf(asertedAswer));
                GeneralUtilities.requestToLocalDataBase(context, substrSharedText, quoteCVA);
                Intent intent = new Intent(TestImagenActivity.this, DayActivity.class);
                intent.putExtra("identity", sharedText);
                //Close "Day activity" and the current Activity.
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }, 2000);
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
}
