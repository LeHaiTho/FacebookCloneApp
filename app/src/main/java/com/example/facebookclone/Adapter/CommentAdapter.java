package com.example.facebookclone.Adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.facebookclone.Model.CommentModel;
import com.example.facebookclone.Model.UserModel;
import com.example.facebookclone.R;
import com.example.facebookclone.databinding.CommentExampleBinding;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.viewHolder>{

    Context context;
    ArrayList<CommentModel> commentModelArrayList;

    public CommentAdapter(Context context, ArrayList<CommentModel> commentModelArrayList) {
        this.context = context;
        this.commentModelArrayList = commentModelArrayList;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.comment_example, parent,false);
        return new viewHolder(view);
    }

//    @Override
//    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
//        CommentModel commentModel = commentModelArrayList.get(position);
//
//        String time = TimeAgo.using(commentModel.getCommentedAt());
//        holder.binding.timeTxt.setText(time);
//
//        FirebaseDatabase.getInstance().getReference()
//                .child("Users")
//                .child(commentModel.getCommentedBy()).addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        UserModel userModel = snapshot.getValue(UserModel.class);
//                        Picasso.get()
//                                .load(userModel.getProfileImage())
//                                .placeholder(R.drawable.placeholder)
//                                .into(holder.binding.avatarImage);
//                        holder.binding.notificationTxt.setText(Html.fromHtml("<b>" + userModel.getUsername()
//                        + "</b>" + " " +commentModel.getCommentBody()));
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//
//    }
    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
    CommentModel commentModel = commentModelArrayList.get(position);

    // Convert time to Vietnamese
    String time = convertToVietnameseTime(commentModel.getCommentedAt());
    holder.binding.timeTxt.setText(time);

    FirebaseDatabase.getInstance().getReference()
            .child("Users")
            .child(commentModel.getCommentedBy()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    UserModel userModel = snapshot.getValue(UserModel.class);
                    Picasso.get()
                            .load(userModel.getProfileImage())
                            .placeholder(R.drawable.placeholder)
                            .into(holder.binding.avatarImage);
                    holder.binding.notificationTxt.setText(Html.fromHtml("<b>" + userModel.getUsername()
                            + "</b>" + " " + commentModel.getCommentBody()));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
}

    // Function to convert time to Vietnamese
    static String convertToVietnameseTime(long timestamp) {
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - timestamp;

        // Convert milliseconds to minutes
        long minutes = elapsedTime / (60 * 1000);

        if (minutes < 1) {
            return "Vừa xong";
        } else if (minutes < 60) {
            return String.format("%d phút trước", minutes);
        } else {
            return "Vài giờ trước";
        }
    }

    @Override
    public int getItemCount() {
        return commentModelArrayList.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder{
        CommentExampleBinding binding;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            binding = CommentExampleBinding.bind(itemView);
        }
    }
}
