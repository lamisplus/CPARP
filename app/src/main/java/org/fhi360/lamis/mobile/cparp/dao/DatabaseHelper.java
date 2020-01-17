package org.fhi360.lamis.mobile.cparp.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by aalozie on 7/7/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper instance = null;
    //private static final String DB_NAME = "org.fhi360.lamis.mobile.cparp.db";
    private static final String DB_NAME = "cparp.db";
    private static final int DB_VERSION =1;

    // Use the application context, which will ensure that you don't accidentally leak an Activity's context
    public static DatabaseHelper getInstance(Context context) {
        if(instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    /**
     * Constructor should be private to prevent direct instantiation.
     * make call to static factory method "getInstance()" instead.
     */
    private DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        updateDatabase(db, 0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        updateDatabase(db, oldVersion, newVersion);
    }

    private static void updateDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 1) {
            //Create USER table
            String sql = "CREATE TABLE user(_id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, password TEXT);";
            db.execSQL(sql);

            //Create ACCOUNT table
            sql = "CREATE TABLE account(_id INTEGER PRIMARY KEY AUTOINCREMENT, communitypharm_id INTEGER NOT NULL, pharmacy TEXT NOT NULL, address TEXT, phone TEXT, email TEXT, pin TEXT, time_last_sync NUMERIC);";
            db.execSQL(sql);

            //Create FACILITY table
            sql = "CREATE TABLE facility(_id INTEGER PRIMARY KEY AUTOINCREMENT, facility_id INTEGER NOT NULL, state TEXT, lga TEXT, name TEXT );";
            db.execSQL(sql);

            //Create REGIMEN
            sql = "CREATE TABLE regimen(_id INTEGER PRIMARY KEY AUTOINCREMENT, regimen_id INTEGER NOT NULL, regimen TEXT, regimentype_id INTEGER, regimentype TEXT);";
            db.execSQL(sql);

            //Create PATIENT table
            sql = "CREATE TABLE patient(_id INTEGER PRIMARY KEY AUTOINCREMENT, facility_id INTEGER NOT NULL, patient_id INTEGER NOT NULL, hospital_num TEXT, unique_id TEXT, surname TEXT, other_names TEXT, gender TEXT, date_birth NUMERIC, address TEXT, phone TEXT, date_started NUMERIC, regimentype TEXT, regimen TEXT, last_clinic_stage TEXT, " +
                    " last_viral_load NUMERIC, date_last_viral_load NUMERIC, viral_load_due_date NUMERIC, viral_load_type TEXT, last_cd4 NUMERIC, date_last_cd4 NUMERIC, date_last_refill NUMERIC, date_next_refill NUMERIC, date_last_clinic NUMERIC, date_next_clinic NUMERIC, last_refill_setting TEXT, discontinued NUMERIC);";
            db.execSQL(sql);

            //Create DEVOLVE table
            sql = "CREATE TABLE devolve(_id INTEGER PRIMARY KEY AUTOINCREMENT, facility_id INTEGER NOT NULL, patient_id INTEGER NOT NULL, date_devolved NUMERIC NOT NULL, viral_load_assessed TEXT, last_viral_load NUMERIC, date_last_viral_load NUMERIC, cd4_assessed TEXT, last_cd4 NUMERIC, date_last_cd4 NUMERIC, " +
                    " last_clinic_stage TEXT, date_last_clinic_stage NUMERIC, arv_dispensed TEXT, regimentype TEXT, regimen TEXT, date_last_refill NUMERIC, date_next_refill NUMERIC, date_last_clinic NUMERIC, date_next_clinic NUMERIC, notes TEXT, date_discontinued NUMERIC, reason_discontinued TEXT);";
            db.execSQL(sql);

            //Create ENCOUNTER table
            sql = "CREATE TABLE encounter(_id INTEGER PRIMARY KEY AUTOINCREMENT, facility_id INTEGER NOT NULL, patient_id INTEGER NOT NULL, date_visit NUMERIC NOT NULL, question1 TEXT, question2 TEXT, question3 TEXT, question4 TEXT, question5 TEXT, question6 TEXT, question7 TEXT, question8 TEXT, question9 TEXT, " +
                    " regimen1 TEXT, regimen2 TEXT, regimen3 TEXT, regimen4 TEXT, duration1 INTEGER, duration2 INTEGER, duration3 INTEGER, duration4 INTEGER, prescribed1 INTEGER, prescribed2 INTEGER, prescribed3 INTEGER, prescribed4 INTEGER, dispensed1 INTEGER, dispensed2 INTEGER, dispensed3 INTEGER, dispensed4 INTEGER, notes TEXT, next_refill NUMERIC, regimentype TEXT, time_stamp NUMERIC);";
            db.execSQL(sql);

            //Create CHRONICCARE table
            sql = "CREATE TABLE chroniccare(_id INTEGER PRIMARY KEY AUTOINCREMENT, facility_id INTEGER NOT NULL, patient_id INTEGER NOT NULL, date_visit NUMERIC NOT NULL, client_type TEXT, current_status TEXT, clinic_stage TEXT, pregnancy_status TEXT, last_cd4 NUMERIC, date_last_cd4 NUMERIC, last_viral_load NUMERIC, date_last_viral_load NUMERIC, eligible_cd4 TEXT, eligible_viral_load TEXT, cotrim_eligibility TEXT, ipt TEXT, eligible_ipt TEXT, inh TEXT, tb_treatment TEXT, " +
                    " date_started_tb_treatment NUMERIC, tb_referred TEXT, tb_value1 TEXT, tb_value2 TEXT, tb_value3 TEXT, tb_value4 TEXT, tb_value5 TEXT, body_weight NUMERIC, height NUMERIC, bmi NUMERIC, bmi_category TEXT, muac NUMERIC, muac_category TEXT, supplementary_food TEXT, nutritional_status_referred TEXT, gbv1 TEXT, gbv1_referred TEXT, gbv2 TEXT, gbv2_referred TEXT, hypertensive TEXT, first_hypertensive TEXT, bp TEXT, bp_above TEXT, bp_referred TEXT, " +
                    " diabetic TEXT, first_diabetic TEXT, dm_value1 TEXT, dm_value2 TEXT, dm_referred TEXT, phdp1 TEXT, phdp1_services_provided TEXT, phdp2 TEXT, phdp3 TEXT, phdp4 TEXT, phdp4_services_provided TEXT, phdp5 TEXT, phdp6 INTEGER, phdp7 INTEGER, phdp7_services_provided TEXT, phdp8_services_provided TEXT, additional_services_provided TEXT, reproductive_intentions1 TEXT, reproductive_intentions1_referred TEXT, " +
                    " reproductive_intentions2 TEXT, reproductive_intentions2_referred TEXT, reproductive_intentions3 TEXT, reproductive_intentions3_referred TEXT, malaria_prevention1 TEXT, malaria_prevention1_referred TEXT, malaria_prevention2 TEXT, malaria_prevention2_referred TEXT, time_stamp NUMERIC);";
            db.execSQL(sql);

            //Create DRUGTHERAPY table
            sql = "CREATE TABLE drugtherapy(_id INTEGER PRIMARY KEY AUTOINCREMENT, facility_id INTEGER NOT NULL, patient_id INTEGER NOT NULL, date_visit NUMERIC NOT NULL, ois TEXT, therapy_problem_screened TEXT, adherence_issues TEXT, medication_error TEXT, adrs TEXT, severity TEXT, icsr_form TEXT, adr_referred TEXT, time_stamp NUMERIC);";
            db.execSQL(sql);

            //Create APPOINTMENT table
            sql = "CREATE TABLE appointment(_id INTEGER PRIMARY KEY AUTOINCREMENT, facility_id INTEGER NOT NULL, patient_id INTEGER NOT NULL, date_tracked NUMERIC NOT NULL, type_tracking TEXT, tracking_outcome TEXT, date_last_visit NUMERIC, date_next_visit NUMERIC, date_agreed NUMERIC, time_stamp NUMERIC);";
            db.execSQL(sql);

            //Create HTC table
            sql = "CREATE TABLE htc(_id INTEGER PRIMARY KEY AUTOINCREMENT, month INTEGER NOT NULL, year INTEGER NOT NULL, num_tested INTEGER, num_positive INTEGER, num_referred INTEGER, num_onsite_visit INTEGER, time_stamp NUMERIC);";
            db.execSQL(sql);

            //Create MONITOR table
            sql = "CREATE TABLE monitor(_id INTEGER PRIMARY KEY AUTOINCREMENT, facility_id INTEGER NOT NULL, entity_id TEXT NOT NULL, table_name TEXT NOT NULL, operation_id NUMERIC NOT NULL, time_stamp NUMERIC);";
            db.execSQL(sql);
        }
        else {
            if(DB_VERSION == 1) {
                //Update database tables
            }

            if(DB_VERSION == 2) {
                //Update database tables
            }
        }
    }
}
