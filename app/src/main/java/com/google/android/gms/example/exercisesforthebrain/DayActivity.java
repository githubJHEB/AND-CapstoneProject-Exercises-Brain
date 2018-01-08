package com.google.android.gms.example.exercisesforthebrain;

import android.app.ActivityOptions;
import android.app.LoaderManager;

import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;

import android.database.Cursor;
import android.graphics.Color;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Transition;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.example.exercisesforthebrain.data.Contract;


import butterknife.BindView;
import butterknife.ButterKnife;


public class DayActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    static final String[] PROJECTION = new String[]{
            Contract.Entry.COLUMN_MATHEM,
            Contract.Entry.COLUMN_ALPHABET,
            Contract.Entry.COLUMN_COLOR,
            Contract.Entry.COLUMN_IMAGENS
    };
    private static final int CURSOR_LOADER_ID = 0;
    @BindView(R.id.fabTextColorId)
    FloatingActionButton fabTestColor;
    @BindView(R.id.fabAlphabetId)
    FloatingActionButton fabTestAphabet;
    @BindView(R.id.fabImagenId)
    FloatingActionButton fabTestImagen;
    @BindView(R.id.textViewSubtitleId)
    TextView subtitleApp;
    @BindView(R.id.textViewnumberDayId)
    TextView day;
    @BindView(R.id.textViewDayWarmId)
    TextView textViewdaywarm;
    @BindView(R.id.startExerciseButtonId)
    Button buttonMathematical;
    //private Button buttonMathematical;
    private String sharedText;
    private String dayToInteger;
    private String datamMath = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day);
        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbarTestStartAlphaId);

        setSupportActionBar(toolbar);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        Intent intent = getIntent();
        sharedText = intent.getStringExtra("identity");
        dayToInteger = sharedText.substring(4);
        String textMath = getString(R.string.exerciseMathematical) + " " + sharedText;
        buttonMathematical.setText(textMath);
        buttonMathematical.setContentDescription(getResources().getString(R.string.exerciseMathematical) + sharedText);

        day.setText(sharedText);
        Transition enterTrans = new Explode();
        enterTrans.setDuration(800);
        getWindow().setEnterTransition(enterTrans);

        Transition returnTrans = new Explode();
        returnTrans.setDuration(300);

        //Transition for return Activity TestColor
        Transition exitTrans = new Explode();
        exitTrans.setDuration(300);

        getWindow().setExitTransition(exitTrans);

        Transition reenterTrans = new Explode();
        reenterTrans.setDuration(800);
        getWindow().setReenterTransition(reenterTrans);
        getWindow().setReturnTransition(returnTrans);

        LoaderManager loadermanager = getLoaderManager();
        loadermanager.initLoader(CURSOR_LOADER_ID, null, this);

        //used in d-pad
        buttonMathematical.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    buttonMathematical.setBackgroundColor(getColor(R.color.colorfocusmathedayexe));
                    buttonMathematical.setTextColor(Color.WHITE);
                } else {
                    if (datamMath != null) {
                        buttonMathematical.setBackgroundColor(getColor(R.color.colorReadyAnswered));
                        buttonMathematical.setTextColor(Color.WHITE);
                    } else {
                        buttonMathematical.setBackgroundColor(getColor(R.color.colorfabbuttondayactivity));
                        buttonMathematical.setTextColor(Color.WHITE);
                    }
                }
            }
        });

        //used in d-pad
        subtitleApp.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    subtitleApp.setBackgroundColor(Color.TRANSPARENT);
                    subtitleApp.setTextColor(Color.WHITE);
                } else {
                    subtitleApp.setBackgroundColor(Color.TRANSPARENT);
                    subtitleApp.setTextColor(Color.WHITE);
                }

            }
        });

        //used in d-pad
        day.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    day.setBackgroundColor(getColor(R.color.focus_button));
                } else {
                    day.setBackgroundColor(Color.TRANSPARENT);
                }

            }
        });

        //used in d-pad
        textViewdaywarm.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    textViewdaywarm.setBackgroundColor(getColor(R.color.focus_button));
                } else {
                    textViewdaywarm.setBackgroundColor(Color.TRANSPARENT);
                }

            }
        });

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

    public void buttonTestColorActivity(@SuppressWarnings("UnusedParameters") View view) {

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(DayActivity.this);
        Intent intentTestActivity = new Intent(DayActivity.this, TestColorActivity.class);
        intentTestActivity.putExtra("dayTest", sharedText);
        DayActivity.this.startActivity(intentTestActivity, options.toBundle()); //Make smooth  transition
    }

    public void buttonTestAphabetActivity(@SuppressWarnings("UnusedParameters") View view) {

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(DayActivity.this);
        Intent intentTestActivity = new Intent(DayActivity.this, TestAlphabetActivity.class);
        intentTestActivity.putExtra("dayTest", sharedText);
        DayActivity.this.startActivity(intentTestActivity, options.toBundle()); //Make smooth  transition
    }

    public void buttonTestImagenActivity(@SuppressWarnings("UnusedParameters") View view) {

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(DayActivity.this);
        Intent intentTestActivity = new Intent(DayActivity.this, TestImagenActivity.class);
        intentTestActivity.putExtra("dayTest", sharedText);
        DayActivity.this.startActivity(intentTestActivity, options.toBundle()); //Make smooth  transition
    }

    public void buttonMathematicalActivity(@SuppressWarnings("UnusedParameters") View view) {

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(DayActivity.this);
        Intent intentTestActivity = new Intent(DayActivity.this, MathematicalActivity.class);
        intentTestActivity.putExtra("dayTest", sharedText);
        DayActivity.this.startActivity(intentTestActivity, options.toBundle()); //Make smooth  transition
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,
                Contract.Entry.URI,
                PROJECTION,
                null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        String dataColor = null;
        String dataAlpha = null;
        String datamImagen = null;

        data.moveToPosition(Integer.parseInt(dayToInteger) - 1);
        if (data.moveToPosition(Integer.parseInt(dayToInteger) - 1)) {
            dataAlpha = data.getString(data.getColumnIndex("alphabettest"));
        }
        if (data.moveToPosition(Integer.parseInt(dayToInteger) - 1)) {
            dataColor = data.getString(data.getColumnIndex("colortest"));
        }
        if (data.moveToPosition(Integer.parseInt(dayToInteger) - 1)) {
            datamImagen = data.getString(data.getColumnIndex("imagenstest"));
        }
        if (data.moveToPosition(Integer.parseInt(dayToInteger) - 1)) {
            datamMath = data.getString(data.getColumnIndex("mathem"));
        }

        if (dataAlpha != null) {
            fabTestAphabet.setBackgroundTintList(getResources().getColorStateList(R.color.colorReadyAnswered, null));
        } else {
            fabTestAphabet.setBackgroundTintList(getResources().getColorStateList(R.color.colorfabbuttondayactivity, null));
        }
        if (dataColor != null) {
            fabTestColor.setBackgroundTintList(getResources().getColorStateList(R.color.colorReadyAnswered, null));
        } else {
            fabTestColor.setBackgroundTintList(getResources().getColorStateList(R.color.colorfabbuttondayactivity, null));
        }
        if (datamImagen != null) {
            fabTestImagen.setBackgroundTintList(getResources().getColorStateList(R.color.colorReadyAnswered, null));
        } else {
            fabTestImagen.setBackgroundTintList(getResources().getColorStateList(R.color.colorfabbuttondayactivity, null));
        }
        if (datamMath != null) {
            buttonMathematical.setBackgroundColor(getColor(R.color.colorReadyAnswered));
            buttonMathematical.setTextColor(getColor((R.color.colorBlack)));
        } else {
            buttonMathematical.setBackgroundColor(getColor(R.color.colorfabbuttondayactivity));
            buttonMathematical.setTextColor(Color.WHITE);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        getLoaderManager().restartLoader(CURSOR_LOADER_ID, null, this);
    }

}
