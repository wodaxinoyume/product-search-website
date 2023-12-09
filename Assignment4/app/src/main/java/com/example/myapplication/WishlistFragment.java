package com.example.myapplication;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import android.widget.RelativeLayout;
import android.widget.TextView;
import android.view.View;
import android.view.LayoutInflater;
import androidx.fragment.app.Fragment;
import android.view.ViewGroup;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.ArrayList;

public class WishlistFragment extends Fragment {

    private WishlistAdapter wishlistAdapter;
    private RecyclerView recyclerView;
    private TextView totalCost;
    private TextView count;
    private Toolbar bottomBar;
    private RelativeLayout relativeLayout;
    private RelativeLayout message;

    public WishlistFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wishlist, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);

        totalCost = view.findViewById(R.id.totalCost);

        count = view.findViewById(R.id.count);

        bottomBar = view.findViewById(R.id.toolbar);

        relativeLayout = view.findViewById(R.id.relativeLayout);

        message = view.findViewById(R.id.message);

        performDBSearch();

        return view;
    }

    private void performDBSearch() {

        String apiUrl = "https://stone-timing-405020.wl.r.appspot.com/findAll";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                apiUrl,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        List<Product> productList = generateProductList(response);
                        wishlistAdapter = new WishlistAdapter(productList, requireContext(), totalCost,
                                count, bottomBar, relativeLayout, message);
                        recyclerView.setAdapter(wishlistAdapter);
                        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
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

    private List<Product> generateProductList(JSONObject response) {
        List<Product> productList = new ArrayList<>();

        JSONArray items;
        try {
            items = response.getJSONArray("data");
        } catch (JSONException e) {
            e.printStackTrace();
            return productList;
        }

        for(int i = 0; i < items.length(); i++) {
            try {
                JSONObject item = items.getJSONObject(i).getJSONObject("key");
                productList.add(new Product((item)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return productList;
    }

    public void refresh() {
        performDBSearch();
    }
}