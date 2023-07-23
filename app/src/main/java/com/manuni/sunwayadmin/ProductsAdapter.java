package com.manuni.sunwayadmin;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.manuni.sunwayadmin.databinding.SampleProductsBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductViewHolder>{

    private Context context;
    private ArrayList<ProductsModel> list;

    private String packId;

    public ProductsAdapter(Context context, ArrayList<ProductsModel> list,String packId) {
        this.context = context;
        this.list = list;
        this.packId = packId;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_products,parent,false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {

        ProductsModel data = list.get(position);

        try {
            Picasso.get().load(data.getProductImage()).into(holder.binding.prImage);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        holder.binding.proNumber.setText("Product Number: "+data.getProductNumber());
        holder.binding.proName.setText("Id: "+data.getProductId());
        holder.binding.sellingPrice.setText("Price: "+data.getProductSellingPrice());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent productIntent = new Intent(context,UpdateProducts.class);
                productIntent.putExtra("productNumber",""+data.getProductNumber());
                productIntent.putExtra("sellingPrice",""+data.getProductSellingPrice());
                productIntent.putExtra("productImage",""+data.getProductImage());
                productIntent.putExtra("productId",""+data.getProductId());
                productIntent.putExtra("packId",""+packId);
                context.startActivity(productIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder{

        SampleProductsBinding binding;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = SampleProductsBinding.bind(itemView);
        }
    }
}
