package com.kmpdip.booksio.cards;

import android.content.Context;

import com.kmpdip.booksio.data.structure.Recommendation;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;


/**
 * Main Card
 */
public class CardWrapper extends Card {

    CardHeader cardHeader;
    public CardWrapper(Context context,Recommendation book) {
        super(context);
        cardHeader=new CardHeader(context);
        cardHeader.setTitle(book.getTitle());
        super.addCardHeader(cardHeader);
    }

}

