package com.example.myapplication;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.content.Intent;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;

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

public class MainSearchActivity extends AppCompatActivity {

    private RelativeLayout progressComponent;
    List<Product> productList;
    private ProductAdapter productAdapter;
    private RecyclerView recyclerView;
    private ArrayList<String> favoriteId;
    private RelativeLayout message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_MyApplication);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recyclerView);

        progressComponent = findViewById(R.id.progressComponent);

        message = findViewById(R.id.message);

        message.setVisibility(View.GONE);

        String searchUrl = getIntent().getStringExtra("searchUrl");

        performDBSearch();

        performSearch(searchUrl);
    }

    @Override
    public Intent getSupportParentActivityIntent() {
        finish();
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 114514) {
            boolean inFavBefore = data.getBooleanExtra("inFavBefore", false);
            boolean inFav = data.getBooleanExtra("inFav", false);
            if (inFav != inFavBefore) {
                productAdapter.update(inFav, inFavBefore);
            }
        }
    }

    private void performDBSearch() {

        String apiUrl = "https://stone-timing-405020.wl.r.appspot.com/findAllId";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                apiUrl,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ArrayList<String> idList = new ArrayList<>();

                        try {
                            JSONArray list = response.getJSONArray("data");

                            for (int i = 0; i < list.length(); i++) {
                                idList.add(list.getJSONObject(i).getString("_id"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        favoriteId = idList;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        );

        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

    private void performSearch(String apiUrl) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                apiUrl,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressComponent.setVisibility(View.GONE);
                        productList = generateProductList(response);
                        if (productList.isEmpty()) {
                            message.setVisibility(View.VISIBLE);
                        }
                        productAdapter = new ProductAdapter(productList, favoriteId, MainSearchActivity.this, MainSearchActivity.this);
                        recyclerView.setAdapter(productAdapter);
                        recyclerView.setLayoutManager(new GridLayoutManager(MainSearchActivity.this, 2));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        );

        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

    private List<Product> generateProductList(JSONObject response) {
        List<Product> productList = new ArrayList<>();

        JSONArray items;
        try {
            items = response.getJSONArray("findItemsAdvancedResponse").getJSONObject(0).
                    getJSONArray("searchResult").getJSONObject(0).getJSONArray("item");
        } catch (JSONException e) {
            e.printStackTrace();
            return productList;
        }

        for(int i = 0; i < items.length(); i++) {
            try {
                JSONObject item = items.getJSONObject(i);
                productList.add(new Product((item)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return productList;
    }
}
