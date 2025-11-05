package farmerconnect.controller;


import farmerconnect.model.User;
import farmerconnect.repository.UserRepository;
import farmerconnect.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private  UserRepository userRepository;

    @GetMapping
    public Optional<User> getUser() throws UserPrincipalNotFoundException {
       UserDetails userDetails = userService.getCurrentAuthUser();
       if(userDetails==null) throw new UserPrincipalNotFoundException("User Details Not found");

    return userRepository.findByEmail(userDetails.getUsername());

    }
    @GetMapping("/{id}")
    public Optional<User> getUserById(@PathVariable Integer id) throws UserPrincipalNotFoundException {
        UserDetails userDetails = userService.getCurrentAuthUser();
        return userRepository.findById(id);

    }
}
