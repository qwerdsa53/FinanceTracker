package kostyl.financetracker.user;

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
    private final BCryptPasswordEncoder passwordEncoder;

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
}
