package com.manuni.sunwayadmin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.manuni.sunwayadmin.databinding.PackageConfirmSampleBinding;

import java.util.ArrayList;
import java.util.HashMap;

public class PackageAdapter extends RecyclerView.Adapter<PackageAdapter.PackageViewHolder>{
    private Context context;
    private ArrayList<PackageModel> list;

    public PackageAdapter(Context context, ArrayList<PackageModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public PackageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.package_confirm_sample,parent,false);
        return new PackageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PackageViewHolder holder, int position) {
        PackageModel data = list.get(position);
        String pacName = data.getPackName();
        String userIdPack = data.getUserId();
        String userPackKey = data.getPackKey();
        String userPackId = data.getPackId();

        holder.binding.packName.setText(pacName);

        holder.binding.confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                DatabaseReference myDB;

                 myDB = FirebaseDatabase.getInstance().getReference().child("Users");

                String key = myDB.push().getKey();
                HashMap<String,Object> hashMap = new HashMap<>();
                hashMap.put(""+pacName,"unlocked");
                hashMap.put("packKey",""+userPackKey);
                hashMap.put("packId",""+userPackId);
                hashMap.put("userId",""+userIdPack);
                hashMap.put("packName",""+pacName);


             myDB.child(userIdPack).child("userPackInfo").child(userPackKey).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                 @Override
                 public void onSuccess(Void unused) {
                     Toast.makeText(context, "Package updated.", Toast.LENGTH_SHORT).show();
                 }
             });



            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class PackageViewHolder extends RecyclerView.ViewHolder{
        PackageConfirmSampleBinding binding;

        public PackageViewHolder(View itemView) {
            super(itemView);

            binding  = PackageConfirmSampleBinding.bind(itemView);
        }
    }
}
