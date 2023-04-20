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
import com.google.firebase.database.ValueEventListener;
import com.manuni.sunwayadmin.databinding.PackageConfirmSampleBinding;

import java.util.ArrayList;
import java.util.HashMap;

public class PackageAdapterAdmin extends RecyclerView.Adapter<PackageAdapterAdmin.PackageViewHolder>{
    private Context context;
    private ArrayList<PackageModelAdmin> list;

    public PackageAdapterAdmin(Context context, ArrayList<PackageModelAdmin> list) {
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
        PackageModelAdmin data = list.get(position);
        String pacName = data.getPackName();
        String userIdPack = data.getUserId();
        String userPackKey = data.getPackKey();
        String userPackId = data.getPackId();

        holder.binding.packName.setText(pacName);


        DatabaseReference myD = FirebaseDatabase.getInstance().getReference().child("Users");

        myD.child(userIdPack).child("userPackInfo").child(userPackKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String packStatus = ""+snapshot.child(pacName).getValue();
                    if (packStatus.equals("unlocked")){
                        holder.binding.confirmBtn.setText("unlocked");
                        holder.binding.confirmBtn.setEnabled(false);
                    }else if (packStatus.equals("locked")){
                        holder.binding.confirmBtn.setEnabled(true);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });



        myD.child(userIdPack).child("userPackInfo").child(userPackKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String taskStatus = ""+snapshot.child("taskTaken").getValue();
                    if (taskStatus.equals("false")){
                        holder.binding.enableBtn.setText("Disabled");
                        holder.binding.enableBtn.setEnabled(false);
                    }else if (taskStatus.equals("true")){
                        holder.binding.enableBtn.setEnabled(true);
                    }else {
                        Toast.makeText(context, "User has no task yet.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

        holder.binding.enableBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                holder.binding.enableBtn.setEnabled(false);

                HashMap<String,Object> hashMap = new HashMap<>();
                hashMap.put("taskTaken","false");

                FirebaseDatabase.getInstance().getReference().child("Users")
                        .child(userIdPack).child("userPackInfo")
                        .child(userPackKey).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context, "User will be able to take his/her income now.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

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
                hashMap.put("status","Joined");


             myDB.child(userIdPack).child("userPackInfo").child(userPackKey).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                 @Override
                 public void onSuccess(Void unused) {
                     Toast.makeText(context, "Package updated.", Toast.LENGTH_SHORT).show();

                     /*HashMap<String,Object> hashMap1 = new HashMap<>();
                     hashMap1.put(""+pacName,"false");

                     myDB.child(userIdPack).updateChildren(hashMap1).addOnSuccessListener(new OnSuccessListener<Void>() {
                         @Override
                         public void onSuccess(Void unused) {
                             Toast.makeText(context, "Pack has been updated in the user info.", Toast.LENGTH_SHORT).show();
                         }
                     });*/
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
