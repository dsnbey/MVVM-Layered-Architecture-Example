package com.example.mvvmarchitectureexample;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Note.class}, version = 1)
public abstract class NoteDatabase extends RoomDatabase {

    private static NoteDatabase instance;

    public abstract NoteDAO noteDao();

    /* Singleton: only 1 instance is created -> synchronised is added to prevent
    multiple threads creating multiple instances unintentionally.
     */
    public static synchronized NoteDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            NoteDatabase.class, "note_database")
                    .fallbackToDestructiveMigration() // version no stuff
                    .addCallback(roomCallback)
                    .build();

        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDBAsyncTask(instance).execute();
        }
    };

    private static class PopulateDBAsyncTask extends AsyncTask<Void, Void, Void> {

        private NoteDAO noteDAO;

        public PopulateDBAsyncTask(NoteDatabase db) {
            this.noteDAO = db.noteDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            // For testing purposes
            noteDAO.insert(new Note("T1", "D1", 1));
            noteDAO.insert(new Note("T2", "D2", 2));
            noteDAO.insert(new Note("T3", "D3", 3));
            return null;
        }
    }
}
