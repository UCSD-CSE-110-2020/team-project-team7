package com.example.walkwalkrevolution;

import android.content.Intent;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.runner.AndroidJUnit4;

import com.example.walkwalkrevolution.R;
import com.example.walkwalkrevolution.StepCountActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import com.example.walkwalkrevolution.fitness.FitnessService;
import com.example.walkwalkrevolution.fitness.FitnessServiceFactory;
import com.example.walkwalkrevolution.fitness.TestFitnessServiceAdapter;

import static com.google.common.truth.Truth.assertThat;

@RunWith(AndroidJUnit4.class)
@Config(sdk = 27)
public class FitnessServiceTest {
    private static final String TEST_SERVICE = "TEST_SERVICE";

    private Intent intent;
    private long nextStepCount;

    @Before
    public void setUp() {
        // Instantiate test FitnessService
        //TestFitnessService tfs = new TestFitnessService();
        //FitnessServiceFactory.put(TEST_SERVICE, TestFitnessService::new);
        //FitnessServiceFactory.put(TEST_SERVICE, tfs);
        intent = new Intent(ApplicationProvider.getApplicationContext(), HomePage.class);

        // Pass Test variables to Homepage
        intent.putExtra(HomePage.FITNESS_SERVICE_KEY, TEST_SERVICE);
        intent.putExtra("testStep", true);
    }

    @Test
    public void testUpdateStepsButton() {
//        nextStepCount = 1337;
//
//        ActivityScenario<HomePage> scenario = ActivityScenario.launch(intent);
//
//        FitnessService tfsa = FitnessServiceFactory.getFS(TEST_SERVICE);
//        tfsa.setNextStepCount(nextStepCount);
//        tfsa.updateStepCount();
//
//        scenario.onActivity(activity -> {
//            TextView textSteps = activity.findViewById(R.id.stepCountText);
//            assertThat(textSteps.getText().toString()).isEqualTo(String.valueOf(nextStepCount));
//        });
    }
/*
    private class TestFitnessService implements FitnessService {
        private static final String TAG = "[TestFitnessService]: ";
        private HomePage homePage;

        public TestFitnessService() {
            //this.homePage = homePage;
        }

        @Override
        public int getRequestCode() {
            return 0;
        }

        @Override
        public void setup() {
            System.out.println(TAG + "setup");
        }

        @Override
        public void updateStepCount() {
            System.out.println(TAG + "updateStepCount");
            //homePage.setStepCount(nextStepCount);
           // homePage.updateStepView(String.valueOf(homePage.getStepCount());
        }
    }
 */
}