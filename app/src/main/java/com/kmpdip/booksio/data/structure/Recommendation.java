package com.kmpdip.booksio.data.structure;


import android.graphics.Bitmap;

public class Recommendation extends Book<Recommendation> {
    protected Recommendation(RecommendationBuilder builder) {
        super(builder);
    }

    public static class RecommendationBuilder extends AbstractBookBuilder<RecommendationBuilder>{

        @Override
        protected RecommendationBuilder getThis() {
            return null;
        }

        @Override
        public RecommendationBuilder title(String s) {
            title=s;
            return this;
        }

        @Override
        public RecommendationBuilder userCategory(String s) {
            userCategory=s;
            return this;
        }

        @Override
        public RecommendationBuilder genre(String s) {
            genre=s;
            return this;
        }

        @Override
        public RecommendationBuilder criterion(String s) {
            criterion=s;
            return this;
        }

        @Override
        public RecommendationBuilder ID(String s) {
            ID=s;
            return this;
        }

        @Override
        public RecommendationBuilder description(String s) {
            description=s;
            return this;
        }

        @Override
        public RecommendationBuilder author(String s) {
            author=s;
            return this;
        }

        @Override
        public RecommendationBuilder date(String s) {
            date=s;
            return this;
        }

        @Override
        public RecommendationBuilder image(Bitmap s) {
            image=s;
            return this;
        }

        @Override
        public RecommendationBuilder friendsNumber(int i) {
            friendsNumber=i;
            return this;
        }

        @Override
        public RecommendationBuilder rating(float i) {
            return null;
        }

        @Override
        public RecommendationBuilder status(int s){
            status = s;
            return this;
        }

        @Override
        public Recommendation build() {
            return new Recommendation(this);
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
        return this.criterion;
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
        return this.userCategory;
    }

    @Override
    public Bitmap getImage() {
        return this.image;
    }

    @Override
    public int getFriendsNumber() {
        return this.friendsNumber;
    }

    @Override
    public float getRating() {
        return 0;
    }

    @Override
    public int getStatus() {
        return this.status;
    }
}

