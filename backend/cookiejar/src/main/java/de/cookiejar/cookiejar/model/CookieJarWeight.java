package de.cookiejar.cookiejar.model;

import java.time.Instant;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter 
@Setter 
@NoArgsConstructor
public class CookieJarWeight {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private double weight;
	
	private Instant timeStamp;

	public CookieJarWeight(double weight) {
		this.weight = weight;
		this.timeStamp = Instant.now();
	}

	@Override
	public String toString() {
		return String.format("CookieJahrWeight [id=%d, weight='%s']", id, weight);
	}
}
