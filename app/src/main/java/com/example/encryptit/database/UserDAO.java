package com.example.encryptit.database;

import android.content.ContentValues;
import android.content.Context;

import com.example.encryptit.model.User;

import net.sqlcipher.Cursor;
import net.sqlcipher.SQLException;
import net.sqlcipher.database.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private static final String TABLE_NAME = "users";
    private static final String COLUMN_ID_1 = "id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_HINT = "hint";
    private SQLiteDatabase db;
    private final DatabaseHelper dbHelper;

    public UserDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void addUser(User user) {
        open();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID_1, 1);
        values.put(COLUMN_USERNAME, user.getUserName());
        values.put(COLUMN_PASSWORD, user.getPassWord());
        values.put(COLUMN_HINT, user.getHint());
        db.insert(TABLE_NAME, null, values);
        close();
    }

    public int updateUser(long id, User user) {
        open();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, user.getUserName());
        values.put(COLUMN_PASSWORD, user.getPassWord());
        values.put(COLUMN_HINT, user.getHint());
        int rowsUpdated = db.update(TABLE_NAME, values, COLUMN_ID_1 + "=?", new String[]{String.valueOf(id)});
        close();
        return rowsUpdated;
    }

    public User getUser(long id) {
        open();
        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_ID_1, COLUMN_USERNAME, COLUMN_PASSWORD, COLUMN_HINT}, COLUMN_ID_1 + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null) cursor.moveToFirst();
        User user = new User(cursor.getString(1), cursor.getString(2), cursor.getString(3));
        cursor.close();
        close();
        return user;
    }

    public List<User> getAllUsers() {
        open();
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setUserName(cursor.getString(1));
                user.setPassWord(cursor.getString(2));
                user.setHint(cursor.getString(3));
                users.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        close();
        return users;
    }
}
