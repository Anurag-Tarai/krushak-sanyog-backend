package Ecom.Controller;


import Ecom.Model.User;
import Ecom.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/ecom/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/get-user/{id}")
    public Optional<User> getUserById(@PathVariable Integer id){
        return userRepository.findById(id);
    }
}
