package telran.probes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.probes.configuration.SensorsConfiguration;
import telran.probes.dto.ProbeData;
import telran.probes.service.SensorsService;

@SpringBootApplication
@RequiredArgsConstructor
@Slf4j
@EnableScheduling
public class SensorsAppl {
	public static final long TIMEOUT = 10000;
	final SensorsService sensorService;
	final StreamBridge streamBridge;
	final SensorsConfiguration sensorConfiguration; 
	
	
	public static void main(String[] args) throws InterruptedException {
		ConfigurableApplicationContext ctx = SpringApplication.run(SensorsAppl.class, args);
		Thread.sleep(TIMEOUT);
		ctx.close();
	}
	
	@Scheduled(fixedDelayString = "${app.sensors.fixed-delay:1000}")
	void sendRandomData() {
		String bindingName = sensorConfiguration.getBindingName();
		ProbeData probeData = sensorService.getRandomProbeData();
		streamBridge.send(bindingName, probeData);
		log.debug("probe data: {} has been sent to {}", probeData, bindingName);
		
	}

}
