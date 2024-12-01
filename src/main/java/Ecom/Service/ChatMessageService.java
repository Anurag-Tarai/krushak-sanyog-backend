package Ecom.Service;

import Ecom.Model.ChatMessage;

import java.util.List;

public interface ChatMessageService {
    List<ChatMessage> getMessagesByProductId(Integer productId);
    ChatMessage addMessage(ChatMessage chatMessage);  // Method to add a new message
}
