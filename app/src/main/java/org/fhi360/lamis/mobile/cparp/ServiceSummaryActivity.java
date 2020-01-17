package org.fhi360.lamis.mobile.cparp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.fhi360.lamis.mobile.cparp.dao.DatabaseHelper;
import org.fhi360.lamis.mobile.cparp.dao.HtcDAO;
import org.fhi360.lamis.mobile.cparp.dao.PatientDAO;
import org.fhi360.lamis.mobile.cparp.model.Patient;
import org.fhi360.lamis.mobile.cparp.R;
import org.fhi360.lamis.mobile.cparp.model.Htc;
import org.fhi360.lamis.mobile.cparp.utility.DateUtil;

import java.util.Date;


public class ServiceSummaryActivity extends AppCompatActivity {
    private int art1ml15, art1mg15, art1fl15, art1fg15;
    private int art2ml15, art2mg15, art2fl15, art2fg15;
    private int art3ml15, art3mg15, art3fl15, art3fg15;
    private int art4ml15, art4mg15, art4fl15, art4fg15;
    private int art5ml15, art5mg15, art5fl15, art5fg15;
    private int art6ml15, art6mg15, art6fl15, art6fg15;
    private int art7ml15, art7mg15, art7fl15, art7fg15;
    private int art8ml15, art8mg15, art8fl15, art8fg15;
    private int art9ml15, art9mg15, art9fl15, art9fg15;
    private int art10ml15, art10mg15, art10fl15, art10fg15;
    private int art11ml15, art11mg15, art11fl15, art11fg15;
    private int art12ml15, art12mg15, art12fl15, art12fg15;
    private int art13ml15, art13mg15, art13fl15, art13fg15;
    private int art14ml15, art14mg15, art14fl15, art14fg15;
    private int art15ml15, art15mg15, art15fl15, art15fg15;
    private int art16ml15, art16mg15, art16fl15, art16fg15;
    private int art17ml15, art17mg15, art17fl15, art17fg15;
    private int art18ml15, art18mg15, art18fl15, art18fg15;
    private int art19ml15, art19mg15, art19fl15, art19fg15;
    private int art20ml15, art20mg15, art20fl15, art20fg15;
    private int art21ml15, art21mg15, art21fl15, art21fg15;
    private int art22ml15, art22mg15, art22fl15, art22fg15;
    private int art23ml15, art23mg15, art23fl15, art23fg15;

    private int month, year;

    private Context context;

