package de.cookiejar.cookiejar.error;

public class TareWeightException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1400394542914227524L;

	public TareWeightException() {
		super("Cannot tare weight. No values stored yet or much difference in last 2 values.");
	}
}
