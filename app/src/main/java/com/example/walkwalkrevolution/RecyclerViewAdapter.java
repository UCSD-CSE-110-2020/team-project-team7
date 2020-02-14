/**
 *
 * Icons : https://icons8.com/icon/pack/sports/android
 */

package com.example.walkwalkrevolution;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
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

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";
    public static final String PREVIEW_DETAILS_INTENT = "From_Routes_Details";
    private static final int MAX_LENGTH_NAME = 25;
    private static final int MAX_LENGTH_SP = 15;

    public List<Route> routes;
    private Context mContext;

    public RecyclerViewAdapter(Context mContext, List<Route> routes) {
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
                TreeSetManipulation.setSelectedRoute(routes.get(p));
                Log.d(TAG, "SelectedRoute: " + TreeSetManipulation.getSelectedRoute().name);
                SharedPreferences prefs = mContext.getSharedPreferences(TreeSetManipulation.SHARED_PREFS_TREE_SET, MODE_PRIVATE);
                TreeSetManipulation.saveTreeSet(prefs, routes);
                mContext.startActivity(new Intent(mContext, WalkRunSession.class));
            }

            @Override
            public void onPreviewDetailsPage(int p) {
                Log.d(TAG, "Button Clicked --> onPreviewDetailsPage Called ");
                Intent intent = new Intent(mContext, RoutesForm.class);
                intent.putExtra("From_Intent", PREVIEW_DETAILS_INTENT);
                TreeSetManipulation.setSelectedRoute(routes.get(p));
                Log.d(TAG, "SelectedRoute: " + TreeSetManipulation.getSelectedRoute().name);
                mContext.startActivity(intent);
            }

            @Override
            public void onDeleteCurrentRoute(int p) {
                Log.d(TAG, "Button Clicked --> onDeleteCurrentRoute Called ");
                displayRouteDeletionDialogue(mContext, p);
            }

            @Override
            public void onFavoriteCurrentRoute(int p) {
                Log.d(TAG, "Button Clicked --> onFavoriteCurrentRoute Called ");
                Route routeSelected = routes.get(p);
                routeSelected.toggleIsFavorited();

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

    private void displayRouteDeletionDialogue(final Context context, final int position){
        new AlertDialog.Builder(context)
                .setTitle("Delete Route")
                .setMessage("Are you sure you want to delete this entry? This is an irreversible action.")
                .setCancelable(false)

                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        routes.remove(position);
                        notifyDataSetChanged();
                        Toast.makeText(context, "Route Successfully Deleted", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Route route = routes.get(position);
        Log.d(TAG, "onBindViewHolder: Position" + position + " Route Null: " + (route == null));

        holder.routeName.setText(trimmedRouteValue(route.name, MAX_LENGTH_NAME));
        holder.startingPoint.setText(formatStartingPoint(trimmedRouteValue(route.startingPoint, MAX_LENGTH_SP)));
        holder.routeDate.setText(formatDate(route.date));
        holder.routeSteps.setText(formatSteps(route.steps));
        holder.routeMiles.setText(formatMiles(route.distance));
        holder.additionalInformation.setText(renderOptionalInfo(route));

        if(route.getIsFavorited()){
            holder.favoriteRoute.setBackground(mContext.getResources().getDrawable(R.drawable.favorite_button_states));
        }
        else{
            holder.favoriteRoute.setBackground(mContext.getResources().getDrawable(R.drawable.routes_list_start_button_states));
        }
    }

    private String renderOptionalInfo(Route route){
        StringBuffer builder = new StringBuffer();
        try {
            //For every Toggled button, display it on the Route Screen
            for (String extra : route.optionalFeaturesStr) {
                if (extra != null) {
                    builder.append(extra + " ");
                }
            }
        }
        catch(Exception e){}
        return builder.toString();
    }


    private String trimmedRouteValue(String name, int maxLimit){
        if(name.length() < maxLimit){
            return name;
        }
        //trims till specified length
        String trimmedName = name.substring(0, maxLimit);

        try{
            //retrims string to the last space - ASSUMES there is a space
            trimmedName = trimmedName.substring(0, Math.min(trimmedName.length(), trimmedName.lastIndexOf(" ")));
        }
        catch(Exception e){
            Log.d(TAG, "trimmedRouteName: Exception caught");
        }

        return trimmedName;
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

    private String formatStartingPoint(String startingPoint){
        return "Start: " + startingPoint;
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: " + routes.size());
        return routes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        MyClickListener listener;

        TextView routeName;
        TextView startingPoint;
        TextView routeDate;
        TextView routeSteps;
        TextView routeMiles;
        RelativeLayout parentLayout;
        Button startRoute;
        ImageButton deleteRoute;
        ImageButton favoriteRoute;
        TextView additionalInformation;

        public interface MyClickListener{
            void onStartWalkRunSession(int p);

            void onPreviewDetailsPage(int p);

            void onDeleteCurrentRoute(int p);

            void onFavoriteCurrentRoute(int p);
        }

        public ViewHolder(@NonNull View itemView, MyClickListener listener) {
            super(itemView);
            routeName = itemView.findViewById(R.id.routeName);
            startingPoint = itemView.findViewById(R.id.startingPoint);
            routeDate = itemView.findViewById(R.id.routeDate);
            routeSteps = itemView.findViewById(R.id.routeSteps);
            routeMiles = itemView.findViewById(R.id.routeMiles);
            parentLayout = itemView.findViewById(R.id.parentLayout);
            startRoute = itemView.findViewById(R.id.startRoute);
            deleteRoute = itemView.findViewById(R.id.deleteRoute);
            favoriteRoute = itemView.findViewById(R.id.favoriteRoute);
            additionalInformation = itemView.findViewById(R.id.additionalInfo);

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