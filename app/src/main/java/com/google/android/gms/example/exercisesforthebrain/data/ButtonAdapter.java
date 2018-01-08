package com.google.android.gms.example.exercisesforthebrain.data;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.example.exercisesforthebrain.DayActivity;
import com.google.android.gms.example.exercisesforthebrain.R;

import java.util.List;

/**
 * Created by jman on 8/08/17.
 */

public class ButtonAdapter extends RecyclerView.Adapter<ButtonAdapter.ButtonViewHolder> {
    private final Context context;
    private List<SourceDayData> items;
    private List<Boolean> exercisesAnswered;


    public ButtonAdapter(List<SourceDayData> items, Context context, List<Boolean> exercisesAnswered) {
        this.context = context;
        this.items = items;
        this.exercisesAnswered = exercisesAnswered;

    }

    private boolean checkPreviusExercises(int adapterPosition) { //Only in the case that all previous Mathematical Excercises were done
        boolean statusPreviusExercise = false;                   //then return true
        for (int i = 0; i < adapterPosition - 1; i++) {
            if (exercisesAnswered.get(i)) {
                statusPreviusExercise = true;
            } else {
                statusPreviusExercise = false;
            }
        }
        return statusPreviusExercise;
    }

    private void startIntent(View v, int adapterPosition) {
        Intent intent = new Intent(context, DayActivity.class);
        String dayandnumber = "Day " + String.valueOf(adapterPosition);
        intent.putExtra("identity", dayandnumber);
        final TextView numberOfDayView = v.findViewById(R.id.textViewnumberDayId);

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                (Activity) context, numberOfDayView, "numberDay1");

        context.startActivity(intent, options.toBundle());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public ButtonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.activity_button, viewGroup, false);
        return new ButtonViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ButtonViewHolder viewHolder, int i) {

        viewHolder.day.setText(items.get(i).getDia());
        if (exercisesAnswered.get(i)) {
            viewHolder.gggg.setBackgroundResource(R.drawable.selection);
        } else {
            viewHolder.gggg.setBackgroundResource(R.drawable.circle_selection);
        }
    }

    public class ButtonViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView day;
        private ImageView gggg;


        private ButtonViewHolder(View v) {
            super(v);
            v.setClickable(true);
            v.setOnClickListener(this);
            day = v.findViewById(R.id.textViewnumberDayId);
            gggg = v.findViewById(R.id.imageButtonId);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition() + 1;
            if (adapterPosition == 1) {  //This validation only for the first position
                startIntent(v, adapterPosition); //Start intent and send day identified
            } else {
                if (checkPreviusExercises(adapterPosition)) { //Check for the rest possible exercises answered
                    startIntent(v, adapterPosition);          //Start intent and send day identified
                } else {
                    Toast.makeText(context, context.getString(R.string.mainActivityButtonAdapter), Toast.LENGTH_LONG).show();
                }
            }

        }

    }
}



