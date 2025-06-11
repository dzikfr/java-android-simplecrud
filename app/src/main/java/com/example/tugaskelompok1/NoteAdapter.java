package com.example.tugaskelompok1;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class NoteAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Note> notes;
    private LayoutInflater inflater;
    private ArrayList<Note> originalNotes;

    public NoteAdapter(Context context, ArrayList<Note> notes) {
        this.context = context;
        this.notes = new ArrayList<>(notes); // filtered list
        this.originalNotes = new ArrayList<>(notes); // full list
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return notes.size();
    }

    @Override
    public Object getItem(int position) {
        return notes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return notes.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = inflater.inflate(R.layout.note_item, parent, false);
        }

        TextView title = view.findViewById(R.id.text_title);
        TextView description = view.findViewById(R.id.text_description);
        ImageView image = view.findViewById(R.id.image_note);

        Note note = notes.get(position);
        title.setText(note.getTitle());
        description.setText(note.getDescription());

        if (note.getImagePath() != null) {
            image.setVisibility(View.VISIBLE);
            image.setImageURI(Uri.parse(note.getImagePath()));
        } else {
            image.setVisibility(View.GONE);
        }

        return view;
    }

    public void filter(String query) {
        query = query.toLowerCase().trim();
        notes.clear();

        if (query.isEmpty()) {
            notes.addAll(originalNotes);
        } else {
            for (Note note : originalNotes) {
                if (note.getTitle().toLowerCase().contains(query) ||
                        note.getDescription().toLowerCase().contains(query)) {
                    notes.add(note);
                }
            }
        }

        notifyDataSetChanged();
    }

}
