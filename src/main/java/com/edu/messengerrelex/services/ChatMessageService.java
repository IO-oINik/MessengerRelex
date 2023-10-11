package com.edu.messengerrelex.services;

import com.edu.messengerrelex.dto.ChatMessageDto;
import com.edu.messengerrelex.models.ChatMessage;
import com.edu.messengerrelex.models.User;
import com.edu.messengerrelex.repositories.ChatMessageRepository;
import com.edu.messengerrelex.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public ChatMessageService(ChatMessageRepository chatMessageRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.chatMessageRepository = chatMessageRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    public void save(ChatMessage message) {
         chatMessageRepository.save(message);
    }

    public List<ChatMessageDto> AllMessageByUsername1AndUsername2(String username1, String username2) {
        User user1 = userRepository.findByUsername(username1);
        User user2 = userRepository.findByUsername(username2);
        List<ChatMessage> chatMessageList = chatMessageRepository.findBySenderUserAndRecipientUserOrRecipientUserAndSenderUserOrderByTimestampAsc(user1, user2, user1, user2);
        List<ChatMessageDto> chatMessageDtoList = new ArrayList<>();
        for (int i = 0; i < chatMessageList.size(); ++i) {
            chatMessageDtoList.add(modelMapper.map(chatMessageList.get(i), ChatMessageDto.class));
        }
        return chatMessageDtoList;
    }
}
