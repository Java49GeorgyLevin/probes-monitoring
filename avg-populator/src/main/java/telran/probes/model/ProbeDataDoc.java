package telran.probes.model;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import telran.probes.dto.ProbeData;

@Document(collection="probe_values")
@AllArgsConstructor
public class ProbeDataDoc {
	@Id
	Long sensorId; 
	Float value;
	LocalDateTime timestamp;
	
	public static ProbeDataDoc of(ProbeData probeData) {
	    Instant instant = Instant.ofEpochMilli(probeData.timestamp());
	    LocalDateTime localDateTime = 
	    		LocalDateTime.ofInstant(instant, ZoneId.of("UTC"));
		return new ProbeDataDoc(probeData.sensorId(), probeData.value(), localDateTime);
	}
	
}