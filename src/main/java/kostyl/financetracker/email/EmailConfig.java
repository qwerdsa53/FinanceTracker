package kostyl.financetracker.email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmailConfig {

    @Value("${YANDEX_EMAIL}")
    private String yandexEmail;

    @Bean
    public String trackMyFinanceEmail() {
        return yandexEmail;
    }
}
