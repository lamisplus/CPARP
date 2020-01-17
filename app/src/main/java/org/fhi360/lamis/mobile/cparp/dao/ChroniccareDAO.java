package org.fhi360.lamis.mobile.cparp.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import org.fhi360.lamis.mobile.cparp.model.Chroniccare;
import org.fhi360.lamis.mobile.cparp.utility.DateUtil;
import org.fhi360.lamis.mobile.cparp.utility.StringUtil;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by aalozie on 3/19/2017.
 */

public class ChroniccareDAO {
    private Context context;
    private SQLiteOpenHelper databaseHelper;

    public ChroniccareDAO (Context context) {
        this.context = context;
    }

    public void save(int facilityId, int patientId, Date dateVisit, String clientType, String currentStatus, String pregnancyStatus, String clinicStage, Double lastCd4, Date dateLastCd4, Double lastViralLoad, Date dateLastViralLoad, String eligibleCd4, String eligibleViralLoad,
                     String cotrimEligibility, String ipt, String eligibleIpt, String inh, String tbTreatment, Date dateStartedTbTreatment, String tbReferred, String tbValue1, String tbValue2, String tbValue3, String tbValue4, String tbValue5,
                     Double bodyWeight, Double height, Double bmi, String bmiCategory, Double muac, String muacCategory, String supplementaryFood, String nutritionalStatusReferred, String gbv1, String gbv1Referred, String gbv2, String gbv2Referred, String hypertensive,
                     String firstHypertensive, String bp, String bpAbove, String bpReferred, String diabetic, String firstDiabetic, String dmValue1, String dmValue2, String dmReferred, String phdp1, String phdp1ServicesProvided, String phdp2, String phdp3,
                     String phdp4, String phdp4ServicesProvided, String phdp5, Integer phdp6, Integer phdp7, String phdp7ServicesProvided, String phdp8ServicesProvided, String additionalServicesProvided, String reproductiveIntentions1, String reproductiveIntentions1Referred,
                     String reproductiveIntentions2, String reproductiveIntentions2Referred, String reproductiveIntentions3, String reproductiveIntentions3Referred, String malariaPrevention1, String malariaPrevention1Referred, String malariaPrevention2, String malariaPrevention2Referred) {

        try {
            databaseHelper = DatabaseHelper.getInstance(context);
            SQLiteDatabase db = databaseHelper.getWritableDatabase();

            ContentValues contentValues = new ContentValues();
            contentValues.put("facility_id", facilityId);
            contentValues.put("patient_id", patientId);
            if(dateVisit != null) contentValues.put("date_visit", dateVisit.getTime()); //getTime() returns unix timestamp in milliseconds
            contentValues.put("client_type", clientType);
            contentValues.put("current_status", currentStatus);
            contentValues.put("pregnancy_status", pregnancyStatus);
            contentValues.put("clinic_stage", clinicStage);
            contentValues.put("last_cd4", lastCd4);
            if(dateLastCd4 != null) contentValues.put("date_lastCd4", dateLastCd4.getTime());
            contentValues.put("last_viral_load", lastViralLoad);
            if(dateLastViralLoad != null) contentValues.put("date_last_viral_load", dateLastViralLoad.getTime());
            contentValues.put("eligible_cd4", eligibleCd4);
            contentValues.put("eligible_viral_load", eligibleViralLoad);
            contentValues.put("cotrim_eligibility", cotrimEligibility);
            contentValues.put("ipt", ipt);
            contentValues.put("eligible_ipt", eligibleIpt);
            contentValues.put("inh", inh);
            contentValues.put("tb_treatment", tbTreatment);
            if(dateStartedTbTreatment != null) contentValues.put("date_started_tb_treatment", dateStartedTbTreatment.getTime());
            contentValues.put("tb_referred", tbReferred);
            contentValues.put("tb_value1", tbValue1);
            contentValues.put("tb_value2", tbValue2);
            contentValues.put("tb_value3", tbValue3);
            contentValues.put("tb_value4", tbValue4);
            contentValues.put("tb_value5", tbValue5);
            contentValues.put("body_weight", bodyWeight);
            contentValues.put("height", height);
            contentValues.put("bmi", bmi);
            contentValues.put("bmi_category", bmiCategory);
            contentValues.put("muac", muac);
            contentValues.put("muac_category", muacCategory);
            contentValues.put("supplementary_food", supplementaryFood);
            contentValues.put("nutritional_status_referred", nutritionalStatusReferred);
            contentValues.put("gbv1", gbv1);
            contentValues.put("gbv1_referred", gbv1Referred);
            contentValues.put("gbv2", gbv2);
            contentValues.put("gbv2_referred", gbv2Referred);
            contentValues.put("hypertensive", hypertensive);
            contentValues.put("first_hypertensive", firstHypertensive);
            contentValues.put("bp", bp);
            contentValues.put("bp_above", bpAbove);
            contentValues.put("bp_referred", bpReferred);
            contentValues.put("diabetic", diabetic);
            contentValues.put("first_diabetic", firstDiabetic);
            contentValues.put("dm_value1", dmValue1);
            contentValues.put("dm_value2", dmValue2);
            contentValues.put("dm_referred", dmReferred);
            contentValues.put("phdp1", phdp1);
            contentValues.put("phdp1_services_provided", phdp1ServicesProvided);
            contentValues.put("phdp2", phdp2);
            contentValues.put("phdp3", phdp3);
            contentValues.put("phdp4", phdp4);
            contentValues.put("phdp4_services_provided", phdp4ServicesProvided);
            contentValues.put("phdp5", phdp5);
            contentValues.put("phdp6", phdp6);
            contentValues.put("phdp7", phdp7);
            contentValues.put("phdp7_services_provided", phdp7ServicesProvided);
            contentValues.put("phdp8_services_provided", phdp8ServicesProvided);
            contentValues.put("additional_services_provided", additionalServicesProvided);
            contentValues.put("reproductive_intentions1", reproductiveIntentions1);
            contentValues.put("reproductive_intentions1_referred", reproductiveIntentions1Referred);
            contentValues.put("reproductive_intentions2", reproductiveIntentions2);
            contentValues.put("reproductive_intentions2_referred", reproductiveIntentions2Referred);
            contentValues.put("reproductive_intentions3", reproductiveIntentions3);
            contentValues.put("reproductive_intentions3_referred", reproductiveIntentions3Referred);
            contentValues.put("malaria_prevention1", malariaPrevention1);
            contentValues.put("malaria_prevention1_referred", malariaPrevention1Referred);
            contentValues.put("malaria_prevention2", malariaPrevention2);
            contentValues.put("malaria_prevention2_referred", malariaPrevention2Referred);
            contentValues.put("time_stamp", new Date().getTime());

            db.insert("CHRONICCARE", null, contentValues);
            db.close();
            Log.v("ChroniccareDAO", "Saving...."+additionalServicesProvided);
        }
        catch (Exception exception) {
            Toast toast = Toast.makeText(context, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }

    }

    public void update(int id, int facilityId, int patientId, Date dateVisit, String clientType, String currentStatus, String pregnancyStatus, String clinicStage, Double lastCd4, Date dateLastCd4, Double lastViralLoad, Date dateLastViralLoad, String eligibleCd4, String eligibleViralLoad,
                       String cotrimEligibility, String ipt, String eligibleIpt, String inh, String tbTreatment, Date dateStartedTbTreatment, String tbReferred, String tbValue1, String tbValue2, String tbValue3, String tbValue4, String tbValue5,
                       Double bodyWeight, Double height, Double bmi, String bmiCategory, Double muac, String muacCategory, String supplementaryFood, String nutritionalStatusReferred, String gbv1, String gbv1Referred, String gbv2, String gbv2Referred, String hypertensive,
                       String firstHypertensive, String bp, String bpAbove, String bpReferred, String diabetic, String firstDiabetic, String dmValue1, String dmValue2, String dmReferred, String phdp1, String phdp1ServicesProvided, String phdp2, String phdp3,
                       String phdp4, String phdp4ServicesProvided, String phdp5, Integer phdp6, Integer phdp7, String phdp7ServicesProvided, String phdp8ServicesProvided, String additionalServicesProvided, String reproductiveIntentions1, String reproductiveIntentions1Referred,
                       String reproductiveIntentions2, String reproductiveIntentions2Referred, String reproductiveIntentions3, String reproductiveIntentions3Referred, String malariaPrevention1, String malariaPrevention1Referred, String malariaPrevention2, String malariaPrevention2Referred) {

        try {
            databaseHelper = DatabaseHelper.getInstance(context);
            SQLiteDatabase db = databaseHelper.getWritableDatabase();

            ContentValues contentValues = new ContentValues();
            contentValues.put("facility_id", facilityId);
            contentValues.put("patient_id", patientId);
            if(dateVisit != null) contentValues.put("date_visit", dateVisit.getTime()); //getTime() returns unix timestamp in milliseconds
            contentValues.put("client_type", clientType);
            contentValues.put("current_status", currentStatus);
            contentValues.put("pregnancy_status", pregnancyStatus);
            contentValues.put("clinic_stage", clinicStage);
            contentValues.put("last_cd4", lastCd4);
            if(dateLastCd4 != null) contentValues.put("date_lastCd4", dateLastCd4.getTime());
            contentValues.put("last_viral_load", lastViralLoad);
            if(dateLastViralLoad != null) contentValues.put("date_last_viral_load", dateLastViralLoad.getTime());
            contentValues.put("eligible_cd4", eligibleCd4);
            contentValues.put("eligible_viral_load", eligibleViralLoad);
            contentValues.put("cotrim_eligibility", cotrimEligibility);
            contentValues.put("ipt", ipt);
            contentValues.put("eligible_ipt", eligibleIpt);
            contentValues.put("inh", inh);
            contentValues.put("tb_treatment", tbTreatment);
            if(dateStartedTbTreatment != null) contentValues.put("date_started_tb_treatment", dateStartedTbTreatment.getTime());
            contentValues.put("tb_referred", tbReferred);
            contentValues.put("tb_value1", tbValue1);
            contentValues.put("tb_value2", tbValue2);
            contentValues.put("tb_value3", tbValue3);
            contentValues.put("tb_value4", tbValue4);
            contentValues.put("tb_value5", tbValue5);
            contentValues.put("body_weight", bodyWeight);
            contentValues.put("height", height);
            contentValues.put("bmi", bmi);
            contentValues.put("bmi_category", bmiCategory);
            contentValues.put("muac", muac);
            contentValues.put("muac_category", muacCategory);
            contentValues.put("supplementary_food", supplementaryFood);
            contentValues.put("nutritional_status_referred", nutritionalStatusReferred);
            contentValues.put("gbv1", gbv1);
            contentValues.put("gbv1_referred", gbv1Referred);
            contentValues.put("gbv2", gbv2);
            contentValues.put("gbv2_referred", gbv2Referred);
            contentValues.put("hypertensive", hypertensive);
            contentValues.put("first_hypertensive", firstHypertensive);
            contentValues.put("bp", bp);
            contentValues.put("bp_above", bpAbove);
            contentValues.put("bp_referred", bpReferred);
            contentValues.put("diabetic", diabetic);
            contentValues.put("first_diabetic", firstDiabetic);
            contentValues.put("dm_value1", dmValue1);
            contentValues.put("dm_value2", dmValue2);
            contentValues.put("dm_referred", dmReferred);
            contentValues.put("phdp1", phdp1);
            contentValues.put("phdp1_services_provided", phdp1ServicesProvided);
            contentValues.put("phdp2", phdp2);
            contentValues.put("phdp3", phdp3);
            contentValues.put("phdp4", phdp4);
            contentValues.put("phdp4_services_provided", phdp4ServicesProvided);
            contentValues.put("phdp5", phdp5);
            contentValues.put("phdp6", phdp6);
            contentValues.put("phdp7", phdp7);
            contentValues.put("phdp7_services_provided", phdp7ServicesProvided);
            contentValues.put("phdp8_services_provided", phdp8ServicesProvided);
            contentValues.put("additional_services_provided", additionalServicesProvided);
            contentValues.put("reproductive_intentions1", reproductiveIntentions1);
            contentValues.put("reproductive_intentions1_referred", reproductiveIntentions1Referred);
            contentValues.put("reproductive_intentions2", reproductiveIntentions2);
            contentValues.put("reproductive_intentions2_referred", reproductiveIntentions2Referred);
            contentValues.put("reproductive_intentions3", reproductiveIntentions3);
            contentValues.put("reproductive_intentions3_referred", reproductiveIntentions3Referred);
            contentValues.put("malaria_prevention1", malariaPrevention1);
            contentValues.put("malaria_prevention1_referred", malariaPrevention1Referred);
            contentValues.put("malaria_prevention2", malariaPrevention2);
            contentValues.put("malaria_prevention2_referred", malariaPrevention2Referred);
            contentValues.put("time_stamp", new Date().getTime());

            db.update("CHRONICCARE", contentValues, "_id = ?", new String[] {Integer.toString(id)});
            db.close();
            Log.v("ChroniccareDAO", "Updating...."+additionalServicesProvided);
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
            db.delete("CHRONICCARE", "_id = ?", new String[] {Integer.toString(id)});
            db.close();
        }
        catch (SQLiteException exception) {
            exception.printStackTrace();
        }
    }

    public int getId(int facilityId, int patientId, Date dateVisit) {
        int id = 0;
        try {
            databaseHelper = DatabaseHelper.getInstance(context);
            SQLiteDatabase db = databaseHelper.getReadableDatabase();

            Cursor cursor = db.query("CHRONICCARE",
                    new String[]{"_id"},
                    "facility_id = ? AND patient_id = ? AND date_visit = ?", new String[] {Integer.toString(facilityId), Integer.toString(patientId), Long.toString(dateVisit.getTime())}, null, null, null);
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

    //Retrieve all Chroniccare records
    public ArrayList<Chroniccare> getChroniccares() {
        ArrayList<Chroniccare> chroniccares = new ArrayList<Chroniccare>();
        try {
            databaseHelper = DatabaseHelper.getInstance(context);
            SQLiteDatabase db = databaseHelper.getReadableDatabase();

            Cursor cursor = db.query("CHRONICCARE", null,
                    null, null, null, null, "date_visit DESC");

            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(0);
                    int facilityId = cursor.getInt(1);
                    int patientId = cursor.getInt(2);
                    Date dateVisit  = DateUtil.unixTimestampToDate(cursor.getLong(3), "dd/MM/yyyy");
                    String clientType = cursor.getString(4);
                    String currentStatus = cursor.getString(5);
                    String clinicStage = cursor.getString(6);
                    String pregnancyStatus = cursor.getString(7);
                    int lastCd4 = cursor.getInt(8);
                    Date dateLastCd4  = DateUtil.unixTimestampToDate(cursor.getLong(9), "dd/MM/yyyy");
                    int lastViralLoad = cursor.getInt(10);
                    Date dateLastViralLoad  = DateUtil.unixTimestampToDate(cursor.getLong(11), "dd/MM/yyyy");
                    String eligibleCd4 = cursor.getString(12);
                    String eligibleViralLoad = cursor.getString(13);
                    String cotrimEligibility = cursor.getString(14);
                    String ipt = cursor.getString(15);
                    String eligibleIpt = cursor.getString(16);
                    String inh = cursor.getString(17);
                    String tbTreatment = cursor.getString(18);
                    Date dateStartedTbTreatment  = DateUtil.unixTimestampToDate(cursor.getLong(19), "dd/MM/yyyy");
                    String tbReferred = cursor.getString(20);
                    String tbValue1 = cursor.getString(21);
                    String tbValue2 = cursor.getString(22);
                    String tbValue3 = cursor.getString(23);
                    String tbValue4 = cursor.getString(24);
                    String tbValue5 = cursor.getString(25);
                    double bodyWeight = cursor.getDouble(26);
                    double height = cursor.getDouble(27);
                    double bmi = cursor.getDouble(28);
                    String bmiCategory = cursor.getString(29);
                    double muac = cursor.getDouble(30);
                    String muacCategory = cursor.getString(31);
                    String supplementaryFood = cursor.getString(32);
                    String nutritionalStatusReferred = cursor.getString(33);
                    String gbv1 = cursor.getString(34);
                    String gbv1Referred = cursor.getString(35);
                    String gbv2 = cursor.getString(36);
                    String gbv2Referred = cursor.getString(37);
                    String hypertensive = cursor.getString(38);
                    String firstHypertensive = cursor.getString(39);
                    String bp = cursor.getString(40);
                    String bpAbove = cursor.getString(41);
                    String bpReferred = cursor.getString(42);
                    String diabetic = cursor.getString(43);
                    String firstDiabetic = cursor.getString(44);
                    String dmValue1 = cursor.getString(45);
                    String dmValue2 = cursor.getString(46);
                    String dmReferred = cursor.getString(47);
                    String phdp1 = cursor.getString(48);
                    String phdp1ServicesProvided = cursor.getString(49);
                    String phdp2 = cursor.getString(50);
                    String phdp3 = cursor.getString(51);
                    String phdp4 = cursor.getString(52);
                    String phdp4ServicesProvided = cursor.getString(53);
                    String phdp5 = cursor.getString(54);
                    int phdp6 = cursor.getInt(55);
                    int phdp7 = cursor.getInt(56);
                    String phdp7ServicesProvided = cursor.getString(57);
                    String phdp8ServicesProvided = cursor.getString(58);
                    String additionalServicesProvided = cursor.getString(59);
                    String reproductiveIntentions1 = cursor.getString(60);
                    String reproductiveIntentions1Referred = cursor.getString(61);
                    String reproductiveIntentions2 = cursor.getString(62);
                    String reproductiveIntentions2Referred = cursor.getString(63);
                    String reproductiveIntentions3 = cursor.getString(64);
                    String reproductiveIntentions3Referred = cursor.getString(65);

                    String malariaPrevention1 = cursor.getString(66);
                    String malariaPrevention1Referred = cursor.getString(67);
                    String malariaPrevention2 = cursor.getString(68);
                    String malariaPrevention2Referred = cursor.getString(69);

                    Chroniccare chroniccare = new Chroniccare();
                    chroniccare.setId(id);
                    chroniccare.setFacilityId(facilityId);
                    chroniccare.setPatientId(patientId);
                    chroniccare.setDateVisit(dateVisit);
                    chroniccare.setClientType(clientType);
                    chroniccare.setCurrentStatus(currentStatus);
                    chroniccare.setPregnancyStatus(pregnancyStatus);
                    chroniccare.setClinicStage(clinicStage);
                    chroniccare.setLastCd4(lastCd4);
                    chroniccare.setDateLastCd4(dateLastCd4);
                    chroniccare.setLastViralLoad(lastViralLoad);
                    chroniccare.setDateLastViralLoad(dateLastViralLoad);
                    chroniccare.setEligibleViralLoad(eligibleViralLoad);
                    chroniccare.setEligibleCd4(eligibleCd4);
                    chroniccare.setCotrimEligibility(cotrimEligibility);
                    chroniccare.setIpt(ipt);
                    chroniccare.setInh(inh);
                    chroniccare.setTbTreatment(tbTreatment);
                    chroniccare.setTbReferred(tbReferred);
                    chroniccare.setDateStartedTbTreatment(dateStartedTbTreatment);
                    chroniccare.setEligibleIpt(eligibleIpt);
                    chroniccare.setTbValue1(tbValue1);
                    chroniccare.setTbValue2(tbValue2);
                    chroniccare.setTbValue3(tbValue3);
                    chroniccare.setTbValue4(tbValue4);
                    chroniccare.setTbValue5(tbValue5);
                    chroniccare.setBodyWeight(bodyWeight);
                    chroniccare.setHeight(height);
                    chroniccare.setBmi(bmi);
                    chroniccare.setBmiCategory(bmiCategory);
                    chroniccare.setMuac(muac);
                    chroniccare.setMuacCategory(muacCategory);
                    chroniccare.setSupplementaryFood(supplementaryFood);
                    chroniccare.setNutritionalStatusReferred(nutritionalStatusReferred);
                    chroniccare.setGbv1(gbv1);
                    chroniccare.setGbv1Referred(gbv1Referred);
                    chroniccare.setGbv2(gbv2);
                    chroniccare.setGbv2Referred(gbv2Referred);
                    chroniccare.setHypertensive(hypertensive);
                    chroniccare.setFirstHypertensive(firstHypertensive);
                    chroniccare.setBp(bp);
                    chroniccare.setBpAbove(bpAbove);
                    chroniccare.setBpReferred(bpReferred);
                    chroniccare.setDiabetic(diabetic);
                    chroniccare.setFirstDiabetic(firstDiabetic);
                    chroniccare.setDmReferred(dmReferred);
                    chroniccare.setDmValue1(dmValue1);
                    chroniccare.setDmValue2(dmValue2);
                    chroniccare.setPhdp1(phdp1);
                    chroniccare.setPhdp1ServicesProvided(phdp1ServicesProvided);
                    chroniccare.setPhdp2(phdp2);
                    chroniccare.setPhdp3(phdp3);
                    chroniccare.setPhdp4(phdp4);
                    chroniccare.setPhdp4ServicesProvided(phdp4ServicesProvided);
                    chroniccare.setPhdp5(phdp5);
                    chroniccare.setPhdp6(phdp6);
                    chroniccare.setPhdp7(phdp7);
                    chroniccare.setPhdp7ServicesProvided(phdp7ServicesProvided);
                    chroniccare.setPhdp8ServicesProvided(phdp8ServicesProvided);
                    chroniccare.setAdditionalServicesProvided(additionalServicesProvided);
                    chroniccare.setReproductiveIntentions1(reproductiveIntentions1);
                    chroniccare.setReproductiveIntentions1Referred(reproductiveIntentions1Referred);
                    chroniccare.setReproductiveIntentions2(reproductiveIntentions2);
                    chroniccare.setReproductiveIntentions2Referred(reproductiveIntentions2Referred);
                    chroniccare.setReproductiveIntentions3(reproductiveIntentions3);
                    chroniccare.setReproductiveIntentions3Referred(reproductiveIntentions3Referred);
                    chroniccare.setMalariaPrevention1(malariaPrevention1);
                    chroniccare.setMalariaPrevention1Referred(malariaPrevention1Referred);
                    chroniccare.setMalariaPrevention2(malariaPrevention2);
                    chroniccare.setMalariaPrevention2Referred(malariaPrevention2Referred);

                    chroniccares.add(chroniccare);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        }
        catch (SQLiteException exception) {
            Toast toast = Toast.makeText(context, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
        return chroniccares;
    }

    public ArrayList<Chroniccare> getChroniccares(int facilityId, int patientId) {
        ArrayList<Chroniccare> chroniccares = new ArrayList<Chroniccare>();
        try {
            databaseHelper = DatabaseHelper.getInstance(context);
            SQLiteDatabase db = databaseHelper.getReadableDatabase();

            Cursor cursor = db.query("CHRONICCARE", null,
                    "facility_id = ? AND patient_id = ?", new String[] {Integer.toString(facilityId), Integer.toString(patientId)}, null, null, "date_visit DESC");

            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(0);
                    Date dateVisit  = DateUtil.unixTimestampToDate(cursor.getLong(3), "dd/MM/yyyy");
                    String clientType = cursor.getString(4);
                    String currentStatus = cursor.getString(5);
                    String clinicStage = cursor.getString(6);
                    String pregnancyStatus = cursor.getString(7);
                    int lastCd4 = cursor.getInt(8);
                    Date dateLastCd4  = DateUtil.unixTimestampToDate(cursor.getLong(9), "dd/MM/yyyy");
                    int lastViralLoad = cursor.getInt(10);
                    Date dateLastViralLoad  = DateUtil.unixTimestampToDate(cursor.getLong(11), "dd/MM/yyyy");
                    String eligibleCd4 = cursor.getString(12);
                    String eligibleViralLoad = cursor.getString(13);
                    String cotrimEligibility = cursor.getString(14);
                    String ipt = cursor.getString(15);
                    String eligibleIpt = cursor.getString(16);
                    String inh = cursor.getString(17);
                    String tbTreatment = cursor.getString(18);
                    Date dateStartedTbTreatment  = DateUtil.unixTimestampToDate(cursor.getLong(19), "dd/MM/yyyy");
                    String tbReferred = cursor.getString(20);
                    String tbValue1 = cursor.getString(21);
                    String tbValue2 = cursor.getString(22);
                    String tbValue3 = cursor.getString(23);
                    String tbValue4 = cursor.getString(24);
                    String tbValue5 = cursor.getString(25);
                    double bodyWeight = cursor.getDouble(26);
                    double height = cursor.getDouble(27);
                    double bmi = cursor.getDouble(28);
                    String bmiCategory = cursor.getString(29);
                    double muac = cursor.getDouble(30);
                    String muacCategory = cursor.getString(31);
                    String supplementaryFood = cursor.getString(32);
                    String nutritionalStatusReferred = cursor.getString(33);
                    String gbv1 = cursor.getString(34);
                    String gbv1Referred = cursor.getString(35);
                    String gbv2 = cursor.getString(36);
                    String gbv2Referred = cursor.getString(37);
                    String hypertensive = cursor.getString(38);
                    String firstHypertensive = cursor.getString(39);
                    String bp = cursor.getString(40);
                    String bpAbove = cursor.getString(41);
                    String bpReferred = cursor.getString(42);
                    String diabetic = cursor.getString(43);
                    String firstDiabetic = cursor.getString(44);
                    String dmValue1 = cursor.getString(45);
                    String dmValue2 = cursor.getString(46);
                    String dmReferred = cursor.getString(47);
                    String phdp1 = cursor.getString(48);
                    String phdp1ServicesProvided = cursor.getString(49);
                    String phdp2 = cursor.getString(50);
                    String phdp3 = cursor.getString(51);
                    String phdp4 = cursor.getString(52);
                    String phdp4ServicesProvided = cursor.getString(53);
                    String phdp5 = cursor.getString(54);
                    int phdp6 = cursor.getInt(55);
                    int phdp7 = cursor.getInt(56);
                    String phdp7ServicesProvided = cursor.getString(57);
                    String phdp8ServicesProvided = cursor.getString(58);
                    String additionalServicesProvided = cursor.getString(59);
                    String reproductiveIntentions1 = cursor.getString(60);
                    String reproductiveIntentions1Referred = cursor.getString(61);
                    String reproductiveIntentions2 = cursor.getString(62);
                    String reproductiveIntentions2Referred = cursor.getString(63);
                    String reproductiveIntentions3 = cursor.getString(64);
                    String reproductiveIntentions3Referred = cursor.getString(65);
                    String malariaPrevention1 = cursor.getString(66);
                    String malariaPrevention1Referred = cursor.getString(67);
                    String malariaPrevention2 = cursor.getString(68);
                    String malariaPrevention2Referred = cursor.getString(69);

                    Chroniccare chroniccare = new Chroniccare();
                    chroniccare.setId(id);
                    chroniccare.setFacilityId(facilityId);
                    chroniccare.setPatientId(patientId);
                    chroniccare.setDateVisit(dateVisit);
                    chroniccare.setClientType(clientType);
                    chroniccare.setCurrentStatus(currentStatus);
                    chroniccare.setPregnancyStatus(pregnancyStatus);
                    chroniccare.setClinicStage(clinicStage);
                    chroniccare.setLastCd4(lastCd4);
                    chroniccare.setDateLastCd4(dateLastCd4);
                    chroniccare.setLastViralLoad(lastViralLoad);
                    chroniccare.setDateLastViralLoad(dateLastViralLoad);
                    chroniccare.setEligibleViralLoad(eligibleViralLoad);
                    chroniccare.setEligibleCd4(eligibleCd4);
                    chroniccare.setCotrimEligibility(cotrimEligibility);
                    chroniccare.setIpt(ipt);
                    chroniccare.setInh(inh);
                    chroniccare.setTbTreatment(tbTreatment);
                    chroniccare.setTbReferred(tbReferred);
                    chroniccare.setDateStartedTbTreatment(dateStartedTbTreatment);
                    chroniccare.setEligibleIpt(eligibleIpt);
                    chroniccare.setTbValue1(tbValue1);
                    chroniccare.setTbValue2(tbValue2);
                    chroniccare.setTbValue3(tbValue3);
                    chroniccare.setTbValue4(tbValue4);
                    chroniccare.setTbValue5(tbValue5);
                    chroniccare.setBodyWeight(bodyWeight);
                    chroniccare.setHeight(height);
                    chroniccare.setBmi(bmi);
                    chroniccare.setBmiCategory(bmiCategory);
                    chroniccare.setMuac(muac);
                    chroniccare.setMuacCategory(muacCategory);
                    chroniccare.setSupplementaryFood(supplementaryFood);
                    chroniccare.setNutritionalStatusReferred(nutritionalStatusReferred);
                    chroniccare.setGbv1(gbv1);
                    chroniccare.setGbv1Referred(gbv1Referred);
                    chroniccare.setGbv2(gbv2);
                    chroniccare.setGbv2Referred(gbv2Referred);
                    chroniccare.setHypertensive(hypertensive);
                    chroniccare.setFirstHypertensive(firstHypertensive);
                    chroniccare.setBp(bp);
                    chroniccare.setBpAbove(bpAbove);
                    chroniccare.setBpReferred(bpReferred);
                    chroniccare.setDiabetic(diabetic);
                    chroniccare.setFirstDiabetic(firstDiabetic);
                    chroniccare.setDmReferred(dmReferred);
                    chroniccare.setDmValue1(dmValue1);
                    chroniccare.setDmValue2(dmValue2);
                    chroniccare.setPhdp1(phdp1);
                    chroniccare.setPhdp1ServicesProvided(phdp1ServicesProvided);
                    chroniccare.setPhdp2(phdp2);
                    chroniccare.setPhdp4ServicesProvided(phdp4ServicesProvided);
                    chroniccare.setPhdp3(phdp3);
                    chroniccare.setPhdp4(phdp4);
                    chroniccare.setPhdp5(phdp5);
                    chroniccare.setPhdp6(phdp6);
                    chroniccare.setPhdp7(phdp7);
                    chroniccare.setPhdp7ServicesProvided(phdp7ServicesProvided);
                    chroniccare.setPhdp8ServicesProvided(phdp8ServicesProvided);
                    chroniccare.setAdditionalServicesProvided(additionalServicesProvided);
                    chroniccare.setReproductiveIntentions1(reproductiveIntentions1);
                    chroniccare.setReproductiveIntentions1Referred(reproductiveIntentions1Referred);
                    chroniccare.setReproductiveIntentions2(reproductiveIntentions2);
                    chroniccare.setReproductiveIntentions2Referred(reproductiveIntentions2Referred);
                    chroniccare.setReproductiveIntentions3(reproductiveIntentions3);
                    chroniccare.setReproductiveIntentions3Referred(reproductiveIntentions3Referred);
                    chroniccare.setMalariaPrevention1(malariaPrevention1);
                    chroniccare.setMalariaPrevention1Referred(malariaPrevention1Referred);
                    chroniccare.setMalariaPrevention2(malariaPrevention2);
                    chroniccare.setMalariaPrevention2Referred(malariaPrevention2Referred);

                    chroniccares.add(chroniccare);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        }
        catch (SQLiteException exception) {
            Toast toast = Toast.makeText(context, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
        return chroniccares;
    }

    public Chroniccare getChroniccare(int id) {
        Chroniccare chroniccare = new Chroniccare();
        try {
            databaseHelper = DatabaseHelper.getInstance(context);
            SQLiteDatabase db = databaseHelper.getReadableDatabase();

            Cursor cursor = db.query("CHRONICCARE", null,
                    "_id = ?", new String[] {Integer.toString(id)}, null, null, "date_visit DESC");

            if (cursor.moveToFirst()) {
                do {
                    int facilityId = cursor.getInt(1);
                    int patientId = cursor.getInt(2);
                    Date dateVisit  = DateUtil.unixTimestampToDate(cursor.getLong(3), "dd/MM/yyyy");
                    String clientType = cursor.getString(4);
                    String currentStatus = cursor.getString(5);
                    String clinicStage = cursor.getString(6);
                    String pregnancyStatus = cursor.getString(7);
                    int lastCd4 = cursor.getInt(8);
                    Date dateLastCd4  = DateUtil.unixTimestampToDate(cursor.getLong(9), "dd/MM/yyyy");
                    int lastViralLoad = cursor.getInt(10);
                    Date dateLastViralLoad  = DateUtil.unixTimestampToDate(cursor.getLong(11), "dd/MM/yyyy");
                    String eligibleCd4 = cursor.getString(12);
                    String eligibleViralLoad = cursor.getString(13);
                    String cotrimEligibility = cursor.getString(14);
                    String ipt = cursor.getString(15);
                    String eligibleIpt = cursor.getString(16);
                    String inh = cursor.getString(17);
                    String tbTreatment = cursor.getString(18);
                    Date dateStartedTbTreatment  = DateUtil.unixTimestampToDate(cursor.getLong(19), "dd/MM/yyyy");
                    String tbReferred = cursor.getString(20);
                    String tbValue1 = cursor.getString(21);
                    String tbValue2 = cursor.getString(22);
                    String tbValue3 = cursor.getString(23);
                    String tbValue4 = cursor.getString(24);
                    String tbValue5 = cursor.getString(25);
                    double bodyWeight = cursor.getDouble(26);
                    double height = cursor.getDouble(27);
                    double bmi = cursor.getDouble(28);
                    String bmiCategory = cursor.getString(29);
                    double muac = cursor.getDouble(30);
                    String muacCategory = cursor.getString(31);
                    String supplementaryFood = cursor.getString(32);
                    String nutritionalStatusReferred = cursor.getString(33);
                    String gbv1 = cursor.getString(34);
                    String gbv1Referred = cursor.getString(35);
                    String gbv2 = cursor.getString(36);
                    String gbv2Referred = cursor.getString(37);
                    String hypertensive = cursor.getString(38);
                    String firstHypertensive = cursor.getString(39);
                    String bp = cursor.getString(40);
                    String bpAbove = cursor.getString(41);
                    String bpReferred = cursor.getString(42);
                    String diabetic = cursor.getString(43);
                    String firstDiabetic = cursor.getString(44);
                    String dmValue1 = cursor.getString(45);
                    String dmValue2 = cursor.getString(46);
                    String dmReferred = cursor.getString(47);
                    String phdp1 = cursor.getString(48);
                    String phdp1ServicesProvided = cursor.getString(49);
                    String phdp2 = cursor.getString(50);
                    String phdp3 = cursor.getString(51);
                    String phdp4 = cursor.getString(52);
                    String phdp4ServicesProvided = cursor.getString(53);
                    String phdp5 = cursor.getString(54);
                    int phdp6 = cursor.getInt(55);
                    int phdp7 = cursor.getInt(56);
                    String phdp7ServicesProvided = cursor.getString(57);
                    String phdp8ServicesProvided = cursor.getString(58);
                    String additionalServicesProvided = cursor.getString(59);
                    String reproductiveIntentions1 = cursor.getString(60);
                    String reproductiveIntentions1Referred = cursor.getString(61);
                    String reproductiveIntentions2 = cursor.getString(62);
                    String reproductiveIntentions2Referred = cursor.getString(63);
                    String reproductiveIntentions3 = cursor.getString(64);
                    String reproductiveIntentions3Referred = cursor.getString(65);
                    String malariaPrevention1 = cursor.getString(66);
                    String malariaPrevention1Referred = cursor.getString(67);
                    String malariaPrevention2 = cursor.getString(68);
                    String malariaPrevention2Referred = cursor.getString(69);

                    chroniccare.setId(id);
                    chroniccare.setFacilityId(facilityId);
                    chroniccare.setPatientId(patientId);
                    chroniccare.setDateVisit(dateVisit);
                    chroniccare.setClientType(clientType);
                    chroniccare.setCurrentStatus(currentStatus);
                    chroniccare.setPregnancyStatus(pregnancyStatus);
                    chroniccare.setClinicStage(clinicStage);
                    chroniccare.setLastCd4(lastCd4);
                    chroniccare.setDateLastCd4(dateLastCd4);
                    chroniccare.setLastViralLoad(lastViralLoad);
                    chroniccare.setDateLastViralLoad(dateLastViralLoad);
                    chroniccare.setEligibleViralLoad(eligibleViralLoad);
                    chroniccare.setEligibleCd4(eligibleCd4);
                    chroniccare.setCotrimEligibility(cotrimEligibility);
                    chroniccare.setIpt(ipt);
                    chroniccare.setInh(inh);
                    chroniccare.setTbTreatment(tbTreatment);
                    chroniccare.setTbReferred(tbReferred);
                    chroniccare.setDateStartedTbTreatment(dateStartedTbTreatment);
                    chroniccare.setEligibleIpt(eligibleIpt);
                    chroniccare.setTbValue1(tbValue1);
                    chroniccare.setTbValue2(tbValue2);
                    chroniccare.setTbValue3(tbValue3);
                    chroniccare.setTbValue4(tbValue4);
                    chroniccare.setTbValue5(tbValue5);
                    chroniccare.setBodyWeight(bodyWeight);
                    chroniccare.setHeight(height);
                    chroniccare.setBmi(bmi);
                    chroniccare.setBmiCategory(bmiCategory);
                    chroniccare.setMuac(muac);
                    chroniccare.setMuacCategory(muacCategory);
                    chroniccare.setSupplementaryFood(supplementaryFood);
                    chroniccare.setNutritionalStatusReferred(nutritionalStatusReferred);
                    chroniccare.setGbv1(gbv1);
                    chroniccare.setGbv1Referred(gbv1Referred);
                    chroniccare.setGbv2(gbv2);
                    chroniccare.setGbv2Referred(gbv2Referred);
                    chroniccare.setHypertensive(hypertensive);
                    chroniccare.setFirstHypertensive(firstHypertensive);
                    chroniccare.setBp(bp);
                    chroniccare.setBpAbove(bpAbove);
                    chroniccare.setBpReferred(bpReferred);
                    chroniccare.setDiabetic(diabetic);
                    chroniccare.setFirstDiabetic(firstDiabetic);
                    chroniccare.setDmReferred(dmReferred);
                    chroniccare.setDmValue1(dmValue1);
                    chroniccare.setDmValue2(dmValue2);
                    chroniccare.setPhdp1(phdp1);
                    chroniccare.setPhdp1ServicesProvided(phdp1ServicesProvided);
                    chroniccare.setPhdp2(phdp2);
                    chroniccare.setPhdp4ServicesProvided(phdp4ServicesProvided);
                    chroniccare.setPhdp3(phdp3);
                    chroniccare.setPhdp4(phdp4);
                    chroniccare.setPhdp5(phdp5);
                    chroniccare.setPhdp6(phdp6);
                    chroniccare.setPhdp7(phdp7);
                    chroniccare.setPhdp7ServicesProvided(phdp7ServicesProvided);
                    chroniccare.setPhdp8ServicesProvided(phdp8ServicesProvided);
                    chroniccare.setAdditionalServicesProvided(additionalServicesProvided);
                    chroniccare.setReproductiveIntentions1(reproductiveIntentions1);
                    chroniccare.setReproductiveIntentions1Referred(reproductiveIntentions1Referred);
                    chroniccare.setReproductiveIntentions2(reproductiveIntentions2);
                    chroniccare.setReproductiveIntentions2Referred(reproductiveIntentions2Referred);
                    chroniccare.setReproductiveIntentions3(reproductiveIntentions3);
                    chroniccare.setReproductiveIntentions3Referred(reproductiveIntentions3Referred);
                    chroniccare.setMalariaPrevention1(malariaPrevention1);
                    chroniccare.setMalariaPrevention1Referred(malariaPrevention1Referred);
                    chroniccare.setMalariaPrevention2(malariaPrevention2);
                    chroniccare.setMalariaPrevention2Referred(malariaPrevention2Referred);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        }
        catch (SQLiteException exception) {
            Toast toast = Toast.makeText(context, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
        return chroniccare;
    }

    public JSONArray getContent(long timeLastSync) {
        JSONArray jsonArray = new JSONArray();
        int communitypharmId = new AccountDAO(context).getAccountId();
        try {
            databaseHelper = DatabaseHelper.getInstance(context);
            SQLiteDatabase db = databaseHelper.getReadableDatabase();

            //String query = "SELECT * FROM chroniccare WHERE time_stamp >= " + timeLastSync;
            String query = "SELECT facility_id,patient_id,date_visit,client_type,current_status," +
                    "clinic_stage,pregnancy_status,last_cd4,date_last_cd4,last_viral_load," +
                    "date_last_viral_load,eligible_cd4,eligible_viral_load,cotrim_eligibility," +
                    "ipt,eligible_ipt,inh,tb_treatment,date_started_tb_treatment,tb_referred," +
                    "tb_value1,tb_value2,tb_value3,tb_value4,tb_value5,body_weight,height," +
                    "bmi,bmi_category,muac,muac_category,supplementary_food,nutritional_status_referred," +
                    "gbv1,gbv1_referred,gbv2,gbv2_referred,hypertensive,first_hypertensive,bp," +
                    "bp_above,bp_referred,diabetic,first_diabetic,dm_value1,dm_value2," +
                    "dm_referred,phdp1,phdp1_services_provided,phdp2,phdp3,phdp4,phdp4_services_provided," +
                    "phdp5,phdp6,phdp7,phdp7_services_provided,phdp8_services_provided," +
                    "additional_services_provided,reproductive_intentions1,reproductive_intentions1_referred," +
                    "reproductive_intentions2,reproductive_intentions2_referred,reproductive_intentions3," +
                    "reproductive_intentions3_referred,malaria_prevention1," +
                    "malaria_prevention1_referred,malaria_prevention2,malaria_prevention2_referred " +
                    "FROM chroniccare";
            Cursor cursor = db.rawQuery(query, null);
            int colCount = cursor.getColumnCount();
            while (cursor.moveToNext()) {
                JSONObject rowObject = new JSONObject();
                for(int i = 0; i < colCount; i++) {
                    String columnName = cursor.getColumnName(i);
                    rowObject.put(columnName, unixTimestamp(columnName, cursor.getString(i)));
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

    //If the field is a date field convert from unix timestamp to date string
    private Object unixTimestamp(String columnName, String value) {
        String fields = "date_visit#date_last_cd4#date_last_viral_load#date_started_tb_treatment";
        if(StringUtil.found(columnName, fields)) {
            return value == null? "" : DateUtil.unixTimestampToDateString(Long.parseLong(value), "yyyy-MM-dd"); //dates are stored in database as unix timestamp in milliseconds to convert to seconds divide by 1000
        }
        return value == null? "" : value;
    }

}
