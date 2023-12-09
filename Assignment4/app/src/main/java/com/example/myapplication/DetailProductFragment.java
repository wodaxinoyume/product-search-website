package com.example.myapplication;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DetailProductFragment extends Fragment {

    private Product product;
    private RelativeLayout progressComponent;
    private ViewPager imageSlider;
    private LinearLayout content;
    private TextView title;
    private TextView summary;
    private TextView highlightsContent;
    private TextView specificationsContent;
    private TextView specificationsTitle;
    private View divider2;

    public DetailProductFragment(Product product) {
        this.product = product;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_product, container, false);

        progressComponent = view.findViewById(R.id.progressComponent);

        imageSlider = view.findViewById(R.id.imageSlider);

        content = view.findViewById(R.id.content);

        title = view.findViewById(R.id.title);

        summary = view.findViewById(R.id.summary);

        highlightsContent = view.findViewById(R.id.highlightsContent);

        specificationsContent = view.findViewById(R.id.SpecificationsContent);

        specificationsTitle = view.findViewById(R.id.SpecificationsTitle);

        divider2 = view.findViewById(R.id.divider2);

        content.setVisibility(View.GONE);

        performSearch();

        return view;
    }

    private void performSearch() {

        String apiUrl = "https://stone-timing-405020.wl.r.appspot.com/detail?itemId=" + product.getId();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                apiUrl,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressComponent.setVisibility(View.GONE);
                        content.setVisibility(View.VISIBLE);
                        loadImageData(response);
                        loadTitleData(response);
                        loadSummaryData(response);
                        loadHighlightsData(response);
                        loadSpecificationsData(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        );

        Volley.newRequestQueue(requireContext()).add(jsonObjectRequest);
    }

    private void loadImageData(JSONObject response) {

        ArrayList<String> images = new ArrayList<>();
        try {
            JSONArray imageList = response.getJSONObject("Item").getJSONArray("PictureURL");
            for (int i = 0; i < imageList.length(); i++) {
                images.add(imageList.getString(i));
            }
        } catch (JSONException e) {
            System.out.println("no image");
        }

        ImageAdapter imageAdapter = new ImageAdapter(getActivity().getSupportFragmentManager(), requireContext(), images);

        imageSlider.setAdapter(imageAdapter);
    }

    private void loadTitleData(JSONObject response) {
        title.setText(product.getTitle());
    }

    private void loadSummaryData(JSONObject response) {
        String shipping;
        if(product.getShippingCost().equals("free")) {
            shipping = "Free";
        } else {
            shipping = "$" + product.getShippingCost();
        }
        summary.setText("$" + product.getCost() + " with " + shipping + " shipping");
    }

    private void loadHighlightsData(JSONObject response) {
        highlightsContent.setText("Price         $" + product.getCost());
    }

    private void loadSpecificationsData(JSONObject response) {
        JSONArray nameValueListArray;

        try {
            nameValueListArray = response.getJSONObject("Item")
                    .getJSONObject("ItemSpecifics")
                    .getJSONArray("NameValueList");
        } catch (JSONException e) {
            specificationsContent.setVisibility(View.GONE);
            specificationsTitle.setVisibility(View.GONE);
            divider2.setVisibility(View.GONE);
            return;
        }

        try {
            String result = "";

            for (int i = 0; i < nameValueListArray.length(); i++) {
                JSONObject nameValueObject = nameValueListArray.getJSONObject(i);
                String value = nameValueObject.getJSONArray("Value").getString(0);
                String name = nameValueObject.getString("Name");
                String formattedValue = "• " + capitalizeFirstLetter(value);
                if (name.equals("Brand")) {
                    // result = formattedValue + "\n" + result;
                    highlightsContent.append("\nBrand        " + value);
                } else {
                    result += formattedValue + "\n";
                }
            }

            specificationsContent.setText(result);

            if(result.isEmpty()) {
                specificationsContent.setVisibility(View.GONE);
                specificationsTitle.setVisibility(View.GONE);
                divider2.setVisibility(View.GONE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String capitalizeFirstLetter(String value) {
        if (value != null && !value.isEmpty()) {
            return Character.toUpperCase(value.charAt(0)) + value.substring(1);
        }
        return value;
    }

    private class ImageAdapter extends FragmentPagerAdapter {
        private Context context;
        private ArrayList<String> images;

        public ImageAdapter(FragmentManager fm, Context context, ArrayList<String> images) {
            super(fm);
            this.context = context;
            this.images = images;
        }

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public Fragment getItem(int position) {
            return new DetailProductImagesFragment(context, images.get(position));
        }
    }
}