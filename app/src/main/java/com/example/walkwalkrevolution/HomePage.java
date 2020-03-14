package com.example.walkwalkrevolution;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.walkwalkrevolution.custom_data_classes.ProposedWalk;
import com.example.walkwalkrevolution.custom_data_classes.Route;
import com.example.walkwalkrevolution.fitness.FitnessServiceFactory;
import com.example.walkwalkrevolution.fitness.FitnessService;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.example.walkwalkrevolution.forms.HeightForm;
import com.example.walkwalkrevolution.forms.MockPage;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.List;

public class HomePage extends AppCompatActivity implements UpdateStepTextView {
    //Default set to non-testing
    public static boolean MOCK_TESTING = false;
    public static boolean is_Proposed_Walk_Creator = false;
    private static boolean isFirstTime = true;


    public static final int RC_SIGN_IN = 55;
    public static final String FITNESS_SERVICE_KEY = "FITNESS_SERVICE_KEY";
    public final String TAG = "Home Page";

    public TextView milesText;
    public long stepCount;
    public double milesCount;
    public double stepsPerMile;

    public FitnessService fitnessService;
    private String fitnessServiceKey = "GOOGLE_FIT";
    public boolean testStep = true;
    public StepCountActivity sc;
    public TextView stepCountText;
    public GoogleSignInClient mGoogleSignInClient;
    public GoogleSignInAccount currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Log.d("HOMEPAGE ONCREATE", "creating homepage");

        if(isFirstTime){
            Log.d("Welcome", "LoginPage");
            Boolean value = this.getIntent().getBooleanExtra("testService",false);
            Boolean creator = this.getIntent().getBooleanExtra("creator", false);
            MOCK_TESTING = value;
            is_Proposed_Walk_Creator = creator;
        }

        //TODO - MOCK TESTING DONE HERE FOR CURRENT USER
        if(MOCK_TESTING) {
            if(!isFirstTime){
                Log.d("HOMEPAGE ONCREATE", "USER DETAILS ALREADY MOCKED -- SKIPPING");
                return;
            }
            Log.d("HOMEPAGE ONCREATE", "USER DETAILS SUCCESSFULLY MOCKED");
            isFirstTime = false;
            UserDetails mockCurrentUser = new UserDetails("Yoshi", "Yoshi@gmail.com");
            UserDetailsFactory.put("currentUser", mockCurrentUser);

            TeamMember mockedMember = new TeamMember("Amrit Singh", "aksingh@ucsd.edu", true);
            Route mockedRoute = Route.RouteBuilder.newInstance()
                    .setName("Route 2")
                    .setStartingPoint("start2")
                    .setSteps(40)
                    .setDistance(23.8)
                    .setDate("3/4/20")
                    .setDuration(4, 10)
                    .setOptionalFeatures(null)
                    .setOptionalFeaturesStr(null)
                    .setNotes("notes")
                    .setUserHasWalkedRoute(true)
                    .setCreator(mockedMember)
                    .buildRoute();
            TeamMemberFactory.put("aksingh@ucsd.edu",mockedMember );

            Route mockedRoute2 = Route.RouteBuilder.newInstance()
                    .setName("Route 3")
                    .setStartingPoint("start3")
                    .setSteps(50)
                    .setDistance(12.9)
                    .setDate("1/9/20")
                    .setDuration(1, 29)
                    .setOptionalFeatures(null)
                    .setOptionalFeaturesStr(null)
                    .setNotes("notes")
                    .setUserHasWalkedRoute(false)
                    .setCreator(mockedMember)
                    .buildRoute();
            TeamMemberFactory.put("aksingh@ucsd.edu",mockedMember );

            TeamMemberFactory.addRoute(new Pair<>("aksingh@ucsd.edu",mockedRoute));
            TeamMemberFactory.addRoute(new Pair<>("aksingh@ucsd.edu",mockedRoute2));

            ProposedWalk mock = null;
            if(!HomePage.is_Proposed_Walk_Creator){
                TeamMember creatorMock = TeamMemberFactory.get("aksingh@ucsd.edu");
                Log.d(TAG, "Teammember null: " + (creatorMock == null));
                mock = new ProposedWalk("Ash Road", "03/13/2020", "12:00 PM", creatorMock);
            }else{
                UserDetails user = UserDetailsFactory.get("currentUser");
                Log.d(TAG, "user null: " + (user == null));
                mock = new ProposedWalk("Ash Road", "03/13/2020", "12:00 PM", new TeamMember(user.getName(), user.getEmail(), true));
            }
            TeamMemberFactory.setProposedWalk(mock);

        }else{
            // Initiallize firebase
            Log.d("Firebase", "Firebase");
            FirebaseApp.initializeApp(this);

            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();
            // Build a GoogleSignInClient with the options specified by gso.
            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        }


