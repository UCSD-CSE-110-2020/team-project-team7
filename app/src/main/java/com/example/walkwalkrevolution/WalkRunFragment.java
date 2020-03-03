package com.example.walkwalkrevolution;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import android.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;

import com.example.walkwalkrevolution.fitness.FitnessServiceFactory;
import com.example.walkwalkrevolution.fitness.FitnessService;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WalkRunFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WalkRunFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WalkRunFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    // Constant for logging
    private static final String TAG = "WalkRunSession";

    public static final String WALK_RUN_INTENT = "From_Walk/Run";
    public static final String FITNESS_SERVICE_KEY = "FITNESS_SERVICE_KEY";
    public static final int REQUEST_MOCK_SESSION = 3;

    private boolean isCancelled;
    private long startTime;
    private int minutes, seconds;
    private long stepCount, stepsFromMock;
    private double stepsPerMile, milesCount;
    private TextView timerText, stepCountText, milesText;
    private TimeData timeData;
    private FitnessService fitnessService;
    private StepCountActivity sc;
    private TimerCount runner; // Pass this in from HomePage TODO
    private String resultTime;
    public boolean testStep = true;

    public WalkRunFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TestFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WalkRunFragment newInstance(String param1, String param2) {
        WalkRunFragment fragment = new WalkRunFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        Log.d("in walk/run", "in walk run session");

        // timer text initialize
        Log.d(TAG, "Creating time counter.");
        timerText = findViewById(R.id.timer_text);

        // timer counter initialize
        runner = new TimerCount();
        resultTime = timerText.getText().toString();
        runner.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,resultTime);

        // steps and miles views initialize
        stepCountText = findViewById(R.id.activity_miles_number2);
        milesText = findViewById(R.id.activity_miles_number);
        stepsPerMile = getIntent().getDoubleExtra("stepsPerMileFromHome", 1);

        // Check from String extra if a test FitnessService is being passed
        String fitnessServiceKey = getIntent().getStringExtra(FITNESS_SERVICE_KEY);
        if(fitnessServiceKey == null) {
            fitnessServiceKey = "GOOGLE_FIT";
            testStep = false;
        }
        fitnessService = FitnessServiceFactory.getFS(fitnessServiceKey);


        // Make a new TimeData object based on what's in shared prefs
        timeData = new TimeData();
        timeData.update(getSharedPreferences(TimeData.TIME_DATA, MODE_PRIVATE));
        Log.d(TAG, "Get time: " + timeData.getTime());
        // Initialize startTime, the time we started the session
        startTime = timeData.getTime();
        Log.d(TAG, "Session START TIME: " + startTime);


        // Button that stops the activity
        Button stopActivity = (Button) findViewById(R.id.stop_btn);
        stopActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sc.cancel(true);
                isCancelled = true;
                finish();
                launchRouteForm();
            }
        });
        // Button that opens mockPage
        /*
        Button mockPageButton = (Button) findViewById(R.id.mockPageButton);
        mockPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchMockPage();
            }
        });

         */

        // Check if steps were mocked before this activity
        SharedPreferences sf = getSharedPreferences("MockSteps" , MODE_PRIVATE);
        long stepsFromMock = sf.getLong("getsteps", -1);
        if(stepsFromMock != -1) {
            this.stepsFromMock = stepsFromMock;
            stepCount = 0;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_test, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }

         */
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
