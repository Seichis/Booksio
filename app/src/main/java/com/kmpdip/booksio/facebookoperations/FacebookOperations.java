package com.kmpdip.booksio.facebookoperations;

import com.kmpdip.booksio.data.structure.FacebookUserData;
import com.parse.ParseUser;

import java.util.Random;

/**
 * Created by User1 on 21/11/2015.
 */
public class FacebookOperations {
    private static FacebookOperations facebookOperations =new FacebookOperations();
    private static ParseUser currentUser;
    private static Random random;
    private FacebookOperations(){

    }

    public static FacebookOperations getInstance(ParseUser user){
        currentUser=user;
        random=new Random();
        return facebookOperations;
    }


    public FacebookUserData getFbUserWithData() {
        FacebookUserData.FacebookUserDataBuilder fbBuilder = new FacebookUserData.FacebookUserDataBuilder();
        return (FacebookUserData)fbBuilder.currentUser(currentUser)
                .history((float) (random.nextDouble() * random.nextDouble()))
                .religion((float) (random.nextDouble() * random.nextDouble()))
                .practical((float) (random.nextDouble() * random.nextDouble()))
                .math_science((float) (random.nextDouble() * random.nextDouble()))
                .arts_sports((float) (random.nextDouble() * random.nextDouble()))
                .gender((float) (random.nextInt(2)))
                .number_loans((float) (random.nextInt(1000)))
                .geography((float) (random.nextDouble() * random.nextDouble()))
                .literature((float) (random.nextDouble() * random.nextDouble()))
                .social_science((float) (random.nextDouble() * random.nextDouble()))
                .science_research((float) (random.nextDouble() * random.nextDouble()))
                .age((float) (random.nextInt(70)))
                .build();
    }


}
