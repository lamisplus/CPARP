package org.fhi360.lamis.mobile.cparp.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import org.fhi360.lamis.mobile.cparp.model.Devolve;
import org.fhi360.lamis.mobile.cparp.utility.DateUtil;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by aalozie on 6/13/2017.
 */

public class DevolveDAO {
    private Context context;
    private SQLiteOpenHelper databaseHelper;

    public DevolveDAO(Context context) {
        this.context = context;
    }

    public void save(int facilityId, int patientId, Date dateDevolved, String viralLoadAssessed, double lastViralLoad, Date dateLastViralLoad, String cd4Assessed, double lastCd4, Date dateLastCd4, String lastClinicStage, Date dateLastClinicStage,
                            String arvDispensed, String regimentype, String regimen, Date dateLastRefill, Date dateNextRefill, Date dateLastClinic, Date dateNextClinic, String notes, Date dateDiscontinued, String reasonDiscontinued) {
        try {
            databaseHelper = DatabaseHelper.getInstance(context);
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("facility_id", facilityId);
            contentValues.put("patient_id", patientId);
            contentValues.put("date_devolved", dateDevolved.getTime());
            contentValues.put("viral_load_assessed", viralLoadAssessed);
            contentValues.put("last_viral_load", lastViralLoad);
            if(dateLastViralLoad != null) contentValues.put("date_last_viral_load", dateLastViralLoad.getTime());
            contentValues.put("cd4_assessed", cd4Assessed);
            contentValues.put("last_cd4", lastCd4);
            if(dateLastCd4 != null) contentValues.put("date_last_cd4", dateLastCd4.getTime());
            contentValues.put("last_clinic_stage", lastClinicStage);
            if(dateLastClinicStage != null) contentValues.put("date_last_clinic_stage", dateLastClinicStage.getTime());
            contentValues.put("arv_dispensed", arvDispensed);
            contentValues.put("regimentype", regimentype);
            contentValues.put("regimen", regimen);
            if(dateLastRefill != null) contentValues.put("date_last_refill", dateLastRefill.getTime());
            if(dateNextRefill != null) contentValues.put("date_next_refill", dateNextRefill.getTime());
            if(dateLastClinic != null) contentValues.put("date_last_clinic", dateLastClinic.getTime());
            if(dateNextClinic != null) contentValues.put("date_next_clinic", dateNextClinic.getTime());
            contentValues.put("notes", notes);
            if(dateDiscontinued != null) contentValues.put("date_discontinued", dateDiscontinued.getTime());
            contentValues.put("reason_discontinued", reasonDiscontinued);

            db.insert("DEVOLVE", null, contentValues);
            db.close();
        }
        catch (Exception exception) {
            Toast toast = Toast.makeText(context, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void update(int id, int facilityId, int patientId,  Date dateDevolved, String viralLoadAssessed, double lastViralLoad, Date dateLastViralLoad, String cd4Assessed, double lastCd4, Date dateLastCd4, String lastClinicStage, Date dateLastClinicStage,
                              String arvDispensed, String regimentype, String regimen, Date dateLastRefill, Date dateNextRefill, Date dateLastClinic, Date dateNextClinic, String notes, Date dateDiscontinued, String reasonDiscontinued) {

        try {
            databaseHelper = DatabaseHelper.getInstance(context);
            SQLiteDatabase db = databaseHelper.getWritableDatabase();

            ContentValues contentValues = new ContentValues();
            contentValues.put("facility_id", facilityId);
            contentValues.put("patient_id", patientId);
            contentValues.put("date_devolved", dateDevolved.getTime());
            contentValues.put("viral_load_assessed", viralLoadAssessed);
            contentValues.put("last_viral_load", lastViralLoad);
            if(dateLastViralLoad != null) contentValues.put("date_last_viral_load", dateLastViralLoad.getTime());
            contentValues.put("cd4_assessed", cd4Assessed);
            contentValues.put("last_cd4", lastCd4);
            if(dateLastCd4 != null) contentValues.put("date_last_cd4", dateLastCd4.getTime());
            contentValues.put("last_clinic_stage", lastClinicStage);
            if(dateLastClinicStage != null) contentValues.put("date_last_clinic_stage", dateLastClinicStage.getTime());
            contentValues.put("arv_dispensed", arvDispensed);
            contentValues.put("regimentype", regimentype);
            contentValues.put("regimen", regimen);
            if(dateLastRefill != null) contentValues.put("date_last_refill", dateLastRefill.getTime());
            if(dateNextRefill != null) contentValues.put("date_next_refill", dateNextRefill.getTime());
            if(dateLastClinic != null) contentValues.put("date_last_clinic", dateLastClinic.getTime());
            if(dateNextClinic != null) contentValues.put("date_next_clinic", dateNextClinic.getTime());
            contentValues.put("notes", notes);
            if(dateDiscontinued != null) contentValues.put("date_discontinued", dateDiscontinued.getTime());
            contentValues.put("reason_discontinued", reasonDiscontinued);

            db.update("DEVOLVE", contentValues, "_id = ?", new String[] {Integer.toString(id)});
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
            db.delete("DEVOLVE", "_id = ?", new String[] {Integer.toString(id)});
            db.close();
        }
        catch (SQLiteException exception) {
            exception.printStackTrace();
        }
        Log.v("Devolve", "Deleting....");
    }

    public int getId(int facilityId, int patientId, Date dateDevolved) {
        int id = 0;
        try {
            databaseHelper = DatabaseHelper.getInstance(context);
            SQLiteDatabase db = databaseHelper.getReadableDatabase();

            Cursor cursor = db.query("DEVOLVE",
                    new String[]{"_id"},
                    "facility_id = ? AND patient_id = ? AND date_devolved = ?", new String[] {Integer.toString(facilityId), Integer.toString(patientId), Long.toString(dateDevolved.getTime())}, null, null, null);
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

    public Devolve getDevolve(int id) {
        Devolve devolve = new Devolve();
        try {
            databaseHelper = DatabaseHelper.getInstance(context);
            SQLiteDatabase db = databaseHelper.getReadableDatabase();

            Cursor cursor = db.query("DEVOLVE", null,
                    "_id = ?", new String[] {Integer.toString(id)}, null, null, null);
            if (cursor.moveToFirst()) {
                id = cursor.getInt(0);
                int facilityId  = cursor.getInt(1);
                int patientId = cursor.getInt(2);
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

                devolve.setId(id);
                devolve.setFacilityId(facilityId);
                devolve.setPatientId(patientId);
                devolve.setDateDevolved(dateDevolved);
                devolve.setViralLoadAssessed(viralLoadAssessed);
                devolve.setLastViralLoad(lastViralLoad);
                devolve.setDateLastViralLoad(dateLastViralLoad);
                devolve.setCd4Assessed(cd4Assessed);
                devolve.setLastCd4(lastCd4);
                devolve.setDateLastCd4(dateLastCd4);
                devolve.setLastClinicStage(lastClinicStage);
                devolve.setDateLastClinicStage(dateLastClinicStage);
                devolve.setArvDispensed(arvDispensed);
                devolve.setRegimentype(regimentype);
                devolve.setRegimen(regimen);
                devolve.setDateLastClinic(dateLastClinic);
                devolve.setDateNextClinic(dateNextClinic);
                devolve.setDateLastRefill(dateLastRefill);
                devolve.setDateNextRefill(dateNextRefill);
                devolve.setNotes(notes);
                devolve.setDateDiscontinued(dateDiscontinued);
                devolve.setReasonDiscontinued(reasonDiscontinued);
            }
            cursor.close();
            db.close();
        }
        catch (SQLiteException exception) {
            Toast toast = Toast.makeText(context, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
        return devolve;
    }

    public Devolve getDevolve(int facilityId, int patientId, Date dateDevolved) {
        Devolve devolve = new Devolve();
        try {
            databaseHelper = DatabaseHelper.getInstance(context);
            SQLiteDatabase db = databaseHelper.getReadableDatabase();

            Cursor cursor = db.query("DEVOLVE", null,
                    "facility_id = ? AND patient_id = ? AND date_devolved = ?", new String[] {Integer.toString(facilityId), Integer.toString(patientId), Long.toString(dateDevolved.getTime())}, null, null, null);
            if (cursor.moveToFirst()) {
                int id = cursor.getInt(0);
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

                devolve.setId(id);
                devolve.setFacilityId(facilityId);
                devolve.setPatientId(patientId);
                devolve.setDateDevolved(dateDevolved);
                devolve.setViralLoadAssessed(viralLoadAssessed);
                devolve.setLastViralLoad(lastViralLoad);
                devolve.setDateLastViralLoad(dateLastViralLoad);
                devolve.setCd4Assessed(cd4Assessed);
                devolve.setLastCd4(lastCd4);
                devolve.setDateLastCd4(dateLastCd4);
                devolve.setLastClinicStage(lastClinicStage);
                devolve.setDateLastClinicStage(dateLastClinicStage);
                devolve.setArvDispensed(arvDispensed);
                devolve.setRegimentype(regimentype);
                devolve.setRegimen(regimen);
                devolve.setDateLastClinic(dateLastClinic);
                devolve.setDateNextClinic(dateNextClinic);
                devolve.setDateLastRefill(dateLastRefill);
                devolve.setDateNextRefill(dateNextRefill);
                devolve.setNotes(notes);
                devolve.setDateDiscontinued(dateDiscontinued);
                devolve.setReasonDiscontinued(reasonDiscontinued);
            }
            cursor.close();
            db.close();
        }
        catch (SQLiteException exception) {
            Toast toast = Toast.makeText(context, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
        return devolve;
    }


    //Retrieve all devolves records for populating recycler view
    public ArrayList<Devolve> getDevolves() {
        ArrayList<Devolve> devolves = new ArrayList<Devolve>();
        try {
            databaseHelper = DatabaseHelper.getInstance(context);
            SQLiteDatabase db = databaseHelper.getReadableDatabase();

            Cursor cursor = db.query("DEVOLVE", null,
                    null, null, null, null, null);

            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(0);
                    int facilityId  = cursor.getInt(1);
                    int patientId = cursor.getInt(2);
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

                    Devolve devolve = new Devolve();
                    devolve.setId(id);
                    devolve.setFacilityId(facilityId);
                    devolve.setPatientId(patientId);
                    devolve.setDateDevolved(dateDevolved);
                    devolve.setViralLoadAssessed(viralLoadAssessed);
                    devolve.setLastViralLoad(lastViralLoad);
                    devolve.setDateLastViralLoad(dateLastViralLoad);
                    devolve.setCd4Assessed(cd4Assessed);
                    devolve.setLastCd4(lastCd4);
                    devolve.setDateLastCd4(dateLastCd4);
                    devolve.setLastClinicStage(lastClinicStage);
                    devolve.setDateLastClinicStage(dateLastClinicStage);
                    devolve.setArvDispensed(arvDispensed);
                    devolve.setRegimentype(regimentype);
                    devolve.setRegimen(regimen);
                    devolve.setDateLastClinic(dateLastClinic);
                    devolve.setDateNextClinic(dateNextClinic);
                    devolve.setDateLastRefill(dateLastRefill);
                    devolve.setDateNextRefill(dateNextRefill);
                    devolve.setNotes(notes);
                    devolve.setDateDiscontinued(dateDiscontinued);
                    devolve.setReasonDiscontinued(reasonDiscontinued);

                    devolves.add(devolve);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        }
        catch (SQLiteException exception) {
            Toast toast = Toast.makeText(context, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
        return devolves;
    }

    public Devolve getLastDevolve(int facilityId, int patientId) {
        Devolve devolve = new Devolve();
        try {
            databaseHelper = DatabaseHelper.getInstance(context);
            SQLiteDatabase db = databaseHelper.getReadableDatabase();

            String query = "SELECT * FROM devolve WHERE facility_id = " + facilityId + " AND patient_id = " + patientId + " ORDER BY date_devolved DESC LIMIT 1";
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(0);
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

                    devolve.setId(id);
                    devolve.setFacilityId(facilityId);
                    devolve.setPatientId(patientId);
                    devolve.setDateDevolved(dateDevolved);
                    devolve.setViralLoadAssessed(viralLoadAssessed);
                    devolve.setLastViralLoad(lastViralLoad);
                    devolve.setDateLastViralLoad(dateLastViralLoad);
                    devolve.setCd4Assessed(cd4Assessed);
                    devolve.setLastCd4(lastCd4);
                    devolve.setDateLastCd4(dateLastCd4);
                    devolve.setLastClinicStage(lastClinicStage);
                    devolve.setDateLastClinicStage(dateLastClinicStage);
                    devolve.setArvDispensed(arvDispensed);
                    devolve.setRegimentype(regimentype);
                    devolve.setRegimen(regimen);
                    devolve.setDateLastClinic(dateLastClinic);
                    devolve.setDateNextClinic(dateNextClinic);
                    devolve.setDateLastRefill(dateLastRefill);
                    devolve.setDateNextRefill(dateNextRefill);
                    devolve.setNotes(notes);
                    devolve.setDateDiscontinued(dateDiscontinued);
                    devolve.setReasonDiscontinued(reasonDiscontinued);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        }
        catch (SQLiteException exception) {
            Toast toast = Toast.makeText(context, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
        return devolve;
    }


    public void discontinue(int facilityId, int patientId, Date dateDiscontinued, String reasonDiscontinued) {
        int id = 0;
        try {
            databaseHelper = DatabaseHelper.getInstance(context);
            SQLiteDatabase db = databaseHelper.getReadableDatabase();

            String query = "SELECT * FROM devolve WHERE facility_id = " + facilityId + " AND patient_id = " + patientId + " ORDER BY date_devolved DESC LIMIT 1";
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    id = cursor.getInt(0);
                } while (cursor.moveToNext());
            }
            if(id != 0) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("date_discontinued", dateDiscontinued.getTime());
                contentValues.put("reason_discontinued", reasonDiscontinued);
                db.update("DEVOLVE", contentValues, "_id = ?", new String[] {Integer.toString(id)});
            }
            cursor.close();
            db.close();
        }
        catch (SQLiteException exception) {
            Toast toast = Toast.makeText(context, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
