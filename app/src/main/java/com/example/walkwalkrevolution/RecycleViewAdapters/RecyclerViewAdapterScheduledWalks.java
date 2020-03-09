package com.example.walkwalkrevolution.RecycleViewAdapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.walkwalkrevolution.ProposedWalkDetailsPage;
import com.example.walkwalkrevolution.R;
import com.example.walkwalkrevolution.custom_data_classes.ProposedWalk;
import com.example.walkwalkrevolution.custom_data_classes.ProposedWalkJsonConverter;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapterScheduledWalks extends RecyclerView.Adapter<RecyclerViewAdapterScheduledWalks.ViewHolder>{

    private static final String TAG = "RecyclerViewAdapterSche";

    public static final String PREVIEW_DETAILS_INTENT = "DETAILS_FROM_SCHEDULED";

    public List<ProposedWalk> walks;
    private Context mContext;

    /**
     * Initializes the adapter.
     * @param mContext The page that it will be updating
     * @param walks Teammates that need to be displayed
     */
    public RecyclerViewAdapterScheduledWalks(Context mContext, List<ProposedWalk> walks) {
        this.mContext = mContext;
        this.walks = walks;
        Log.d(TAG, "Recycler View Adapter Constructor");

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_teammate_item, parent, false);
        final ViewHolder holder = new ViewHolder(view, new ViewHolder.MyClickListener(){
            @Override
            public void onPreviewDetailsForScheduledWalk(int p) {
                ProposedWalk walk = walks.get(p);
                Intent intent = new Intent(mContext, ProposedWalkDetailsPage.class);
                intent.putExtra("From_Intent", PREVIEW_DETAILS_INTENT);
                intent.putExtra("Scheduled Walk",ProposedWalkJsonConverter.convertWalkToJson(walk));
                mContext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ProposedWalk walk = walks.get(position);

        holder.icon.setText(walk.getCreator().getInitials());
        holder.name.setText(walk.getName());
        holder.icon.getBackground().setColorFilter(walk.getCreator().getColorVal(), PorterDuff.Mode.MULTIPLY);
        holder.parentLayout.setBackground(mContext.getResources().getDrawable(R.drawable.route_item_states));
    }

    @Override
    public int getItemCount() {
        return walks.size();
    }

    /**
     * Generalize the Holder so each route's information can be generalized to a specific part of the screen.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        Button icon;
        TextView name;
        RelativeLayout parentLayout;
        MyClickListener listener;

        public interface MyClickListener{
            void onPreviewDetailsForScheduledWalk(int p);
        }

        /**
         * Provides a reference to the holder of all parts of the layout that are subject to change (modifiable
         * by the Adapter).
         * @param itemView
         */
        public ViewHolder(@NonNull View itemView, MyClickListener listener) {
            super(itemView);

            //attaches variables to all layout ids
            this.icon = (Button) itemView.findViewById(R.id.teammateIcon);
            this.name = (TextView) itemView.findViewById(R.id.teammateName);
            this.parentLayout = (RelativeLayout) itemView.findViewById(R.id.parentLayout);

            this.listener = listener;

            this.parentLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onPreviewDetailsForScheduledWalk(this.getLayoutPosition());
        }
    }
}
