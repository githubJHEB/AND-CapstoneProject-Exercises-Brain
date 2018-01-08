package com.google.android.gms.example.exercisesforthebrain.utilities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.google.android.gms.example.exercisesforthebrain.R;
import com.google.android.gms.example.exercisesforthebrain.data.Contract;


/**
 * Created by jman on 2/09/17.
 */

public class GeneralUtilities {
    private static int im1, im2, im3, im4, im5, im6, im7, im8, im9, im10, im11, im12, im13, im14, im15, im16, im17, im18, im19, im20, im21, im22, im23, im24, im25, im26, im27, im28, im29, im30;
    private static Integer[] mThumbIdsExercise = {im1, im2, im3, im4, im5, im6, im7, im8, im9, im10, im11, im12, im13, im14, im15, im16, im17, im18, im19, im20, im21, im22, im23, im24, im25, im26, im27, im28, im29, im30};
    private static Integer[] mThumbBaseImageExercise = {R.drawable.a1, R.drawable.a2, R.drawable.a3,
            R.drawable.a4, R.drawable.a5, R.drawable.a6, R.drawable.a7, R.drawable.a8, R.drawable.a9,
            R.drawable.a10, R.drawable.a11, R.drawable.a12, R.drawable.a13, R.drawable.a14, R.drawable.a15,
            R.drawable.a16, R.drawable.a17, R.drawable.a18, R.drawable.a19, R.drawable.a20, R.drawable.a21,
            R.drawable.a22, R.drawable.a23, R.drawable.a24, R.drawable.a25, R.drawable.a26, R.drawable.a27,
            R.drawable.a28, R.drawable.a29, R.drawable.a30};
    private static Integer[] mThumbScamImageExercise = {R.drawable.a31, R.drawable.a32, R.drawable.a33,
            R.drawable.a34, R.drawable.a35, R.drawable.a36, R.drawable.a37, R.drawable.a38, R.drawable.a39,
            R.drawable.a40, R.drawable.a41, R.drawable.a42, R.drawable.a43, R.drawable.a44, R.drawable.a45,
            R.drawable.a46, R.drawable.a47, R.drawable.a48, R.drawable.a49, R.drawable.a50, R.drawable.a51,
            R.drawable.a52, R.drawable.a53, R.drawable.a54, R.drawable.a55, R.drawable.a56, R.drawable.a57,
            R.drawable.a58, R.drawable.a59, R.drawable.a60};


    private static void updateLocalDataBase(Context context, String substrSharedText, ContentValues quoteCVA) {
        context.getContentResolver()
                .update(
                        Contract.Entry.makeUriForMathe(substrSharedText),
                        quoteCVA,
                        null,
                        null);
    }

    private static void insertLocalDataBase(Context context, ContentValues quoteCVA) {
        context.getContentResolver()
                .insert(
                        Contract.Entry.URI,
                        quoteCVA);
    }

    public static void requestToLocalDataBase(Context context, String substrSharedText, ContentValues quoteCVA) {
        Cursor rs = context.getContentResolver()
                .query(Contract.Entry.URI,
                        //Contract.Entry.makeUriForMathe(substrSharedText), //Contract.Entry.URI,
                        null,
                        null,
                        null,
                        null);
        int position = Integer.parseInt(substrSharedText) - 1;
        boolean dataAvailable = rs.moveToPosition(position);
        WidgetService.startActionUpdateWidget(context);
        if (dataAvailable) {
            updateLocalDataBase(context, substrSharedText, quoteCVA);
        } else {
            insertLocalDataBase(context, quoteCVA);
        }
        rs.close();
    }


    public static Integer[] inicialImageReference() {
        System.arraycopy(mThumbScamImageExercise, 0, mThumbIdsExercise, 0, 30);
        return mThumbIdsExercise;
    }

    public static Integer[] rememberImageReference() {
        System.arraycopy(mThumbBaseImageExercise, 0, mThumbIdsExercise, 0, 30);
        return mThumbIdsExercise;
    }

    public static Integer[] toSelectImageReference(Integer[] imacorrectpos) {
        System.arraycopy(mThumbScamImageExercise, 0, mThumbIdsExercise, 0, 30);

        for (int iii = 0; iii < 10; iii++) {
            mThumbIdsExercise[imacorrectpos[iii]] = mThumbBaseImageExercise[imacorrectpos[iii]];
        }
        return mThumbIdsExercise;
    }
}
