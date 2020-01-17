package org.fhi360.lamis.mobile.cparp.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import org.fhi360.lamis.mobile.cparp.model.Htc;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by aalozie on 6/28/2017.
 */

public class HtcDAO {
    private Context context;
    private SQLiteOpenHelper databaseHelper;

    public HtcDAO(Context context) {
        this.context = context;
    }

    public void save(int numTested, int numPositive, int numReferred, int numOnsiteVisit, int month, int year) {
        try {
            databaseHelper = DatabaseHelper.getInstance(context);
            SQLiteDatabase db = databaseHelper.getWritableDatabase();

            ContentValues contentValues = new ContentValues();
            contentValues.put("num_tested", numTested);
            contentValues.put("num_positive", numPositive);
            contentValues.put("num_referred", numReferred);
            contentValues.put("num_onsite_visit", numOnsiteVisit);
            contentValues.put("month", month);
            contentValues.put("year", year);
            contentValues.put("time_stamp", new Date().getTime());
            db.insert("HTC", null, contentValues);
            db.close();
        }
        catch (Exception exception) {
            Toast toast = Toast.makeText(context, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void update(int id, int numTested, int numPositive, int numReferred, int numOnsiteVisit, int month, int year) {
        try {
            databaseHelper = DatabaseHelper.getInstance(context);
            SQLiteDatabase db = databaseHelper.getWritableDatabase();

            ContentValues contentValues = new ContentValues();
            contentValues.put("num_tested", numTested);
            contentValues.put("num_positive", numPositive);
            contentValues.put("num_referred", numReferred);
            contentValues.put("num_onsite_visit", numOnsiteVisit);
            contentValues.put("month", month);
            contentValues.put("year", year);
            contentValues.put("time_stamp", new Date().getTime());
            db.update("HTC", contentValues, "_id = ?", new String[] {Integer.toString(id)});
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
            db.delete("HTC", "_id = ?", new String[] {Integer.toString(id)});
            db.close();
        }
        catch (SQLiteException exception) {
            exception.printStackTrace();
        }
        Log.v("Htc", "Deleting....");
    }

    public int getId(int month, int year) {
        int id = 0;
        try {
            databaseHelper = DatabaseHelper.getInstance(context);
            SQLiteDatabase db = databaseHelper.getReadableDatabase();

            Cursor cursor = db.query("HTC",
                    new String[]{"_id"},
                    "month = ? AND year = ?", new String[] {Integer.toString(month), Integer.toString(year)}, null, null, null);
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

    public Htc getHtc(int id) {
        Htc htc = new Htc();
        try {
            databaseHelper = DatabaseHelper.getInstance(context);
            SQLiteDatabase db = databaseHelper.getReadableDatabase();

            Cursor cursor = db.query("HTC", null,
                    "_id = ?", new String[] {Integer.toString(id)}, null, null, null);

            if (cursor.moveToFirst()) {
                do {
                    id = cursor.getInt(0);
                    int month = cursor.getInt(1);
                    int year = cursor.getInt(2);
                    int numTested = cursor.getInt(3);
                    int numPositive = cursor.getInt(4);
                    int numReferred = cursor.getInt(5);
                    int numOnsiteVisit = cursor.getInt(6);

                    htc.setId(id);
                    htc.setNumTested(numTested);
                    htc.setNumPositive(numPositive);
                    htc.setNumReferred(numReferred);
                    htc.setNumOnsiteVisit(numOnsiteVisit);
                    htc.setMonth(month);
                    htc.setYear(year);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        }
        catch (SQLiteException exception) {
            Toast toast = Toast.makeText(context, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
        return htc;
    }

    public JSONArray getContent(long timeLastSync) {
        JSONArray jsonArray = new JSONArray();
        int communitypharmId = new AccountDAO(context).getAccountId();
        try {
            databaseHelper = DatabaseHelper.getInstance(context);
            SQLiteDatabase db = databaseHelper.getReadableDatabase();

            //String query = "SELECT * FROM htc WHERE time_stamp >= " + timeLastSync;
            String query = "SELECT month,year,num_tested,num_positive,num_referred,num_onsite_visit FROM htc";
            Cursor cursor = db.rawQuery(query, null);
            int colCount = cursor.getColumnCount();
            while (cursor.moveToNext()) {
                JSONObject rowObject = new JSONObject();
                for(int i = 0; i < colCount; i++) {
                    String columnName = cursor.getColumnName(i);
                    String value = cursor.getString(i) == null? "" : cursor.getString(i);
                    rowObject.put(columnName, value);
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


}
