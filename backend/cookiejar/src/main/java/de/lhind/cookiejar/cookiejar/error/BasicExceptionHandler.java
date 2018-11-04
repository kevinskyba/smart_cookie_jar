package de.lhind.cookiejar.cookiejar.error;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class BasicExceptionHandler extends ResponseEntityExceptionHandler {

	@Autowired
	private ErrorAttributes errorAttributes;

	@ExceptionHandler(value = TimestampTooOldException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE)
	public ExceptionResponse handleTimeStampTooOldException(WebRequest request, Exception exception) {
		return new ExceptionResponse(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE.value(), getErrorAttributes(request));
	}

	// default exception handling
	@ExceptionHandler(Exception.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ExceptionResponse defaultExceptionHandler(WebRequest request, Exception exception) {
		return new ExceptionResponse(HttpStatus.BAD_REQUEST.value(), getErrorAttributes(request));
	}

	private Map<String, Object> getErrorAttributes(WebRequest webRequest) {
		Map<String, Object> errorMap = new HashMap<>();
		errorMap.putAll(errorAttributes.getErrorAttributes(webRequest, false));
		return errorMap;
	}

	public void setErrorAttributes(ErrorAttributes errorAttributes) {
		this.errorAttributes = errorAttributes;
	}
}
