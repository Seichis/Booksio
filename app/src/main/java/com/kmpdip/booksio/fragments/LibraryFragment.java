package com.kmpdip.booksio.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kmpdip.booksio.R;
import com.kmpdip.booksio.data.structure.LibraryBook;

import it.gmariotti.cardslib.library.internal.Card;

public class LibraryFragment extends DialogFragment {

    LibraryFragmentListener libraryFragmentListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            libraryFragmentListener = (LibraryFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_library, container, false);

        return rootView;
    }

    public interface LibraryFragmentListener {
        void loadLibraryBooksFromDatabase();

        Card createLibraryCard(LibraryBook book);

        void initLibraryCards();
    }


}
