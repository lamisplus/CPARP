package org.fhi360.lamis.mobile.cparp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Spinner;

import org.fhi360.lamis.mobile.cparp.R;
import org.fhi360.lamis.mobile.cparp.utility.SpinnerUtil;

import static org.fhi360.lamis.mobile.cparp.utility.Constants.PREFERENCES_ENCOUNTER;

public class PositiveHealthActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_positive_health);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Positive Health Dignity & Prevention");
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
                Intent intent = new Intent(this, AdditionalServicesActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_navigate_backward:
                intent = new Intent(this, ChronicConditionActivity.class);
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
        restorePreferences();
    }

    @Override
    public void onPause() {
        super.onPause();
        savePreferences();
    }

    public void savePreferences() {
        phdp1 = String.valueOf(((Spinner) findViewById(R.id.phdp1)).getSelectedItem());
        phdp1ServicesProvided = String.valueOf(((Spinner) findViewById(R.id.phdp1_services_provided)).getSelectedItem());
        phdp2 = String.valueOf(((Spinner) findViewById(R.id.phdp2)).getSelectedItem());
        phdp3 = String.valueOf(((Spinner) findViewById(R.id.phdp3)).getSelectedItem());
        phdp4 = String.valueOf(((Spinner) findViewById(R.id.phdp4)).getSelectedItem());
        phdp4ServicesProvided = String.valueOf(((Spinner) findViewById(R.id.phdp4_services_provided)).getSelectedItem());
        phdp5 = String.valueOf(((Spinner) findViewById(R.id.phdp5)).getSelectedItem());
        String value = ((EditText) findViewById(R.id.phdp6)).getText().toString();
        phdp6 = value.trim().isEmpty()? null : Integer.parseInt(value);
        value = ((EditText) findViewById(R.id.phdp7)).getText().toString();
        phdp7 = value.trim().isEmpty()? null : Integer.parseInt(value);
        phdp7ServicesProvided = String.valueOf(((Spinner) findViewById(R.id.phdp7_services_provided)).getSelectedItem());
        phdp8ServicesProvided = String.valueOf(((Spinner) findViewById(R.id.phdp8_services_provided)).getSelectedItem());

        // We need an Editor object to make preference changes. All objects are from android.context.Context
        SharedPreferences preferences = getSharedPreferences(PREFERENCES_ENCOUNTER, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("phdp1", phdp1);
        editor.putString("phdp1ServicesProvided", phdp1ServicesProvided);
        editor.putString("phdp2", phdp2);
        editor.putString("phdp3", phdp3);
        editor.putString("phdp4", phdp4);
        editor.putString("phdp4ServicesProvided", phdp4ServicesProvided);
        editor.putString("phdp5", phdp5);
        editor.putString("phdp6", phdp6 == null? "" : Integer.toString(phdp6));
        editor.putString("phdp7", phdp7 == null? "" : Integer.toString(phdp7));
        editor.putString("phdp7ServicesProvided", phdp7ServicesProvided);
        editor.putString("phdp8ServicesProvided", phdp8ServicesProvided);
        editor.commit();
    }

    public void restorePreferences() {
        Log.v("PositiveActivity", "Retrieving from shared reference....");
        // Restore preference
        SharedPreferences preferences = getSharedPreferences(PREFERENCES_ENCOUNTER, 0);
        phdp1 = preferences.getString("phdp1", "");
        phdp1ServicesProvided = preferences.getString("phdp1ServicesProvided", "");
        phdp2 = preferences.getString("phdp2", "");
        phdp3 = preferences.getString("phdp3", "");
        phdp4 = preferences.getString("phdp4", "");
        phdp4ServicesProvided = preferences.getString("phdp4ServicesProvided", "");
        phdp5 = preferences.getString("phdp5", "");
        String value = preferences.getString("phdp6", "");
        phdp6 = value.trim().isEmpty()? null : Integer.parseInt(value);
        value = preferences.getString("phdp7", "");
        phdp7 = value.trim().isEmpty()? null : Integer.parseInt(value);
        phdp7ServicesProvided = preferences.getString("phdp7ServicesProvided", "");
        phdp8ServicesProvided = preferences.getString("phdp8ServicesProvided", "");

        Spinner spinner = (Spinner) findViewById(R.id.phdp1);
        spinner.setSelection(SpinnerUtil.getIndex(spinner, phdp1));
        spinner = (Spinner) findViewById(R.id.phdp1_services_provided);
        spinner.setSelection(SpinnerUtil.getIndex(spinner, phdp1ServicesProvided));
        spinner = (Spinner) findViewById(R.id.phdp2);
        spinner.setSelection(SpinnerUtil.getIndex(spinner, phdp2));
        spinner = (Spinner) findViewById(R.id.phdp3);
        spinner.setSelection(SpinnerUtil.getIndex(spinner, phdp3));
        spinner = (Spinner) findViewById(R.id.phdp4);
        spinner.setSelection(SpinnerUtil.getIndex(spinner, phdp4));
        spinner = (Spinner) findViewById(R.id.phdp4_services_provided);
        spinner.setSelection(SpinnerUtil.getIndex(spinner, phdp4ServicesProvided));
        spinner = (Spinner) findViewById(R.id.phdp5);
        spinner.setSelection(SpinnerUtil.getIndex(spinner, phdp5));
        EditText editText = (EditText) findViewById(R.id.phdp6);
        editText.setText(phdp6 == null? "" : Integer.toString(phdp6));
        editText = (EditText) findViewById(R.id.phdp7);
        editText.setText(phdp7 == null? "" : Integer.toString(phdp7));
        spinner = (Spinner) findViewById(R.id.phdp7_services_provided);
        spinner.setSelection(SpinnerUtil.getIndex(spinner, phdp7ServicesProvided));
        spinner = (Spinner) findViewById(R.id.phdp8_services_provided);
        spinner.setSelection(SpinnerUtil.getIndex(spinner, phdp8ServicesProvided));
    }

}
