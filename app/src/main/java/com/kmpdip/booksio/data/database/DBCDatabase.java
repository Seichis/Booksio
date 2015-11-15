package com.kmpdip.booksio.data.database;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.util.Log;

import com.kmpdip.booksio.data.structure.Book;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        /////////////////
        //this is for debugging
        Cursor cur = getReadableDatabase().rawQuery("select 0 _id, image from Library", null);
        if (cur != null) {
            if (cur.moveToFirst()) {
                do {
                    byte[] image = cur.getBlob(cur.getColumnIndex("image"));
//                    String st = cur.getString(cur.getColumnIndex("image"));
                    if (image != null) {
                        Log.i("librarychevk", String.valueOf(image));
                    }
                } while (cur.moveToNext());
            }
        }
        return bookList;
    }

    public void updateLibrary(Book book, int status){
        //Check for existence
        String sqlCheck = "select 0 _id, book_id from Library where book_id ='"+book.getID()+"'";
        Cursor c = getReadableDatabase().rawQuery(sqlCheck, null);
        if (c.getCount() == 0) {
            byte[] image = convertImage(book.getImage());
            String sqlInsert = "INSERT INTO Library VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ? )";
            getWritableDatabase().execSQL(sqlInsert, new Object[]{book.getID(), book.getTitle(),
                    book.getAuthor(), book.getDescription(), book.getDate(), book.getGenre(), image, status, null});
            Log.i("saved", "saved to db "+book.getTitle());

        }else {
            Log.i("saved", "book already exists");
        }
    }
    public byte[] convertImage(Bitmap bitmap){
        byte[] image;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        if (bitmap != null) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
            image = stream.toByteArray();
        } else {
            image = null;
        }
        return  image;
    }
    public void addRating(final Book book, final float rating){
        //Check for existence
        String sqlCheck = "select 0 _id, book_id from Library where book_id ='"+book.getID()+"'";
        Cursor c = getReadableDatabase().rawQuery(sqlCheck, null);
        byte[] image = convertImage(book.getImage());
        if (c.getCount() == 0) {
            String sqlInsert = "INSERT INTO Library VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ? )";
            getWritableDatabase().execSQL(sqlInsert, new Object[]{book.getID(), book.getTitle(),
                    book.getAuthor(), book.getDescription(), book.getDate(), book.getGenre(), image, 2, (int)rating});
            Log.i("saved", "saved to db "+book.getTitle());
        }else {
            String sqlUpdate = "UPDATE Library SET rating = ? AND status = 2 WHERE book_id = ?";
            getWritableDatabase().execSQL(sqlUpdate, new Object[] {(int)rating, book.getID()});
            Log.i("saved", "updated object "+book.getTitle());
        }
    }
}
