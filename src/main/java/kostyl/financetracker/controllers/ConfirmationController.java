package kostyl.financetracker.controllers;

import jakarta.persistence.EntityNotFoundException;
import kostyl.financetracker.email.TokenService;
import kostyl.financetracker.user.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("api/v1/auth")
@AllArgsConstructor
@Slf4j
public class ConfirmationController {
    private final TokenService tokenService;
    private final UserService userService;

    @GetMapping("/confirm")
    public ResponseEntity<String> confirmToken(@RequestParam("token") String token) {
        Long userId = tokenService.getUserIdByConfirmToken(token);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid token");
        }
        try {
            userService.activateUser(userId);
            tokenService.deleteConfirmToken(token);
            return ResponseEntity.ok("User activated successfully");
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Invalid user ID format.");
        } catch (EntityNotFoundException | NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        } catch (DataAccessException e) {
            log.error("Database error occurred during user activation: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Database error occurred.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error occurred.");
        }
    }


}
