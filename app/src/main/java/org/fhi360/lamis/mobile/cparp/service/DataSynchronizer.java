package org.fhi360.lamis.mobile.cparp.service;

import android.content.Context;

import org.fhi360.lamis.mobile.cparp.webservice.HttpGetRequest;
import org.fhi360.lamis.mobile.cparp.webservice.HttpPostRequest;
import org.fhi360.lamis.mobile.cparp.R;

import org.fhi360.lamis.mobile.cparp.webservice.WebserviceResponseHandler;
import org.fhi360.lamis.mobile.cparp.dao.AccountDAO;
import org.fhi360.lamis.mobile.cparp.dao.AppointmentDAO;
import org.fhi360.lamis.mobile.cparp.dao.ChroniccareDAO;
import org.fhi360.lamis.mobile.cparp.dao.DrugtherapyDAO;
import org.fhi360.lamis.mobile.cparp.dao.EncounterDAO;
import org.fhi360.lamis.mobile.cparp.dao.HtcDAO;
import org.fhi360.lamis.mobile.cparp.dao.MonitorDAO;

/**
 * Created by aalozie on 10/23/2017.
 */

public class DataSynchronizer {
    private Context context;
    private String serverUrl;

    public  DataSynchronizer(Context context) {
        this.context = context;
        this.serverUrl = context.getResources().getString(R.string.server_url);
        /*
        phoneNumber= 07086005960
        activationCode= la0019
         */
    }

    public void sync() {
        String content = "";
        String url = "";
        long timeLastSync = new AccountDAO(context).getTimeLastSync();
        String[] tables = {"monitor", "encounter", "chroniccare", "drugtherapy", "appointment", "htc"};
        for(String table : tables) {
            if(table.equalsIgnoreCase("monitor")) content = (new MonitorDAO(context).getContent(timeLastSync)).toString();
            if(table.equalsIgnoreCase("encounter")) content = (new EncounterDAO(context).getContent(timeLastSync)).toString();
            if(table.equalsIgnoreCase("chroniccare")) content = (new ChroniccareDAO(context).getContent(timeLastSync)).toString();
            if(table.equalsIgnoreCase("drugtherapy")) content = (new DrugtherapyDAO(context).getContent(timeLastSync)).toString();
            if(table.equalsIgnoreCase("appointment")) content = (new AppointmentDAO(context).getContent(timeLastSync)).toString();
            if(table.equalsIgnoreCase("mhtc")) content = (new HtcDAO(context).getContent(timeLastSync)).toString();
            url = serverUrl + "resources/webservice/mobile/syncs/"+table;
            String result = new HttpPostRequest(context).postData(url, content);
            new WebserviceResponseHandler(context).parseResult(result);
            int communitypharmId = new AccountDAO(context).getAccountId();
            url = serverUrl + "resources/webservice/mobile/devolved/"+communitypharmId;
            result = new HttpGetRequest(context).getData(url);
            new WebserviceResponseHandler(context).parseResult(result);
        }
        new AccountDAO(context).updateLastSync();

    }
}
