package com.manuni.sunwayadmin;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.manuni.sunwayadmin.databinding.WithdrawsSampleBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class WithdrawAdapter extends RecyclerView.Adapter<WithdrawAdapter.WithdrawViewHolder> {

    private ArrayList<WithdrawModel> list;
    private Context context;



    public WithdrawAdapter(ArrayList<WithdrawModel> list, Context context) {
        this.list = list;
        this.context = context;

    }

    @Override
    public WithdrawViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.withdraws_sample, parent, false);
        return new WithdrawViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WithdrawViewHolder holder, int position) {
        WithdrawModel data = list.get(position);
        String complete = data.getComplete();
        String count = data.getCount();
        String image = data.getImage();
        String withdraw = data.getWithdraw();
        String name = data.getName();
        String timestamp = data.getTimestamp();
        String trcAddress = data.getTrcAddress();
        String status = data.getStatus();
        String remainedBalance = data.getRemainBalance();
        String uid = data.getUid();


        holder.binding.nameWithdraw.setText(name);
        holder.binding.balanceWithdraw.setText("$"+withdraw);
        if (status.equals("Pending")){
            holder.binding.approveBtn.setText("Approve");
            holder.binding.approveBtn.setEnabled(true);
            holder.binding.approveStatus.setText("Pending");
            holder.binding.approveStatus.setTextColor(Color.parseColor("#F44336"));
            holder.binding.approveStatus.setBackground(context.getResources().getDrawable(R.drawable.text_pending_bg));
        }else {
            holder.binding.approveBtn.setText("Approved");
            holder.binding.approveBtn.setEnabled(false);
            holder.binding.approveStatus.setText("Approved");
            //  holder.binding.approveStatus.setTextColor(Color.GREEN);
            holder.binding.approveStatus.setTextColor(Color.parseColor("#4CAF50"));
            holder.binding.approveStatus.setBackground(context.getResources().getDrawable(R.drawable.text_approved_bg));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
                holder.binding.balanceWithdraw.setBackground(context.getResources().getDrawable(R.drawable.withdraw_background_approved));
            }else {
                holder.binding.balanceWithdraw.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.withdraw_background_approved));
            }







        }

        try {
            Picasso.get().load(image).placeholder(R.drawable.impl6).into(holder.binding.imageWithdraw);
        } catch (Exception e) {
            holder.binding.imageWithdraw.setImageResource(R.drawable.impl6);
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(timestamp));
        String dateTime = DateFormat.format("dd/MM/yyyy hh:mm aa",calendar).toString();

        holder.binding.dateTime.setText(dateTime);

        holder.binding.approveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context, "Approve button is clicked.", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder  = new AlertDialog.Builder(context);
                    builder.setMessage("Do you want to make it approved?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                holder.binding.approveBtn.setText("Approving...");
                holder.binding.approveBtn.setEnabled(false);

                HashMap<String,Object> hashMap = new HashMap<>();
                hashMap.put("status","Approved");
                hashMap.put("complete","true");

                FirebaseDatabase.getInstance().getReference().child("Withdraws")
                        .child(uid).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        HashMap<String,Object> hashMap1 = new HashMap<>();
                        hashMap1.put("withdrawCount","0");

                        FirebaseDatabase.getInstance().getReference().child("Users")
                                .child(uid)
                                .updateChildren(hashMap1).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                holder.binding.approveBtn.setText("Approved");
                                Toast.makeText(context, "Withdraw info updated.Your user can now withdraw again after threshold reached.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                                }
                            }).setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
            }
        });

        holder.binding.copyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String copyData = data.getTrcAddress();

                ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("text",copyData);
                clipboardManager.setPrimaryClip(clipData);

                Toast.makeText(context, "Text copied!", Toast.LENGTH_SHORT).show();
            }
        });

    /*    holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/
        holder.binding.accountNumber.setText(trcAddress);

        double remBalance = Double.parseDouble(remainedBalance);

        holder.binding.remainsBalance.setText(String.format("$%.2f",remBalance));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class WithdrawViewHolder extends RecyclerView.ViewHolder {

        WithdrawsSampleBinding binding;

        public WithdrawViewHolder(View itemView) {
            super(itemView);

            binding = WithdrawsSampleBinding.bind(itemView);
        }
    }
}
