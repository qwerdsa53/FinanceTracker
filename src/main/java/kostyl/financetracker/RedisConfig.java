package kostyl.financetracker;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisReactiveAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableAutoConfiguration(exclude = {RedisReactiveAutoConfiguration.class})
public class RedisConfig {
    @Primary
    @Bean
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory jwtConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jwtConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericToStringSerializer<>(Object.class));
        return template;
    }

    @Bean
    public LettuceConnectionFactory jwtConnectionFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setDatabase(0); // bd №0
        return new LettuceConnectionFactory(configuration);
    }

    @Bean
    public LettuceConnectionFactory uuidConnectionFactoryForConfirm() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setDatabase(1); // bd №1
        return new LettuceConnectionFactory(configuration);
    }

    @Bean
    public LettuceConnectionFactory uuidConnectionFactoryForRecovery() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setDatabase(2); // bd №2
        return new LettuceConnectionFactory(configuration);
    }

    @Bean
    public RedisTemplate<String, String> jwtRedisTemplate(LettuceConnectionFactory jwtConnectionFactory) {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(jwtConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        return template;
    }

    @Bean
    public RedisTemplate<String, Long> uuidRedisTemplateForConfirm(LettuceConnectionFactory uuidConnectionFactoryForConfirm) {
        RedisTemplate<String, Long> template = new RedisTemplate<>();
        template.setConnectionFactory(uuidConnectionFactoryForConfirm);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericToStringSerializer<>(Long.class));
        return template;
    }

    @Bean
    public RedisTemplate<String, Long> uuidRedisTemplateForRecovery(LettuceConnectionFactory uuidConnectionFactoryForRecovery) {
        RedisTemplate<String, Long> template = new RedisTemplate<>();
        template.setConnectionFactory(uuidConnectionFactoryForRecovery);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericToStringSerializer<>(Long.class));
        return template;
    }
}

