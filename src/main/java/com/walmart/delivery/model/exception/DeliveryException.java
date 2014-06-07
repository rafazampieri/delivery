package com.walmart.delivery.model.exception;

public class DeliveryException extends Exception{
	private static final long serialVersionUID = 1L;

	public DeliveryException() {
		super();
	}

	public DeliveryException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public DeliveryException(String message, Throwable cause) {
		super(message, cause);
	}

	public DeliveryException(String message) {
		super(message);
	}

	public DeliveryException(Throwable cause) {
		super(cause);
	}

}
