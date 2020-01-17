package org.fhi360.lamis.mobile.cparp.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.fhi360.lamis.mobile.cparp.R;
import org.fhi360.lamis.mobile.cparp.model.Patient;

import java.util.ArrayList;

/**
 * Created by aalozie on 4/22/2017.
 */

public class DefaulterListAdapter extends BaseAdapter {
    public ArrayList<Patient> patients;
    private Activity activity;

    public DefaulterListAdapter(Activity activity, ArrayList<Patient> patients) {
        this.activity = activity;
        this.patients = patients;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return patients.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return patients.get(position);
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
        DefaulterListAdapter.ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();
        if(convertView == null) {
            holder = new DefaulterListAdapter.ViewHolder();
            convertView = inflater.inflate(R.layout.defaulter_list_view, null);
            holder.textFirst = (TextView) convertView.findViewById(R.id.name);
            holder.textSecond = (TextView) convertView.findViewById(R.id.address);
            holder.textThird = (TextView) convertView.findViewById(R.id.phone);
            convertView.setTag(holder);
        }
        else {
            holder = (DefaulterListAdapter.ViewHolder) convertView.getTag();
        }
        Patient patient = patients.get(position);
        holder.textFirst.setText(patient.getSurname() + " " + patient.getOtherNames());
        holder.textSecond.setText(patient.getAddress());
        holder.textThird.setText(patient.getPhone());
        return convertView;
    }

}
