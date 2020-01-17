package org.fhi360.lamis.mobile.cparp;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import org.fhi360.lamis.mobile.cparp.dao.AccountDAO;
import org.fhi360.lamis.mobile.cparp.dao.AppointmentDAO;
import org.fhi360.lamis.mobile.cparp.dao.ChroniccareDAO;
import org.fhi360.lamis.mobile.cparp.dao.DrugtherapyDAO;
import org.fhi360.lamis.mobile.cparp.dao.EncounterDAO;
import org.fhi360.lamis.mobile.cparp.dao.HtcDAO;
import org.fhi360.lamis.mobile.cparp.dao.MonitorDAO;


public class SynchronizeFragment extends Fragment implements View.OnClickListener {
    private View layout;
    private CheckBox downloadCheckbox;
    private Button syncButton;
    private Button synchronize;
    private ProgressDialog mPb;

    public SynchronizeFragment() {
    }

    OnSyncButtonClickListener listener;

    public interface OnSyncButtonClickListener {
        void sync(String method, String url, String content);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        layout = inflater.inflate(R.layout.fragment_synchronize, container, false);

        //Get reference to the download checkbox
        downloadCheckbox = (CheckBox) layout.findViewById(R.id.download_checkbox);
        //Get reference to the sync button
        syncButton = (Button) layout.findViewById(R.id.sync_button);
        //addClickListenerOnSyncButton();
        syncButton.setOnClickListener(this);

        return layout;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        //This makes sure that the container activity has implemented the callback interface
        try {
            this.listener = (OnSyncButtonClickListener) context;
        } catch (ClassCastException exception) {
            throw new ClassCastException(context.toString());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    //Called when the sync button is clicked.
    //Inside the method a callback on the MainActivity implementation of sync() method of the
    //OnSyncButtonClickListener interface is done, effectively transfering the event to the parent Activity (MainActivity)
    @Override
    public void onClick(View view) {
        String content = "";
        String url = "";
        //Retrieve data not synced since the last sync operation
        long timeLastSync = new AccountDAO(layout.getContext()).getTimeLastSync();
        String[] tables = {"monitor", "encounter", "chroniccare", "drugtherapy", "appointment", "mhtc"};
        for(String table : tables) {
            url = "resources/webservice/mobile/syncs/"+table;
            if(table.equalsIgnoreCase("monitor")) content = (new MonitorDAO(layout.getContext()).getContent(timeLastSync)).toString();
            if(table.equalsIgnoreCase("encounter")) content = (new EncounterDAO(layout.getContext()).getContent(timeLastSync)).toString();
            if(table.equalsIgnoreCase("chroniccare")) content = (new ChroniccareDAO(layout.getContext()).getContent(timeLastSync)).toString();
            if(table.equalsIgnoreCase("drugtherapy")) content = (new DrugtherapyDAO(layout.getContext()).getContent(timeLastSync)).toString();
            if(table.equalsIgnoreCase("appointment")) content = (new AppointmentDAO(layout.getContext()).getContent(timeLastSync)).toString();
            if(table.equalsIgnoreCase("mhtc")) content = (new HtcDAO(layout.getContext()).getContent(timeLastSync)).toString();
            listener.sync("POST", url, content);
        }

        if (downloadCheckbox.isChecked()) {
            int communitypharmId = new AccountDAO(layout.getContext()).getAccountId();
            Log.v("Sync.........", "CP ID " + communitypharmId);
            url = "resources/webservice/mobile/devolved/" + communitypharmId;
            listener.sync("GET", url, content);
        }
        //Update the data of last sync field in account table
        new AccountDAO(layout.getContext()).updateLastSync();
    }

}
