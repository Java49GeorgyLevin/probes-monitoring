package telran.probes;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;

import com.fasterxml.jackson.databind.ObjectMapper;

import telran.probes.dto.ProbeData;
import telran.probes.service.AvgValueService;

@SpringBootTest
@Import(TestChannelBinderConfiguration.class)
class AvgReducerControllerTest {
	private static final long SENSOR_ID = 123l;
	private static final float VALUE = 50f;
	static final ProbeData probeData = new ProbeData(SENSOR_ID, VALUE, 0);
	ObjectMapper mapper = new ObjectMapper();
	@Autowired
	InputDestination producer;
	@Autowired
	OutputDestination consumer;
	@Value("${app.average.binding.name:reduced-out-0}")
	String bindingNameProducer;
	String bindingNameConsumer = "consumerProbeDataReducing-in-0";
	@MockBean
	AvgValueService avgValueService;

	@Test
	void nullAvgValueTest() {
		when(avgValueService.getAvgValue(probeData)).thenReturn(null);
		producer.send(new GenericMessage<ProbeData>(probeData), bindingNameConsumer);
		Message<byte[]> message = consumer.receive(10, bindingNameProducer);
		assertNull(message);		
	}
	
	@Test
	void valueAvgValueTest() throws Exception {
		Long avgValue = 50L;
		when(avgValueService.getAvgValue(probeData)).thenReturn(avgValue);
		producer.send(new GenericMessage<ProbeData>(probeData), bindingNameConsumer);
		Message<byte[]> message = consumer.receive(10, bindingNameProducer);
		assertNotNull(message);
		ProbeData actualAvgValue = mapper.readValue(message.getPayload(), ProbeData.class);
		assertEquals(probeData, actualAvgValue);
	}

}
