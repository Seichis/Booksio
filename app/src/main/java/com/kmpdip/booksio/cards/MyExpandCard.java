package com.kmpdip.booksio.cards;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kmpdip.booksio.R;
import com.kmpdip.booksio.data.structure.Book;

import it.gmariotti.cardslib.library.internal.CardExpand;


/**
 * Created by User1 on 13/11/2015.
 */

public class MyExpandCard extends CardExpand implements ICard {
    Book book;

    //Use your resource ID for your inner layout
    public MyExpandCard(Context context, Book book) {
        super(context);
        if (book.getClass().getSimpleName().equals("Recommendation")){
            super.setInnerLayout(R.layout.inner_card_rec);
        }else if (book.getClass().getSimpleName().equals("LibraryBook")){
            super.setInnerLayout(R.layout.inner_card_library);
    }
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
        //Set value in text views

    }

    @Override
    public void setRecommendationCardView(View view) {
        TextView descriptionTextView = (TextView) view.findViewById(R.id.description);
        descriptionTextView.setText(String.valueOf(book.getDescription()));
    }

    @Override
    public void setLibraryCardView(View view) {

    }
}