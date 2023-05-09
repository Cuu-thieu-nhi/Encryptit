package com.example.encryptit.database;

import android.content.ContentValues;
import android.content.Context;

import com.example.encryptit.model.EncryptFile;

import net.sqlcipher.Cursor;
import net.sqlcipher.SQLException;
import net.sqlcipher.database.SQLiteDatabase;

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
        android.database.Cursor cursor = db.rawQuery("SELECT MAX(_id) FROM " + DatabaseHelper.TABLE_FILE, null);
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

        db.insert(DatabaseHelper.TABLE_FILE, null, values);
        close();

    }

    public void deleteFile(EncryptFile encryptFile) {
        open();
        db.delete(DatabaseHelper.TABLE_FILE, DatabaseHelper.COLUMN_FILE_PATH + " = ?", new String[]{String.valueOf(encryptFile.getFilePath())});
        close();
    }

    public List<EncryptFile> getAllImages(String email) {
        open();
        List<EncryptFile> encryptFiles = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + DatabaseHelper.TABLE_FILE + " WHERE is_image = 1 AND email = '" + email + "';";

        Cursor cursor = db.rawQuery(selectQuery, null);

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
            } while (cursor.moveToNext());
        }
        cursor.close();
        close();
        return encryptFiles;
    }

    public List<EncryptFile> getAllFiles(String email) {
        open();
        List<EncryptFile> encryptFiles = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + DatabaseHelper.TABLE_FILE + " WHERE is_image = 0 AND email = '" + email + "';";

        Cursor cursor = db.rawQuery(selectQuery, null);

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
            } while (cursor.moveToNext());
        }
        cursor.close();
        close();
        return encryptFiles;
    }

}
