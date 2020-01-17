package org.fhi360.lamis.mobile.cparp.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.fhi360.lamis.mobile.cparp.R;
import org.fhi360.lamis.mobile.cparp.model.Chroniccare;
import org.fhi360.lamis.mobile.cparp.utility.DateUtil;

import java.util.ArrayList;

/**
 * Created by aalozie on 3/22/2017.
 */

public class ChroniccareListAdapter extends BaseAdapter {
    public ArrayList<Chroniccare> chroniccares;
    private Activity activity;

    public ChroniccareListAdapter(Activity activity, ArrayList<Chroniccare> chroniccares) {
        this.activity = activity;
        this.chroniccares = chroniccares;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return chroniccares.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return chroniccares.get(position);
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
        TextView textFourth;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChroniccareListAdapter.ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.care_support_list_view, null);
            holder = new ChroniccareListAdapter.ViewHolder();
            holder.textFirst = (TextView) convertView.findViewById(R.id.date_visit);
            holder.textSecond = (TextView) convertView.findViewById(R.id.tb_treatment);
            holder.textThird = (TextView) convertView.findViewById(R.id.hypertensive);
            holder.textFourth = (TextView) convertView.findViewById(R.id.diabetic);
            convertView.setTag(holder);
        }
        else {
            holder = (ChroniccareListAdapter.ViewHolder) convertView.getTag();
        }

        Chroniccare chroniccare = chroniccares.get(position);
        String tb = chroniccare.getTbTreatment();
        String tbScreen = chroniccare.getTbReferred();
        if(tb.equalsIgnoreCase("Yes")) {
            tb = "On TB treatment";
        }
        else {
            if(tb.equalsIgnoreCase("No")) {
                if(tbScreen.equalsIgnoreCase("Yes")) {
                    tb = "Referred for TB diagnosis";
                }
                else {
                    tb = "Not on TB treatment";
                }
            }
            else {
                tb = "Not on TB treatment";
            }
        }

        String hypertensive = chroniccare.getHypertensive();
        if(hypertensive.equalsIgnoreCase("Yes")) {
            hypertensive = "Hypertensive";
        }
        else {
            if(hypertensive.equalsIgnoreCase("No")) {
                hypertensive = "Not hypertensive";
            }
            else {
                hypertensive = "Hypertension screening not done";
            }
        }

        String diabetic = chroniccare.getDiabetic();
        if(diabetic.equalsIgnoreCase("Yes")) {
            diabetic = "Diabetic";
        }
        else {
            if(diabetic.equalsIgnoreCase("No")) {
                diabetic = "Not diabetic";
            }
            else {
                diabetic = "Diabetics screening not done";
            }
        }

        holder.textFirst.setText(DateUtil.parseDateToString(chroniccare.getDateVisit(), "dd/MM/yyyy"));
        holder.textSecond.setText(tb);
        holder.textThird.setText(hypertensive);
        holder.textFourth.setText(diabetic);
        return convertView;
    }
}
