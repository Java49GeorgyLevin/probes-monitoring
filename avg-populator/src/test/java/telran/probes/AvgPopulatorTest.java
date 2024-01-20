package telran.probes;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import telran.probes.dto.ProbeData;
import telran.probes.model.ProbeDataDoc;
import telran.probes.repo.ProbeDataRepo;

@SpringBootTest
@Import(TestChannelBinderConfiguration.class)
class AvgPopulatorTest {
	private static final long SENSOR_ID = 123l;
	private static final float VALUE = 50f;
	static final ProbeData probeData = new ProbeData(SENSOR_ID, VALUE, 0);
	ObjectMapper mapper = new ObjectMapper();
	@Autowired
	InputDestination producer;
	@Autowired
	OutputDestination consumer;
	String bindingNameProducer = "populator-out-0";
	String bindingNameConsumer = "consumerProbeData-in-0";
	
	@Autowired
	ProbeDataRepo probeRepo;

	@Test
	@Transactional(readOnly = false)
	void properDocumentPopulationTest() throws Exception {
		ProbeDataDoc probeDataDoc = ProbeDataDoc.of(probeData);
		probeRepo.save(probeDataDoc);
		assertEquals(1, probeRepo.findAll().size());
		
		producer.send(new GenericMessage<ProbeData>(probeData), bindingNameConsumer);
		Message<byte[]> message = consumer.receive(10, bindingNameProducer);
		assertNotNull(message);
		ProbeData actualProbeData = mapper.readValue(message.getPayload(), ProbeData.class);
		assertEquals(probeData, actualProbeData);
		
	}

}
