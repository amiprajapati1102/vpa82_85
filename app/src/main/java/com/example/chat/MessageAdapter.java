package com.example.chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.CustomeViewHolder> {

    class CustomeViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        public CustomeViewHolder(View itemView){
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.textMessage);
        }
    }
    List<ResponseMessage> responseMessagesList;

    public MessageAdapter(List<ResponseMessage> responseMessageList) {
        this.responseMessagesList = responseMessageList;
    }

    @Override
    public int getItemViewType(int position) {
        if(responseMessagesList.get(position).isMe()){
            return R.layout.me_bubbel;
        } else{
            return R.layout.bot_bubble;
        }
    }

    @NonNull
    @Override
    public CustomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CustomeViewHolder(LayoutInflater.from(parent.getContext()).inflate(viewType,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.CustomeViewHolder holder, int position) {
        holder.textView.setText(responseMessagesList.get(position).gettextMessage());
    }

    @Override
    public int getItemCount() {
        return responseMessagesList.size();
    }
}
