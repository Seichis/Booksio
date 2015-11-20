package com.kmpdip.booksio.data.structure;

import com.parse.ParseUser;

import java.lang.reflect.Array;

/**
 * Created by User1 on 20/11/2015.
 */
public class FacebookUserData extends UserToClassify{
    private final ParseUser currentUser;

    public ParseUser getCurrentUser() {
        return currentUser;
    }

    public Array getWantsToRead() {
        return wantsToRead;
    }

    public Array getLikes() {
        return likes;
    }

    public Array getDislikes() {
        return dislikes;
    }

    private final Array wantsToRead;
    private final Array likes;
    private final Array dislikes;

    private FacebookUserData(FacebookUserDataBuilder builder) {
        super(builder);
        currentUser=builder.currentUser;
        likes=builder.likes;
        dislikes=builder.dislikes;
        wantsToRead=builder.wantsToRead;
    }

    public static class FacebookUserDataBuilder extends UserToClassifyBuilder{

        ParseUser currentUser;
        Array wantsToRead;
        Array likes;
        Array dislikes;

        public FacebookUserDataBuilder currentUser(ParseUser s){
            this.currentUser=s;
            return this;
        }
         public FacebookUserDataBuilder wantsToRead(Array s){
            this.wantsToRead=s;
            return this;
        }
         public FacebookUserDataBuilder likes(Array s){
            this.likes=s;
            return this;
        }
         public FacebookUserDataBuilder dislikes(Array s){
            this.dislikes=s;
            return this;
        }
        @Override
        public FacebookUserData build() {
            return new FacebookUserData(this);
        }
    }
}
