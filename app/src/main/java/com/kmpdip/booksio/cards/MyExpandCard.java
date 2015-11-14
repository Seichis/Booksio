package com.kmpdip.booksio.cards;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.kmpdip.booksio.R;
import com.kmpdip.booksio.data.structure.Recommendation;

import it.gmariotti.cardslib.library.internal.CardExpand;


/**
 * Created by User1 on 13/11/2015.
 */

public class MyExpandCard extends CardExpand{
    Recommendation book;
    //Use your resource ID for your inner layout
    public MyExpandCard(Context context,Recommendation book) {
        super(context, R.layout.recommendation_card);
        this.book=book;
    }


    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

        if (view == null) return;
        //Retrieve TextView elements
        ImageView cover=(ImageView)view.findViewById(R.id.cover_img);
        Button likeButton=(Button)view.findViewById(R.id.like_button);
        Button dislikeButton=(Button)view.findViewById(R.id.dislike_button);


        TextView bAuthorTextView=(TextView)view.findViewById(R.id.author);
        Log.i("Author", String.valueOf(this.book.getAuthor()));
        bAuthorTextView.setText(String.valueOf(this.book.getAuthor()));
                //Set value in text views

    }

}