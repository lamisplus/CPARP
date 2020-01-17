package org.fhi360.lamis.mobile.cparp.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import org.fhi360.lamis.mobile.cparp.model.Patient;
import org.fhi360.lamis.mobile.cparp.utility.DateUtil;
import org.fhi360.lamis.mobile.cparp.utility.Scrambler;
import org.fhi360.lamis.mobile.cparp.utility.StringUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by aalozie on 7/10/2016.
 */
public class PatientDAO {
    private Context context;
    private SQLiteOpenHelper databaseHelper;

    public PatientDAO(Context context) {
        this.context = context;
    }

    public void save(int facilityId, int patientId, String hospitalNum, String uniqueId, String surname, String otherNames, String gender, Date dateBirth, String address, String phone, Date dateStarted, String regimentype, String regimen, String lastClinicStage,
                     double lastViralLoad, Date dateLastViralLoad, Date viralLoadDueDate, String viralLoadType, double lastCd4, Date dateLastCd4, Date dateLastRefill, Date dateNextRefill, Date dateLastClinic, Date dateNextClinic, String lastRefillSetting) {
        try {
            databaseHelper = DatabaseHelper.getInstance(context);
            SQLiteDatabase db = databaseHelper.getWritableDatabase();

            ContentValues contentValues = new ContentValues();
            contentValues.put("facility_id", facilityId);
            contentValues.put("patient_id", patientId);
            contentValues.put("hospital_num", hospitalNum);
            contentValues.put("unique_id", uniqueId);
            contentValues.put("surname", surname);
            contentValues.put("other_names", otherNames);
            contentValues.put("gender", gender);
            if(dateBirth != null) contentValues.put("date_birth", dateBirth.getTime()); // Covert date to long to be able to store in SQLite
            contentValues.put("address", address);
            contentValues.put("phone", phone);
            if(dateStarted != null) contentValues.put("date_started", dateStarted.getTime());
            contentValues.put("last_clinic_stage", lastClinicStage);
            contentValues.put("regimentype", regimentype);
            contentValues.put("regimen", regimen);
            contentValues.put("last_viral_load", lastViralLoad);
            if(dateLastViralLoad != null) contentValues.put("date_last_viral_load", dateLastViralLoad.getTime());
            if(viralLoadDueDate != null) contentValues.put("viral_load_due_date", viralLoadDueDate.getTime());
            contentValues.put("viral_load_type", viralLoadType);
            contentValues.put("last_cd4", lastCd4);
            if(dateLastCd4 != null) contentValues.put("date_last_cd4", dateLastCd4.getTime());
            if(dateLastRefill != null) contentValues.put("date_last_refill", dateLastRefill.getTime());
            if(dateNextRefill != null) contentValues.put("date_next_refill", dateNextRefill.getTime());
            if(dateLastClinic != null) contentValues.put("date_last_clinic", dateLastClinic.getTime());
            if(dateNextClinic != null) contentValues.put("date_next_clinic", dateNextClinic.getTime());
            contentValues.put("last_refill_setting", lastRefillSetting);
            contentValues.put("discontinued", 0);

            db.insert("PATIENT", null, contentValues);
            db.close();
        }
        catch (Exception exception) {
            Toast toast = Toast.makeText(context, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void update(int id, int facilityId, int patientId, String hospitalNum, String uniqueId, String surname, String otherNames, String gender, Date dateBirth, String address, String phone, Date dateStarted, String regimentype, String regimen, String lastClinicStage,
                       double lastViralLoad, Date dateLastViralLoad, Date viralLoadDueDate, String viralLoadType, double lastCd4, Date dateLastCd4, Date dateLastRefill, Date dateNextRefill, Date dateLastClinic, Date dateNextClinic, String lastRefillSetting) {

        try {
            databaseHelper = DatabaseHelper.getInstance(context);
            SQLiteDatabase db = databaseHelper.getWritableDatabase();

            ContentValues contentValues = new ContentValues();
            contentValues.put("facility_id", facilityId);
            contentValues.put("patient_id", patientId);
            contentValues.put("hospital_num", hospitalNum);
            contentValues.put("unique_id", uniqueId);
            contentValues.put("surname", surname);
            contentValues.put("other_names", otherNames);
            contentValues.put("gender", gender);
            if(dateBirth != null) contentValues.put("date_birth", dateBirth.getTime()); // Covert date to long to be able to store in SQLite
            contentValues.put("address", address);
            contentValues.put("phone", phone);
            if(dateStarted != null) contentValues.put("date_started", dateStarted.getTime());
            contentValues.put("last_clinic_stage", lastClinicStage);
            contentValues.put("regimentype", regimentype);
            contentValues.put("regimen", regimen);
            contentValues.put("last_viral_load", lastViralLoad);
            if(dateLastViralLoad != null) contentValues.put("date_last_viral_load", dateLastViralLoad.getTime());
            if(viralLoadDueDate != null) contentValues.put("viral_load_due_date", viralLoadDueDate.getTime());
            contentValues.put("viral_load_type", viralLoadType);
            contentValues.put("last_cd4", lastCd4);
            if(dateLastCd4 != null) contentValues.put("date_last_cd4", dateLastCd4.getTime());
            if(dateLastRefill != null) contentValues.put("date_last_refill", dateLastRefill.getTime());
            if(dateNextRefill != null) contentValues.put("date_next_refill", dateNextRefill.getTime());
            if(dateLastClinic != null) contentValues.put("date_last_clinic", dateLastClinic.getTime());
            if(dateNextClinic != null) contentValues.put("date_next_clinic", dateNextClinic.getTime());
            contentValues.put("last_refill_setting", lastRefillSetting);

            db.update("PATIENT", contentValues, "_id = ?", new String[] {Integer.toString(id)});
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
            db.delete("PATIENT", "_id = ?", new String[] {Integer.toString(id)});
            db.close();
        }
        catch (SQLiteException exception) {
            exception.printStackTrace();
        }
        Log.v("Patient", "Deleting....");
    }

    public int getId(int facilityId, int patientId) {
        int id = 0;
        try {
            databaseHelper = DatabaseHelper.getInstance(context);
            SQLiteDatabase db = databaseHelper.getReadableDatabase();

            Cursor cursor = db.query("PATIENT",
                    new String[]{"_id"},
                    "facility_id = ? AND patient_id = ?", new String[] {Integer.toString(facilityId), Integer.toString(patientId)}, null, null, null);
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

    public Patient getPatient(int id) {
        Patient patient = new Patient();
        Scrambler scrambler = new Scrambler();
        try {
            databaseHelper = DatabaseHelper.getInstance(context);
            SQLiteDatabase db = databaseHelper.getReadableDatabase();

            Cursor cursor = db.query("PATIENT", null,
                    "_id = ?", new String[] {Integer.toString(id)}, null, null, null);
            if (cursor.moveToFirst()) {
                id = cursor.getInt(0);
                int facilityId = cursor.getInt(1);
                int patientId = cursor.getInt(2);
                String hospitalNum = cursor.getString(3);
                String uniqueId = cursor.getString(4);
                String surname = cursor.getString(5);
                surname = (scrambler.unscrambleCharacters(surname)).toUpperCase();
                String otherNames = cursor.getString(6);
                otherNames = StringUtil.captalize(scrambler.unscrambleCharacters(otherNames));
                String gender = cursor.getString(7);
                Date dateBirth = DateUtil.unixTimestampToDate(cursor.getLong(8), "dd/MM/yyyy");
                String address = cursor.getString(9);
                address = scrambler.unscrambleCharacters(address);
                String phone = cursor.getString(10);
                phone = scrambler.unscrambleNumbers(phone);
                Date dateStarted = DateUtil.unixTimestampToDate(cursor.getLong(11), "dd/MM/yyyy");
                String regimentype = cursor.getString(12);
                String regimen = cursor.getString(13);
                String lastClinicStage = cursor.getString(14);
                double lastViralLoad = cursor.getDouble(15);
                Date dateLastViralLoad = DateUtil.unixTimestampToDate(cursor.getLong(16), "dd/MM/yyyy");
                Date viralLoadDueDate = DateUtil.unixTimestampToDate(cursor.getLong(17), "dd/MM/yyyy");
                String viralLoadType = cursor.getString(18);
                double lastCd4 = cursor.getDouble(19);
                Date dateLastCd4 = DateUtil.unixTimestampToDate(cursor.getLong(20), "dd/MM/yyyy");
                Date dateLastRefill = DateUtil.unixTimestampToDate(cursor.getLong(21), "dd/MM/yyyy");
                Date dateNextRefill = DateUtil.unixTimestampToDate(cursor.getLong(22), "dd/MM/yyyy");
                Date dateLastClinic = DateUtil.unixTimestampToDate(cursor.getLong(23), "dd/MM/yyyy");
                Date dateNextClinic = DateUtil.unixTimestampToDate(cursor.getLong(24), "dd/MM/yyyy");
                String lastRefillSetting = cursor.getString(25);
                int discontinued = cursor.getInt(26);

                patient.setId(id);
                patient.setFacilityId(facilityId);
                patient.setPatientId(patientId);
                patient.setHospitalNum(hospitalNum);
                patient.setUniqueId(uniqueId);
                patient.setSurname(surname);
                patient.setOtherNames(otherNames);
                patient.setGender(gender);
                patient.setDateBirth(dateBirth);
                patient.setAddress(address);
                patient.setPhone(phone);
                patient.setDateStarted(dateStarted);
                patient.setLastClinicStage(lastClinicStage);;
                patient.setRegimentype(regimentype);
                patient.setRegimen(regimen);
                patient.setLastViralLoad(lastViralLoad);
                patient.setDateLastViralLoad(dateLastViralLoad);
                patient.setViralLoadDueDate(viralLoadDueDate);
                patient.setViralLoadType(viralLoadType);
                patient.setLastCd4(lastCd4);
                patient.setDateLastCd4(dateLastCd4);
                patient.setDateLastClinic(dateLastClinic);
                patient.setDateNextClinic(dateNextClinic);
                patient.setDateLastRefill(dateLastRefill);
                patient.setDateNextRefill(dateNextRefill);
                patient.setLastRefillSetting(lastRefillSetting);
                patient.setDiscontinued(discontinued);
            }
            cursor.close();
            db.close();
        }
        catch (SQLiteException exception) {
            Toast toast = Toast.makeText(context, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
        return patient;
    }

    public Patient getPatient(int facilityId, int patientId) {
        Patient patient = new Patient();
        Scrambler scrambler = new Scrambler();
        try {
            databaseHelper = DatabaseHelper.getInstance(context);
            SQLiteDatabase db = databaseHelper.getReadableDatabase();

            Cursor cursor = db.query("PATIENT", null,
                    "facility_id = ? AND patient_id = ?", new String[] {Integer.toString(facilityId), Integer.toString(patientId)}, null, null, null);
            if (cursor.moveToFirst()) {
                int id = cursor.getInt(0);
                String hospitalNum = cursor.getString(3);
                String uniqueId = cursor.getString(4);
                String surname = cursor.getString(5);
                surname = (scrambler.unscrambleCharacters(surname)).toUpperCase();
                String otherNames = cursor.getString(6);
                otherNames = StringUtil.captalize(scrambler.unscrambleCharacters(otherNames));
                String gender = cursor.getString(7);
                Date dateBirth = DateUtil.unixTimestampToDate(cursor.getLong(8), "dd/MM/yyyy");
                String address = cursor.getString(9);
                address = scrambler.unscrambleCharacters(address);
                String phone = cursor.getString(10);
                phone = scrambler.unscrambleNumbers(phone);
                Date dateStarted = DateUtil.unixTimestampToDate(cursor.getLong(11), "dd/MM/yyyy");
                String regimentype = cursor.getString(12);
                String regimen = cursor.getString(13);
                String lastClinicStage = cursor.getString(14);
                double lastViralLoad = cursor.getDouble(15);
                Date dateLastViralLoad = DateUtil.unixTimestampToDate(cursor.getLong(16), "dd/MM/yyyy");
                Date viralLoadDueDate = DateUtil.unixTimestampToDate(cursor.getLong(17), "dd/MM/yyyy");
                String viralLoadType = cursor.getString(18);
                double lastCd4 = cursor.getDouble(19);
                Date dateLastCd4 = DateUtil.unixTimestampToDate(cursor.getLong(20), "dd/MM/yyyy");
                Date dateLastRefill = DateUtil.unixTimestampToDate(cursor.getLong(21), "dd/MM/yyyy");
                Date dateNextRefill = DateUtil.unixTimestampToDate(cursor.getLong(22), "dd/MM/yyyy");
                Date dateLastClinic = DateUtil.unixTimestampToDate(cursor.getLong(23), "dd/MM/yyyy");
                Date dateNextClinic = DateUtil.unixTimestampToDate(cursor.getLong(24), "dd/MM/yyyy");
                String lastRefillSetting = cursor.getString(25);
                int discontinued = cursor.getInt(26);

                patient.setId(id);
                patient.setFacilityId(facilityId);
                patient.setPatientId(patientId);
                patient.setHospitalNum(hospitalNum);
                patient.setUniqueId(uniqueId);
                patient.setSurname(surname);
                patient.setOtherNames(otherNames);
                patient.setGender(gender);
                patient.setDateBirth(dateBirth);
                patient.setAddress(address);
                patient.setPhone(phone);
                patient.setDateStarted(dateStarted);
                patient.setLastClinicStage(lastClinicStage);
                patient.setRegimentype(regimentype);
                patient.setRegimen(regimen);
                patient.setLastClinicStage(lastClinicStage);
                patient.setLastViralLoad(lastViralLoad);
                patient.setDateLastViralLoad(dateLastViralLoad);
                patient.setViralLoadDueDate(viralLoadDueDate);
                patient.setViralLoadType(viralLoadType);
                patient.setLastCd4(lastCd4);
                patient.setDateLastCd4(dateLastCd4);
                patient.setDateLastClinic(dateLastClinic);
                patient.setDateNextClinic(dateNextClinic);
                patient.setDateLastRefill(dateLastRefill);
                patient.setDateNextRefill(dateNextRefill);
                patient.setLastRefillSetting(lastRefillSetting);
                patient.setDiscontinued(discontinued);
            }
            cursor.close();
            db.close();
        }
        catch (SQLiteException exception) {
            Toast toast = Toast.makeText(context, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
        return patient;
    }


    //Retrieve all patients records for populating recycler view
    public ArrayList<Patient> getPatients() {
        ArrayList<Patient> patients = new ArrayList<Patient>();
        Scrambler scrambler = new Scrambler();
        try {
            databaseHelper = DatabaseHelper.getInstance(context);
            SQLiteDatabase db = databaseHelper.getReadableDatabase();

            Cursor cursor = db.query("PATIENT", null,
                    null, null, null, null, "surname ASC");

            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(0);
                    int facilityId = cursor.getInt(1);
                    int patientId = cursor.getInt(2);
                    String hospitalNum = cursor.getString(3);
                    String uniqueId = cursor.getString(4);
                    String surname = cursor.getString(5);
                    surname = (scrambler.unscrambleCharacters(surname)).toUpperCase();
                    String otherNames = cursor.getString(6);
                    otherNames = StringUtil.captalize(scrambler.unscrambleCharacters(otherNames));
                    String gender = cursor.getString(7);
                    Date dateBirth = DateUtil.unixTimestampToDate(cursor.getLong(8), "dd/MM/yyyy");
                    String address = cursor.getString(9);
                    address = scrambler.unscrambleCharacters(address);
                    String phone = cursor.getString(10);
                    phone = scrambler.unscrambleNumbers(phone);
                    Date dateStarted = DateUtil.unixTimestampToDate(cursor.getLong(11), "dd/MM/yyyy");
                    String regimentype = cursor.getString(12);
                    String regimen = cursor.getString(13);
                    String lastClinicStage = cursor.getString(14);
                    double lastViralLoad = cursor.getDouble(15);
                    Date dateLastViralLoad = DateUtil.unixTimestampToDate(cursor.getLong(16), "dd/MM/yyyy");
                    Date viralLoadDueDate = DateUtil.unixTimestampToDate(cursor.getLong(17), "dd/MM/yyyy");
                    String viralLoadType = cursor.getString(18);
                    double lastCd4 = cursor.getDouble(19);
                    Date dateLastCd4 = DateUtil.unixTimestampToDate(cursor.getLong(20), "dd/MM/yyyy");
                    Date dateLastRefill = DateUtil.unixTimestampToDate(cursor.getLong(21), "dd/MM/yyyy");
                    Date dateNextRefill = DateUtil.unixTimestampToDate(cursor.getLong(22), "dd/MM/yyyy");
                    Date dateLastClinic = DateUtil.unixTimestampToDate(cursor.getLong(23), "dd/MM/yyyy");
                    Date dateNextClinic = DateUtil.unixTimestampToDate(cursor.getLong(24), "dd/MM/yyyy");
                    String lastRefillSetting = cursor.getString(25);
                    int discontinued = cursor.getInt(26);

                    Patient patient = new Patient();
                    patient.setId(id);
                    patient.setFacilityId(facilityId);
                    patient.setPatientId(patientId);
                    patient.setHospitalNum(hospitalNum);
                    patient.setUniqueId(uniqueId);
                    patient.setSurname(surname);
                    patient.setOtherNames(otherNames);
                    patient.setGender(gender);
                    patient.setDateBirth(dateBirth);
                    patient.setAddress(address);
                    patient.setPhone(phone);
                    patient.setDateStarted(dateStarted);
                    patient.setRegimentype(regimentype);
                    patient.setRegimen(regimen);
                    patient.setLastClinicStage(lastClinicStage);
                    patient.setLastViralLoad(lastViralLoad);
                    patient.setDateLastViralLoad(dateLastViralLoad);
                    patient.setViralLoadDueDate(viralLoadDueDate);
                    patient.setViralLoadType(viralLoadType);
                    patient.setLastCd4(lastCd4);
                    patient.setDateLastCd4(dateLastCd4);
                    patient.setDateLastClinic(dateLastClinic);
                    patient.setDateNextClinic(dateNextClinic);
                    patient.setDateLastRefill(dateLastRefill);
                    patient.setDateNextRefill(dateNextRefill);
                    patient.setLastRefillSetting(lastRefillSetting);
                    patient.setDiscontinued(discontinued);

                    patients.add(patient);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        }
        catch (SQLiteException exception) {
            Toast toast = Toast.makeText(context, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
        return patients;
    }

    //Retrieve all patients records for populating filtered recycler view
    public ArrayList<Patient> getPatients(String fullname) {
        fullname = fullname+"%";
        ArrayList<Patient> patients = new ArrayList<Patient>();
        Scrambler scrambler = new Scrambler();
        try {
            databaseHelper = DatabaseHelper.getInstance(context);
            SQLiteDatabase db = databaseHelper.getReadableDatabase();

            Cursor cursor = db.query("PATIENT", null,
                    "surname LIKE ? OR other_names LIKE ?", new String[] {fullname, fullname}, null, null, null, null);

            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(0);
                    int facilityId = cursor.getInt(1);
                    int patientId = cursor.getInt(2);
                    String hospitalNum = cursor.getString(3);
                    String uniqueId = cursor.getString(4);
                    String surname = cursor.getString(5);
                    surname = (scrambler.unscrambleCharacters(surname)).toUpperCase();
                    String otherNames = cursor.getString(6);
                    otherNames = StringUtil.captalize(scrambler.unscrambleCharacters(otherNames));
                    String gender = cursor.getString(7);
                    Date dateBirth = DateUtil.unixTimestampToDate(cursor.getLong(8), "dd/MM/yyyy");
                    String address = cursor.getString(9);
                    address = scrambler.unscrambleCharacters(address);
                    String phone = cursor.getString(10);
                    phone = scrambler.unscrambleNumbers(phone);
                    Date dateStarted = DateUtil.unixTimestampToDate(cursor.getLong(11), "dd/MM/yyyy");
                    String regimentype = cursor.getString(12);
                    String regimen = cursor.getString(13);
                    String lastClinicStage = cursor.getString(14);
                    double lastViralLoad = cursor.getDouble(15);
                    Date dateLastViralLoad = DateUtil.unixTimestampToDate(cursor.getLong(16), "dd/MM/yyyy");
                    Date viralLoadDueDate = DateUtil.unixTimestampToDate(cursor.getLong(17), "dd/MM/yyyy");
                    String viralLoadType = cursor.getString(18);
                    double lastCd4 = cursor.getDouble(19);
                    Date dateLastCd4 = DateUtil.unixTimestampToDate(cursor.getLong(20), "dd/MM/yyyy");
                    Date dateLastRefill = DateUtil.unixTimestampToDate(cursor.getLong(21), "dd/MM/yyyy");
                    Date dateNextRefill = DateUtil.unixTimestampToDate(cursor.getLong(22), "dd/MM/yyyy");
                    Date dateLastClinic = DateUtil.unixTimestampToDate(cursor.getLong(23), "dd/MM/yyyy");
                    Date dateNextClinic = DateUtil.unixTimestampToDate(cursor.getLong(24), "dd/MM/yyyy");
                    String lastRefillSetting = cursor.getString(25);
                    int discontinued = cursor.getInt(26);

                    Patient patient = new Patient();
                    patient.setId(id);
                    patient.setFacilityId(facilityId);
                    patient.setPatientId(patientId);
                    patient.setHospitalNum(hospitalNum);
                    patient.setUniqueId(uniqueId);
                    patient.setSurname(surname);
                    patient.setOtherNames(otherNames);
                    patient.setGender(gender);
                    patient.setDateBirth(dateBirth);
                    patient.setAddress(address);
                    patient.setPhone(phone);
                    patient.setDateStarted(dateStarted);
                    patient.setLastClinicStage(lastClinicStage);
                    patient.setRegimentype(regimentype);
                    patient.setRegimen(regimen);
                    patient.setLastViralLoad(lastViralLoad);
                    patient.setDateLastViralLoad(dateLastViralLoad);
                    patient.setViralLoadDueDate(viralLoadDueDate);
                    patient.setViralLoadType(viralLoadType);
                    patient.setLastCd4(lastCd4);
                    patient.setDateLastCd4(dateLastCd4);
                    patient.setDateLastClinic(dateLastClinic);
                    patient.setDateNextClinic(dateNextClinic);
                    patient.setDateLastRefill(dateLastRefill);
                    patient.setDateNextRefill(dateNextRefill);
                    patient.setLastRefillSetting(lastRefillSetting);
                    patient.setDiscontinued(discontinued);

                    patients.add(patient);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        }
        catch (SQLiteException exception) {
            Toast toast = Toast.makeText(context, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
        return patients;
    }

    public void updateRefillSetting(int facilityId, int patientId) {
        try {
            databaseHelper = DatabaseHelper.getInstance(context);
            SQLiteDatabase db = databaseHelper.getWritableDatabase();

            ContentValues contentValues = new ContentValues();
            contentValues.put("last_refill_setting", "COMMUNITY");
            db.update("PATIENT", contentValues, "facility_id = ? AND patient_id = ?", new String[] {Integer.toString(facilityId), Integer.toString(patientId)});
            db.close();
        }
        catch (Exception exception) {
            Log.v("Database unavailable", exception.getMessage());
            Toast toast = Toast.makeText(context, "Database unavailable "+exception.getMessage(), Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public boolean defaulters() {
        boolean defaulter = false;
        long today = System.currentTimeMillis();
        long period = 90 * 24 * 60 * 60 * 1000;     // 90 days in milliseconds
        try {
            databaseHelper = DatabaseHelper.getInstance(context);
            SQLiteDatabase db = databaseHelper.getReadableDatabase();

            Cursor cursor = db.query("PATIENT", null,
                    "? - date_next_refill > ?" , new String[] {Long.toString(today), Long.toString(period)}, null, null, "surname ASC");

            if (cursor.moveToFirst()) defaulter = true;
            cursor.close();
            db.close();
        }
        catch (SQLiteException exception) {
            Toast toast = Toast.makeText(context, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
        return defaulter;
    }

    public ArrayList<Patient> getDefaulters() {
        ArrayList<Patient> patients = new ArrayList<Patient>();

/*
TimeUnit.DAYS.toMillis(1);     // 1 day to milliseconds.
TimeUnit.MINUTES.toMillis(23); // 23 minutes to milliseconds.
TimeUnit.HOURS.toMillis(4);    // 4 hours to milliseconds.
TimeUnit.SECONDS.toMillis(96); // 96 seconds to milliseconds.
 */
        long today = System.currentTimeMillis();
        Log.v("TODAY ",today+"");
        long period = (long) 90 * 24 * 60 * 60 * 1000; // 90 days in milliseconds

        Log.v("PERIOD ",String.valueOf(period));
        String test="1553036400000";
        Log.v("SUBSTRACT  ",today-Long.parseLong(test)+"");
        //1553036400000
        try {
            databaseHelper = DatabaseHelper.getInstance(context);
            SQLiteDatabase db = databaseHelper.getReadableDatabase();

            String selectQuery = "SELECT  *  FROM patient WHERE CURRENT_TIME > date_next_refill ORDER BY surname ASC";
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(0);
                    int facilityId = cursor.getInt(1);
                    int patientId = cursor.getInt(2);
                    String hospitalNum = cursor.getString(3);
                    String uniqueId = cursor.getString(4);
                    String surname = cursor.getString(5);
                    surname = surname.toUpperCase();
                    String otherNames = cursor.getString(6);
                    otherNames = StringUtil.captalize(otherNames);
                    String gender = cursor.getString(7);
                    Date dateBirth = DateUtil.unixTimestampToDate(cursor.getLong(8), "dd/MM/yyyy");
                    String address = cursor.getString(9);
                    address = address;
                    String phone = cursor.getString(10);
                    phone = phone;
                    Date dateStarted = DateUtil.unixTimestampToDate(cursor.getLong(11), "dd/MM/yyyy");
                    String regimentype = cursor.getString(12);
                    String regimen = cursor.getString(13);
                    String lastClinicStage = cursor.getString(14);
                    double lastViralLoad = cursor.getDouble(15);
                    Date dateLastViralLoad = DateUtil.unixTimestampToDate(cursor.getLong(16), "dd/MM/yyyy");
                    Date viralLoadDueDate = DateUtil.unixTimestampToDate(cursor.getLong(17), "dd/MM/yyyy");
                    String viralLoadType = cursor.getString(18);
                    double lastCd4 = cursor.getDouble(19);
                    Date dateLastCd4 = DateUtil.unixTimestampToDate(cursor.getLong(20), "dd/MM/yyyy");
                    Date dateLastRefill = DateUtil.unixTimestampToDate(cursor.getLong(21), "dd/MM/yyyy");
                    Date dateNextRefill = DateUtil.unixTimestampToDate(cursor.getLong(22), "dd/MM/yyyy");
                    Date dateLastClinic = DateUtil.unixTimestampToDate(cursor.getLong(23), "dd/MM/yyyy");
                    Date dateNextClinic = DateUtil.unixTimestampToDate(cursor.getLong(24), "dd/MM/yyyy");
                    String lastRefillSetting = cursor.getString(25);
                    int discontinued = cursor.getInt(26);

                    Patient patient = new Patient();
                    patient.setId(id);
                    patient.setFacilityId(facilityId);
                    patient.setPatientId(patientId);
                    patient.setHospitalNum(hospitalNum);
                    patient.setUniqueId(uniqueId);
                    patient.setSurname(surname);
                    patient.setOtherNames(otherNames);
                    patient.setGender(gender);
                    patient.setDateBirth(dateBirth);
                    patient.setAddress(address);
                    patient.setPhone(phone);
                    patient.setDateStarted(dateStarted);
                    patient.setLastClinicStage(lastClinicStage);
                    patient.setRegimentype(regimentype);
                    patient.setRegimen(regimen);
                    patient.setLastViralLoad(lastViralLoad);
                    patient.setDateLastViralLoad(dateLastViralLoad);
                    patient.setViralLoadDueDate(viralLoadDueDate);
                    patient.setViralLoadType(viralLoadType);
                    patient.setLastCd4(lastCd4);
                    patient.setDateLastCd4(dateLastCd4);
                    patient.setDateLastClinic(dateLastClinic);
                    patient.setDateNextClinic(dateNextClinic);
                    patient.setDateLastRefill(dateLastRefill);
                    patient.setDateNextRefill(dateNextRefill);
                    patient.setLastRefillSetting(lastRefillSetting);
                    patient.setDiscontinued(discontinued);

                    patients.add(patient);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        }
        catch (SQLiteException exception) {
            Toast toast = Toast.makeText(context, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
        return patients;
    }

    //Retrieve all patients records for populating filtered recycler view
    public ArrayList<Patient> getDefaulters(String fullname) {
        Log.v("PatientDAO", "Defaulters search....");
        fullname = fullname+"%";
        ArrayList<Patient> patients = new ArrayList<Patient>();
        Scrambler scrambler = new Scrambler();

        long today = System.currentTimeMillis();
        long period = 90 * 24 * 60 * 60 * 1000;     // 90 days in milliseconds
        try {
            databaseHelper = DatabaseHelper.getInstance(context);
            SQLiteDatabase db = databaseHelper.getReadableDatabase();

            Cursor cursor = db.query("PATIENT", null,
                    "? - date_next_refill > ? AND surname LIKE ? OR other_names LIKE ?", new String[] {Long.toString(today), Long.toString(period), fullname, fullname}, null, null, null, null);

            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(0);
                    int facilityId = cursor.getInt(1);
                    int patientId = cursor.getInt(2);
                    String hospitalNum = cursor.getString(3);
                    String uniqueId = cursor.getString(4);
                    String surname = cursor.getString(5);
                    surname = (scrambler.unscrambleCharacters(surname)).toUpperCase();
                    String otherNames = cursor.getString(6);
                    otherNames = StringUtil.captalize(scrambler.unscrambleCharacters(otherNames));
                    String gender = cursor.getString(7);
                    Date dateBirth = DateUtil.unixTimestampToDate(cursor.getLong(8), "dd/MM/yyyy");
                    String address = cursor.getString(9);
                    address = scrambler.unscrambleCharacters(address);
                    String phone = cursor.getString(10);
                    phone = scrambler.unscrambleNumbers(phone);
                    Date dateStarted = DateUtil.unixTimestampToDate(cursor.getLong(11), "dd/MM/yyyy");
                    String regimentype = cursor.getString(12);
                    String regimen = cursor.getString(13);
                    String lastClinicStage = cursor.getString(14);
                    double lastViralLoad = cursor.getDouble(15);
                    Date dateLastViralLoad = DateUtil.unixTimestampToDate(cursor.getLong(16), "dd/MM/yyyy");
                    Date viralLoadDueDate = DateUtil.unixTimestampToDate(cursor.getLong(17), "dd/MM/yyyy");
                    String viralLoadType = cursor.getString(18);
                    double lastCd4 = cursor.getDouble(19);
                    Date dateLastCd4 = DateUtil.unixTimestampToDate(cursor.getLong(20), "dd/MM/yyyy");
                    Date dateLastRefill = DateUtil.unixTimestampToDate(cursor.getLong(21), "dd/MM/yyyy");
                    Date dateNextRefill = DateUtil.unixTimestampToDate(cursor.getLong(22), "dd/MM/yyyy");
                    Date dateLastClinic = DateUtil.unixTimestampToDate(cursor.getLong(23), "dd/MM/yyyy");
                    Date dateNextClinic = DateUtil.unixTimestampToDate(cursor.getLong(24), "dd/MM/yyyy");
                    String lastRefillSetting = cursor.getString(25);
                    int discontinued = cursor.getInt(26);

                    Patient patient = new Patient();
                    patient.setId(id);
                    patient.setFacilityId(facilityId);
                    patient.setPatientId(patientId);
                    patient.setHospitalNum(hospitalNum);
                    patient.setUniqueId(uniqueId);
                    patient.setSurname(surname);
                    patient.setOtherNames(otherNames);
                    patient.setGender(gender);
                    patient.setDateBirth(dateBirth);
                    patient.setAddress(address);
                    patient.setPhone(phone);
                    patient.setDateStarted(dateStarted);
                    patient.setLastClinicStage(lastClinicStage);
                    patient.setRegimentype(regimentype);
                    patient.setRegimen(regimen);
                    patient.setLastViralLoad(lastViralLoad);
                    patient.setDateLastViralLoad(dateLastViralLoad);
                    patient.setViralLoadDueDate(viralLoadDueDate);
                    patient.setViralLoadType(viralLoadType);
                    patient.setLastCd4(lastCd4);
                    patient.setDateLastCd4(dateLastCd4);
                    patient.setDateLastClinic(dateLastClinic);
                    patient.setDateNextClinic(dateNextClinic);
                    patient.setDateLastRefill(dateLastRefill);
                    patient.setDateNextRefill(dateNextRefill);
                    patient.setLastRefillSetting(lastRefillSetting);
                    patient.setDiscontinued(discontinued);

                    patients.add(patient);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        }
        catch (SQLiteException exception) {
            Toast toast = Toast.makeText(context, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
        return patients;
    }

}

// public Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy
//db.query("PATIENT" null, date_last_refill + " BETWEEN ? AND ?", new String[] {minDate + " 00:00:00", maxDate + " 23:59:59" }, null, null, null, null);

