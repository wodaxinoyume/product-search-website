package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;

public class MainDetailActivity extends AppCompatActivity {

    private boolean inFavBefore;
    private boolean inFav;
    private Product product;
    private ImageButton favButton;
    private TabLayout tab;
    private ViewPager viewPager;
    private DetailPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_MyApplication);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_detail);

        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getProductJson();

        tab = findViewById(R.id.tab);
        viewPager = findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(3);

        adapter = new DetailPagerAdapter(getSupportFragmentManager(), product);

        viewPager.setAdapter(adapter);

        tab.setupWithViewPager(viewPager);

        tab.getTabAt(0).setIcon(R.drawable.information_variant_selected);
        tab.getTabAt(1).setIcon(R.drawable.truck_delivery_selected);
        tab.getTabAt(2).setIcon(R.drawable.google_selected);
        tab.getTabAt(3).setIcon(R.drawable.equal_selected);

        favButton = findViewById(R.id.favButton);
        addButtonFunctionality();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.toolbarIcon) {
            String url = "https://www.facebook.com/sharer.php?u=";
            url += product.getItemUrl();
            url += "&hashtag=%23CSCI571Fall23AndroidApp";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public Intent getSupportParentActivityIntent() {
        Intent data = new Intent();
        data.putExtra("inFavBefore", inFavBefore);
        data.putExtra("inFav", inFav);
        setResult(RESULT_OK, data);
        finish();
        return null;
    }

    private void getProductJson() {
        String jsonString = getIntent().getStringExtra("product");

        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            product = new Product(jsonObject);
            setTitle(product.getTitle());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void addButtonFunctionality() {
        inFav = getIntent().getBooleanExtra("inFav", false);
        inFavBefore = inFav;

        if (inFav) {
            favButton.setImageResource(R.drawable.cart_remove_orange);
        } else {
            favButton.setImageResource(R.drawable.cart_add_orange);
        }

        favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inFav) {
                    deleteItem();
                } else {
                    addItem();
                    inFav = true;
                    favButton.setImageResource(R.drawable.cart_remove_orange);

                    String title = product.getTitle();
                    if (title.length() > 10) {
                        title = title.substring(0, 10) + "...";
                    }
                    String message = title + " was added to wishlist";
                    Toast.makeText(MainDetailActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addItem() {
        String apiUrl = "https://stone-timing-405020.wl.r.appspot.com/addItem";

        JSONObject jsonParams = new JSONObject();

        try {
            jsonParams.put("_id", product.getId());
            jsonParams.put("key", product.getJson());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                apiUrl,
                jsonParams,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
//                        String title = product.getTitle();
//                        if (title.length() > 10) {
//                            title = title.substring(0, 10) + "...";
//                        }
//                        String message = title + " was added to wishlist";
//                        Toast.makeText(MainDetailActivity.this, message, Toast.LENGTH_SHORT).show();

//                        inFav = true;
//                        favButton.setImageResource(R.drawable.cart_remove_orange);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String message = "add failed";
                        Toast.makeText(MainDetailActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                }
        );

        Volley.newRequestQueue(MainDetailActivity.this).add(jsonObjectRequest);
    }

    private void deleteItem() {
        String apiUrl = "https://stone-timing-405020.wl.r.appspot.com/deleteItem";

        JSONObject jsonParams = new JSONObject();

        try {
            jsonParams.put("_id", product.getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                apiUrl,
                jsonParams,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String title = product.getTitle();
                        if (title.length() > 10) {
                            title = title.substring(0, 10) + "...";
                        }
                        String message = title + " was removed from wishlist";
                        Toast.makeText(MainDetailActivity.this, message, Toast.LENGTH_SHORT).show();

                        inFav = false;
                        favButton.setImageResource(R.drawable.cart_add_orange);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String message = "delete failed";
                        Toast.makeText(MainDetailActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                }
        );

        Volley.newRequestQueue(MainDetailActivity.this).add(jsonObjectRequest);
    }
}