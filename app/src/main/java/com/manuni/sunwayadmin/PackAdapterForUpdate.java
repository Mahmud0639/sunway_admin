package com.manuni.sunwayadmin;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.manuni.sunwayadmin.databinding.PackageSampleBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PackAdapterForUpdate extends RecyclerView.Adapter<PackAdapterForUpdate.PackViewModelUpdate>{

    private Context context;
    private ArrayList<PackModel> list;

    public PackAdapterForUpdate(Context context, ArrayList<PackModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public PackViewModelUpdate onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.package_sample,parent,false);
        return new PackViewModelUpdate(view);
    }

    @Override
    public void onBindViewHolder(PackViewModelUpdate holder, int position) {
        PackModel data = list.get(position);

        String imagePack = data.getPackImage();
        String levelName = data.getLevelName();
        String orderPack = data.getPerOrderQuantity();
        String idPack = data.getId();
        String sellPrice = data.getSellingPrice();

        holder.binding.levelName.setText("Level Name: "+levelName);
        holder.binding.perOrder.setText("Per Order: "+orderPack);
        holder.binding.sellingPrice.setText("Selling Price: "+sellPrice);

        try {
            Picasso.get().load(imagePack).placeholder(R.drawable.impl1).into(holder.binding.lvImage);
        } catch (Exception e) {
            holder.binding.lvImage.setImageResource(R.drawable.impl1);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,UpdatePackImage.class);
                intent.putExtra("packId",""+idPack);
                intent.putExtra("imagePack",""+imagePack);
                intent.putExtra("packLevel",""+levelName);
                intent.putExtra("perOrder",""+orderPack);
                intent.putExtra("sellingPrice",""+sellPrice);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class PackViewModelUpdate extends RecyclerView.ViewHolder {

        PackageSampleBinding binding;
        public PackViewModelUpdate(View itemView) {
            super(itemView);

            binding = PackageSampleBinding.bind(itemView);
        }
    }
}
