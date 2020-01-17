package org.fhi360.lamis.mobile.cparp.adapter;

/**
 * Created by aalozie on 2/23/2017.
 */
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.fhi360.lamis.mobile.cparp.R;

import java.util.Random;

public class PatientRecyclerAdapter extends RecyclerView.Adapter<PatientRecyclerAdapter.ViewHolder>{
    //These variables will hold the data for the views
    private String [] profiles;
    private String [] facilities;

    private Listener listener;

    public static interface Listener{
        public void onClick(int position);
    }

    //Provide a reference to views used in the recycler view. Each ViewHolder will display a CardView
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        public ViewHolder(CardView v) {
            super(v);
            cardView = v;
        }
    }

    //Pass data to the adapter in its constructor
    public PatientRecyclerAdapter(String [] profiles, String [] facilities) {
        this.profiles = profiles;
        this.facilities = facilities;
    }

    //Create a new view and specify what layout to use for the contents of the ViewHolder
    @Override
    public PatientRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_card_view, parent, false);
        return new ViewHolder(cardView);
    }

    //Set the values inside the given view.
    //This method get called whenever the recyler view needs to display data the in a view holder
    //It takes two parameters: the view holder that data needs to be bound to and the position in the data set of the data that needs to be bound.
    //Declare variable position as final because we are referencing it in the inner class View.OnClickListener
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        CardView cardView = holder.cardView;
        String firstLetter = String.valueOf(profiles[position].charAt(0));
        Random mRandom = new Random();
        TextView circleImages= (TextView) cardView.findViewById(R.id.circleImage);
        int color = Color.argb(255, mRandom.nextInt(256), mRandom.nextInt(256), mRandom.nextInt(256));
        ((GradientDrawable) circleImages.getBackground()).setColor(color);
        circleImages.setText(firstLetter);

        TextView profileView = (TextView) cardView.findViewById(R.id.patient_profile);
        profileView.setText(profiles[position]);
        TextView facilityView = (TextView) cardView.findViewById(R.id.facility_name);
        facilityView.setText(facilities[position]);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(position);  //When the CardView is clicked, call the Listener onClick method
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        //Return the number items in the data set.
        //The length of the profile array equals the number of data items in the recycler view
        return profiles.length;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }
}
