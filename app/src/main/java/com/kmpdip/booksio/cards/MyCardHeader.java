package com.kmpdip.booksio.cards;

import android.content.Context;
import android.support.percent.PercentRelativeLayout;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.kmpdip.booksio.MainActivity;
import com.kmpdip.booksio.R;
import com.kmpdip.booksio.data.database.DBCDatabase;
import com.kmpdip.booksio.data.structure.Book;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;

/**
 * Created by User1 on 15/11/2015.
 */
public class MyCardHeader extends CardHeader implements ICard{

    Book book;
    Context context;


    public MyCardHeader(Context context, Book book) {
        super(context);
        Log.i("BookClass",book.getClass().getSimpleName());
        if (book.getClass().getSimpleName().equals("Recommendation")){
            Log.i("BookClass","inside if");
            super.setInnerLayout(R.layout.card_header_rec);
        }else if (book.getClass().getSimpleName().equals("LibraryBook")){
            super.setInnerLayout(R.layout.card_header_library);
        }
        this.context = context;
        this.book = book;
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

        if (view == null) return;

        if (book.getClass().getSimpleName().equals("Recommendation")){
            setRecommendationCardView(view);
        }else if (book.getClass().getSimpleName().equals("LibraryBook")){
            setLibraryCardView(view);
        }
    }


    @Override
    public void setRecommendationCardView(final View view) {
        ImageView cover = (ImageView) view.findViewById(R.id.cover_img);
        Button likeButton = (Button) view.findViewById(R.id.like_button);
        Button dislikeButton = (Button) view.findViewById(R.id.dislike_button);
        RatingBar ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
        TextView titleTextView = (TextView) view.findViewById(R.id.title);
        TextView authorTextView = (TextView) view.findViewById(R.id.author);
        TextView numberFriendsTextView = (TextView) view.findViewById(R.id.friends_number);
        TextView genreTextView = (TextView) view.findViewById(R.id.genres);

        titleTextView.setText(String.valueOf(this.book.getTitle()));
        authorTextView.setText(String.valueOf("By : " + this.book.getAuthor()));
        numberFriendsTextView.setText(String.valueOf(this.book.getFriendsNumber()));
        genreTextView.setText(String.valueOf("Genres : " + this.book.getGenre()));
        cover.setImageBitmap(book.getImage());
        ratingBar.setStepSize((float) 1.0);
        //Set value in text views
        final DBCDatabase db = new DBCDatabase(context);
        likeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View buttonView) {
                db.updateLibrary(book, 1);
                db.close();
                animateCard(view);

            }

        });
        dislikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View buttonView) {
                db.updateLibrary(book, 0);
                db.close();
                animateCard(view);
            }
        });

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                db.addRating(book, rating);
                db.close();
                animateCard(view);
            }
        });

    }

    private void animateCard(View view){
//        // Prepare the View for the animation
//        view.setVisibility(View.VISIBLE);
//        view.setAlpha(0.0f);
//
//// Start the animation
//        view.animate()
//                .translationY(view.getHeight())
//                .alpha(1.0f);
        MainActivity.getInstance().removeCardFromList(this.getParentCard());
    }



    @Override
    public void setLibraryCardView(View view) {

    }
}

