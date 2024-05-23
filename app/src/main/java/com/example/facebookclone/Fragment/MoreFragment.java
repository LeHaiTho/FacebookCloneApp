package com.example.facebookclone.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.facebookclone.Adapter.FollowAdapter;
import com.example.facebookclone.Model.FollowModel;
import com.example.facebookclone.Model.UserModel;
import com.example.facebookclone.R;
import com.example.facebookclone.databinding.FragmentMoreBinding;
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

import java.util.ArrayList;
import java.util.Objects;

public class MoreFragment extends Fragment {
    ArrayList<FollowModel> list;
    FragmentMoreBinding binding;
    FirebaseAuth mAuth;
    FirebaseStorage storage;
    FirebaseDatabase database;

    public MoreFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMoreBinding.inflate(inflater, container, false);

        // fetch data user from firebase
        database.getReference().child("Users").child(mAuth.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    //để đọc dữ liệu từ Firebase Realtime Database.
                    // Phương thức này sẽ đăng ký một lắng nghe cho một sự kiện duy nhất và sau đó hủy bỏ lắng nghe
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            UserModel userModel = snapshot.getValue(UserModel.class);
                            Picasso.get().load(userModel.getCoverBanner()).placeholder(R.drawable.placeholder_bg).into(binding.bannerImage);
                            Picasso.get().load(userModel.getProfileImage()).placeholder(R.drawable.placeholder).into(binding.profileImage);
                            binding.userNameTxt.setText(userModel.getUsername());
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        list = new ArrayList<>();
        FollowAdapter adapter = new FollowAdapter(list, getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.listFriendRv.setLayoutManager(linearLayoutManager);
        binding.listFriendRv.setAdapter(adapter);

        database.getReference().child("Users")
                .child(mAuth.getUid())
                .child("followers")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            FollowModel followModel = dataSnapshot.getValue(FollowModel.class);
                            list.add(followModel);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        // change banner profile
        binding.changeBannerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 11);
            }
        });

        // change avatar profile
        binding.changeAvatarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 12);
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 11) {
            if (data.getData() != null) {
                Uri uri = data.getData();
                binding.bannerImage.setImageURI(uri);

                final StorageReference storageReference = storage.getReference().child("cover_banner").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));
                storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getContext(), "Ảnh bìa đã lưu", Toast.LENGTH_SHORT).show();
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                database.getReference().child("Users").child(mAuth.getUid()).child("coverBanner").setValue(uri.toString());
                            }
                        });
                    }
                });

            }
        }
        if(requestCode == 12){

            if (data.getData() != null) {
                Uri uri = data.getData();
                binding.profileImage.setImageURI(uri);

                final StorageReference storageReference = storage.getReference().child("profile_image").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));
                storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getContext(), "Ảnh đại diện đã lưu", Toast.LENGTH_SHORT).show();
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                database.getReference().child("Users").child(mAuth.getUid()).child("profileImage").setValue(uri.toString());
                            }
                        });
                    }
                });

            }
        }
    }
}

