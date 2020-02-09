/**
 *
 * Icons : https://icons8.com/icon/pack/sports/android
 */

package com.example.walkwalkrevolution;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Iterator;
import java.util.TreeSet;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";
    private static final int MAX_LENGTH = 25;

    private TreeSet<Route> routes = new TreeSet<Route>();
    private Context mContext;

    public RecyclerViewAdapter(Context mContext, TreeSet<Route> routes) {
        this.mContext = mContext;
        this.routes = routes;
        Log.d(TAG, "Recycler View Adapter Constructor");

    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_route_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Route route = nthRouteInTreeSet(position);
        Log.d(TAG, "onBindViewHolder: Position" + position + " Route Null: " + (route == null));

        //holder.routeName.setText(trimmedRouteName(route.name));
        holder.routeName.setText(route.name);
        holder.routeDate.setText(formatDate(route.date));
        holder.routeSteps.setText(formatSteps(route.steps));
        holder.routeMiles.setText(formatMiles(route.distance));

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext,"Clicking works..." , Toast.LENGTH_SHORT).show();
                //should link to details form with prefilled information
            }
        });

    }

    private String trimmedRouteName(String name){
        int maxLength = MAX_LENGTH;
        if(name.length() < MAX_LENGTH){
            maxLength = name.length();
        }
        //trims till specified length
        String trimmedName = name.substring(0, maxLength);
        //retrims string to the last space (so no words are cut off)
        return trimmedName.substring(0, Math.min(trimmedName.length(), trimmedName.lastIndexOf(" ")));
    }

    private Route nthRouteInTreeSet(int position){
        Iterator<Route> it = routes.iterator();
        int i = -1;
        Route route = null;
        while(it.hasNext() && i < position) {
            route = it.next();
            i++;
        }
        return route;
    }

    private String formatDate(String date){
        return "Date: " + date;
    }

    private String formatSteps(int steps){
        return "Steps: " + steps;
    }

    private String formatMiles(float miles){
        return "Miles: " + miles;
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount");
        return routes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView routeName;
        TextView routeDate;
        TextView routeSteps;
        TextView routeMiles;
        RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            routeName = itemView.findViewById(R.id.routeName);
            routeDate = itemView.findViewById(R.id.routeDate);
            routeSteps = itemView.findViewById(R.id.routeSteps);
            routeMiles = itemView.findViewById(R.id.routeMiles);
            parentLayout = itemView.findViewById(R.id.relativeLayoutRouteItem);
        }
    }
}