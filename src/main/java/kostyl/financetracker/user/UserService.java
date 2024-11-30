package kostyl.financetracker.user;

import jakarta.transaction.Transactional;
import kostyl.financetracker.email.EmailService;
import kostyl.financetracker.email.TokenService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @Async
    public void registerUser(UserRegistrationDto userDto) {
        System.out.println("Executing in thread: " + Thread.currentThread().getName());
        User user = User.builder()
                .username(userDto.getUsername())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .email(userDto.getEmail())
                .role("USER")
                .enabled(false)
                .build();
        try {
            userRepository.save(user);
            String token = tokenService.addTokenToRedis(user.getId());
            emailService.sendEmail(
                    user.getEmail(),
                    "Welcome to TrackMyFinance!",
                    "Thank you for signing up. Please confirm your email: " +
                            "http://localhost:8080/api/v1/auth/confirm?token=" + token
            );
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Username already exists");
        } catch (Exception e) {
            throw new RuntimeException("Error during registration process: " + e.getMessage(), e);
        }
    }

    public User getUserById(Long id){
        return userRepository.findById(id).orElseThrow();
    }

    public void updateUser(UserDTO userDTO, Long userId){
        User user = userRepository.findById(userId).orElseThrow(
                () -> new RuntimeException("User not found")
        );
        if(!Objects.equals(userDTO.getEmail(), user.getEmail())){
            user.setEmail(userDTO.getEmail());
            user.setEnabled(false);
        }
        user.setUsername(userDTO.getUsername());
        userRepository.save(user);
    }

    public void deleteUser(Long id){
        userRepository.deleteById(id);
    }

    @Transactional
    public void activateUser(long userId) {
        userRepository.enableUserById(userId);
    }
}
