package com.kmpdip.booksio.data.structure;

import android.graphics.Bitmap;

/**
 * Created by Ioanna on 11/16/2015.
 */
public class LibraryBook extends Book {
    protected final int status;
    protected final int rating;
    protected LibraryBook(LibraryBookBuilder builder) {
        super(builder);
        status = builder.status;
        rating = builder.rating;
    }

    public static class LibraryBookBuilder extends AbstractBookBuilder<LibraryBookBuilder>{
        int status;
        int rating;

        public LibraryBookBuilder status(int s){
            status = s;
            return this;
        }

        public LibraryBookBuilder rating(int s){
            rating = s;
            return this;
        }

        @Override
        protected LibraryBookBuilder getThis() {
            return null;
        }

        @Override
        public LibraryBookBuilder title(String s) {
            title = s;
            return this;
        }

        @Override
        public LibraryBookBuilder userCategory(String s) {
            return null;
        }

        @Override
        public LibraryBookBuilder genre(String s) {
            genre = s;
            return this;
        }

        @Override
        public LibraryBookBuilder criterion(String s) {
            return null;
        }

        @Override
        public LibraryBookBuilder ID(String s) {
            ID = s;
            return this;
        }

        @Override
        public LibraryBookBuilder description(String s) {
            description = s;
            return this;
        }

        @Override
        public LibraryBookBuilder author(String s) {
            author = s;
            return this;
        }

        @Override
        public LibraryBookBuilder date(String s) {
            date = s;

            return this;
        }

        @Override
        public LibraryBookBuilder image(Bitmap s) {
            image = s;
            return this;
        }

        @Override
        public LibraryBookBuilder friendsNumber(int i) {
            return null;
        }

        @Override
        public Book build() {
            return new LibraryBook(this);
        }
    }
    @Override
    public String getTitle() {

        return this.title;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public String getID() {
        return this.ID;
    }

    @Override
    public String getAuthor() {
        return this.author;
    }

    @Override
    public String getCriterion() {
        return null;
    }

    @Override
    public String getGenre() {
        return this.genre;
    }

    @Override
    public String getDate() {
        return this.date;
    }

    @Override
    public String getUserCategory() {
        return null;
    }

    @Override
    public Bitmap getImage() {
        return this.image;
    }

    @Override
    public int getFriendsNumber() {
        return 0;
    }
}
