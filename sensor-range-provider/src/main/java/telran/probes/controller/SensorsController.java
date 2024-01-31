package telran.probes.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import telran.probes.dto.RangeParams;
import telran.probes.service.SensorsService;

@RestController
@RequiredArgsConstructor
public class SensorsController {
	final SensorsService sensorsService;
	
	@GetMapping("range/{id}")
	RangeParams getRangeParams(@PathVariable long id) {
		return sensorsService.getRangeParams(id);
	}
}
