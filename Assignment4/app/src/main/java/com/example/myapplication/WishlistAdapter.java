package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.ViewHolder> {

    private List<Product> productList;
    private Context context;
    private TextView totalCost;
    private TextView count;
    private Toolbar bottomBar;
    private RelativeLayout relativeLayout;
    private RelativeLayout message;

    public WishlistAdapter(List<Product> productList, Context context, TextView totalCost, TextView count,
                           Toolbar bottomBar, RelativeLayout relativeLayout, RelativeLayout message) {
        this.productList = productList;
        this.context = context;
        this.totalCost = totalCost;
        this.count = count;
        this.bottomBar = bottomBar;
        this.relativeLayout = relativeLayout;
        this.message = message;

        hideAndDisplay();
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

    private void hideAndDisplay() {
        String units = " items)";
        if(getItemCount() < 2) {
            units = " item)";
        }
        count.setText("Wishlist Total(" + getItemCount() + units);

        double cost = 0;
        for (int i = 0; i < getItemCount(); i++) {
            cost += Double.parseDouble(productList.get(i).getCost());
        }
        String costStr =  new DecimalFormat("0.00").format(cost);
        totalCost.setText("$" + costStr);

        if (getItemCount() == 0) {
            bottomBar.setVisibility(View.GONE);
            relativeLayout.setVisibility(View.GONE);
            message.setVisibility(View.VISIBLE);
        } else {
            bottomBar.setVisibility(View.VISIBLE);
            relativeLayout.setVisibility(View.VISIBLE);
            message.setVisibility(View.GONE);
        }
    }

    private void initializeCard(@NonNull ViewHolder holder, Product product) {
        holder.titleTextView.setText(product.getTitle());
        holder.zipTextView.setText("Zip: " + product.getZipcode());
        holder.costTextView.setText(product.getCost());
        holder.shippingCostTextView.setText(product.getShippingCost());
        holder.conditionTextView.setText(product.getCondition());
        holder.favoriteStatus.setImageResource(R.drawable.cart_remove);

        Glide.with(context)
                .load(product.getImgUrl())
                .into(holder.productImageView);
    }

    private void addButtonListener(@NonNull ViewHolder holder, Product product) {
        holder.favoriteStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteItem(holder, product);
            }
        });
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
                        productList.remove(product);
                        notifyDataSetChanged();
                        hideAndDisplay();
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
                Intent intent = new Intent(context, MainDetailActivity.class);
                intent.putExtra("product", product.getJson().toString());
                intent.putExtra("inFav", true);
                context.startActivity(intent);
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
