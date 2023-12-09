package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private int lastVisit;
    private List<Product> productList;
    private Context context;
    private Activity activity;
    private ArrayList<String> favoriteId;

    public ProductAdapter(List<Product> productList, ArrayList<String> favoriteId, Context context, Activity activity) {
        this.productList = productList;
        this.context = context;
        this.favoriteId = favoriteId;
        this.activity = activity;
    }

    public void update(boolean inFav, boolean inFavBefore) {
        String itemId = productList.get(lastVisit).getId();
        if (inFavBefore) {
            favoriteId.remove(itemId);
        } else {
            favoriteId.add(itemId);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Product product = productList.get(position);

        initializeCard(holder, product);

        addButtonListener(holder, product);

        addViewListener(holder, product);
    }

    private void initializeCard(@NonNull ViewHolder holder, Product product) {
        if (product.getTitle().length() < 30) {
            holder.titleTextView.setText(product.getTitle() + '\n');
        } else {
            holder.titleTextView.setText(product.getTitle());
        }
        holder.zipTextView.setText("Zip: " + product.getZipcode());
        holder.costTextView.setText(product.getCost());
        holder.shippingCostTextView.setText(product.getShippingCost());
        holder.conditionTextView.setText(product.getCondition());

        if (favoriteId.contains(product.getId())) {
            holder.favoriteStatus.setImageResource(R.drawable.cart_remove);
        } else {
            holder.favoriteStatus.setImageResource(R.drawable.cart_plus);
        }

        Glide.with(context)
                .load(product.getImgUrl())
                .into(holder.productImageView);
    }

    private void addButtonListener(@NonNull ViewHolder holder, Product product) {
        holder.favoriteStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (favoriteId.contains(product.getId())) {
                    deleteItem(holder, product);
                } else {
                    addItem(holder, product);
                    favoriteId.add(product.getId());
                    holder.favoriteStatus.setImageResource(R.drawable.cart_remove);

                    String title = product.getTitle();
                    if (title.length() > 10) {
                        title = title.substring(0, 10) + "...";
                    }
                    String message = title + " was added to wishlist";
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addItem(@NonNull ViewHolder holder, Product product) {
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
//                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
//                        favoriteId.add(product.getId());
//                        holder.favoriteStatus.setImageResource(R.drawable.cart_remove);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String message = "add failed";
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    }
                }
        );

        Volley.newRequestQueue(context).add(jsonObjectRequest);
    }

    private void deleteItem(@NonNull ViewHolder holder, Product product) {
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
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        favoriteId.remove(product.getId());
                        holder.favoriteStatus.setImageResource(R.drawable.cart_plus);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String message = "delete failed";
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    }
                }
        );

        Volley.newRequestQueue(context).add(jsonObjectRequest);
    }

    private void addViewListener(@NonNull ViewHolder holder, Product product) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lastVisit = productList.indexOf(product);
                Intent intent = new Intent(context, MainDetailActivity.class);
                intent.putExtra("product", product.getJson().toString());
                intent.putExtra("inFav", favoriteId.contains(product.getId()));
                activity.startActivityForResult(intent, 114514);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageButton favoriteStatus;
        ImageView productImageView;
        TextView titleTextView;
        TextView costTextView;
        TextView conditionTextView;
        TextView shippingCostTextView;
        TextView zipTextView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            favoriteStatus = itemView.findViewById(R.id.imageButton);
            productImageView = itemView.findViewById(R.id.img);
            titleTextView = itemView.findViewById(R.id.title);
            costTextView = itemView.findViewById(R.id.cost);
            conditionTextView = itemView.findViewById(R.id.condition);
            shippingCostTextView = itemView.findViewById(R.id.shippingCost);
            zipTextView = itemView.findViewById(R.id.zipcode);
        }
    }
}

