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
 * Created by aalozie on 7/10/2016.
 */
public class RegimenDAO {
    private Context context;
    private SQLiteOpenHelper databaseHelper;
    private List<String> regimens = new ArrayList<String>();
    private List<String> regimentypes = new ArrayList<String>();

    public RegimenDAO(Context context) {
        this.context = context;
    }

    public void save(int regimenId, String regimen, int regimentypeId, String regimentype) {
        try {
            databaseHelper = DatabaseHelper.getInstance(context);
            SQLiteDatabase db = databaseHelper.getReadableDatabase();

            ContentValues contentValues = new ContentValues();
            contentValues.put("regimen_id", regimenId);
            contentValues.put("regimen", regimen);
            contentValues.put("regimentype_id", regimentypeId);
            contentValues.put("regimentype", regimentype);
            db.insert("REGIMEN", null, contentValues);
            db.close();
        }
        catch (SQLiteException exception) {
            exception.printStackTrace();
        }
        Log.v("Regimen", "Saving....");
    }

    public void update(int id, int regimenId, String regimen, int regimentypeId, String regimentype) {
        try {
            databaseHelper = DatabaseHelper.getInstance(context);
            SQLiteDatabase db = databaseHelper.getWritableDatabase();

            ContentValues contentValues = new ContentValues();
            contentValues.put("regimen", regimen);
            contentValues.put("regimentype_id", regimentypeId);
            contentValues.put("regimentype", regimentype);
            db.update("REGIMEN", contentValues, "_id = ?", new String[] {Integer.toString(id)});
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
            db.delete("REGIMEN", "_id = ?", new String[] {Integer.toString(id)});
            db.close();
        }
        catch (SQLiteException exception) {
            exception.printStackTrace();
        }
    }

    public int getId(int regimenId) {
        int id = 0;
        try {
            databaseHelper = DatabaseHelper.getInstance(context);
            SQLiteDatabase db = databaseHelper.getReadableDatabase();

            Cursor cursor = db.query("REGIMEN",
                    new String[]{"_id"},
                    "regimen_id = ?", new String[] {Integer.toString(regimenId)}, null, null, null);
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

    public int getRegimentypeId(String regimentype) {
        int id = 0;
        try {
            databaseHelper = DatabaseHelper.getInstance(context);
            SQLiteDatabase db = databaseHelper.getReadableDatabase();

            Cursor cursor = db.query("REGIMEN",
                    new String[]{"regimentype_id"},
                    "regimentype = ?", new String[] {regimentype}, null, null, null);
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

    public List<String> getRegimentypes() {
        try {
            databaseHelper = DatabaseHelper.getInstance(context);
            SQLiteDatabase db = databaseHelper.getReadableDatabase();

            Cursor cursor = db.query(true, "REGIMEN",
                    new String[]{"_id", "regimentype_id", "regimentype"},
                    null, null, null, null, null, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    int regimentypeId = cursor.getInt(1);
                    String regimentype = cursor.getString(2);
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
        return regimentypes;
    }

    public List<String> getRegimens(int regimentypeId) {
        regimens.add("");
        try {
            databaseHelper = DatabaseHelper.getInstance(context);
            SQLiteDatabase db = databaseHelper.getReadableDatabase();

            Cursor cursor = db.query(true, "REGIMEN",
                    new String[]{"_id", "regimen"},
                    "regimentype_id = ? ", new String[] {Integer.toString(regimentypeId)}, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    regimens.add(cursor.getString(1));
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        }
        catch (SQLiteException exception) {
            Toast toast = Toast.makeText(context, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
        return regimens;
    }

    public List<String> getARV() {
        regimens.add("");
        try {
            databaseHelper = DatabaseHelper.getInstance(context);
            SQLiteDatabase db = databaseHelper.getReadableDatabase();

            Cursor cursor = db.query(true, "REGIMEN",
                    new String[]{"_id", "regimen"},
                    "(regimentype_id >= ? AND regimentype_id <= ?) OR regimentype_id = ?", new String[] {"1", "4", "14"}, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    regimens.add(cursor.getString(1));
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        }
        catch (SQLiteException exception) {
            Toast toast = Toast.makeText(context, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
        return regimens;
    }

    public List<String> getOtherMedicines() {
        regimens.add("");
        try {
            databaseHelper = DatabaseHelper.getInstance(context);
            SQLiteDatabase db = databaseHelper.getReadableDatabase();

            Cursor cursor = db.query(true, "REGIMEN",
                    new String[]{"_id", "regimen"},
                    "regimentype_id > ?", new String[] {"8"}, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    regimens.add(cursor.getString(1));
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        }
        catch (SQLiteException exception) {
            Toast toast = Toast.makeText(context, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
        return regimens;
    }

}
