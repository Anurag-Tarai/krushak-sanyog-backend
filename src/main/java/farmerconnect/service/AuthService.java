package farmerconnect.service;

import farmerconnect.dto.AuthRequest;
import farmerconnect.dto.AuthResponse;
import farmerconnect.dto.UserRegisterDTO;
import farmerconnect.enums.UserAccountStatus;
import farmerconnect.enums.UserRole;
import farmerconnect.exception.InvalidCredentialException;
import farmerconnect.model.User;
import farmerconnect.repository.UserRepository;
import farmerconnect.security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // ---------------- REGISTER ----------------
    public ResponseEntity<?> register(UserRegisterDTO dto) {

        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email already in use"));
        }

        if (dto.getRole() == null ||
                (!dto.getRole().equals(UserRole.ROLE_FARMER) && !dto.getRole().equals(UserRole.ROLE_BUYER))) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid role type"));
        }

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

    // ---------------- LOGIN ----------------
    public ResponseEntity<?> login(AuthRequest authRequest, HttpServletResponse response) {

        // 1. authenticate (uses your AuthenticationProvider / UserDetailsService)
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getEmail(),
                            authRequest.getPassword())
            );
        } catch (Exception ex) {
            // Authentication failed
            throw new InvalidCredentialException("Invalid email or password");
        }

        // 2. load user
        User user = userRepository.findByEmail(authRequest.getEmail())
                .orElseThrow(() -> new InvalidCredentialException("Invalid email or password"));

        // 3. role check
        if (!user.getRole().equals(authRequest.getRole())) {
            return ResponseEntity.status(403)
                    .body(Map.of("error", "Access denied: You are registered as " + user.getRole()));
        }

        // 4. generate JWT
        String token = jwtService.generateToken(user.getEmail(), user.getRole().name());

        // 5. set HttpOnly cookie
        ResponseCookie jwtCookie = ResponseCookie.from("jwt", token)
                .httpOnly(true)
                .secure(Boolean.parseBoolean(System.getProperty("app.cookie.secure", "false"))) // switch in prod
                .path("/")
                .maxAge(Duration.ofDays(1))
                .sameSite("Lax") // consider SameSite=None for cross-site + secure=true
                .build();
        response.addHeader("Set-Cookie", jwtCookie.toString());

        // 7. prepare response WITHOUT token (HttpOnly cookie used instead)
        AuthResponse resp = new AuthResponse();

        resp.setRole(user.getRole());
        resp.setName(user.getFirstName() + " " + user.getLastName());


        return ResponseEntity.ok(resp);
    }

    // ---------------- LOGOUT ----------------
    public ResponseEntity<?> logout(HttpServletResponse response) {
        ResponseCookie clearJwt = ResponseCookie.from("jwt", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .sameSite("Lax")
                .build();
        response.addHeader("Set-Cookie", clearJwt.toString());

        ResponseCookie clearXsrf = ResponseCookie.from("XSRF-TOKEN", "")
                .httpOnly(false)
                .secure(false)
                .path("/")
                .maxAge(0)
                .sameSite("Lax")
                .build();
        response.addHeader("Set-Cookie", clearXsrf.toString());

        return ResponseEntity.ok(Map.of("message", "Logged out"));
    }
}
