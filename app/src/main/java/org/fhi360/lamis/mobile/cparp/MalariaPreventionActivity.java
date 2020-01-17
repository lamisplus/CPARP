package org.fhi360.lamis.mobile.cparp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;

import org.fhi360.lamis.mobile.cparp.dao.ChroniccareDAO;
import org.fhi360.lamis.mobile.cparp.dao.MonitorDAO;
import org.fhi360.lamis.mobile.cparp.model.Patient;
import org.fhi360.lamis.mobile.cparp.R;
import org.fhi360.lamis.mobile.cparp.utility.DateUtil;
import org.fhi360.lamis.mobile.cparp.utility.SpinnerUtil;

import java.util.Date;

import static org.fhi360.lamis.mobile.cparp.utility.Constants.PREFERENCES_ENCOUNTER;

public class MalariaPreventionActivity extends AppCompatActivity {
    private int id;
    private int patientId;
    private int facilityId;
    private Date dateVisit;
    private String clientType;
    private String currentStatus;
    private String pregnancyStatus;
    private String clinicStage;
    private Double lastCd4;
    private Date dateLastCd4;
    private Double lastViralLoad;
    private Date dateLastViralLoad;
    private String eligibleCd4;
    private String eligibleViralLoad;
    private String cotrimEligibility;
    private String ipt;
    private String eligibleIpt;
    private String inh;
    private String tbTreatment;
    private Date dateStartedTbTreatment;
    private String tbReferred;
    private String tbValue1;
    private String tbValue2;
    private String tbValue3;
    private String tbValue4;
    private String tbValue5;
    private Double bodyWeight;
    private Double height;
    private Double bmi;
    private String bmiCategory;
    private Double muac;
    private String muacCategory;
    private String supplementaryFood;
    private String nutritionalStatusReferred;
    private String gbv1;
    private String gbv1Referred;
    private String gbv2;
    private String gbv2Referred;
    private String hypertensive;
    private String firstHypertensive;
    private String bp;
    private String bpAbove;
    private String bpReferred;
    private String diabetic;
    private String firstDiabetic;
    private String dmValue1;
    private String dmValue2;
    private String dmValue3;
    private String dmReferred;
    private String phdp1;
    private String phdp1ServicesProvided;
    private String phdp2;
    private String phdp3;
    private String phdp4;
    private String phdp4ServicesProvided;
    private String phdp5;
    private Integer phdp6;
    private Integer phdp7;
    private String phdp7ServicesProvided;
    private String phdp8ServicesProvided;
    private String additionalServicesProvided;
    private String reproductiveIntentions1;
    private String reproductiveIntentions1Referred;
    private String reproductiveIntentions2;
    private String reproductiveIntentions2Referred;
    private String reproductiveIntentions3;
    private String reproductiveIntentions3Referred;

    private String malariaPrevention1;
    private String malariaPrevention1Referred;
    private String malariaPrevention2;
    private String malariaPrevention2Referred;

