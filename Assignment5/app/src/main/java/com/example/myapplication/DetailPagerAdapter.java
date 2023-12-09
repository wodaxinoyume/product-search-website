package com.example.myapplication;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class DetailPagerAdapter extends FragmentStatePagerAdapter {

    private DetailProductFragment productFragment;
    private DetailShippingFragment shippingFragment;
    private DetailPhotoFragment photoFragment;
    private DetailSimilarFragment similarFragment;


    public DetailPagerAdapter(FragmentManager fm, Product product) {
        super(fm);
        productFragment = new DetailProductFragment(product);
        shippingFragment = new DetailShippingFragment(product);
        photoFragment = new DetailPhotoFragment(product);
        similarFragment = new DetailSimilarFragment(product);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return productFragment;
        } else if (position == 1) {
            return shippingFragment;
        } else if (position == 2) {
            return photoFragment;
        } else {
            return similarFragment;
        }
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "PRODUCT";
        } else if (position == 1) {
            return "SHIPPING";
        } else if (position == 2) {
            return "PHOTOS";
        } else {
            return "SIMILAR";
        }
    }
}

