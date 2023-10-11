package com.edu.messengerrelex.repositories;

import com.edu.messengerrelex.models.ChatMessage;
import com.edu.messengerrelex.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findBySenderUserAndRecipientUserOrRecipientUserAndSenderUserOrderByTimestampAsc(User senderUser, User recipientUser, User recipientUser2, User senderUser2);
}
