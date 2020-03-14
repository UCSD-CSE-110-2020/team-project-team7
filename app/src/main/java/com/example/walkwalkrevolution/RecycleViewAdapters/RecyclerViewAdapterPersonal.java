/**
 *
 * Icons : https://icons8.com/icon/pack/sports/android
 */

package com.example.walkwalkrevolution.RecycleViewAdapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.walkwalkrevolution.FireBaseMessagingService;
import com.example.walkwalkrevolution.R;
import com.example.walkwalkrevolution.RoutesForm;
import com.example.walkwalkrevolution.custom_data_classes.Route;
import com.example.walkwalkrevolution.TreeSetManipulation;
import com.example.walkwalkrevolution.WalkRunSession;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Fills up RoutesPage with saved Routes.
 */
public class RecyclerViewAdapterPersonal extends RecyclerView.Adapter<RecyclerViewAdapterPersonal.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";
    public static final String PREVIEW_DETAILS_INTENT = "From_Routes_Details";
    private static final int MAX_LENGTH_NAME = 25; //max display length for any route name
    private static final int MAX_LENGTH_SP = 15; //max display length for any route starting point

    public List<Route> routes;
    private Context mContext;

    /**
     * Initializes the adapter.
     * @param mContext The page that it will be updating
     * @param routes Routes that need to be displayed
     */
    public RecyclerViewAdapterPersonal(Context mContext, List<Route> routes) {
        this.mContext = mContext;
        this.routes = routes;
        Log.d(TAG, "Recycler View Adapter Constructor");

    }

    /**
     * Inflates all the views to fit the screen, and attaches functionality to buttons.
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, final int viewType) {
        Log.d(TAG, "onCreateViewHolder");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_route_item, parent, false);
        final ViewHolder holder = new ViewHolder(view, new ViewHolder.MyClickListener() {
            /**
             * Start button on a Route clicked, so launches Walk/Run Session Screen.
             * @param p
             */
            @Override
            public void onStartWalkRunSession(int p) {
                Log.d(TAG, "Button Clicked --> onStartWalkRunSession Called ");
                TreeSetManipulation.setSelectedRoute(routes.get(p));
                Log.d(TAG, "SelectedRoute: " + TreeSetManipulation.getSelectedRoute().name);
                SharedPreferences prefs = mContext.getSharedPreferences(TreeSetManipulation.SHARED_PREFS_TREE_SET, MODE_PRIVATE);
                TreeSetManipulation.saveTreeSet(prefs, routes);
                mContext.startActivity(new Intent(mContext, WalkRunSession.class));
            }

            /**
             * Route button clicked, so launches prefilled RoutesForm (editable).
             * @param p
             */
            @Override
            public void onPreviewDetailsPage(int p) {
                Log.d(TAG, "Button Clicked --> onPreviewDetailsPage Called ");
                Intent intent = new Intent(mContext, RoutesForm.class);
                intent.putExtra("From_Intent", PREVIEW_DETAILS_INTENT);
                TreeSetManipulation.setSelectedRoute(routes.get(p));
                Log.d(TAG, "SelectedRoute: " + TreeSetManipulation.getSelectedRoute().name);
                mContext.startActivity(intent);
            }

            /**
             * Delete button of route clicked, so deletes that route and updates page.
             * @param p
             */
            @Override
            public void onDeleteCurrentRoute(int p) {
                Log.d(TAG, "Button Clicked --> onDeleteCurrentRoute Called ");
                displayRouteDeletionDialogue(mContext, p);
            }

            /**
             * Favorite button of route clicked, so favorites route if star is grey or
             * unfavorites route if star is orange.
             * @param p
             */
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

    /**
     * Helper dialogue to delete button, confirms with user they want to delete the route.
     * @param context
     * @param position
     */
    private void displayRouteDeletionDialogue(final Context context, final int position){
        Log.d(TAG, "Delete Button - Dialogue Popped Up");
        new AlertDialog.Builder(context)
                .setTitle("Delete Route")
                .setMessage("Are you sure you want to delete this entry? This is an irreversible action.")
                .setCancelable(false)

                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        routes.remove(position);
                        notifyDataSetChanged();
                        Toast.makeText(context, "Route Successfully Deleted", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "Delete Dialogue - Chose to delete");
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Log.d(TAG, "Delete Dialogue - Chose to cancel");
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    /**
     * Binds each route's information to the correlated places on their view.
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Route route = routes.get(position);
        Log.d(TAG, "onBindViewHolder: Position" + position + " Route Null: " + (route == null));

        //sets text for all editTexts
        holder.routeName.setText(trimmedRouteValue(route.name, MAX_LENGTH_NAME));
        holder.startingPoint.setText(formatStartingPoint(trimmedRouteValue(route.startingPoint, MAX_LENGTH_SP)));
        holder.routeDate.setText(formatDate(route.date));
        holder.routeSteps.setText(formatSteps(route.steps));
        holder.routeMiles.setText(formatMiles(route.distance));

        //updates for any additional information clicked (Toggle Buttons + Notes)
        holder.additionalInformation.setText(renderOptionalInfo(route));

        //renders star state for route's favorited state
        if(route.getIsFavorited()){
            holder.favoriteRoute.setBackground(mContext.getResources().getDrawable(R.drawable.favorite_button_states));
        }
        else{
            holder.favoriteRoute.setBackground(mContext.getResources().getDrawable(R.drawable.routes_list_start_button_states));
        }
    }

    /**
     * Displays any additional information (Toggle Buttons) that were cliked for the route onto the route's view.
     * @param route
     * @return
     */
    private String renderOptionalInfo(Route route){
        //Buffer is thread safe
        StringBuffer builder = new StringBuffer();
        try {
            //For every Toggled button, display its text on the Route Screen
            for (String extra : route.optionalFeaturesStr) {
                if (extra != null) {
                    builder.append(extra + " ");
                }
            }
        }
        catch(Exception e){}
        return builder.toString();
    }

    /**
     * Trims both Route name and Route starting point so they fit within the screen, and no words are cut off.
     * @param name
     * @param maxLimit
     * @return
     */
    private String trimmedRouteValue(String name, int maxLimit){
        if (name == null){
            return null;
        }
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

    /**
     * Formats date to Route's screen.
     * @param date
     * @return
     */
    private String formatDate(String date){
        return "Date: " + date;
    }

    private String formatSteps(long steps){
        return "Steps: " + steps;
    }

    private String formatMiles(double miles){
        return "Miles: " + miles;
    }

    /**
     * Formats starting point to Route's screen. Doesn't show starting point if no value
     * was put for it in the RouteForm (null).
     *
     * @param startingPoint
     * @return
     */
    private String formatStartingPoint(String startingPoint){
        if(startingPoint == null){
            return null;
        }
        return "Start: " + startingPoint;
    }

    /**
     * Tells the adapter the number of views the screen will make/hold.
     * @return
     */
    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: " + routes.size());
        return routes.size();
    }

    /**
     * Generalize the Holder so each route's information can be generalized to a specific part of the screen.
     */
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

        /**
         * Informs RecyclerViewAdapter of every onClickFunctionality it needs to implement
         */
        public interface MyClickListener{
            void onStartWalkRunSession(int p);

            void onPreviewDetailsPage(int p);

            void onDeleteCurrentRoute(int p);

            void onFavoriteCurrentRoute(int p);
        }

        /**
         * Provides a reference to the holder of all parts of the layout that are subject to change (modifiable
         * by the Adapter).
         * @param itemView
         * @param listener
         */
        public ViewHolder(@NonNull View itemView, MyClickListener listener) {
            super(itemView);

            //attaches variables to all layout ids
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

            //sets listener functionalities for all buttons for each route view
            startRoute.setOnClickListener(this);
            deleteRoute.setOnClickListener(this);
            parentLayout.setOnClickListener(this);
            favoriteRoute.setOnClickListener(this);
        }

        /**
         * Determines which clickFunction should be executes when a Route is clicked, dependent on
         * which part was touched.
         * @param view
         */
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                //Start Route Clicked
                case R.id.startRoute:
                    listener.onStartWalkRunSession(this.getLayoutPosition());
                    break;
                //Route Clicked (Details)
                case R.id.parentLayout:
                    listener.onPreviewDetailsPage(this.getLayoutPosition());
                    break;
                //Delete Button Clicked
                case R.id.deleteRoute:
                    listener.onDeleteCurrentRoute(this.getLayoutPosition());
                    break;
                //Favorite Button Clicked
                case R.id.favoriteRoute:
                    listener.onFavoriteCurrentRoute(this.getLayoutPosition());
                    break;
                default:
                    break;
            }
        }

    }
}