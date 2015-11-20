package com.kmpdip.booksio.data.structure;

import com.parse.ParseUser;

public class UserToClassify{
    private final float age;
    private final float number_loans;
    private final float science_research;
    private final float religion;
    private final float social_science;
    private final float geography;
    private final float math_science;
    private final float practical;
    private final float arts_sports;
    private final float literature;
    private final float history;
    private final float gender;

    protected UserToClassify(UserToClassifyBuilder builder) {

        age = builder.age;
        number_loans = builder.number_loans;
        science_research = builder.science_research;
        religion = builder.religion;
        social_science = builder.social_science;
        geography = builder.geography;
        math_science = builder.math_science;
        practical = builder.practical;
        arts_sports = builder.arts_sports;
        literature = builder.literature;
        history = builder.history;
        gender = builder.gender;
    }

    public float getAge() {
        return age;
    }

    public float getNumber_loans() {
        return number_loans;
    }

    public float getScience_research() {
        return science_research;
    }

    public float getReligion() {
        return religion;
    }

    public float getSocial_science() {
        return social_science;
    }

    public float getGeography() {
        return geography;
    }

    public float getMath_science() {
        return math_science;
    }

    public float getPractical() {
        return practical;
    }

    public float getArts_sports() {
        return arts_sports;
    }

    public float getLiterature() {
        return literature;
    }

    public float getHistory() {
        return history;
    }

    public float getGender() {
        return gender;
    }

    public static class UserToClassifyBuilder {
        float age;
        float number_loans;
        float science_research;
        float religion;
        float social_science;
        float geography;
        float math_science;
        float practical;
        float arts_sports;
        float literature;
        float history;
        float gender;

        public UserToClassifyBuilder() {
        }

        public UserToClassifyBuilder age(float s) {
            age = s;
            return this;
        }

        public UserToClassifyBuilder number_loans(float s) {
            number_loans = s;
            return this;
        }

        public UserToClassifyBuilder science_research(float s) {
            science_research = s;
            return this;
        }

        public UserToClassifyBuilder religion(float s) {
            religion = s;
            return this;
        }

        public UserToClassifyBuilder social_science(float s) {
            social_science = s;
            return this;
        }

        public UserToClassifyBuilder geography(float s) {
            geography = s;
            return this;
        }

        public UserToClassifyBuilder math_science(float s) {
            math_science = s;
            return this;
        }

        public UserToClassifyBuilder practical(float s) {
            practical = s;
            return this;
        }

        public UserToClassifyBuilder arts_sports(float s) {
            arts_sports = s;
            return this;
        }

        public UserToClassifyBuilder literature(float s) {
            literature = s;
            return this;
        }

        public UserToClassifyBuilder history(float s) {
            history = s;
            return this;
        }

        public UserToClassifyBuilder gender(float s) {
            gender = s;
            return this;
        }

        public UserToClassify build() {
            return new UserToClassify(this);
        }
    }
}
