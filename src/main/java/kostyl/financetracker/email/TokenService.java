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

    private final RedisTemplate<String, Long> redisTemplate;

    public TokenService(@Qualifier("uuidRedisTemplate") RedisTemplate<String, Long> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void saveToken(String token, Long userId) {
        redisTemplate.opsForValue().set("confirmationToken:" + token, userId, Duration.ofHours(24));
    }

    public String generateToken() {
        return UUID.randomUUID().toString();
    }

    public String addTokenToRedis(Long userId) {
        String token = generateToken();
        saveToken(token, userId);
        return token;
    }

    public Long getUserIdByToken(String token) {
        return redisTemplate.opsForValue().get("confirmationToken:" + token);
    }

    public void deleteToken(String token) {
        redisTemplate.delete("confirmationToken:" + token);
    }
}