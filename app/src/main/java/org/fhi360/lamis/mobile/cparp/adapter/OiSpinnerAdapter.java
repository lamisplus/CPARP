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
import org.fhi360.lamis.mobile.cparp.model.Oi;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by aalozie on 11/2/2017.
 */

public class OiSpinnerAdapter extends ArrayAdapter<Oi> {
    private Context context;
    private ArrayList<Oi> listOis;
    private OiSpinnerAdapter oiSpinnerAdapter;

    public OiSpinnerAdapter(Context context, int resource, List<Oi> objects) {
        super(context, resource, objects);
        this.context = context;
        this.listOis = (ArrayList<Oi>) objects;
        this.oiSpinnerAdapter = this;
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
        final ViewHolder holder;
        LayoutInflater layoutInflator = LayoutInflater.from(context);
        if (convertView == null) {
            convertView = layoutInflator.inflate(R.layout.spinner_item, null);
            holder = new ViewHolder();
            holder.text = (TextView) convertView.findViewById(R.id.text);
            holder.checkbox = (CheckBox) convertView.findViewById(R.id.checkbox);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.text.setText(listOis.get(position).getDescription());
        holder.checkbox.setChecked(listOis.get(position).isSelected());

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
                    listOis.get(position).setSelected(true);
                    Log.v("Oi selected", "Checked..."+position);
                }
                else {
                    listOis.get(position).setSelected(false);
                    Log.v("Oi selected", "Unchecked...."+position);
                }
            }
        });
        return convertView;
    }

    public ArrayList<Oi> getArrayList(){
        return listOis;
    }
}

/*
        holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int position = (Integer) buttonView.getTag();
                if(isChecked) {
                    ois.get(position).setSelected(true);
                    Log.v("Oi selected", "Checked..."+position);
                }
                else {
                    ois.get(position).setSelected(false);
                    Log.v("Oi selected", "Unchecked...."+position);
                }
            }
        });
*/
