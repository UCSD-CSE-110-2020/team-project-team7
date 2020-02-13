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

import static com.google.common.truth.Truth.assertThat;

@RunWith(AndroidJUnit4.class)
@Config(sdk = 27)
public class StepCountActivityUnitTest {
    private static final String TEST_SERVICE = "TEST_SERVICE";

    private Intent intent;
    private long nextStepCount;

    @Before
    public void setUp() {
        FitnessServiceFactory.put(TEST_SERVICE, TestFitnessService::new);
        System.out.println("after put");
        intent = new Intent(ApplicationProvider.getApplicationContext(), HomePage.class);
        System.out.println("intent");
        //intent.putExtra(HomePage.FITNESS_SERVICE_KEY, TEST_SERVICE);
    }

    @Test
    public void testUpdateStepsButton() {
        nextStepCount = 1337;
        System.out.println("step");

        ActivityScenario<HomePage> scenario = ActivityScenario.launch(intent);

        System.out.println("launched activity");

        scenario.onActivity(activity -> {
            TextView textSteps = activity.findViewById(R.id.stepCountText);
            activity.fitnessServiceKey = TEST_SERVICE;

            assertThat(textSteps.getText().toString()).isEqualTo(String.valueOf(nextStepCount));
        });
    }

    private class TestFitnessService implements FitnessService {
        private static final String TAG = "[TestFitnessService]: ";
        private HomePage homePage;

        public TestFitnessService(HomePage homePage) {
            this.homePage = homePage;
            System.out.println("homepage ctor");
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
            homePage.setStepCount(nextStepCount);
        }
    }
}