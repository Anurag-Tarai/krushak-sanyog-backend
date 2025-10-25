//package farmerconnect.controller;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.web.bind.annotation.*;
//
//import farmerconnect.model.User;
//import farmerconnect.dto.AdminDTO;
//import farmerconnect.dto.UserDTO;
//import farmerconnect.service.UserService;
//
//@RestController
//@RequestMapping("/ecom/admin")
//@RequiredArgsConstructor
//public class AdminController {
//
//    private final PasswordEncoder passwordEncoder;
//
//    @Autowired
//    UserService userService;
//
//    @PostMapping("/signup")
//    public ResponseEntity<User> addUser(@RequestBody AdminDTO user) {
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        User addedUser = userService.addUserAdmin(user);
//        return ResponseEntity.ok(addedUser);
//    }
//
//    @PutMapping("/updatepassword/{adminId}")
//    public ResponseEntity<User> updateUserPassword(@PathVariable("adminId") Integer customerId, @RequestBody UserDTO userdto) {
//        User updatedUser = userService.changePassword(customerId, userdto);
//        return ResponseEntity.ok(updatedUser);
//    }
//
//
//
//}
