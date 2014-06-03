package com.walmart.delivery.controller.uri;

public class ProjectRestURIConstants {
	
	public class DeliveryController{
		public static final String POST_ADD_LOCATION_TO_MAP = "/rest/delivery/addLocationToMap";
		public static final String GET_CALCULATE_MINOR_PATH = "/rest/delivery/calculateMinorPath/{mapName}/{locationBegin}/{locationEnd}/{fuelAutonomy}/{fuelCost:.+}";
	}
	

}
