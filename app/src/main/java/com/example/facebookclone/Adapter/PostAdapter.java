package com.example.facebookclone.Adapter;

import static com.example.facebookclone.Adapter.CommentAdapter.convertToVietnameseTime;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.facebookclone.CommentActivity;
import com.example.facebookclone.Model.NotificationModel;
import com.example.facebookclone.Model.PostModel;
import com.example.facebookclone.Model.UserModel;
import com.example.facebookclone.R;
import com.example.facebookclone.databinding.DashboardRvExampleBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.viewHolder> {

    ArrayList<PostModel> list;
    Context context;


    public PostAdapter(ArrayList<PostModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dashboard_rv_example, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        PostModel postModel = list.get(position);

        // Check if postModel.getPostImage() is null or empty
        if (TextUtils.isEmpty(postModel.getPostImage())) {
            // If there is no image, hide the ImageView
            holder.binding.imagPost.setVisibility(View.GONE);
        } else {
            // If there is an image, show the ImageView and load the image using Picasso
            holder.binding.imagPost.setVisibility(View.VISIBLE);
            Picasso.get().load(postModel.getPostImage())
                    .placeholder(R.drawable.placeholder_bg)
                    .into(holder.binding.imagPost);
        }

        holder.binding.totalLikeTxt.setText(postModel.getPostLike() + " lượt thích");
        holder.binding.totalCommentTxt.setText(postModel.getCommentCount() + " bình luận");
        // nếu phần title rỗng thì không hiện textView
        String description = postModel.getPostDescription();
        if (TextUtils.isEmpty(description)) {
            holder.binding.statusTxt.setVisibility(View.GONE);
        } else {
            holder.binding.statusTxt.setText(postModel.getPostDescription());
            holder.binding.statusTxt.setVisibility(View.VISIBLE);
        }
        String time = convertToVietnameseTime(postModel.getPostedAt());
        holder.binding.timeTxt2.setText(time);
        // get username post
        FirebaseDatabase.getInstance().getReference().child("Users").child(postModel.getPostedBy()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserModel userModel = snapshot.getValue(UserModel.class);
                Picasso.get()
                        .load(userModel.getProfileImage())
                        .placeholder(R.drawable.placeholder)
                        .into(holder.binding.avatarImage);
                holder.binding.userName.setText(userModel.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //



        FirebaseDatabase.getInstance().getReference()
                .child("posts")
                .child(postModel.getPostId())
                .child("likes")
                .child(FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            holder.binding.likeBtn.setTextColor(context.getResources().getColor(R.color.blue));
                            holder.binding.likeBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.like_img, 0, 0, 0);
                        } else {
                            // sự kiện click like button
                            holder.binding.likeBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    FirebaseDatabase.getInstance().getReference()
                                            .child("posts")
                                            .child(postModel.getPostId())
                                            .child("likes")
                                            .child(FirebaseAuth.getInstance().getUid())
                                            .setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    FirebaseDatabase.getInstance().getReference()
                                                            .child("posts")
                                                            .child(postModel.getPostId())
                                                            .child("postLike")
                                                            .setValue(postModel.getPostLike() + 1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) {
                                                                    // How to programmatically set drawableLeft on Android button?
                                                                    holder.binding.likeBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.like_img, 0, 0, 0);

                                                                    NotificationModel notificationModel = new NotificationModel();
                                                                    notificationModel.setNotificationBy(FirebaseAuth.getInstance().getUid());
                                                                    notificationModel.setNotificationAt(new Date().getTime());
                                                                    notificationModel.setPostId(postModel.getPostId());
                                                                    notificationModel.setPostedBy(postModel.getPostedBy());
                                                                    notificationModel.setType("like");

                                                                    FirebaseDatabase.getInstance().getReference()
                                                                            .child("notification")
                                                                            .child(postModel.getPostedBy())
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
        // button comment to comment activity
        holder.binding.commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CommentActivity.class);
                intent.putExtra("postId", postModel.getPostId());
                intent.putExtra("postedBy", postModel.getPostedBy());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {

        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        DashboardRvExampleBinding binding;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DashboardRvExampleBinding.bind(itemView);

        }
    }

}
