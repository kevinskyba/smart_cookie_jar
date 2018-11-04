package de.lhind.cookiejar.cookiejar.model;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import de.lhind.cookiejar.cookiejar.error.TimestampTooOldException;

public class CookieJarWeightVisualData {

	private static final double EMPTY_WEIGHT_WITH_TOP = -6615392.0;
	
	private static final double EMPTY_WEIGHT_MIDDLE = -6615449.5;

	private static final double EMPTY_WEIGHT_WITHOUT_TOP = -6615507.0;
	
	private static final double TOLERANCE = 1000000.0;

	private static final double MAXIMUM_WEIGHT = 1000.0;

	private static final int TIME_LIMIT_IN_SECONDS_UNTIL_EXCEPTION = 30;

	// ./. empty weight with top
	public static CookieJarWeightVO getAverageObject(CookieJarWeightRepository repository) {
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
		weight = (weight / ((double) (counter))) - EMPTY_WEIGHT_MIDDLE;
		average.setWeight(weight);
		average.setFillLevel(getFillLevelPercentage(weight));
		average.setTimeStamp(last);

		return average;
	}
	
	public static CookieJarWeightVO getLastObject(CookieJarWeightRepository repository) {
		CookieJarWeightVO returnValue = mapCookieJarWeight(repository.findTop3ByOrderByIdDesc().stream().findFirst().orElse(null));
		if (returnValue.getTimeStamp() == null) {
			return returnValue;
		}
		if (!cookieJarAvailableInProperTime(returnValue.getTimeStamp())) {
			throw new TimestampTooOldException(returnValue.getTimeStamp());
		}
		return returnValue;
	}

	public static double getAverageValue(CookieJarWeightRepository repository) {
		double value = 0.0;
		int counter = 0;
		for (CookieJarWeight cookieJarWeight : repository.findTop3ByOrderByIdDesc()) {
			value += cookieJarWeight.getWeight();
			++counter;
		}
		value = value / ((double) (counter));
		return value;
	}

	public static double getAverageValueMinusEmptyWeightWithTop(CookieJarWeightRepository repository) {
		return (getAverageValue(repository) - EMPTY_WEIGHT_WITH_TOP);
	}

	public static double getAverageValueMinusEmptyWeightWithoutTop(CookieJarWeightRepository repository) {
		return (getAverageValue(repository) - EMPTY_WEIGHT_WITHOUT_TOP);
	}

	public static boolean cookieJarAvailableInProperTime(Instant lastSavedTime) {
		if (lastSavedTime == null) {
			return true;
		}
		return (Duration.between(lastSavedTime, Instant.now()).getSeconds() <= TIME_LIMIT_IN_SECONDS_UNTIL_EXCEPTION);
	}

	public static double getFillLevelPercentage(double weight) {
		if (weight <= 0) {
			return 0.0;
		}
		if (weight >= MAXIMUM_WEIGHT) {
			return 100.0;
		}
		return weight * 100.0 / MAXIMUM_WEIGHT;
	}

	public static CookieJarWeightVO mapCookieJarWeight(CookieJarWeight input) {
		if (input == null) {
			return new CookieJarWeightVO();
		}
		CookieJarWeightVO output = new CookieJarWeightVO();
		// if needed, remove empty weight
		if (input.getWeight() < (EMPTY_WEIGHT_MIDDLE + TOLERANCE)) {
			output.setWeight(input.getWeight() - EMPTY_WEIGHT_MIDDLE);
		} else {
			output.setWeight(input.getWeight());
		}
		output.setTimeStamp(input.getTimeStamp());
		output.setFillLevel(getFillLevelPercentage(output.getWeight()));
		return output;
	}

	public static List<CookieJarWeightVO> mapCookieJarWeightAsList(List<CookieJarWeight> inputList) {
		if (inputList == null || inputList.isEmpty()) {
			return new ArrayList<CookieJarWeightVO>();
		}
		return inputList.stream().map(cookieJarWeight -> mapCookieJarWeight(cookieJarWeight))
				.collect(Collectors.toList());
	}
}
