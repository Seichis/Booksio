package com.kmpdip.booksio.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.astuetz.PagerSlidingTabStrip;
import com.kmpdip.booksio.R;

/**
 * Created by User1 on 13/11/2015.
 */
public class RecommendationsFragment extends DialogFragment {

//    public static RecommendationsFragment newInstance() {
//        RecommendationsFragment f = new RecommendationsFragment();
//        return f;
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (getDialog() != null) {
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        View rootView = inflater.inflate(R.layout.fragment_recommendations, container, false);


        return rootView;
    }
}