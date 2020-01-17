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

import org.fhi360.lamis.mobile.cparp.adapter.ClientTrackingListAdapter;
import org.fhi360.lamis.mobile.cparp.model.Patient;
import org.fhi360.lamis.mobile.cparp.R;
import org.fhi360.lamis.mobile.cparp.dao.AppointmentDAO;
import org.fhi360.lamis.mobile.cparp.model.Appointment;
import org.fhi360.lamis.mobile.cparp.utility.DateUtil;

import java.util.ArrayList;

import static org.fhi360.lamis.mobile.cparp.utility.Constants.PREFERENCES_ENCOUNTER;

public class  SearchClientTrackingActivity extends AppCompatActivity {
    private Cursor cursor;
    private SQLiteDatabase db;
    private ClientTrackingListAdapter adapter;
    private ListView clientTrackinglistView;
    private ArrayList<Appointment> appointments;

    private Patient patient;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_client_tracking);

        this.preferences = getSharedPreferences(PREFERENCES_ENCOUNTER, 0);
        String json = preferences.getString("patient", "");
        patient = new Gson().fromJson(json, Patient.class);
        int facilityId = patient.getFacilityId();
        int patientId = patient.getPatientId();

        clientTrackinglistView = (ListView) findViewById(R.id.clientTrackinglistView);
        appointments = new AppointmentDAO(this).getAppointments(facilityId, patientId);
        adapter = new ClientTrackingListAdapter(this, appointments);
        clientTrackinglistView.setAdapter(adapter);
        clientTrackinglistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                edit(appointments.get(position));
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

    private void edit(Appointment appointment) {
        preferences = getSharedPreferences(PREFERENCES_ENCOUNTER, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("edit_mode", true);

        editor.putInt("id", appointment.getId());
        editor.putString("dateTracked", DateUtil.parseDateToString(appointment.getDateTracked(), "dd/MM/yyyy"));
        editor.putString("typeTracking", appointment.getTypeTracking());
        editor.putString("trackingOutcome", appointment.getTrackingOutcome());
        editor.putString("dateAgreed", DateUtil.parseDateToString(appointment.getDateAgreed(), "dd/MM/yyyy"));
        editor.putString("dateLastVisit", DateUtil.parseDateToString(appointment.getDateLastVisit(), "dd/MM/yyyy"));
        editor.putString("dateNextVisit", DateUtil.parseDateToString(appointment.getDateNextVisit(), "dd/MM/yyyy"));
        editor.commit();

        Intent intent = new Intent(this, ClientTrackingActivity.class);
        startActivity(intent);
    }
}
