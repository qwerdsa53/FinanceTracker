package kostyl.financetracker.email;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

@Service
@Slf4j
public class TokenService {

    private final RedisTemplate<String, Long> redisTemplateForConfirm;
    private final RedisTemplate<String, Long> redisTemplateForRecovery;

    public TokenService(
            @Qualifier("uuidRedisTemplateForConfirm") RedisTemplate<String, Long> redisTemplateForConfirm,
            @Qualifier("uuidRedisTemplateForRecovery") RedisTemplate<String, Long> redisTemplateForRecovery
    ) {
        this.redisTemplateForConfirm = redisTemplateForConfirm;
        this.redisTemplateForRecovery = redisTemplateForRecovery;
    }

    public String saveConfirmToken(Long userId) {
        String token = generateToken();
        redisTemplateForConfirm.opsForValue().set("confirmationToken:" + token, userId, Duration.ofHours(24));
        return token;
    }

    public Long getUserIdByConfirmToken(String token) {
        return redisTemplateForConfirm.opsForValue().get("confirmationToken:" + token);
    }

    public void deleteConfirmToken(String token) {
        redisTemplateForConfirm.delete("confirmationToken:" + token);
    }

    public String saveRecoveryToken(Long userId) {
        String token = generateToken();
        redisTemplateForRecovery.opsForValue().set("recoveryToken:" + token, userId, Duration.ofHours(24));
        return token;
    }

    public Long getUserIdByRecoveryToken(String token) {
        return redisTemplateForRecovery.opsForValue().get("recoveryToken:" + token);
    }

    public void deleteRecoveryToken(String token) {
        redisTemplateForRecovery.delete("recoveryToken:" + token);
    }

    public String generateToken() {
        return UUID.randomUUID().toString();
    }
}