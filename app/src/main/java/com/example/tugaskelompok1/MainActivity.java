package com.example.tugaskelompok1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private NoteDatabaseHelper dbHelper;
    private ArrayList<Note> noteList;
    private EditText inputSearch;
    private NoteAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.list_notes);
        Button btnAdd = findViewById(R.id.btn_add_note);
        inputSearch = findViewById(R.id.input_search);
        dbHelper = new NoteDatabaseHelper(this);

        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (adapter != null) {
                    adapter.filter(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        btnAdd.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddEditNoteActivity.class);
            startActivity(intent);
        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Note selectedNote = noteList.get(position);
            Intent intent = new Intent(MainActivity.this, AddEditNoteActivity.class);
            intent.putExtra("note_id", selectedNote.getId());
            intent.putExtra("note_title", selectedNote.getTitle());
            intent.putExtra("note_description", selectedNote.getDescription());
            if (selectedNote.getImagePath() != null) {
                intent.putExtra("note_image_path", selectedNote.getImagePath());
            }

            startActivity(intent);
        });

        loadNotes();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadNotes();
    }

    private void loadNotes() {
        try {
            noteList = dbHelper.getAllNotes();
            adapter = new NoteAdapter(this, noteList);
            listView.setAdapter(adapter);
        } catch (Exception e) {
            Log.e("MainActivity", "loadNotes error: " + e.getMessage(), e);
            Toast.makeText(this, "Gagal load data", Toast.LENGTH_SHORT).show();
        }
    }
}
