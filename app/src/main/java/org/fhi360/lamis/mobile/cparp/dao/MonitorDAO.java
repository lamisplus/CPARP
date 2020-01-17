package org.fhi360.lamis.mobile.cparp.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.Date;

public class MonitorDAO {
    private Context context;
    private SQLiteOpenHelper databaseHelper;
    public MonitorDAO(Context context) {
        this.context = context;
    }

    public void save(int facilityId, String entityId, String tableName, int operationId) {
        try {
            databaseHelper = DatabaseHelper.getInstance(context);
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("facility_id", facilityId);
            contentValues.put("entity_id", entityId);
            contentValues.put("table_name", tableName);
            contentValues.put("operation_id", operationId);
            contentValues.put("time_stamp", new Date().getTime());
            db.insert("MONITOR", null, contentValues);
            db.close();
        }
        catch (Exception exception) {
            Toast toast = Toast.makeText(context, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public JSONArray getContent(long timeLastSync) {
        JSONArray jsonArray = new JSONArray();
        int communitypharmId = new AccountDAO(context).getAccountId();
        try {
            databaseHelper = DatabaseHelper.getInstance(context);
            SQLiteDatabase db = databaseHelper.getReadableDatabase();
            String query = "SELECT * FROM monitor WHERE time_stamp >= " + timeLastSync;
            Cursor cursor = db.rawQuery(query, null);
            int colCount = cursor.getColumnCount();
            while (cursor.moveToNext()) {
                JSONObject rowObject = new JSONObject();
                for(int i = 0; i < colCount; i++) {
                    String columnName = cursor.getColumnName(i);
                    rowObject.put(columnName, cursor.getString(i));
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
