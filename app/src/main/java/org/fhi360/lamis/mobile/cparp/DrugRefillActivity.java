package org.fhi360.lamis.mobile.cparp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.fhi360.lamis.mobile.cparp.dao.DevolveDAO;
import org.fhi360.lamis.mobile.cparp.dao.EncounterDAO;
import org.fhi360.lamis.mobile.cparp.dao.MonitorDAO;
import org.fhi360.lamis.mobile.cparp.dao.RegimenDAO;
import org.fhi360.lamis.mobile.cparp.model.Patient;
import org.fhi360.lamis.mobile.cparp.R;
import org.fhi360.lamis.mobile.cparp.model.Devolve;
import org.fhi360.lamis.mobile.cparp.utility.DateUtil;
import org.fhi360.lamis.mobile.cparp.utility.EditTextDatePicker;
import org.fhi360.lamis.mobile.cparp.utility.SpinnerUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.fhi360.lamis.mobile.cparp.utility.Constants.PREFERENCES_ENCOUNTER;

public class DrugRefillActivity extends AppCompatActivity {
    private int id;
    private int facilityId;
    private int patientId;
    private Date dateVisit;
    private String question1;
    private String question2;
    private String question3;
    private String question4;
    private String question5;
    private String question6;
    private String question7;
    private String question8;
    private String question9;
    private String regimen1;
    private String regimen2;
    private String regimen3;
    private String regimen4;
    private Integer duration1;
    private Integer duration2;
    private Integer duration3;
    private Integer duration4;
    private Integer prescribed1;
    private Integer prescribed2;
    private Integer prescribed3;
    private Integer prescribed4;
    private Integer dispensed1;
    private Integer dispensed2;
    private Integer dispensed3;
    private Integer dispensed4;
    private Date nextRefill;
    private String notes;
    private String regimentype;

    private Spinner medicine1;
    private Spinner medicine2;
    private Spinner medicine3;
    private Spinner medicine4;

