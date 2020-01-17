package org.fhi360.lamis.mobile.cparp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import org.fhi360.lamis.mobile.cparp.R;
import org.fhi360.lamis.mobile.cparp.model.Adr;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aalozie on 11/2/2017.
 */

public class AdrSpinnerAdapter extends ArrayAdapter<Adr> {
    private Context context;
    private ArrayList<Adr> listAdrs;
    private AdrSpinnerAdapter adrSpinnerAdapter;
    private boolean isFromView = false;

    public AdrSpinnerAdapter(Context context, int resource, List<Adr> objects) {
        super(context, resource, objects);
        this.context = context;
        this.listAdrs = (ArrayList<Adr>) objects;
        this.adrSpinnerAdapter = this;
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
        holder.text.setText(listAdrs.get(position).getDescription());
        holder.checkbox.setChecked(listAdrs.get(position).isSelected());
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
                    listAdrs.get(position).setSelected(true);
                }
                else {
                    listAdrs.get(position).setSelected(false);
                }
            }
        });
        return convertView;
    }

    public ArrayList<Adr> getArrayList(){
        return listAdrs;
    }

}