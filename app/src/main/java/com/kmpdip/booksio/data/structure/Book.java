package com.kmpdip.booksio.data.structure;

import android.graphics.Bitmap;

public abstract class Book<B extends Book<B>> {

    protected final String title;
    protected final String description;
    protected final String ID;
    protected final String author;
    protected final String criterion;
    protected final String genre;
    protected final String date;
    protected final String userCategory;
    protected final Bitmap image;
    protected final float rating;
    protected final int status;

    protected final int friendsNumber;


    public abstract static class AbstractBookBuilder<T extends AbstractBookBuilder<T>> {
        protected abstract T getThis();

        String title;
        String description;
        String ID;
        String author;
        String criterion;
        String genre;
        String date;
        String userCategory;
        int friendsNumber;
        Bitmap image;
        float rating;
        int status;

        public abstract T title(String s);

        public abstract T userCategory(String s);

        public abstract T genre(String s);

        public abstract T criterion(String s);

        public abstract T ID(String s);

        public abstract T description(String s);

        public abstract T author(String s);

        public abstract T date(String s);

        public abstract T image(Bitmap s);

        public abstract T friendsNumber(int i);

        public abstract T rating(float i);

        public abstract T status(int s);

        public abstract Book build();
    }


    protected Book(AbstractBookBuilder builder) {

        title = builder.title;
        description = builder.description;
        ID = builder.ID;
        author = builder.author;
        criterion = builder.criterion;
        genre = builder.genre;
        date = builder.date;
        userCategory = builder.userCategory;
        image = builder.image;
        friendsNumber = builder.friendsNumber;
        rating = builder.rating;
        status = builder.status;
    }


    public abstract String getTitle();

    public abstract String getDescription();

    public abstract String getID();

    public abstract String getAuthor();

    public abstract String getCriterion();

    public abstract String getGenre();

    public abstract String getDate();

    public abstract String getUserCategory();

    public abstract Bitmap getImage();

    public abstract int getFriendsNumber();

    public abstract float getRating();

    public abstract int getStatus();
}
