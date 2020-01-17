package org.fhi360.lamis.mobile.cparp.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.shashank.sony.fancytoastlib.FancyToast;

import org.fhi360.lamis.mobile.cparp.model.Encounter;
import org.fhi360.lamis.mobile.cparp.utility.DateUtil;
import org.fhi360.lamis.mobile.cparp.utility.StringUtil;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;


/**
 * Created by aalozie on 2/12/2017.
 */

public class EncounterDAO {
    private Context context;
    private SQLiteOpenHelper databaseHelper;

    public EncounterDAO(Context context) {
        this.context = context;
    }

    //Save encounter
    public void save(int facilityId, int patientId, Date dateVisit, String question1, String question2, String question3, String question4, String question5, String question6, String question7, String question8, String question9, String regimen1, String regimen2, String regimen3, String regimen4,
                     Integer duration1, Integer duration2, Integer duration3, Integer duration4, Integer prescribed1, Integer prescribed2, Integer prescribed3, Integer prescribed4, Integer dispensed1, Integer dispensed2, Integer dispensed3, Integer dispensed4, String notes, Date nextRefill, String regimentype) {
        try {
            databaseHelper = DatabaseHelper.getInstance(context);
            SQLiteDatabase db = databaseHelper.getWritableDatabase();

            ContentValues contentValues = new ContentValues();
            contentValues.put("facility_id", facilityId);
            contentValues.put("patient_id", patientId);
            if (dateVisit != null) {
                contentValues.put("date_visit", dateVisit.getTime()); //getTime() returns unix timestamp in milliseconds
            }
            contentValues.put("question1", question1);
            contentValues.put("question2", question2);
            contentValues.put("question3", question3);
            contentValues.put("question4", question4);
            contentValues.put("question5", question5);
            contentValues.put("question6", question6);
            contentValues.put("question7", question7);
            contentValues.put("question8", question8);
            contentValues.put("question9", question9);
            contentValues.put("regimen1", regimen1);
            contentValues.put("regimen2", regimen2);
            contentValues.put("regimen3", regimen3);
            contentValues.put("regimen4", regimen4);
            contentValues.put("duration1", duration1);
            contentValues.put("duration2", duration2);
            contentValues.put("duration3", duration3);
            contentValues.put("duration4", duration4);
            contentValues.put("prescribed1", prescribed1);
            contentValues.put("prescribed2", prescribed2);
            contentValues.put("prescribed3", prescribed3);
            contentValues.put("prescribed4", prescribed4);
            contentValues.put("dispensed1", dispensed1);
            contentValues.put("dispensed2", dispensed2);
            contentValues.put("dispensed3", dispensed3);
            contentValues.put("dispensed4", dispensed4);
            contentValues.put("notes", notes);
            contentValues.put("next_refill", nextRefill.getTime());
            contentValues.put("regimentype", regimentype);
            contentValues.put("time_stamp", System.currentTimeMillis());
            db.insert("ENCOUNTER", null, contentValues);
            db.close();

        } catch (Exception exception) {
            FancyToast.makeText(context, "Database unavailable ", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();

        }
    }

    public void update(int id, int facilityId, int patientId, Date dateVisit, String question1, String question2, String question3, String question4, String question5, String question6, String question7, String question8, String question9, String regimen1, String regimen2, String regimen3, String regimen4,
                       Integer duration1, Integer duration2, Integer duration3, Integer duration4, Integer prescribed1, Integer prescribed2, Integer prescribed3, Integer prescribed4, Integer dispensed1, Integer dispensed2, Integer dispensed3, Integer dispensed4, String notes, Date nextRefill, String regimentype) {

        try {

            databaseHelper = DatabaseHelper.getInstance(context);
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("facility_id", facilityId);
            contentValues.put("patient_id", patientId);
            if (dateVisit != null)
                contentValues.put("date_visit", dateVisit.getTime()); //getTime() returns unix timestamp in milliseconds
            contentValues.put("question1", question1);
            contentValues.put("question2", question2);
            contentValues.put("question3", question3);
            contentValues.put("question4", question4);
            contentValues.put("question5", question5);
            contentValues.put("question6", question6);
            contentValues.put("question7", question7);
            contentValues.put("question8", question8);
            contentValues.put("question9", question9);
            contentValues.put("regimen1", regimen1);
            contentValues.put("regimen2", regimen2);
            contentValues.put("regimen3", regimen3);
            contentValues.put("regimen4", regimen4);
            contentValues.put("duration1", duration1);
            contentValues.put("duration2", duration2);
            contentValues.put("duration3", duration3);
            contentValues.put("duration4", duration4);
            contentValues.put("prescribed1", prescribed1);
            contentValues.put("prescribed2", prescribed2);
            contentValues.put("prescribed3", prescribed3);
            contentValues.put("prescribed4", prescribed4);
            contentValues.put("dispensed1", dispensed1);
            contentValues.put("dispensed2", dispensed2);
            contentValues.put("dispensed3", dispensed3);
            contentValues.put("dispensed4", dispensed4);
            contentValues.put("notes", notes);
            contentValues.put("next_refill", nextRefill.getTime());
            contentValues.put("regimentype", regimentype);
            contentValues.put("time_stamp", System.currentTimeMillis());
            db.update("ENCOUNTER", contentValues, "_id = ?", new String[]{Integer.toString(id)});
            db.close();
            Log.v("Encounter", "Updating....");
        } catch (Exception exception) {
            //Toast toast = Toast.makeText(context, "Database unavailable ", Toast.LENGTH_SHORT);
            FancyToast.makeText(context, "Database unavailable ", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();

        }
    }

    public void delete(int id) {
        try {
            databaseHelper = DatabaseHelper.getInstance(context);
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            db.delete("ENCOUNTER", "_id = ?", new String[]{Integer.toString(id)});
            db.close();
        } catch (SQLiteException exception) {
            exception.printStackTrace();
        }
        Log.v("Encounter", "Deleting....");
    }

    public int getId(int facilityId, int patientId, Date dateVisit) {
        int id = 0;
        try {
            databaseHelper = DatabaseHelper.getInstance(context);
            SQLiteDatabase db = databaseHelper.getReadableDatabase();

            Cursor cursor = db.query("ENCOUNTER",
                    new String[]{"_id"},
                    "facility_id = ? AND patient_id = ? AND date_visit = ?", new String[]{Integer.toString(facilityId), Integer.toString(patientId), Long.toString(dateVisit.getTime())}, null, null, null);
            if (cursor.moveToFirst()) id = cursor.getInt(0);
            cursor.close();
            db.close();
        } catch (SQLiteException exception) {
            Toast toast = Toast.makeText(context, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
        return id;
    }

    //Retrieve all encounter records
    public ArrayList<Encounter> getEncounters() {
        ArrayList<Encounter> encounters = new ArrayList<Encounter>();
        try {
            databaseHelper = DatabaseHelper.getInstance(context);
            SQLiteDatabase db = databaseHelper.getReadableDatabase();

            Cursor cursor = db.query("ENCOUNTER", null,
                    null, null, null, null, "date_visit DESC");

            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(0);
                    int facilityId = cursor.getInt(1);
                    int patientId = cursor.getInt(2);
                    Date dateVisit = DateUtil.unixTimestampToDate(cursor.getLong(3), "dd/MM/yyyy");
                    String question1 = cursor.getString(4);
                    String question2 = cursor.getString(5);
                    String question3 = cursor.getString(6);
                    String question4 = cursor.getString(7);
                    String question5 = cursor.getString(8);
                    String question6 = cursor.getString(9);
                    String question7 = cursor.getString(10);
                    String question8 = cursor.getString(11);
                    String question9 = cursor.getString(12);
                    String regimen1 = cursor.getString(13);
                    String regimen2 = cursor.getString(14);
                    String regimen3 = cursor.getString(15);
                    String regimen4 = cursor.getString(16);
                    int duration1 = cursor.getInt(17);
                    int duration2 = cursor.getInt(18);
                    int duration3 = cursor.getInt(19);
                    int duration4 = cursor.getInt(20);
                    int prescribed1 = cursor.getInt(21);
                    int prescribed2 = cursor.getInt(22);
                    int prescribed3 = cursor.getInt(23);
                    int prescribed4 = cursor.getInt(24);
                    int dispensed1 = cursor.getInt(25);
                    int dispensed2 = cursor.getInt(26);
                    int dispensed3 = cursor.getInt(27);
                    int dispensed4 = cursor.getInt(28);
                    String notes = cursor.getString(29);
                    Date nextRefill = DateUtil.unixTimestampToDate(cursor.getLong(30), "dd/MM/yyyy");
                    String regimentype = cursor.getString(31);

                    Encounter encounter = new Encounter();
                    encounter.setId(id);
                    encounter.setFacilityId(facilityId);
                    encounter.setPatientId(patientId);
                    encounter.setDateVisit(dateVisit);
                    encounter.setQuestion1(question1);
                    encounter.setQuestion2(question2);
                    encounter.setQuestion3(question3);
                    encounter.setQuestion4(question4);
                    encounter.setQuestion5(question5);
                    encounter.setQuestion6(question6);
                    encounter.setQuestion7(question7);
                    encounter.setQuestion8(question8);
                    encounter.setQuestion9(question9);
                    encounter.setRegimen1(regimen1);
                    encounter.setRegimen2(regimen2);
                    encounter.setRegimen3(regimen3);
                    encounter.setRegimen4(regimen4);
                    encounter.setDuration1(duration1);
                    encounter.setDuration2(duration2);
                    encounter.setDuration3(duration3);
                    encounter.setDuration4(duration4);
                    encounter.setPrescribed1(prescribed1);
                    encounter.setPrescribed2(prescribed2);
                    encounter.setPrescribed3(prescribed3);
                    encounter.setPrescribed4(prescribed4);
                    encounter.setDispensed1(dispensed1);
                    encounter.setDispensed2(dispensed2);
                    encounter.setDispensed3(dispensed3);
                    encounter.setDispensed4(dispensed4);
                    encounter.setNotes(notes);
                    encounter.setNextRefill(nextRefill);
                    encounter.setRegimentype(regimentype);

                    encounters.add(encounter);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        } catch (SQLiteException exception) {
            Toast toast = Toast.makeText(context, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
        return encounters;
    }


    public ArrayList<Encounter> getEncounters(int facilityId, int patientId) {
        ArrayList<Encounter> encounters = new ArrayList<Encounter>();
        try {
            databaseHelper = DatabaseHelper.getInstance(context);
            SQLiteDatabase db = databaseHelper.getReadableDatabase();

            Cursor cursor = db.query("ENCOUNTER", null,
                    "facility_id = ? AND patient_id = ?", new String[]{Integer.toString(facilityId), Integer.toString(patientId)}, null, null, "date_visit DESC");

            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(0);
                    Date dateVisit = DateUtil.unixTimestampToDate(cursor.getLong(3), "dd/MM/yyyy");

                    String question1 = cursor.getString(4);
                    String question2 = cursor.getString(5);
                    String question3 = cursor.getString(6);
                    String question4 = cursor.getString(7);
                    String question5 = cursor.getString(8);
                    String question6 = cursor.getString(9);
                    String question7 = cursor.getString(10);
                    String question8 = cursor.getString(11);
                    String question9 = cursor.getString(12);
                    String regimen1 = cursor.getString(13);
                    String regimen2 = cursor.getString(14);
                    String regimen3 = cursor.getString(15);
                    String regimen4 = cursor.getString(16);
                    int duration1 = cursor.getInt(17);
                    int duration2 = cursor.getInt(18);
                    int duration3 = cursor.getInt(19);
                    int duration4 = cursor.getInt(20);
                    int prescribed1 = cursor.getInt(21);
                    int prescribed2 = cursor.getInt(22);
                    int prescribed3 = cursor.getInt(23);
                    int prescribed4 = cursor.getInt(24);
                    int dispensed1 = cursor.getInt(25);
                    int dispensed2 = cursor.getInt(26);
                    int dispensed3 = cursor.getInt(27);
                    int dispensed4 = cursor.getInt(28);
                    String notes = cursor.getString(29);
                    Date nextRefill = DateUtil.unixTimestampToDate(cursor.getLong(30), "dd/MM/yyyy");
                    String regimentype = cursor.getString(31);

                    Encounter encounter = new Encounter();
                    encounter.setId(id);
                    encounter.setFacilityId(facilityId);
                    encounter.setPatientId(patientId);
                    encounter.setDateVisit(dateVisit);
                    encounter.setQuestion1(question1);
                    encounter.setQuestion2(question2);
                    encounter.setQuestion3(question3);
                    encounter.setQuestion4(question4);
                    encounter.setQuestion5(question5);
                    encounter.setQuestion6(question6);
                    encounter.setQuestion7(question7);
                    encounter.setQuestion8(question8);
                    encounter.setQuestion9(question9);
                    encounter.setRegimen1(regimen1);
                    encounter.setRegimen2(regimen2);
                    encounter.setRegimen3(regimen3);
                    encounter.setRegimen4(regimen4);
                    encounter.setDuration1(duration1);
                    encounter.setDuration2(duration2);
                    encounter.setDuration3(duration3);
                    encounter.setDuration4(duration4);
                    encounter.setPrescribed1(prescribed1);
                    encounter.setPrescribed2(prescribed2);
                    encounter.setPrescribed3(prescribed3);
                    encounter.setPrescribed4(prescribed4);
                    encounter.setDispensed1(dispensed1);
                    encounter.setDispensed2(dispensed2);
                    encounter.setDispensed3(dispensed3);
                    encounter.setDispensed4(dispensed4);
                    encounter.setNotes(notes);
                    encounter.setNextRefill(nextRefill);
                    encounter.setRegimentype(regimentype);

                    encounters.add(encounter);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        } catch (SQLiteException exception) {
            Toast toast = Toast.makeText(context, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
        return encounters;
    }

    public Encounter getEncounter(int id) {
        Encounter encounter = new Encounter();
        try {
            databaseHelper = DatabaseHelper.getInstance(context);
            SQLiteDatabase db = databaseHelper.getReadableDatabase();

            Cursor cursor = db.query("ENCOUNTER", null,
                    "_id = ?", new String[]{Integer.toString(id)}, null, null, "date_visit DESC");

            if (cursor.moveToFirst()) {
                do {
                    int facilityId = cursor.getInt(1);
                    int patientId = cursor.getInt(2);
                    Date dateVisit = DateUtil.unixTimestampToDate(cursor.getLong(3), "dd/MM/yyyy");

                    String question1 = cursor.getString(4);
                    String question2 = cursor.getString(5);
                    String question3 = cursor.getString(6);
                    String question4 = cursor.getString(7);
                    String question5 = cursor.getString(8);
                    String question6 = cursor.getString(9);
                    String question7 = cursor.getString(10);
                    String question8 = cursor.getString(11);
                    String question9 = cursor.getString(12);
                    String regimen1 = cursor.getString(13);
                    String regimen2 = cursor.getString(14);
                    String regimen3 = cursor.getString(15);
                    String regimen4 = cursor.getString(16);
                    int duration1 = cursor.getInt(17);
                    int duration2 = cursor.getInt(18);
                    int duration3 = cursor.getInt(19);
                    int duration4 = cursor.getInt(20);
                    int prescribed1 = cursor.getInt(21);
                    int prescribed2 = cursor.getInt(22);
                    int prescribed3 = cursor.getInt(23);
                    int prescribed4 = cursor.getInt(24);
                    int dispensed1 = cursor.getInt(25);
                    int dispensed2 = cursor.getInt(26);
                    int dispensed3 = cursor.getInt(27);
                    int dispensed4 = cursor.getInt(28);
                    //int dispensed4 = cursor.getInt(cursor.getColumnIndex("facilityId"));
                    String notes = cursor.getString(29);
                    Date nextRefill = DateUtil.unixTimestampToDate(cursor.getLong(30), "dd/MM/yyyy");
                    String regimentype = cursor.getString(31);

                    encounter.setId(id);
                    encounter.setFacilityId(facilityId);
                    encounter.setPatientId(patientId);
                    encounter.setDateVisit(dateVisit);
                    encounter.setQuestion1(question1);
                    encounter.setQuestion2(question2);
                    encounter.setQuestion3(question3);
                    encounter.setQuestion4(question4);
                    encounter.setQuestion5(question5);
                    encounter.setQuestion6(question6);
                    encounter.setQuestion7(question7);
                    encounter.setQuestion8(question8);
                    encounter.setQuestion9(question9);
                    encounter.setRegimen1(regimen1);
                    encounter.setRegimen2(regimen2);
                    encounter.setRegimen3(regimen3);
                    encounter.setRegimen4(regimen4);
                    encounter.setDuration1(duration1);
                    encounter.setDuration2(duration2);
                    encounter.setDuration3(duration3);
                    encounter.setDuration4(duration4);
                    encounter.setPrescribed1(prescribed1);
                    encounter.setPrescribed2(prescribed2);
                    encounter.setPrescribed3(prescribed3);
                    encounter.setPrescribed4(prescribed4);
                    encounter.setDispensed1(dispensed1);
                    encounter.setDispensed2(dispensed2);
                    encounter.setDispensed3(dispensed3);
                    encounter.setDispensed4(dispensed4);
                    encounter.setNotes(notes);
                    encounter.setNextRefill(nextRefill);
                    encounter.setRegimentype(regimentype);

                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        } catch (SQLiteException exception) {
            Toast toast = Toast.makeText(context, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
        return encounter;
    }

    public Encounter getLastEncounter(int facilityId, int patientId) {
        Encounter encounter = new Encounter();
        try {
            databaseHelper = DatabaseHelper.getInstance(context);
            SQLiteDatabase db = databaseHelper.getReadableDatabase();

            String query = "SELECT * FROM encounter WHERE facility_id = " + facilityId + " AND patient_id = " + patientId + " ORDER BY date_visit DESC LIMIT 1";
            Log.v("EncounterActivity", "Values...." + facilityId + "   " + patientId);

            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(0);
                    Date dateVisit = DateUtil.unixTimestampToDate(cursor.getLong(3), "dd/MM/yyyy");
                    String question1 = cursor.getString(4);
                    String question2 = cursor.getString(5);
                    String question3 = cursor.getString(6);
                    String question4 = cursor.getString(7);
                    String question5 = cursor.getString(8);
                    String question6 = cursor.getString(9);
                    String question7 = cursor.getString(10);
                    String question8 = cursor.getString(11);
                    String question9 = cursor.getString(12);
                    String regimen1 = cursor.getString(13);
                    String regimen2 = cursor.getString(14);
                    String regimen3 = cursor.getString(15);
                    String regimen4 = cursor.getString(16);
                    int duration1 = cursor.getInt(17);
                    int duration2 = cursor.getInt(18);
                    int duration3 = cursor.getInt(19);
                    int duration4 = cursor.getInt(20);
                    int prescribed1 = cursor.getInt(21);
                    int prescribed2 = cursor.getInt(22);
                    int prescribed3 = cursor.getInt(23);
                    int prescribed4 = cursor.getInt(24);
                    int dispensed1 = cursor.getInt(25);
                    int dispensed2 = cursor.getInt(26);
                    int dispensed3 = cursor.getInt(27);
                    int dispensed4 = cursor.getInt(28);
                    String notes = cursor.getString(29);
                    Date nextRefill = DateUtil.unixTimestampToDate(cursor.getLong(30), "dd/MM/yyyy");
                    String regimentype = cursor.getString(31);

                    encounter.setId(id);
                    encounter.setFacilityId(facilityId);
                    encounter.setPatientId(patientId);
                    encounter.setDateVisit(dateVisit);
                    encounter.setQuestion1(question1);
                    encounter.setQuestion2(question2);
                    encounter.setQuestion3(question3);
                    encounter.setQuestion4(question4);
                    encounter.setQuestion5(question5);
                    encounter.setQuestion6(question6);
                    encounter.setQuestion7(question7);
                    encounter.setQuestion8(question8);
                    encounter.setQuestion9(question9);
                    encounter.setRegimen1(regimen1);
                    encounter.setRegimen2(regimen2);
                    encounter.setRegimen3(regimen3);
                    encounter.setRegimen4(regimen4);
                    encounter.setDuration1(duration1);
                    encounter.setDuration2(duration2);
                    encounter.setDuration3(duration3);
                    encounter.setDuration4(duration4);
                    encounter.setPrescribed1(prescribed1);
                    encounter.setPrescribed2(prescribed2);
                    encounter.setPrescribed3(prescribed3);
                    encounter.setPrescribed4(prescribed4);
                    encounter.setDispensed1(dispensed1);
                    encounter.setDispensed2(dispensed2);
                    encounter.setDispensed3(dispensed3);
                    encounter.setDispensed4(dispensed4);
                    encounter.setNotes(notes);
                    encounter.setNextRefill(nextRefill);
                    encounter.setRegimentype(regimentype);
                    Log.v("EncounterActivity", "Values...." + nextRefill + "   " + dateVisit);

                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        } catch (SQLiteException exception) {
            Toast toast = Toast.makeText(context, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
        return encounter;
    }

    public JSONArray getContent(long timeLastSync) {
        JSONArray jsonArray = new JSONArray();
        int communitypharmId = new AccountDAO(context).getAccountId();
        try {
            databaseHelper = DatabaseHelper.getInstance(context);
            SQLiteDatabase db = databaseHelper.getReadableDatabase();
            //String query = "SELECT * FROM encounter WHERE time_stamp >= " + timeLastSync;
            String query = "SELECT facility_id,patient_id,date_visit,question1," +
                    "question2,question3,question4,question5,question6," +
                    "question7,question8,question9,regimen1,regimen2,regimen3," +
                    "regimen4,duration1,duration2,duration3,duration4," +
                    "prescribed1,prescribed2,prescribed3,prescribed4," +
                    "dispensed1,dispensed2,dispensed3,dispensed4," +
                    "notes,regimentype FROM encounter";
            Cursor cursor = db.rawQuery(query, null);
            int colCount = cursor.getColumnCount();
            while (cursor.moveToNext()) {
                JSONObject rowObject = new JSONObject();
                for (int i = 0; i < colCount; i++) {
                    String columnName = cursor.getColumnName(i);
                    rowObject.put(columnName, unixTimestamp(columnName, cursor.getString(i)));
                }
                rowObject.put("communitypharm_id", Integer.toString(communitypharmId));
                jsonArray.put(rowObject);
                Log.v("CursorConverter", "JSON" + jsonArray);
            }
            cursor.close();
            db.close();
        } catch (Exception exception) {
            Toast toast = Toast.makeText(context, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
        return jsonArray;
    }


    //If the field is a date field convert from unix timestamp to date string
    private Object unixTimestamp(String columnName, String value) {
        String fields = "date_visit#next_refill#next_appointment";
        if (StringUtil.found(columnName, fields)) {
            return value == null ? "" : DateUtil.unixTimestampToDateString(Long.parseLong(value), "yyyy-MM-dd"); //dates are stored in database as unix timestamp in milliseconds to convert to seconds divide by 1000
        }
        return value == null ? "" : value;
    }

}
