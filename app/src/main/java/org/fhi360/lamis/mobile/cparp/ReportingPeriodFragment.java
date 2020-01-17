package org.fhi360.lamis.mobile.cparp;


import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v4.app.Fragment;
//import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.shashank.sony.fancytoastlib.FancyToast;

import org.fhi360.lamis.mobile.cparp.R;
import org.fhi360.lamis.mobile.cparp.utility.DateUtil;

import java.util.ArrayList;
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReportingPeriodFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private int month;
    private int year;

    private View layout;
    private Context context;
    private Button okButton;

    private Spinner yearSpinner;
    private Spinner monthSpinner;

    private SQLiteOpenHelper databaseHelper;

    public ReportingPeriodFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        layout = inflater.inflate(R.layout.fragment_reporting_period, container, false);
        context = inflater.getContext();

        ArrayList<String> years = new ArrayList<String>();
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = thisYear; i >= thisYear-5; i--) {
            years.add(Integer.toString(i));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, years);

        yearSpinner =  layout.findViewById(R.id.year);
        yearSpinner.setAdapter(adapter);
        yearSpinner.setOnItemSelectedListener(this);

        monthSpinner =  layout.findViewById(R.id.month);
        monthSpinner.setOnItemSelectedListener(this);

        //Get reference to the save button
        okButton = (Button) layout.findViewById(R.id.ok_button);
        okButton.setOnClickListener(this);
        return layout;
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
    }

    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(context, ServiceSummaryActivity.class);

        String value = String.valueOf(((Spinner) layout.findViewById(R.id.month)).getSelectedItem());
        month = DateUtil.getMonth(value);
        value = String.valueOf(((Spinner) layout.findViewById(R.id.year)).getSelectedItem());
        year = value.trim().isEmpty()? null : Integer.parseInt(value);

        if(month != 0 && year != 0) {
            intent.putExtra("month", month);
            intent.putExtra("year", year);
            startActivity(intent);
        }
        else {
            FancyToast.makeText(getActivity(), "Please enter month and year of reporting", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
      }
    }
}
