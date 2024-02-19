package telran.probes.security;
import java.util.Arrays;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.probes.configuration.AdminConsoleConfiguration;
import telran.security.accounting.dto.AccountDto;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {
	final RestTemplate restTemplate;
	final AdminConsoleConfiguration adminConsoleConfiguration;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		AccountDto accountDto = getAccountDto(username);
		String[] roles = Arrays.stream(accountDto.roles())
				.map(r -> "ROLE_" + r).toArray(String[]::new);
		log.debug("username: {}, password: {}, roles: {}",
				accountDto.email(), accountDto.password(), Arrays.deepToString(roles));	
		return new User(accountDto.email(), accountDto.password(), AuthorityUtils.createAuthorityList(roles));
	}

	private AccountDto getAccountDto(String username) {
		AccountDto res = null;
		try {
			ResponseEntity<?> responseEntity =
			restTemplate.exchange(getFullUrl(username), HttpMethod.GET, null, AccountDto[].class);
			if(!responseEntity.getStatusCode().is2xxSuccessful()) {
				throw new Exception((String) responseEntity.getBody());
			}
			res = (AccountDto) responseEntity.getBody();
		} catch (Exception e) {
			log.error("No account provided for username {}, reason: {}", username, e.getMessage());
		}

		log.debug("Account for username {} is {}", res.email(), res);
		return res;
	}
	
	private String getFullUrl(String username) {
		String res = String.format("http://%s:%d%s/%d",
				adminConsoleConfiguration.getHost(),
				adminConsoleConfiguration.getPort(),
				adminConsoleConfiguration.getUrl(),
				username);
		log.debug("url:{}", res);
		return res;
	}

}
