package farmerconnect.service;

import farmerconnect.model.ChatMessage;

import java.util.List;

public interface ChatMessageService {
    List<ChatMessage> getMessagesByProductId(Integer productId);
    ChatMessage addMessage(ChatMessage chatMessage);  // Method to add a new message
}
