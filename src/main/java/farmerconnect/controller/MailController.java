package farmerconnect.controller;

// Example in a Controller
import farmerconnect.service.MailClientService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/mail")
public class MailController {

    private final MailClientService mailClientService;

    public MailController(MailClientService mailClientService) {
        this.mailClientService = mailClientService;
    }

    @GetMapping("/test-send-mail")
    public String testMail() {
        String to = "anuragtarai051@gmail.com";
        String subject = "Welcome to Farmer Connect";
        String body = "Hello Hey There";
        return mailClientService.sendEmail(to, subject, body);
    }
}
