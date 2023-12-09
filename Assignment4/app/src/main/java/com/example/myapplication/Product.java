package com.example.myapplication;

import org.json.JSONException;
import org.json.JSONObject;
import java.text.DecimalFormat;

public class Product {
    private String imgUrl;
    private String title;
    private String zipcode;
    private String condition;
    private String cost;
    private String shippingCost;
    private String id;
    private JSONObject json;

    public Product(JSONObject item) {
        json = item;
        setImg(item);
        setTitle(item);
        setCondition(item);
        setZipcode(item);
        setShippingCost(item);
        setCost(item);
        setId(item);
    }

    public JSONObject getJson() {
        return json;
    }

    public String getId() {
        return id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getZipcode() {
        return zipcode;
    }

    public String getCondition() {
        return condition;
    }

    public String getCost() {
        return cost;
    }

    public String getShippingCost() {
        return shippingCost;
    }

    private void setId(JSONObject item) {
        try {
            id = item.getJSONArray("itemId").getString(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setImg(JSONObject item) {
        try {
            imgUrl = item.getJSONArray("galleryURL").getString(0);
        } catch (JSONException e) {
            imgUrl = "https://www.bobswatches.com/rolex-blog/wp-content/uploads/2017/01/ebay-inventory-management-post.jpg";
        }
    }

    private void setTitle(JSONObject item) {
        try {
            title = item.getJSONArray("title").getString(0);
        } catch (JSONException e) {
            title = "N/A";
        }
    }

    //"condition":[{"conditionId":["2010"],"conditionDisplayName":["Excellent - Refurbished"]}]
    private void setCondition(JSONObject item) {
        try {
            condition = item.getJSONArray("condition")
                    .getJSONObject(0)
                    .getJSONArray("conditionDisplayName")
                    .getString(0);
            if(condition.length() > 10) {
                condition = condition.substring(0, 10) + "...";
            }
        } catch (JSONException e) {
            condition = "N/A";
        }
    }

    private void setCost(JSONObject item) {
        try {
            double value = item.getJSONArray("sellingStatus")
                    .getJSONObject(0)
                    .getJSONArray("currentPrice")
                    .getJSONObject(0)
                    .getDouble("__value__");
            cost = new DecimalFormat("0.00").format(value);
        } catch (JSONException e) {
            cost = "N/A";
        }
    }

    private void setShippingCost(JSONObject item) {
        try {
            double value = item.getJSONArray("shippingInfo")
                    .getJSONObject(0)
                    .getJSONArray("shippingServiceCost")
                    .getJSONObject(0)
                    .getDouble("__value__");
            if(value == 0) {
                shippingCost = "free";
            } else {
                shippingCost = new DecimalFormat("0.00").format(value);
            }
        } catch (JSONException e) {
            shippingCost = "N/A";
        }
    }

    private void setZipcode(JSONObject item) {
        try {
            zipcode = item.getJSONArray("postalCode").getString(0);
        } catch (JSONException e) {
            zipcode = "N/A";
        }
    }

    public String getItemUrl() {
        String url;
        try {
            url = json.getJSONArray("viewItemURL").getString(0);
        } catch (JSONException e) {
            url = "";
        }
        return url;
    }

}
