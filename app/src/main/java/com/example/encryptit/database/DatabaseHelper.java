package com.example.encryptit.database;

import android.content.Context;

import com.example.encryptit.cryptography.DatabasePasswordManager;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "user_db";
    public static final String DATABASE_PASSWORD;
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_USER = "users";
    public static final String COLUMN_ID_1 = "id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_HINT = "hint";
    public static final String TABLE_FILE = "file";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_FILE_PATH = "file_path";
    public static final String COLUMN_FILE_NAME_AND_EXTENSION = "file_name_and_extension";
    public static final String COLUMN_FILE_NAME = "file_name";
    public static final String COLUMN_FILE_EXTENSION = "file_extension";
    public static final String COLUMN_FILE_LOCATION = "file_location";
    public static final String COLUMN_ALIAS = "alias";
    public static final String COLUMN_IS_IMAGE = "is_image";

    public static final String COLUMN_EMAIL = "email";

    static {
        try {
            DATABASE_PASSWORD = DatabasePasswordManager.decrypt("XElYF8oK0Vp+++IN1y0wgw==");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        SQLiteDatabase.loadLibs(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query1 = "CREATE TABLE " + TABLE_USER + " (" + COLUMN_ID_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_USERNAME + " TEXT, " + COLUMN_PASSWORD + " TEXT, " + COLUMN_HINT + " TEXT" + ");";
        String query2 = "CREATE TABLE " + TABLE_FILE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_FILE_PATH + " TEXT, " + COLUMN_FILE_NAME_AND_EXTENSION + " TEXT, " + COLUMN_FILE_NAME + " TEXT, " + COLUMN_FILE_EXTENSION + " TEXT, " + COLUMN_FILE_LOCATION + " TEXT, " + COLUMN_ALIAS + " TEXT, " + COLUMN_IS_IMAGE + " INTEGER, " + COLUMN_EMAIL + " TEXT);";
        db.execSQL(query1);
        db.execSQL(query2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query1 = "DROP TABLE IF EXISTS " + TABLE_USER;
        String query2 = "DROP TABLE IF EXISTS " + TABLE_FILE;
        db.execSQL(query1);
        db.execSQL(query2);
        onCreate(db);
    }

    public SQLiteDatabase getWritableDatabase() {
        return getWritableDatabase(DATABASE_PASSWORD);
    }

    public SQLiteDatabase getReadableDatabase() {
        return getReadableDatabase(DATABASE_PASSWORD);
    }
}
