package Ecom.Controller;

import Ecom.Model.ChatMessage;
import Ecom.Model.User;
import Ecom.Service.ChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat")
public class    ChatMessageController {

    @Autowired
    private ChatMessageService chatMessageService;

    // Endpoint to fetch messages for a specific product
    @GetMapping("/{productId}")
    public ResponseEntity<List<ChatMessage>> getAllMessages(@PathVariable("productId") Integer productId) {
        List<ChatMessage> messages = chatMessageService.getMessagesByProductId(productId);
        if (messages.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);  // 404 No Content if no messages
        }
        return ResponseEntity.ok(messages);  // 200 OK with the list of messages
    }

    // Endpoint to add a new message
    @PostMapping("/")
    public ResponseEntity<ChatMessage> addMessage(@RequestBody ChatMessage chatMessage) {
        // Get the currently authenticated user from the SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Set the userName to the authenticated user's username
        chatMessage.setUserName(authentication.getName());

        // Set isAdmin to true if the user has the ADMIN role, else false
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
        chatMessage.setIsAdmin(isAdmin);

        // Save the chat message
        ChatMessage savedMessage = chatMessageService.addMessage(chatMessage);

        // Return the saved message with status 201 Created
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMessage);
    }

}
