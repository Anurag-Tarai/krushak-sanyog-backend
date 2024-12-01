package Ecom.Repository;

import Ecom.Model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage,Integer> {
    List<ChatMessage> findByProductId(Integer productId);
}
