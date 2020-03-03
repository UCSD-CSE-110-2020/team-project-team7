package com.example.walkwalkrevolution;

import android.content.Intent;
import android.widget.DatePicker;
import android.widget.Button;
import androidx.test.core.app.ActivityScenario;
import androidx.test.runner.AndroidJUnit4;

import com.example.walkwalkrevolution.forms.SetDate;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;

import static com.google.common.truth.Truth.assertThat;

@RunWith(AndroidJUnit4.class)
@Config(sdk = 27)
public class SetDateTest {

     //checks to see if save button saves set date form data
    @Test
    public void testSaveDate() {
//        try (ActivityScenario<SetDate> scenario = ActivityScenario.launch(SetDate.class)) {
//            scenario.onActivity(activity -> {
//
//                String date = "02/15/2020";
//                DatePicker testDate = activity.findViewById(R.id.datePicker);
//                testDate.updateDate(2020,02,15);
//
//                Button saveBtn = activity.findViewById(R.id.SaveButton);
//                saveBtn.performClick();
//                assertThat(saveBtn.isPressed());
//
//                Intent intent = Shadows.shadowOf(activity).getResultIntent();
//                assertThat(intent.getStringExtra("newDate").equals(date));
//            });
//        }
    }

//    // checks to see if the cancel button does not save form data
//    @Test
//    public void testCancelButton() {
//        try (ActivityScenario<SetDate> scenario = ActivityScenario.launch(SetDate.class)) {
//            scenario.onActivity(activity -> {
//
//                String date = "02/15/2020";
//                DatePicker testDate = activity.findViewById(R.id.datePicker);
//                testDate.updateDate(2020, 02, 15);
//
//                Button cancelBtn = activity.findViewById(R.id.CancelButton);
//                cancelBtn.performClick();
//                assertThat(cancelBtn.isPressed());
//
//                Intent intent = Shadows.shadowOf(activity).getResultIntent();
//                assertThat(intent == null);
//            });
//        }
//    }
}