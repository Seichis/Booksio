package com.kmpdip.booksio.fragments;

import android.app.Activity;
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

    RecommendationsFragmentListener recommendationsFragmentListener;

    public interface RecommendationsFragmentListener{
        void initCard();
        Card createRecommendationCard(Recommendation book);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            recommendationsFragmentListener = (RecommendationsFragmentListener) activity;
        }catch (ClassCastException e){
            throw new ClassCastException(activity.toString());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_recommendations, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}