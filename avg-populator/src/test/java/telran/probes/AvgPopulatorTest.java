package telran.probes;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.support.GenericMessage;
import telran.probes.dto.ProbeData;
import telran.probes.model.ProbeDataDoc;
import telran.probes.repo.ProbeDataRepo;

@SpringBootTest
@Import(TestChannelBinderConfiguration.class)
class AvgPopulatorTest {
	private static final long SENSOR_ID = 123l;
	private static final float VALUE = 50f;
	static final ProbeData probeData = new ProbeData(SENSOR_ID, VALUE, 0);
	@Autowired
	InputDestination producer;
	String bindingNameConsumer = "consumerProbeData-in-0";
	
	@Autowired
	ProbeDataRepo probeRepo;
	
	@BeforeEach
	void toEmptyRepo() {
		probeRepo.deleteAll();
	}

	@Test
	void properDocumentPopulationTest() throws Exception {
		assertEquals(0, probeRepo.findAll().size());
		producer.send(new GenericMessage<ProbeData>(probeData), bindingNameConsumer);
		List<ProbeDataDoc> docs = probeRepo.findAll();
		assertEquals(1, docs.size());
		ProbeDataDoc probeDoc = docs.get(0);
		assertEquals(SENSOR_ID, probeDoc.getSensorId());
		assertEquals(VALUE, probeDoc.getValue());
		(new ProbeDataDoc(probeData)).equals(probeDoc);
	}

}
