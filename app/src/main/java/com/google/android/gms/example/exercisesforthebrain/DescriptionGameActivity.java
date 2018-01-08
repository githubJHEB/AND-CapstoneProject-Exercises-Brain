package com.google.android.gms.example.exercisesforthebrain;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.transition.Explode;
import android.transition.Transition;
import android.view.MenuItem;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DescriptionGameActivity extends AppCompatActivity {
    @BindView(R.id.descriptionAppId)
    TextView movement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description_game);
        ButterKnife.bind(this);
        movement.setMovementMethod(new ScrollingMovementMethod());

        Toolbar toolbar = findViewById(R.id.toolbarescriptionId);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Transition enterTrans = new Explode();
        getWindow().setEnterTransition(enterTrans);

        Transition returnTrans = new Explode();
        getWindow().setReturnTransition(returnTrans);

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
