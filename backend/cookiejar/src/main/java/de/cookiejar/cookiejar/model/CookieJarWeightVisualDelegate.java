package de.cookiejar.cookiejar.model;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Component
public class CookieJarWeightVisualDelegate {
	
	@Value("${cj.emptyweight:-6615338.5}")
	private double emptyWeight;

	@Value("${cj.weighttolerance:1000000.0}")
	private double tolerance;
	
	@Value("${cj.maximumweight:1250.0}")
	private double maximumWeight;
	
	@Deprecated
	private static final int TIME_LIMIT_IN_SECONDS_UNTIL_EXCEPTION = 1200;
	
	public CookieJarWeightVO getLastObject(CookieJarWeightRepository repository) {
		CookieJarWeightVO returnValue = mapCookieJarWeight(repository.findTop3ByOrderByIdDesc().stream().findFirst().orElse(null));
//		if (returnValue.getTimeStamp() == null) {
//			return returnValue;
//		}
//		if (!cookieJarAvailableInProperTime(returnValue.getTimeStamp())) {
//			throw new TimestampTooOldException(returnValue.getTimeStamp());
//		}
		return returnValue;
	}
	
	public CookieJarWeightVO getObjectById(CookieJarWeightRepository repository, String id) {
		return mapCookieJarWeight(repository.findById(Long.valueOf(id)).orElse(null));
	}
	
	public List<CookieJarWeightVO> getLast500Objects(CookieJarWeightRepository repository) {
		return mapCookieJarWeightAsList(repository.findTop500ByOrderByIdDesc());
	}

	@Deprecated
	public boolean cookieJarAvailableInProperTime(Instant lastSavedTime) {
		if (lastSavedTime == null) {
			return true;
		}
		return (Duration.between(lastSavedTime, Instant.now()).getSeconds() <= TIME_LIMIT_IN_SECONDS_UNTIL_EXCEPTION);
	}

	public double getFillLevelPercentage(double weight) {
		if (weight <= 0) {
			return 0.0;
		}
		if (weight >= this.maximumWeight) {
			return 100.0;
		}
		return weight * 100.0 / this.maximumWeight;
	}

	public CookieJarWeightVO mapCookieJarWeight(CookieJarWeight input) {
		if (input == null) {
			return new CookieJarWeightVO();
		}
		CookieJarWeightVO output = new CookieJarWeightVO();
		// if needed, remove empty weight
		if (input.getWeight() < (this.emptyWeight + this.tolerance)) {
			output.setWeight(input.getWeight() - this.emptyWeight);
		} else {
			output.setWeight(input.getWeight());
		}
		if (output.getWeight() < 0.0) {
			output.setWeight(0.0);
		}
		output.setTimeStamp(input.getTimeStamp());
		output.setFillLevel(getFillLevelPercentage(output.getWeight()));
		return output;
	}

	public List<CookieJarWeightVO> mapCookieJarWeightAsList(List<CookieJarWeight> inputList) {
		if (inputList == null || inputList.isEmpty()) {
			return new ArrayList<CookieJarWeightVO>();
		}
		return inputList.stream().map(cookieJarWeight -> mapCookieJarWeight(cookieJarWeight))
				.collect(Collectors.toList());
	}
	
	public ConfigurationVO getConfiguration() {
		ConfigurationVO configuration = new ConfigurationVO();
		configuration.setEmptyWeight(this.emptyWeight);
		configuration.setMaximumWeight(this.maximumWeight);
		configuration.setTolerance(this.tolerance);
		return configuration;
	}
}
