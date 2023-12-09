package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class SimilarProductAdapter extends RecyclerView.Adapter<SimilarProductAdapter.ViewHolder> {

    private Context context;
    private ArrayList<SimilarProduct> similarList;
    private ArrayList<SimilarProduct> originalList;

    public SimilarProductAdapter(ArrayList<SimilarProduct> similarList, Context context) {
        this.originalList = similarList;
        this.similarList = new ArrayList<>(originalList);
        this.context = context;
    }

    public void refresh(int index1, int index2) {
        if (index1 == 0) {
            similarList = new ArrayList<>(originalList);
        } else if (index1 == 1) {
            if (index2 == 0) {
                similarList.sort((a, b) -> a.getTitle().compareTo(b.getTitle()));
            } else {
                similarList.sort((a, b) -> b.getTitle().compareTo(a.getTitle()));
            }
        } else if (index1 == 2) {
            if (index2 == 0) {
                similarList.sort((a, b) -> Double.compare(a.getCost(), b.getCost()));
            } else {
                similarList.sort((a, b) -> Double.compare(b.getCost(), a.getCost()));
            }
        } else {
            if (index2 == 0) {
                similarList.sort((a, b) -> a.getDays() - b.getDays());
            } else {
                similarList.sort((a, b) -> b.getDays() - a.getDays());
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SimilarProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_similar, parent, false);
        return new SimilarProductAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SimilarProductAdapter.ViewHolder holder, int position) {
        SimilarProduct product = similarList.get(position);

        Glide.with(context)
                .load(product.getImage())
                .into(holder.img);

        holder.title.setText(product.getTitle());

        if (product.getShippingCost() < 0) {
            holder.shippingCost.setText("N/A");
        } else {
            holder.shippingCost.setText(new DecimalFormat("0.00").format(product.getShippingCost()));
        }

        if (product.getCost() < 0) {
            holder.cost.setText("N/A");
        } else {
            holder.cost.setText(new DecimalFormat("0.00").format(product.getCost()));
        }

        if (product.getDays() < 0) {
            holder.days.setText("N/A");
        } else {
            holder.days.setText(product.getDays() +" Days Left");
        }

        // addViewListener(holder, product);
    }

    private void addViewListener(@NonNull SimilarProductAdapter.ViewHolder holder, SimilarProduct product) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(product.getItemUrl()));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return similarList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView img;
        private TextView title;
        private TextView shippingCost;
        private TextView cost;
        private TextView days;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            title = itemView.findViewById(R.id.title);
            shippingCost = itemView.findViewById(R.id.shippingCost);
            cost = itemView.findViewById(R.id.cost);
            days = itemView.findViewById(R.id.days);
        }
    }
}
