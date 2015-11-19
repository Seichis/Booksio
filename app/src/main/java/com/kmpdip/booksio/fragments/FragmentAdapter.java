package com.kmpdip.booksio.fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

;

public class FragmentAdapter extends FragmentPagerAdapter {
    private final String[] TITLES = {"Recommendations", "Library"};
    LibraryFragment mLibraryFragment;
    RecommendationsFragment mRecommendationsFragment;

    public FragmentAdapter(FragmentManager fm) {
        super(fm);
        this.mLibraryFragment = new LibraryFragment();
        this.mRecommendationsFragment = new RecommendationsFragment();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES[position];
    }

    @Override
    public Fragment getItem(int position) {
        // TODO Auto-generated method stub
        switch (position) {
            case 0:
                return this.mRecommendationsFragment;
            case 1:
                return this.mLibraryFragment;
            default:
                break;
        }
        return null;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return TITLES.length;
    }
}