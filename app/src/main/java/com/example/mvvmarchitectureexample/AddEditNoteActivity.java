package com.example.mvvmarchitectureexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

public class AddEditNoteActivity extends AppCompatActivity {

    // in complicated project, another ViewModel class be needed
    // for simplicity - since this activity is only used to get inputs -> don't interact with anything
    // intent extras can be used to handle data transfer to main activity
    public static final String EXTRA_TITLE = "TITLE";
    public static final String EXTRA_DESC = "DESC";
    public static final String EXTRA_PRIORITY = "PRIORITY";
    public static final String EXTRA_ID = "ID";

    private EditText edtTitle, edtDescription;
    private NumberPicker nPickerPriority;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        edtTitle = findViewById(R.id.edt_title);
        edtDescription = findViewById(R.id.edt_description);
        nPickerPriority = findViewById(R.id.number_picker_priority);

        nPickerPriority.setMinValue(1);
        nPickerPriority.setMaxValue(10);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.baseline_close_24);

        Intent intent = getIntent();

        if (intent.hasExtra(EXTRA_ID)) { // EXTRA_ID is added when we want to use edit functionality
            setTitle("Edit Note");
            edtTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            edtDescription.setText(intent.getStringExtra(EXTRA_DESC));
            nPickerPriority.setValue(intent.getIntExtra(EXTRA_PRIORITY, 1));
        }
        else {
            setTitle("Add Note");
        }
    }

    private void saveNote() {
        String title = edtTitle.getText().toString();
        String description = edtDescription.getText().toString();
        int priority = nPickerPriority.getValue();

        if (title.trim().isEmpty() || description.trim().isEmpty()) {
            Toast.makeText(this, "Please insert title & description", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_DESC, description);
        data.putExtra(EXTRA_PRIORITY, priority);

        if (getIntent().hasExtra(EXTRA_ID)) {
            data.putExtra(EXTRA_ID, getIntent().getIntExtra(EXTRA_ID, -1)); // I know it's stupid but i don't care
        }

        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_note:
                saveNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}