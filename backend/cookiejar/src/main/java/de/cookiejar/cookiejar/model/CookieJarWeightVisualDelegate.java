package de.cookiejar.cookiejar.model;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import de.cookiejar.cookiejar.error.TareWeightException;
import de.cookiejar.cookiejar.error.TimestampTooOldException;
import lombok.NoArgsConstructor;

@Component
@NoArgsConstructor
public class CookieJarWeightVisualDelegate {

	private double tareWeightValue = 0.0;

	private static final double TOLERANCE = 1000000.0;

	private static final double MAXIMUM_WEIGHT = 1250.0;

	private static final double MAXIMUM_TARE_DIFFERENCE = 150.0;

	private static final int TIME_LIMIT_IN_SECONDS_UNTIL_EXCEPTION = 600;

	// ./. empty weight with top
	public CookieJarWeightVO getAverageObject(CookieJarWeightRepository repository) {
		Instant last = null;
		double weight = 0.0;
		int counter = 0;
		for (CookieJarWeight cookieJarWeight : repository.findTop3ByOrderByIdDesc()) {
			weight += cookieJarWeight.getWeight();
			if (last == null || cookieJarWeight.getTimeStamp().isAfter(last)) {
				last = cookieJarWeight.getTimeStamp();
			}
			++counter;
		}

		if (!cookieJarAvailableInProperTime(last)) {
			throw new TimestampTooOldException(last);
		}

		CookieJarWeightVO average = new CookieJarWeightVO();
		weight = (weight / ((double) (counter))) - tareWeightValue;
		average.setWeight(weight);
		average.setFillLevel(getFillLevelPercentage(weight));
		average.setTimeStamp(last);

		return average;
	}

	public CookieJarWeightVO getLastObject(CookieJarWeightRepository repository) {
		CookieJarWeightVO returnValue = mapCookieJarWeight(
				repository.findTop3ByOrderByIdDesc().stream().findFirst().orElse(null));
		if (returnValue.getTimeStamp() == null) {
			return returnValue;
		}
		if (!cookieJarAvailableInProperTime(returnValue.getTimeStamp())) {
			throw new TimestampTooOldException(returnValue.getTimeStamp());
		}
		return returnValue;
	}

	public double getAverageValue(CookieJarWeightRepository repository) {
		double value = 0.0;
		int counter = 0;
		for (CookieJarWeight cookieJarWeight : repository.findTop3ByOrderByIdDesc()) {
			value += cookieJarWeight.getWeight();
			++counter;
		}
		value = value / ((double) (counter));
		return value;
	}

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
		if (weight >= MAXIMUM_WEIGHT) {
			return 100.0;
		}
		return weight * 100.0 / MAXIMUM_WEIGHT;
	}

	public CookieJarWeightVO mapCookieJarWeight(CookieJarWeight input) {
		if (input == null) {
			return new CookieJarWeightVO();
		}
		CookieJarWeightVO output = new CookieJarWeightVO();
		// if needed, remove empty weight
		if (input.getWeight() < (tareWeightValue + TOLERANCE)) {
			output.setWeight(input.getWeight() - tareWeightValue);
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

	public Boolean tareWeight(CookieJarWeightRepository repository) {
		if (repository.findTop2ByOrderByIdDesc() == null || repository.findTop2ByOrderByIdDesc().isEmpty()
				|| repository.findTop2ByOrderByIdDesc().size() < 2) {
			throw new TareWeightException();
		}
		List<CookieJarWeight> top2 = repository.findTop2ByOrderByIdDesc();
		Double firstValue = null;
		for (CookieJarWeight cookieJarWeight : top2) {
			if (firstValue == null) {
				firstValue = Double.valueOf(cookieJarWeight.getWeight());
			} else {
				// second step
				double difference = firstValue.doubleValue() - cookieJarWeight.getWeight();
				if (Math.abs(difference) > MAXIMUM_TARE_DIFFERENCE) {
					throw new TareWeightException();
				} else {
					this.tareWeightValue = firstValue.doubleValue();
				}
			}
		}
		return true;
	}

}