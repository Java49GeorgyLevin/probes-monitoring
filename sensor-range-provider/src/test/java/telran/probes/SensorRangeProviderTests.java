package telran.probes;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.MongoTransactionManager;

import lombok.RequiredArgsConstructor;
import telran.probes.controller.SensorsController;
import telran.probes.dto.RangeParams;
import telran.probes.dto.Sensor;
import telran.probes.model.SensorDoc;
import telran.probes.repo.SensorRepo;
import telran.probes.service.SensorsService;

@SpringBootTest
@AutoConfigureMockMvc
class SensorRangeProviderTests {
	@Autowired
	SensorsService sensorsService;
	@Autowired
	SensorsController sensorsController;
	@Autowired
	SensorRepo sensorRepo;
	@MockBean
	MongoTransactionManager transactionManager;
	
	Sensor sensor1 = new Sensor(1l, -10, 10, -100f, 100f);
	Sensor sensor2 = new Sensor(2l, -20, 20, -200f, 200f);
	
	@BeforeEach
	void fillRepo() {
	sensorRepo.deleteAll();
	sensorRepo.save(SensorDoc.of(sensor1));
	sensorRepo.save(SensorDoc.of(sensor2));
	}
	
	
	@Test
	void fieldsNotNull() {		
		assertFalse(sensorsService.equals(null));
		assertFalse(sensorsController.equals(null));
		assertFalse(sensorRepo.equals(null));
	}
	

	@Test
	void getRangeParamsTest() {
		RangeParams actual = sensorsService.getRangeParams(sensor1.id());
		System.out.println(String.format("actual: %s", actual));
	}

}
