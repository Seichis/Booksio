package com.kmpdip.booksio.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.kmpdip.booksio.MainActivity;
import com.kmpdip.booksio.R;
import com.kmpdip.booksio.cards.CardWrapper;
import com.kmpdip.booksio.cards.MyExpandCard;
import com.kmpdip.booksio.data.structure.Recommendation;

import java.util.ArrayList;
import java.util.List;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.ViewToClickToExpand;
import it.gmariotti.cardslib.library.view.CardListView;

/**
 * Created by User1 on 13/11/2015.
 */
public class RecommendationsFragment extends DialogFragment {

    public static RecommendationsFragment getInstance;
    public List<Card> myCardlist = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recommendations, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initCard();
    }

    private void initCard() {
        for (Recommendation rec : MainActivity.getInstance().recommendations){
            myCardlist.add(createCard(rec));
        }

        //Set the arrayAdapter
        CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(getActivity(), myCardlist);
        CardListView cardListView = (CardListView) getActivity().findViewById(R.id.myList);

        //animCardArrayAdapter.setInitialDelayMillis(500);
        if (cardListView != null) {
            cardListView.setAdapter(mCardArrayAdapter);
        }
    }
    public Card createCard(Recommendation book){
        CardWrapper cardWrapper = new CardWrapper(this.getActivity(), book);
        MyExpandCard cardInside = new MyExpandCard(this.getActivity(), book);
        cardWrapper.addCardExpand(cardInside);

        //Add a viewToClickExpand to enable click on whole card
        ViewToClickToExpand viewToClickToExpand =
                ViewToClickToExpand.builder()
                        .highlightView(false)
                        .setupCardElement(ViewToClickToExpand.CardElementUI.CARD);
        cardWrapper.setViewToClickToExpand(viewToClickToExpand);
        cardWrapper.setSwipeable(true);
        return cardWrapper;
    }
}