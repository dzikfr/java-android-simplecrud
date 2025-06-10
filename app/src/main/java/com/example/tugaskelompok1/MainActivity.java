package com.example.tugaskelompok1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.text.Editable;
import android.text.TextWatcher;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private NoteDatabaseHelper dbHelper;
    private ArrayList<Note> noteList;
    private EditText inputSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.list_notes);
        Button btnAdd = findViewById(R.id.btn_add_note);
        dbHelper = new NoteDatabaseHelper(this);
        inputSearch = findViewById(R.id.input_search);

        loadNotes();

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
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadNotes();
    }

    private void loadNotes() {
        noteList = dbHelper.getAllNotes();
        ArrayList<String> noteTitles = new ArrayList<>();
        for (Note note : noteList) {
            noteTitles.add(note.getTitle());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, noteTitles);
        listView.setAdapter(adapter);

        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s); // auto filter list
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
}
