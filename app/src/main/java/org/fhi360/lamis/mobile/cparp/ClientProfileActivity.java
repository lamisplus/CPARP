package org.fhi360.lamis.mobile.cparp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.gson.Gson;

import org.fhi360.lamis.mobile.cparp.model.Patient;
import org.fhi360.lamis.mobile.cparp.R;
import org.fhi360.lamis.mobile.cparp.utility.DateUtil;
import org.fhi360.lamis.mobile.cparp.utility.Scrambler;

import java.util.Date;

import static org.fhi360.lamis.mobile.cparp.utility.Constants.PREFERENCES_ENCOUNTER;

public class ClientProfileActivity extends AppCompatActivity {
    private Patient patient;
    private boolean INITIAL = true;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_profile);

        this.preferences = getSharedPreferences(PREFERENCES_ENCOUNTER, 0);
        restorePreferences();
        if (savedInstanceState != null) {
            String json = savedInstanceState.getString("patient");
            patient = new Gson().fromJson(json, Patient.class);
        }
        String name = patient.getSurname() + " " + patient.getOtherNames();
        ((TextView) findViewById(R.id.name)).setText(name);

        int age = DateUtil.getAge(patient.getDateBirth(), new Date());
        ((TextView) findViewById(R.id.age)).setText(Integer.toString(age));
        ((TextView) findViewById(R.id.gender)).setText(patient.getGender());
        ((TextView) findViewById(R.id.address)).setText(patient.getAddress());
        ((TextView) findViewById(R.id.phone)).setText(patient.getPhone());

        String lastCd4 = Double.toString(patient.getLastCd4());
        String dateLastCd4 = (patient.getDateLastCd4() == null)? "" : DateUtil.parseDateToString(patient.getDateLastCd4(), "dd/MM/yyyy");
        if(!lastCd4.isEmpty() && !dateLastCd4.isEmpty()) ((TextView) findViewById(R.id.cd4)).setText(lastCd4 + "  (" + dateLastCd4 + ")");
        String lastViralLoad = Double.toString(patient.getLastViralLoad());
        String dateLastViralLoad = (patient.getDateLastViralLoad() == null)? "" : DateUtil.parseDateToString(patient.getDateLastViralLoad(), "dd/MM/yyyy");
        if(!lastViralLoad.isEmpty() && !dateLastViralLoad.isEmpty()) ((TextView) findViewById(R.id.viral_load)).setText(lastViralLoad + "  (" + dateLastViralLoad + ")");
        String dateNextClinic = (patient.getDateNextClinic() == null)? "" : DateUtil.parseDateToString(patient.getDateNextClinic(), "dd/MM/yyyy");
        if(!dateNextClinic.isEmpty()) ((TextView) findViewById(R.id.next_clinic)).setText( "(" + dateNextClinic + ")");
        String viralLoadDueDate = (patient.getViralLoadDueDate() == null)? "" : DateUtil.parseDateToString(patient.getViralLoadDueDate(), "dd/MM/yyyy");
        if(!viralLoadDueDate.isEmpty()) ((TextView) findViewById(R.id.next_viralLoad)).setText( "(" + viralLoadDueDate + ")");

        if(patient.getLastRefillSetting().equalsIgnoreCase("COMMUNITY")) {
            INITIAL = false;
        }
        else {
            INITIAL = true;
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Client Profile");
    }

    //Create the menu option
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate the menu; this adds items to the action bar if it is present
        getMenuInflater().inflate(R.menu.menu_client_profile, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle other ActionBar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        clearPreferences();
        switch (item.getItemId()) {
            case R.id.action_encounter:
                Intent intent = null;
                if(INITIAL) {
                   intent = new Intent(this, EncounterActivity.class);
                }
                else {
                    intent = new Intent(this, DrugRefillActivity.class);
                }
                startActivity(intent);
                return true;
            case R.id.action_chroniccare:
                intent = new Intent(this, TbScreeningActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_drugtherapy:
                intent = new Intent(this, DrugTherapyActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_discontinue:
                intent = new Intent(this, DiscontinueServiceActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_refill_history:
                intent = new Intent(this, SearchEncounterActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_chroniccare_history:
                intent = new Intent(this, SearchChroniccareActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_drugtherapy_history:
                intent = new Intent(this, SearchDrugTherapyActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_clienttracking_history:
                intent = new Intent(this, SearchClientTrackingActivity.class);
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
    public void onSaveInstanceState(Bundle saveInstanceState) {
        super.onSaveInstanceState(saveInstanceState);
        //saveInstanceState.putBoolean("edit_mode", EDIT_MODE);
        saveInstanceState.putString("patient", new Gson().toJson(patient));
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String json = savedInstanceState.getString("patient");
        patient = new Gson().fromJson(json, Patient.class);
    }

    private void restorePreferences() {
        String json = preferences.getString("patient", "");
        patient = new Gson().fromJson(json, Patient.class);
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
