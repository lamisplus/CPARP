package org.fhi360.lamis.mobile.cparp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import org.fhi360.lamis.mobile.cparp.R;
import org.fhi360.lamis.mobile.cparp.utility.SpinnerUtil;

import static org.fhi360.lamis.mobile.cparp.utility.Constants.PREFERENCES_ENCOUNTER;

public class ChronicConditionActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private LinearLayout layoutDiabeticNo;
    private Spinner diabeticSpinner;

    private String hypertensive;
    private String firstHypertensive;
    private String bp;
    private String bpAbove;
    private String bpReferred;
    private String diabetic;
    private String firstDiabetic;
    private String dmValue1;
    private String dmValue2;
    private String dmReferred;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chronic_condition);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        layoutDiabeticNo = (LinearLayout) findViewById(R.id.diabetic_no);
        diabeticSpinner = (Spinner) findViewById(R.id.diabetic);
        diabeticSpinner.setOnItemSelectedListener(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Chronic Conditions");
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
                Intent intent = new Intent(this, PositiveHealthActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_navigate_backward:
                intent = new Intent(this, GenderBasedViolenceActivity.class);
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

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        String diabetic = String.valueOf(diabeticSpinner.getSelectedItem());
        if(diabetic.equalsIgnoreCase("YES") || diabetic.isEmpty()) {
            layoutDiabeticNo.setVisibility(View.GONE);
        }
        else {
            layoutDiabeticNo.setVisibility(View.VISIBLE);
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
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

    public void savePreferences() {
        hypertensive = String.valueOf(((Spinner) findViewById(R.id.hypertensive)).getSelectedItem());
        firstHypertensive = String.valueOf(((Spinner) findViewById(R.id.first_hypertensive)).getSelectedItem());
        bp = ((EditText) findViewById(R.id.bp)).getText().toString();
        bpAbove = String.valueOf(((Spinner) findViewById(R.id.bp_above)).getSelectedItem());
        bpReferred = String.valueOf(((Spinner) findViewById(R.id.bp_referred)).getSelectedItem());
        diabetic = String.valueOf(((Spinner) findViewById(R.id.diabetic)).getSelectedItem());
        firstDiabetic = String.valueOf(((Spinner) findViewById(R.id.first_diabetic)).getSelectedItem());
        dmValue1 = String.valueOf(((Spinner) findViewById(R.id.dm_value1)).getSelectedItem());
        dmValue2 = String.valueOf(((Spinner) findViewById(R.id.dm_value2)).getSelectedItem());
        dmReferred = String.valueOf(((Spinner) findViewById(R.id.dm_referred)).getSelectedItem());

        // We need an Editor object to make preference changes. All objects are from android.context.Context
        SharedPreferences preferences = getSharedPreferences(PREFERENCES_ENCOUNTER, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("hypertensive", hypertensive);
        editor.putString("firstHypertensive", firstHypertensive);
        editor.putString("bp", bp);
        editor.putString("bpAbove", bpAbove);
        editor.putString("bpReferred", bpReferred);
        editor.putString("diabetic", diabetic);
        editor.putString("firstDiabetic", firstDiabetic);
        editor.putString("dmValue1", dmValue1);
        editor.putString("dmValue2", dmValue2);
        editor.putString("dmReferred", dmReferred);
        editor.commit();
    }

    public void restorePreferences() {
        SharedPreferences preferences = getSharedPreferences(PREFERENCES_ENCOUNTER, 0);
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

        Spinner spinner =  findViewById(R.id.hypertensive);
        spinner.setSelection(SpinnerUtil.getIndex(spinner, hypertensive));
        spinner =  findViewById(R.id.first_hypertensive);
        spinner.setSelection(SpinnerUtil.getIndex(spinner, firstHypertensive));
        EditText editText = (EditText) findViewById(R.id.bp);
        editText.setText(bp);
        spinner = findViewById(R.id.bp_above);
        spinner.setSelection(SpinnerUtil.getIndex(spinner, bpAbove));
        spinner =  findViewById(R.id.bp_referred);
        spinner.setSelection(SpinnerUtil.getIndex(spinner, bpReferred));
        spinner =  findViewById(R.id.diabetic);
        spinner.setSelection(SpinnerUtil.getIndex(spinner, diabetic));
        spinner =  findViewById(R.id.first_diabetic);
        spinner.setSelection(SpinnerUtil.getIndex(spinner, firstDiabetic));
        spinner =findViewById(R.id.dm_value1);
        spinner.setSelection(SpinnerUtil.getIndex(spinner, dmValue1));
        spinner =  findViewById(R.id.dm_value2);
        spinner.setSelection(SpinnerUtil.getIndex(spinner, dmValue2));
        spinner =  findViewById(R.id.dm_referred);
        spinner.setSelection(SpinnerUtil.getIndex(spinner, dmReferred));
    }

}
