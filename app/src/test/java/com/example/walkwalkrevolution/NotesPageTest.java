package com.example.walkwalkrevolution;


import android.content.Intent;
import android.widget.EditText;
import android.widget.Button;
import androidx.test.core.app.ActivityScenario;
import androidx.test.runner.AndroidJUnit4;

import com.example.walkwalkrevolution.forms.NotesPage;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;

import static com.google.common.truth.Truth.assertThat;

@RunWith(AndroidJUnit4.class)
@Config(sdk = 27)
public class NotesPageTest {

    // checks to see if the save button saves form data
    @Test
    public void testSaveButton() {
        try (ActivityScenario<NotesPage> scenario = ActivityScenario.launch(NotesPage.class)) {
            scenario.onActivity(activity -> {

                String testString = "hello";
                EditText text = activity.findViewById(R.id.notesEditText);
                text.setText(testString);

                Button saveBtn = activity.findViewById(R.id.SaveButton);
                saveBtn.performClick();
                assertThat(saveBtn.isPressed());

                Intent intent = Shadows.shadowOf(activity).getResultIntent();
                assertThat(intent.getStringExtra("newNotes").equals(testString));
            });
        }
    }

    // checks to see if the cancel button does not save form data
    @Test
    public void testCancelButton() {
        try (ActivityScenario<NotesPage> scenario = ActivityScenario.launch(NotesPage.class)) {
            scenario.onActivity(activity -> {

                String testString = "hello";
                EditText text = activity.findViewById(R.id.notesEditText);
                text.setText(testString);

                Button cancelBtn = activity.findViewById(R.id.CancelButton);
                cancelBtn.performClick();
                assertThat(cancelBtn.isPressed());

                Intent intent = Shadows.shadowOf(activity).getResultIntent();
                assertThat(intent == null);
            });
        }
    }

}
