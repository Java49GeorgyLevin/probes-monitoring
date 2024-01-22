package telran.probes;

import java.util.function.Consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.probes.dto.ProbeData;
import telran.probes.model.ProbeDataDoc;
import telran.probes.repo.ProbeDataRepo;

@SpringBootApplication
@RequiredArgsConstructor
@Slf4j
public class AvgPopulatorApp {
	final ProbeDataRepo probeRepo;

	public static void main(String[] args) {
		SpringApplication.run(AvgPopulatorApp.class, args);

	}
	
    @Bean
    Consumer<ProbeData> consumerProbeData() {
		return this::ConsumeMethod;		
	};
	
	void ConsumeMethod(ProbeData probeData) {
		log.trace("recieved probeData: {}", probeData);
		ProbeDataDoc probeDoc = ProbeDataDoc.of(probeData);
		probeRepo.save(probeDoc);
		log.debug("probeDataDoc {} saved", probeDoc);
		}

}
