package telran.probes.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import telran.probes.dto.RangeParams;
import telran.probes.model.SensorDoc;

public interface SensorRepo extends MongoRepository<SensorDoc, Long> {
	@Query(value = "{id: ?0}", fields = "pressureMin:1, pressureMax:1, temperatureMin:1, temperatureMax:1, id:0")	
	RangeParams findRangeParams(long id);
}