        // retrieve height;
        final SharedPreferences getHeight = getSharedPreferences("height", 0);
        int feet = getHeight.getInt("height_ft", 5);
        int inches = getHeight.getInt("height_in", 7);
        int heightInInches = (feet * 12) + inches;
        stepsPerMile = calculateStepsPerMile(heightInInches);

        // save stepsPerMile into shared prefs as a string
        SharedPreferences sharedPreferences = getSharedPreferences("stepsPerMileFromHome", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("stepsPerMileFromHome", stepsPerMile + "");
        editor.apply();


        // Check from String extra if a test FitnessService is being passed
        fitnessServiceKey = getIntent().getStringExtra(FITNESS_SERVICE_KEY);
        //fitnessServiceKey = "TEST_SERVICE";
        if(fitnessServiceKey == null) {
            fitnessServiceKey = "GOOGLE_FIT";
            testStep = false;
        }

        // Creates specified FitnessService adapter depending on key
        FitnessServiceFactory.put(fitnessServiceKey, FitnessServiceFactory.create(fitnessServiceKey, this));

        // Get specified FitnessService using fitnessServiceKey from Blueprint
        fitnessService = FitnessServiceFactory.getFS(fitnessServiceKey);
        fitnessService.setup();
        checkNotif();

        // Async Textviews
        stepCountText = findViewById(R.id.stepCountText);
        milesText = findViewById(R.id.distanceCountText);

        // used to start the walk/run activity
        Log.d(TAG, "Started AsyncTask for step counter");

        Button launchActivity = (Button) findViewById(R.id.startButt);
        launchActivity.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                launchSession();
            }
        });

        // used to go to the routes page
        Button routesPage = findViewById(R.id.routesButt);
        routesPage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                routesSession();
            }
        });

        // Button that opens mockPage
        Button mockPageButton = (Button) findViewById(R.id.mockPageButton);
        mockPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchMockPage();
            }
        });

        // Button that opens mockPage
        Button teammatesButton = (Button) findViewById(R.id.teammatesButton);
        teammatesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchTeammatesPage();
            }
        });

        // Initiallize firebase
        FirebaseApp.initializeApp(this);

        // --------------- [START] GOOGLE SIGN IN --------------- //
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        // --------------- [END]   GOOGLE SIGN IN --------------- //
    }

    @Override
    protected void onStop() {
        Log.d("HOMEPAGE ON STOP", "stopped called");
        super.onStop();

        // stop async task
        if (!HomePage.MOCK_TESTING) {
            sc.cancel(true);
        }
    }

    @Override
    protected void onStart() {
        Log.d("HOMEPAGE ON START", "start called");
        super.onStart();

        if(MOCK_TESTING){
            UserDetails currUser = UserDetailsFactory.get("currentUser");
            SharedPreferences sharedPreferences = getSharedPreferences(CurrentUserLocalStorage.SHARED_PREFS_CURRENT_USER_INFO, MODE_PRIVATE);
            CurrentUserLocalStorage.firstTimeLogin(sharedPreferences, currUser.getName(), currUser.getEmail());
            return;
        }

        // --------------- [START] GOOGLE SIGNIN --------------- //
        currentUser = GoogleSignIn.getLastSignedInAccount(this);
        if(currentUser == null || currentUser.getEmail() == null) {
            Log.d("GOOGLEAUTH", "before sign in");
            signIn();
        } else {
            Log.d("GOOGLEAUTH", "signed in apparently, email: " + currentUser.getEmail());

            SharedPreferences sharedPreferences = getSharedPreferences(CurrentUserLocalStorage.SHARED_PREFS_CURRENT_USER_INFO, MODE_PRIVATE);
            CurrentUserLocalStorage.firstTimeLogin(sharedPreferences, currentUser.getDisplayName(), currentUser.getEmail());

            CloudDatabase.populateUserDetails(currentUser.getEmail(), currentUser.getDisplayName(), new CloudCallBack() {
                @Override
                public void callBack() {
                    subscribeToNotificationsTopic();
                    evokeFirstLogin();
                    Log.d("FIRST LOGIN", "CALLED FIRST LOGIN");
                }
            });
        }
        // --------------- [END]   GOOGLE SIGNIN --------------- //

        // check to see if user is new
        //SharedPreferences settings = getSharedPreferences("MyPrefsFile", 0);
        //firstLogin(settings);

        // get latest walk
        displayLastWalk();

        // Create async task
        sc = new StepCountActivity(fitnessService, testStep);
        sc.updateStep = this;
        checkNotif();

        // if steps were modified from mock page change steps to static mock steps
        SharedPreferences sf = getSharedPreferences("MockSteps" , MODE_PRIVATE);

        long stepsFromMock = sf.getLong("getsteps", -1);
        if(stepsFromMock != -1) {
            setStepCount(stepsFromMock);
            setMiles((Math.floor((stepsFromMock / stepsPerMile) * 100)) / 100);
            updateStepView(String.valueOf(getStepCount()) + " steps");
            updatesMilesView(String.valueOf(getMiles()) + " miles");
            sc.turnOffAPI = true;   // turn off google api if doing mock
        }
        sc.execute();
    }

    @Override
    protected void onDestroy() {
        Log.d("HOMEPAGE ON DESTROY", "being destroy");
        super.onDestroy();
        if(HomePage.MOCK_TESTING){
            return;
        }
        if(!sc.isCancelled()) {
            sc.cancel(true);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //If authentication was required during google fit setup,
        // this will be called after the user authenticates
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == fitnessService.getRequestCode()) {
                fitnessService.updateStepCount();
            }
            // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
            if (requestCode == RC_SIGN_IN) {
                // The Task returned from this call is always completed, no need to attach
                // a listener.
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                handleSignInResult(task);
            }
        } else {
            Log.e(TAG, "ERROR, google fit result code: " + resultCode);
        }
    }

    /**
     * google signin method for access to account details
     */
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    /**
     * currentUser now holds user's email, uid
     */
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        //TODO - MOCK TESTING DONE HERE FOR USER SIGN IN
        if(MOCK_TESTING){
            UserDetails currUser = UserDetailsFactory.get("currentUser");
            SharedPreferences sharedPreferences = getSharedPreferences(CurrentUserLocalStorage.SHARED_PREFS_CURRENT_USER_INFO, MODE_PRIVATE);
            CurrentUserLocalStorage.firstTimeLogin(sharedPreferences, currUser.getName(), currUser.getEmail());
            return;
        }
        try {
            currentUser = completedTask.getResult(ApiException.class);

            SharedPreferences sharedPreferences = getSharedPreferences(CurrentUserLocalStorage.SHARED_PREFS_CURRENT_USER_INFO, MODE_PRIVATE);
            CurrentUserLocalStorage.firstTimeLogin(sharedPreferences, currentUser.getDisplayName(), currentUser.getEmail());

            CloudDatabase.populateUserDetails(currentUser.getEmail(), currentUser.getDisplayName(), new CloudCallBack() {
                @Override
                public void callBack() {
                    subscribeToNotificationsTopic();
                }
            });
            Log.d("GOOGLEAUTH", "inside handlesignin, currentUser.getEmail()");
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }

    /**
     * Displays the last intentional (saved) walk taken by the user on the HomeScreen.
     */
    private void displayLastWalk(){
        //get references to HomeScreen Views
        TextView stepValue = (TextView) findViewById(R.id.stepsValue);
        TextView distanceValue = (TextView) findViewById(R.id.distanceValue);
        TextView timeValue = (TextView) findViewById(R.id.timeValue);

        //gets last walk via sharedPreferences
        List<String> list = LastIntentionalWalk.loadLastWalk(getSharedPreferences(LastIntentionalWalk.SHARED_PREFS_INTENTIONAL_WALK, MODE_PRIVATE));
        if(list == null) {
            stepValue.setText("0");
            distanceValue.setText("0");
            timeValue.setText("0");
        } else {
            stepValue.setText(list.get(0));
            distanceValue.setText(list.get(1));
            timeValue.setText(list.get(2));
        }
    }

    /**
     * updateStepView, setStepCount, getStepCount implement UpdateStepInterface
     * setStepCount is called within GoogleFitAdapter.java --> updates stepCount to amount of steps
     * getStepCount is called within StepCountActivity.java --> get stepCount
     * updateStepView is called within StepCountActivity.java --> update TextView to stepCount
     */
    public void updateStepView(String str) { stepCountText.setText(str); }

    public void setStepCount(long sc) { stepCount = sc; }

    public long getStepCount() { return stepCount; }

    public void updatesMilesView(String str) { milesText.setText(str); }

    public void setMiles(double mi) { milesCount = mi; }

    public double getMiles() { return milesCount; }

    public double getStepsPerMile() { return this.stepsPerMile; }

    /**
     * used to launch the Routes Screen
     */
    public void launchTeammatesPage(){
        Log.d(TAG, "Launching Team Page");
        Intent intent = new Intent(this, TeammatesPage.class);
        startActivity(intent);
    }

    /**
     * used to launch the Routes Screen
     */
    public void routesSession(){
        Log.d(TAG, "Launching Routes Screen");
        Intent intent = new Intent(this, RoutesList.class);
        startActivity(intent);
    }

    /**
     * used to launch the walk/run session
     */
    public void launchSession(){
        Log.d("HOMEPAGE LAUNCH WALK SESSION", "launching walkrunsession");
        Intent intent = new Intent(this, WalkRunSession.class);

        intent.putExtra(FITNESS_SERVICE_KEY, fitnessServiceKey);
        SharedPreferences sf = getSharedPreferences("MockSteps" , 0);
        //sf.edit().putLong("getsteps", -1).apply();
        startActivity(intent);
    }

    /**
     * launched only once, when the app is opened for the first time
     */
    public void launchFirstSession(){
        Log.d(TAG, "Launching HeightForm");
        Intent intent = new Intent(this, HeightForm.class);
        startActivity(intent);
    }

    /**
     * first time the user opens the app calls HeightForm
     */
    public void firstLogin(SharedPreferences pref){
        if (pref.getBoolean("my_first_time", true)) {
            Log.d(TAG, "First Time Launch");
            //the app is being launched for first time
            launchFirstSession();
            // record the fact that the app has been started at least once
            pref.edit().putBoolean("my_first_time", false).commit();
        }
    }

    public int calculateStepsPerMile(int heightInInches) {
        double strideLengthFeet = (heightInInches * 0.413) / 12;
        return (int)(5280 / strideLengthFeet);
    }

    /**
     * launches the mock page
     */
    private void launchMockPage() {
        Log.d("IN LAUNCH MOCKPAGE", String.valueOf(stepCount));
        Intent intent = new Intent(this, MockPage.class);
        intent.putExtra("stepCountFromHome", stepCount);
        startActivity(intent);
    }


    /**
     * subscribes to the team pages notification
     */
    public static void subscribeToNotificationsTopic() {
            FirebaseMessaging.getInstance().subscribeToTopic("topic")
                    .addOnCompleteListener(task -> {
                                String msg = "Notif subbed!";
                                if (!task.isSuccessful()) {
                                    msg = "Notif failed :(";
                                }
                                Log.d("Sub_Message", msg);
                            }
                    );

        FirebaseMessaging.getInstance().subscribeToTopic("topic2")
                .addOnCompleteListener(task -> {
                            String msg = "Notif2 subbed!";
                            if (!task.isSuccessful()) {
                                msg = "Notif2 failed :(";
                            }
                            Log.d("Sub_Message", msg);
                        }
                );
    }
    public void checkNotif(){
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        Log.d(TAG, "Intent: check notif " + intent.toString());
        if (intent.getExtras() != null) {
            Log.d(TAG, "Extras: " + intent.getExtras().toString());
            Log.d(TAG, "Extras Keyset: " + intent.getExtras().keySet().toString());
        }else{
            Log.d(TAG, "Null");
        }
        if (intent != null) {
            String intentStringExtra = intent.getStringExtra(Intent.EXTRA_TEXT);
            if (intentStringExtra != null) {
                Log.d(TAG, "intentStringExtra: " + intentStringExtra);
            }
        }
        if (bundle != null) {
            boolean check = false;
            boolean details = false;
            String buffer = "";
            for (String key : bundle.keySet()) {
                if (bundle.get(key) != null) {
                    Log.d(TAG, "key: " + key + ", value: " + bundle.get(key).toString());
                    if(bundle.get(key).equals("invite_page")){
                        check = true;
                    }
                    if(key.equals("email")){
                        buffer = bundle.get(key).toString();
                    }
                    if(bundle.get(key).equals("proposed_details")){
                        details = true;
                    }
                } else {
                    Log.d(TAG, "key: " + key + ", value: None");
                }
            }
            if(check){
                Intent team = new Intent(this, TeammatesPage.class);
                Log.d(TAG, buffer);
                team.putExtra("email", buffer);
                startActivity(team);
            }

            if(details){
                Intent team = new Intent(this, TeammatesPage.class);
                startActivity(team);
            }
        }
    }


    /**
     * Helper method to evoke first login if necessary
     */
    private void evokeFirstLogin() {
        SharedPreferences settings = getSharedPreferences("MyPrefsFile", 0);
        firstLogin(settings);
    }
}


