package com.kmpdip.booksio.parseoperations;

import com.kmpdip.booksio.data.structure.FacebookUserData;
import com.kmpdip.booksio.data.structure.UserToClassify;
import com.parse.ParseUser;

/**
 * Created by User1 on 21/11/2015.
 */
public class ParseOperations {
    private static ParseOperations parseOperations=new ParseOperations();
    private static ParseUser currentUser;
    private ParseOperations(){
    }

    public static ParseOperations getInstance(ParseUser user){
        currentUser=user;
        return parseOperations;
    }

    public void updateParseUserOnline(FacebookUserData fbUser) {
        currentUser.put("age",fbUser.getAge());
        currentUser.put("arts_sports", fbUser.getArts_sports());
        currentUser.put("gender",fbUser.getGender());
        currentUser.put("geography", fbUser.getGeography());
        currentUser.put("literature",fbUser.getLiterature());
        currentUser.put("math_science", fbUser.getMath_science());
        currentUser.put("practical",fbUser.getPractical());
        currentUser.put("religion",fbUser.getReligion());
        currentUser.put("number_loans", fbUser.getNumber_loans());
        currentUser.put("social_science", fbUser.getSocial_science());
        currentUser.put("science_research", fbUser.getScience_research());
        currentUser.put("history",fbUser.getHistory());
        currentUser.saveInBackground();

    }

    public UserToClassify getUserFeatures() {

        UserToClassify.UserToClassifyBuilder userBuilder=new UserToClassify.UserToClassifyBuilder();
        return userBuilder.age(Float.parseFloat(String.valueOf(currentUser.getDouble("age"))))
                .arts_sports( Float.parseFloat(String.valueOf(currentUser.getDouble("arts_sports"))))
                .gender(Float.parseFloat(String.valueOf(currentUser.getDouble("gender"))))
                .geography(Float.parseFloat(String.valueOf(currentUser.getDouble("geography"))))
                .literature(Float.parseFloat(String.valueOf(currentUser.getDouble("literature"))))
                .math_science(Float.parseFloat(String.valueOf(currentUser.getDouble("math_science"))))
                .practical(Float.parseFloat(String.valueOf(currentUser.getDouble("practical"))))
                .religion(Float.parseFloat(String.valueOf(currentUser.getDouble("religion"))))
                .number_loans(Float.parseFloat(String.valueOf(currentUser.getDouble("number_loans"))))
                .social_science(Float.parseFloat(String.valueOf(currentUser.getDouble("social_science"))))
                .science_research(Float.parseFloat(String.valueOf(currentUser.getDouble("science_research"))))
                .history(Float.parseFloat(String.valueOf(currentUser.getDouble("history"))))
                .build();
    }
}
