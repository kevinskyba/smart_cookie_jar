package de.cookiejar.cookiejar.model;

import java.time.Instant;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter 
@NoArgsConstructor
public class CookieJarWeightVO {

	private double weight;
	
	private double fillLevel;
	
	private Instant timeStamp;
}
