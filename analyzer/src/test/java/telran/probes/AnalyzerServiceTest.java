package telran.probes;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.HashMap;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.web.client.RestTemplate;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import telran.probes.dto.SensorRange;
import telran.probes.service.SensorRangeProviderConfiguration;
import telran.probes.service.SensorRangeProviderService;

@SpringBootTest
@Slf4j
@Import(TestChannelBinderConfiguration.class)
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
public class AnalyzerServiceTest {
	@Autowired
	InputDestination producer;
	String bindingNameConsumer = "configChangeConsumer-in-0";
	@MockBean
	RestTemplate restTemplate;
	@Autowired
	SensorRangeProviderService providerService;
	@Autowired	
	SensorRangeProviderConfiguration providerConfiguration;
	@MockBean
	HashMap<Long, SensorRange> mapRanges;
	static Float defaultMin;
	static Float defaultMax;
	static SensorRange defaultRange;
	
	static final long SENSOR_ID = 123l;
	static final long SENSOR_ID_NOT_EXISTS = 124l;
	private static final float MIN_VALUE = 10;
	private static final float MAX_VALUE = 20;
	private static final float MIN_VALUE_UPD = 15;
	private static final float MAX_VALUE_UPD = 25;
	static final SensorRange SENSOR_RANGE = new SensorRange(MIN_VALUE, MAX_VALUE); 
	
	@PostConstruct 
	void setup() {
		defaultMin = providerConfiguration.getMinDefaultValue();
		defaultMax = providerConfiguration.getMaxDefaultValue();
		defaultRange = new SensorRange(defaultMin, defaultMax);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	@Order(1)
	void normalFlowWithNoMapData() {
		ResponseEntity<SensorRange> responseEntity = new ResponseEntity<>(SENSOR_RANGE, HttpStatus.OK);
		when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(), any(Class.class)))
		.thenReturn(responseEntity);
		SensorRange actual = providerService.getSensorRange(SENSOR_ID);
		assertEquals(SENSOR_RANGE, actual);		
	}
	@Test
	@Order(2)
	void normalFlowWithMapData() {
		SensorRange actual = providerService.getSensorRange(SENSOR_ID);
		assertEquals(SENSOR_RANGE, actual);
	}
	@SuppressWarnings("unchecked")
	@Test
	@Order(3)
	void flowNotFound() {
		String sensorNotFound = "Sensor Not Found";
		when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(), any(Class.class)))
		.thenThrow(new RuntimeException(sensorNotFound));
		SensorRange actual = providerService.getSensorRange(SENSOR_ID_NOT_EXISTS);		
		assertEquals(defaultRange, actual);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	@Order(4)
	void remoteServiceNotAvalible() {
		String serviceNotAvalible = "Service Not Avalible";
//?? why doesn't work when(mapRanges.get(SENSOR_ID)).thenReturn(null)
		when(mapRanges.get(any())).thenReturn(null);
		
		when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(), any(Class.class)))
		.thenThrow(new RuntimeException(serviceNotAvalible));
		SensorRange actual = providerService.getSensorRange(SENSOR_ID_NOT_EXISTS);
		assertEquals(defaultRange, actual);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	@Order(5)
	void normalFlowSensorExistingRangeUpdated() {
		SensorRange updRange = new SensorRange(MIN_VALUE_UPD, MAX_VALUE_UPD);
		ResponseEntity<SensorRange> responseEntity = new ResponseEntity<>(updRange, HttpStatus.OK);
		String updateString = "range-update#" + Long.toString(SENSOR_ID);
		when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(), any(Class.class)))
		.thenReturn(responseEntity);
				
		producer.send(new GenericMessage<String>(updateString), bindingNameConsumer);
		SensorRange actual = providerService.getSensorRange(SENSOR_ID);
		assertEquals(updRange, actual);		
	}
	
	@SuppressWarnings("unchecked")
	@Test
	@Order(6)
	void normalFlowSensorNotExistingRangeUpdated() {
		String updateString = "range-update#" + Long.toString(SENSOR_ID_NOT_EXISTS);
		log.debug("string to send: {}", updateString);
		producer.send(new GenericMessage<String>(updateString), bindingNameConsumer);
		
		String sensorNotFound = "Sensor Not Found";
		when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(), any(Class.class)))
		.thenThrow(new RuntimeException(sensorNotFound));
		
		SensorRange actual = providerService.getSensorRange(SENSOR_ID_NOT_EXISTS);
		assertEquals(defaultRange, actual);		
	}	
	
	@Test
	@Order(7)
	void normalFlowEmailUpdated() {
		//? email address 
		
	}
	
	
}
