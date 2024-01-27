package telran.probes.service;

import java.util.*;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.probes.dto.SensorRange;

@Configuration
@Service
@RequiredArgsConstructor
@Slf4j
public class SensorRangeProviderServiceImpl implements SensorRangeProviderService {
	@Getter
	HashMap<Long, SensorRange> mapRanges = new HashMap<>();
	@Value("${{app.update.message.delimetr:#}")
	String delimeter;
	@Value("${app.update.token.range:range-update}")
	String rangeUpdateToken;
	final SensorRangeProviderConfiguration providerConfiguration;
	final RestTemplate restTemplate; 

	@Override
	public SensorRange getSensorRange(long sensorId) {
		SensorRange range = mapRanges.get(sensorId);
		log.debug("sensorId: {}, range from mapRange: {}", sensorId, range);
		return range == null? getRangeFromService(sensorId) : range;
	}

	@Bean
	Consumer<String> configChangeConsumer() {
		return this::CheckConfigurationUpdate;
	}
	
	private void CheckConfigurationUpdate(String message) {
		log.debug("recieved message: ", message);
		String[] tokens = message.split(delimeter);
		if(tokens[0].equals(rangeUpdateToken)) {
			updateMapRanges(tokens[1]);			
		}		
	}		
		
	private void updateMapRanges(String sensorIdString) {
		Long sensorId = Long.parseLong(sensorIdString);
		log.debug("long Id: {}", sensorIdString);
		if(mapRanges.containsKey(sensorId)) {
			mapRanges.put(sensorId, getRangeFromService(sensorId));
		}		
	}

	private SensorRange getRangeFromService(long sensorId) {
		SensorRange res = null;
		try {
			ResponseEntity<SensorRange> responseEntity =
					restTemplate.exchange(getFullUrl(sensorId), HttpMethod.GET, null, SensorRange.class);
			res = responseEntity.getBody();
			mapRanges.put(sensorId, res);
		} catch (Exception e) {
			log.error("no sensor range provided for sensor {}, reason: {}",
					sensorId, e.getMessage());
			res = getDefaultRange();
			log.warn("Taken default range {} - {}", res.minValue(), res.maxValue());
		}
		log.debug("Range for sensor {} is {}", sensorId, res);
		return res;
	}	
	
	private SensorRange getDefaultRange() {
		
		return new SensorRange(providerConfiguration.getMinDefaultValue(),
				providerConfiguration.getMaxDefaultValue());
	}
	
	private String getFullUrl(long id) {
		String res = String.format("http://%s:%d%s/%d",
				providerConfiguration.getHost(),
				providerConfiguration.getPort(),
				providerConfiguration.getUrl(),
				id);
		log.debug("url:{}", res);
		return res;
	}

}
