package com.example.myapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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

public class DetailSimilarFragment extends Fragment {

    private Product product;
    private RelativeLayout progressComponent;
    private LinearLayout content;
    private Spinner sortName;
    private Spinner sortDirection;
    private ArrayList<SimilarProduct> similarList;
    private SimilarProductAdapter adapter;
    private RecyclerView recyclerView;
    private int index1;
    private int index2;

    public DetailSimilarFragment(Product product) {
        this.product = product;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail_similar, container, false);

        progressComponent = view.findViewById(R.id.progressComponent);

        sortName = view.findViewById(R.id.sortName);
        sortDirection = view.findViewById(R.id.sortDirection);

        content = view.findViewById(R.id.content);
        content.setVisibility(View.GONE);

        recyclerView = view.findViewById(R.id.recyclerView);

        index1 = 0;
        index2 = 0;
        sortDirection.setEnabled(false);

        addSpinnerNameFunctionality();
        addSpinnerDirectionFunctionality();

        performSearch();

        return view;
    }

    private void performSearch() {

        String apiUrl = "https://stone-timing-405020.wl.r.appspot.com/similar?" + "itemId=" + product.getId();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                apiUrl,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        content.setVisibility(View.VISIBLE);
                        progressComponent.setVisibility(View.GONE);
                        similarList = generateSimilarList(response);
                        adapter = new SimilarProductAdapter(similarList, requireContext());
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
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

    private void addSpinnerNameFunctionality() {
        ArrayAdapter<CharSequence> sadapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.sortName,
                android.R.layout.simple_spinner_item
        );

        sadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sortName.setAdapter(sadapter);

        sortName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                switch (position) {
                    case 0:
                        index1 = 0;
                        sortDirection.setEnabled(false);
                        adapter.refresh(index1, index2);
                        break;
                    case 1:
                        index1 = 1;
                        sortDirection.setEnabled(true);
                        adapter.refresh(index1, index2);
                        break;
                    case 2:
                        index1 = 2;
                        sortDirection.setEnabled(true);
                        adapter.refresh(index1, index2);
                        break;
                    case 3:
                        index1 = 3;
                        sortDirection.setEnabled(true);
                        adapter.refresh(index1, index2);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void addSpinnerDirectionFunctionality() {
        ArrayAdapter<CharSequence> sadapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.sortDirection,
                android.R.layout.simple_spinner_item
        );

        sadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sortDirection.setAdapter(sadapter);

        sortDirection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                switch (position) {
                    case 0:
                        index2 = 0;
                        adapter.refresh(index1, index2);
                        break;
                    case 1:
                        index2 = 1;
                        adapter.refresh(index1, index2);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private ArrayList<SimilarProduct> generateSimilarList(JSONObject response) {
        ArrayList<SimilarProduct> similars = new ArrayList<>();

        try {
            JSONArray items = response.getJSONObject("getSimilarItemsResponse")
                    .getJSONObject("itemRecommendations")
                    .getJSONArray("item");

            for(int i = 0; i < items.length(); i++) {
                similars.add(new SimilarProduct(items.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return similars;
    }
}