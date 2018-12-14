package de.cookiejar.cookiejar.error;

import java.time.Instant;

// Deprecated, since cookiejar doesn't send values frequently anymore.
// Values will only be sent if there's a change.
@Deprecated
public class TimestampTooOldException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1509630907360736655L;

	public TimestampTooOldException(String message) {
		super(message);
	}
	
	public TimestampTooOldException(Instant lastTime) {
		super("The cookie jar seems not to be available in proper time. Last time: " + lastTime.toString() + " current Time: " + Instant.now().toString());
	}
}
