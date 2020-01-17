package org.fhi360.lamis.mobile.cparp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.fhi360.lamis.mobile.cparp.dao.DevolveDAO;
import org.fhi360.lamis.mobile.cparp.model.Patient;
import org.fhi360.lamis.mobile.cparp.R;
import org.fhi360.lamis.mobile.cparp.utility.DateUtil;
import org.fhi360.lamis.mobile.cparp.utility.EditTextDatePicker;

import java.util.Date;

import static org.fhi360.lamis.mobile.cparp.utility.Constants.PREFERENCES_ENCOUNTER;

public class DiscontinueServiceActivity extends AppCompatActivity {
    private int id;
    private int facilityId;
    private int patientId;
    private Date dateDiscontinued;
    private String reasonDiscontinued;

    private Patient patient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discontinue_service);

        SharedPreferences preferences = getSharedPreferences(PREFERENCES_ENCOUNTER, 0);
        String json = preferences.getString("patient", "");
        patient = new Gson().fromJson(json, Patient.class);
        facilityId = patient.getFacilityId();
        patientId = patient.getPatientId();

        //Set date picker on the edit text
        new EditTextDatePicker(this, (EditText) findViewById(R.id.date_discontinued));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Discontinue Service");
    }

    @Override
    public void onSaveInstanceState(Bundle saveInstanceState) {
        super.onSaveInstanceState(saveInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    public void onClickSaveButton(View view) {
        dateDiscontinued = DateUtil.parseStringToDate(((EditText) findViewById(R.id.date_discontinued)).getText().toString(), "dd/MM/yyyy");
        reasonDiscontinued = String.valueOf(((Spinner) findViewById(R.id.reason_discontinued)).getSelectedItem());

        if(dateDiscontinued != null) {
            new DevolveDAO(this).discontinue(facilityId, patientId, dateDiscontinued, reasonDiscontinued);
             FancyToast.makeText(getApplicationContext(), "Client discontinued from service", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
            Intent intent = new Intent(this, ClientProfileActivity.class);
            startActivity(intent);
        }
        else {
            FancyToast.makeText(getApplicationContext(), "Please enter date discontinued from service", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
   }
    }

}
