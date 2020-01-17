package org.fhi360.lamis.mobile.cparp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import org.fhi360.lamis.mobile.cparp.R;
import org.fhi360.lamis.mobile.cparp.model.Medierror;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aalozie on 11/2/2017.
 */

public class MedicationErrorSpinnerAdapter extends ArrayAdapter<Medierror> {
    private Context context;
    private ArrayList<Medierror> listErrors;
    private MedicationErrorSpinnerAdapter medicationErrorSpinnerAdapter;

    public MedicationErrorSpinnerAdapter(Context context, int resource, List<Medierror> objects) {
        super(context, resource, objects);
        this.context = context;
        this.listErrors = (ArrayList<Medierror>) objects;
        this.medicationErrorSpinnerAdapter = this;
    }

    private class ViewHolder {
        private TextView text;
        private CheckBox checkbox;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(final int position, View convertView, ViewGroup parent) {
        final MedicationErrorSpinnerAdapter.ViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflator = LayoutInflater.from(context);
            convertView = layoutInflator.inflate(R.layout.spinner_item, null);
            holder = new MedicationErrorSpinnerAdapter.ViewHolder();
            holder.text = (TextView) convertView.findViewById(R.id.text);
            holder.checkbox = (CheckBox) convertView.findViewById(R.id.checkbox);
            convertView.setTag(holder);
        }
        else {
            holder = (MedicationErrorSpinnerAdapter.ViewHolder) convertView.getTag();
        }
        holder.text.setText(listErrors.get(position).getDescription());
        holder.checkbox.setChecked(listErrors.get(position).isSelected());

        if ((position == 0)) {
            holder.checkbox.setVisibility(View.INVISIBLE);
        }
        else {
            holder.checkbox.setVisibility(View.VISIBLE);
        }
        holder.checkbox.setTag(position);
        holder.checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    listErrors.get(position).setSelected(true);
                    Log.v("Error selected", "Checked..."+position);
                }
                else {
                    listErrors.get(position).setSelected(false);
                    Log.v("Error selected", "Unchecked...."+position);
                }
            }
        });
        return convertView;
    }

    public ArrayList<Medierror> getArrayList(){
        return listErrors;
    }
}
