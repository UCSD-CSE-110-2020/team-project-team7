/**
 *
 * Icons : https://icons8.com/icon/pack/sports/android
 */

package com.example.walkwalkrevolution;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Iterator;
import java.util.TreeSet;

import static android.content.Context.MODE_PRIVATE;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";
    public static final String PREVIEW_DETAILS_INTENT = "From_Routes_Details";
    private static final int MAX_LENGTH = 25;

    private TreeSet<Route> routes;
    private Context mContext;

    public RecyclerViewAdapter(Context mContext, TreeSet<Route> routes) {
        this.mContext = mContext;
        this.routes = routes;
        Log.d(TAG, "Recycler View Adapter Constructor");

    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, final int viewType) {
        Log.d(TAG, "onCreateViewHolder");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_route_item, parent, false);
        final ViewHolder holder = new ViewHolder(view, new ViewHolder.MyClickListener() {
            @Override
            public void onStartWalkRunSession(int p) {
                Log.d(TAG, "Button Clicked --> onStartWalkRunSession Called ");
                TreeSetManipulation.setSelectedRoute(nthRouteInTreeSet(p));
                Log.d(TAG, "SelectedRoute: " + TreeSetManipulation.getSelectedRoute().name);
                mContext.startActivity(new Intent(mContext, WalkRunSession.class));
            }

            @Override
            public void onPreviewDetailsPage(int p) {
                Log.d(TAG, "Button Clicked --> onPreviewDetailsPage Called ");
                Intent intent = new Intent(mContext, RoutesForm.class);
                // Push data to RouteForm
                intent.putExtra("From_Intent", PREVIEW_DETAILS_INTENT);
                TreeSetManipulation.setSelectedRoute(nthRouteInTreeSet(p));
                Log.d(TAG, "SelectedRoute: " + TreeSetManipulation.getSelectedRoute().name);
                mContext.startActivity(intent);
            }

            @Override
            public void onDeleteCurrentRoute(int p) {
                Log.d(TAG, "Button Clicked --> onDeleteCurrentRoute Called ");
                SharedPreferences prefs = mContext.getSharedPreferences(TreeSetManipulation.SHARED_PREFS_TREE_SET, MODE_PRIVATE);
                routes = TreeSetManipulation.deleteRouteInTreeSet(prefs, new TreeSetComparator(), nthRouteInTreeSet(p));
                notifyDataSetChanged();
                Toast.makeText(mContext, "Route Successfully Deleted", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFavoriteCurrentRoute(int p) {
                Log.d(TAG, "Button Clicked --> onFavoriteCurrentRoute Called ");
                Route routeSelected = nthRouteInTreeSet(p);
                routeSelected.toggleIsFavorited();

                SharedPreferences prefs = mContext.getSharedPreferences(TreeSetManipulation.SHARED_PREFS_TREE_SET, MODE_PRIVATE);
                routes = TreeSetManipulation.updateRoot(prefs, new TreeSetComparator(), routeSelected);
                notifyDataSetChanged();

                if(routeSelected.getIsFavorited()){
                    Toast.makeText(mContext, "Route Favorited", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(mContext, "Route Unfavorited", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Route route = nthRouteInTreeSet(position);
        Log.d(TAG, "onBindViewHolder: Position" + position + " Route Null: " + (route == null));

        holder.routeName.setText(trimmedRouteName(route.name));
        holder.routeDate.setText(formatDate(route.date));
        holder.routeSteps.setText(formatSteps(route.steps));
        holder.routeMiles.setText(formatMiles(route.distance));

        favoriteButtonStyle(holder, route);
    }

    private void favoriteButtonStyle(ViewHolder holder, Route route){
        if(route.getIsFavorited()){
            holder.favoriteRoute.setBackground(mContext.getResources().getDrawable(R.drawable.favorite_button_clicked_style));
        }
        else{
            holder.favoriteRoute.setBackground(mContext.getResources().getDrawable(R.drawable.routes_list_start_button_style));
        }
    }


    private String trimmedRouteName(String name){
        if(name.length() < MAX_LENGTH){
            return name;
        }
        //trims till specified length
        String trimmedName = name.substring(0, MAX_LENGTH);

        try{
            //retrims string to the last space - ASSUMES there is a space
            trimmedName = trimmedName.substring(0, Math.min(trimmedName.length(), trimmedName.lastIndexOf(" ")));
        }
        catch(Exception e){
            Log.d(TAG, "trimmedRouteName: Exception caught");
        }

        return trimmedName;
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
        ImageButton deleteRoute;
        ImageButton favoriteRoute;

        public interface MyClickListener{
            void onStartWalkRunSession(int p);

            void onPreviewDetailsPage(int p);

            void onDeleteCurrentRoute(int p);

            void onFavoriteCurrentRoute(int p);
        }

        public ViewHolder(@NonNull View itemView, MyClickListener listener) {
            super(itemView);
            routeName = itemView.findViewById(R.id.routeName);
            routeDate = itemView.findViewById(R.id.routeDate);
            routeSteps = itemView.findViewById(R.id.routeSteps);
            routeMiles = itemView.findViewById(R.id.routeMiles);
            parentLayout = itemView.findViewById(R.id.parentLayout);
            startRoute = itemView.findViewById(R.id.startRoute);
            deleteRoute = itemView.findViewById(R.id.deleteRoute);
            favoriteRoute = itemView.findViewById(R.id.favoriteRoute);

            this.listener = listener;

            startRoute.setOnClickListener(this);
            deleteRoute.setOnClickListener(this);
            parentLayout.setOnClickListener(this);
            favoriteRoute.setOnClickListener(this);
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
                case R.id.deleteRoute:
                    listener.onDeleteCurrentRoute(this.getLayoutPosition());
                    break;
                case R.id.favoriteRoute:
                    listener.onFavoriteCurrentRoute(this.getLayoutPosition());
                    break;
                default:
                    break;
            }
        }

    }
}