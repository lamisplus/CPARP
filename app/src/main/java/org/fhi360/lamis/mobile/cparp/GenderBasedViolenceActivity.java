package org.fhi360.lamis.mobile.cparp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Spinner;

import org.fhi360.lamis.mobile.cparp.R;
import org.fhi360.lamis.mobile.cparp.utility.SpinnerUtil;

import static org.fhi360.lamis.mobile.cparp.utility.Constants.PREFERENCES_ENCOUNTER;

public class GenderBasedViolenceActivity extends AppCompatActivity {

    private String gbv1;
    private String gbv1Referred;
    private String gbv2;
    private String gbv2Referred;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gender_based_violence);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Gender Based Violence");
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
                Intent intent = new Intent(this, ChronicConditionActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_navigate_backward:
                intent = new Intent(this, NutritionalStatusActivity.class);
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
        restorePreferences();
    }

    @Override
    public void onPause() {
        super.onPause();
        savePreferences();
    }

    public void savePreferences() {
        Log.v("GenderBasedActivity", "Saving to shared reference....");
        gbv1 = String.valueOf(((Spinner) findViewById(R.id.gbv1)).getSelectedItem());
        gbv1Referred = String.valueOf(((Spinner) findViewById(R.id.gbv1_referred)).getSelectedItem());
        gbv2 = String.valueOf(((Spinner) findViewById(R.id.gbv2)).getSelectedItem());
        gbv2Referred = String.valueOf(((Spinner) findViewById(R.id.gbv2_referred)).getSelectedItem());

        // We need an Editor object to make preference changes. All objects are from android.context.Context
        SharedPreferences preferences = getSharedPreferences(PREFERENCES_ENCOUNTER, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("gbv1", gbv1);
        editor.putString("gbv1Referred", gbv1Referred);
        editor.putString("gbv2", gbv2);
        editor.putString("gbv2Referred", gbv2Referred);
        editor.commit();
    }

    public void restorePreferences() {
        Log.v("GenderBasedActivity", "Retrieving from shared reference....");
        // Restore preference
        SharedPreferences preferences = getSharedPreferences(PREFERENCES_ENCOUNTER, 0);
        gbv1 = preferences.getString("gbv1", "");
        gbv1Referred = preferences.getString("gbv1Referred", "");
        gbv2 = preferences.getString("gbv2", "");
        gbv2Referred = preferences.getString("gbv2Referred", "");

        Spinner spinner = (Spinner) findViewById(R.id.gbv1);
        spinner.setSelection(SpinnerUtil.getIndex(spinner, gbv1));
        spinner = (Spinner) findViewById(R.id.gbv1_referred);
        spinner.setSelection(SpinnerUtil.getIndex(spinner, gbv1Referred));
        spinner = (Spinner) findViewById(R.id.gbv2);
        spinner.setSelection(SpinnerUtil.getIndex(spinner, gbv1));
        spinner = (Spinner) findViewById(R.id.gbv2_referred);
        spinner.setSelection(SpinnerUtil.getIndex(spinner, gbv2Referred));
    }


}
