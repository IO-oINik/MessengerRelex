package com.edu.messengerrelex.controllers;

import com.edu.messengerrelex.dto.ChatMessageDto;
import com.edu.messengerrelex.models.ChatMessage;
import com.edu.messengerrelex.models.User;
import com.edu.messengerrelex.payload.requests.ChatMessageRequest;
import com.edu.messengerrelex.services.ChatMessageService;
import com.edu.messengerrelex.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
public class ChatMessageController {
    private final ChatMessageService chatMessageService;
    private final UserService userService;

    public ChatMessageController(ChatMessageService chatMessageService, UserService userService) {
        this.chatMessageService = chatMessageService;
        this.userService = userService;
    }

    @GetMapping("/{username}")
    public ResponseEntity<?> messages(@PathVariable String username, Authentication authentication) {
        Map<String, Object> responseMap = new HashMap<>();
        User senderUser = userService.getUserByUsername(username);
        if(senderUser == null) {
            responseMap.put("error", true);
            responseMap.put("message", "User not found");
            return ResponseEntity.status(404).body(responseMap);
        }
        if(senderUser.getUsername().equals(authentication.getName())) {
            responseMap.put("error", true);
            responseMap.put("message", "You can't view messages sent to yourself");
            return ResponseEntity.status(400).body(responseMap);
        }
        List<ChatMessageDto> chatMessageList = chatMessageService.AllMessageByUsername1AndUsername2(username, authentication.getName());
        responseMap.put("error", false);
        responseMap.put("messages", chatMessageList);
        return ResponseEntity.ok(responseMap);
    }

    @PostMapping("/{username}/send")
    public ResponseEntity<?> send(@PathVariable String username, Authentication authentication, @RequestBody ChatMessageRequest chatMessageRequest) {
        Map<String, Object> responseMap = new HashMap<>();
        User recipientUser = userService.getUserByUsername(username);
        if(recipientUser == null) {
            responseMap.put("error", true);
            responseMap.put("message", "User not found");
            return ResponseEntity.status(404).body(responseMap);
        }
        if(recipientUser.getUsername().equals(authentication.getName())) {
            responseMap.put("error", true);
            responseMap.put("message", "You can't send a message to yourself");
            return ResponseEntity.status(400).body(responseMap);
        }
        if(chatMessageRequest.getContent() == null || chatMessageRequest.getContent().isEmpty()) {
            responseMap.put("error", true);
            responseMap.put("message", "Invalid response body");
            return ResponseEntity.status(400).body(responseMap);
        }
        User senderUser = userService.getUserByUsername(authentication.getName());
        ChatMessage chatMessage = new ChatMessage(senderUser, recipientUser, chatMessageRequest.getContent());
        chatMessageService.save(chatMessage);
        responseMap.put("error", false);
        responseMap.put("message", "Successfully sent content");
        ChatMessageDto chatMessageDto = new ChatMessageDto(chatMessage);
        responseMap.put("send_message", chatMessageDto);
        return ResponseEntity.ok(responseMap);
    }
}
