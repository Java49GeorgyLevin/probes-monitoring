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
import telran.probes.service.AvgValueService;

@SpringBootApplication
@RequiredArgsConstructor
@Slf4j
public class AvgReducerAppl {
final AvgValueService avgValueService;
final StreamBridge streamBridge;
@Value("${app.average.binding.name:reduced-out-0}")
String avgValueBindingName;
	public static void main(String[] args) {
		SpringApplication.run(AvgReducerAppl.class, args);

	}
	
	@Bean
	Consumer<ProbeData> consumerProbeDataReducing() { 
		return this::ConsumerMethod;
	}
	
	private void ConsumerMethod(ProbeData probeData) {
		log.trace("recived probeData: {}", probeData);
		long sensorId = probeData.sensorId();
		Long avgValue = avgValueService.getAvgValue(probeData);
		log.debug("avgValue: {}", avgValue);
		if(!avgValue.equals(null)) {
			log.trace("for patient {} avg value is {}", sensorId, avgValue);
			streamBridge.send(avgValueBindingName, new ProbeData(sensorId, avgValue, System.currentTimeMillis()));
			log.debug("average value {} sent to {}", avgValue, avgValueBindingName);
		} else {
			log.trace("for patient {} no avg value yet", sensorId);
		}
		
		
	}

}
