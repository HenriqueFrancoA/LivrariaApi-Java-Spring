package br.com.henrique.JWT.exceptions.handler;

import br.com.henrique.JWT.exceptions.ExceptionResponse;
import br.com.henrique.JWT.exceptions.InvalidPaymentMethodException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@RestControllerAdvice
public class CustomizedInvalidPaymentMethodExceptionHandler {

	@ExceptionHandler(InvalidPaymentMethodException.class)
	public final ResponseEntity<ExceptionResponse> handleInvalidPaymentMethodExceptions(
			InvalidPaymentMethodException ex, WebRequest request) {

		ExceptionResponse exceptionResponse = new ExceptionResponse(
				new Date(),
				ex.getMessage(),
				request.getDescription(false));

		return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
	}

}
