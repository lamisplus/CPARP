package org.fhi360.lamis.mobile.cparp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;

import org.fhi360.lamis.mobile.cparp.adapter.ChroniccareListAdapter;
import org.fhi360.lamis.mobile.cparp.dao.ChroniccareDAO;
import org.fhi360.lamis.mobile.cparp.model.Chroniccare;
import org.fhi360.lamis.mobile.cparp.model.Patient;
import org.fhi360.lamis.mobile.cparp.R;
import org.fhi360.lamis.mobile.cparp.utility.DateUtil;

import java.util.ArrayList;

import static org.fhi360.lamis.mobile.cparp.utility.Constants.PREFERENCES_ENCOUNTER;

public class SearchChroniccareActivity extends AppCompatActivity {
    private Cursor cursor;
    private SQLiteDatabase db;
    private ChroniccareListAdapter adapter;
    private ListView careSupportlistView;
    private ArrayList<Chroniccare> chroniccares;

    private Patient patient;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_care_support);

        this.preferences = getSharedPreferences(PREFERENCES_ENCOUNTER, 0);
        String json = preferences.getString("patient", "");
        patient = new Gson().fromJson(json, Patient.class);
        int facilityId = patient.getFacilityId();
        int patientId = patient.getPatientId();

        careSupportlistView = (ListView) findViewById(R.id.careSupportlistView);
        chroniccares = new ChroniccareDAO(this).getChroniccares(facilityId, patientId);
        adapter = new ChroniccareListAdapter(this, chroniccares);
        careSupportlistView.setAdapter(adapter);
        careSupportlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                edit(chroniccares.get(position));
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Search Record");
    }

    @Override
    public void onSaveInstanceState(Bundle saveInstanceState) {
        super.onSaveInstanceState(saveInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    private void edit(Chroniccare chroniccare) {
        preferences = getSharedPreferences(PREFERENCES_ENCOUNTER, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("edit_mode", true);

        editor.putInt("id", chroniccare.getId());
        editor.putString("dateVisit", DateUtil.parseDateToString(chroniccare.getDateVisit(), "dd/MM/yyyy"));
        editor.putString("ipt", chroniccare.getIpt());
        editor.putString("eligibleIpt", chroniccare.getEligibleIpt());
        editor.putString("inh", chroniccare.getInh());
        editor.putString("tbTreatment", chroniccare.getTbTreatment());
        editor.putString("dateStartedTbTreatment", DateUtil.parseDateToString(chroniccare.getDateStartedTbTreatment(), "dd/MM/yyyy"));
        editor.putString("tbReferred", chroniccare.getTbReferred());
        editor.putString("tbValue1",chroniccare.getTbValue1());
        editor.putString("tbValue2", chroniccare.getTbValue2());
        editor.putString("tbValue3", chroniccare.getTbValue3());
        editor.putString("tbValue4", chroniccare.getTbValue4());
        editor.putString("tbValue5", chroniccare.getTbValue5());
        editor.putString("bodyWeight", (Double) chroniccare.getBodyWeight() == null? "" : Double.toString(chroniccare.getBodyWeight()));
        editor.putString("height", (Double) chroniccare.getHeight() == null? "" : Double.toString(chroniccare.getHeight()));
        editor.putString("bmi", (Double) chroniccare.getBmi() == null? "" : Double.toString(chroniccare.getBmi()));
        editor.putString("bmiCategory", chroniccare.getBmiCategory());
        editor.putString("muac", (Double) chroniccare.getMuac() == null? "" : Double.toString(chroniccare.getMuac()));
        editor.putString("muacCategory", chroniccare.getMuacCategory());
        editor.putString("supplementaryFood", chroniccare.getSupplementaryFood());
        editor.putString("nutritionalStatusReferred", chroniccare.getNutritionalStatusReferred());
        editor.putString("gbv1", chroniccare.getGbv1());
        editor.putString("gbv1Referred", chroniccare.getGbv1Referred());
        editor.putString("gbv2", chroniccare.getGbv2());
        editor.putString("gbv2Referred", chroniccare.getGbv2Referred());
        editor.putString("hypertensive", chroniccare.getHypertensive());
        editor.putString("firstHypertensive", chroniccare.getFirstHypertensive());
        editor.putString("bp", chroniccare.getBp());
        editor.putString("bpAbove", chroniccare.getBpAbove());
        editor.putString("bpReferred", chroniccare.getBpReferred());
        editor.putString("diabetic", chroniccare.getDiabetic());
        editor.putString("firstDiabetic", chroniccare.getFirstDiabetic());
        editor.putString("dmValue1", chroniccare.getDmValue1());
        editor.putString("dmValue2", chroniccare.getDmValue2());
        editor.putString("dmReferred", chroniccare.getDmReferred());
        editor.putString("phdp1", chroniccare.getPhdp1());
        editor.putString("phdp1ServicesProvided", chroniccare.getPhdp1ServicesProvided());
        editor.putString("phdp2", chroniccare.getPhdp2());
        editor.putString("phdp3", chroniccare.getPhdp3());
        editor.putString("phdp4", chroniccare.getPhdp4());
        editor.putString("phdp4ServicesProvided", chroniccare.getPhdp4ServicesProvided());
        editor.putString("phdp5", chroniccare.getPhdp5());
        editor.putString("phdp6", (Integer) chroniccare.getPhdp6() == null? "" : Integer.toString(chroniccare.getPhdp6()));
        editor.putString("phdp7", (Integer) chroniccare.getPhdp7() == null? "" : Integer.toString(chroniccare.getPhdp7()));
        editor.putString("phdp7ServicesProvided", chroniccare.getPhdp7ServicesProvided());
        editor.putString("phdp8ServicesProvided", chroniccare.getPhdp8ServicesProvided());

        Log.v("SearchChroniccare", "Restoring additional services...."+chroniccare.getAdditionalServicesProvided());
        editor.putString("additionalServicesProvided", chroniccare.getAdditionalServicesProvided());
        editor.putString("reproductiveIntentions1", chroniccare.getReproductiveIntentions1());
        editor.putString("reproductiveIntentions1Referred", chroniccare.getReproductiveIntentions1Referred());
        editor.putString("reproductiveIntentions2", chroniccare.getReproductiveIntentions2());
        editor.putString("reproductiveIntentions2Referred", chroniccare.getReproductiveIntentions2Referred());
        editor.putString("reproductiveIntentions3", chroniccare.getReproductiveIntentions3());
        editor.putString("reproductiveIntentions3Referred", chroniccare.getReproductiveIntentions3Referred());

        Log.v("SearchChroniccare", "Restoring malaria...."+chroniccare.getMalariaPrevention1());
        editor.putString("malariaPrevention1", chroniccare.getMalariaPrevention1());
        editor.putString("malariaPrevention1Referred", chroniccare.getMalariaPrevention1Referred());
        editor.putString("malariaPrevention2", chroniccare.getMalariaPrevention2());
        editor.putString("malariaPrevention2Referred", chroniccare.getMalariaPrevention2Referred());
        editor.commit();

        Intent intent = new Intent(this, TbScreeningActivity.class);
        startActivity(intent);
    }
}
