package com.google.android.gms.example.exercisesforthebrain;

import android.app.ActivityOptions;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.transition.Explode;
import android.transition.Transition;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.example.exercisesforthebrain.data.ButtonAdapter;
import com.google.android.gms.example.exercisesforthebrain.data.Contract;
import com.google.android.gms.example.exercisesforthebrain.data.SourceDayData;
import com.google.android.gms.example.exercisesforthebrain.utilities.WidgetService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
//Licensed under the Apache License, Version 2.0 (the "License");
//        you may not use this file except in compliance with the License.
//        You may obtain a copy of the License at
//
//        http://www.apache.org/licenses/LICENSE-2.0
//
//        Unless required by applicable law or agreed to in writing, software
//        distributed under the License is distributed on an "AS IS" BASIS,
//        WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//        See the License for the specific language governing permissions and
//        limitations under the License.
public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    static final String[] PROJECTION = new String[]{
            Contract.Entry.COLUMN_MATHEM
    };
    private static final String DEBUG_TAG = "TRAK";
    private static final int CURSOR_LOADER_ID = 0;
    private static String[] textMathArray =
            {"2 x 3", "4 + 3", "8 + 2",
                    "0 + 2", "2 + 9", "7 + 7",
                    "4 + 8", "6 + 9", "9 + 0"};
    private static String[] textMathArrayH =
            {"2 x 3 + 4", "4 + 3 - 1",
                    "9 + 9 / 1", "1 + 7 - 0",
                    "2 + 9 x 4", "7 + 7 + 6",
                    "4 + 8 x 2"};
    @BindView(R.id.buttonSingButtonId)
    Button singButton;
    @BindView(R.id.buttonReadMoreId)
    Button buttonReadMore;
    @BindView(R.id.textViewEmailId)
    TextView textoLoginConfirmation;
    @BindView(R.id.textViewErasedataId)
    TextView erasedata;
    @BindView(R.id.textViewDescriptionId)
    TextView descriptionApp;
    @BindView(R.id.textViewsubTitleId)
    TextView subtitleApp;
    @BindView(R.id.textViewTotalDaysId)
    TextView textView;


    private RecyclerView.Adapter adapter;
    private List<Boolean> exerciseAnswered = new ArrayList<>();
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    public static String[] getMyMathArray() {
        return textMathArray;
    }

    public static String[] getMyMathArrayH() {
        return textMathArrayH;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Transition exitTrans = new Explode(); //Entry and exit transition
        exitTrans.setDuration(300);
        getWindow().setExitTransition(exitTrans);
        Transition reenterTrans = new Explode();
        reenterTrans.setDuration(800);
        getWindow().setReenterTransition(reenterTrans);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        descriptionApp.setMovementMethod(new ScrollingMovementMethod());


        if (user != null && user.isEmailVerified()) {
            textoLoginConfirmation.setText(user.getEmail());
            textoLoginConfirmation.setContentDescription(user.getEmail());
            singButton.setText(getResources().getString(R.string.singOut));
        } else {
            textoLoginConfirmation.setText(" ");
            singButton.setText(getResources().getString(R.string.singIn)); //Update status od sing in or sing out
        }


        LoaderManager loadermanager = getLoaderManager();
        loadermanager.initLoader(CURSOR_LOADER_ID, null, this);

        List items = new ArrayList();
        for (int i = 1; i < 31; i++) {
            items.add(new SourceDayData(getResources().getString(R.string.day) + " " + i));
        }

        for (int i = 0; i < 30; i++) {
            exerciseAnswered.add(i, false);
        }

        Toolbar toolbar = findViewById(R.id.toolbarTestStartAlphaId);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRefi = database.getReference(getResources().getString(R.string.medium_dificult_level));
        DatabaseReference myRefiq = database.getReference(getResources().getString(R.string.high_dificult_level));
        // Read from the database
        myRefi.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Only if there is a change in the database in Firebase
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);

                if (value != null) {
                    textMathArray = value.split(":"); //Basic level of Mathematical dificult
                } else {
                    Log.d(DEBUG_TAG, getString(R.string.noDataAvailableOnTheCloud) + " " + "");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

        myRefiq.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Only if there is a change in the database in Firebase
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                if (value != null) {
                    textMathArrayH = value.split(":"); //Medium level of Mathematical dificult
                }
                Log.d(DEBUG_TAG, getString(R.string.noDataAvailableOnTheCloud) + " " + "");
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });

        RecyclerView recycler = findViewById(R.id.recicladorDaySelectorId);
        recycler.setHasFixedSize(true);
        RecyclerView.LayoutManager lManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recycler.setLayoutManager(lManager);
        adapter = new ButtonAdapter(items, this, exerciseAnswered);
        recycler.setAdapter(adapter);
    }

    @OnClick(R.id.buttonSingButtonId)
    void singUser() {
        Intent intentSing = new Intent(MainActivity.this, SingActivity.class);
        if (singButton.getText().equals(getResources().getString(R.string.singOut))) {
            if (user != null && user.isEmailVerified()) {
                mAuth.signOut();
                singButton.setText(getResources().getString(R.string.singIn));
                textoLoginConfirmation.setText("");
            }
        } else {
            startActivity(intentSing);
        }
    }

    //Used en d-pad
    @OnFocusChange(R.id.buttonSingButtonId)
    void singUserOnFocus(boolean hasFocus) {
        if (hasFocus) {
            singButton.setBackgroundColor(getColor(R.color.focus_button));
            singButton.setTextColor(Color.BLACK);
        } else {
            singButton.setBackgroundColor(getColor(R.color.sing_color));
            singButton.setTextColor(Color.WHITE);
        }
    }

    @OnClick(R.id.buttonReadMoreId)
    void readMoreDetail() {
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this);
        Intent intent = new Intent(MainActivity.this, DescriptionGameActivity.class);
        MainActivity.this.startActivity(intent, options.toBundle());
    }

    //Used en d-pad
    @OnFocusChange(R.id.buttonReadMoreId)
    void readMoreDetailFocus(boolean hasFocus) {
        if (hasFocus) {
            buttonReadMore.setBackgroundColor(getColor(R.color.focus_button));
            buttonReadMore.setTextColor(Color.BLACK);
        } else {
            buttonReadMore.setBackgroundColor(getColor(R.color.colorButtonReadMore));
            buttonReadMore.setTextColor(Color.WHITE);
        }
    }

    @OnClick(R.id.textViewErasedataId)
    void eraseHistoricalata() {
        MainActivity.this.getContentResolver()
                .delete(
                        Contract.Entry.URI,
                        getResources().getString(R.string.code_erase), //Delete local database only if this values is provided.
                        null);
        for (int i = 0; i < 30; i++) {
            exerciseAnswered.add(i, false);
        }
        WidgetService.startActionUpdateWidget(this);
        adapter.notifyDataSetChanged();
    }

    // Used en d-pad
    @OnFocusChange(R.id.textViewErasedataId)
    void eraseHistoricalataFocus(boolean hasFocus) {
        if (hasFocus) {
            erasedata.setBackgroundColor(getColor(R.color.focus_button));
        } else {
            erasedata.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    // Used en d-pad
    @OnFocusChange(R.id.textViewTotalDaysId)
    void numberOfaDay(boolean hasFocus) {
        if (hasFocus) {
            textView.setBackgroundColor(getColor(R.color.focus_button));
        } else {
            textView.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    //Used en d-pad
    @OnFocusChange(R.id.textViewsubTitleId)
    void subTitle(boolean hasFocus) {
        if (hasFocus) {
            subtitleApp.setBackgroundColor(Color.TRANSPARENT);
            subtitleApp.setTextColor(Color.WHITE);
        } else {
            subtitleApp.setBackgroundColor(Color.TRANSPARENT);
            subtitleApp.setTextColor(Color.WHITE);
        }
    }

    //Used en d-pad
    @OnFocusChange(R.id.textViewDescriptionId)
    void descriptionApp(boolean hasFocus) {
        if (hasFocus) {
            descriptionApp.setBackgroundColor(getColor(R.color.focus_button));
        } else {
            descriptionApp.setBackgroundColor(Color.TRANSPARENT);
        }
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
        for (int i = 0; i < 30; i++) {
            if (data.moveToPosition(i)) {
                String datamMath = data.getString(data.getColumnIndex("mathem"));
                if (datamMath != null) {
                    exerciseAnswered.set(i, true); //Only if methematical excercises is completed
                }
            }
        }
        adapter.notifyDataSetChanged(); //Keep update the information about the day that
        //the user make the mathematical practice
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        getLoaderManager().restartLoader(CURSOR_LOADER_ID, null, this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent settingactivity = new Intent(this, SettingsActivity.class);
                startActivity(settingactivity); //Setting for change of level of dicult of mathematical excercises
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        this.invalidateOptionsMenu();
        return true;
    }
}
