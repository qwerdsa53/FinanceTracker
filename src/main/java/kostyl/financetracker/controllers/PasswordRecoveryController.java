package kostyl.financetracker.controllers;

import kostyl.financetracker.email.EmailService;
import kostyl.financetracker.email.TokenService;
import kostyl.financetracker.security.BlacklistService;
import kostyl.financetracker.security.JwtTokenProvider;
import kostyl.financetracker.user.EmailDTO;
import kostyl.financetracker.user.ResetPasswordRequestDTO;
import kostyl.financetracker.user.User;
import kostyl.financetracker.user.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/password")
@Slf4j
@AllArgsConstructor
public class PasswordRecoveryController {
    private final TokenService tokenService;
    private final EmailService emailService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/recovery")
    public ResponseEntity<String> sendPasswordResetEmail(@RequestBody EmailDTO email) {
        User user = userService.findUserByEmail(email.getEmail());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        String token = tokenService.saveRecoveryToken(user.getId());

        String resetLink = "http://localhost:8080/api/v1/auth/reset-password?token=" + token;
        emailService.sendEmail(
                user.getEmail(),
                "Password Reset Request",
                "To reset your password, click the link: " + resetLink
        );

        return ResponseEntity.ok("Password reset link sent to email");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam("token") String token, @RequestBody ResetPasswordRequestDTO request) {
        Long userId = tokenService.getUserIdByRecoveryToken(token);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired token");
        }

        userService.updatePassword(userId, passwordEncoder.encode(request.getPassword()));

        tokenService.deleteRecoveryToken(token);

        return ResponseEntity.ok("Password updated successfully");
    }
}
