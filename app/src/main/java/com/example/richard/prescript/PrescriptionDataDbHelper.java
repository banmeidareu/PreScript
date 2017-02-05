package com.example.richard.prescript;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Jason on 2/4/2017.
 */

public class PrescriptionDataDbHelper extends SQLiteOpenHelper {

    //increment the data base version if the schema is changed
    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "PrescriptionData.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + PrescriptionDataContract.PrescriptionEntry.TABLE_NAME + " (" +
                    PrescriptionDataContract.PrescriptionEntry._ID + " INTEGER PRIMARY KEY," +
                    PrescriptionDataContract.PrescriptionEntry.COLUMN_NAME_DRUG + " TEXT," +
                    PrescriptionDataContract.PrescriptionEntry.COLUMN_NAME_DOSE + " TEXT," +
                    PrescriptionDataContract.PrescriptionEntry.COLUMN_NAME_FREQUENCY + " TEXT );";

    private static final String SQL_DELETE_ENRTIES =
            "DROP TABLE IF EXISTS " + PrescriptionDataContract.PrescriptionEntry.TABLE_NAME;

    public PrescriptionDataDbHelper (Context context) {
        super (context,DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate (SQLiteDatabase db) {
        db.execSQL (SQL_CREATE_ENTRIES);
    }

    public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion) {
        //policy is to discard data and start over
        db.execSQL(SQL_DELETE_ENRTIES);
        onCreate(db);
    }
    public void onDownGrade (SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}
