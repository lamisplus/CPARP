package org.fhi360.lamis.mobile.cparp.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import org.fhi360.lamis.mobile.cparp.model.Appointment;
import org.fhi360.lamis.mobile.cparp.utility.DateUtil;
import org.fhi360.lamis.mobile.cparp.utility.StringUtil;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by aalozie on 5/6/2017.
 */

public class AppointmentDAO {
    private Context context;
    private SQLiteOpenHelper databaseHelper;

    public AppointmentDAO(Context context) {
        this.context = context;
    }

    //Save Tracker
    public void save(int facilityId, int patientId, Date dateTracked, String typeTracking, String trackingOutcome, Date dateLastVisit, Date dateNextVisit, Date dateAgreed) {
        // System.out.println("Appointment" + dateLastVisit + "...." + dateNextVisit + "....." + dateAgreed);
        try {
            databaseHelper = DatabaseHelper.getInstance(context);
            SQLiteDatabase db = databaseHelper.getWritableDatabase();

            ContentValues contentValues = new ContentValues();
            contentValues.put("facility_id", facilityId);
            contentValues.put("patient_id", patientId);
            if (dateTracked != null)
                contentValues.put("date_tracked", dateTracked.getTime()); //getTime() returns unix timestamp in milliseconds
            contentValues.put("type_tracking", typeTracking);
            contentValues.put("tracking_outcome", trackingOutcome);
            if (dateLastVisit != null)
                contentValues.put("date_last_visit", dateLastVisit.getTime());
            if (dateNextVisit != null)
                contentValues.put("date_next_visit", dateNextVisit.getTime());
            if (dateAgreed != null) contentValues.put("date_agreed", dateAgreed.getTime());
            contentValues.put("time_stamp", System.currentTimeMillis());
            db.insert("APPOINTMENT", null, contentValues);
            db.close();
        } catch (Exception exception) {
            Toast toast = Toast.makeText(context, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void update(int id, int facilityId, int patientId, Date dateTracked, String typeTracking, String trackingOutcome, Date dateLastVisit, Date dateNextVisit, Date dateAgreed) {
        try {
            databaseHelper = DatabaseHelper.getInstance(context);
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("facility_id", facilityId);
            contentValues.put("patient_id", patientId);
            if (dateTracked != null) contentValues.put("date_tracked", dateTracked.getTime());
            contentValues.put("type_tracking", typeTracking);
            contentValues.put("tracking_outcome", trackingOutcome);
            if (dateLastVisit != null)
                contentValues.put("date_last_visit", dateLastVisit.getTime());
            if (dateNextVisit != null)
                contentValues.put("date_next_visit", dateNextVisit.getTime());
            if (dateAgreed != null) contentValues.put("date_agreed", dateAgreed.getTime());
            contentValues.put("time_stamp", new Date().getTime());
            db.update("APPOINTMENT", contentValues, "_id = ?", new String[]{Integer.toString(id)});
            db.close();
        } catch (Exception exception) {
            Toast toast = Toast.makeText(context, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void delete(int id) {
        try {
            databaseHelper = DatabaseHelper.getInstance(context);
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            db.delete("APPOINTMENT", "_id = ?", new String[]{Integer.toString(id)});
            db.close();
        } catch (SQLiteException exception) {
            exception.printStackTrace();
        }
    }

    public int getId(int facilityId, int patientId, Date dateTracked) {
        int id = 0;
        try {
            databaseHelper = DatabaseHelper.getInstance(context);
            SQLiteDatabase db = databaseHelper.getReadableDatabase();

            Cursor cursor = db.query("APPOINTMENT",
                    new String[]{"_id"},
                    "facility_id = ? AND patient_id = ? AND date_tracked = ?", new String[]{Integer.toString(facilityId), Integer.toString(patientId), Long.toString(dateTracked.getTime())}, null, null, null);
            if (cursor.moveToFirst()) id = cursor.getInt(0);
            cursor.close();
            db.close();
        } catch (SQLiteException exception) {
            Toast toast = Toast.makeText(context, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
        return id;
    }

    public Appointment getAppointment(int id) {
        Appointment appointment = new Appointment();
        try {
            databaseHelper = DatabaseHelper.getInstance(context);
            SQLiteDatabase db = databaseHelper.getReadableDatabase();

            Cursor cursor = db.query("APPOINTMENT", null,
                    "_id = ?", new String[]{Integer.toString(id)}, null, null, "date_tracked DESC");

            if (cursor.moveToFirst()) {
                do {
                    id = cursor.getInt(0);
                    int facilityId = cursor.getInt(1);
                    int patientId = cursor.getInt(2);
                    Date dateTracked = DateUtil.unixTimestampToDate(cursor.getLong(3), "dd/MM/yyyy");
                    String typeTracking = cursor.getString(4);
                    String trackingOutcome = cursor.getString(5);
                    Date dateLastVisit = DateUtil.unixTimestampToDate(cursor.getLong(6), "dd/MM/yyyy");
                    Date dateNextVisit = DateUtil.unixTimestampToDate(cursor.getLong(7), "dd/MM/yyyy");
                    Date dateAgreed = DateUtil.unixTimestampToDate(cursor.getLong(8), "dd/MM/yyyy");
                    appointment.setId(id);
                    appointment.setFacilityId(facilityId);
                    appointment.setPatientId(patientId);
                    appointment.setDateTracked(dateTracked);
                    appointment.setTypeTracking(typeTracking);
                    appointment.setTrackingOutcome(trackingOutcome);
                    appointment.setDateLastVisit(dateLastVisit);
                    appointment.setDateNextVisit(dateNextVisit);
                    appointment.setDateAgreed(dateAgreed);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        } catch (SQLiteException exception) {
            Toast toast = Toast.makeText(context, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
        return appointment;
    }


    public JSONArray getAppointments(long timeLastSync) {
        JSONArray jsonArray = new JSONArray();
        try {
            databaseHelper = DatabaseHelper.getInstance(context);
            SQLiteDatabase db = databaseHelper.getReadableDatabase();
            String query = "SELECT * FROM appointment";
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(0);
                    int facilityId = cursor.getInt(1);
                    int patientId = cursor.getInt(2);
                    Date dateTracked = DateUtil.unixTimestampToDate(cursor.getLong(3), "dd/MM/yyyy");
                    String typeTracking = cursor.getString(4);
                    String trackingOutcome = cursor.getString(5);
                    Date dateLastVisit = DateUtil.unixTimestampToDate(cursor.getLong(6), "dd/MM/yyyy");
                    Date dateNextVisit = DateUtil.unixTimestampToDate(cursor.getLong(7), "dd/MM/yyyy");
                    Date dateAgreed = DateUtil.unixTimestampToDate(cursor.getLong(8), "dd/MM/yyyy");
                    Appointment appointment = new Appointment();
                    int communitypharmId = new AccountDAO(context).getAccountId();
                    appointment.setCommunitypharmId(communitypharmId);
                    appointment.setId(id);
                    appointment.setFacilityId(facilityId);
                    appointment.setPatientId(patientId);
                    appointment.setDateTracked(dateTracked);
                    appointment.setTypeTracking(typeTracking);
                    appointment.setTrackingOutcome(trackingOutcome);
                    appointment.setDateLastVisit(dateLastVisit);
                    appointment.setDateNextVisit(dateNextVisit);
                    appointment.setDateAgreed(dateAgreed);
                    jsonArray.put(appointment);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        } catch (SQLiteException exception) {
            Toast toast = Toast.makeText(context, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
        return jsonArray;
    }


    public ArrayList<Appointment> getAppointments(int facilityId, int patientId) {
        ArrayList<Appointment> appointments = new ArrayList<Appointment>();
        try {
            databaseHelper = DatabaseHelper.getInstance(context);
            SQLiteDatabase db = databaseHelper.getReadableDatabase();

            Cursor cursor = db.query("APPOINTMENT", null,
                    "facility_id = ? AND patient_id = ?", new String[]{Integer.toString(facilityId), Integer.toString(patientId)}, null, null, "date_tracked DESC");
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(0);
                    Date dateTracked = DateUtil.unixTimestampToDate(cursor.getLong(3), "dd/MM/yyyy");
                    String typeTracking = cursor.getString(4);
                    String trackingOutcome = cursor.getString(5);
                    Date dateLastVisit = DateUtil.unixTimestampToDate(cursor.getLong(6), "dd/MM/yyyy");
                    Date dateNextVisit = DateUtil.unixTimestampToDate(cursor.getLong(7), "dd/MM/yyyy");
                    Date dateAgreed = DateUtil.unixTimestampToDate(cursor.getLong(8), "dd/MM/yyyy");

                    Appointment appointment = new Appointment();
                    appointment.setId(id);
                    appointment.setFacilityId(facilityId);
                    appointment.setPatientId(patientId);
                    appointment.setDateTracked(dateTracked);
                    appointment.setTypeTracking(typeTracking);
                    appointment.setTrackingOutcome(trackingOutcome);
                    appointment.setDateLastVisit(dateLastVisit);
                    appointment.setDateNextVisit(dateNextVisit);
                    appointment.setDateAgreed(dateAgreed);

                    appointments.add(appointment);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        } catch (SQLiteException exception) {
            Toast toast = Toast.makeText(context, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
        return appointments;
    }

    public JSONArray getContent(long timeLastSync) {
        JSONArray jsonArray = new JSONArray();
        int communitypharmId = new AccountDAO(context).getAccountId();
        try {
            databaseHelper = DatabaseHelper.getInstance(context);
            SQLiteDatabase db = databaseHelper.getReadableDatabase();
//"_id":"1","facility_id":"612","patient_id":"9222","date_tracked":"1569106800000","type_tracking":"Home Visit","tracking_outcome":"Self Transfer","time_stamp":"1569177506302","communitypharm_id":"117"
            //   String query = "SELECT * FROM appointment WHERE time_stamp >= " + timeLastSync;
            String query = "SELECT facility_id,patient_id,date_tracked,type_tracking,tracking_outcome FROM appointment";
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
            exception.printStackTrace();
            Toast toast = Toast.makeText(context, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
        return jsonArray;
    }


    //If the field is a date field convert from unix timestamp to date string
    private Object unixTimestamp(String columnName, String value) {
        String fields = "date_tracked#date_last_visit#date_next_visit#date_agreed";
        if (StringUtil.found(columnName, fields)) {
            return value == null ? "" : DateUtil.unixTimestampToDateString(Long.parseLong(value), "yyyy-MM-dd"); //dates are stored in database as unix timestamp in milliseconds to convert to seconds divide by 1000
        }
        return value == null ? "" : value;
    }


}
