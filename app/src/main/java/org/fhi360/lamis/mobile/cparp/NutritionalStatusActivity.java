package org.fhi360.lamis.mobile.cparp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import com.google.gson.Gson;
import org.fhi360.lamis.mobile.cparp.model.Patient;
import org.fhi360.lamis.mobile.cparp.utility.DateUtil;
import org.fhi360.lamis.mobile.cparp.utility.SpinnerUtil;
import java.util.Date;
import static org.fhi360.lamis.mobile.cparp.utility.Constants.PREFERENCES_ENCOUNTER;

public class NutritionalStatusActivity extends AppCompatActivity implements View.OnFocusChangeListener{
    private Patient patient;
    private Double bodyWeight;
    private Double height;
    private Double bmi;
    private String bmiCategory;
    private Double muac;
    private String muacCategory;
    private String supplementaryFood;
    private String nutritionalStatusReferred;

    private EditText bodyWeightEditText;
    private EditText heightEditText;
    private LinearLayout layoutAdult;
    private LinearLayout layoutPediatrics;
    private LinearLayout layoutSupplement;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutritional_status);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        this.preferences = getSharedPreferences(PREFERENCES_ENCOUNTER, 0);
        restorePreferences();

        String json = preferences.getString("patient", "");
        patient = new Gson().fromJson(json, Patient.class);

        layoutAdult = (LinearLayout) findViewById(R.id.adult);
        layoutPediatrics = (LinearLayout) findViewById(R.id.pediatrics);
        layoutSupplement = (LinearLayout) findViewById(R.id.supplement);
        layoutSupplement.setVisibility(View.GONE);

        if (DateUtil.getAge(patient.getDateBirth(), new Date()) >= 5) {
            layoutAdult.setVisibility(View.VISIBLE);
            layoutPediatrics.setVisibility(View.GONE);
        }
        else {
            layoutAdult.setVisibility(View.GONE);
            layoutPediatrics.setVisibility(View.VISIBLE);
        }

        bodyWeightEditText = (EditText) findViewById(R.id.body_weight);
        bodyWeightEditText.setOnFocusChangeListener(this);
        heightEditText = (EditText) findViewById(R.id.height);
        heightEditText.setOnFocusChangeListener(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Nutritional Status");
    }

    //Create the menu option
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate the menu; this adds items to the action bar if it is present
        getMenuInflater().inflate(R.menu.menu_chroniccare_navigate, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_navigate_forward:
                Intent intent = new Intent(this, GenderBasedViolenceActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_navigate_backward:
                intent = new Intent(this, TbScreeningActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_tb_screening:
                intent = new Intent(this, TbScreeningActivity.class);
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
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onResume() {
        super.onResume();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        restorePreferences();
    }

    @Override
    public void onPause() {
        super.onPause();
        savePreferences();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        Log.v("NutritionalActivity", "Calculating bmi....");
        String cat = "";
        if (!hasFocus){
            String value = ((EditText) findViewById(R.id.body_weight)).getText().toString();
            bodyWeight = value.trim().isEmpty()? null : Double.parseDouble(value);
            value = ((EditText) findViewById(R.id.height)).getText().toString();
            height = value.trim().isEmpty()? null : Double.parseDouble(value);
            if(bodyWeight != null && height != null){
                if (DateUtil.getAge(patient.getDateBirth(), new Date()) >= 5) {
                    double bmi = bodyWeight/(height * height);
                    bmi = Math.round(bmi*100)/100;
                    ((EditText) findViewById(R.id.bmi)).setText(Double.toString(bmi));

                    if(bmi < 18.5) cat = "<18.5 (Underweight)";
                    if(bmi >= 18.5 && bmi <= 24.9) cat = "18.5-24.9 (Healthy)";
                    if(bmi >= 25.0 && bmi <= 29.9) cat = "25.0-29.9 (Overweight)";
                    if(bmi >= 30.0 && bmi <= 39.9) cat = ">30 (Obesity)";
                    if(bmi >= 40.0) cat = ">40 (Morbid Obesity)";
                    Spinner spinner = (Spinner) findViewById(R.id.bmi_category);
                    spinner.setSelection(SpinnerUtil.getIndex(spinner, cat));
                    if(bmi < 18.5) {
                        layoutSupplement.setVisibility(View.VISIBLE);
                    }
                    else {
                        layoutSupplement.setVisibility(View.GONE);
                    }
                }
                else {
                    value = ((EditText) findViewById(R.id.muac)).getText().toString();
                    double muac = value.trim().isEmpty()? null : Double.parseDouble(value);
                    if(bodyWeight != null) {
                        if(muac < 11.5) cat = "<11.5cm (Severe Acute Malnutrition)";
                        if(muac >= 11.5 && muac <= 12.5) cat = "11.5-12.5cm (Moderate Acute Malnutrition)";
                        if(muac > 12.5) cat = ">12.5cm (Well nourished)";
                        Spinner spinner = (Spinner) findViewById(R.id.muac_category);
                        spinner.setSelection(SpinnerUtil.getIndex(spinner, cat));

                        if(muac <= 12.5) {
                            layoutSupplement.setVisibility(View.VISIBLE);
                        }
                        else {
                            layoutSupplement.setVisibility(View.GONE);
                        }
                    }
                }
            }
        }
        if (hasFocus) {}
    }

    public void savePreferences() {
        Log.v("NutritionalActivity", "Saving to shared reference....");
        String value = ((EditText) findViewById(R.id.body_weight)).getText().toString();
        bodyWeight = value.trim().isEmpty()? null : Double.parseDouble(value);
        value = ((EditText) findViewById(R.id.height)).getText().toString();
        height = value.trim().isEmpty()? null : Double.parseDouble(value);
        value = ((EditText) findViewById(R.id.bmi)).getText().toString();
        bmi = value.trim().isEmpty()? null : Double.parseDouble(value);
        bmiCategory = String.valueOf(((Spinner) findViewById(R.id.bmi_category)).getSelectedItem());
        value = ((EditText) findViewById(R.id.muac)).getText().toString();
        muac = value.trim().isEmpty()? null : Double.parseDouble(value);
        muacCategory = String.valueOf(((Spinner) findViewById(R.id.muac_category)).getSelectedItem());
        supplementaryFood = String.valueOf(((Spinner) findViewById(R.id.supplementary_food)).getSelectedItem());
        nutritionalStatusReferred = String.valueOf(((Spinner) findViewById(R.id.nutritional_status_referred)).getSelectedItem());

        // We need an Editor object to make preference changes. All objects are from android.context.Context
        SharedPreferences preferences = getSharedPreferences(PREFERENCES_ENCOUNTER, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("bodyWeight", bodyWeight == null? "" : Double.toString(bodyWeight));
        editor.putString("height", height == null? "" : Double.toString(height));
        editor.putString("bmi", bmi == null? "" : Double.toString(bmi));
        editor.putString("bmiCategory", bmiCategory);
        editor.putString("muac", muac == null? "" : Double.toString(muac));
        editor.putString("muacCategory", muacCategory);
        editor.putString("supplementaryFood", supplementaryFood);
        editor.putString("nutritionalStatusReferred", nutritionalStatusReferred);
        editor.commit();
    }

    public void restorePreferences() {
        String value = preferences.getString("bodyWeight", "");
        bodyWeight = value.trim().isEmpty()? null : Double.parseDouble(value);
        value = preferences.getString("height", "");
        height = value.trim().isEmpty()? null : Double.parseDouble(value);
        value = preferences.getString("bmi", "");
        bmi = value.trim().isEmpty()? null : Double.parseDouble(value);
        bmiCategory = preferences.getString("bmiCategory", "");
        value = preferences.getString("muac", "");
        muac = value.trim().isEmpty()? null : Double.parseDouble(value);
        muacCategory = preferences.getString("muacCategory", "");

        supplementaryFood = preferences.getString("supplementaryFood", "");
        nutritionalStatusReferred = preferences.getString("nutritionalStatusReferred", "");

        EditText editText =  findViewById(R.id.body_weight);
        editText.setText(bodyWeight == null? "" : Double.toString(bodyWeight));
        editText = findViewById(R.id.height);
        editText.setText(height == null? "" : Double.toString(height));
        editText =  findViewById(R.id.bmi);
        editText.setText(bmi == null? "" : Double.toString(bmi));
        Spinner spinner =  findViewById(R.id.bmi_category);
        spinner.setSelection(SpinnerUtil.getIndex(spinner, bmiCategory));
        editText = findViewById(R.id.muac);
        editText.setText(muac == null? "" : Double.toString(muac));
        spinner =  findViewById(R.id.muac_category);
        spinner.setSelection(SpinnerUtil.getIndex(spinner,  muacCategory));
        spinner =  findViewById(R.id.supplementary_food);
        spinner.setSelection(SpinnerUtil.getIndex(spinner,supplementaryFood));
        spinner =  findViewById(R.id.nutritional_status_referred);
        spinner.setSelection(SpinnerUtil.getIndex(spinner, nutritionalStatusReferred));
    }

}
