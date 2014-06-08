package com.walmart.delivery.controller.exceptionhandler;

public class ResponseRestExceptionInfo {
	
	private String requestUrl;
    private String errorMessage;
    private Integer httpCode;
    
	public String getRequestUrl() {
		return requestUrl;
	}
	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public Integer getHttpCode() {
		return httpCode;
	}
	public void setHttpCode(Integer httpCode) {
		this.httpCode = httpCode;
	}

	@Override
	public String toString() {
		return "ResponseRestExceptionInfo [requestUrl=" + requestUrl
				+ ", errorMessage=" + errorMessage + ", httpCode=" + httpCode
				+ "]";
	}

}