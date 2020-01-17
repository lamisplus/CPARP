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
import org.fhi360.lamis.mobile.cparp.adapter.AdrSpinnerAdapter;
import org.fhi360.lamis.mobile.cparp.adapter.MedicationErrorSpinnerAdapter;
import org.fhi360.lamis.mobile.cparp.adapter.OiSpinnerAdapter;
import org.fhi360.lamis.mobile.cparp.dao.DrugtherapyDAO;
import org.fhi360.lamis.mobile.cparp.dao.MonitorDAO;
import org.fhi360.lamis.mobile.cparp.model.Adr;
import org.fhi360.lamis.mobile.cparp.model.Medierror;
import org.fhi360.lamis.mobile.cparp.model.Patient;
import org.fhi360.lamis.mobile.cparp.R;
import org.fhi360.lamis.mobile.cparp.model.Oi;
import org.fhi360.lamis.mobile.cparp.utility.DateUtil;
import org.fhi360.lamis.mobile.cparp.utility.EditTextDatePicker;
import org.fhi360.lamis.mobile.cparp.utility.SpinnerUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import static org.fhi360.lamis.mobile.cparp.utility.Constants.PREFERENCES_ENCOUNTER;

public class DrugTherapyActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private int id;
    private int facilityId;
    private int patientId;
    private Date dateVisit;
    private String ois;
    private String therapyProblemScreened;
    private String adherenceIssues;
    private String medicationError;
    private String adrs;
    private String severity;
    private String icsrForm;
    private String adrReferred;

    private  ArrayList<Oi> listOis;
    private ArrayList<Medierror> listErrors;
    private ArrayList<Adr> listAdrs;

    private LinearLayout layoutOiYes;
    private LinearLayout layoutAdrYes;
    private LinearLayout layoutMedicationErrorYes;
    private Spinner medicationErrorSpinner;
    private Spinner adrsSpinner;
    private Spinner oisSpinner;

    private Spinner mediErrorSpinner;
    private Spinner oiSpinner;
    private Spinner adrSpinner;
    private OiSpinnerAdapter oiAdapter;
    private MedicationErrorSpinnerAdapter errorAdapter;
    private AdrSpinnerAdapter adrAdapter;

    private Patient patient;
    private boolean EDIT_MODE;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drug_therapy);

        this.preferences = getSharedPreferences(PREFERENCES_ENCOUNTER, 0);
        restorePreferences();

        //Set date picker on the edit text
        new EditTextDatePicker(this, (EditText) findViewById(R.id.date_visit));

        layoutOiYes = (LinearLayout) findViewById(R.id.oi_yes);
        layoutAdrYes = (LinearLayout) findViewById(R.id.adr_yes);
        layoutMedicationErrorYes = (LinearLayout) findViewById(R.id.medicationError_yes);

        oisSpinner = (Spinner) findViewById(R.id.ois);
        oisSpinner.setOnItemSelectedListener(this);

        medicationErrorSpinner = (Spinner) findViewById(R.id.medication_error);
        medicationErrorSpinner.setOnItemSelectedListener(this);

        adrsSpinner = (Spinner) findViewById(R.id.adrs);
        adrsSpinner.setOnItemSelectedListener(this);

        listOis = new ArrayList<>();
        final String[] selectOi = {"", "Herpes Zoster", "Pneumonia", "Dementia/Encephallis", "Thrush Oral/Vaginal", "Fever", "Cough"};
        for (int i = 0; i < selectOi.length; i++) {
            Oi oi = new Oi();
            oi.setDescription(selectOi[i]);
            if(ois.contains(Integer.toString(i))) {
                oi.setSelected(true);
            }
            else {
                oi.setSelected(false);
            }
            listOis.add(oi);
        }
        oiAdapter = new OiSpinnerAdapter(DrugTherapyActivity.this, 0, listOis);
        oiSpinner = (Spinner) findViewById(R.id.oiSpinner);
        oiSpinner.setAdapter(oiAdapter);

        listErrors = new ArrayList<>();
        final String[] selectError = {"", "Duration &/or frequency of medication inappropriate", "Incorrect dose (low does or high dose prescribed", "Incorrect ARV drugs combinations/regimens prescribed", "No drug for the patients medical problem"};
        for (int i = 0; i < selectError.length; i++) {
            Medierror medierror = new Medierror();
            medierror.setDescription(selectError[i]);
            if(medicationError.contains(Integer.toString(i))) {
                medierror.setSelected(true);
            }
            else {
                medierror.setSelected(false);
            }
            listErrors.add(medierror);
        }

        errorAdapter = new MedicationErrorSpinnerAdapter(DrugTherapyActivity.this, 0, listErrors);
        mediErrorSpinner = (Spinner) findViewById(R.id.mediErrorSpinner);
        mediErrorSpinner.setAdapter(errorAdapter );

        listAdrs = new ArrayList<>();
        final String[] selectAdr = {"", "Nausea/Vomiting", "Headache", "Insomnia/Bad dreams", "Fatigue/weakness", "PV bleeding/Discharge", "Rash", "FAT Changes", "Anemia", "Drainage of liquor", "Stevens Johnson Syndrome", "Hyperglycemia"};
        for (int i = 0; i < selectAdr.length; i++) {
            Adr adr = new Adr();
            adr.setDescription(selectAdr[i]);
            if(adrs.contains(Integer.toString(i))) {
                adr.setSelected(true);
            }
            else {
                adr.setSelected(false);
            }
            listAdrs.add(adr);
        }

        adrAdapter = new AdrSpinnerAdapter(DrugTherapyActivity.this, 0, listAdrs);
        adrSpinner = (Spinner) findViewById(R.id.adrSpinner);
        adrSpinner.setAdapter(adrAdapter);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Drug Therapy Monitoring");
    }

    //Create the menu option
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate the menu; this adds items to the action bar if it is present
        getMenuInflater().inflate(R.menu.menu_drugtherapy, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle other ActionBar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_delete:
                new DrugtherapyDAO(this).delete(id);

                //Log delete record in the monitor for replication on the server
                String entityId = Long.toString(patientId) + "#" + dateVisit.toString();
                new MonitorDAO(this).save(facilityId, entityId, "drugtherapy", 3);

                Intent intent = new Intent(this, SearchDrugTherapyActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_new:
                clearPreferences();
                restorePreferences();
                intent = new Intent(this, DrugTherapyActivity.class);
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

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        String ois = String.valueOf(oisSpinner.getSelectedItem());
        if(ois.equalsIgnoreCase("YES")) {
            layoutOiYes.setVisibility(View.VISIBLE);
        }
        else {
            layoutOiYes.setVisibility(View.GONE);
        }

        String medication_error = String.valueOf(medicationErrorSpinner.getSelectedItem());
        if(medication_error.equalsIgnoreCase("YES")) {
            layoutMedicationErrorYes.setVisibility(View.VISIBLE);
        }
        else {
            layoutMedicationErrorYes.setVisibility(View.GONE);
        }

        String adrs = String.valueOf(adrsSpinner.getSelectedItem());
        if(adrs.equalsIgnoreCase("YES")) {
            layoutAdrYes.setVisibility(View.VISIBLE);
        }
        else {
            layoutAdrYes.setVisibility(View.GONE);
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
        extractViewData();

        DrugtherapyDAO drugtherapyDAO = new DrugtherapyDAO(this);
        if(id != 0 ) {
            drugtherapyDAO.update(id, facilityId, patientId, dateVisit, ois, therapyProblemScreened, adherenceIssues, medicationError, adrs, severity, icsrForm, adrReferred);

            FancyToast.makeText(getApplicationContext(), "Client therapy monitoring data updated", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();

        }
        else {
            drugtherapyDAO.save(facilityId, patientId, dateVisit, ois, therapyProblemScreened, adherenceIssues, medicationError, adrs, severity, icsrForm, adrReferred);
            FancyToast.makeText(getApplicationContext(), "Client therapy monitoring data saved", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();

        }
        clearPreferences();
        Intent intent = new Intent(this, ClientProfileActivity.class);
        if(EDIT_MODE) intent = new Intent(this, SearchDrugTherapyActivity.class);
        startActivity(intent);
    }

    public void savePreferences() {
        // We need an Editor object to make preference changes. All objects are from android.context.Context
        extractViewData();
        SharedPreferences.Editor editor = preferences.edit();

        // We need an Editor object to make preference changes. All objects are from android.context.Context
        editor.putString("dateVisit", DateUtil.parseDateToString(dateVisit, "dd/MM/yyyy"));
        editor.putString("ois", ois);
        editor.putString("therapyProblemScreened", therapyProblemScreened);
        editor.putString("adherenceIssues", adherenceIssues);
        editor.putString("medicationError", medicationError);
        editor.putString("adrs", adrs);
        editor.putString("severity", severity);
        editor.putString("icsrForm", icsrForm);
        editor.putString("adrReferred", adrReferred);
        editor.commit();
    }

    public void restorePreferences() {
        // Restore preference
        EDIT_MODE = preferences.getBoolean("edit_mode", false);
        String json = preferences.getString("patient", "");
        patient = new Gson().fromJson(json, Patient.class);
        facilityId = patient.getFacilityId();
        patientId = patient.getPatientId();

        id = preferences.getInt("id", 0);
        dateVisit = DateUtil.parseStringToDate(preferences.getString("dateVisit",  ""), "dd/MM/yyyy");
        ois = preferences.getString("ois", "");
        therapyProblemScreened = preferences.getString("therapyProblemScreened", "");
        adherenceIssues = preferences.getString("adherenceIssues", "");
        medicationError = preferences.getString("medicationError", "");
        adrs = preferences.getString("adrs", "");
        severity = preferences.getString("severity", "");
        icsrForm = preferences.getString("icsrForm", "");
        adrReferred = preferences.getString("adrReferred", "");

        EditText editText = (EditText) findViewById(R.id.date_visit);
        editText.setText(DateUtil.parseDateToString(dateVisit, "dd/MM/yyyy"));
        Spinner spinner = (Spinner) findViewById(R.id.ois);
        spinner.setSelection(SpinnerUtil.getIndex(spinner, ois));
        spinner = (Spinner) findViewById(R.id.therapy_problem_screened);
        spinner.setSelection(SpinnerUtil.getIndex(spinner, therapyProblemScreened));
        spinner = (Spinner) findViewById(R.id.adherence_issues);
        spinner.setSelection(SpinnerUtil.getIndex(spinner, adherenceIssues));
        spinner = (Spinner) findViewById(R.id.medication_error);
        spinner.setSelection(SpinnerUtil.getIndex(spinner, medicationError));
        spinner = (Spinner) findViewById(R.id.adrs);
        spinner.setSelection(SpinnerUtil.getIndex(spinner, adrs));
        spinner = (Spinner) findViewById(R.id.severity);
        spinner.setSelection(SpinnerUtil.getIndex(spinner,severity));
        spinner = (Spinner) findViewById(R.id.icsr_form);
        spinner.setSelection(SpinnerUtil.getIndex(spinner, icsrForm));
        spinner = (Spinner) findViewById(R.id.adr_referred);
        spinner.setSelection(SpinnerUtil.getIndex(spinner, adrReferred));
    }

    private void extractViewData() {
        dateVisit = DateUtil.parseStringToDate(((EditText) findViewById(R.id.date_visit)).getText().toString(), "dd/MM/yyyy");

        int i = 0;
        listOis = oiAdapter.getArrayList();
        for(Iterator<Oi> iterator = listOis.iterator(); iterator.hasNext();) {
            Oi oi = iterator.next();
            if(oi.isSelected()) {
                if(ois.isEmpty()) {
                    ois = Integer.toString(i);
                }
                else {
                    ois = ois + "," + Integer.toString(i);
                }
            }
        }

        i = 0;
        listErrors = errorAdapter.getArrayList();
        for(Iterator<Medierror> iterator = listErrors.iterator(); iterator.hasNext();) {
            Medierror medierror = iterator.next();
            if(medierror.isSelected()) {
                if(ois.isEmpty()) {
                    medicationError = Integer.toString(i);
                }
                else {
                    medicationError = medicationError + "," + Integer.toString(i);
                }
            }
        }

        i = 0;
        listAdrs = adrAdapter.getArrayList();
        for(Iterator<Adr> iterator = listAdrs.iterator(); iterator.hasNext();) {
            Adr adr = iterator.next();
            if(adr.isSelected()) {
                if(ois.isEmpty()) {
                    adrs = Integer.toString(i);
                }
                else {
                    adrs = adrs + "," + Integer.toString(i);
                }
            }
        }

        ois = String.valueOf(((Spinner) findViewById(R.id.ois)).getSelectedItem());
        medicationError = String.valueOf(((Spinner) findViewById(R.id.medication_error)).getSelectedItem());
        adrs = String.valueOf(((Spinner) findViewById(R.id.adrs)).getSelectedItem());
        therapyProblemScreened = String.valueOf(((Spinner) findViewById(R.id.therapy_problem_screened)).getSelectedItem());
        adherenceIssues = String.valueOf(((Spinner) findViewById(R.id.adherence_issues)).getSelectedItem());
        severity = String.valueOf(((Spinner) findViewById(R.id.severity)).getSelectedItem());
        icsrForm = String.valueOf(((Spinner) findViewById(R.id.icsr_form)).getSelectedItem());
        adrReferred = String.valueOf(((Spinner) findViewById(R.id.adr_referred)).getSelectedItem());
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
