package kostyl.financetracker.controllers;

import kostyl.financetracker.security.CustomUserDetails;
import kostyl.financetracker.user.User;
import kostyl.financetracker.user.UserDTO;
import kostyl.financetracker.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("api/v1/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public UserDTO getUser() {
        Long userId = getUserId();
        User user = userService.getUserById(userId);
        return new UserDTO(user.getUsername(), user.getEmail());
    }

    @PutMapping
    public ResponseEntity<String> updateUser(@RequestBody UserDTO userDTO) {
        try {
            userService.updateUser(userDTO, getUserId());
            return ResponseEntity.status(HttpStatus.OK).body("User updated");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid input: " + e.getMessage());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating User: " + e.getMessage());
        }
    }

    @DeleteMapping
    public ResponseEntity<String> deleteUser() {
        try {
            userService.deleteUser(getUserId());
            return ResponseEntity.status(HttpStatus.OK).body("User deleted");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid input: " + e.getMessage());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting User: " + e.getMessage());
        }
    }

    private Long getUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getPrincipal() instanceof CustomUserDetails userDetails) {
            return userDetails.getId();
        } else {
            throw new IllegalStateException("Authentication principal is not an instance of CustomUserDetails");
        }
    }
}