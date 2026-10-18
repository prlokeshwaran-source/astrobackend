package com.example.astro.service;

import com.example.astro.dto.ChatMessageRequest;
import com.example.astro.dto.CreateChatRoomRequest;
import com.example.astro.model.ChatMessage;
import com.example.astro.model.ChatRoom;
import com.example.astro.repository.AstrologerProfileRepository;
import com.example.astro.repository.ChatMessageRepository;
import com.example.astro.repository.ChatRoomRepository;
import com.example.astro.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Locale;

@Service
public class ChatService {
    private final ChatRoomRepository roomRepository;
    private final ChatMessageRepository messageRepository;
    private final UserRepository userRepository;
    private final AstrologerProfileRepository astrologerRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final String astrologerEmail;

    public ChatService(ChatRoomRepository roomRepository, ChatMessageRepository messageRepository,
                       UserRepository userRepository, AstrologerProfileRepository astrologerRepository,
                       SimpMessagingTemplate messagingTemplate,
                       @Value("${app.auth.astrologer.email}") String astrologerEmail) {
        this.roomRepository = roomRepository;
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.astrologerRepository = astrologerRepository;
        this.messagingTemplate = messagingTemplate;
        this.astrologerEmail = astrologerEmail.trim().toLowerCase(Locale.ROOT);
    }

    public ChatRoom createRoom(CreateChatRoomRequest request) {
        String email = request.userEmail().trim().toLowerCase(Locale.ROOT);
        if (!userRepository.existsByEmail(email)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User account not found");
        }
        if (!astrologerRepository.existsById(request.astrologerProfileId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Astrologer profile not found");
        }
        return roomRepository.save(new ChatRoom(email, request.astrologerProfileId()));
    }

    public List<ChatMessage> history(String roomCode) {
        requireRoom(roomCode);
        return messageRepository.findByRoomCodeOrderBySentAtAsc(roomCode);
    }

    public ChatMessage send(String roomCode, ChatMessageRequest request) {
        ChatRoom room = requireRoom(roomCode);
        String email = request.senderEmail().trim().toLowerCase(Locale.ROOT);
        boolean isUser = room.getUserEmail().equals(email);
        boolean isAstrologer = astrologerEmail.equals(email);
        if (!isUser && !isAstrologer) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Sender is not a participant in this chat");
        }
        ChatMessage saved = messageRepository.save(new ChatMessage(roomCode, email, request.content().trim()));
        messagingTemplate.convertAndSend("/topic/chats/" + roomCode, saved);
        return saved;
    }

    private ChatRoom requireRoom(String roomCode) {
        return roomRepository.findByRoomCode(roomCode)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Chat room not found"));
    }
}
