package com.example.richard.prescript;

import android.provider.BaseColumns;

/**
 * Created by Jason on 2/4/2017.
 */

public final class PrescriptionDataContract {
    //private constructor
    private PrescriptionDataContract() {}

    public static class PrescriptionEntry implements BaseColumns {
        public static final String TABLE_NAME = "prescriptions";
        public static final String COLUMN_NAME_DRUG = "drug";
        public static final String COLUMN_NAME_DOSE = "dose";
        public static final String COLUMN_NAME_FREQUENCY = "frequency";
    }

}
