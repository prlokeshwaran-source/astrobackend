package com.example.astro.controller;

import com.example.astro.dto.ChatMessageRequest;
import com.example.astro.dto.CreateChatRoomRequest;
import com.example.astro.model.ChatMessage;
import com.example.astro.model.ChatRoom;
import com.example.astro.service.ChatService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/chats")
public class ChatController {
    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ChatRoom create(@Valid @RequestBody CreateChatRoomRequest request) {
        return chatService.createRoom(request);
    }

    @GetMapping("/{roomCode}/messages")
    public List<ChatMessage> history(@PathVariable String roomCode) {
        return chatService.history(roomCode);
    }

    @MessageMapping("/chats/{roomCode}")
    public void send(@DestinationVariable String roomCode, @Valid @Payload ChatMessageRequest request) {
        chatService.send(roomCode, request);
    }
}
