package com.example.walkwalkrevolution;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class NotesPage extends AppCompatActivity {

    // Activity objects
    private Button saveButton;
    private Button cancelButton;
    private EditText notesEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notes_page);

        notesEditText = (EditText) findViewById(R.id.notesEditText);

        // Check which intent we are from
        Intent fromIntent = this.getIntent();
        if (fromIntent != null) {

            try {
                // Try to get the current saved notes of the route
                String notes = fromIntent.getExtras().getString("notes");

                notesEditText.setText(notes);

            } catch(NullPointerException e) {
            }

        }

        // Handle onClickListeners for Save and Cancel Buttons
        saveButton = (Button) findViewById(R.id.SaveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                saveNotes();
            }
        });
        cancelButton = (Button) findViewById(R.id.CancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                cancel();
            }
        });
    }

    /**
     * Save button behavior. Saves the notes in the notesEditText.
     */
    private void saveNotes() {
        String notes = notesEditText.getText().toString();

        // Pass the new date back to the RoutesForm
        Intent intent = new Intent();
        intent.putExtra("newNotes", notes);
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * Cancel button behavior. Go to Routes Form Activity without saving data.
     */
    private void cancel() {
        finish();
    }

}
