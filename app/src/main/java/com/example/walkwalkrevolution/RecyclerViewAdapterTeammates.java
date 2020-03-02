package com.example.walkwalkrevolution;

import android.content.Context;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapterTeammates extends RecyclerView.Adapter<RecyclerViewAdapterTeammates.ViewHolder>{

    private static final String TAG = "RecyclerViewAdapterTeam";

    public List<Teammate> teammates;
    private Context mContext;

    /**
     * Initializes the adapter.
     * @param mContext The page that it will be updating
     * @param teammates Teammates that need to be displayed
     */
    public RecyclerViewAdapterTeammates(Context mContext, List<Teammate> teammates) {
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
        final Teammate teammate = teammates.get(position);
        Log.d(TAG, "onBindViewHolder: Position " + position + " Teammate Initials: " + teammate.getInitials());

        holder.icon.setText(teammate.getInitials());
        holder.name.setText(teammate.getName());
        holder.icon.getBackground().setColorFilter(Color.parseColor(teammate.getColor()), PorterDuff.Mode.MULTIPLY);

    }

    @Override
    public int getItemCount() {
        return teammates.size();
    }

    /**
     * Generalize the Holder so each route's information can be generalized to a specific part of the screen.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder{

        Button icon;
        TextView name;

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
        }

    }
}
