package com.example.chat;

public class ResponseMessage {

    String textMessage;
    boolean isMe;

    public ResponseMessage(String textMessage, boolean isMe) {
        this.textMessage = textMessage;
        this.isMe = isMe;
    }

    public String gettextMessage() {
        return textMessage;
    }

    public void settextMessage(String textMessage) {
        this.textMessage = textMessage;
    }

    public boolean isMe() {
        return isMe;
    }

    public void setMe(boolean me) {
        isMe = me;
    }
}
