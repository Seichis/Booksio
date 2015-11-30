package com.kmpdip.booksio.fragments;

import android.app.Activity;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kmpdip.booksio.R;
import com.kmpdip.booksio.data.structure.LibraryBook;

import it.gmariotti.cardslib.library.internal.Card;

/**
 * Created by User1 on 19/11/2015.
 */
public class LibraryHasReadFragment extends DialogFragment {

    LibraryHasReadFragmentListener libraryHasReadFragmentListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            libraryHasReadFragmentListener = (LibraryHasReadFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragmen_has_read, container, false);

        return rootView;
    }

    public interface LibraryHasReadFragmentListener {
        void loadLibraryHasReadBooksFromDatabase();

        Card createLibraryHasReadCard(LibraryBook book);

        void initLibraryHasReadCards();
    }


}

