package org.fhi360.lamis.mobile.cparp;

import android.app.Activity;
import android.support.v4.app.Fragment;
//import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;

import org.fhi360.lamis.mobile.cparp.adapter.DefaulterListAdapter;
import org.fhi360.lamis.mobile.cparp.dao.PatientDAO;
import org.fhi360.lamis.mobile.cparp.model.Patient;
import org.fhi360.lamis.mobile.cparp.R;

import java.util.ArrayList;

import static org.fhi360.lamis.mobile.cparp.utility.Constants.PREFERENCES_ENCOUNTER;


public class DefaulterListFragment extends Fragment {
    private Cursor cursor;
    private SQLiteDatabase db;
    private View layout;
    private DefaulterListAdapter adapter;
    private ListView defaulterListView;
    private ArrayList<Patient> patients;
    private Context context;
    private int patientId;
    private int facilityId;
    private SharedPreferences preferences;
    public DefaulterListFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.fragment_defaulter_list, container, false);
        context = inflater.getContext();

        // Inflate the layout for this fragment
        defaulterListView = (ListView) layout.findViewById(R.id.defaulterlistView);
        patients = new PatientDAO(context).getDefaulters();
        adapter = new DefaulterListAdapter((Activity) context, patients);
        defaulterListView.setAdapter(adapter);
        defaulterListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                savePreferences(patients.get(position));
                Intent intent = new Intent(context, ClientTrackingActivity.class);
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
            patients = new PatientDAO(context).getDefaulters(s.toString());
        }
        else {
            patients = new PatientDAO(context).getDefaulters();
        }
        adapter = new DefaulterListAdapter((Activity) context, patients);
        defaulterListView.setAdapter(adapter);
    }

    private void savePreferences(Patient patient) {
        preferences =  getActivity().getSharedPreferences(PREFERENCES_ENCOUNTER, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("patient", new Gson().toJson(patient));
        editor.putBoolean("edit_mode", false);
        editor.commit();

    }

}
