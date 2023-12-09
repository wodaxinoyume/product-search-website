package com.example.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

public class DetailProductImagesFragment extends Fragment {

    private String url;
    private Context context;

    public DetailProductImagesFragment(Context context, String url) {
        this.url = url;
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_product_images, container, false);

        ImageView imageView = new ImageView(requireContext());

        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        imageView.setLayoutParams(layoutParams);

        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

        Glide.with(context)
                .load(url)
                .into(imageView);

        ((ViewGroup) view).addView(imageView);

        return view;
    }
}
