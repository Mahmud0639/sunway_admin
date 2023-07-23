package com.manuni.sunwayadmin;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.manuni.sunwayadmin.databinding.UsersSampleBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UsersAdapterViewHolder>{
    private Context context;
    private ArrayList<Users> list;

    public UsersAdapter(Context context, ArrayList<Users> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public UsersAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.users_sample,parent,false);
        return new UsersAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UsersAdapterViewHolder holder, int position) {
        Users data = list.get(position);

        String email = data.getEmail();
        String userFullName = data.getFullName();
        String onlineStatus = data.getOnline();
        String userPhone = data.getPhoneNumber();
        String userProfile = data.getProfileImage();
        String userTimeStamp = data.getTimestamp();
        String userId = data.getUid();
        String userBal = data.getBalance();

        FirebaseDatabase.getInstance().getReference().child("Users").orderByChild("uid").equalTo(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                        String totalCountOrder = ""+dataSnapshot.child("totalCount").getValue();

                        if (totalCountOrder.equals("0")){
                            holder.binding.totalCountOrder.setVisibility(View.INVISIBLE);
                        }else {
                            holder.binding.totalCountOrder.setText(totalCountOrder);
                            holder.binding.totalCountOrder.setVisibility(View.VISIBLE);
                        }


                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });




        try {
            Picasso.get().load(userProfile).placeholder(R.drawable.ic_person).into(holder.binding.userImage);
        } catch (Exception e) {
            holder.binding.userImage.setImageResource(R.drawable.ic_person);
        }

        holder.binding.userPhone.setText(userPhone);

        holder.binding.userEmail.setText(email);
        holder.binding.userName.setText(userFullName);

        double userBalAsDouble = Double.parseDouble(userBal);


        String balText = String.format("Balance: <b>$%.2f</b>",userBalAsDouble);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            Spanned spannedBalText = Html.fromHtml(balText,Html.FROM_HTML_MODE_LEGACY);
            holder.binding.userBalance.setText(spannedBalText);
        }else {
          //  holder.binding.userBalance.setText(String.format("Balance: $%.2f",userBalAsDouble));
            holder.binding.userBalance.setText(Html.fromHtml(balText));
        }



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent usersIntent = new Intent(context,UsersDetailsActivity.class);
                usersIntent.putExtra("userId",""+userId);
                context.startActivity(usersIntent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class UsersAdapterViewHolder extends RecyclerView.ViewHolder{

        UsersSampleBinding binding;

        public UsersAdapterViewHolder(View itemView) {
            super(itemView);

            binding = UsersSampleBinding.bind(itemView);
        }
    }
}
