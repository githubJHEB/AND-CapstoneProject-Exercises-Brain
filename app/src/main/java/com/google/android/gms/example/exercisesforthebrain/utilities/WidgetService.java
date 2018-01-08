package com.google.android.gms.example.exercisesforthebrain.utilities;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import com.google.android.gms.example.exercisesforthebrain.R;
import com.google.android.gms.example.exercisesforthebrain.data.Contract;
import com.google.android.gms.example.exercisesforthebrain.data.WidgetProvider;

/**
 * Created by jman on 20/11/17.
 */

public class WidgetService extends IntentService {

    public static final String ACTION_UPDATE_WIDGET = "com.google.android.gms.example.exercisesforthebrain.action.update_widget";

    public WidgetService() {
        super("WidgetService");
    }

    public static void startActionUpdateWidget(Context context) {
        Intent intent = new Intent(context, WidgetService.class);
        intent.setAction(ACTION_UPDATE_WIDGET);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPDATE_WIDGET.equals(action)) {
                handleActionUpdateWidget();
            }
        }
    }

    private void handleActionUpdateWidget() {
        String resultTimeColorTestToShow = getString(R.string.initialValueWidget);
        String resultCorrectColorTestToShow = getString(R.string.initialValueWidget);
        String resultTimeMatheTestToShow = getString(R.string.initialValueWidget);
        String resultCorrectMatheTestToShow = getString(R.string.initialValueWidget);
        String resultCorrectImageTestToShow = getString(R.string.initialValueWidget);
        String resultTimeAlphaTestToShow = getString(R.string.initialValueWidget);
        String day = getString(R.string.day1Widget);
        Cursor cursor = getContentResolver().query(
                Contract.Entry.URI,
                null,
                null,
                null,
                null
        );

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToLast();
            int idIndex = cursor.getColumnIndex(Contract.Entry.COLUMN_ID);
            int colorTestIndex = cursor.getColumnIndex(Contract.Entry.COLUMN_COLOR);
            int alphabetTestIndex = cursor.getColumnIndex(Contract.Entry.COLUMN_ALPHABET);
            int imageTestIndex = cursor.getColumnIndex(Contract.Entry.COLUMN_IMAGENS);
            int mathematicalTestIndex = cursor.getColumnIndex(Contract.Entry.COLUMN_MATHEM);
            String resultColorTest = cursor.getString(colorTestIndex);
            String resultAlphaTest = cursor.getString(alphabetTestIndex);
            String resultImageTest = cursor.getString(imageTestIndex);
            String resultMatheTest = cursor.getString(mathematicalTestIndex);
            day = cursor.getString((idIndex));

            if (resultColorTest != null) {
                resultTimeColorTestToShow = resultColorTest.substring(resultColorTest.indexOf(",") + 1);
                resultCorrectColorTestToShow = resultColorTest.substring(0, resultColorTest.indexOf(","));
            }
            if (resultMatheTest != null) {
                resultTimeMatheTestToShow = resultMatheTest.substring(resultMatheTest.indexOf(",") + 1);
                resultCorrectMatheTestToShow = resultMatheTest.substring(0, resultMatheTest.indexOf(","));
            }
            if (resultAlphaTest != null) {
                resultTimeAlphaTestToShow = resultAlphaTest;
            }
            if (resultImageTest != null) {
                resultCorrectImageTestToShow = resultImageTest;
            }
            cursor.close();

        }
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetId = appWidgetManager.getAppWidgetIds(new ComponentName(this, WidgetProvider.class));
        WidgetProvider.updateWidgets(this, appWidgetManager, appWidgetId, resultTimeColorTestToShow, resultCorrectColorTestToShow,
                resultTimeMatheTestToShow, resultCorrectMatheTestToShow, resultTimeAlphaTestToShow, resultCorrectImageTestToShow, day);
    }
}
