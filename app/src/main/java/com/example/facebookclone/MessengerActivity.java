package com.example.facebookclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Application;
import android.os.Bundle;
import android.view.View;

import com.example.facebookclone.Adapter.SenderandRecipientAdapter;
import com.example.facebookclone.Fragment.ChatFragment;
import com.example.facebookclone.Model.MessModel;
import com.example.facebookclone.databinding.ActivityMessengerBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.zegocloud.uikit.prebuilt.call.ZegoUIKitPrebuiltCallService;
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationConfig;
import com.zegocloud.uikit.service.defines.ZegoUIKitUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class MessengerActivity extends AppCompatActivity {
    ActivityMessengerBinding binding;
    FirebaseDatabase database;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMessengerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        final String senderId = mAuth.getUid();
        String recipientId = getIntent().getStringExtra("userId");
        String username = getIntent().getStringExtra("username");
        String profileImage = getIntent().getStringExtra("profileImage");

        binding.usernameTxt.setText(username);
        Picasso.get().load(profileImage).placeholder(R.drawable.placeholder).into(binding.avatarImage2);

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                  onBackPressed();
            }
        });

        final ArrayList<MessModel> messModels = new ArrayList<>();

        final SenderandRecipientAdapter senderandRecipientAdapter = new SenderandRecipientAdapter(messModels, this);
        binding.messChatListRv.setAdapter(senderandRecipientAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.messChatListRv.setLayoutManager(layoutManager);

        final String senderRoom = senderId + recipientId;
        final String recipientRoom =  recipientId + senderId    ;


        // render mess chat
        database.getReference().child("mess")
                .child(senderRoom)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                messModels.clear();
                                 for(DataSnapshot snapshot1 : snapshot.getChildren()){
                                     MessModel model = snapshot1.getValue(MessModel.class);
                                     messModels.add(model);
                                 }
                                 senderandRecipientAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

        //
        binding.sendMessBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = binding.messageEdt.getText().toString();
                final MessModel model  = new MessModel(senderId, message);
                model.setTimeStamp(new Date().getTime());
                binding.messageEdt.setText("");

                database.getReference().child("mess")
                        .child(senderRoom)
                        .push()
                        .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                database.getReference().child("mess")
                                        .child(recipientRoom)
                                        .push()
                                        .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {

                                            }
                                        });
                            }
                        });

            }
        });

        binding.callVideoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }

        });
        startService(senderId);
        setCallVideo(recipientId);



    }
    public void startService(String userId) {
        Application application = getApplication(); // Android's application context
        long appID = 1754437905;   // yourAppID
        String appSign ="9d79a75694f058b05240d44457a192415c4efa42111114a38dc5424bf024a5a4";  // yourAppSign
        String userID =userId; // yourUserID, userID should only contain numbers, English characters, and '_'.
        String userName =userId;   // yourUserName

        ZegoUIKitPrebuiltCallInvitationConfig callInvitationConfig = new ZegoUIKitPrebuiltCallInvitationConfig();

        ZegoUIKitPrebuiltCallService.init(getApplication(), appID, appSign, userID, userName,callInvitationConfig);
    }


    void setCallVideo(String targetUserId){
        binding.callVideoBtn.setIsVideoCall(true);
        binding.callVideoBtn.setResourceID("zego_uikit_call"); // Please fill in the resource ID name that has been configured in the ZEGOCLOUD's console here.
        binding.callVideoBtn.setInvitees(Collections.singletonList(new ZegoUIKitUser(targetUserId)));
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        ZegoUIKitPrebuiltCallService.unInit();
    }


   
}