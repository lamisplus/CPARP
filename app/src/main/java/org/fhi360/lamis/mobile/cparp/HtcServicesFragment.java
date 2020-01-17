package org.fhi360.lamis.mobile.cparp;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
//import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.shashank.sony.fancytoastlib.FancyToast;

import org.fhi360.lamis.mobile.cparp.R;
import org.fhi360.lamis.mobile.cparp.dao.AccountDAO;
import org.fhi360.lamis.mobile.cparp.dao.HtcDAO;
import org.fhi360.lamis.mobile.cparp.dao.MonitorDAO;
import org.fhi360.lamis.mobile.cparp.model.Htc;
import org.fhi360.lamis.mobile.cparp.utility.DateUtil;
import org.fhi360.lamis.mobile.cparp.utility.SpinnerUtil;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class HtcServicesFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private Integer numTested;
    private Integer numPositive;
    private Integer numReferred;
    private Integer numOnsiteVisit;
    private int month;
    private int year;
    private int htcId;
    private View layout;
    private Context context;
    private Button saveButton;
    private Spinner yearSpinner;
    private Spinner monthSpinner;
    private boolean EDIT_MODE;
    public HtcServicesFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        layout = inflater.inflate(R.layout.fragment_htc_services, container, false);
        context = inflater.getContext();

        ArrayList<String> years = new ArrayList<String>();
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = thisYear; i >= thisYear-5; i--) {
            years.add(Integer.toString(i));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, years);

        yearSpinner = (Spinner) layout.findViewById(R.id.year);
        yearSpinner.setAdapter(adapter);
        yearSpinner.setOnItemSelectedListener(this);

        monthSpinner = (Spinner) layout.findViewById(R.id.month);
        monthSpinner.setOnItemSelectedListener(this);

        //Get reference to the save button
        saveButton = (Button) layout.findViewById(R.id.save_button);
        saveButton.setOnClickListener(this);
        return layout;
    }

    //Create the menu option
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_htcservice, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                new HtcDAO(context).delete(htcId);
                //Log delete record in the monitor for replication on the server
                int communitypharmId = new AccountDAO(context).getAccountId();
                String entityId = Integer.toString(month) + "#" + Integer.toString(year);
                new MonitorDAO(context).save(communitypharmId, entityId, "htc", 3);

                EDIT_MODE = false;
                getActivity().invalidateOptionsMenu();
                clearViews();
                return true;
            case R.id.action_new:
                EDIT_MODE = false;
                getActivity().invalidateOptionsMenu();
                clearViews();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onPrepareOptionsMenu(Menu menu) {
        if(!EDIT_MODE) {
            menu.findItem(R.id.action_delete).setVisible(false);
            menu.findItem(R.id.action_new).setVisible(false);
        }
        super.onPrepareOptionsMenu(menu);
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        String value = String.valueOf(((Spinner) layout.findViewById(R.id.month)).getSelectedItem());
        month = DateUtil.getMonth(value);
        value = String.valueOf(((Spinner) layout.findViewById(R.id.year)).getSelectedItem());
        year = value.trim().isEmpty()? null : Integer.parseInt(value);

        HtcDAO htcDAO = new HtcDAO(context);
        htcId = htcDAO.getId(month, year);
        if(htcId != 0 ) {
            EDIT_MODE = true;
            getActivity().invalidateOptionsMenu();
            Htc htc = htcDAO.getHtc(htcId);
            numTested = htc.getNumTested();
            numPositive = htc.getNumPositive();
            numReferred = htc.getNumReferred();
            numOnsiteVisit = htc.getNumOnsiteVisit();
            restoreViews();
        }
        else {
            EditText editText = (EditText) layout.findViewById(R.id.num_tested);
            editText.setText("");
            editText = (EditText) layout.findViewById(R.id.num_positive);
            editText.setText("");
            editText = (EditText) layout.findViewById(R.id.num_referred);
            editText.setText("");
            editText = (EditText) layout.findViewById(R.id.num_onsite_visit);
            editText.setText("");
        }
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
        String value = ((EditText) layout.findViewById(R.id.num_tested)).getText().toString();
        numTested = value.trim().isEmpty()? null : Integer.parseInt(value);             //Integer.intValue()
        value = ((EditText) layout.findViewById(R.id.num_positive)).getText().toString();
        numPositive = value.trim().isEmpty()? null : Integer.parseInt(value);
        value = ((EditText) layout.findViewById(R.id.num_referred)).getText().toString();
        numReferred = value.trim().isEmpty()? null : Integer.parseInt(value);
        value = ((EditText) layout.findViewById(R.id.num_onsite_visit)).getText().toString();
        numOnsiteVisit = value.trim().isEmpty()? null : Integer.parseInt(value);
        value = String.valueOf(((Spinner) layout.findViewById(R.id.month)).getSelectedItem());
        month = DateUtil.getMonth(value);
        value = String.valueOf(((Spinner) layout.findViewById(R.id.year)).getSelectedItem());
        year = value.trim().isEmpty()? null : Integer.parseInt(value);
        if(month != 0 && year != 0) {
            HtcDAO htcDAO = new HtcDAO(context);
            int id = htcDAO.getId(month, year);
            if(id != 0 ) {
                htcDAO.update(id, numTested, numPositive, numReferred, numOnsiteVisit, month, year);
                FancyToast.makeText(getActivity(), "HTC Service data updated", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();

            }
            else {
                htcDAO.save(numTested, numPositive, numReferred, numOnsiteVisit, month, year);
                FancyToast.makeText(getActivity(), "HTC Service data updated", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();

                clearViews();
            }
        }
        else {
            FancyToast.makeText(getActivity(), "Please enter month and year of reporting", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
       }
    }

    private void restoreViews() {
        EditText editText =  layout.findViewById(R.id.num_tested);
        editText.setText(Integer.toString(numTested));
        editText = layout.findViewById(R.id.num_positive);
        editText.setText(Integer.toString(numPositive));
        editText =  layout.findViewById(R.id.num_referred);
        editText.setText(Integer.toString(numReferred));
        editText =  layout.findViewById(R.id.num_onsite_visit);
        editText.setText(Integer.toString(numOnsiteVisit));
    }

    private void clearViews() {
        EditText editText =  layout.findViewById(R.id.num_tested);
        editText.setText("");
        editText =  layout.findViewById(R.id.num_positive);
        editText.setText("");
        editText =  layout.findViewById(R.id.num_referred);
        editText.setText("");
        editText = layout.findViewById(R.id.num_onsite_visit);
        editText.setText("");
        Spinner spinner =  layout.findViewById(R.id.year);
        spinner.setSelection(SpinnerUtil.getIndex(spinner, ""));
        spinner =  layout.findViewById(R.id.month);
        spinner.setSelection(SpinnerUtil.getIndex(spinner, ""));
    }
}
