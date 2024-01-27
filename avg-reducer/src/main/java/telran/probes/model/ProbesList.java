package telran.probes.model;

import java.util.*;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.*;

@RedisHash
@Getter

@ToString
@NoArgsConstructor
@EqualsAndHashCode(of="sensorId")
public class ProbesList {
	@Id
	long sensorId;
	List<Float> values;
	public ProbesList(long sensorId) {
		this.sensorId = sensorId;
		values = new ArrayList<>();
	}
	
}
