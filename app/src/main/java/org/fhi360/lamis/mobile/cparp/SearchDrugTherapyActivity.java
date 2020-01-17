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

import org.fhi360.lamis.mobile.cparp.adapter.DrugTherapyListAdapter;
import org.fhi360.lamis.mobile.cparp.dao.DrugtherapyDAO;
import org.fhi360.lamis.mobile.cparp.model.Patient;
import org.fhi360.lamis.mobile.cparp.R;
import org.fhi360.lamis.mobile.cparp.model.Drugtherapy;
import org.fhi360.lamis.mobile.cparp.utility.DateUtil;

import java.util.ArrayList;

import static org.fhi360.lamis.mobile.cparp.utility.Constants.PREFERENCES_ENCOUNTER;

public class SearchDrugTherapyActivity extends AppCompatActivity {
    private Cursor cursor;
    private SQLiteDatabase db;
    private DrugTherapyListAdapter adapter;
    private ListView drugtherapyListView;
    private ArrayList<Drugtherapy> drugtherapies;

    private Patient patient;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_drug_therapy);

        this.preferences = getSharedPreferences(PREFERENCES_ENCOUNTER, 0);
        String json = preferences.getString("patient", "");
        patient = new Gson().fromJson(json, Patient.class);
        int facilityId = patient.getFacilityId();
        int patientId = patient.getPatientId();

        drugtherapyListView = (ListView) findViewById(R.id.drugtherapylistView);
        drugtherapies = new DrugtherapyDAO(this).getDrugtherapies(facilityId, patientId);
        adapter = new DrugTherapyListAdapter(this, drugtherapies);
        drugtherapyListView.setAdapter(adapter);
        drugtherapyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                edit(drugtherapies.get(position));
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Search Record");
    }

    private void edit(Drugtherapy drugtherapy) {
        //Retrieve the drugtherapy to modify and populate variables in Shared Preferences object
        preferences = getSharedPreferences(PREFERENCES_ENCOUNTER, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("edit_mode", true);

        editor.putInt("id", drugtherapy.getId());
        editor.putString("dateVisit", DateUtil.parseDateToString(drugtherapy.getDateVisit(), "dd/MM/yyyy"));
        editor.putString("ois", drugtherapy.getOis());
        editor.putString("therapyProblemScreened", drugtherapy.getTherapyProblemScreened());
        editor.putString("adherenceIssues", drugtherapy.getAdherenceIssues());
        editor.putString("medicationError", drugtherapy.getMedicationError());
        editor.putString("adrs", drugtherapy.getAdrs());
        editor.putString("severity", drugtherapy.getSeverity());
        editor.putString("icsrForm", drugtherapy.getIcsrForm());
        editor.putString("adrReferred", drugtherapy.getAdrReferred());
        editor.commit();

        Intent intent = new Intent(this, DrugTherapyActivity.class);
        startActivity(intent);
    }

}
