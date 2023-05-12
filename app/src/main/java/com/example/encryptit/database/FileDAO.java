package com.example.encryptit.database;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.example.encryptit.model.EncryptFile;

import net.sqlcipher.Cursor;
import net.sqlcipher.SQLException;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteStatement;

import java.util.ArrayList;
import java.util.List;


public class FileDAO {
    private final DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    public FileDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public int getMaxId() {
        open();
        int maxId = 0;
        String query = "SELECT MAX(_id) FROM " + DatabaseHelper.TABLE_FILE;
        android.database.Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            maxId = cursor.getInt(0);
        }
        cursor.close();
        close();
        return maxId;
    }

    public void addFile(EncryptFile encryptFile) {
        open();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_FILE_PATH, encryptFile.getFilePath());
        values.put(DatabaseHelper.COLUMN_FILE_NAME_AND_EXTENSION, encryptFile.getFileNameAndExtension());
        values.put(DatabaseHelper.COLUMN_FILE_NAME, encryptFile.getFileName());
        values.put(DatabaseHelper.COLUMN_FILE_EXTENSION, encryptFile.getFileExtension());
        values.put(DatabaseHelper.COLUMN_FILE_LOCATION, encryptFile.getFileLocation());
        values.put(DatabaseHelper.COLUMN_ALIAS, encryptFile.getAlias());
        values.put(DatabaseHelper.COLUMN_IS_IMAGE, encryptFile.getImage() ? 1 : 0);
        values.put(DatabaseHelper.COLUMN_EMAIL, encryptFile.getEmail());

        String query = "INSERT INTO " + DatabaseHelper.TABLE_FILE + " (" +
                DatabaseHelper.COLUMN_FILE_PATH + "," +
                DatabaseHelper.COLUMN_FILE_NAME_AND_EXTENSION + "," +
                DatabaseHelper.COLUMN_FILE_NAME + "," +
                DatabaseHelper.COLUMN_FILE_EXTENSION + "," +
                DatabaseHelper.COLUMN_FILE_LOCATION + "," +
                DatabaseHelper.COLUMN_ALIAS + "," +
                DatabaseHelper.COLUMN_IS_IMAGE + "," +
                DatabaseHelper.COLUMN_EMAIL + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        SQLiteStatement statement = db.compileStatement(query);
        statement.bindString(1, encryptFile.getFilePath());
        statement.bindString(2, encryptFile.getFileNameAndExtension());
        statement.bindString(3, encryptFile.getFileName());
        statement.bindString(4, encryptFile.getFileExtension());
        statement.bindString(5, encryptFile.getFileLocation());
        statement.bindString(6, encryptFile.getAlias());
        statement.bindLong(7, encryptFile.getImage() ? 1 : 0);
        statement.bindString(8, encryptFile.getEmail());

        statement.executeInsert();

        Log.d("Database", "add ok " + encryptFile.getEmail());

        close();
    }


    public void deleteFile(EncryptFile encryptFile) {
        open();
        String whereClause = DatabaseHelper.COLUMN_FILE_PATH + " = ?";
        String[] whereArgs = new String[]{encryptFile.getFilePath()};
        db.delete(DatabaseHelper.TABLE_FILE, whereClause, whereArgs);
        close();
    }


    public List<EncryptFile> getAllImages(String email) {
        open();
        List<EncryptFile> encryptFiles = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + DatabaseHelper.TABLE_FILE + " WHERE is_image = ? AND email = ?;";
        String[] selectionArgs = new String[]{"1", email};
        Cursor cursor = db.rawQuery(selectQuery, selectionArgs);

        if (cursor.moveToFirst()) {
            do {
                EncryptFile encryptFile = new EncryptFile();
                encryptFile.setFilePath(cursor.getString(1));
                encryptFile.setFileNameAndExtension(cursor.getString(2));
                encryptFile.setFileName(cursor.getString(3));
                encryptFile.setFileExtension(cursor.getString(4));
                encryptFile.setFileLocation(cursor.getString(5));
                encryptFile.setAlias(cursor.getString(6));
                encryptFile.setImage(cursor.getInt(7) == 1);
                encryptFile.setEmail(cursor.getString(8));
                encryptFiles.add(encryptFile);

                Log.d("Database", "read ok " + encryptFile.getEmail());

            } while (cursor.moveToNext());
        }
        cursor.close();
        close();
        return encryptFiles;
    }


    public List<EncryptFile> getAllFiles(String email) {
        open();
        List<EncryptFile> encryptFiles = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_FILE + " WHERE is_image = 0 AND email = ?;";
        String[] selectionArgs = {email};

        Cursor cursor = db.rawQuery(selectQuery, selectionArgs);

        if (cursor.moveToFirst()) {
            do {
                EncryptFile encryptFile = new EncryptFile();
                encryptFile.setFilePath(cursor.getString(1));
                encryptFile.setFileNameAndExtension(cursor.getString(2));
                encryptFile.setFileName(cursor.getString(3));
                encryptFile.setFileExtension(cursor.getString(4));
                encryptFile.setFileLocation(cursor.getString(5));
                encryptFile.setAlias(cursor.getString(6));
                encryptFile.setImage(cursor.getInt(7) == 0);
                encryptFile.setEmail(cursor.getString(8));
                encryptFiles.add(encryptFile);

                Log.d("Database", "read ok " + encryptFile.getEmail());

            } while (cursor.moveToNext());
        }
        cursor.close();
        close();
        return encryptFiles;
    }

}
