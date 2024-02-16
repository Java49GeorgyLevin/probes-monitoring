package telran.probes.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import telran.probes.service.AccountProviderService;
import telran.security.accounting.dto.AccountDto;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountProviderController {	
	final AccountProviderService accountService; 

	@GetMapping("{email}")
	public AccountDto getAccountDto(@PathVariable String email) {
		return accountService.getAccountDto(email);
	}
}
