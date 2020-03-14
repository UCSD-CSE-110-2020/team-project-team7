package com.example.walkwalkrevolution.RecycleViewAdapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.walkwalkrevolution.R;
import com.example.walkwalkrevolution.TeamMember;
import com.example.walkwalkrevolution.custom_data_classes.ProposedWalk;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapterProposedAvailability extends RecyclerView.Adapter<RecyclerViewAdapterProposedAvailability.ViewHolder>{

    private static final String TAG = "RecyclerViewAdapterTeam";

    private List<TeamMember> teammates;
    private Context mContext;

    /**
     * Initializes the adapter.
     * @param mContext The page that it will be updating
     * @param teammates Teammates that need to be displayed
     */
    public RecyclerViewAdapterProposedAvailability(Context mContext, List<TeamMember> teammates) {
        this.mContext = mContext;
        this.teammates = teammates;
        Log.d(TAG, "Recycler View Adapter Constructor");

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_teammate_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final TeamMember teammate = teammates.get(position);
        Log.d(TAG, "onBindViewHolder: Position " + position + " Teammate Initials: " + teammate.getInitials() + " Status: " + teammate.getProposedWalkStatus());

        holder.icon.setText(teammate.getInitials());
        holder.icon.getBackground().setColorFilter(teammate.getColorVal(), PorterDuff.Mode.MULTIPLY);

        setHolderInformation(holder, teammate.getProposedWalkStatus());

    }

    private void setHolderInformation(ViewHolder holder, int status){
        switch(status){
            case 0:
                holder.name.setText("Pending Response");
                holder.name.setTypeface(null, Typeface.ITALIC);
                holder.parentLayout.getBackground().setColorFilter(Color.parseColor("#9E9E9E"), PorterDuff.Mode.MULTIPLY );
                break;
            case 1:
                holder.name.setText("Bad Time");
                holder.parentLayout.getBackground().setColorFilter(Color.parseColor("#F44336"), PorterDuff.Mode.MULTIPLY );
                break;
            case 2:
                holder.name.setText("Bad Route");
                holder.parentLayout.getBackground().setColorFilter(Color.parseColor("#F44336"), PorterDuff.Mode.MULTIPLY );
                break;
            case 3:
                holder.name.setText("Accepted");
                holder.parentLayout.getBackground().setColorFilter(Color.parseColor("#AED581"), PorterDuff.Mode.MULTIPLY );
                break;
        }
    }

    @Override
    public int getItemCount() {
        return  teammates.size();
    }

    /**
     * Generalize the Holder so each route's information can be generalized to a specific part of the screen.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder{

        Button icon;
        TextView name;
        RelativeLayout parentLayout;

        /**
         * Provides a reference to the holder of all parts of the layout that are subject to change (modifiable
         * by the Adapter).
         * @param itemView
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //attaches variables to all layout ids
            this.icon = (Button) itemView.findViewById(R.id.teammateIcon);
            this.name = (TextView) itemView.findViewById(R.id.teammateName);
            this.parentLayout = (RelativeLayout) itemView.findViewById(R.id.parentLayout);
        }

    }
}
