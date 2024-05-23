package com.example.facebookclone;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.facebookclone.Adapter.CommentAdapter;
import com.example.facebookclone.Model.CommentModel;
import com.example.facebookclone.Model.NotificationModel;
import com.example.facebookclone.Model.PostModel;
import com.example.facebookclone.Model.UserModel;
import com.example.facebookclone.databinding.ActivityCommentBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class CommentActivity extends AppCompatActivity {

    ActivityCommentBinding binding;
    Intent intent;
    String postId;
    String postedBy;
    FirebaseDatabase database;
    FirebaseAuth mAuth;
    ArrayList<CommentModel> commentModelArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCommentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        intent = getIntent();
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // set up toolbar comment
        setSupportActionBar(binding.toolbar);
        CommentActivity.this.setTitle("Bình luận");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        postId = intent.getStringExtra("postId");
        postedBy = intent.getStringExtra("postedBy");

        // lấy các thông tin từ PosT Model
        database.getReference().child("posts").child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                PostModel postModel = snapshot.getValue(PostModel.class);
                if (TextUtils.isEmpty(postModel.getPostImage())) {
                    // If there is no image, hide the ImageView
                    binding.postImage.setVisibility(View.GONE);
                }else {
                    Picasso.get()
                            .load(postModel.getPostImage())
                            .placeholder(R.drawable.placeholder_bg)
                            .into(binding.postImage);
                        binding.totalLikeTxt.setText(postModel.getPostLike() + " lượt thích");

                    binding.totalCommentTxt.setText(postModel.getCommentCount() + " bình luận");
                }
                }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // lấy các dữ liệu từ User model(ảnh profile, tên người đăng bài viết)
        database.getReference().child("Users").child(postedBy).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserModel userModel = snapshot.getValue(UserModel.class);

//                Picasso.get().load(userModel.getProfileImage()).placeholder(R.drawable.placeholder).into(binding.avatarImage);
//                binding.userNameTxt.setText(userModel.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // sự kiện nút gửi comment vào bài viết
        binding.sendCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommentModel commentModel = new CommentModel();
                commentModel.setCommentBody(binding.addCommentEdt.getText().toString());
                commentModel.setCommentedAt(new Date().getTime());
                commentModel.setCommentedBy(FirebaseAuth.getInstance().getUid());

                // lưu comment vào firebase
                database.getReference()
                        .child("posts")
                        .child(postId)
                        .child("comments")
                        .push()
                        .setValue(commentModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                database.getReference()
                                        .child("posts")
                                        .child(postId)
                                        .child("commentCount").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                int commentCount = 0;
                                                if (snapshot.exists()) {
                                                    commentCount = snapshot.getValue(Integer.class);
                                                }
                                                database.getReference()
                                                        .child("posts")
                                                        .child(postId)
                                                        .child("commentCount")
                                                        .setValue(commentCount + 1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                binding.addCommentEdt.setText("");
                                                                Toast.makeText(CommentActivity.this, "commented", Toast.LENGTH_SHORT).show();
                                                                NotificationModel notificationModel = new NotificationModel();
                                                                notificationModel.setNotificationBy(FirebaseAuth.getInstance().getUid());
                                                                notificationModel.setNotificationAt(new Date().getTime());
                                                                notificationModel.setPostId(postId);
                                                                notificationModel.setPostedBy(postedBy);
                                                                notificationModel.setType("comment");

                                                                FirebaseDatabase.getInstance().getReference()
                                                                        .child("notification")
                                                                        .child(postedBy)
                                                                        .push()
                                                                        .setValue(notificationModel);
                                                            }
                                                        });
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                            }
                        });
            }
        });
        CommentAdapter commentAdapter = new CommentAdapter(this, commentModelArrayList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.commentRv.setLayoutManager(linearLayoutManager);
        binding.commentRv.setAdapter(commentAdapter);

        database.getReference()
                .child("posts")
                .child(postId)
                .child("comments").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        commentModelArrayList.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            CommentModel commentModel = dataSnapshot.getValue(CommentModel.class);
                            commentModelArrayList.add(commentModel);
                        }
                        commentAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}