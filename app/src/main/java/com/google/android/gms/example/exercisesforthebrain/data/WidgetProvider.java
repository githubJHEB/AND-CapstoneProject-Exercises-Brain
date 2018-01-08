package com.google.android.gms.example.exercisesforthebrain.data;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.google.android.gms.example.exercisesforthebrain.MainActivity;
import com.google.android.gms.example.exercisesforthebrain.R;
import com.google.android.gms.example.exercisesforthebrain.utilities.WidgetService;

import static android.appwidget.AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH;

/**
 * Implementation of App Widget functionality.
 */
public class WidgetProvider extends AppWidgetProvider {

    private static String colorTimeCopy;
    private static String colorCorrectCopy;
    private static String mathTimeCopy;
    private static String mathCorrectCopy;
    private static String alphaTimeCopy;
    private static String imageCorrectCopy;
    private static String lastDayActivityCopy;

    public static void updateWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds, String colorTime, String colorCorrect, String mathTime, String mathCorrect,
                                     String alphaTime, String imageCorrect, String lastDayActivity) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateWidget(context, appWidgetManager, appWidgetId, colorTime, colorCorrect, mathTime, mathCorrect, alphaTime, imageCorrect, lastDayActivity);
            colorTimeCopy = colorTime;
            colorCorrectCopy = colorCorrect;
            mathTimeCopy = mathTime;
            mathCorrectCopy = mathCorrect;
            alphaTimeCopy = alphaTime;
            imageCorrectCopy = imageCorrect;
            lastDayActivityCopy = lastDayActivity;
        }
    }

    static void updateWidget(Context context, AppWidgetManager appWidgetManager,
                             int appWidgetId, String colorTime, String colorCorrect, String mathTime,
                             String mathCorrect, String alphaTime, String imageCorrect, String lastDayActivity) {
        int layoutId;
        int textViewDayId = R.id.textViewWidgetSmallId;
        String day;
        Bundle options = appWidgetManager.getAppWidgetOptions(appWidgetId);

        if (options.containsKey(OPTION_APPWIDGET_MIN_WIDTH)) {
            float minWidthDp = options.getInt(OPTION_APPWIDGET_MIN_WIDTH);

            float mediumWidth = 200;

            if (minWidthDp >= mediumWidth) {
                layoutId = R.layout.mediumwidget;
                day = lastDayActivity;
            } else {
                layoutId = R.layout.smallwidget;
                day = lastDayActivity;
            }
            RemoteViews views = new RemoteViews(context.getPackageName(), layoutId);

            if (minWidthDp >= mediumWidth) {
                int textViewColorId = R.id.textViewWidgetColorId;
                int textViewMathId = R.id.textViewWidgetMathId;
                int textViewAlphaId = R.id.textViewWidgetAlphaId;
                int textViewImageId = R.id.textViewWidgetImageId;

                views.setTextViewText(textViewColorId, context.getString(R.string.colorWidget) + " " + colorTime);
                views.setTextViewText(textViewMathId, context.getString(R.string.mathWidget) + " " + mathTime);
                views.setTextViewText(textViewAlphaId, context.getString(R.string.alphaWidget) + " " + alphaTime);
                views.setTextViewText(textViewImageId, context.getString(R.string.imageWidget) + " " + imageCorrect);
                views.setTextViewText(textViewDayId, context.getString(R.string.dayWidget) + " " + day);
            } else {
                views.setTextViewText(textViewDayId, context.getString(R.string.dayWidgetSmall) + " " + day);
            }

            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            views.setOnClickPendingIntent(R.id.image_widget_Id, pendingIntent);
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        WidgetService.startActionUpdateWidget(context);
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager,
                                          int appWidgetId, Bundle newOptions) {
        String colorTime = colorTimeCopy;
        String colorCorrect = colorCorrectCopy;
        String mathTime = mathTimeCopy;
        String mathCorrect = mathCorrectCopy;
        String alphaTime = alphaTimeCopy;
        String imageCorrect = imageCorrectCopy;
        String lastDayActivity = lastDayActivityCopy;
        updateWidget(context, appWidgetManager, appWidgetId, colorTime, colorCorrect,
                mathTime, mathCorrect, alphaTime, imageCorrect, lastDayActivity);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

