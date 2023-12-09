package com.example.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.wssholmes.stark.circular_score.CircularScoreView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DetailShippingFragment extends Fragment {

    private Product product;
    private TableLayout sellerTable;
    private TableLayout shippingTable;
    private TableLayout ReturnTable;
    private RelativeLayout progressComponent;
    private LinearLayout content;
    private TableRow line1;
    private TableRow line2;
    private TableRow line3;
    private TableRow line4;
    private TextView value1;
    private TextView value2;
    private CircularScoreView value3;
    private ImageView value4;

    public DetailShippingFragment(Product product) {
        this.product = product;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_shipping, container, false);

        sellerTable = view.findViewById(R.id.sellerInfo);

        shippingTable = view.findViewById(R.id.shippingInfo);

        ReturnTable = view.findViewById(R.id.returnPolicy);

        progressComponent = view.findViewById(R.id.progressComponent);

        line1 = view.findViewById(R.id.line1);
        line2 = view.findViewById(R.id.line2);
        line3 = view.findViewById(R.id.line3);
        line4 = view.findViewById(R.id.line4);

        value1 = view.findViewById(R.id.value1);
        value2 = view.findViewById(R.id.value2);
        value3 = view.findViewById(R.id.value3);
        value4 = view.findViewById(R.id.value4);

        content = view.findViewById(R.id.content);
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
                        data2Return(response);
                        data2Seller(response);
                        data2Shipping(response);
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

    private void data2Shipping(JSONObject response) {
        if(!product.getShippingCost().equals("N/A")) {
            if(product.getShippingCost().equals("free")) {
                addDataToTable(shippingTable, "Shipping Cost", "Free");
            } else {
                addDataToTable(shippingTable, "Shipping Cost", "$" + product.getShippingCost());
            }
        }

        JSONObject returnObject;
        try {
            returnObject = response.getJSONObject("Item");
        } catch (JSONException e) {
            returnObject = new JSONObject();
        }

        try {
            String value = returnObject.getString("GlobalShipping");
            value = value.equals("true") ? "Yes" : "No";
            addDataToTable(shippingTable, "Global Shipping", value);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            int value = returnObject.getInt("HandlingTime");
            String str = value > 1 ? value + " days" : value + " day";
            addDataToTable(shippingTable, "Handling Time", str);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            String value = returnObject.getString("ConditionDescription");
            addDataToTable(shippingTable, "Condition", value);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void data2Return(JSONObject response) {

        JSONObject returnObject;
        try {
            returnObject = response.getJSONObject("Item")
                    .getJSONObject("ReturnPolicy");
        } catch (JSONException e) {
            returnObject = new JSONObject();
        }

        try {
            String value = returnObject.getString("ReturnsAccepted");
            addDataToTable(ReturnTable, "Policy", value);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            String value = returnObject.getString("ReturnsWithin");
            addDataToTable(ReturnTable, "Returns Within", value);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            String value = returnObject.getString("Refund");
            addDataToTable(ReturnTable, "Refund Mode", value);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            String value = returnObject.getString("ShippingCostPaidBy");
            addDataToTable(ReturnTable, "Shipped By", value);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void data2Seller(JSONObject response) {
        try {
            JSONObject storefront = response.getJSONObject("Item").getJSONObject("Storefront");
            String storeUrl = storefront.getString("StoreURL");
            String storeName = storefront.getString("StoreName");
            setLink(storeName, storeUrl);
        } catch (JSONException e) {
            line1.setVisibility(View.GONE);
        }

        try {
            JSONObject seller = response.getJSONObject("Item").getJSONObject("Seller");
            String score = seller.getString("FeedbackScore");
            value2.setText(score);
        } catch (JSONException e) {
            line2.setVisibility(View.GONE);
        }

        try {
            JSONObject seller = response.getJSONObject("Item").getJSONObject("Seller");
            String popularity = seller.getString("PositiveFeedbackPercent");
            value3.setScore((int)Float.parseFloat(popularity));
        } catch (JSONException e) {
            line3.setVisibility(View.GONE);
        }

        try {
            JSONObject seller = response.getJSONObject("Item").getJSONObject("Seller");
            String star = seller.getString("FeedbackRatingStar");
            setStar(star);
        } catch (JSONException e) {
            line4.setVisibility(View.GONE);
        }
    }

    private void setLink(String name, String url) {
        SpannableString content = new SpannableString(name);
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        value1.setText(content);
        value1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });
    }

    private void setStar(String star) {

        if ("Yellow".equals(star)) {
            value4.setImageResource(R.drawable.staroutline);
            value4.setColorFilter(Color.YELLOW);
        } else if ("Blue".equals(star)) {
            value4.setImageResource(R.drawable.staroutline);
            value4.setColorFilter(Color.BLUE);
        } else if ("Turquoise".equals(star)) {
            value4.setImageResource(R.drawable.staroutline);
            value4.setColorFilter(Color.parseColor("#40E0D0"));
        } else if ("Purple".equals(star)) {
            value4.setImageResource(R.drawable.staroutline);
            value4.setColorFilter(Color.parseColor("#800080"));
        } else if ("Red".equals(star)) {
            value4.setImageResource(R.drawable.staroutline);
            value4.setColorFilter(Color.RED);
        } else if ("Green".equals(star)) {
            value4.setImageResource(R.drawable.staroutline);
            value4.setColorFilter(Color.GREEN);
        } else if ("YellowShooting".equals(star)) {
            value4.setImageResource(R.drawable.starcircle);
            value4.setColorFilter(Color.YELLOW);
        } else if ("TurquoiseShooting".equals(star)) {
            value4.setImageResource(R.drawable.starcircle);
            value4.setColorFilter(Color.parseColor("#40E0D0"));
        } else if ("PurpleShooting".equals(star)) {
            value4.setImageResource(R.drawable.starcircle);
            value4.setColorFilter(Color.parseColor("#800080"));
        } else if ("RedShooting".equals(star)) {
            value4.setImageResource(R.drawable.starcircle);
            value4.setColorFilter(Color.RED);
        } else if ("GreenShooting".equals(star)) {
            value4.setImageResource(R.drawable.starcircle);
            value4.setColorFilter(Color.GREEN);
        } else if ("SilverShooting".equals(star)) {
            value4.setImageResource(R.drawable.starcircle);
            value4.setColorFilter(Color.GRAY);
        }

    }

    private void addDataToTable(TableLayout tableLayout, String key, String value) {
        TableRow row = new TableRow(requireContext());

        TextView keyTextView = new TextView(requireContext());
        keyTextView.setText(key);
        keyTextView.setTextSize(20);
        keyTextView.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
        keyTextView.setMaxLines(1);

        TextView valueTextView = new TextView(requireContext());
        valueTextView.setText(value);
        valueTextView.setTextSize(20);
        valueTextView.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
        valueTextView.setMaxLines(1);
        valueTextView.setEllipsize(TextUtils.TruncateAt.END);

        row.addView(keyTextView);
        row.addView(valueTextView);

        tableLayout.addView(row);
    }
}