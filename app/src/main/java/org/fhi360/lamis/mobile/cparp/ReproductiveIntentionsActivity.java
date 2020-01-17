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

public class ReproductiveIntentionsActivity extends AppCompatActivity {

    private String reproductiveIntentions1;
    private String reproductiveIntentions1Referred;
    private String reproductiveIntentions2;
    private String reproductiveIntentions2Referred;
    private String reproductiveIntentions3;
    private String reproductiveIntentions3Referred;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reproductive_intentions);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Reproductive Intentions");
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
                Intent intent = new Intent(this, MalariaPreventionActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_navigate_backward:
                intent = new Intent(this, AdditionalServicesActivity.class);
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
        Log.v("ReproductActivity", "Saving to shared reference....");
        reproductiveIntentions1 = String.valueOf(((Spinner) findViewById(R.id.reproductive_intentions1)).getSelectedItem());
        reproductiveIntentions1Referred = String.valueOf(((Spinner) findViewById(R.id.reproductive_intentions1_referred)).getSelectedItem());
        reproductiveIntentions2 = String.valueOf(((Spinner) findViewById(R.id.reproductive_intentions2)).getSelectedItem());
        reproductiveIntentions2Referred = String.valueOf(((Spinner) findViewById(R.id.reproductive_intentions2_referred)).getSelectedItem());
        reproductiveIntentions3 = String.valueOf(((Spinner) findViewById(R.id.reproductive_intentions3)).getSelectedItem());
        reproductiveIntentions3Referred = String.valueOf(((Spinner) findViewById(R.id.reproductive_intentions3_referred)).getSelectedItem());

        // We need an Editor object to make preference changes. All objects are from android.context.Context
        SharedPreferences preferences = getSharedPreferences(PREFERENCES_ENCOUNTER, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("reproductiveIntentions1", reproductiveIntentions1);
        editor.putString("reproductiveIntentions1Referred", reproductiveIntentions1Referred);
        editor.putString("reproductiveIntentions2", reproductiveIntentions2);
        editor.putString("reproductiveIntentions2Referred", reproductiveIntentions2Referred);
        editor.putString("reproductiveIntentions3", reproductiveIntentions3);
        editor.putString("reproductiveIntentions3Referred", reproductiveIntentions3Referred);
        editor.commit();
    }

    public void restorePreferences() {
        Log.v("ReproductActivity", "Retrieving from shared reference....");
        // Restore preference
        SharedPreferences preferences = getSharedPreferences(PREFERENCES_ENCOUNTER, 0);
        reproductiveIntentions1 = preferences.getString("reproductiveIntentions1", "");
        reproductiveIntentions1Referred = preferences.getString("reproductiveIntentions1Referred", "");
        reproductiveIntentions2 = preferences.getString("reproductiveIntentions2", "");
        reproductiveIntentions2Referred = preferences.getString("reproductiveIntentions2Referred", "");
        reproductiveIntentions3 = preferences.getString("reproductiveIntentions3", "");
        reproductiveIntentions3Referred = preferences.getString("reproductiveIntentions3Referred", "");

        Spinner spinner = (Spinner) findViewById(R.id.reproductive_intentions1);
        spinner.setSelection(SpinnerUtil.getIndex(spinner, reproductiveIntentions1));
        spinner = (Spinner) findViewById(R.id.reproductive_intentions1_referred);
        spinner.setSelection(SpinnerUtil.getIndex(spinner, reproductiveIntentions1Referred));
        spinner = (Spinner) findViewById(R.id.reproductive_intentions2);
        spinner.setSelection(SpinnerUtil.getIndex(spinner, reproductiveIntentions2));
        spinner = (Spinner) findViewById(R.id.reproductive_intentions2_referred);
        spinner.setSelection(SpinnerUtil.getIndex(spinner, reproductiveIntentions2Referred));
        spinner = (Spinner) findViewById(R.id.reproductive_intentions3);
        spinner.setSelection(SpinnerUtil.getIndex(spinner, reproductiveIntentions3));
        spinner = (Spinner) findViewById(R.id.reproductive_intentions3_referred);
        spinner.setSelection(SpinnerUtil.getIndex(spinner, reproductiveIntentions3Referred));
    }


}
