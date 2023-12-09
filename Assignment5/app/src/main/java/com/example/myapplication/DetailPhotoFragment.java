package com.example.myapplication;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.jar.JarException;

public class DetailPhotoFragment extends Fragment {

    private Product product;
    private ArrayList<String> urlList;
    private LinearLayout content;
    private RelativeLayout progressBar;

    public DetailPhotoFragment(Product product) {
        this.product = product;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_photo, container, false);

        content = view.findViewById(R.id.content);

        progressBar = view.findViewById(R.id.progressComponent);

        performSearch();

        return view;
    }

    private void performSearch() {

        String apiUrl = "https://stone-timing-405020.wl.r.appspot.com/photo?productTitle=" + product.getTitle();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                apiUrl,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.GONE);
                        urlList = generateUrlList(response);
                        addImage(urlList);
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

    private void addImage(ArrayList<String> images) {
        for(int i = 0; i < images.size(); i++) {
            CardView cardView = new CardView(requireContext());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            layoutParams.setMargins(16, 16, 16, 16);
            cardView.setLayoutParams(layoutParams);

            ImageView imageView = new ImageView(requireContext());
            imageView.setLayoutParams(new CardView.LayoutParams(
                    CardView.LayoutParams.MATCH_PARENT,
                    CardView.LayoutParams.WRAP_CONTENT
            ));
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

            Glide.with(requireContext())
                    .load(images.get(i))
                    .into(imageView);

            cardView.addView(imageView);

            content.addView(cardView);
        }
    }

    private ArrayList<String> generateUrlList(JSONObject response) {
        ArrayList<String> imgList = new ArrayList<>();

        ArrayList<String> imageUrlList = new ArrayList<>();
        imageUrlList.add("https://rabujoi.files.wordpress.com/2021/12/mt235.jpg?w=840");
        imageUrlList.add("https://static1.cbrimages.com/wordpress/wp-content/uploads/2022/03/Eris-Boreas-Greyrat-from-Moshuku-Tensei-Jobless-Reincarnation.jpg");
        imageUrlList.add("https://image.civitai.com/xG1nkqKTMzGDvpLrqFT7WA/e26f464d-8fab-46c1-a4eb-a7b647bf6d40/width=450/00137.jpeg");
        imageUrlList.add("https://images.goodsmile.info/cgm/images/product/20220421/12602/97889/large/ccd56e5284c5e9ea8d83792604db507f.jpg");
        imageUrlList.add("https://m.media-amazon.com/images/I/618OPljgw8L._AC_UF894,1000_QL80_.jpg");
        imageUrlList.add("https://resize.cdn.otakumode.com/ex/800.1200/shop/product/aedb477aaf124a51bf64852d2376ae6a.jpg");
        imageUrlList.add("https://images.goodsmile.info/cgm/images/product/20220331/12522/97161/large/a6205cd069027a7114fb0c4181b1b211.jpg");
        imageUrlList.add("https://image.civitai.com/xG1nkqKTMzGDvpLrqFT7WA/605f5031-bf86-44cd-abec-eeaeca3c66f4/width=450/00136.jpeg");

        try {
            JSONArray list = response.getJSONArray("items");
            for(int i = 0; i < list.length(); i++) {
                imgList.add(list.getJSONObject(i).getString("link"));
            }
            if (imgList.size() == 0) {
                return imageUrlList;
            }
        } catch(JSONException e) {
            return imageUrlList;
        }

        return imgList;
    }
}