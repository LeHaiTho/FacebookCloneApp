package com.example.facebookclone.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.facebookclone.Model.FollowModel;
import com.example.facebookclone.Model.NotificationModel;
import com.example.facebookclone.Model.UserModel;
import com.example.facebookclone.R;
import com.example.facebookclone.databinding.UserRvExampleBinding;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.viewHolder> {

    Context context;
    ArrayList<UserModel> list;

    public UserAdapter(Context context, ArrayList<UserModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_rv_example, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        UserModel userModel = list.get(position);
        Picasso.get().load(userModel.getProfileImage()).placeholder(R.drawable.placeholder).into(holder.binding.avatarImage2);
        holder.binding.userNameTxt.setText(userModel.getUsername());
        FirebaseDatabase.getInstance().getReference()
                        .child("Users")
                                .child(userModel.getUserId())
                                        .child("followers")
                                                .child(FirebaseAuth.getInstance().getUid())
                                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                if(snapshot.exists()){
                                                                    holder.binding.followBtn.setBackgroundDrawable(ContextCompat.getDrawable(context,R.drawable.background_button_follow_enable));
                                                                    holder.binding.followBtn.setText("Đang theo dõi");
                                                                    holder.binding.followBtn.setTextColor(context.getResources().getColor(R.color.black));
                                                                    holder.binding.followBtn.setPadding(20,5,20,5);
                                                                    holder.binding.followBtn.setEnabled(false);
                                                                }else {
                                                                    holder.binding.followBtn.setOnClickListener(new View.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(View view) {
                                                                            FollowModel followModel = new FollowModel();
                                                                            followModel.setFollowedBy(FirebaseAuth.getInstance().getUid());
                                                                            followModel.setFollowedAt(new Date().getTime());

                                                                            FirebaseDatabase.getInstance().getReference()
                                                                                    .child("Users")
                                                                                    .child(userModel.getUserId())
                                                                                    .child("followers")
                                                                                    .child(FirebaseAuth.getInstance().getUid())
                                                                                    .setValue(followModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                        @Override
                                                                                        public void onSuccess(Void unused) {
                                                                                            FirebaseDatabase.getInstance().getReference()
                                                                                                    .child("Users")
                                                                                                    .child(userModel.getUserId())
                                                                                                    .child("followCount")
                                                                                                    .setValue(userModel.getFollowerCount()+1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                        @Override
                                                                                                        public void onSuccess(Void unused) {
                                                                                                            holder.binding.followBtn.setBackgroundDrawable(ContextCompat.getDrawable(context,R.drawable.background_button_follow_enable));
                                                                                                            holder.binding.followBtn.setText("Đang theo dõi");
                                                                                                            holder.binding.followBtn.setTextColor(context.getResources().getColor(R.color.black));
                                                                                                            holder.binding.followBtn.setPadding(20,5,20,5);
                                                                                                            holder.binding.followBtn.setEnabled(false);
                                                                                                            Toast.makeText(context, "Bạn đã theo dõi " + userModel.getUsername(), Toast.LENGTH_SHORT).show();

                                                                                                            NotificationModel notificationModel = new NotificationModel();
                                                                                                            notificationModel.setNotificationBy(FirebaseAuth.getInstance().getUid());
                                                                                                            notificationModel.setNotificationAt(new Date().getTime());
                                                                                                            notificationModel.setType("follow");

                                                                                                            FirebaseDatabase.getInstance().getReference()
                                                                                                                    .child("notification")
                                                                                                                    .child(userModel.getUserId())
                                                                                                                    .push()
                                                                                                                    .setValue(notificationModel);
                                                                                                        }
                                                                                                    });
                                                                                        }
                                                                                    });

                                                                        }
                                                                    });
                                                                }
                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError error) {

                                                            }
                                                        });



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder {
        UserRvExampleBinding binding;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            binding = UserRvExampleBinding.bind(itemView);
        }
    }
}
