package com.kmpdip.booksio.cards;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.kmpdip.booksio.MainActivity;
import com.kmpdip.booksio.R;
import com.kmpdip.booksio.data.database.DBCDatabase;
import com.kmpdip.booksio.data.structure.Recommendation;

import it.gmariotti.cardslib.library.internal.CardHeader;

/**
 * Created by User1 on 15/11/2015.
 */
public class MyCardHeader extends CardHeader {
    private static MyCardHeader myCardHeader;
    Recommendation book;
    Context context;
    public MyCardHeader(Context context, Recommendation book) {
        super(context, R.layout.card_header_rec);
        this.context=context;
        this.book=book;
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

        if (view == null) return;
        //Retrieve TextView elements
        ImageView cover=(ImageView)view.findViewById(R.id.cover_img);
        Button likeButton=(Button)view.findViewById(R.id.like_button);
        Button dislikeButton=(Button)view.findViewById(R.id.dislike_button);
        TextView titleTextView=(TextView)view.findViewById(R.id.title);
        TextView authorTextView=(TextView)view.findViewById(R.id.author);
        TextView numberFriendsTextView=(TextView)view.findViewById(R.id.friends_number);
        TextView genreTextView=(TextView)view.findViewById(R.id.genres);

        titleTextView.setText(String.valueOf(this.book.getTitle()));
        authorTextView.setText(String.valueOf("By : " + this.book.getAuthor()));
        numberFriendsTextView.setText(String.valueOf(this.book.getFriendsNumber()));
        genreTextView.setText(String.valueOf("Genres : " + this.book.getGenre()));
        cover.setImageBitmap(book.getImage());
        //Set value in text views
        final DBCDatabase db = new DBCDatabase(context);
        likeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                db.updateLibrary(book, true, null);
                db.close();
            }

        });
        dislikeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                db.updateLibrary(book, false, null);
                db.close();
            }
        });

    }
}
