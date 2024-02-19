package telran.probes.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;
import lombok.Getter;

@Configuration
@Getter
public class AdminConsoleConfiguration {
	@Value("${app.account.provider.host:localhost}")
	String host;
	@Value("${app.account.provider.port:8989}")
	int port;
	@Value("${app.account.provider.url:/accounts}")
	String url;
	
	@Bean
	RestTemplate getRestTemplate() {
		return new RestTemplate();
	}
	
	@Bean
	PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	SecurityFilterChain confugure(HttpSecurity http) throws Exception {
		http.cors(custom -> custom.disable());
		http.csrf(custom -> custom.disable());
		http.authorizeHttpRequests(requests -> requests.anyRequest().authenticated());
		http.httpBasic(Customizer.withDefaults());		
		return http.build();		
	}
}
