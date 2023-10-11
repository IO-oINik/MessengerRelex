package com.edu.messengerrelex.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name="ChatMessage")
public class ChatMessage {

    public ChatMessage() {}
    public ChatMessage(User senderUser, User recipientUser, String content) {
        this.senderUser = senderUser;
        this.recipientUser = recipientUser;
        this.content = content;
        this.timestamp = new Date();
    }

    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    @JoinColumn(nullable = false)
    private User senderUser;
    @ManyToOne
    @JoinColumn(nullable = false)
    private User recipientUser;
    @Column(nullable = false)
    private String content;
    @Column(nullable = false)
    private Date timestamp;
}
