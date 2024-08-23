package br.com.henrique.JWT.exceptions.handler;

import br.com.henrique.JWT.exceptions.ExceptionResponse;
import br.com.henrique.JWT.exceptions.InvalidJwtAuthenticationException;
import br.com.henrique.JWT.exceptions.ListEmptyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@ControllerAdvice
@RestController
public class CustomizedListEmptyExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(ListEmptyException.class)
	public final ResponseEntity<ExceptionResponse> handleListEmptyExceptions(
			Exception ex, WebRequest request) {

		ExceptionResponse exceptionResponse = new ExceptionResponse(
				new Date(),
				ex.getMessage(),
				request.getDescription(false));

		return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
	}

}
