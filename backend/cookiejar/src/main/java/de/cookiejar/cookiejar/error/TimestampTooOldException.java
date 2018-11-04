package de.cookiejar.cookiejar.error;

import java.time.Instant;

public class TimestampTooOldException extends RuntimeException {

	public TimestampTooOldException(String message) {
		super(message);
	}
	
	public TimestampTooOldException(Instant lastTime) {
		super("The cookie jar seems not to be available in proper time. Last time: " + lastTime.toString() + " current Time: " + Instant.now().toString());
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = -7964715905896141472L;

}
