package telran.probes.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import telran.exceptions.NotFoundException;
import telran.probes.repo.AccountProviderRepo;
import telran.security.accounting.dto.AccountDto;
import telran.security.accounting.model.Account;

@Service
@Slf4j
public class AccountProviderServiceImpl implements AccountProviderService {
	@Autowired
	AccountProviderRepo accountRepo;
	
	@Override
	public AccountDto getAccountDto(String email) {
		Account account = accountRepo.findById(email)
		.orElseThrow(() -> new NotFoundException(String.format("account %s not found", email)));
		log.debug("account {} was found", email);
		
		return account.build();
	}

}
