package com.kmpdip.booksio.data.database;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.kmpdip.booksio.data.structure.Book;
import com.kmpdip.booksio.data.structure.LibraryBook;
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
        int counter = 0;
        ArrayList bookList = new ArrayList();
        boolean inLibrary;
        String hasReadString = "";
        for (String s : hasRead) {
            hasReadString += '"' + s + '"' + ",";
        }
        hasReadString = hasReadString.substring(0, hasReadString.length() - 1);
        Cursor c = getReadableDatabase().
                rawQuery("select 0 _id, book_id from Books where user_class LIKE '%" + userClass + "%'" +
                        " AND book_id NOT IN (" + hasReadString + ")", null);
        Log.i("cursor", "  " + c.getCount());

        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    String book_id = c.getString(c.getColumnIndex("book_id"));
                    inLibrary = isInLibrary(book_id);
                    if (inLibrary == false) {
                        bookList.add(book_id); // "Title" is the field name(column) of the Table
                        counter++;
                    }
                } while (c.moveToNext() && counter < 5);
            }
        }
        /////////////////
        //this is for debugging
        Cursor cur = getReadableDatabase().rawQuery("select 0 _id, * from Library", null);
        if (cur != null) {
            if (cur.moveToFirst()) {
                do {
                    byte[] image = cur.getBlob(cur.getColumnIndex("image"));
                    String st = cur.getString(cur.getColumnIndex("book_id"));

                        Log.i("librarychevk", st);
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

    public boolean isInLibrary (String book_id){
        String sqlCheck = "select 0 _id, book_id from Library where book_id ='"+book_id+"'";
        Cursor cursor = getReadableDatabase().rawQuery(sqlCheck, null);
        if (cursor.getCount() == 0) {
            return false;
        }else {
            return true;
        }
    }

    /**
     * Method for reading books from Library table based on book status
     * @param status 0 for dislike, 1 for like, 2 for has read(has rating)
     * @return
     */
    public List<LibraryBook> getBooksDetails(int status) {
        Bitmap bitmapImage = null;
        Cursor cur = getReadableDatabase().rawQuery("select 0 _id, * from Library where status=?", new String[] {String.valueOf(status)});
        List<LibraryBook> booksDetails = new ArrayList<>();
        LibraryBook.LibraryBookBuilder bookBuilder = new LibraryBook.LibraryBookBuilder();
        if (cur != null) {
            if (cur.moveToFirst()) {
                do {
                    byte[] image = cur.getBlob(cur.getColumnIndex("image"));
                    if (image != null) {
                        bitmapImage = BitmapFactory.decodeByteArray(image, 0, image.length);
                    }
                    LibraryBook book = bookBuilder.ID(cur.getString(cur.getColumnIndex("book_id")))
                            .title(cur.getString(cur.getColumnIndex("title")))
                            .author(cur.getString(cur.getColumnIndex("author")))
                            .description(cur.getString(cur.getColumnIndex("abstract")))
                            .date(cur.getString(cur.getColumnIndex("date")))
                            .genre(cur.getString(cur.getColumnIndex("subject")))
                            .image(bitmapImage)
                            .status(cur.getInt(cur.getColumnIndex("status")))
                            .rating(cur.getInt(cur.getColumnIndex("rating")))
                            .build();

                    booksDetails.add(book);
                } while (cur.moveToNext());
            }
        }
        return booksDetails;
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

}
