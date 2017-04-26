package ru.rkhamatyarov.yatranslator;


import android.app.Activity;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

/**
 * Created by Asus on 25.04.2017.
 */

public class FavoriteActivity extends ListActivity implements View.OnClickListener {

    private final String LOG_TAG = "YaLog";

    private static final String EXTRA_LINE_FROM = "wordsFrom";
    private static final String EXTRA_LINE_TO = "wordsTo";
    private static final String EXTRA_LANG_DIR = "langDir";
    private DBHelper dbHelper;
    private SQLiteDatabase db;
    private Cursor cursor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(LOG_TAG, "beginning");
        super.onCreate(savedInstanceState);
        ListView favoriteWordsView = getListView();
//        setContentView(R.layout.activity_favorite);
        Log.d(LOG_TAG, "after getListView()");
        dbHelper = new DBHelper(this);
        try {
            db = dbHelper.getReadableDatabase();
            Log.d(LOG_TAG, "after getReadableDatabase()");
            cursor = db.query("FAVORITE_WORDS",
                            new String[]{"_id", "WordsFrom"},
                            null, null, null, null, "WordsFrom"+" DESC");

            Log.d(LOG_TAG, "after db.query()");
            CursorAdapter favoriteAdapter = new SimpleCursorAdapter(this, R.layout.activity_favorite,
                    cursor, new String[]{"WordsFrom"},
                    new int[]{R.id.wordsFrom}, 0);
            Log.d(LOG_TAG, "favoriteAdapter");
            favoriteWordsView.setAdapter(favoriteAdapter);
//            Log.d("ListView", favoriteWordsView.getItemAtPosition(0).toString());

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    String str;
                    do {
                        str = "";
                        for (String cn : cursor.getColumnNames()) {
                            str = str.concat(cn + " = "
                                    + cursor.getString(cursor.getColumnIndex(cn)) + "; ");
                        }
                        Log.d(LOG_TAG, str);

                    } while (cursor.moveToNext());
                }
//                cursor.close();
            } else
                Log.d(LOG_TAG, "Cursor is null");

        } catch (SQLiteException exc) {
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
        Log.d(LOG_TAG, "before insertRow");
        insertRow();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cursor.close();
        db.close();
}

    public void insertRow() {
        //get strings from main activity
        Log.d(LOG_TAG, "beginning");
        Intent intent = getIntent();
        String langDirs = (String) intent.getExtras().get(EXTRA_LANG_DIR);
        String linesFrom = (String) intent.getExtras().get(EXTRA_LINE_FROM);
        String linesTo = (String) intent.getExtras().get(EXTRA_LINE_TO);
        Log.d(LOG_TAG, "after get intent");
        Log.d(LOG_TAG, linesFrom);
        ContentValues transValues = new ContentValues();
        transValues.put("LangDir", langDirs);
        transValues.put("WordsFrom", linesFrom);
        transValues.put("WordsTo", linesTo);
        Log.d(LOG_TAG, "after contentvalues");
        Log.d(LOG_TAG, transValues.get("WordsFrom").toString());
        // start database
        dbHelper = new DBHelper(this);
        try{
            db = dbHelper.getWritableDatabase();
            db.insert("FAVORITE_WORDS", null, transValues);
            Log.d(LOG_TAG, "after insert");
        } catch (SQLiteException exc) {
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    public void onClick(View v) {

    }

    class DBHelper extends SQLiteOpenHelper {
        final static int DB_VER = 1;
        final static String DB_NAME = "translateYaDB";
        final String TABLE_NAME = "FAVORITE_WORDS";
        final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS "+TABLE_NAME+
                " (_id INTEGER PRIMARY KEY, "+
                "LangDir TEXT, " +
                "WordsFrom TEXT , " +
                "WordsTo TEXT)";
        final String DROP_TABLE = "DROP TABLE IF EXISTS "+TABLE_NAME;
//        final String DATA_FILE_NAME = "data.txt";
        Context context;

        public DBHelper(Context context) {
            super(context, DB_NAME, null, DB_VER);
            this.context = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d(LOG_TAG, "create db "+CREATE_TABLE);
            db.execSQL(CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
