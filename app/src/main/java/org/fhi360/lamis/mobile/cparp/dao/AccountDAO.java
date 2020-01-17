package org.fhi360.lamis.mobile.cparp.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import org.fhi360.lamis.mobile.cparp.model.Account;

import java.util.Date;

/**
 * Created by aalozie on 2/13/2017.
 */

public class AccountDAO {
    private Context context;
    private SQLiteOpenHelper databaseHelper;
    public AccountDAO(Context context) {
        this.context = context;
    }
    public void save(int communitypharmId, String pharmacy, String address, String phone, String email, String pin) {
        try {
            databaseHelper = DatabaseHelper.getInstance(context);
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("communitypharm_id", communitypharmId);
            contentValues.put("pharmacy", pharmacy);
            contentValues.put("address", address);
            contentValues.put("phone", phone);
            contentValues.put("email", email);
            contentValues.put("pin", pin);
            contentValues.put("time_last_sync", new Date().getTime());
            db.insert("ACCOUNT", null, contentValues);
            db.close();
        }
        catch (SQLiteException exception) {
            exception.printStackTrace();
        }
    }

    public void update(int id, int communitypharmId, String pharmacy, String address, String phone, String email, String pin) {
        try {
            databaseHelper = DatabaseHelper.getInstance(context);
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("communitypharm_id", communitypharmId);
            contentValues.put("pharmacy", pharmacy);
            contentValues.put("address", address);
            contentValues.put("phone", phone);
            contentValues.put("email", email);
            contentValues.put("pin", pin);
            db.update("ACCOUNT", contentValues, "_id = ?", new String[] {Integer.toString(id)});
            db.close();
        }
        catch (SQLiteException exception) {
            exception.printStackTrace();
        }
    }

    public void delete(int id) {
        try {
            databaseHelper = DatabaseHelper.getInstance(context);
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            db.delete("ACCOUNT", "_id = ?", new String[] {Integer.toString(id)});
            db.close();
        }
        catch (SQLiteException exception) {
            exception.printStackTrace();
        }
        Log.v("UsernameAndPassword", "Deleting....");
    }

    public int getId(int communitypharmId) {
        int id = 0;
        try {
            databaseHelper = DatabaseHelper.getInstance(context);
            SQLiteDatabase db = databaseHelper.getReadableDatabase();

            Cursor cursor = db.query("ACCOUNT",
                    new String[]{"_id"},
                    "communitypharm_id = ?", new String[] {Integer.toString(communitypharmId)}, null, null, null);
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

    public int getAccountId() {
        int id = 0;
        try {
            databaseHelper = DatabaseHelper.getInstance(context);
            SQLiteDatabase db = databaseHelper.getReadableDatabase();

            Cursor cursor = db.query("ACCOUNT",
                    new String[]{"communitypharm_id"},
                    null, null, null, null, null);
            if (cursor.moveToFirst())  id = cursor.getInt(0);
            cursor.close();
            db.close();
        }
        catch (SQLiteException exception) {
            Toast toast = Toast.makeText(context, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
        return id;
    }

    public String getAccountName() {
        String pharmacy = "";
        try {
            databaseHelper = DatabaseHelper.getInstance(context);
            SQLiteDatabase db = databaseHelper.getReadableDatabase();

            Cursor cursor = db.query("ACCOUNT",
                    new String[]{"pharmacy"},
                    null, null, null, null, null);
            if (cursor.moveToFirst())  pharmacy = cursor.getString(0);
            cursor.close();
            db.close();
        }
        catch (SQLiteException exception) {
            Toast toast = Toast.makeText(context, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
        return pharmacy;
    }

    public Account getAccount() {
        Account account = new Account();
        try {
            databaseHelper = DatabaseHelper.getInstance(context);
            SQLiteDatabase db = databaseHelper.getReadableDatabase();

            Cursor cursor = db.query("ACCOUNT",
                    null, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                int id = cursor.getInt(0);
                int communitypharmId = cursor.getInt(1);
                String pharmacy = cursor.getString(2);
                String address = cursor.getString(3);
                String phone = cursor.getString(4);
                String email = cursor.getString(5);
                String pin = cursor.getString(6);

                account.setId(id);
                account.setCommunitypharmId(communitypharmId);
                account.setPharmacy(pharmacy);
                account.setAddress(address);
                account.setPhone(phone);
                account.setEmail(email);
                account.setPin(pin);
            }
            cursor.close();
            db.close();
        }
        catch (SQLiteException exception) {
            Toast toast = Toast.makeText(context, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
        return account;
    }

    public void updateLastSync() {
        try {
            databaseHelper = DatabaseHelper.getInstance(context);
            SQLiteDatabase db = databaseHelper.getWritableDatabase();

            ContentValues contentValues = new ContentValues();
            contentValues.put("time_last_sync", new Date().getTime());
            db.update("ACCOUNT", contentValues, null, null);
            db.close();
        }
        catch (SQLiteException exception) {
            exception.printStackTrace();
        }
    }

    public long getTimeLastSync() {
        long timeLastSync = 0;
        try {
            databaseHelper = DatabaseHelper.getInstance(context);
            SQLiteDatabase db = databaseHelper.getReadableDatabase();

            Cursor cursor = db.query("ACCOUNT",
                    new String[]{"time_last_sync"},
                    null, null, null, null, null);
            if (cursor.moveToFirst()) timeLastSync = cursor.getLong(0);
            cursor.close();
            db.close();
        }
        catch (SQLiteException exception) {
            Toast toast = Toast.makeText(context, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
        return timeLastSync;
    }
}
