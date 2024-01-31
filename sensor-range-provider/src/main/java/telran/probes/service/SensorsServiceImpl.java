package telran.probes.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.probes.dto.RangeParams;
import telran.probes.repo.SensorRepo;
@Service
@Slf4j
@RequiredArgsConstructor
public class SensorsServiceImpl implements SensorsService {
	final SensorRepo sensorRepo;

	@Override
	public RangeParams getRangeParams(long id) {
		RangeParams res = sensorRepo.findRangeParams(id);
		log.debug("Pmin:{}, Pmax:{}, Tmin:{}, Tmax:{}", res.getPressureMin(), res.getPressureMax(), res.getTemperatureMin(), res.getTemperatureMax());
		return res;
	}

}
