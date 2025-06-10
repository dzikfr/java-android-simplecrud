package com.example.tugaskelompok1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class AddEditNoteActivity extends AppCompatActivity {

    private EditText inputTitle, inputDescription;

    private NoteDatabaseHelper dbHelper;
    private int noteId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_note);

        inputTitle = findViewById(R.id.input_title);
        inputDescription = findViewById(R.id.input_description);
        Button btnSave = findViewById(R.id.btn_save);
        Button btnDelete = findViewById(R.id.btn_delete);
        dbHelper = new NoteDatabaseHelper(this);

        // Cek apakah ini edit mode atau tambah mode
        Intent intent = getIntent();
        if (intent.hasExtra("note_id")) {
            // Edit Mode
            noteId = intent.getIntExtra("note_id", -1);
            inputTitle.setText(intent.getStringExtra("note_title"));
            inputDescription.setText(intent.getStringExtra("note_description"));
            btnDelete.setVisibility(View.VISIBLE);
        }

        btnSave.setOnClickListener(v -> {
            String title = inputTitle.getText().toString().trim();
            String desc = inputDescription.getText().toString().trim();

            if (title.isEmpty() || desc.isEmpty()) {
                Toast.makeText(this, "Isi semua data!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (noteId == -1) {
                dbHelper.insertNote(title, desc);
                Toast.makeText(this, "Note ditambahkan!", Toast.LENGTH_SHORT).show();
            } else {
                dbHelper.updateNote(noteId, title, desc);
                Toast.makeText(this, "Note diperbarui!", Toast.LENGTH_SHORT).show();
            }

            finish(); // Balik ke MainActivity
        });

        btnDelete.setOnClickListener(v -> new AlertDialog.Builder(this)
                .setTitle("Yakin?")
                .setMessage("Catatan akan dihapus permanen.")
                .setPositiveButton("Hapus", (dialog, which) -> {
                    dbHelper.deleteNote(noteId);
                    finish();
                })
                .setNegativeButton("Batal", null)
                .show());
    }
}