    private Patient patient;
    private boolean EDIT_MODE;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drug_refill);

        this.preferences = getSharedPreferences(PREFERENCES_ENCOUNTER, 0);
        restorePreferences();
        //Get reference to the regimen1 spinner
        medicine1 =  findViewById(R.id.regimen1);
        medicine2 = (Spinner) findViewById(R.id.regimen2);
        medicine3 = (Spinner) findViewById(R.id.regimen3);
        medicine4 = (Spinner) findViewById(R.id.regimen4);
        addItemsMedicineSpinners();

        //Set date picker on the edit text
        new EditTextDatePicker(this, (EditText) findViewById(R.id.date_visit));  //use this for fragment:  new EditTextDatePicker(getContext(), R.id.date_visit);
        new EditTextDatePicker(this, (EditText) findViewById(R.id.next_refill));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("ARV Refill");

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
                new EncounterDAO(this).delete(id);

                //Log delete record in the monitor for replication on the server
                String entityId = Long.toString(patientId) + "#" + dateVisit.toString();
                new MonitorDAO(this).save(facilityId, entityId, "encounter", 3);

                Intent intent = new Intent(this, SearchEncounterActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_new:
                clearPreferences();
                intent = new Intent(this, EncounterActivity.class);
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

    public void addItemsMedicineSpinners() {
        //Get ARV
        Devolve devolve = new DevolveDAO(this).getLastDevolve(facilityId, patientId);
        regimentype = devolve.getRegimentype() == null? "" : devolve.getRegimentype();
        regimen1 = devolve.getRegimen() == null? "" : devolve.getRegimen();

        int regimentypeId = 0;
        List<String> list = new ArrayList<String>();
        if(!regimentype.isEmpty()) {
            regimentypeId = new RegimenDAO(this).getRegimentypeId(regimentype);
            list =  new RegimenDAO(this).getRegimens(regimentypeId);
        }
        else {
            list =  new RegimenDAO(this).getARV();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        medicine1.setAdapter(adapter);
        Spinner spinner = (Spinner) findViewById(R.id.regimen1);
        spinner.setSelection(SpinnerUtil.getIndex(spinner, regimen1));


        //Get Cotrim
        regimentypeId = 8;
        list =  new RegimenDAO(this).getRegimens(regimentypeId);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        medicine2.setAdapter(adapter);

        //Get IPT
        regimentypeId = 15;
        list =  new RegimenDAO(this).getRegimens(regimentypeId);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        medicine3.setAdapter(adapter);

        //Get other medicines
        list =  new RegimenDAO(this).getOtherMedicines();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        medicine4.setAdapter(adapter);
    }

    @Override
    public void onSaveInstanceState(Bundle saveInstanceState) {
        super.onSaveInstanceState(saveInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
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
        extractViewData();
        if(dateVisit != null) {
            EncounterDAO encounterDAO = new EncounterDAO(this);
            if(id != 0 ) {
                encounterDAO.update(id, facilityId, patientId, dateVisit, question1, question2, question3, question4, question5, question6, question7, question8, question9, regimen1, regimen2, regimen3, regimen4,
                        duration1, duration2, duration3, duration4, prescribed1, prescribed2, prescribed3, prescribed4, dispensed1, dispensed2, dispensed3, dispensed4, notes, nextRefill, regimentype);
                FancyToast.makeText(getApplicationContext(), "Client encounter data updated", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();

            }
            else {
                encounterDAO.save(facilityId, patientId, dateVisit, question1, question2, question3, question4, question5, question6, question7, question8, question9, regimen1, regimen2, regimen3, regimen4,
                        duration1, duration2, duration3, duration4, prescribed1, prescribed2, prescribed3, prescribed4, dispensed1, dispensed2, dispensed3, dispensed4, notes, nextRefill, regimentype);
               // Toast.makeText(DrugRefillActivity.this, "Client encounter data saved", Toast.LENGTH_LONG).show();
                FancyToast.makeText(getApplicationContext(), "Client encounter data saved", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();

            }
            clearPreferences();
            Intent intent = new Intent(this, ClientProfileActivity.class);
            if(EDIT_MODE) intent = new Intent(this, SearchEncounterActivity.class);
            startActivity(intent);
        }
        else {
          //  Toast.makeText(DrugRefillActivity.this, "Please enter date of encounter", Toast.LENGTH_LONG).show();
            FancyToast.makeText(getApplicationContext(), "Please enter date of encounter", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();

        }
    }

    private void savePreferences() {
        // We need an Editor object to make preference changes. All objects are from android.context.Context
        extractViewData();
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("dateVisit", DateUtil.parseDateToString(dateVisit, "dd/MM/yyyy"));
        editor.putString("question1", question1);
        editor.putString("question2", question2);
        editor.putString("question3", question3);
        editor.putString("question4", question4);
        editor.putString("question5", question5);
        editor.putString("question6", question6);
        editor.putString("question7", question7);
        editor.putString("regimen1", regimen1);
        editor.putString("regimen2", regimen2);
        editor.putString("regimen3", regimen3);
        editor.putString("regimen4", regimen4);
        editor.putString("duration1", duration1 == null? "" : Integer.toString(duration1));
        editor.putString("duration2", duration2 == null? "" : Integer.toString(duration2));
        editor.putString("duration3", duration3 == null? "" : Integer.toString(duration3));
        editor.putString("duration4", duration4 == null? "" : Integer.toString(duration4));
        editor.putString("prescribed1", prescribed1 == null? "" : Integer.toString(prescribed1));
        editor.putString("prescribed2", prescribed2 == null? "" : Integer.toString(prescribed2));
        editor.putString("prescribed3", prescribed3 == null? "" : Integer.toString(prescribed3));
        editor.putString("prescribed4", prescribed4 == null? "" : Integer.toString(prescribed4));
        editor.putString("dispensed1", dispensed1 == null? "" : Integer.toString(dispensed1));
        editor.putString("dispensed2", dispensed2 == null? "" : Integer.toString(dispensed2));
        editor.putString("dispensed3", dispensed3 == null? "" : Integer.toString(dispensed3));
        editor.putString("dispensed4", dispensed4 == null? "" : Integer.toString(dispensed4));
        editor.putString("nextRefill", DateUtil.parseDateToString(nextRefill, "dd/MM/yyyy"));
        editor.putString("regimentype", regimentype);
        editor.commit();
    }

    private void restorePreferences() {
        EDIT_MODE = preferences.getBoolean("edit_mode", false);
        String json = preferences.getString("patient", "");
        patient = new Gson().fromJson(json, Patient.class);
        facilityId = patient.getFacilityId();
        patientId = patient.getFacilityId();

        id = preferences.getInt("id", 0);
        dateVisit = DateUtil.parseStringToDate(preferences.getString("dateVisit",  ""), "dd/MM/yyyy");   //dateVisit = DateUtil.unixTimestampToDate(preferences.getLong("dateVisit",  new Date().getTime())/1000L);
        question5 = preferences.getString("question5", "");
        question6 = preferences.getString("question6", "");
        question7 = preferences.getString("question7", "");
        question8 = preferences.getString("question8", "");
        question9 = preferences.getString("question9", "");
        regimen1 = preferences.getString("regimen1", "");
        regimen2 = preferences.getString("regimen2", "");
        regimen3 = preferences.getString("regimen3", "");
        regimen4 = preferences.getString("regimen4", "");

        String value = preferences.getString("duration1", "");
        duration1 = value.trim().isEmpty()? null : Integer.parseInt(value);
        value = preferences.getString("duration2", "");
        duration2 = value.trim().isEmpty()? null : Integer.parseInt(value);
        value = preferences.getString("duration3", "");
        duration3 = value.trim().isEmpty()? null : Integer.parseInt(value);
        value = preferences.getString("duration4", "");
        duration4 = value.trim().isEmpty()? null : Integer.parseInt(value);

        value = preferences.getString("prescribed1", "");
        prescribed1 = value.trim().isEmpty()? null : Integer.parseInt(value);
        value = preferences.getString("prescribed2", "");
        prescribed2 = value.trim().isEmpty()? null : Integer.parseInt(value);
        value = preferences.getString("prescribed3", "");
        prescribed3 = value.trim().isEmpty()? null : Integer.parseInt(value);
        value = preferences.getString("prescribed4", "");
        prescribed4 = value.trim().isEmpty()? null : Integer.parseInt(value);

        value = preferences.getString("dispensed1", "");
        dispensed1 = value.trim().isEmpty()? null : Integer.parseInt(value);
        value = preferences.getString("dispensed2", "");
        dispensed2 = value.trim().isEmpty()? null : Integer.parseInt(value);
        value = preferences.getString("dispensed3", "");
        dispensed3 = value.trim().isEmpty()? null : Integer.parseInt(value);
        value = preferences.getString("dispensed4", "");
        dispensed4 = value.trim().isEmpty()? null : Integer.parseInt(value);
        nextRefill = DateUtil.parseStringToDate(preferences.getString("nextRefill",  ""), "dd/MM/yyyy");
        regimentype = preferences.getString("regimentype", "");

        EditText editText = (EditText) findViewById(R.id.date_visit);
        editText.setText(DateUtil.parseDateToString(dateVisit, "dd/MM/yyyy"));
        Spinner spinner = (Spinner) findViewById(R.id.question5);
        spinner.setSelection(SpinnerUtil.getIndex(spinner, question5));
        spinner = (Spinner) findViewById(R.id.question6);
        spinner.setSelection(SpinnerUtil.getIndex(spinner, question6));
        spinner = (Spinner) findViewById(R.id.question7);
        spinner.setSelection(SpinnerUtil.getIndex(spinner, question7));
        spinner = (Spinner) findViewById(R.id.question8);
        spinner.setSelection(SpinnerUtil.getIndex(spinner, question8));
        spinner = (Spinner) findViewById(R.id.question9);
        spinner.setSelection(SpinnerUtil.getIndex(spinner, question9));
        spinner = (Spinner) findViewById(R.id.regimen1);
        spinner.setSelection(SpinnerUtil.getIndex(spinner, regimen1));
        spinner = (Spinner) findViewById(R.id.regimen2);
        spinner.setSelection(SpinnerUtil.getIndex(spinner, regimen2));
        spinner = (Spinner) findViewById(R.id.regimen3);
        spinner.setSelection(SpinnerUtil.getIndex(spinner, regimen3));
        spinner = (Spinner) findViewById(R.id.regimen4);
        spinner.setSelection(SpinnerUtil.getIndex(spinner, regimen4));
        editText = (EditText) findViewById(R.id.duration1);
        editText.setText(duration1 == null? "" : Integer.toString(duration1));
        editText = (EditText) findViewById(R.id.duration2);
        editText.setText(duration2 == null? "" : Integer.toString(duration2));
        editText = (EditText) findViewById(R.id.duration3);
        editText.setText(duration3 == null? "" : Integer.toString(duration3));
        editText = (EditText) findViewById(R.id.duration4);
        editText.setText(duration4 == null? "" : Integer.toString(duration4));
        editText = (EditText) findViewById(R.id.prescribed1);
        editText.setText(prescribed1 == null? "" : Integer.toString(prescribed1));
        editText = (EditText) findViewById(R.id.prescribed2);
        editText.setText(prescribed2 == null? "" : Integer.toString(prescribed2));
        editText = (EditText) findViewById(R.id.prescribed3);
        editText.setText(prescribed3 == null? "" : Integer.toString(prescribed3));
        editText = (EditText) findViewById(R.id.prescribed4);
        editText.setText(prescribed4 == null? "" : Integer.toString(prescribed4));
        editText = (EditText) findViewById(R.id.dispensed1);
        editText.setText(dispensed1 == null? "" : Integer.toString(dispensed1));
        editText = (EditText) findViewById(R.id.dispensed2);
        editText.setText(dispensed2 == null? "" : Integer.toString(dispensed2));
        editText = (EditText) findViewById(R.id.dispensed3);
        editText.setText(dispensed3 == null? "" : Integer.toString(dispensed3));
        editText = (EditText) findViewById(R.id.dispensed4);
        editText.setText(dispensed4 == null? "" : Integer.toString(dispensed4));
        editText = (EditText) findViewById(R.id.notes);
        editText.setText(notes);
        editText = (EditText) findViewById(R.id.next_refill);
        editText.setText(DateUtil.parseDateToString(nextRefill, "dd/MM/yyyy"));
    }

    private void extractViewData() {
        dateVisit = DateUtil.parseStringToDate(((EditText) findViewById(R.id.date_visit)).getText().toString(), "dd/MM/yyyy");
        question5 = String.valueOf(((Spinner) findViewById(R.id.question5)).getSelectedItem());
        question6 = String.valueOf(((Spinner) findViewById(R.id.question6)).getSelectedItem());
        question7 = String.valueOf(((Spinner) findViewById(R.id.question7)).getSelectedItem());
        question8 = String.valueOf(((Spinner) findViewById(R.id.question8)).getSelectedItem());
        question9 = String.valueOf(((Spinner) findViewById(R.id.question9)).getSelectedItem());
        regimen1 = String.valueOf(((Spinner) findViewById(R.id.regimen1)).getSelectedItem());
        regimen2 = String.valueOf(((Spinner) findViewById(R.id.regimen2)).getSelectedItem());
        regimen3 = String.valueOf(((Spinner) findViewById(R.id.regimen3)).getSelectedItem());
        regimen4 = String.valueOf(((Spinner) findViewById(R.id.regimen4)).getSelectedItem());

        String value = ((EditText) findViewById(R.id.duration1)).getText().toString();
        duration1 = value.trim().isEmpty()? null : Integer.parseInt(value);
        value = ((EditText) findViewById(R.id.duration2)).getText().toString();
        duration2 = value.trim().isEmpty()? null : Integer.parseInt(value);
        value = ((EditText) findViewById(R.id.duration3)).getText().toString();
        duration3 = value.trim().isEmpty()? null : Integer.parseInt(value);
        value = ((EditText) findViewById(R.id.duration4)).getText().toString();
        duration4 = value.trim().isEmpty()? null : Integer.parseInt(value);
        value = ((EditText) findViewById(R.id.prescribed1)).getText().toString();
        prescribed1 = value.trim().isEmpty()? null : Integer.parseInt(value);
        value = ((EditText) findViewById(R.id.prescribed2)).getText().toString();
        prescribed2 = value.trim().isEmpty()? null : Integer.parseInt(value);
        value = ((EditText) findViewById(R.id.prescribed3)).getText().toString();
        prescribed3 = value.trim().isEmpty()? null : Integer.parseInt(value);
        value = ((EditText) findViewById(R.id.prescribed4)).getText().toString();
        prescribed4 = value.trim().isEmpty()? null : Integer.parseInt(value);
        value = ((EditText) findViewById(R.id.dispensed1)).getText().toString();
        dispensed1 = value.trim().isEmpty()? null : Integer.parseInt(value);
        value = ((EditText) findViewById(R.id.dispensed2)).getText().toString();
        dispensed2 = value.trim().isEmpty()? null : Integer.parseInt(value);
        value = ((EditText) findViewById(R.id.dispensed3)).getText().toString();
        dispensed3 = value.trim().isEmpty()? null : Integer.parseInt(value);
        value = ((EditText) findViewById(R.id.dispensed4)).getText().toString();
        dispensed4 = value.trim().isEmpty()? null : Integer.parseInt(value);
        notes = ((EditText) findViewById(R.id.notes)).getText().toString();
        nextRefill = DateUtil.parseStringToDate(((EditText) findViewById(R.id.next_refill)).getText().toString(), "dd/MM/yyyy");
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
