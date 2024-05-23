package com.example.facebookclone.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.helper.widget.Layer;
import androidx.recyclerview.widget.RecyclerView;

import com.example.facebookclone.Model.MessModel;
import com.example.facebookclone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SenderandRecipientAdapter extends  RecyclerView.Adapter{

    ArrayList<MessModel> messModels;
    Context context;
    String recId;
    int SENDER_VIEW_TYPE = 1;
    int RECIPIENT_VIEW_TYPE = 2;

    public SenderandRecipientAdapter(ArrayList<MessModel> messModels, Context context) {
        this.messModels = messModels;
        this.context = context;
    }

    public SenderandRecipientAdapter(ArrayList<MessModel> messModels, Context context, String recId) {
        this.messModels = messModels;
        this.context = context;
        this.recId = recId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == SENDER_VIEW_TYPE){
            View view = LayoutInflater.from(context).inflate(R.layout.sender_rv_example, parent,false);
            return new SenderViewHolder(view);
        }else  {
            View view = LayoutInflater.from(context).inflate(R.layout.recipient_rv_example, parent, false);
            return  new RecipientViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MessModel messModel = messModels.get(position);
        if(holder.getClass() == SenderViewHolder.class){
            ((SenderViewHolder)holder).senderText.setText(messModel.getMessage());
        }else {
            ((RecipientViewHolder)holder).recipientText.setText(messModel.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return messModels.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(messModels.get(position).getuId().equals(FirebaseAuth.getInstance().getUid())){
            return SENDER_VIEW_TYPE;
        }else{
            return RECIPIENT_VIEW_TYPE;
        }
    }

    public static class SenderViewHolder extends RecyclerView.ViewHolder{
        TextView senderText, senderTime;
        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            senderText = itemView.findViewById(R.id.senderText);
        }
    }
    public static class RecipientViewHolder extends RecyclerView.ViewHolder{
        TextView recipientText, recipientTime;
        public RecipientViewHolder(@NonNull View itemView) {
            super(itemView);
            recipientText = itemView.findViewById(R.id.recipientText);
        }
    }
}
