package farmerconnect.service.impl;

import farmerconnect.model.ChatMessage;
import farmerconnect.repository.ChatMessageRepository;
import farmerconnect.service.ChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatMessageServiceImpl implements ChatMessageService {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Override
    public List<ChatMessage> getMessagesByProductId(Integer productId) {
        return chatMessageRepository.findByProductId(productId);  // Fetch messages by productId
    }

    @Override
    public ChatMessage addMessage(ChatMessage chatMessage) {
        return chatMessageRepository.save(chatMessage);  // Save new message to the database
    }
}
