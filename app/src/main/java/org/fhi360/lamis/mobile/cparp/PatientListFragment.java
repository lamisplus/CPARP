package org.fhi360.lamis.mobile.cparp;


import java.util.ArrayList;

import android.app.Activity;
import android.support.v4.app.Fragment;
//import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;

import org.fhi360.lamis.mobile.cparp.adapter.PatientListAdapter;
import org.fhi360.lamis.mobile.cparp.dao.PatientDAO;
import org.fhi360.lamis.mobile.cparp.model.Patient;
import org.fhi360.lamis.mobile.cparp.R;

import static org.fhi360.lamis.mobile.cparp.utility.Constants.PREFERENCES_ENCOUNTER;


/**
 * A simple {@link Fragment} subclass.
 */
public class PatientListFragment extends Fragment {
    private ArrayList<Patient> patients;
    private PatientListAdapter adapter;
    private ListView patientListView;
    private String PATIENTS = "patients";
    private View layout;
    private Context context;

    private SharedPreferences preferences;

    public PatientListFragment() {  }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.fragment_patient_list, container, false);
        context = inflater.getContext();

        preferences = getActivity().getSharedPreferences(PREFERENCES_ENCOUNTER, 0);

        // Inflate the layout for this fragment
        patientListView = (ListView) layout.findViewById(R.id.patientlistView);
        patients = new PatientDAO(context).getPatients();
        adapter = new PatientListAdapter((Activity) context, patients);
        patientListView.setAdapter(adapter);
        patientListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                savePreferences(patients.get(position));
                Intent intent = new Intent(context, ClientProfileActivity.class);
                startActivity(intent);
            }
        });
        return layout;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        //Get a shared preferences object to hold data between activities
        preferences = getActivity().getSharedPreferences(PREFERENCES_ENCOUNTER, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    //This method is called by the onTextChanged method of the TextWatcher in MainActivity
    public void refreshListView(CharSequence s) {
        if (!s.toString().equals("")) {
            patients = new PatientDAO(context).getPatients(s.toString());
        }
        else {
            patients = new PatientDAO(context).getPatients();
        }
        adapter = new PatientListAdapter((Activity) context, patients);
        patientListView.setAdapter(adapter);
    }

    private void savePreferences(Patient patient) {
        preferences =  getActivity().getSharedPreferences(PREFERENCES_ENCOUNTER, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("patient", new Gson().toJson(patient));
        editor.putBoolean("edit_mode", false);
        editor.commit();
    }
}
