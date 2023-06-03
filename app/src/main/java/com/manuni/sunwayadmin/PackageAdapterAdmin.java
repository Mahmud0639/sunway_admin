package com.manuni.sunwayadmin;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
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
        String userPackPrice = data.getPrice();
        String account = data.getAccountNumber();

        holder.binding.packName.setText(pacName);

        holder.binding.packPrice.setText("Price: $"+userPackPrice);

        holder.binding.accountNumber.setText("Account Number: "+account);



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

        holder.binding.confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are your sure to confirm ?")
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                FirebaseDatabase.getInstance().getReference().child("Users").child(userIdPack).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot snapshot) {
                                        if (snapshot.exists()){
                                            String totalAsString = ""+snapshot.child("totalCount").getValue();
                                            String balanceOf = ""+snapshot.child("balance").getValue();

                                            double balanceDouble = Double.parseDouble(balanceOf);
                                            double totalOfBalance = balanceDouble + Double.parseDouble(userPackPrice);

                                            int totalAsInt = Integer.parseInt(totalAsString);
                                            int afterReduced = totalAsInt - 1;

                                            HashMap<String,Object> hashMap = new HashMap<>();
                                            hashMap.put("totalCount",""+afterReduced);
                                            hashMap.put("balance",""+totalOfBalance);

                                            FirebaseDatabase.getInstance().getReference().child("Users").child(userIdPack)
                                                    .updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {

                                                    FirebaseFirestore.getInstance().collection("users").document(userIdPack)
                                                            .update("balance",FieldValue.increment(Double.parseDouble(userPackPrice)));


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

                                                            FirebaseDatabase.getInstance().getReference().child("Users")
                                                                    .child(userIdPack).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(DataSnapshot snapshot) {
                                                                    if (snapshot.exists()){
                                                                        String referUser = ""+snapshot.child("referUid").getValue();

                                                                        FirebaseDatabase.getInstance().getReference().child("Users").child(referUser)
                                                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                    @Override
                                                                                    public void onDataChange(DataSnapshot snapshot) {
                                                                                        if (snapshot.exists()){

                                                                                            String userMainBalance = ""+snapshot.child("balance").getValue();

                                                                                            int userPacPrice = Integer.parseInt(userPackPrice);
                                                                                            double originalGift = 0.10*userPacPrice;
                                                                                            double userMainBalanceDouble = Double.parseDouble(userMainBalance);
                                                                                            double totalUserBalance = originalGift + userMainBalanceDouble;


                                                                                            HashMap<String,Object> hash = new HashMap<>();
                                                                                            hash.put("balance",""+totalUserBalance);

                                                                                            FirebaseDatabase.getInstance().getReference().child("Users")
                                                                                                    .child(referUser).updateChildren(hash).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                @Override
                                                                                                public void onSuccess(Void unused) {
                                                                                                    FirebaseFirestore.getInstance().collection("users")
                                                                                                            .document(referUser).update("balance", FieldValue.increment(originalGift)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                        @Override
                                                                                                        public void onSuccess(Void unused) {
                                                                                                            Toast.makeText(context, "Package updated.", Toast.LENGTH_SHORT).show();
                                                                                                        }
                                                                                                    });

                                                                                                }
                                                                                            });
                                                                                        }
                                                                                    }

                                                                                    @Override
                                                                                    public void onCancelled(DatabaseError error) {

                                                                                    }
                                                                                });

                                                                    }





                                                                }

                                                                @Override
                                                                public void onCancelled(DatabaseError error) {

                                                                }
                                                            });






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


                                                  //  Toast.makeText(context, "Pack is now in enabled mode.", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError error) {

                                    }
                                });
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
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



       /* holder.binding.confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {







            }
        });*/
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
