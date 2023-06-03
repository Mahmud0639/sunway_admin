package com.manuni.sunwayadmin;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.manuni.sunwayadmin.databinding.SliderSampleBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderViewHolder>{
    private Context context;
    private ArrayList<SliderModel> list;
    private ProgressDialog dialog;

    public SliderAdapter(Context context, ArrayList<SliderModel> list){
        this.context = context;
        this.list = list;
        dialog = new ProgressDialog(context);
    }


    @Override
    public SliderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.slider_sample,parent,false);
        return new SliderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SliderViewHolder holder, int position) {
        SliderModel data = list.get(position);


        holder.binding.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.setMessage("Deleting slider...");
                dialog.show();
                FirebaseDatabase.getInstance().getReference().child("SliderImages")
                        .child(data.getId()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        dialog.dismiss();
                        Toast.makeText(context, "Slider deleted successfully!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        try {
            Picasso.get().load(data.getSlider()).placeholder(R.drawable.impl1).into(holder.binding.sliderImage);
        } catch (Exception e) {
            holder.binding.sliderImage.setImageResource(R.drawable.impl1);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class SliderViewHolder extends RecyclerView.ViewHolder{

        SliderSampleBinding binding;

        public SliderViewHolder(View itemView) {
            super(itemView);
            binding = SliderSampleBinding.bind(itemView);
        }
    }
}
