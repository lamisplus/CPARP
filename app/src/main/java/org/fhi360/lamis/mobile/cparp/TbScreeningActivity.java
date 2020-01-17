package org.fhi360.lamis.mobile.cparp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.google.gson.Gson;

import org.fhi360.lamis.mobile.cparp.dao.ChroniccareDAO;
import org.fhi360.lamis.mobile.cparp.dao.MonitorDAO;
import org.fhi360.lamis.mobile.cparp.model.Patient;
import org.fhi360.lamis.mobile.cparp.R;
import org.fhi360.lamis.mobile.cparp.utility.DateUtil;
import org.fhi360.lamis.mobile.cparp.utility.EditTextDatePicker;
import org.fhi360.lamis.mobile.cparp.utility.SpinnerUtil;

import java.util.Date;

import static org.fhi360.lamis.mobile.cparp.utility.Constants.PREFERENCES_ENCOUNTER;

public class TbScreeningActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private int id;
    private int facilityId;
    private int patientId;
    private Date dateVisit;
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
    private LinearLayout layoutTbYes;
    private LinearLayout layoutTbNo;
    private LinearLayout layoutOutcomeTb;
    private LinearLayout layoutOutcomeIpt;
    private Spinner tbTreatmentSpinner;
    private Spinner tbValue1Spinner;
    private Spinner tbValue2Spinner;
    private Spinner tbValue3Spinner;
    private Spinner tbValue4Spinner;
    private Spinner tbValue5Spinner;

    private Patient patient;
    private boolean EDIT_MODE;
    private SharedPreferences preferences;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tb_screening);

        this.preferences = getSharedPreferences(PREFERENCES_ENCOUNTER, 0);
        restorePreferences();

        layoutTbYes = (LinearLayout) findViewById(R.id.tb_treatment_yes);
        layoutTbNo = (LinearLayout) findViewById(R.id.tb_treatment_no);
        layoutOutcomeTb = (LinearLayout) findViewById(R.id.outcome_tb);
        layoutOutcomeIpt = (LinearLayout) findViewById(R.id.outcome_ipt);

        tbTreatmentSpinner = (Spinner) findViewById(R.id.tb_treatment);
        tbTreatmentSpinner.setOnItemSelectedListener(this);
        tbValue1Spinner = (Spinner) findViewById(R.id.tb_value1);
        tbValue1Spinner.setOnItemSelectedListener(this);
        tbValue2Spinner = (Spinner) findViewById(R.id.tb_value2);
        tbValue2Spinner.setOnItemSelectedListener(this);
        tbValue3Spinner = (Spinner) findViewById(R.id.tb_value3);
        tbValue3Spinner.setOnItemSelectedListener(this);
        tbValue4Spinner = (Spinner) findViewById(R.id.tb_value4);
        tbValue4Spinner.setOnItemSelectedListener(this);
        tbValue5Spinner = (Spinner) findViewById(R.id.tb_value5);
        tbValue5Spinner.setOnItemSelectedListener(this);

        //Set date picker on the edit text
        new EditTextDatePicker(this, (EditText) findViewById(R.id.date_visit));
        new EditTextDatePicker(this, (EditText) findViewById(R.id.date_started_tb_treatment));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("TB Screening");
    }

    //Create the menu option
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate the menu; this adds items to the action bar if it is present
        getMenuInflater().inflate(R.menu.menu_chroniccare_begin, menu);
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
            case R.id.action_navigate_forward:
                intent = new Intent(this, NutritionalStatusActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_navigate_backward:
                intent = new Intent(this, ClientProfileActivity.class);
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
            case R.id.action_malaria_prevention:
                intent = new Intent(this, MalariaPreventionActivity.class);
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

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        String tbTreatment = String.valueOf(tbTreatmentSpinner.getSelectedItem());
        if(tbTreatment.isEmpty()) {
            layoutTbYes.setVisibility(View.GONE);
            layoutTbNo.setVisibility(View.GONE);
            layoutOutcomeTb.setVisibility(View.GONE);
            layoutOutcomeIpt.setVisibility(View.GONE);
        }
        else {
            if(tbTreatment.equalsIgnoreCase("YES")) {
                layoutTbYes.setVisibility(View.VISIBLE);
                layoutTbNo.setVisibility(View.GONE);
                layoutOutcomeTb.setVisibility(View.GONE);
                layoutOutcomeIpt.setVisibility(View.GONE);
            }
            else {
                layoutTbYes.setVisibility(View.GONE);
                layoutTbNo.setVisibility(View.VISIBLE);

                tbValue1 = String.valueOf(((Spinner) findViewById(R.id.tb_value1)).getSelectedItem());
                tbValue2 = String.valueOf(((Spinner) findViewById(R.id.tb_value2)).getSelectedItem());
                tbValue3 = String.valueOf(((Spinner) findViewById(R.id.tb_value3)).getSelectedItem());
                tbValue4 = String.valueOf(((Spinner) findViewById(R.id.tb_value4)).getSelectedItem());
                tbValue5 = String.valueOf(((Spinner) findViewById(R.id.tb_value5)).getSelectedItem());
                if(tbValue1.isEmpty() && tbValue2.isEmpty() && tbValue3.isEmpty() && tbValue4.isEmpty() && tbValue5.isEmpty()) {
                    layoutOutcomeTb.setVisibility(View.GONE);
                    layoutOutcomeIpt.setVisibility(View.GONE);
                }
                else {
                    if(tbValue1.equalsIgnoreCase("YES") || tbValue2.equalsIgnoreCase("YES") || tbValue3.equalsIgnoreCase("YES") || tbValue4.equalsIgnoreCase("YES") || tbValue5.equalsIgnoreCase("YES")) {
                        layoutOutcomeTb.setVisibility(View.VISIBLE);
                        layoutOutcomeIpt.setVisibility(View.GONE);
                    }
                    else {
                        layoutOutcomeTb.setVisibility(View.GONE);
                        layoutOutcomeIpt.setVisibility(View.VISIBLE);
                    }

                }
            }
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
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


    public void savePreferences() {
        dateVisit = DateUtil.parseStringToDate(((EditText) findViewById(R.id.date_visit)).getText().toString(), "dd/MM/yyyy");
       ipt = String.valueOf(((Spinner) findViewById(R.id.ipt)).getSelectedItem());
        eligibleIpt = String.valueOf(((Spinner) findViewById(R.id.eligible_ipt)).getSelectedItem());
        tbTreatment = String.valueOf(((Spinner) findViewById(R.id.tb_treatment)).getSelectedItem());
        dateStartedTbTreatment = DateUtil.parseStringToDate(((EditText) findViewById(R.id.date_started_tb_treatment)).getText().toString(), "dd/MM/yyyy");
        tbReferred = String.valueOf(((Spinner) findViewById(R.id.tb_referred)).getSelectedItem());
        tbValue1 = String.valueOf(((Spinner) findViewById(R.id.tb_value1)).getSelectedItem());
        tbValue2 = String.valueOf(((Spinner) findViewById(R.id.tb_value2)).getSelectedItem());
        tbValue3 = String.valueOf(((Spinner) findViewById(R.id.tb_value3)).getSelectedItem());
        tbValue4 = String.valueOf(((Spinner) findViewById(R.id.tb_value4)).getSelectedItem());
        tbValue5 = String.valueOf(((Spinner) findViewById(R.id.tb_value5)).getSelectedItem());

        // We need an Editor object to make preference changes. All objects are from android.context.Context
        preferences = getSharedPreferences(PREFERENCES_ENCOUNTER, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("dateVisit", DateUtil.parseDateToString(dateVisit, "dd/MM/yyyy"));
        editor.putString("ipt", ipt);
        editor.putString("eligibleIpt", eligibleIpt);
        editor.putString("inh", inh);
        editor.putString("tbTreatment", tbTreatment);
        editor.putString("dateStartedTbTreatment", DateUtil.parseDateToString(dateStartedTbTreatment, "dd/MM/yyyy"));
        editor.putString("tbReferred", tbReferred);
        editor.putString("tbValue1", tbValue1);
        editor.putString("tbValue2", tbValue2);
        editor.putString("tbValue3", tbValue3);
        editor.putString("tbValue4", tbValue4);
        editor.putString("tbValue5", tbValue5);
        editor.commit();
    }

    public void restorePreferences() {
        EDIT_MODE = preferences.getBoolean("edit_mode", false);
        String json = preferences.getString("patient", "");
        patient = new Gson().fromJson(json, Patient.class);
        facilityId = patient.getFacilityId();
        patientId = patient.getFacilityId();

        id = preferences.getInt("id", 0);
        dateVisit = DateUtil.parseStringToDate(preferences.getString("dateVisit",  ""), "dd/MM/yyyy");
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

        EditText editText = (EditText) findViewById(R.id.date_visit);
        editText.setText(DateUtil.parseDateToString(dateVisit, "dd/MM/yyyy"));
        Spinner spinner = (Spinner) findViewById(R.id.ipt);
        spinner.setSelection(SpinnerUtil.getIndex(spinner, ipt));
        spinner = (Spinner) findViewById(R.id.tb_treatment);
        spinner.setSelection(SpinnerUtil.getIndex(spinner, tbTreatment));
        editText = (EditText) findViewById(R.id.date_started_tb_treatment);
        editText.setText(DateUtil.parseDateToString(dateStartedTbTreatment, "dd/MM/yyyy"));
        spinner = (Spinner) findViewById(R.id.tb_value1);
        spinner.setSelection(SpinnerUtil.getIndex(spinner, tbValue1));
        spinner = (Spinner) findViewById(R.id.tb_value2);
        spinner.setSelection(SpinnerUtil.getIndex(spinner, tbValue2));
        spinner = (Spinner) findViewById(R.id.tb_value3);
        spinner.setSelection(SpinnerUtil.getIndex(spinner, tbValue3));
        spinner = (Spinner) findViewById(R.id.tb_value4);
        spinner.setSelection(SpinnerUtil.getIndex(spinner, tbValue4));
        spinner = (Spinner) findViewById(R.id.tb_value5);
        spinner.setSelection(SpinnerUtil.getIndex(spinner, tbValue5));
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
