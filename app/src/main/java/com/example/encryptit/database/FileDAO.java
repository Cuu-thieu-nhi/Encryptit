package com.example.encryptit.database;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

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

        db.insert(DatabaseHelper.TABLE_FILE, null, values);
        close();

    }

    public void deleteFile(EncryptFile encryptFile) {
        open();
        db.delete(DatabaseHelper.TABLE_FILE, DatabaseHelper.COLUMN_FILE_PATH + " = ?", new String[]{String.valueOf(encryptFile.getFilePath())});
        close();
    }

    public List<EncryptFile> getAllImages() {
        open();
        List<EncryptFile> encryptFiles = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + DatabaseHelper.TABLE_FILE + " WHERE is_image = 1;";

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
                encryptFiles.add(encryptFile);
            } while (cursor.moveToNext());
        }
        cursor.close();
        close();
        return encryptFiles;
    }

//    public List<EncryptFile> getAllImages() {
//        List<EncryptFile> encryptFiles = new ArrayList<>();
//
//        try {
//            open();
//
//            String[] projection = {
//                    DatabaseHelper.COLUMN_FILE_PATH,
//                    DatabaseHelper.COLUMN_FILE_NAME_AND_EXTENSION,
//                    DatabaseHelper.COLUMN_FILE_NAME,
//                    DatabaseHelper.COLUMN_FILE_EXTENSION,
//                    DatabaseHelper. COLUMN_FILE_LOCATION,
//                    DatabaseHelper.COLUMN_ALIAS,
//                    DatabaseHelper.COLUMN_IS_IMAGE
//            };
//            String selection = DatabaseHelper.COLUMN_IS_IMAGE + "=?";
//            String[] selectionArgs = { "1" };
//            String sortOrder = null;
//
//            Cursor cursor = db.query(
//                    DatabaseHelper.TABLE_FILE,  // table name
//                    projection,                 // columns to return
//                    selection,                  // columns for WHERE clause
//                    selectionArgs,              // values for WHERE clause
//                    null,                       // don't group the rows
//                    null,                       // don't filter by row groups
//                    sortOrder                   // sort order
//            );
//
//            while (cursor.moveToNext()) {
//                EncryptFile encryptFile = new EncryptFile();
//                encryptFile.setFilePath(cursor.getString(1));
//                encryptFile.setFileNameAndExtension(cursor.getString(2));
//                encryptFile.setFileName(cursor.getString(3));
//                encryptFile.setFileExtension(cursor.getString(4));
//                encryptFile.setFileLocation(cursor.getString(5));
//                encryptFile.setAlias(cursor.getString(6));
//                encryptFile.setImage(cursor.getInt(7) == 1);
//                encryptFiles.add(encryptFile);
//            }
//        } catch (SQLException e) {
//            Log.e("FileDAO", "Error getting all files", e);
//        } finally {
//            close();
//        }
//        return encryptFiles;
//    }

    public List<EncryptFile> getAllFiles() {
        List<EncryptFile> encryptFiles = new ArrayList<>();

        try {
            open();

            String[] projection = {DatabaseHelper.COLUMN_FILE_PATH, DatabaseHelper.COLUMN_FILE_NAME_AND_EXTENSION, DatabaseHelper.COLUMN_FILE_NAME, DatabaseHelper.COLUMN_FILE_EXTENSION, DatabaseHelper.COLUMN_FILE_LOCATION, DatabaseHelper.COLUMN_ALIAS, DatabaseHelper.COLUMN_IS_IMAGE};
            String selection = DatabaseHelper.COLUMN_IS_IMAGE + "=?";
            String[] selectionArgs = {"0"};
            String sortOrder = null;

            Cursor cursor = db.query(DatabaseHelper.TABLE_FILE,  // table name
                    projection,                 // columns to return
                    selection,                  // columns for WHERE clause
                    selectionArgs,              // values for WHERE clause
                    null,                       // don't group the rows
                    null,                       // don't filter by row groups
                    sortOrder                   // sort order
            );

            while (cursor.moveToNext()) {
                EncryptFile encryptFile = new EncryptFile();
                encryptFile.setFilePath(cursor.getString(1));
                encryptFile.setFileNameAndExtension(cursor.getString(2));
                encryptFile.setFileName(cursor.getString(3));
                encryptFile.setFileExtension(cursor.getString(4));
                encryptFile.setFileLocation(cursor.getString(5));
                encryptFile.setAlias(cursor.getString(6));
                encryptFile.setImage(cursor.getInt(7) == 0);
                encryptFiles.add(encryptFile);
            }
        } catch (SQLException e) {
            Log.e("FileDAO", "Error getting all files", e);
        } finally {
            close();
        }
        return encryptFiles;
    }


}
