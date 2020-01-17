package org.fhi360.lamis.mobile.cparp.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.fhi360.lamis.mobile.cparp.R;
import org.fhi360.lamis.mobile.cparp.model.Drugtherapy;
import org.fhi360.lamis.mobile.cparp.utility.DateUtil;


import java.util.ArrayList;

/**
 * Created by aalozie on 6/4/2017.
 */

public class DrugTherapyListAdapter extends BaseAdapter {
    public ArrayList<Drugtherapy> drugtherapies;
    private Activity activity;

    public DrugTherapyListAdapter(Activity activity, ArrayList<Drugtherapy> drugtherapies) {
        this.activity = activity;
        this.drugtherapies = drugtherapies;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return drugtherapies.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return drugtherapies.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    private class ViewHolder {
        TextView textFirst;
        TextView textSecond;
        TextView textThird;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        DrugTherapyListAdapter.ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();
        if(convertView == null) {
            holder = new DrugTherapyListAdapter.ViewHolder();
            convertView = inflater.inflate(R.layout.drugtherapy_list_view, null);
            holder.textFirst = (TextView) convertView.findViewById(R.id.date_visit);
            holder.textSecond = (TextView) convertView.findViewById(R.id.ois);
            holder.textThird = (TextView) convertView.findViewById(R.id.adherence_issues);
            convertView.setTag(holder);
        }
        else {
            holder = (DrugTherapyListAdapter.ViewHolder) convertView.getTag();
        }

        Drugtherapy drugtherapy = drugtherapies.get(position);
        String ois = drugtherapy.getOis();
        if(ois.equalsIgnoreCase("Yes")) {
            ois = "OIs present";
        }
        else {
            if(ois.equalsIgnoreCase("No")) {
                ois = "No OIs present";
            }
            else {
                ois = "OI screening not done";
            }
        }

        String adr = drugtherapy.getAdrs();
        if(adr.equalsIgnoreCase("Yes")) {
            adr = "ADRs present";
        }
        else {
            if(adr.equalsIgnoreCase("No")) {
                adr = "No ADRs present";
            }
            else {
                adr = "ADR screening not done";
            }
        }

        holder.textFirst.setText(DateUtil.parseDateToString(drugtherapy.getDateVisit(), "dd/MM/yyyy"));
        holder.textSecond.setText(ois);
        holder.textThird.setText(adr);
        return convertView;
    }


}
