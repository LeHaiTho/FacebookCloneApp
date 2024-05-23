package com.example.facebookclone;

import static android.app.PendingIntent.getActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.facebookclone.Fragment.HomeFragment;
import com.example.facebookclone.Model.PostModel;
import com.example.facebookclone.Model.UserModel;
import com.example.facebookclone.databinding.ActivityAddPostBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class AddPostActivity extends AppCompatActivity {
    ActivityAddPostBinding binding;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    Uri uri ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddPostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initial
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();


        // Load
        database.getReference()
                .child("Users")
                .child(FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            UserModel userModel = snapshot.getValue(UserModel.class);
                            Picasso.get()
                                    .load(userModel.getProfileImage())
                                    .placeholder(R.drawable.placeholder)
                                    .into(binding.avatarImage2);
                            binding.userNameTxt.setText(userModel.getUsername());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        // sự kiện ấn nút tạo bài viết
//        binding.createPostBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                binding.progressBarId.setVisibility(View.VISIBLE);
//
//                // image vào storage của Firebase
//                final StorageReference reference = storage.getReference().child("posts")
//                        .child(FirebaseAuth.getInstance().getUid())
//                        .child(new Date().getTime() + "");
//                reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                            @Override
//                            public void onSuccess(Uri uri) {
//                                PostModel postModel  = new PostModel();
//                                postModel.setPostImage(uri.toString());
//                                postModel.setPostedBy(FirebaseAuth.getInstance().getUid());
//                                postModel.setPostDescription(binding.descriptionEdt.getText().toString());
//                                postModel.setPostedAt(new Date().getTime());
//
//                                database.getReference().child("posts")
//                                        .push()
//                                        .setValue(postModel).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                            @Override
//                                            public void onSuccess(Void unused) {
//                                                Toast.makeText(AddPostActivity.this, "Đăng bài viết thành công!", Toast.LENGTH_SHORT).show();
//
//                                            }
//                                        });
//                            }
//                        });
//                    }
//                });
//
//            }
//        });


//        binding.createPostBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // image vào storage của Firebase
//                final StorageReference reference = storage.getReference().child("posts")
//                        .child(FirebaseAuth.getInstance().getUid())
//                        .child(new Date().getTime() + "");
//
//                if (uri != null) {
//                    reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                @Override
//                                public void onSuccess(Uri uri) {
//                                    PostModel postModel = new PostModel();
//                                    postModel.setPostImage(uri.toString());
//                                    postModel.setPostedBy(FirebaseAuth.getInstance().getUid());
//                                    postModel.setPostDescription(binding.descriptionEdt.getText().toString());
//                                    postModel.setPostedAt(new Date().getTime());
//
//                                    database.getReference().child("posts")
//                                            .push()
//                                            .setValue(postModel).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                                @Override
//                                                public void onSuccess(Void unused) {
//                                                    Toast.makeText(AddPostActivity.this, "Đăng bài viết thành công!", Toast.LENGTH_SHORT).show();
//                                                }
//                                            });
//                                }
//                            });
//                        }
//                    });
//                } else {
//                    // Handle the case when no image is selected
//                    PostModel postModel = new PostModel();
//                    postModel.setPostedBy(FirebaseAuth.getInstance().getUid());
//                    postModel.setPostDescription(binding.descriptionEdt.getText().toString());
//                    postModel.setPostedAt(new Date().getTime());
//
//                    database.getReference().child("posts")
//                            .push()
//                            .setValue(postModel).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                @Override
//                                public void onSuccess(Void unused) {
//                                    Toast.makeText(AddPostActivity.this, "Đăng bài viết thành công!", Toast.LENGTH_SHORT).show();
//                                }
//                            });
//                }
//            }
//        });

        // TEST
        // back to homefragment
        binding.arrowBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               finish();
               onBackPressed();
            }
        });
        // create post btn
        binding.createPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // image vào storage của Firebase
                final StorageReference reference = storage.getReference().child("posts")
                        .child(FirebaseAuth.getInstance().getUid())
                        .child(new Date().getTime() + "");

                if (uri != null) {
                    reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    PostModel postModel = new PostModel();
                                    postModel.setPostImage(uri.toString());
                                    postModel.setPostedBy(FirebaseAuth.getInstance().getUid());
                                    postModel.setPostDescription(binding.descriptionEdt.getText().toString());
                                    postModel.setPostedAt(new Date().getTime());

                                    database.getReference().child("posts")
                                            .push()
                                            .setValue(postModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Toast.makeText(AddPostActivity.this, "Đăng bài viết thành công!", Toast.LENGTH_SHORT).show();

                                                    // Navigate to the first page after completing the post
                                                    Intent intent = new Intent(AddPostActivity.this, HomeFragment.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    startActivity(intent);
                                                }
                                            });
                                }
                            });
                        }
                    });
                } else {
                    // Handle the case when no image is selected
                    PostModel postModel = new PostModel();
                    postModel.setPostedBy(FirebaseAuth.getInstance().getUid());
                    postModel.setPostDescription(binding.descriptionEdt.getText().toString());
                    postModel.setPostedAt(new Date().getTime());

                    database.getReference().child("posts")
                            .push()
                            .setValue(postModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(AddPostActivity.this, "Đăng bài viết thành công!", Toast.LENGTH_SHORT).show();

                                    // Navigate to the first page after completing the post
                                    Intent intent = new Intent(AddPostActivity.this, HomeFragment.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                            });
                }
            }
        });



        // mô tả khi có dữ liệu, và không có dữ liệu thay đổi nút ĐĂNG.
        binding.descriptionEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String desc = binding.descriptionEdt.getText().toString();
                if (!desc.isEmpty()) {
                    binding.createPostBtn.setBackgroundDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.background_button_follow));
                    binding.createPostBtn.setTextColor(getBaseContext().getResources().getColor(R.color.white));
                    binding.createPostBtn.setEnabled(true);
                } else {
                    binding.createPostBtn.setBackgroundDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.background_button_follow_enable));
                    binding.createPostBtn.setTextColor(getBaseContext().getResources().getColor(R.color.outline_icon));
                    binding.createPostBtn.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        // thêm ảnh vào bài post mới.
        binding.uploadImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 13);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data.getData() != null) {
            uri = data.getData();
            binding.newPostImage.setImageURI(uri);
            binding.newPostImage.setVisibility(View.VISIBLE);
            binding.createPostBtn.setBackgroundDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.background_button_follow));
            binding.createPostBtn.setTextColor(getBaseContext().getResources().getColor(R.color.white));
            binding.createPostBtn.setEnabled(true);
        }
    }

}