package com.walmart.delivery.controller.exceptionhandler;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.walmart.delivery.model.exception.DeliveryException;
import com.walmart.delivery.model.service.DeliveryService;

@ControllerAdvice
public class DeliveryWebExceptionHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(DeliveryService.class);
	
	@ResponseStatus(value=HttpStatus.BAD_REQUEST)
	@ExceptionHandler(DeliveryException.class)
	public @ResponseBody ResponseRestExceptionInfo handleSQLException(HttpServletRequest request, Exception exception){
        logger.info("DeliveryException Occured: URL="+request.getRequestURL());
		
		ResponseRestExceptionInfo responseRestExceptionInfo = new ResponseRestExceptionInfo();
		responseRestExceptionInfo.setRequestUrl( request.getRequestURL().toString() );
		responseRestExceptionInfo.setErrorMessage( exception.getMessage() );
		responseRestExceptionInfo.setHttpCode( HttpStatus.BAD_REQUEST.value() );
		
		logger.debug("Erro message returned: {}", responseRestExceptionInfo);
		
        return responseRestExceptionInfo;
    }
	
}