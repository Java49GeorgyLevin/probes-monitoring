package telran.probes.service;

import telran.security.accounting.dto.AccountDto;

public interface AccountProviderService {
	AccountDto getAccountDto(String email);
	
}
