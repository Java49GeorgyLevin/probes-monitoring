package telran.probes.model;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.*;
import telran.probes.dto.ProbeData;


@Document(collection="probe_values")
@NoArgsConstructor
@Getter
@ToString
public class ProbeDataDoc {
	Long sensorId; 
	Float value;
	LocalDateTime timestamp;
	
	public ProbeDataDoc(ProbeData probeData) {
		Instant instant = Instant.ofEpochMilli(probeData.timestamp());	    		
	    sensorId = probeData.sensorId();
	    value = probeData.value();
	    timestamp = LocalDateTime.ofInstant(instant, ZoneId.of("UTC"));	
	}

}
