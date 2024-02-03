package telran.probes.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.probes.dto.SensorRange;
import telran.probes.service.SensorRangeProviderService;

@RestController
@RequiredArgsConstructor
@Slf4j
public class SensorRangesProviderController {
	final SensorRangeProviderService service;
	
	@GetMapping("${app.sensor.range.provider.url}" + "/{id}")
	SensorRange getSensorRange(@PathVariable long id) {
		SensorRange sensorRange = service.getSensorRange(id);		
		log.debug("sensor range received is {}", sensorRange);
		return sensorRange;
	}
}
