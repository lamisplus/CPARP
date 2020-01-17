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
import org.fhi360.lamis.mobile.cparp.model.Encounter;
import org.fhi360.lamis.mobile.cparp.utility.DateUtil;

import java.util.ArrayList;

public class EncounterListAdapter extends BaseAdapter{
    public ArrayList<Encounter> encounters;
    private Activity activity;

    public EncounterListAdapter(Activity activity, ArrayList<Encounter> encounters) {
        super();
        this.activity = activity;
        this.encounters = encounters;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return encounters.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return encounters.get(position);
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
            convertView = inflater.inflate(R.layout.encounter_list_view, null);
            holder.textFirst =  convertView.findViewById(R.id.date_visit);
            holder.textSecond = convertView.findViewById(R.id.regimen1);
            holder.textThird =  convertView.findViewById(R.id.duration1);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        Encounter encounter = encounters.get(position);
        holder.textFirst.setText(DateUtil.parseDateToString(encounter.getDateVisit(), "dd/MM/yyyy"));
        holder.textSecond.setText(encounter.getRegimen1());
        holder.textThird.setText(Integer.toString(encounter.getDuration1()));
        return convertView;
    }
}
