package com.example.spacerace.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;

public class NoteDB extends SQLiteOpenHelper {
    // Database metadata
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "spacerace";

    public NoteDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Tables
    private static final String TABLE_NOTES = "notes";

    // Columns
    private static final String COLUMN_NOTE_ID = "id";
    private static final String COLUMN_NOTE_TITLE = "title";
    private static final String COLUMN_NOTE_BODY = "body";
    private static final String COLUMN_NOTE_DATE = "date";


    // SQL
    private static final String CREATE_NOTE_TABLE = "CREATE TABLE " + TABLE_NOTES + "("
            + COLUMN_NOTE_ID + " INTEGER PRIMARY KEY, "
            + COLUMN_NOTE_TITLE + " TEXT, "
            + COLUMN_NOTE_BODY + " TEXT, "
            + COLUMN_NOTE_DATE + " TEXT" + ")";

    /**
     * Intialize the database by creating tables
     * @param db The database to operate on
     * @author Zachary Allard
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_NOTE_TABLE);
    }

    /**
     * Drop old tables when upgraded
     * @param db Database to reset
     * @param oldVersion Old version number
     * @param newVersion New version number
     * @author Zachary Allard
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
        onCreate(db);
    }

    // CRUD Operations

    /**
     * Create Note in database
     * @param note Note to create
     * @author Zachary Allard
     */
    public void addNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOTE_TITLE, note.getTitle());
        values.put(COLUMN_NOTE_BODY, note.getBody());
        values.put(COLUMN_NOTE_DATE, note.getDate());
        db.insert(TABLE_NOTES, null, values);
        db.close();
    }

    /**
     * Retrieve single Note from database
     * @param id ID of Note in database
     * @author Zachary Allard
     */
    public Note getNote(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NOTES,
                new String[] { COLUMN_NOTE_ID, COLUMN_NOTE_TITLE, COLUMN_NOTE_BODY, COLUMN_NOTE_DATE}, COLUMN_NOTE_ID+ "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        Note note = new Note(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3));
        return note;
    }

    /**
     * Retrieve all Notes from database
     * @return ArrayList of all Notes
     * @author Zachary Allard
     */
    public ArrayList<Note> getAllNotes() {
        ArrayList<Note> notes = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_NOTES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Note note = new Note();
                note.setId(Integer.parseInt(cursor.getString(0)));
                note.setTitle(cursor.getString(1));
                note.setBody(cursor.getString(2));
                note.setDate(cursor.getString(3));
                notes.add(note);
            } while (cursor.moveToNext());
        }
        return notes;
    }

    /**
     * Update a Note in the database
     * @param note The Note to modify
     * @return The status of the update
     * @author Zachary Allard
     */
    public int updateNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOTE_TITLE, note.getTitle());
        values.put(COLUMN_NOTE_BODY, note.getBody());
        values.put(COLUMN_NOTE_DATE, note.getDate());
        return db.update(TABLE_NOTES, values, COLUMN_NOTE_ID + " = ?", new String[] { String.valueOf(note.getId())});
    }

    /**
     * Delete a Note in the database
     * @param note_id The ID of the note being deleted
     * @author Zachary Allard
     */
    public void deleteNote(long note_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NOTES, COLUMN_NOTE_ID + " = ?",
                new String[] { String.valueOf(note_id) });
    }


    /**
     * Use in external classes to close connection
     * @author Zachary Allard
     */
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }
}
