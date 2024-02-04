package telran.probes.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import telran.probes.configuration.SensorsConfiguration;
import telran.probes.model.SensorEmailsDoc;
import telran.probes.model.SensorRangeDoc;
import telran.probes.repo.SensorEmailsRepo;
import telran.probes.repo.SensorRangesRepo;

@Component
public class SensorsDataPopulation {
	@Autowired
	private SensorRangesRepo sensorRangesRepo;
	@Autowired
	private SensorEmailsRepo sensorEmailsRepo;
	@Autowired
	private SensorsConfiguration sensorsConfiguration;
	
	@PostConstruct
	void initialPopulation() {
		populateSensorRanges();
		populateSensorEmails();		
	}

	private void populateSensorEmails() {
		List<SensorEmailsDoc> sensorEmailsDocs = sensorsConfiguration.getSensorsDataMap().entrySet().stream()
				.map(e -> new SensorEmailsDoc(e.getKey(), e.getValue().emails()))
				.toList();		
		sensorEmailsRepo.saveAll(sensorEmailsDocs);		
	}

	private void populateSensorRanges() {
		List<SensorRangeDoc> sensorRangesDocs = sensorsConfiguration.getSensorsDataMap().entrySet().stream()
				.map(e -> new SensorRangeDoc(e.getKey(), e.getValue().minValue(), e.getValue().maxValue()))
				.toList();		
		sensorRangesRepo.saveAll(sensorRangesDocs);		
	}

}
