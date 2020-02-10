/**
 *
 * Icons : https://icons8.com/icon/pack/sports/android
 */

package com.example.walkwalkrevolution;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Iterator;
import java.util.TreeSet;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";
    public static final String PREVIEW_DETAILS_INTENT = "From_Routes_Details";
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
        ViewHolder holder = new ViewHolder(view, new ViewHolder.MyClickListener() {
            @Override
            public void onStartWalkRunSession(int p) {
                Log.d(TAG, "Button Clicked --> onStartWalkRunSession Called ");
                TreeSetManipulation.setSelectedRoute(nthRouteInTreeSet(p));
                Log.d(TAG, "RouteBeingWalked: " + TreeSetManipulation.getSelectedRoute().name);
                mContext.startActivity(new Intent(mContext, WalkRunSession.class));
            }

            @Override
            public void onPreviewDetailsPage(int p) {
                Log.d(TAG, "Button Clicked --> onPreviewDetailsPage Called ");
                Intent intent = new Intent(mContext, RoutesForm.class);
                // Push data to RouteForm
                intent.putExtra("From_Intent", PREVIEW_DETAILS_INTENT);
                TreeSetManipulation.setSelectedRoute(nthRouteInTreeSet(p));
                Log.d(TAG, "RouteBeingWalked: " + TreeSetManipulation.getSelectedRoute().name);
                mContext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Route route = nthRouteInTreeSet(position);
        Log.d(TAG, "onBindViewHolder: Position" + position + " Route Null: " + (route == null));

        //holder.routeName.setText(trimmedRouteName(route.name));
        holder.routeName.setText(route.name);
        holder.routeDate.setText(formatDate(route.date));
        holder.routeSteps.setText(formatSteps(route.steps));
        holder.routeMiles.setText(formatMiles(route.distance));
    }

    //NEED TO FIX THIS METHOD
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

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        MyClickListener listener;

        TextView routeName;
        TextView routeDate;
        TextView routeSteps;
        TextView routeMiles;
        RelativeLayout parentLayout;
        Button startRoute;

        public interface MyClickListener{
            void onStartWalkRunSession(int p);

            void onPreviewDetailsPage(int p);
        }

        public ViewHolder(@NonNull View itemView, MyClickListener listener) {
            super(itemView);
            routeName = itemView.findViewById(R.id.routeName);
            routeDate = itemView.findViewById(R.id.routeDate);
            routeSteps = itemView.findViewById(R.id.routeSteps);
            routeMiles = itemView.findViewById(R.id.routeMiles);
            parentLayout = itemView.findViewById(R.id.parentLayout);
            startRoute = itemView.findViewById(R.id.startRoute);

            this.listener = listener;

            startRoute.setOnClickListener(this);
            parentLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.startRoute:
                    listener.onStartWalkRunSession(this.getLayoutPosition());
                    break;
                case R.id.parentLayout:
                    listener.onPreviewDetailsPage(this.getLayoutPosition());
                    break;
                default:
                    break;
            }
        }

    }
}