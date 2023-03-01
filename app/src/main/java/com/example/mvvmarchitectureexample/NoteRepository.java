package com.example.mvvmarchitectureexample;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class NoteRepository {

    private NoteDAO noteDAO;
    private LiveData<List<Note>> allNotes;

    public NoteRepository(Application application) {
        NoteDatabase database = NoteDatabase.getInstance(application);
        noteDAO = database.noteDao(); // since db instance is created with dbBuilder, noteDAO methods are generated
        allNotes = noteDAO.getAllNotes();
    }


    // Following method is an API for db operations:

    public void insert(Note note) {
        new InsertNoteAsyncTask(noteDAO).execute(note);
    }
    public void update(Note note) {
        new UpdateNoteAsyncTask(noteDAO).execute(note);
    }
    public void delete(Note note) {
        new DeleteNoteAsyncTask(noteDAO).execute(note);
    }
    public void deleteAllNotes() {
        new DeleteAllNoteAsyncTask(noteDAO).execute();
    }

    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }

    // Room does db operations not in main thread, otherwise the app would freeze.
    // To separate the db tasks from main thread, use async task. -> If not, app would crash!
    // static -> no reference class -> no memory leak
    private static class InsertNoteAsyncTask extends AsyncTask<Note, Void, Void> {

        private NoteDAO noteDAO;

        private InsertNoteAsyncTask(NoteDAO noteDAO) {
            this.noteDAO = noteDAO;
        }
        @Override
        protected Void doInBackground(Note... notes) {
            noteDAO.insert(notes[0]); // pass 1 note, access first element
            return null;
        }
    }

    private static class UpdateNoteAsyncTask extends AsyncTask<Note, Void, Void> {

        private NoteDAO noteDAO;

        private UpdateNoteAsyncTask(NoteDAO noteDAO) {
            this.noteDAO = noteDAO;
        }
        @Override
        protected Void doInBackground(Note... notes) {
            noteDAO.update(notes[0]); // pass 1 note, access first element
            return null;
        }
    }

    private static class DeleteNoteAsyncTask extends AsyncTask<Note, Void, Void> {

        private NoteDAO noteDAO;

        private DeleteNoteAsyncTask(NoteDAO noteDAO) {
            this.noteDAO = noteDAO;
        }
        @Override
        protected Void doInBackground(Note... notes) {
            noteDAO.delete(notes[0]); // pass 1 note, access first element
            return null;
        }
    }

    private static class DeleteAllNoteAsyncTask extends AsyncTask<Void, Void, Void> {

        private NoteDAO noteDAO;

        private DeleteAllNoteAsyncTask(NoteDAO noteDAO) {
            this.noteDAO = noteDAO;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            noteDAO.deleteAllNotes(); // pass 1 note, access first element
            return null;
        }
    }
}
