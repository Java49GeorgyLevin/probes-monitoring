package telran.probes;

import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.probes.dto.ProbeData;

@SpringBootApplication
@RequiredArgsConstructor
@Slf4j
public class AvgPopulatorApp {
	final StreamBridge streamBridge;
	@Value("${app.populator.binding.name:populator-out-0}")
	String deviationBindingName;

	public static void main(String[] args) {
		SpringApplication.run(AvgPopulatorApp.class, args);

	}
	
    @Bean
    public Consumer<ProbeData> consumerProbeData() {
		return this::ConsumeMethod;		
	};
	
	void ConsumeMethod(ProbeData probeData) {
		log.trace("resived probe: {}", probeData);

		streamBridge.send(deviationBindingName, probeData);	
		log.debug("deviation data {} sent to {}", probeData, deviationBindingName);
		}

}
