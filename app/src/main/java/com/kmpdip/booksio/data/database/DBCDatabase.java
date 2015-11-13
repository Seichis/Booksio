package com.kmpdip.booksio.data.database;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ioanna on 11/8/2015.
 */
public class DBCDatabase extends SQLiteAssetHelper {
    private static final String DATABASE_NAME = "dbcdatabase.db";
    private static final int DATABASE_VERSION = 2;

    public DBCDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public ArrayList getBooks(String userClass, List<String> hasRead) {
        String hasReadString = "";
        for (String s : hasRead) {
            hasReadString += '"' + s + '"' + ",";
        }
        hasReadString = hasReadString.substring(0, hasReadString.length() - 1);
        Cursor c = getReadableDatabase().
                rawQuery("select 0 _id, book_id from Books where user_class LIKE '%" + userClass + "%'" +
                        " AND book_id NOT IN (" + hasReadString + ")", null);
        Log.i("cursor", "  " + c.getCount());
        int counter = 0;
        ArrayList bookList = new ArrayList();
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    bookList.add(c.getString(c.getColumnIndex("book_id"))); // "Title" is the field name(column) of the Table
                    counter++;
                } while (c.moveToNext() && counter < 20);
            }
        }
        return bookList;
    }

}
