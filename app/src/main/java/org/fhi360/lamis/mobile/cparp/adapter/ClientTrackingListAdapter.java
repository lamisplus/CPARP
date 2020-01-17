package org.fhi360.lamis.mobile.cparp.adapter;

/**
 * Created by aalozie on 2/27/2017.
 */
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.fhi360.lamis.mobile.cparp.R;
import org.fhi360.lamis.mobile.cparp.model.Appointment;
import org.fhi360.lamis.mobile.cparp.utility.DateUtil;


import java.util.ArrayList;

public class ClientTrackingListAdapter extends BaseAdapter{
    public ArrayList<Appointment> appointments;
    private Activity activity;

    public ClientTrackingListAdapter(Activity activity, ArrayList<Appointment> appointments) {
        this.activity = activity;
        this.appointments = appointments;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return appointments.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return appointments.get(position);
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

        ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.client_tracking_list_view, null);
            holder.textFirst = (TextView) convertView.findViewById(R.id.date_tracked);
            holder.textSecond = (TextView) convertView.findViewById(R.id.type_tracking);
            holder.textThird = (TextView) convertView.findViewById(R.id.tracking_outcome);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        Appointment appointment = appointments.get(position);
        holder.textFirst.setText(DateUtil.parseDateToString(appointment.getDateTracked(), "dd/MM/yyyy"));
        holder.textSecond.setText(appointment.getTypeTracking());
        holder.textThird.setText(appointment.getTrackingOutcome());
        return convertView;
    }
}
