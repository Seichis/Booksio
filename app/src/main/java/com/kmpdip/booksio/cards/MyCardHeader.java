package com.kmpdip.booksio.cards;

import android.content.Context;
import android.content.Intent;
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
import com.kmpdip.booksio.data.structure.LibraryBook;

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
            this.book = book;
        }else if (book.getClass().getSimpleName().equals("LibraryBook")){
            super.setInnerLayout(R.layout.card_header_library);
            this.book =book;
        }
        this.context = context;

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
        final Card rec=this.getParentCard();
        ImageView cover = (ImageView) view.findViewById(R.id.cover_img);
        Button likeButton = (Button) view.findViewById(R.id.like_button);
        Button dislikeButton = (Button) view.findViewById(R.id.dislike_button);
        RatingBar ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
        TextView titleTextView = (TextView) view.findViewById(R.id.title);
        TextView authorTextView = (TextView) view.findViewById(R.id.author);
        TextView numberFriendsTextView = (TextView) view.findViewById(R.id.friends_number);
        TextView genreTextView = (TextView) view.findViewById(R.id.genres);

        titleTextView.setText(String.valueOf(this.book.getTitle()));
        authorTextView.setText(String.valueOf("Af : " + this.book.getAuthor()));
        numberFriendsTextView.setText(String.valueOf(this.book.getFriendsNumber()));
        genreTextView.setText(String.valueOf("Tags : " + this.book.getGenre()));
        if(this.book.getImage()!=null){
            cover.setImageBitmap(this.book.getImage());
        }else{
            cover.setImageDrawable(context.getResources().getDrawable(R.drawable.book2));
        }

        ratingBar.setStepSize((float) 1.0);
        //Set value in text views
        final DBCDatabase db = new DBCDatabase(context);
        likeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View buttonView) {
                db.updateLibrary(book, 1);
                db.close();
                MainActivity.getInstance().removeCardFromList(rec);

            }

        });
        dislikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View buttonView) {
                db.updateLibrary(book, 0);
                db.close();
                MainActivity.getInstance().removeCardFromList(rec);
            }
        });

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                db.addRating(book, rating);
                db.close();
                MainActivity.getInstance().removeCardFromList(rec);
            }
        });

//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                setInnerLayout(R.layout.inner_card_rec);
//            }
//        });
    }


    @Override
    public void setLibraryCardView(View view) {
        //final DBCDatabase db = new DBCDatabase(context);
        ImageView cover = (ImageView) view.findViewById(R.id.cover_img);
        RatingBar ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
        TextView titleTextView = (TextView) view.findViewById(R.id.title);
        TextView authorTextView = (TextView) view.findViewById(R.id.author);
        Button shareButton = (Button) view.findViewById(R.id.cart_button);

        titleTextView.setText(this.book.getTitle());
        authorTextView.setText("Af : " + this.book.getAuthor());
        if(book.getImage()!= null) {
            cover.setImageBitmap(this.book.getImage());
        }else{
            cover.setImageDrawable(context.getResources().getDrawable(R.drawable.book2));
        }
        //ratingBar.setStepSize((float) 1.0);
        ratingBar.setRating(this.book.getRating());
        Log.i("rating", String.valueOf(this.book.getRating()));
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View buttonView) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "Jeg tror, at du vil finde denne interessante: " + book.getTitle() + " af " + book.getAuthor() + ". Tjek den på DBC App!";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "DBC - Tjek denne bog!");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                context.startActivity(Intent.createChooser(sharingIntent, "Del via"));
            }
        });

//        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
//            public void onRatingChanged(RatingBar ratingBar, float rating,
//                                        boolean fromUser) {
//                db.addRating(this.book, rating);
//                db.close();
//            }
//        });
    }


}

