package com.example.facebookclone.Fragment;

import static com.example.facebookclone.R.id;
import static com.example.facebookclone.R.layout;

import android.accessibilityservice.GestureDescription;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.example.facebookclone.Adapter.PostAdapter;
import com.example.facebookclone.Adapter.StoryAdapter;
import com.example.facebookclone.AddPostActivity;
import com.example.facebookclone.MainActivity;
import com.example.facebookclone.Model.PostModel;
import com.example.facebookclone.Model.StoryModel;
import com.example.facebookclone.Model.UserModel;
import com.example.facebookclone.Model.UserStories;
import com.example.facebookclone.R;
import com.example.facebookclone.databinding.FragmentChatBinding;
import com.example.facebookclone.databinding.FragmentHomeBinding;
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
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Objects;

public class HomeFragment extends Fragment {
    RecyclerView dashboardRv;
    ArrayList<PostModel> postList;
    RecyclerView storyRv;
    ArrayList<StoryModel> storyList;
    MainActivity mainActivity;
    AppCompatImageButton addBtn;
    FirebaseDatabase database;
    FirebaseStorage storage;
    FirebaseAuth mAuth;
    RoundedImageView addStory;
    ActivityResultLauncher<String> galleryLauncher;
    FragmentHomeBinding binding;

    public HomeFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        mainActivity = (MainActivity) getActivity();

        binding.postContentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AddPostActivity.class));
            }
        });

        database.getReference().child("Users").child(mAuth.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    //để đọc dữ liệu từ Firebase Realtime Database.
                    // Phương thức này sẽ đăng ký một lắng nghe cho một sự kiện duy nhất và sau đó hủy bỏ lắng nghe
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            UserModel userModel = snapshot.getValue(UserModel.class);
                            Picasso.get()
                                    .load(userModel.getProfileImage())
                                    .placeholder(R.drawable.placeholder)
                                    .into(binding.avatarImage);
                            Picasso.get()
                                    .load(userModel.getProfileImage())
                                    .placeholder(R.drawable.placeholder)
                                    .into(binding.storyImage);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        storyList = new ArrayList<>();
        StoryAdapter adapter = new StoryAdapter(storyList, getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.listStoryRv.setLayoutManager(linearLayoutManager);
        binding.listStoryRv.setNestedScrollingEnabled(false);
        binding.listStoryRv.setAdapter(adapter);

        // Story recycler view
        database.getReference()
                .child("story")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            storyList.clear();
                            for (DataSnapshot storySnapshot : snapshot.getChildren()){
                                StoryModel story = new StoryModel();
                                story.setStoryBy(storySnapshot.getKey());
                                story.setStoryAt(storySnapshot.child("postedBy").getValue(Long.class));

                                ArrayList<UserStories> stories = new ArrayList<>();
                                for(DataSnapshot snapshot1 : storySnapshot.child("userStory").getChildren()){
                                    UserStories userStories = snapshot1.getValue(UserStories.class);
                                    stories.add(userStories);
                                }
                                story.setStories(stories);
                                storyList.add(story);
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        // Dashboard recycle view
        postList = new ArrayList<>();
        PostAdapter postAdapter = new PostAdapter(postList, getContext());
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getContext());
        binding.dashboardRv.setLayoutManager(linearLayoutManager1);
        binding.dashboardRv.setNestedScrollingEnabled(false);
        binding.dashboardRv.setAdapter(postAdapter);

        database.getReference().child("posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    PostModel postModel = dataSnapshot.getValue(PostModel.class);
                    postModel.setPostId(dataSnapshot.getKey());
                    postList.add(postModel);
                }
                postList.sort((post1, post2) -> post2.getPostId().compareTo(post1.getPostId()));
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        // story
        binding.storyImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                galleryLauncher.launch("image/*");
            }
        });
        galleryLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {

                        final StorageReference reference = storage.getReference()
                                .child("story")
                                .child(FirebaseAuth.getInstance().getUid())
                                .child(new Date().getTime() + "");
                        reference.putFile(result).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        StoryModel story = new StoryModel();
                                        story.setStoryAt(new  Date().getTime());

                                        database.getReference()
                                                .child("story")
                                                .child(FirebaseAuth.getInstance().getUid())
                                                .child("postedBy")
                                                .setValue(story.getStoryAt()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                       UserStories stories = new UserStories(uri.toString(), story.getStoryAt());

                                                       database.getReference()
                                                               .child("story")
                                                               .child(FirebaseAuth.getInstance().getUid())
                                                               .child("userStory")
                                                               .push()
                                                               .setValue(stories);
                                                    }
                                                });
                                    }

                                });
                            }
                        });
                    }
                }
        );
        return binding.getRoot();
    }
}
