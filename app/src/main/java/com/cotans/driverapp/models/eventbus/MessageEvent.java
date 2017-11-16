package com.cotans.driverapp.models.eventbus;


public class MessageEvent {

    String messageText;

    public MessageEvent(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }
}
