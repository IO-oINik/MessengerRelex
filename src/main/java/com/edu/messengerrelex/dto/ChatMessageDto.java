package com.edu.messengerrelex.dto;

import com.edu.messengerrelex.models.ChatMessage;
import com.edu.messengerrelex.models.User;
import lombok.Data;

import java.util.Date;

@Data
public class ChatMessageDto {

    public ChatMessageDto() {}
    public ChatMessageDto(ChatMessage chatMessage) {
        this.senderUsername = chatMessage.getSenderUser().getUsername();
        this.recipientUsername = chatMessage.getRecipientUser().getUsername();
        this.content = chatMessage.getContent();
        this.timestamp = chatMessage.getTimestamp();
    }

    private String senderUsername;
    private String recipientUsername;
    private String content;
    private Date timestamp;
}
