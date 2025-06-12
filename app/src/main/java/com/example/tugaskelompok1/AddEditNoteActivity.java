package com.example.tugaskelompok1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class AddEditNoteActivity extends AppCompatActivity {

    private EditText inputTitle, inputDescription;
    private NoteDatabaseHelper dbHelper;
    private int noteId = -1;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    private ImageView imgPreview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_note);

        inputTitle = findViewById(R.id.input_title);
        inputDescription = findViewById(R.id.input_description);
        Button btnSave = findViewById(R.id.btn_save);
        Button btnDelete = findViewById(R.id.btn_delete);
        dbHelper = new NoteDatabaseHelper(this);
        imgPreview = findViewById(R.id.img_preview);
        Button btnPickImage = findViewById(R.id.btn_pick_image);

        btnPickImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        Intent intent = getIntent();
        if (intent.hasExtra("note_id")) {
            noteId = intent.getIntExtra("note_id", -1);
            inputTitle.setText(intent.getStringExtra("note_title"));
            inputDescription.setText(intent.getStringExtra("note_description"));

            // Set image URI dari intent biar bisa diedit
            String imgPath = intent.getStringExtra("note_image_path");
            if (imgPath != null && !imgPath.isEmpty()) {
                try {
                    imageUri = Uri.parse(imgPath);
                    imgPreview.setImageURI(imageUri);
                } catch (Exception e) {
                    Toast.makeText(this, "Gagal load gambar", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

            btnDelete.setVisibility(View.VISIBLE);
        }

        btnSave.setOnClickListener(v -> {
            String title = inputTitle.getText().toString().trim();
            String desc = inputDescription.getText().toString().trim();
            String imagePath = imageUri != null ? imageUri.toString() : null;

            if (title.isEmpty() || desc.isEmpty()) {
                Toast.makeText(this, "Isi semua data!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (noteId == -1) {
                dbHelper.insertNote(title, desc, imagePath);
                Toast.makeText(this, "Note ditambahkan!", Toast.LENGTH_SHORT).show();
            } else {
                dbHelper.updateNote(noteId, title, desc, imagePath);
                Toast.makeText(this, "Note diperbarui!", Toast.LENGTH_SHORT).show();
            }

            finish();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            if (imageUri != null) {
                try {
                    imgPreview.setImageURI(imageUri);
                } catch (Exception e) {
                    Toast.makeText(this, "Preview gagal", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }
}