    private Patient patient;
    private boolean EDIT_MODE;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_malaria_prevention);

        this.preferences = getSharedPreferences(PREFERENCES_ENCOUNTER, 0);
        restorePreferences();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Malaria Prevention");
    }

    //Create the menu option
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate the menu; this adds items to the action bar if it is present
        getMenuInflater().inflate(R.menu.menu_chroniccare_end, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                new ChroniccareDAO(this).delete(id);

                //Log delete record in the monitor for replication on the server
                String entityId = Long.toString(patientId) + "#" + dateVisit.toString();
                new MonitorDAO(this).save(facilityId, entityId, "chroniccare", 3);

                Intent intent = new Intent(this, SearchChroniccareActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_new:
                clearPreferences();
                restorePreferences();
                intent = new Intent(this, TbScreeningActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_navigate_backward:
                intent = new Intent(this, ReproductiveIntentionsActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_tb_screening:
                intent = new Intent(this, TbScreeningActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_nutritional_status:
                intent = new Intent(this, NutritionalStatusActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_gender_based_violence:
                intent = new Intent(this, GenderBasedViolenceActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_chronic_condition:
                intent = new Intent(this, ChronicConditionActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_positive_health:
                intent = new Intent(this, PositiveHealthActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_additional_services:
                intent = new Intent(this, AdditionalServicesActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_reproductive_intention:
                intent = new Intent(this, ReproductiveIntentionsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        if(!EDIT_MODE) {
            menu.findItem(R.id.action_delete).setVisible(false);
            menu.findItem(R.id.action_new).setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onResume() {
        super.onResume();
        restorePreferences();
    }

    @Override
    public void onPause() {
        super.onPause();
        savePreferences();
    }

    public void onClickSaveButton(View view) {
        savePreferences();
        restorePreferences();
        if(dateVisit != null) {
            ChroniccareDAO chroniccareDAO = new ChroniccareDAO(this);
            int id = chroniccareDAO.getId(facilityId, patientId, dateVisit);
            if(id != 0 ) {
                chroniccareDAO.update(id, facilityId, patientId, dateVisit, clientType, currentStatus, pregnancyStatus, clinicStage, lastCd4, dateLastCd4, lastViralLoad, dateLastViralLoad, eligibleCd4, eligibleViralLoad,
                        cotrimEligibility, ipt, eligibleIpt, inh, tbTreatment, dateStartedTbTreatment, tbReferred, tbValue1, tbValue2, tbValue3, tbValue4, tbValue5,
                        bodyWeight, height, bmi, bmiCategory, muac, muacCategory, supplementaryFood, nutritionalStatusReferred, gbv1, gbv1Referred, gbv2, gbv2Referred, hypertensive,
                        firstHypertensive, bp, bpAbove, bpReferred, diabetic, firstDiabetic, dmValue1, dmValue2, dmReferred, phdp1, phdp1ServicesProvided, phdp2, phdp3,
                        phdp4, phdp4ServicesProvided, phdp5, phdp6, phdp7, phdp7ServicesProvided, phdp8ServicesProvided, additionalServicesProvided, reproductiveIntentions1, reproductiveIntentions1Referred,
                        reproductiveIntentions2, reproductiveIntentions2Referred, reproductiveIntentions3, reproductiveIntentions3Referred, malariaPrevention1, malariaPrevention1Referred, malariaPrevention2, malariaPrevention2Referred);

                        Toast.makeText(MalariaPreventionActivity.this, "Care and support data updated", Toast.LENGTH_LONG).show();
            }
            else {
                chroniccareDAO.save(facilityId, patientId, dateVisit, clientType, currentStatus, pregnancyStatus, clinicStage, lastCd4, dateLastCd4, lastViralLoad, dateLastViralLoad, eligibleCd4, eligibleViralLoad,
                        cotrimEligibility, ipt, eligibleIpt, inh, tbTreatment, dateStartedTbTreatment, tbReferred, tbValue1, tbValue2, tbValue3, tbValue4, tbValue5,
                        bodyWeight, height, bmi, bmiCategory, muac, muacCategory, supplementaryFood, nutritionalStatusReferred, gbv1, gbv1Referred, gbv2, gbv2Referred, hypertensive,
                        firstHypertensive, bp, bpAbove, bpReferred, diabetic, firstDiabetic, dmValue1, dmValue2, dmReferred, phdp1, phdp1ServicesProvided, phdp2, phdp3,
                        phdp4, phdp4ServicesProvided, phdp5, phdp6, phdp7, phdp7ServicesProvided, phdp8ServicesProvided, additionalServicesProvided, reproductiveIntentions1, reproductiveIntentions1Referred,
                        reproductiveIntentions2, reproductiveIntentions2Referred, reproductiveIntentions3, reproductiveIntentions3Referred, malariaPrevention1, malariaPrevention1Referred, malariaPrevention2, malariaPrevention2Referred);

                Toast.makeText(MalariaPreventionActivity.this, "Care and support data saved", Toast.LENGTH_LONG).show();
            }
            clearPreferences();
            Intent intent = new Intent(this, ClientProfileActivity.class);
            if(EDIT_MODE) intent = new Intent(this, SearchChroniccareActivity.class);
            startActivity(intent);
        }
        else {
            Toast.makeText(MalariaPreventionActivity.this, "Please enter date of encounter", Toast.LENGTH_LONG).show();
        }
    }

    public void savePreferences() {
        Log.v("MalariaActivity", "Saving to shared reference....");
        malariaPrevention1 = String.valueOf(((Spinner) findViewById(R.id.malaria_prevention1)).getSelectedItem());
        malariaPrevention1Referred = String.valueOf(((Spinner) findViewById(R.id.malaria_prevention1_referred)).getSelectedItem());
        malariaPrevention2 = String.valueOf(((Spinner) findViewById(R.id.malaria_prevention2)).getSelectedItem());
        malariaPrevention2Referred = String.valueOf(((Spinner) findViewById(R.id.malaria_prevention2_referred)).getSelectedItem());

        // We need an Editor object to make preference changes. All objects are from android.context.Context
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("malariaPrevention1", malariaPrevention1);
        editor.putString("malariaPrevention1Referred", malariaPrevention1Referred);
        editor.putString("malariaPrevention2", malariaPrevention2);
        editor.putString("malariaPrevention2Referred", malariaPrevention2Referred);
        editor.commit();
    }

    private void restorePreferences() {
        EDIT_MODE = preferences.getBoolean("edit_mode", false);
        String json = preferences.getString("patient", "");
        patient = new Gson().fromJson(json, Patient.class);
        patientId = patient.getPatientId();
        facilityId = patient.getFacilityId();

        id = preferences.getInt("id", 0);
        dateVisit = DateUtil.parseStringToDate(preferences.getString("dateVisit",  ""), "dd/MM/yyyy");         //dateVisit = DateUtil.unixTimestampToDate(preferences.getLong("dateVisit",  new Date().getTime())/1000L);
        //TB screening data
        ipt = preferences.getString("ipt", "");
        eligibleIpt = preferences.getString("eligibleIpt", "");
        inh = preferences.getString("inh", "");
        tbTreatment = preferences.getString("tbTreatment", "");
        dateStartedTbTreatment = DateUtil.parseStringToDate(preferences.getString("dateStartedTbTreatment",  ""), "dd/MM/yyyy");
        tbReferred = preferences.getString("tbReferred", "");
        tbValue1 = preferences.getString("tbValue1", "");
        tbValue2 = preferences.getString("tbValue2", "");
        tbValue3 = preferences.getString("tbValue3", "");
        tbValue4 = preferences.getString("tbValue4", "");
        tbValue5 = preferences.getString("tbValue5", "");
        //Nutritional status data
        String value = preferences.getString("bodyWeight", "");
        bodyWeight = value.trim().isEmpty()? null : Double.parseDouble(value);
        value = preferences.getString("height", "");
        height = value.trim().isEmpty()? null : Double.parseDouble(value);
        value = preferences.getString("bmi", "");
        bmi = value.trim().isEmpty()? null : Double.parseDouble(value);
        bmiCategory =  preferences.getString("bmiCategory", "");
        value = preferences.getString("muac", "");
        muac = value.trim().isEmpty()? null : Double.parseDouble(value);
        muacCategory =  preferences.getString("muacCategory", "");
        supplementaryFood = preferences.getString("supplementaryFood", "");
        nutritionalStatusReferred = preferences.getString("nutritionalStatusReferred", "");

        //Gender based violence data
        gbv1 = preferences.getString("gbv1", "");
        gbv1Referred = preferences.getString("gbv1Referred", "");
        gbv2 = preferences.getString("gbv2", "");
        gbv2Referred = preferences.getString("gbv2Referred", "");
        //Chronic condition data
        hypertensive = preferences.getString("hypertensive", "");
        firstHypertensive = preferences.getString("firstHypertensive", "");
        bp = preferences.getString("bp", "");
        bpAbove = preferences.getString("bpAbove", "");
        bpReferred = preferences.getString("bpReferred", "");
        diabetic = preferences.getString("diabetic", "");
        firstDiabetic = preferences.getString("firstDiabetic", "");
        dmValue1 = preferences.getString("dmValue1", "");
        dmValue2 = preferences.getString("dmValue2", "");
        dmReferred = preferences.getString("dmReferred", "");
        //Positive health data
        phdp1 = preferences.getString("phdp1", "");
        phdp1ServicesProvided = preferences.getString("phdp1ServicesProvided", "");
        phdp2 = preferences.getString("phdp2", "");
        phdp3 = preferences.getString("phdp3", "");
        phdp4 = preferences.getString("phdp4", "");
        phdp4ServicesProvided = preferences.getString("phdp4ServicesProvided", "");
        phdp5 = preferences.getString("phdp5", "");
        value = preferences.getString("phdp6", "");
        phdp6 = value.trim().isEmpty()? null : Integer.parseInt(value);
        value = preferences.getString("phdp7", "");
        phdp7 = value.trim().isEmpty()? null : Integer.parseInt(value);
        phdp7ServicesProvided = preferences.getString("phdp7ServicesProvided", "");
        phdp8ServicesProvided = preferences.getString("phdp8ServicesProvided", "");
        additionalServicesProvided = preferences.getString("additionalServicesProvided", "");
        //Reproductive intentions data
        reproductiveIntentions1 = preferences.getString("reproductiveIntentions1", "");
        reproductiveIntentions1Referred = preferences.getString("reproductiveIntentions1Referred", "");
        reproductiveIntentions2 = preferences.getString("reproductiveIntentions2", "");
        reproductiveIntentions2Referred = preferences.getString("reproductiveIntentions2Referred", "");
        reproductiveIntentions3 = preferences.getString("reproductiveIntentions3", "");
        reproductiveIntentions3Referred = preferences.getString("reproductiveIntentions3Referred", "");
        //Malaria prevention intentions data
        malariaPrevention1 = preferences.getString("malariaPrevention1", "");
        malariaPrevention1Referred = preferences.getString("malariaPrevention1Referred", "");
        malariaPrevention2 = preferences.getString("malariaPrevention2", "");
        malariaPrevention2Referred = preferences.getString("malariaPrevention2Referred", "");

        Spinner spinner = (Spinner) findViewById(R.id.malaria_prevention1);
        spinner.setSelection(SpinnerUtil.getIndex(spinner, malariaPrevention1));
        spinner = (Spinner) findViewById(R.id.malaria_prevention1_referred);
        spinner.setSelection(SpinnerUtil.getIndex(spinner, malariaPrevention1Referred));
        spinner = (Spinner) findViewById(R.id.malaria_prevention2);
        spinner.setSelection(SpinnerUtil.getIndex(spinner, malariaPrevention2Referred));
        spinner = (Spinner) findViewById(R.id.malaria_prevention2_referred);
        spinner.setSelection(SpinnerUtil.getIndex(spinner, malariaPrevention2Referred));
    }

    private void clearPreferences() {
        //Reset application shared preference
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.putBoolean("edit_mode", false);
        editor.putString("patient", new Gson().toJson(patient));
        editor.commit();
    }

}
