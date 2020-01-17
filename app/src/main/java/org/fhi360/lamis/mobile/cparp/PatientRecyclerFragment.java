package org.fhi360.lamis.mobile.cparp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import org.fhi360.lamis.mobile.cparp.adapter.PatientRecyclerAdapter;
import org.fhi360.lamis.mobile.cparp.model.Patient;
import org.fhi360.lamis.mobile.cparp.R;
import org.fhi360.lamis.mobile.cparp.dao.FacilityDAO;
import org.fhi360.lamis.mobile.cparp.dao.PatientDAO;

import java.util.ArrayList;

import static org.fhi360.lamis.mobile.cparp.utility.Constants.PREFERENCES_ENCOUNTER;

public class PatientRecyclerFragment extends Fragment {
    private ArrayList<Patient> patients;
    private PatientRecyclerAdapter adapter;
    private RecyclerView patientRecycler;
    private String[] profiles;
    private String[] facilities;
    private int[] ids;
    private String PATIENTS = "patients";
    private Context context;

    private SharedPreferences preferences;


    public PatientRecyclerFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = inflater.getContext();

        preferences = getActivity().getSharedPreferences(PREFERENCES_ENCOUNTER, 0);
        // Inflate the layout for this fragment and get the ListView object from the layout
        patientRecycler = (RecyclerView) inflater.inflate(R.layout.fragment_patient_recycler, container, false);
        patients = new PatientDAO(context).getPatients();
        adapterDataSet(patients);

        //Initializing the list view. We will set an Recycler Adapter (containing card view of patients) and a itemClick Listener
        //Setting the list adapter. We fill the adapter with filtered list because that is the list we want to show.
        //Retrieve patient record from database into a List. Then create an adapter and set it to the recycler view on the fragment layout

        adapter = new PatientRecyclerAdapter(profiles, facilities);
        patientRecycler.setAdapter(adapter);

        //Use the LinearLayout manager to arrange the views linearly.
        //To display in a grid use new GridLayoutManager(getActivity, 2) where 2 means display in two columns
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        patientRecycler.setLayoutManager(layoutManager);

        //Create a onItemClickListener to enable the activity to listen for an item click on the recycler view
        //When a card view is clicked, the activity creates an intent for EncounterActivity
        final Gson gson = new Gson();
        adapter.setListener(new PatientRecyclerAdapter.Listener() {
            @Override
            public void onClick(int position) {
                //Get the id of the patient in the position clicked and retrieve the select patient from the patients array
                //and convert the object to JSON representation and put it in the intent for the Encounter activity

                savePreferences(patients.get(position));
                Intent intent = new Intent(context, ClientProfileActivity.class);
                startActivity(intent);
            }
        });
        return patientRecycler;
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
        adapterDataSet(patients);
        adapter = new PatientRecyclerAdapter(profiles, facilities);
        patientRecycler.setAdapter(adapter);
    }

    private void adapterDataSet(ArrayList<Patient> patients) {
        profiles = new String[patients.size()];
        facilities = new String[patients.size()];
        ids = new int[patients.size()];
        for(int i = 0; i < ids.length; i++) {
            ids[i] = patients.get(i).getId();
            profiles[i] = patients.get(i).getSurname() + " " + patients.get(i).getOtherNames();

            int facilityId = patients.get(i).getFacilityId();
            facilities[i] = new FacilityDAO(context).getFacility(facilityId);
        }
    }

    private void savePreferences(Patient patient) {
        preferences =  getActivity().getSharedPreferences(PREFERENCES_ENCOUNTER, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("patient", new Gson().toJson(patient));
        editor.putBoolean("edit_mode", false);
        editor.commit();

    }
}
