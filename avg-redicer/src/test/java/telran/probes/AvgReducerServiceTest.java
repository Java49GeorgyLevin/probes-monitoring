package telran.probes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import lombok.extern.slf4j.Slf4j;
import telran.probes.dto.ProbeData;
import telran.probes.model.ProbesList;
import telran.probes.repo.ProbesListRepo;
import telran.probes.service.AvgValueService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.*;

@SpringBootTest
@Slf4j
public class AvgReducerServiceTest {
	List<Float> VALUES_NO_AVG;
	List<Float> VALUES_AVG;
	final long SENSOR_ID_NO_REDIS_RECORD = 123l;
	final long SENSOR_ID_NO_AVG = 124l;
	final long SENSOR_ID_AVG = 125l;
	final float VALUE = 100f;
	final ProbeData PROBE_NO_REDIS_RECORD = new ProbeData(SENSOR_ID_NO_REDIS_RECORD, VALUE, 0);
	final ProbeData PROBE_NO_AVG = new ProbeData(SENSOR_ID_NO_AVG, VALUE, 0);
	final ProbeData PROBE_AVG = new ProbeData(SENSOR_ID_AVG, VALUE, 0);
	final ProbesList PROBES_LIST_NO_AVG = new ProbesList(SENSOR_ID_NO_AVG);
	final ProbesList PROBES_LIST_AVG = new ProbesList(SENSOR_ID_AVG);
	final ProbesList PROBES_LIST_NO_RECORD = new ProbesList(SENSOR_ID_NO_REDIS_RECORD);
	Map<Long, ProbesList> mapRedis = new HashMap<>();
	@Autowired
	AvgValueService avgValueService;	
	@MockBean
	ProbesListRepo probesListRepo;
	@BeforeEach
	void setUp() {
		VALUES_NO_AVG = PROBES_LIST_NO_AVG.getValues();
		VALUES_AVG = PROBES_LIST_AVG.getValues();
//to down		VALUES_AVG.add(VALUE);
		mapRedis.put(SENSOR_ID_NO_AVG, PROBES_LIST_NO_AVG);
		mapRedis.put(SENSOR_ID_AVG, PROBES_LIST_AVG);
		VALUES_AVG.add(VALUE);
	}
	@Test
	void testNoRedisRecord() {
		when(probesListRepo.findById(SENSOR_ID_NO_REDIS_RECORD)).thenReturn(Optional.ofNullable(null));
		when(probesListRepo.save(PROBES_LIST_NO_RECORD)).thenAnswer(new Answer<ProbesList>() {

			@Override
			public ProbesList answer(InvocationOnMock invocation) throws Throwable {
				mapRedis.put(SENSOR_ID_NO_REDIS_RECORD, invocation.getArgument(0));
				return invocation.getArgument(0);
			}
		});
		Long res = avgValueService.getAvgValue(PROBE_NO_REDIS_RECORD);
		assertNull(res);
		ProbesList probesList = mapRedis.get(SENSOR_ID_NO_REDIS_RECORD);
		assertNotNull(probesList);
		assertEquals(VALUE, probesList.getValues().get(0));
	}
	@Test
	void testNoAvgValue() {
		when(probesListRepo.findById(SENSOR_ID_NO_AVG)).thenReturn(Optional.of(PROBES_LIST_NO_AVG));
		when(probesListRepo.save(PROBES_LIST_NO_AVG)).thenAnswer(new Answer<ProbesList>() {

			@Override
			public ProbesList answer(InvocationOnMock invocation) throws Throwable {
				mapRedis.put(SENSOR_ID_NO_AVG, invocation.getArgument(0));
				return invocation.getArgument(0);
			}
		});
		Long res = avgValueService.getAvgValue(PROBE_NO_AVG);
		assertNull(res);
		ProbesList probesList = mapRedis.get(SENSOR_ID_NO_AVG);
		assertNotNull(probesList);
		assertEquals(VALUE, probesList.getValues().get(0));	
		
	}
	@Test
	void testAvgValue() {
log.debug("in test PROBES_LIST_AVG: {}", PROBES_LIST_AVG);
log.debug("in test PROBE_AVG: {}", PROBE_AVG);	
		when(probesListRepo.findById(SENSOR_ID_AVG)).thenReturn(Optional.of(PROBES_LIST_AVG));

//*
		when(probesListRepo.save(PROBES_LIST_AVG)).thenAnswer(invocation -> {
log.debug("Hello, I'm in the Answer");
			mapRedis.put(SENSOR_ID_AVG, invocation.getArgument(0));
				return invocation.getArgument(0);			
		});
//*		
		assertEquals(VALUE, mapRedis.get(SENSOR_ID_AVG).getValues().get(0));
		
		Long res = avgValueService.getAvgValue(PROBE_AVG);
log.debug("in test after RES PROBES_LIST_AVG: {}", PROBES_LIST_AVG);
log.debug("in test after RES mapRedis.get: {}", mapRedis.get(SENSOR_ID_AVG).getValues());		
		assertNotNull(res);
		assertEquals(VALUE, res.floatValue());
		ProbesList probesList = mapRedis.get(SENSOR_ID_AVG);		
		assertNotNull(probesList);
		assertTrue(probesList.getValues().isEmpty());


		
	}
}
