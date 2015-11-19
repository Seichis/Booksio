package com.kmpdip.booksio.cards;

import android.content.Context;

import com.kmpdip.booksio.data.structure.Book;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;


/**
 * Main Card
 */
public class CardWrapper extends Card {

    CardHeader cardHeader;

    public CardWrapper(Context context, Book book) {
        super(context);
        cardHeader = new MyCardHeader(context, book);
        super.addCardHeader(cardHeader);
    }

}

