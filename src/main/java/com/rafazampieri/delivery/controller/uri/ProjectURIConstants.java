package com.rafazampieri.delivery.controller.uri;

public class ProjectURIConstants {
	
	public class DeliveryRestController{
		public static final String PREFIX_MAPPING = "/rest/delivery";
		
		public static final String POST_ADD_LOCATION_TO_MAP = "/addLocationToMap/{mapName}/{locationBegin}/{locationEnd}/{cost}";
		public static final String GET_CALCULATE_MINOR_PATH = "/calculateMinorPath/{mapName}/{locationBegin}/{locationEnd}/{fuelAutonomy}/{fuelCost:.+}";
	}
	

}
