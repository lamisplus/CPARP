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
import com.shashank.sony.fancytoastlib.FancyToast;

import org.fhi360.lamis.mobile.cparp.dao.AppointmentDAO;
import org.fhi360.lamis.mobile.cparp.dao.EncounterDAO;
import org.fhi360.lamis.mobile.cparp.dao.MonitorDAO;
import org.fhi360.lamis.mobile.cparp.model.Encounter;
import org.fhi360.lamis.mobile.cparp.model.Patient;
import org.fhi360.lamis.mobile.cparp.R;
import org.fhi360.lamis.mobile.cparp.utility.DateUtil;
import org.fhi360.lamis.mobile.cparp.utility.EditTextDatePicker;
import org.fhi360.lamis.mobile.cparp.utility.SpinnerUtil;

import java.util.Date;

import static org.fhi360.lamis.mobile.cparp.utility.Constants.PREFERENCES_ENCOUNTER;

public class ClientTrackingActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private int id;
    private int facilityId;
    private int patientId;
    private Date dateLastVisit;
    private Date dateNextVisit;
    private Date dateTracked;
    private String typeTracking;
    private String trackingOutcome;
    private Date dateAgreed;
    private LinearLayout layoutAgreedYes;
    private Spinner typeTrackingSpinner;
    private Spinner trackingOutcomeSpinner;

    private Patient patient;
    private boolean EDIT_MODE;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_tracking);

        this.preferences = getSharedPreferences(PREFERENCES_ENCOUNTER, 0);
        restorePreferences();

        Encounter encounter = new EncounterDAO(this).getLastEncounter(facilityId, patientId);
        dateLastVisit = encounter.getDateVisit();
        dateNextVisit = encounter.getNextRefill();

        layoutAgreedYes = (LinearLayout) findViewById(R.id.agreed_yes);
        layoutAgreedYes.setVisibility(View.GONE);
        //Get reference to the spinners
        typeTrackingSpinner = (Spinner) findViewById(R.id.type_tracking);
        trackingOutcomeSpinner = (Spinner) findViewById(R.id.tracking_outcome);
        trackingOutcomeSpinner.setOnItemSelectedListener(this);

        //Set date picker on the edit text
        new EditTextDatePicker(this, (EditText) findViewById(R.id.date_tracked));
        new EditTextDatePicker(this, (EditText) findViewById(R.id.date_agreed));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Client Tracking");
    }

    //Create the menu option
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate the menu; this adds items to the action bar if it is present
        getMenuInflater().inflate(R.menu.menu_encounter, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle other ActionBar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_delete:
                new AppointmentDAO(this).delete(id);

                //Log delete record in the monitor for replication on the server
                String entityId = Long.toString(patientId) + "#" + dateTracked.toString();
                new MonitorDAO(this).save(facilityId, entityId, "appointment", 3);

                Intent intent = new Intent(this, SearchClientTrackingActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_new:
                clearPreferences();
                restorePreferences();
                intent = new Intent(this, ClientTrackingActivity.class);
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
    public void onSaveInstanceState(Bundle saveInstanceState) {
        super.onSaveInstanceState(saveInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        String outcome = String.valueOf(trackingOutcomeSpinner.getSelectedItem());
        if(outcome.equalsIgnoreCase("Agreed to Return")) {
            layoutAgreedYes.setVisibility(View.VISIBLE);
        }
        else {
            layoutAgreedYes.setVisibility(View.GONE);
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

    public void onClickSaveButton(View view) {
        dateTracked = DateUtil.parseStringToDate(((EditText) findViewById(R.id.date_tracked)).getText().toString(), "dd/MM/yyyy");
        typeTracking = String.valueOf(((Spinner) findViewById(R.id.type_tracking)).getSelectedItem());
        trackingOutcome = String.valueOf(((Spinner) findViewById(R.id.tracking_outcome)).getSelectedItem());
        dateAgreed = DateUtil.parseStringToDate(((EditText) findViewById(R.id.date_agreed)).getText().toString(), "dd/MM/yyyy");

        if(dateTracked != null) {
            AppointmentDAO appointmentDAO = new AppointmentDAO(this);
            int id = appointmentDAO.getId(facilityId, patientId, dateTracked);
            if(id != 0 ) {
                appointmentDAO.update(id, facilityId, patientId, dateTracked, typeTracking, trackingOutcome, dateLastVisit, dateNextVisit, dateAgreed);
                FancyToast.makeText(getApplicationContext(), "Client tracking data updated", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();

            }
            else {
                appointmentDAO.save(facilityId, patientId, dateTracked, typeTracking, trackingOutcome, dateLastVisit, dateNextVisit, dateAgreed);
                FancyToast.makeText(getApplicationContext(), "Client tracking data saved", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();

            }
            Intent intent = new Intent(this, DefaulterListActivity.class);
            startActivity(intent);
        }
        else {
            FancyToast.makeText(getApplicationContext(), "Please enter date of tracking", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();

              }
    }

    private void savePreferences() {
        extractViewData();
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("dateTracked", DateUtil.parseDateToString(dateTracked, "dd/MM/yyyy"));
        editor.putString("typeTracking", typeTracking);
        editor.putString("trackingOutcome", trackingOutcome);
        editor.putString("dateAgreed", DateUtil.parseDateToString(dateAgreed, "dd/MM/yyyy"));
        editor.putString("dateLastVisit", DateUtil.parseDateToString(dateLastVisit, "dd/MM/yyyy"));
        editor.putString("dateNextVisit", DateUtil.parseDateToString(dateNextVisit, "dd/MM/yyyy"));
        editor.commit();
    }

    private void restorePreferences() {
        EDIT_MODE = preferences.getBoolean("edit_mode", false);
        String json = preferences.getString("patient", "");
        patient = new Gson().fromJson(json, Patient.class);
        facilityId = patient.getFacilityId();
        patientId = patient.getPatientId();

        id = preferences.getInt("id", 0);
        dateTracked = DateUtil.parseStringToDate(preferences.getString("dateTracked",  ""), "dd/MM/yyyy");
        typeTracking = preferences.getString("typeTracking", "");
        trackingOutcome = preferences.getString("trackingOutcome", "");
        dateAgreed = DateUtil.parseStringToDate(preferences.getString("dateAgreed",  ""), "dd/MM/yyyy");
        dateLastVisit = DateUtil.parseStringToDate(preferences.getString("dateLastVisit",  ""), "dd/MM/yyyy");
        dateNextVisit = DateUtil.parseStringToDate(preferences.getString("dateNextVisit",  ""), "dd/MM/yyyy");

        EditText editText = (EditText) findViewById(R.id.date_tracked);
        editText.setText(DateUtil.parseDateToString(dateTracked, "dd/MM/yyyy"));
        Spinner spinner = (Spinner) findViewById(R.id.type_tracking);
        spinner.setSelection(SpinnerUtil.getIndex(spinner, typeTracking));
        spinner = (Spinner) findViewById(R.id.tracking_outcome);
        spinner.setSelection(SpinnerUtil.getIndex(spinner, trackingOutcome));
        editText = (EditText) findViewById(R.id.date_agreed);
        editText.setText(DateUtil.parseDateToString(dateAgreed, "dd/MM/yyyy"));
    }

    private void extractViewData() {
        dateTracked = DateUtil.parseStringToDate(((EditText) findViewById(R.id.date_tracked)).getText().toString(), "dd/MM/yyyy");
        typeTracking = String.valueOf(((Spinner) findViewById(R.id.type_tracking)).getSelectedItem());
        trackingOutcome = String.valueOf(((Spinner) findViewById(R.id.tracking_outcome)).getSelectedItem());
        dateAgreed = DateUtil.parseStringToDate(((EditText) findViewById(R.id.date_agreed)).getText().toString(), "dd/MM/yyyy");
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
