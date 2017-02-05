package com.example.richard.prescript;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class PrescriptionList extends AppCompatActivity {

    PrescriptionDataDbHelper dbHelper;
    public final String err = PrescriptionList.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription_list);

        dbHelper = new PrescriptionDataDbHelper(getApplicationContext());
        addPrescription("Ibuprofin", "Twice a week", "1");
        displayDatabase();
        /*
        for (int i = 0; i < 12; i++) {
            deletePrescription(i);
        }*/
    }

    private void addDefaultDrug () {
        addPrescription("Tylenol", "Every day", "3");
    }

    public void addPrescription (String drug, String frequency, String dose) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        //create map of values, where columns are keys
        ContentValues values = new ContentValues();
        values.put (PrescriptionDataContract.PrescriptionEntry.COLUMN_NAME_DRUG, drug);
        values.put (PrescriptionDataContract.PrescriptionEntry.COLUMN_NAME_FREQUENCY, frequency);
        values.put (PrescriptionDataContract.PrescriptionEntry.COLUMN_NAME_DOSE, dose);

        //insert the new row
        long newRowId = db.insert (PrescriptionDataContract.PrescriptionEntry.TABLE_NAME, null, values);
    }

    private void displayDatabase () {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        //define projection which specifies which columns will be used
        String[] projection = {
                PrescriptionDataContract.PrescriptionEntry._ID,
                PrescriptionDataContract.PrescriptionEntry.COLUMN_NAME_DRUG,
                PrescriptionDataContract.PrescriptionEntry.COLUMN_NAME_DOSE,
                PrescriptionDataContract.PrescriptionEntry.COLUMN_NAME_FREQUENCY
        };

        //Filter results for WHERE clauses
        String selection = null;//PrescriptionDataContract.PrescriptionEntry.COLUMN_NAME_DRUG + " = ?";
        String[] selectionArgs = null;// "Tylenol" };

        //How results are sorted
        String sortOrder = null;
                //PrescriptionDataContract.PrescriptionEntry.COLUMN_NAME_DRUG;

        Cursor cursor = db.query(
                PrescriptionDataContract.PrescriptionEntry.TABLE_NAME,
                projection, selection, selectionArgs,
                null, null,
                sortOrder
        );


        List drugNames = new ArrayList<>();
        while (cursor.moveToNext()) {
            String entryDrugName = cursor.getString(
                    cursor.getColumnIndexOrThrow(PrescriptionDataContract.PrescriptionEntry.COLUMN_NAME_DRUG));
            String entryFrequency = cursor.getString (
                    cursor.getColumnIndexOrThrow(PrescriptionDataContract.PrescriptionEntry.COLUMN_NAME_FREQUENCY));
            String entryDose = cursor.getString (
                    cursor.getColumnIndexOrThrow(PrescriptionDataContract.PrescriptionEntry.COLUMN_NAME_DOSE));
            drugNames.add (entryDrugName);
            Log.d(err, "drug: " + entryDrugName + " frequency: " + entryFrequency+ " dose: " + entryDose);
        }
        cursor.close();
    }

    public void deletePrescription (int id) {
        String selection = PrescriptionDataContract.PrescriptionEntry._ID + " LIKE ?";
        String [] selectionArgs = { Integer.toString(id) };
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(PrescriptionDataContract.PrescriptionEntry.TABLE_NAME, selection, selectionArgs);
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}
