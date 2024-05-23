package com.example.facebookclone.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.facebookclone.MessengerActivity;
import com.example.facebookclone.Model.UserModel;
import com.example.facebookclone.R;
import com.example.facebookclone.databinding.ChatRvExampleBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.viewHolder> {
    Context context;
    ArrayList<UserModel> list;

    public ChatAdapter(Context context, ArrayList<UserModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_rv_example,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        UserModel userModel = list.get(position);
        Picasso.get()
                .load(userModel.getProfileImage())
                .placeholder(R.drawable.placeholder)
                .into(holder.binding.avatarImage2);
        holder.binding.userNameTxt.setText(userModel.getUsername());

        // last message
//        FirebaseDatabase.getInstance().getReference().child("mess")
//                        .child(FirebaseAuth.getInstance().getUid() + userModel.getUserId())
//                                .orderByChild("timeStamp")
//                                        .limitToLast(1)
//                                                .addListenerForSingleValueEvent(new ValueEventListener() {
//                                                    @Override
//                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                                        if(snapshot.hasChildren()){
//                                                            for(DataSnapshot snapshot1 : snapshot.getChildren()){
//                                                                holder.binding.lastMessTxt.setText(Objects.requireNonNull(snapshot1.child("message").getValue()).toString());
//                                                            }
//                                                        }
//                                                    }
//
//                                                    @Override
//                                                    public void onCancelled(@NonNull DatabaseError error) {
//
//                                                    }
//                                                });


        FirebaseDatabase.getInstance().getReference().child("mess")
                .child(FirebaseAuth.getInstance().getUid() + userModel.getUserId())
                .orderByChild("timeStamp")
                .limitToLast(1)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChildren()){
                            for(DataSnapshot snapshot1 : snapshot.getChildren()){
                                String lastMessage = Objects.requireNonNull(snapshot1.child("message").getValue()).toString();
                                holder.binding.lastMessTxt.setText(lastMessage);

                                // Show the UI element or take any other action
                                holder.binding.lastMessTxt.setVisibility(View.VISIBLE);
                            }
                        } else {
                            // No messages found, hide the UI element or take any other action
                            holder.binding.lastMessTxt.setText(" ");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle errors if any
                    }
                });



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MessengerActivity.class);
                intent.putExtra("userId", userModel.getUserId());
                intent.putExtra("profileImage", userModel.getProfileImage());
                intent.putExtra("username", userModel.getUsername());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder{
        ChatRvExampleBinding binding;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
           binding = ChatRvExampleBinding.bind(itemView);

        }
    }
}
