package org.fhi360.lamis.mobile.cparp.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import org.fhi360.lamis.mobile.cparp.model.Drugtherapy;
import org.fhi360.lamis.mobile.cparp.utility.DateUtil;
import org.fhi360.lamis.mobile.cparp.utility.StringUtil;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by aalozie on 6/4/2017.
 */

public class DrugtherapyDAO {
    private Context context;
    private SQLiteOpenHelper databaseHelper;

    public DrugtherapyDAO(Context context) {
        this.context = context;
    }

    public void save(int facilityId, int patientId, Date dateVisit, String ois, String therapyProblemScreened, String adherenceIssues, String medicationError,
                     String adrs, String severity, String icsrForm, String adrReferred) {
        try {
            databaseHelper = DatabaseHelper.getInstance(context);
            SQLiteDatabase db = databaseHelper.getWritableDatabase();

            ContentValues contentValues = new ContentValues();
            contentValues.put("facility_id", facilityId);
            contentValues.put("patient_id", patientId);
            if(dateVisit != null) contentValues.put("date_visit", dateVisit.getTime()); //getTime() returns unix timestamp in milliseconds
            contentValues.put("ois", ois);
            contentValues.put("therapy_problem_screened", therapyProblemScreened);
            contentValues.put("adherence_issues", adherenceIssues);
            contentValues.put("medication_error", medicationError);
            contentValues.put("adrs", adrs);
            contentValues.put("severity", severity);
            contentValues.put("icsr_form", icsrForm);
            contentValues.put("adr_referred", adrReferred);
            contentValues.put("time_stamp", System.currentTimeMillis());

            db.insert("DRUGTHERAPY", null, contentValues);
            db.close();
            Log.v("DrugTherapyDAO", "Saving....");
        }
        catch (Exception exception) {
            Toast toast = Toast.makeText(context, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }

    }

    public void update(int id, int facilityId, int patientId, Date dateVisit, String ois, String therapyProblemScreened, String adherenceIssues, String medicationError,
                       String adrs, String severity, String icsrForm, String adrReferred) {
        try {
            databaseHelper = DatabaseHelper.getInstance(context);
            SQLiteDatabase db = databaseHelper.getWritableDatabase();

            ContentValues contentValues = new ContentValues();
            contentValues.put("facility_id", facilityId);
            contentValues.put("patient_id", patientId);
            if(dateVisit != null) contentValues.put("date_visit", dateVisit.getTime()); //getTime() returns unix timestamp in milliseconds
            contentValues.put("ois", ois);
            contentValues.put("therapy_problem_screened", therapyProblemScreened);
            contentValues.put("adherence_issues", adherenceIssues);
            contentValues.put("medication_error", medicationError);
            contentValues.put("adrs", adrs);
            contentValues.put("severity", severity);
            contentValues.put("icsr_form", icsrForm);
            contentValues.put("adr_referred", adrReferred);
            contentValues.put("time_stamp", System.currentTimeMillis());

            db.update("DRUGTHERAPY", contentValues, "_id = ?", new String[] {Integer.toString(id)});
            db.close();
            Log.v("DrugtherapyDAO", "Updating....");
        }
        catch (Exception exception) {
            Toast toast = Toast.makeText(context, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }

    }

    public void delete(int id) {
        try {
            databaseHelper = DatabaseHelper.getInstance(context);
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            db.delete("DRUGTHERAPY", "_id = ?", new String[] {Integer.toString(id)});
            db.close();
        }
        catch (SQLiteException exception) {
            exception.printStackTrace();
        }
        Log.v("Drugtherapy", "Deleting....");
    }

    public int getId(int facilityId, int patientId, Date dateVisit) {
        int id = 0;
        try {
            databaseHelper = DatabaseHelper.getInstance(context);
            SQLiteDatabase db = databaseHelper.getReadableDatabase();

            Cursor cursor = db.query("DRUGTHERAPY",
                    new String[]{"_id"},
                    "facility_id = ? AND patient_id = ? AND date_visit = ?", new String[] {Integer.toString(facilityId), Integer.toString(patientId), Long.toString(dateVisit.getTime())}, null, null, null);
            if (cursor.moveToFirst()) id = cursor.getInt(0);
            cursor.close();
            db.close();
        }
        catch (SQLiteException exception) {
            Toast toast = Toast.makeText(context, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
        return 0;
    }

    //Retrieve all drugtherapy records
    public ArrayList<Drugtherapy> getDrugtherapies() {
        ArrayList<Drugtherapy> drugtherapies = new ArrayList<Drugtherapy>();
        try {
            databaseHelper = DatabaseHelper.getInstance(context);
            SQLiteDatabase db = databaseHelper.getReadableDatabase();

            Cursor cursor = db.query("DRUGTHERAPY", null,
                    null, null, null, null, "date_visit DESC");

            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(0);
                    int facilityId = cursor.getInt(1);
                    int patientId = cursor.getInt(2);
                    Date dateVisit = DateUtil.unixTimestampToDate(cursor.getLong(3), "dd/MM/yyyy");
                    String ois = cursor.getString(4);
                    String therapyProblemScreened = cursor.getString(5);
                    String adherenceIssues = cursor.getString(6);
                    String medicationError = cursor.getString(7);
                    String adrs = cursor.getString(8);
                    String severity = cursor.getString(9);
                    String icsrForm = cursor.getString(10);
                    String adrReferred = cursor.getString(11);

                    Drugtherapy drugtherapy = new Drugtherapy();
                    drugtherapy.setId(id);
                    drugtherapy.setFacilityId(facilityId);
                    drugtherapy.setPatientId(patientId);
                    drugtherapy.setDateVisit(dateVisit);
                    drugtherapy.setOis(ois);
                    drugtherapy.setTherapyProblemScreened(therapyProblemScreened);
                    drugtherapy.setAdherenceIssues(adherenceIssues);
                    drugtherapy.setMedicationError(medicationError);
                    drugtherapy.setAdrs(adrs);
                    drugtherapy.setSeverity(severity);
                    drugtherapy.setIcsrForm(icsrForm);
                    drugtherapy.setAdrReferred(adrReferred);

                    drugtherapies.add(drugtherapy);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        }
        catch (SQLiteException exception) {
            Toast toast = Toast.makeText(context, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
        return drugtherapies;
    }

    //Retrieve all drugtherapy records
    public ArrayList<Drugtherapy> getDrugtherapies(int facilityId, int patientId) {
        ArrayList<Drugtherapy> drugtherapies = new ArrayList<Drugtherapy>();
        try {
            databaseHelper = DatabaseHelper.getInstance(context);
            SQLiteDatabase db = databaseHelper.getReadableDatabase();

            Cursor cursor = db.query("DRUGTHERAPY", null,
                    "facility_id = ? AND patient_id = ?", new String[] {Integer.toString(facilityId), Integer.toString(patientId)}, null, null, "date_visit DESC");

            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(0);
                    Date dateVisit = DateUtil.unixTimestampToDate(cursor.getLong(3), "dd/MM/yyyy");
                    String ois = cursor.getString(4);
                    String therapyProblemScreened = cursor.getString(5);
                    String adherenceIssues = cursor.getString(6);
                    String medicationError = cursor.getString(7);
                    String adrs = cursor.getString(8);
                    String severity = cursor.getString(9);
                    String icsrForm = cursor.getString(10);
                    String adrReferred = cursor.getString(11);

                    Drugtherapy drugtherapy = new Drugtherapy();
                    drugtherapy.setId(id);
                    drugtherapy.setFacilityId(facilityId);
                    drugtherapy.setPatientId(patientId);
                    drugtherapy.setDateVisit(dateVisit);
                    drugtherapy.setOis(ois);
                    drugtherapy.setTherapyProblemScreened(therapyProblemScreened);
                    drugtherapy.setAdherenceIssues(adherenceIssues);
                    drugtherapy.setMedicationError(medicationError);
                    drugtherapy.setAdrs(adrs);
                    drugtherapy.setSeverity(severity);
                    drugtherapy.setIcsrForm(icsrForm);
                    drugtherapy.setAdrReferred(adrReferred);

                    drugtherapies.add(drugtherapy);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        }
        catch (SQLiteException exception) {
            Toast toast = Toast.makeText(context, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
        return drugtherapies;
    }

    public Drugtherapy getDrugtherapy(int id) {
        Drugtherapy drugtherapy = new Drugtherapy();
        try {
            databaseHelper = DatabaseHelper.getInstance(context);
            SQLiteDatabase db = databaseHelper.getReadableDatabase();

            Cursor cursor = db.query("DRUGTHERAPY", null,
                    "_id = ?", new String[] {Integer.toString(id)}, null, null, "date_visit DESC");

            if (cursor.moveToFirst()) {
                do {
                    id = cursor.getInt(0);
                    int facilityId = cursor.getInt(1);
                    int patientId = cursor.getInt(2);
                    Date dateVisit = DateUtil.unixTimestampToDate(cursor.getLong(3), "dd/MM/yyyy");
                    String ois = cursor.getString(4);
                    String therapyProblemScreened = cursor.getString(5);
                    String adherenceIssues = cursor.getString(6);
                    String medicationError = cursor.getString(7);
                    String adrs = cursor.getString(8);
                    String severity = cursor.getString(9);
                    String icsrForm = cursor.getString(10);
                    String adrReferred = cursor.getString(11);

                    drugtherapy.setId(id);
                    drugtherapy.setFacilityId(facilityId);
                    drugtherapy.setPatientId(patientId);
                    drugtherapy.setDateVisit(dateVisit);
                    drugtherapy.setOis(ois);
                    drugtherapy.setTherapyProblemScreened(therapyProblemScreened);
                    drugtherapy.setAdherenceIssues(adherenceIssues);
                    drugtherapy.setMedicationError(medicationError);
                    drugtherapy.setAdrs(adrs);
                    drugtherapy.setSeverity(severity);
                    drugtherapy.setIcsrForm(icsrForm);
                    drugtherapy.setAdrReferred(adrReferred);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        }
        catch (SQLiteException exception) {
            Toast toast = Toast.makeText(context, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
        return drugtherapy;
    }

    public JSONArray getContent(long timeLastSync) {
        JSONArray jsonArray = new JSONArray();
        int communitypharmId = new AccountDAO(context).getAccountId();
        try {
            databaseHelper = DatabaseHelper.getInstance(context);
            SQLiteDatabase db = databaseHelper.getReadableDatabase();
           //String query = "SELECT * FROM drugtherapy WHERE time_stamp >= " + timeLastSync;
            String query = "SELECT facility_id,patient_id,date_visit,therapy_problem_screened,adherence_issues,medication_error,adrs,severity,icsr_form,adr_referred FROM drugtherapy";
            Cursor cursor = db.rawQuery(query, null);
            int colCount = cursor.getColumnCount();
            while (cursor.moveToNext()) {
                JSONObject rowObject = new JSONObject();
                for(int i = 0; i < colCount; i++) {
                    String columnName = cursor.getColumnName(i);
                    rowObject.put(columnName, unixTimestamp(columnName, cursor.getString(i)));
                }
                rowObject.put("communitypharm_id", Integer.toString(communitypharmId));
                jsonArray.put(rowObject);
                Log.v("CursorConverter", "JSON"+jsonArray);
            }
            cursor.close();
            db.close();
        }
        catch (Exception exception) {
            Toast toast = Toast.makeText(context, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
        return jsonArray;
    }

    //If the field is a date field convert from unix timestamp to date string
    private Object unixTimestamp(String columnName, String value) {
        String fields = "date_visit";
        if(StringUtil.found(columnName, fields)) {
            return value == null? "" : DateUtil.unixTimestampToDateString(Long.parseLong(value), "yyyy-MM-dd"); //dates are stored in database as unix timestamp in milliseconds to convert to seconds divide by 1000
        }
        return value == null? "" : value;
    }

}
