package com.kmpdip.booksio.cards;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;

import com.kmpdip.booksio.R;
import com.kmpdip.booksio.data.structure.Book;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.ViewToClickToExpand;
import it.gmariotti.cardslib.library.view.CardView;
import it.gmariotti.cardslib.library.view.CardViewNative;


/**
 * Main Card
 */
public class CardWrapper extends Card implements ICard{

    CardHeader cardHeader;
    MyExpandCard cardInside;
    public CardWrapper(Context context, Book book) {
        super(context);
        this.cardHeader = new MyCardHeader(context, book);
        this.cardInside = new MyExpandCard(context, book);
        super.addCardHeader(cardHeader);
        super.addCardExpand(cardInside);

        //Add a viewToClickExpand to enable click on whole card
        ViewToClickToExpand viewToClickToExpand =
                ViewToClickToExpand.builder()
                        .highlightView(true)
                        .setupCardElement(ViewToClickToExpand.CardElementUI.CARD);

        super.setViewToClickToExpand(viewToClickToExpand);
        }


    @Override
    public void setRecommendationCardView(View view) {

    }

    @Override
    public void setLibraryCardView(View view) {

    }
}

