package telran.probes.dto;

import jakarta.validation.constraints.NotNull;

public record Sensor(Long id, @NotNull int pressureMin, @NotNull int pressureMax, 
		@NotNull float temperatureMin, @NotNull float temperatureMax ) {

}
