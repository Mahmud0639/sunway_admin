package com.manuni.sunwayadmin;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.manuni.sunwayadmin.databinding.PackagesSampleBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PackAdapter extends RecyclerView.Adapter<PackAdapter.PackViewHolder>{
    private Context context;
    private ArrayList<PackModel> list;

    public PackAdapter(Context context, ArrayList<PackModel> list) {
        this.context = context;
        this.list = list;
    }


    @Override
    public PackViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.packages_sample,parent,false);
        return new PackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PackViewHolder holder, int position) {
        PackModel data = list.get(position);
        String packId = data.getId();
        String packLevelName = data.getLevelName();
        String packPerOrder = data.getPerOrderQuantity();
        String packageImage = data.getPackImage();

        holder.binding.levelName.setText(packLevelName);
        holder.binding.perOrder.setText("$"+packPerOrder);

        try {
            Picasso.get().load(packageImage).placeholder(R.drawable.impl1).into(holder.binding.packImage);
        } catch (Exception e) {
            holder.binding.packImage.setImageResource(R.drawable.impl1);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, ""+packLevelName, Toast.LENGTH_SHORT).show();
                Intent pIntent = new Intent(context,PackInfoActivity.class);
                pIntent.putExtra("pacImg",""+packageImage);
                pIntent.putExtra("pacLevel",""+packLevelName);
                pIntent.putExtra("pacPerOrder",""+packPerOrder);
                pIntent.putExtra("pacId",""+packId);
                context.startActivity(pIntent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class PackViewHolder extends RecyclerView.ViewHolder{

        PackagesSampleBinding binding;

        public PackViewHolder(View itemView) {
            super(itemView);

            binding = PackagesSampleBinding.bind(itemView);
        }
    }
}
