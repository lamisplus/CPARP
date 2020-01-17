package org.fhi360.lamis.mobile.cparp.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import org.fhi360.lamis.mobile.cparp.R;
import org.fhi360.lamis.mobile.cparp.dao.FacilityDAO;
import org.fhi360.lamis.mobile.cparp.model.Patient;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by aalozie on 4/22/2017.
 */

public class PatientListAdapter extends BaseAdapter {
    public ArrayList<Patient> patients;
    private Activity activity;
    private SimpleDateFormat dateFormat;
    private String date;
    public PatientListAdapter(Activity activity, ArrayList<Patient> patients) {
        this.activity = activity;
        this.patients = patients;
    }

    @Override
    public int getCount() {
        return patients.size();
    }

    @Override
    public Object getItem(int position) {
        return patients.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    private class ViewHolder {
        TextView profileView;
        TextView facilityView;
        TextView circleImage, dateText;
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PatientListAdapter.ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();
        if(convertView == null) {
            holder = new PatientListAdapter.ViewHolder();
            convertView = inflater.inflate(R.layout.patient_list_view, null);
            holder.profileView =  convertView.findViewById(R.id.patient_profile);
            holder.facilityView =  convertView.findViewById(R.id.facility_name);
            holder.circleImage =  convertView.findViewById(R.id.circleImage);
            holder.dateText=convertView.findViewById(R.id.dateText);
            convertView.setTag(holder);
        }
        else {
            holder = (PatientListAdapter.ViewHolder) convertView.getTag();
        }
        String surname = patients.get(position).getSurname();
        String firstLetterSurname = String.valueOf(patients.get(position).getSurname().charAt(0));
        String otherName = patients.get(position).getOtherNames();
        String firstLettersOtherName = String.valueOf(patients.get(position).getOtherNames().charAt(0));

        String fullSurname=firstLetterSurname.toUpperCase()+ surname.substring(1).toLowerCase();

        String fullOtherName=firstLettersOtherName.toUpperCase()+ otherName.substring(1).toLowerCase();

        String profile = fullSurname + " " + fullOtherName;
        int facilityId = patients.get(position).getFacilityId();
        String facility = new FacilityDAO(activity.getApplicationContext()).getFacility(facilityId);
        holder.profileView.setText(profile);
        holder.facilityView.setText(facility);
        dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        date = dateFormat.format(patients.get(position).getDateStarted());
        holder.dateText.setText(date);
        String firstLetter = String.valueOf( patients.get(position).getSurname().charAt(0));
        Random mRandom = new Random();
        int color = Color.argb(255,
                mRandom.nextInt(256),
                mRandom.nextInt(256),
                mRandom.nextInt(256));
        ((GradientDrawable)   holder.circleImage.getBackground()).setColor(color);
        holder.circleImage.setText(firstLetter);
        return convertView;
    }

}
