package de.cookiejar.cookiejar.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ConfigurationVO {
	private double tolerance;
	private double emptyWeight;
	private double maximumWeight;
}
