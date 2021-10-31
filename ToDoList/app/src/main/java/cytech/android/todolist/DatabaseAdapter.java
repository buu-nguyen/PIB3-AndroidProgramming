package cytech.android.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseAdapter {

    DatabaseHelper helper;
    SQLiteDatabase db;
    Context context;

    public DatabaseAdapter(Context context) {
        helper = new DatabaseHelper(context);
        db = helper.getWritableDatabase();
        this.context = context;
    }

    public Cursor reloadCursor(){
        String[] columns = {DatabaseHelper.KEY_ID, DatabaseHelper.KEY_NAME};

        return db.query(DatabaseHelper.TABLE_NAME, columns, null, null, null, null, null, null);
    }

    public SimpleCursorAdapter populateListViewFromDB(){
        String[] columns = {DatabaseHelper.KEY_ID, DatabaseHelper.KEY_NAME};
        Cursor cursor = db.query(DatabaseHelper.TABLE_NAME, columns,null, null,null, null, null, null);
        String[] fromFieldNames = new String[]{
                DatabaseHelper.KEY_ID, DatabaseHelper.KEY_NAME
        };
        int[] toViewIDs = new int[]{R.id.item_id, R.id.item_name};
        return new SimpleCursorAdapter(
                context,
                R.layout.single_item,
                cursor,
                fromFieldNames,
                toViewIDs
        );
    }

    public boolean create(String data) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.KEY_NAME, data);
        boolean createSuccessful = db.insert(DatabaseHelper.TABLE_NAME, null, values) > 0;
        db.close();

        return createSuccessful;
    }

    public boolean delete(int id) {
        boolean deleteSuccessful;
        String[] whereArgs = {""+id};
        deleteSuccessful = db.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper.KEY_ID + "=?", whereArgs) > 0;
        db.close();

        return deleteSuccessful;
    }

    public static class DatabaseHelper extends SQLiteOpenHelper {

        private static final String DATABASE_NAME = "mydb.db";
        private static final String TABLE_NAME = "Tasks";
        // When you do change the structure of the database change the version number from 1 to 2
        private static final int DATABASE_VERSION = 1;
        private static final String KEY_ID = "_id";
        private static final String KEY_NAME="name";
        private static final String CREATE_TABLE = "create table "+ TABLE_NAME+
                " ("+ KEY_ID+" integer primary key autoincrement, "+ KEY_NAME + " text)";
        private static final String DROP_TABLE = "drop table if exists "+ TABLE_NAME;
        private final Context context;

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.context = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try{
                db.execSQL(CREATE_TABLE);
                Toast.makeText(context, "onCreate called", Toast.LENGTH_SHORT).show();
            }catch (SQLException e){
                Toast.makeText(context, ""+e, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try{
                Toast.makeText(context, "onUpgrade called", Toast.LENGTH_SHORT).show();
                db.execSQL(DROP_TABLE);
                onCreate(db);
            }catch (SQLException e){
                Toast.makeText(context, ""+e, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
