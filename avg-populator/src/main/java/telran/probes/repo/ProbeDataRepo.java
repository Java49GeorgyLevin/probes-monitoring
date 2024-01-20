package telran.probes.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import telran.probes.model.ProbeDataDoc;

public interface ProbeDataRepo extends MongoRepository<ProbeDataDoc, Long> {

}
