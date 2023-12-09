package com.example.myapplication;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimilarProduct {

    private String itemUrl;
    private String image;
    private String title;
    private double shippingCost;
    private double cost;
    private int days;

    public  SimilarProduct(JSONObject item) {
        setCost(item);
        setDays(item);
        setItemUrl(item);
        setShippingCost(item);
        setTitle(item);
        setImage(item);
    }

    private void setItemUrl(JSONObject item) {
        try {
            itemUrl = item.getString("viewItemURL");
        } catch (JSONException e) {
            itemUrl = "N/A";
        }
    }

    private void setImage(JSONObject item) {
        try {
            image = item.getString("imageURL");
        } catch (JSONException e) {
            image = "N/A";
        }
    }

    private void setTitle(JSONObject item) {
        try {
            title = item.getString("title");
        } catch (JSONException e) {
            title = "N/A";
        }
    }

    private void setShippingCost(JSONObject item) {
        try {
            shippingCost = item.getJSONObject("shippingCost").getDouble("__value__");
        } catch (JSONException e) {
            shippingCost = -1;
        }
    }

    private void setCost(JSONObject item) {
        try {
            cost = item.getJSONObject("buyItNowPrice").getDouble("__value__");
        } catch (JSONException e) {
            cost = -1;
        }
    }

    private void setDays(JSONObject item) {
        try {
            String s = item.getString("timeLeft");

            Pattern pattern = Pattern.compile("P(\\d+)D");
            Matcher matcher = pattern.matcher(s);

            if (matcher.find()) {
                String daysString = matcher.group(1);
                days = Integer.parseInt(daysString);
            } else {
                days = -1;
            }
        } catch (JSONException e) {
                days = -1;
        }
    }

    public String getTitle() {
        return title;
    }

    public double getCost() {
        return cost;
    }

    public double getShippingCost() {
        return shippingCost;
    }

    public int getDays() {
        return days;
    }

    public String getImage() {
        return image;
    }

    public String getItemUrl() {
        return itemUrl;
    }
}

