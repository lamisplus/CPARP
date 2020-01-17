package org.fhi360.lamis.mobile.cparp.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aalozie on 7/9/2016.
 */
public class FacilityDAO {
    private Context context;
    private SQLiteOpenHelper databaseHelper;
    private List<String> states = new ArrayList<String>();;
    private List<String> lgas = new ArrayList<String>();;
    private List<String> facilities = new ArrayList<String>();
    private String facility;

    public FacilityDAO(Context context) {
        this.context = context;
    }

    public void save(int facilityId, String state, String lga, String name) {
        try {
            databaseHelper = DatabaseHelper.getInstance(context);
            SQLiteDatabase db = databaseHelper.getWritableDatabase();

            ContentValues contentValues = new ContentValues();
            contentValues.put("facility_id", facilityId);
            contentValues.put("state", state);
            contentValues.put("lga", lga);
            contentValues.put("name", name);
            db.insert("FACILITY", null, contentValues);
            db.close();
        }
        catch (SQLiteException exception) {
            exception.printStackTrace();
        }
        Log.v("Facility", "Saving....");
    }

    public void update(int id, int facilityId, String state, String lga, String name) {
        try {
            databaseHelper = DatabaseHelper.getInstance(context);
            SQLiteDatabase db = databaseHelper.getWritableDatabase();

            ContentValues contentValues = new ContentValues();
            contentValues.put("state", state);
            contentValues.put("lga", lga);
            contentValues.put("name", name);
            db.update("FACILITY", contentValues, "_id = ?", new String[] {Integer.toString(id)});
            db.close();
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
            db.delete("FACILITY", "_id = ?", new String[] {Integer.toString(id)});
            db.close();
        }
        catch (SQLiteException exception) {
            exception.printStackTrace();
        }
    }

    public int getId(int facilityId) {
        int id = 0;
        try {
            databaseHelper = DatabaseHelper.getInstance(context);
            SQLiteDatabase db = databaseHelper.getReadableDatabase();

            Cursor cursor = db.query("FACILITY",
                    new String[]{"_id"},
                    "facility_id = ?", new String[] {Integer.toString(facilityId)}, null, null, null);
            if (cursor.moveToFirst()) id = cursor.getInt(0);
            cursor.close();
            db.close();
        }
        catch (SQLiteException exception) {
            Toast toast = Toast.makeText(context, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
        return id;
    }

    public List<String> getStates() {
        try {
            databaseHelper = DatabaseHelper.getInstance(context);
            SQLiteDatabase db = databaseHelper.getReadableDatabase();

            //SELECT DISTINCT state FROM facility
            Cursor cursor = db.query(true, "FACILITY",
                    new String[]{"_id", "state"},
                    null, null, "state", null, null, null);

            int numberOfRecords = cursor.getCount();
            Log.v("FacilityDAO", Integer.toString(numberOfRecords));
            if (cursor.moveToFirst()) {
                do {
                    states.add(cursor.getString(1));
                } while (cursor.moveToNext());
            }
            // closing connection
            cursor.close();
            db.close();
        }
        catch (SQLiteException exception) {
            Toast toast = Toast.makeText(context, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
        return states;
    }

    public List<String> getLgas(String state) {
        try {
            databaseHelper = DatabaseHelper.getInstance(context);
            SQLiteDatabase db = databaseHelper.getReadableDatabase();

            //public Cursor query (boolean distinct, String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit)
            //SELECT DISTINCT lga FROM facility WHERE state = ?
            Cursor cursor = db.query(true, "FACILITY",
                    new String[]{"_id", "lga"},
                    "state = ?", new String[] {state}, "lga", null, null, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    lgas.add(cursor.getString(1));
                } while (cursor.moveToNext());
            }
            // closing connection
            cursor.close();
            db.close();
        }
        catch (SQLiteException exception) {
            Toast toast = Toast.makeText(context, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
        return lgas;
    }

    public List<String> getFacilities(String state, String lga) {
        try {
            databaseHelper = DatabaseHelper.getInstance(context);
            SQLiteDatabase db = databaseHelper.getReadableDatabase();

            //public Cursor query (boolean distinct, String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit)
            //SELECT DISTINCT name FROM facility WHERE state = ? AND lga = ?
            Cursor cursor = db.query(true, "FACILITY",
                    new String[]{"_id", "name"},
                    "state = ? AND lga = ?", new String[] {state, lga}, "name", null, null, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    facilities.add(cursor.getString(1));
                } while (cursor.moveToNext());
            }
            // closing connection
            cursor.close();
            db.close();
        }
        catch (SQLiteException exception) {
            Toast toast = Toast.makeText(context, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
        return facilities;
    }

    public String getFacility(int facilityId) {
        try {
            databaseHelper = DatabaseHelper.getInstance(context);
            SQLiteDatabase db = databaseHelper.getReadableDatabase();

            //public Cursor query (boolean distinct, String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit)
            //SELECT DISTINCT name FROM facility WHERE state = ? AND lga = ?
            Cursor cursor = db.query(true, "FACILITY",
                    new String[]{"_id", "name"},
                    "facility_id = ?", new String[] {String.valueOf(facilityId)}, "name", null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    facility = cursor.getString(1);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        }
        catch (SQLiteException exception) {
            Toast toast = Toast.makeText(context, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
        return facility;
    }

    public int getFacilityId(String state, String lga, String name) {
        int id = 0;
        try {
            databaseHelper = DatabaseHelper.getInstance(context);
            SQLiteDatabase db = databaseHelper.getReadableDatabase();

            //SELECT DISTINCT facility_id FROM facility WHERE state = ? AND lga = ? AND name = ?
            Cursor cursor = db.query(true, "FACILITY",
                    new String[]{"_id", "facility_id"},
                    "state = ? AND lga = ? AND name = ?", new String[] {state, lga, name}, null, null, null, null);

            if (cursor.moveToFirst()) id = cursor.getInt(1);
            cursor.close();
            db.close();
        }
        catch (SQLiteException exception) {
            Toast toast = Toast.makeText(context, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
        return id;
    }


}
