package org.fhi360.lamis.mobile.cparp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;

import java.util.ArrayList;

import org.fhi360.lamis.mobile.cparp.adapter.EncounterListAdapter;
import org.fhi360.lamis.mobile.cparp.dao.EncounterDAO;
import org.fhi360.lamis.mobile.cparp.model.Encounter;
import org.fhi360.lamis.mobile.cparp.model.Patient;
import org.fhi360.lamis.mobile.cparp.R;
import org.fhi360.lamis.mobile.cparp.utility.DateUtil;

import static org.fhi360.lamis.mobile.cparp.utility.Constants.PREFERENCES_ENCOUNTER;

public class SearchEncounterActivity extends AppCompatActivity {
    private EncounterListAdapter adapter;
    private ListView encounterListView;
    private ArrayList<Encounter> encounters;
    private Patient patient;
    private SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_encounter);

        this.preferences = getSharedPreferences(PREFERENCES_ENCOUNTER, 0);
        String json = preferences.getString("patient", "");
        patient = new Gson().fromJson(json, Patient.class);
        int facilityId = patient.getFacilityId();
        int patientId = patient.getPatientId();

        encounterListView = findViewById(R.id.encounterlistView);
        encounters = new EncounterDAO(this).getEncounters(facilityId, patientId);
        adapter = new EncounterListAdapter(this, encounters);
        encounterListView.setAdapter(adapter);
        encounterListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                edit(encounters.get(position));
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Search Record");
    }

    @Override
    public void onSaveInstanceState(Bundle saveInstanceState) {
        super.onSaveInstanceState(saveInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    private void edit(Encounter encounter) {
        //Retrieve the encounter to modify and populate variables in Shared Preferences object
        preferences = getSharedPreferences(PREFERENCES_ENCOUNTER, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("edit_mode", true);
        editor.putInt("id", encounter.getId());
        editor.putString("dateVisit", DateUtil.parseDateToString(encounter.getDateVisit(), "dd/MM/yyyy"));
        editor.putString("question1", encounter.getQuestion1());
        editor.putString("question2", encounter.getQuestion2());
        editor.putString("question3", encounter.getQuestion3());
        editor.putString("question4", encounter.getQuestion4());
        editor.putString("question5", encounter.getQuestion5());
        editor.putString("question6", encounter.getQuestion6());
        editor.putString("question7", encounter.getQuestion7());
        editor.putString("question8", encounter.getQuestion8());
        editor.putString("question9", encounter.getQuestion9());
        editor.putString("regimen1", encounter.getRegimen1());
        editor.putString("regimen2", encounter.getRegimen2());
        editor.putString("regimen3", encounter.getRegimen3());
        editor.putString("regimen4", encounter.getRegimen4());
        editor.putString("duration1", (Integer) encounter.getDuration1() == null? "" : Integer.toString(encounter.getDuration1()));
        editor.putString("duration2", (Integer) encounter.getDuration2() == null? "" : Integer.toString(encounter.getDuration2()));
        editor.putString("duration3", (Integer) encounter.getDuration3() == null? "" : Integer.toString(encounter.getDuration3()));
        editor.putString("duration4", (Integer) encounter.getDuration4() == null? "" : Integer.toString(encounter.getDuration4()));
        editor.putString("prescribed1", (Integer) encounter.getPrescribed1() == null? "" : Integer.toString(encounter.getPrescribed1()));
        editor.putString("prescribed2", (Integer) encounter.getPrescribed2() == null? "" : Integer.toString(encounter.getPrescribed2()));
        editor.putString("prescribed3", (Integer) encounter.getPrescribed3() == null? "" : Integer.toString(encounter.getPrescribed3()));
        editor.putString("prescribed4", (Integer) encounter.getPrescribed4() == null? "" : Integer.toString(encounter.getPrescribed4()));
        editor.putString("dispensed1", (Integer) encounter.getDispensed1() == null? "" : Integer.toString(encounter.getDispensed1()));
        editor.putString("dispensed2",(Integer) encounter.getDispensed2() == null? "" : Integer.toString(encounter.getDispensed2()));
        editor.putString("dispensed3", (Integer) encounter.getDispensed3() == null? "" : Integer.toString(encounter.getDispensed3()));
        editor.putString("dispensed4", (Integer) encounter.getDispensed4() == null? "" : Integer.toString(encounter.getDispensed4()));
        editor.putString("notes", encounter.getNotes());
        editor.putString("nextRefill", DateUtil.parseDateToString(encounter.getNextRefill(), "dd/MM/yyyy"));
        editor.commit();
        Intent intent = new Intent(this, EncounterActivity.class);
        startActivity(intent);
    }
}