    private SQLiteOpenHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_summary);

        Intent intent = getIntent();
        month = intent.getIntExtra("month", 0);
        year = intent.getIntExtra("year", 0);
        Log.v("ServiceSummary", "Period...." + month + ", " + year);
        if (month != 0 && year != 0) {
            calculate();
            restoreViews();
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Monthly Service Summary");
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void calculate() {
        try {
            databaseHelper = DatabaseHelper.getInstance(this);
            SQLiteDatabase db = databaseHelper.getReadableDatabase();

            String query = "SELECT * FROM devolve";
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(0);
                    int facilityId = cursor.getInt(1);
                    int patientId = cursor.getInt(2);
                    //Get gender and date of birth of this patient
                    //Use the date of birth to determine age as at end of reporting month
                    Patient patient = new PatientDAO(this).getPatient(facilityId, patientId);
                    String gender = patient.getGender() != null ? patient.getGender() : "";
                    Date dob = patient.getDateBirth();
                    Date ref = DateUtil.getLastDateOfMonth(year, month);
                    int age = DateUtil.getAge(dob, ref);
                    Date dateDevolved = DateUtil.unixTimestampToDate(cursor.getLong(3), "dd/MM/yyyy");
                    String viralLoadAssessed = cursor.getString(4);
                    double lastViralLoad = cursor.getDouble(5);
                    Date dateLastViralLoad = DateUtil.unixTimestampToDate(cursor.getLong(6), "dd/MM/yyyy");
                    String cd4Assessed = cursor.getString(7);
                    double lastCd4 = cursor.getDouble(8);
                    Date dateLastCd4 = DateUtil.unixTimestampToDate(cursor.getLong(9), "dd/MM/yyyy");
                    String lastClinicStage = cursor.getString(10);
                    Date dateLastClinicStage = DateUtil.unixTimestampToDate(cursor.getLong(11), "dd/MM/yyyy");
                    String arvDispensed = cursor.getString(12);
                    String regimentype = cursor.getString(13);
                    String regimen = cursor.getString(14);
                    Date dateLastRefill = DateUtil.unixTimestampToDate(cursor.getLong(15), "dd/MM/yyyy");
                    Date dateNextRefill = DateUtil.unixTimestampToDate(cursor.getLong(16), "dd/MM/yyyy");
                    Date dateLastClinic = DateUtil.unixTimestampToDate(cursor.getLong(17), "dd/MM/yyyy");
                    Date dateNextClinic = DateUtil.unixTimestampToDate(cursor.getLong(18), "dd/MM/yyyy");
                    String notes = cursor.getString(19);
                    Date dateDiscontinued = DateUtil.unixTimestampToDate(cursor.getLong(20), "dd/MM/yyyy");
                    String reasonDiscontinued = cursor.getString(21);

                    if (gender.equalsIgnoreCase("Male")) {
                        if (age < 15) {
                            //ART 1 - Number of ART clients devolved to the Community Pharmacy to receive ART refills (CUMMULATIVE)
                            //ART 2 - Number of ART clients devolved to the Community Pharmacy to receive ART refills this reporting month
                            //ART 3 - Number of devolved ART clients who were scheduled to access ART refills in community pharmacy this reporting month
                            art1ml15++;
                            if (DateUtil.getMonth(dateDevolved) == month && DateUtil.getYear(dateDevolved) == year)
                                art2ml15++;
                            if (DateUtil.getMonth(dateNextRefill) == month && DateUtil.getYear(dateNextRefill) == year)
                                art3ml15++;

                            //ART 8 - Number of devolved ART clients who received refills for Cotrimoxazole prophylaxis (CTX) this reporting month
                            //ART 9 - Number of devolved ART clients who received refill for Isoniazid prophylaxis (INH) this reporting month
                            //ART 10 - Number of devolved ART clients who were provided Chronic Care Screening services using care and support checklist this reporting month
                            //ART 11 - Number of devolved ART clients who had medication adherence issue(s) this reporting month
                            //ART 12 - Number of devolved ART clients with suspected Adverse Drug Reactions (ADRs) this reporting month
                            //ART 13 - Number of individual case safety reports form [ADR reports] filled for devolved clients this reporting month
                            if (cotrim(facilityId, patientId, month, year)) art8ml15++;
                            if (inh(facilityId, patientId, month, year)) art9ml15++;
                            if (chroniccare(facilityId, patientId, month, year)) art10ml15++;
                            if (adhereissues(facilityId, patientId, month, year)) art11ml15++;
                            if (adrs(facilityId, patientId, month, year)) art12ml15++;
                            if (icsr(facilityId, patientId, month, year)) art13ml15++;

                            //ART 14 - Number of devolved ART clients who are eligible to return back to hospital for semiannual clinical and laboratory reassements this reporting month
                            //ART 15 - Number of devolved ART clients who are eligible for viral load re-assessment this reporting month
                            if (DateUtil.getMonth(dateNextClinic) == month && DateUtil.getYear(dateNextClinic) == year)
                                art14ml15++;
                            //if(DateUtil.getMonth(viralLoadDueDate) == month && DateUtil.getYear(viralLoadDueDate) == year) art15ml15++;

                            //ART 17 - Number of devolved ART clients who accessed viral load re-assessment with viral load test result this reporting month
                            //ART 18 - Number of devolved ART clients who had viral load re-assessment and are virologically suppressed (VL <1000 c/ml) this reporting month
                            //ART 19 - Number of devolved ART clients who discontinued Community ART refill services and returned back to the Hospital to continue ART refill services this reporting
                            //if(DateUtil.getMonth(dateLastViralLoad) == month && DateUtil.getYear(dateLastViralLoad) == year) art17ml15++;
                            if (lastViralLoad < 1000) art18ml15++;
                            if (DateUtil.getMonth(dateDiscontinued) == month && DateUtil.getYear(dateDiscontinued) == year)
                                art19ml15++;

                        } else {
                            art1mg15++;
                            if (DateUtil.getMonth(dateDevolved) == month && DateUtil.getYear(dateDevolved) == year)
                                art2mg15++;
                            if (DateUtil.getMonth(dateNextRefill) == month && DateUtil.getYear(dateNextRefill) == year)
                                art3mg15++;
                            if (lastViralLoad < 1000) art2mg15++;
                            if (DateUtil.getMonth(dateDiscontinued) == month && DateUtil.getYear(dateDiscontinued) == year)
                                art3ml15++;
                        }
                    } else {
                        if (age < 15) {
                            art1fl15++;
                            if (DateUtil.getMonth(dateDevolved) == month && DateUtil.getYear(dateDevolved) == year)
                                art2fl15++;
                            if (DateUtil.getMonth(dateNextRefill) == month && DateUtil.getYear(dateNextRefill) == year)
                                art3fl15++;
                            if (lastViralLoad < 1000) art2fl15++;
                            if (DateUtil.getMonth(dateDiscontinued) == month && DateUtil.getYear(dateDiscontinued) == year)
                                art3ml15++;
                        } else {
                            art1fg15++;
                            if (DateUtil.getMonth(dateDevolved) == month && DateUtil.getYear(dateDevolved) == year)
                                art2fg15++;
                            if (DateUtil.getMonth(dateNextRefill) == month && DateUtil.getYear(dateNextRefill) == year)
                                art3fg15++;
                            if (lastViralLoad < 1000) art2fl15++;
                            if (DateUtil.getMonth(dateDiscontinued) == month && DateUtil.getYear(dateDiscontinued) == year)
                                art3ml15++;
                        }
                    }
                } while (cursor.moveToNext());
            }


/*
            query = "SELECT * FROM encounter WHERE YEAR(date_visit) = " + year + " AND MONTH(date_visit) = " + month;
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(0);
                    int facilityId = cursor.getInt(1);
                    int patientId = cursor.getInt(2);

                    //Get gender and date of birth of this patient
                    //Use the date of birth to determine age as at end of reporting month
                    Patient patient = new  PatientDAO(this).getPatient(facilityId, patientId);
                    String gender = patient.getGender();
                    Date dob = patient.getDateBirth();
                    Date ref = DateUtil.getLastDateOfMonth(year, month);
                    int age = DateUtil.getAge(dob, ref);

                    Date dateVisit = DateUtil.unixTimestampToDate(cursor.getLong(3), "dd/MM/yyyy");
                    String question3 = cursor.getString(6);

                    if(gender.equalsIgnoreCase("Male")) {
                        if(age < 15) {
                            //ART 4 - Number of  devolved ART clients who accessed ART refills within 7 days of appointment in community pharmacy this reporting month
                            //ART 5 - Number of devolved ART clients who defaulted to come for a refill within 7 days of the appointment date at the Community Pharmacy this reporting month
                            //ART 6 - Number of devolved ART clients who defaulted to come for a refill, were tracked or not and returned to pick up ART refill at the Community Pharmacy this reporting month
                            //ART 7 - Number of devolved ART clients who defaulted to come for a refill within 7 days of appointment, were tracked or not and did not return for ART refill at the Community Pharmacy this reporting month
                            if(DateUtil.getMonth(dateVisit) == month && DateUtil.getYear(dateVisit) == year) art4ml15++;

                            //ART 16 - Number of devolved ART clients who had clinical or/& laboratory re-assessments done at the hospital with new ART prescriptions for Community ART refills this reporting month
                            if(question3.equalsIgnoreCase("YES")) art16mg15++;
                        }
                        else {
                        }
                    }
                    else {
                        if(age < 15) {
                            if(DateUtil.getMonth(dateVisit) == month && DateUtil.getYear(dateVisit) == year) art4fl15++;
                        }
                        else {
                            if(DateUtil.getMonth(dateVisit) == month && DateUtil.getYear(dateVisit) == year) art4fg15++;
                        }
                    }
                } while (cursor.moveToNext());
            }
*/

            //HIV Testing Services
            HtcDAO htcDAO = new HtcDAO(context);
            int htcId = htcDAO.getId(month, year);
            if (htcId != 0) {
                Htc htc = htcDAO.getHtc(htcId);
                art20ml15 = htc.getNumTested();
                art21ml15 = htc.getNumPositive();
                art22ml15 = htc.getNumReferred();
                art23ml15 = htc.getNumOnsiteVisit();
            }
            cursor.close();
            db.close();
        } catch (SQLiteException exception) {
            Toast toast = Toast.makeText(context, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private boolean cotrim(int facilityId, int patientId, int month, int year) {
        boolean found = false;
        System.out.println("FACILITY "+facilityId );
        System.out.println("patientId "+patientId );
        System.out.println("patientId "+month );
        System.out.println("year "+year );
        try {
            databaseHelper = DatabaseHelper.getInstance(context);
            SQLiteDatabase db = databaseHelper.getReadableDatabase();

            Cursor cursor = db.query("ENCOUNTER",
                    new String[]{"_id"},
                    "facility_id = ? AND patient_id = ? AND MONTH(date_visit) = ? AND YEAR(date_visit) = ? AND regimen3 != ''", new String[]{Integer.toString(facilityId), Integer.toString(patientId), Integer.toString(month), Integer.toString(year)}, null, null, null);
            if (cursor.moveToFirst()) found = true;
            cursor.close();
            db.close();
        } catch (SQLiteException exception) {
          //  Toast toast = Toast.makeText(context, "Database unavailable", Toast.LENGTH_SHORT);
           // toast.show();
        }
        return found;
    }

    private boolean inh(int facilityId, int patientId, int month, int year) {
        boolean found = false;
        try {
            databaseHelper = DatabaseHelper.getInstance(context);
            SQLiteDatabase db = databaseHelper.getReadableDatabase();

            Cursor cursor = db.query("ENCOUNTER",
                    new String[]{"_id"},
                    "facility_id = ? AND patient_id = ? AND MONTH(date_visit) = ? AND YEAR(date_visit) = ? AND regimen3 != ''", new String[]{Integer.toString(facilityId), Integer.toString(patientId), Integer.toString(month), Integer.toString(year)}, null, null, null);
            if (cursor.moveToFirst()) found = true;
            cursor.close();
            db.close();
        } catch (SQLiteException exception) {
            exception.printStackTrace();
           // Toast toast = Toast.makeText(context, "Database unavailable", Toast.LENGTH_SHORT);
            //toast.show();
        }
        return found;
    }

    private boolean chroniccare(int facilityId, int patientId, int month, int year) {
        boolean found = false;
        try {
            databaseHelper = DatabaseHelper.getInstance(context);
            SQLiteDatabase db = databaseHelper.getReadableDatabase();

            Cursor cursor = db.query("CHRONICCARE",
                    new String[]{"_id"},
                    "facility_id = ? AND patient_id = ? AND MONTH(date_visit) = ? AND YEAR(date_visit) = ?", new String[]{Integer.toString(facilityId), Integer.toString(patientId), Integer.toString(month), Integer.toString(year)}, null, null, null);
            if (cursor.moveToFirst()) found = true;
            cursor.close();
            db.close();
        } catch (SQLiteException exception) {
//            Toast toast = Toast.makeText(context, "Database unavailable", Toast.LENGTH_SHORT);
          //  toast.show();
        }
        return found;
    }

    private boolean adhereissues(int facilityId, int patientId, int month, int year) {
        boolean found = false;
        try {
            databaseHelper = DatabaseHelper.getInstance(context);
            SQLiteDatabase db = databaseHelper.getReadableDatabase();

            Cursor cursor = db.query("DRUGTHERAPY",
                    new String[]{"_id"},
                    "facility_id = ? AND patient_id = ? AND MONTH(date_visit) = ? AND YEAR(date_visit) = ? AND adherence_issues != ''", new String[]{Integer.toString(facilityId), Integer.toString(patientId), Integer.toString(month), Integer.toString(year)}, null, null, null);
            if (cursor.moveToFirst()) found = true;
            cursor.close();
            db.close();
        } catch (SQLiteException exception) {
            //Toast toast = Toast.makeText(context, "Database unavailable", Toast.LENGTH_SHORT);
           // toast.show();
        }
        return found;
    }

    private boolean adrs(int facilityId, int patientId, int month, int year) {
        boolean found = false;
        try {
            databaseHelper = DatabaseHelper.getInstance(context);
            SQLiteDatabase db = databaseHelper.getReadableDatabase();

            Cursor cursor = db.query("DRUGTHERAPY",
                    new String[]{"_id"},
                    "facility_id = ? AND patient_id = ? AND MONTH(date_visit) = ? AND YEAR(date_visit) = ? AND adrs != ''", new String[]{Integer.toString(facilityId), Integer.toString(patientId), Integer.toString(month), Integer.toString(year)}, null, null, null);
            if (cursor.moveToFirst()) found = true;
            cursor.close();
            db.close();
        } catch (SQLiteException exception) {
//            Toast toast = Toast.makeText(context, "Database unavailable", Toast.LENGTH_SHORT);
          //  toast.show();
        }
        return found;
    }

    private boolean icsr(int facilityId, int patientId, int month, int year) {
        boolean found = false;
        try {
            databaseHelper = DatabaseHelper.getInstance(context);
            SQLiteDatabase db = databaseHelper.getReadableDatabase();

            Cursor cursor = db.query("DRUGTHERAPY",
                    new String[]{"_id"},
                    "facility_id = ? AND patient_id = ? AND MONTH(date_visit) = ? AND YEAR(date_visit) = ? AND icsr_form != ''", new String[]{Integer.toString(facilityId), Integer.toString(patientId), Integer.toString(month), Integer.toString(year)}, null, null, null);
            if (cursor.moveToFirst()) found = true;
            cursor.close();
            db.close();
        } catch (SQLiteException exception) {
           // Toast toast = Toast.makeText(context, "Database unavailable", Toast.LENGTH_SHORT);
            //toast.show();
        }
        return found;
    }

    private void restoreViews() {
        String mnth = DateUtil.getMonth(month);
        ((TextView) findViewById(R.id.month)).setText(mnth);
        ((TextView) findViewById(R.id.year)).setText(Integer.toString(year));
        Log.v("ServiceSummary", "Period...." + mnth + ", " + year);

        ((TextView) findViewById(R.id.art1ml15)).setText(Integer.toString(art1ml15));
        ((TextView) findViewById(R.id.art1mg15)).setText(Integer.toString(art1mg15));
        ((TextView) findViewById(R.id.art1fl15)).setText(Integer.toString(art1fl15));
        ((TextView) findViewById(R.id.art1fg15)).setText(Integer.toString(art1fg15));
        int total = art1ml15 + art1mg15 + art1fl15 + art1fg15;
        ((TextView) findViewById(R.id.art1t)).setText(Integer.toString(total));

        ((TextView) findViewById(R.id.art2ml15)).setText(Integer.toString(art2ml15));
        ((TextView) findViewById(R.id.art2mg15)).setText(Integer.toString(art2mg15));
        ((TextView) findViewById(R.id.art2fl15)).setText(Integer.toString(art2fl15));
        ((TextView) findViewById(R.id.art2fg15)).setText(Integer.toString(art2fg15));
        total = art2ml15 + art2mg15 + art2fl15 + art2fg15;
        ((TextView) findViewById(R.id.art2t)).setText(Integer.toString(total));

        ((TextView) findViewById(R.id.art3ml15)).setText(Integer.toString(art3ml15));
        ((TextView) findViewById(R.id.art3mg15)).setText(Integer.toString(art3mg15));
        ((TextView) findViewById(R.id.art3fl15)).setText(Integer.toString(art3fl15));
        ((TextView) findViewById(R.id.art3fg15)).setText(Integer.toString(art3fg15));
        total = art3ml15 + art3mg15 + art3fl15 + art3fg15;
        ((TextView) findViewById(R.id.art3t)).setText(Integer.toString(total));

        ((TextView) findViewById(R.id.art4ml15)).setText(Integer.toString(art4ml15));
        ((TextView) findViewById(R.id.art4mg15)).setText(Integer.toString(art4mg15));
        ((TextView) findViewById(R.id.art4fl15)).setText(Integer.toString(art4fl15));
        ((TextView) findViewById(R.id.art4fg15)).setText(Integer.toString(art4fg15));
        total = art4ml15 + art4mg15 + art4fl15 + art4fg15;
        ((TextView) findViewById(R.id.art4t)).setText(Integer.toString(total));

        ((TextView) findViewById(R.id.art5ml15)).setText(Integer.toString(art5ml15));
        ((TextView) findViewById(R.id.art5mg15)).setText(Integer.toString(art5mg15));
        ((TextView) findViewById(R.id.art5fl15)).setText(Integer.toString(art5fl15));
        ((TextView) findViewById(R.id.art5fg15)).setText(Integer.toString(art5fg15));
        total = art5ml15 + art5mg15 + art5fl15 + art5fg15;
        ((TextView) findViewById(R.id.art5t)).setText(Integer.toString(total));

        ((TextView) findViewById(R.id.art6ml15)).setText(Integer.toString(art6ml15));
        ((TextView) findViewById(R.id.art6mg15)).setText(Integer.toString(art6mg15));
        ((TextView) findViewById(R.id.art6fl15)).setText(Integer.toString(art6fl15));
        ((TextView) findViewById(R.id.art6fg15)).setText(Integer.toString(art6fg15));
        total = art6ml15 + art6mg15 + art6fl15 + art6fg15;
        ((TextView) findViewById(R.id.art6t)).setText(Integer.toString(total));

        ((TextView) findViewById(R.id.art7ml15)).setText(Integer.toString(art7ml15));
        ((TextView) findViewById(R.id.art7mg15)).setText(Integer.toString(art7mg15));
        ((TextView) findViewById(R.id.art7fl15)).setText(Integer.toString(art7fl15));
        ((TextView) findViewById(R.id.art7fg15)).setText(Integer.toString(art7fg15));
        total = art7ml15 + art7mg15 + art7fl15 + art7fg15;
        ((TextView) findViewById(R.id.art7t)).setText(Integer.toString(total));

        ((TextView) findViewById(R.id.art8ml15)).setText(Integer.toString(art8ml15));
        ((TextView) findViewById(R.id.art8mg15)).setText(Integer.toString(art8mg15));
        ((TextView) findViewById(R.id.art8fl15)).setText(Integer.toString(art8fl15));
        ((TextView) findViewById(R.id.art8fg15)).setText(Integer.toString(art8fg15));
        total = art8ml15 + art8mg15 + art8fl15 + art8fg15;
        ((TextView) findViewById(R.id.art8t)).setText(Integer.toString(total));

        ((TextView) findViewById(R.id.art9ml15)).setText(Integer.toString(art9ml15));
        ((TextView) findViewById(R.id.art9mg15)).setText(Integer.toString(art9mg15));
        ((TextView) findViewById(R.id.art9fl15)).setText(Integer.toString(art9fl15));
        ((TextView) findViewById(R.id.art9fg15)).setText(Integer.toString(art9fg15));
        total = art9ml15 + art9mg15 + art9fl15 + art9fg15;
        ((TextView) findViewById(R.id.art9t)).setText(Integer.toString(total));

        ((TextView) findViewById(R.id.art10ml15)).setText(Integer.toString(art10ml15));
        ((TextView) findViewById(R.id.art10mg15)).setText(Integer.toString(art10mg15));
        ((TextView) findViewById(R.id.art10fl15)).setText(Integer.toString(art10fl15));
        ((TextView) findViewById(R.id.art10fg15)).setText(Integer.toString(art10fg15));
        total = art10ml15 + art10mg15 + art10fl15 + art10fg15;
        ((TextView) findViewById(R.id.art10t)).setText(Integer.toString(total));

        ((TextView) findViewById(R.id.art11ml15)).setText(Integer.toString(art11ml15));
        ((TextView) findViewById(R.id.art11mg15)).setText(Integer.toString(art11mg15));
        ((TextView) findViewById(R.id.art11fl15)).setText(Integer.toString(art11fl15));
        ((TextView) findViewById(R.id.art11fg15)).setText(Integer.toString(art11fg15));
        total = art11ml15 + art11mg15 + art11fl15 + art11fg15;
        ((TextView) findViewById(R.id.art11t)).setText(Integer.toString(total));

        ((TextView) findViewById(R.id.art12ml15)).setText(Integer.toString(art12ml15));
        ((TextView) findViewById(R.id.art12mg15)).setText(Integer.toString(art12mg15));
        ((TextView) findViewById(R.id.art12fl15)).setText(Integer.toString(art12fl15));
        ((TextView) findViewById(R.id.art12fg15)).setText(Integer.toString(art12fg15));
        total = art12ml15 + art12mg15 + art12fl15 + art12fg15;
        ((TextView) findViewById(R.id.art12t)).setText(Integer.toString(total));

        ((TextView) findViewById(R.id.art13ml15)).setText(Integer.toString(art13ml15));
        ((TextView) findViewById(R.id.art13mg15)).setText(Integer.toString(art13mg15));
        ((TextView) findViewById(R.id.art13fl15)).setText(Integer.toString(art13fl15));
        ((TextView) findViewById(R.id.art13fg15)).setText(Integer.toString(art13fg15));
        total = art13ml15 + art13mg15 + art13fl15 + art13fg15;
        ((TextView) findViewById(R.id.art13t)).setText(Integer.toString(total));

        ((TextView) findViewById(R.id.art14ml15)).setText(Integer.toString(art14ml15));
        ((TextView) findViewById(R.id.art14mg15)).setText(Integer.toString(art14mg15));
        ((TextView) findViewById(R.id.art14fl15)).setText(Integer.toString(art14fl15));
        ((TextView) findViewById(R.id.art14fg15)).setText(Integer.toString(art14fg15));
        total = art14ml15 + art14mg15 + art14fl15 + art14fg15;
        ((TextView) findViewById(R.id.art14t)).setText(Integer.toString(total));

        ((TextView) findViewById(R.id.art15ml15)).setText(Integer.toString(art15ml15));
        ((TextView) findViewById(R.id.art15mg15)).setText(Integer.toString(art15mg15));
        ((TextView) findViewById(R.id.art15fl15)).setText(Integer.toString(art15fl15));
        ((TextView) findViewById(R.id.art15fg15)).setText(Integer.toString(art15fg15));
        total = art15ml15 + art15mg15 + art15fl15 + art15fg15;
        ((TextView) findViewById(R.id.art15t)).setText(Integer.toString(total));

        ((TextView) findViewById(R.id.art16ml15)).setText(Integer.toString(art16ml15));
        ((TextView) findViewById(R.id.art16mg15)).setText(Integer.toString(art16mg15));
        ((TextView) findViewById(R.id.art16fl15)).setText(Integer.toString(art16fl15));
        ((TextView) findViewById(R.id.art16fg15)).setText(Integer.toString(art16fg15));
        total = art16ml15 + art16mg15 + art16fl15 + art16fg15;
        ((TextView) findViewById(R.id.art16t)).setText(Integer.toString(total));

        ((TextView) findViewById(R.id.art17ml15)).setText(Integer.toString(art17ml15));
        ((TextView) findViewById(R.id.art17mg15)).setText(Integer.toString(art17mg15));
        ((TextView) findViewById(R.id.art17fl15)).setText(Integer.toString(art17fl15));
        ((TextView) findViewById(R.id.art17fg15)).setText(Integer.toString(art17fg15));
        total = art17ml15 + art17mg15 + art17fl15 + art17fg15;
        ((TextView) findViewById(R.id.art17t)).setText(Integer.toString(total));

        ((TextView) findViewById(R.id.art18ml15)).setText(Integer.toString(art18ml15));
        ((TextView) findViewById(R.id.art18mg15)).setText(Integer.toString(art18mg15));
        ((TextView) findViewById(R.id.art18fl15)).setText(Integer.toString(art18fl15));
        ((TextView) findViewById(R.id.art18fg15)).setText(Integer.toString(art18fg15));
        total = art18ml15 + art18mg15 + art18fl15 + art18fg15;
        ((TextView) findViewById(R.id.art18t)).setText(Integer.toString(total));

        ((TextView) findViewById(R.id.art19ml15)).setText(Integer.toString(art19ml15));
        ((TextView) findViewById(R.id.art19mg15)).setText(Integer.toString(art19mg15));
        ((TextView) findViewById(R.id.art19fl15)).setText(Integer.toString(art19fl15));
        ((TextView) findViewById(R.id.art19fg15)).setText(Integer.toString(art19fg15));
        total = art19ml15 + art19mg15 + art19fl15 + art19fg15;
        ((TextView) findViewById(R.id.art19t)).setText(Integer.toString(total));

        ((TextView) findViewById(R.id.art20ml15)).setText(Integer.toString(art20ml15));
        ((TextView) findViewById(R.id.art20mg15)).setText(Integer.toString(art20mg15));
        ((TextView) findViewById(R.id.art20fl15)).setText(Integer.toString(art20fl15));
        ((TextView) findViewById(R.id.art20fg15)).setText(Integer.toString(art20fg15));
        total = art20ml15 + art20mg15 + art20fl15 + art20fg15;
        ((TextView) findViewById(R.id.art20t)).setText(Integer.toString(total));

        ((TextView) findViewById(R.id.art21ml15)).setText(Integer.toString(art21ml15));
        ((TextView) findViewById(R.id.art21mg15)).setText(Integer.toString(art21mg15));
        ((TextView) findViewById(R.id.art21fl15)).setText(Integer.toString(art21fl15));
        ((TextView) findViewById(R.id.art21fg15)).setText(Integer.toString(art21fg15));
        total = art21ml15 + art21mg15 + art21fl15 + art21fg15;
        ((TextView) findViewById(R.id.art21t)).setText(Integer.toString(total));

        ((TextView) findViewById(R.id.art22ml15)).setText(Integer.toString(art22ml15));
        ((TextView) findViewById(R.id.art22mg15)).setText(Integer.toString(art22mg15));
        ((TextView) findViewById(R.id.art22fl15)).setText(Integer.toString(art22fl15));
        ((TextView) findViewById(R.id.art22fg15)).setText(Integer.toString(art22fg15));
        total = art22ml15 + art22mg15 + art22fl15 + art22fg15;
        ((TextView) findViewById(R.id.art22t)).setText(Integer.toString(total));
    }

}
