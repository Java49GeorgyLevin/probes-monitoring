package telran.probes.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import telran.probes.dto.Sensor;
@AllArgsConstructor
@Document(collection = "sensors")
@Getter
public class SensorDoc {
	@Id
	Long id;
	int pressureMin; 
	int pressureMax; 
	float temperatureMin; 
	float temperatureMax;
	
	public static SensorDoc of(Sensor sensor) {
		return new SensorDoc(sensor.id(), sensor.pressureMin(), sensor.pressureMax(), sensor.temperatureMin(), sensor.temperatureMax()); 
	}
	
	public Sensor bild() {
		return new Sensor(id, pressureMin, pressureMax, temperatureMax, temperatureMax);
	}
}
