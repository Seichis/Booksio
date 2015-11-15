package com.kmpdip.booksio.cards;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.kmpdip.booksio.R;
import com.kmpdip.booksio.data.structure.Recommendation;

import it.gmariotti.cardslib.library.internal.CardExpand;


/**
 * Created by User1 on 13/11/2015.
 */

public class MyExpandCard extends CardExpand {
    Recommendation book;

    //Use your resource ID for your inner layout
    public MyExpandCard(Context context, Recommendation book) {
        super(context, R.layout.inner_card_rec);
        this.book = book;
    }


    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

        if (view == null) return;

        //Set value in text views

    }

}