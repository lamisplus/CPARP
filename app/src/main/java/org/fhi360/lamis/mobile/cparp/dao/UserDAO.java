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
import org.fhi360.lamis.mobile.cparp.model.UsernameAndPassword;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aalozie on 11/1/2017.
 */

public class UserDAO {

    private Context context;
    private SQLiteOpenHelper databaseHelper;

    public UserDAO(Context context) {
        this.context = context;
    }

    public void save(String username, String password) {
        try {
            databaseHelper = DatabaseHelper.getInstance(context);
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("username", username);
            contentValues.put("password", password);
            db.insert("USER", null, contentValues);
            db.close();
        } catch (SQLiteException exception) {
            exception.printStackTrace();
        }
        Log.v("User", "Saving....");
    }

    public void update(int id, String username, String password) {
        try {
            databaseHelper = DatabaseHelper.getInstance(context);
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("username", username);
            contentValues.put("password", password);
            db.update("USER", contentValues, "_id = ?", new String[]{Integer.toString(id)});
            db.close();
        } catch (SQLiteException exception) {
            exception.printStackTrace();
        }
        Log.v("User", "Updating....");
    }


    public boolean getId(String username, String password) {
        boolean bol = false;
        try {
            databaseHelper = DatabaseHelper.getInstance(context);
            SQLiteDatabase db = databaseHelper.getReadableDatabase();
            Cursor cursor = db.query("USER",
                    new String[]{"_id"},
                    "username = ? AND password = ?", new String[]{username, password}, null, null, null, null);
            if (cursor.getCount() > 0) {
                bol = true;
            }
            cursor.close();
            db.close();
        } catch (SQLiteException exception) {
            Toast toast = Toast.makeText(context, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
        return bol;
    }

    public int getId() {
        int id = 0;
        try {
            databaseHelper = DatabaseHelper.getInstance(context);
            SQLiteDatabase db = databaseHelper.getReadableDatabase();
            Cursor cursor = db.query("USER",
                    null, null, null, null, null, null);
            if (cursor.moveToFirst()) id = cursor.getInt(0);
            cursor.close();
            db.close();
        } catch (SQLiteException exception) {
            Toast toast = Toast.makeText(context, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
        return id;
    }


    public ArrayList<UsernameAndPassword> getAllUser() {
        ArrayList<UsernameAndPassword> list = new ArrayList();
        try {
            databaseHelper = DatabaseHelper.getInstance(context);
            SQLiteDatabase db = databaseHelper.getReadableDatabase();
            String selectQuery = "SELECT * FROM USER";
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    UsernameAndPassword usernameAndPassword = new UsernameAndPassword();
                    usernameAndPassword.setAccountUserName(cursor.getString(1));
                    usernameAndPassword.setAccountPassword(cursor.getString(2));
                    list.add(usernameAndPassword);
                } while (cursor.moveToNext());


            }
            cursor.close();
            db.close();
        } catch (SQLiteException exception) {
            Toast toast = Toast.makeText(context, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
        return list;
    }

    public UsernameAndPassword getUsrnameAndPassword() {
        UsernameAndPassword usernameAndPassword = new UsernameAndPassword();
        try {
            databaseHelper = DatabaseHelper.getInstance(context);
            SQLiteDatabase db = databaseHelper.getReadableDatabase();
            String selectQuery = "SELECT * FROM USER";
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                    usernameAndPassword.setAccountUserName(cursor.getString(1));
                    usernameAndPassword.setAccountPassword(cursor.getString(2));

            }
            cursor.close();
            db.close();
        } catch (SQLiteException exception) {
            Toast toast = Toast.makeText(context, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
        return usernameAndPassword;
    }


}
