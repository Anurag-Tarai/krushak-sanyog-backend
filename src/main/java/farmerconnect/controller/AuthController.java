package farmerconnect.controller;

import farmerconnect.dto.AuthRequest;
import farmerconnect.dto.AuthResponse;
import farmerconnect.dto.UserRegisterDTO;
import farmerconnect.enums.UserAccountStatus;
import farmerconnect.enums.UserRole;
import farmerconnect.exception.InvalidCredentialException;
import farmerconnect.model.User;
import farmerconnect.repository.UserRepository;
import farmerconnect.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // -------------------- REGISTER --------------------
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRegisterDTO dto) {

        // 1️⃣ Check if email already registered
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email already in use");
        }

        // 2️⃣ Validate role type (only FARMER or USER)
        if (dto.getRole() == null ||
                (!dto.getRole().equals(UserRole.ROLE_FARMER) && !dto.getRole().equals(UserRole.ROLE_BUYER))) {
            return ResponseEntity.badRequest().body("Invalid role type");
        }

        // 3️⃣ Create user
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setRole(dto.getRole());
        user.setUserAccountStatus(UserAccountStatus.ACTIVE);
        user.setRegisterTime(LocalDateTime.now());

        userRepository.save(user);

        return ResponseEntity.ok(Map.of(
                "message", "Registered successfully as " + dto.getRole().name(),
                "role", dto.getRole()
        ));
    }

    // -------------------- LOGIN --------------------
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {

        // 1️⃣ Find user
        User user = userRepository.findByEmail(authRequest.getEmail())
                .orElseThrow(() -> new InvalidCredentialException("Invalid email or password"));

        // 2️⃣ Check password
        if (!passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }

        if (!user.getRole().equals(authRequest.getRole())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access denied: You are registered as " + user.getRole());
        }
//        // 3️⃣ Validate role match (farmer vs buyer form)
//        if (!user.getRole().name().equals(authRequest.getRole())) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN)
//                    .body("Access denied: You are registered as " + user.getRole());
//        }

        // 4️⃣ Generate JWT
        String token = jwtService.generateToken(user.getEmail(), user.getRole().name());

        AuthResponse response = new AuthResponse();
        response.setToken(token);
        response.setRole(user.getRole());
        response.setName(user.getFirstName() + " " + user.getLastName());
        response.setUserId(user.getUserId());

        return ResponseEntity.ok(response);
    }
}

