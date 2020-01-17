package org.fhi360.lamis.mobile.cparp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;


import static org.fhi360.lamis.mobile.cparp.utility.Constants.PREFERENCES_ENCOUNTER;

public class AdditionalServicesActivity extends AppCompatActivity {
    private String additionalServicesProvided;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additional_services);

        this.preferences = getSharedPreferences(PREFERENCES_ENCOUNTER, 0);
        restorePreferences();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Additional Services Provided");
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
                Intent intent = new Intent(this, ReproductiveIntentionsActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_navigate_backward:
                intent = new Intent(this, PositiveHealthActivity.class);
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
        additionalServicesProvided = "#";
        if(((CheckBox) findViewById(R.id.additional_service1)).isChecked()) additionalServicesProvided = additionalServicesProvided + "1#";

        if(((CheckBox) findViewById(R.id.additional_service2)).isChecked()) additionalServicesProvided = additionalServicesProvided + "2#";

        if(((CheckBox) findViewById(R.id.additional_service3)).isChecked()) additionalServicesProvided = additionalServicesProvided + "3#";

        if(((CheckBox) findViewById(R.id.additional_service4)).isChecked()) additionalServicesProvided = additionalServicesProvided + "4#";

        if(((CheckBox) findViewById(R.id.additional_service5)).isChecked()) additionalServicesProvided = additionalServicesProvided + "5#";

        if(((CheckBox) findViewById(R.id.additional_service6)).isChecked()) additionalServicesProvided = additionalServicesProvided + "6#";

        if(((CheckBox) findViewById(R.id.additional_service7)).isChecked()) additionalServicesProvided = additionalServicesProvided + "7#";

        if(((CheckBox) findViewById(R.id.additional_service8)).isChecked()) additionalServicesProvided = additionalServicesProvided + "8#";

        if(((CheckBox) findViewById(R.id.additional_service9)).isChecked()) additionalServicesProvided = additionalServicesProvided + "9#";

        if(((CheckBox) findViewById(R.id.additional_service10)).isChecked()) additionalServicesProvided = additionalServicesProvided + "10#";

        if(((CheckBox) findViewById(R.id.additional_service11)).isChecked()) additionalServicesProvided = additionalServicesProvided + "11#";
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("additionalServicesProvided", additionalServicesProvided);
        editor.commit();
    }

    public void restorePreferences() {
        //In order to change it, use either setChecked(boolean) for an explicit value or toggle() to inverse it.
        additionalServicesProvided = preferences.getString("additionalServicesProvided", "");
        if (additionalServicesProvided.indexOf("#1#") != -1) ((CheckBox) findViewById(R.id.additional_service1)).setChecked(true);
        if (additionalServicesProvided.indexOf("#2#") != -1) ((CheckBox) findViewById(R.id.additional_service2)).setChecked(true);
        if (additionalServicesProvided.indexOf("#3#") != -1) ((CheckBox) findViewById(R.id.additional_service3)).setChecked(true);
        if (additionalServicesProvided.indexOf("#4#") != -1) ((CheckBox) findViewById(R.id.additional_service4)).setChecked(true);
        if (additionalServicesProvided.indexOf("#5#") != -1) ((CheckBox) findViewById(R.id.additional_service5)).setChecked(true);
        if (additionalServicesProvided.indexOf("#6#") != -1) ((CheckBox) findViewById(R.id.additional_service6)).setChecked(true);
        if (additionalServicesProvided.indexOf("#7#") != -1) ((CheckBox) findViewById(R.id.additional_service7)).setChecked(true);
        if (additionalServicesProvided.indexOf("#8#") != -1) ((CheckBox) findViewById(R.id.additional_service8)).setChecked(true);
        if (additionalServicesProvided.indexOf("#9#") != -1) ((CheckBox) findViewById(R.id.additional_service9)).setChecked(true);
        if (additionalServicesProvided.indexOf("#10#") != -1) ((CheckBox) findViewById(R.id.additional_service10)).setChecked(true);
        if (additionalServicesProvided.indexOf("#11#") != -1) ((CheckBox) findViewById(R.id.additional_service11)).setChecked(true);
    }

}
