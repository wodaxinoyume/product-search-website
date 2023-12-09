package com.example.myapplication;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MyPagerAdapter extends FragmentStatePagerAdapter {

    private SearchFragment searchFragment;
    private WishlistFragment wishlistFragment;


    public MyPagerAdapter(FragmentManager fm) {
        super(fm);
        searchFragment = new SearchFragment();
        wishlistFragment = new WishlistFragment();
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return searchFragment;
        } else {
            return wishlistFragment;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "SEARCH";
        } else {
            return "WISHLIST";
        }
    }

    public void refresh() {
        wishlistFragment.refresh();
    }
}
