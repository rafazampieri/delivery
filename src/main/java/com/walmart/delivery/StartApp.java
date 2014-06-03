package com.walmart.delivery;

import com.walmart.delivery.model.service.DeliveryService;
import com.walmart.delivery.model.spring.SpringApplicationContext;
import com.walmart.delivery.model.to.MinorPathTO;

public class StartApp {
	
	public static void main(String[] args) throws Exception {
		DeliveryService deliveryService = SpringApplicationContext.SERVICE.getDeliveryService();
		
		String mapName = "SP";
		deliveryService.addLocationToMap(mapName, "A", "B", 10);
		deliveryService.addLocationToMap(mapName, "B", "D", 15);
		deliveryService.addLocationToMap(mapName, "A", "C", 20);
		deliveryService.addLocationToMap(mapName, "C", "D", 30);
		deliveryService.addLocationToMap(mapName, "B", "E", 50);
		deliveryService.addLocationToMap(mapName, "D", "E", 30); 
		
		MinorPathTO minorPathTO = deliveryService.calculateMinorPath("SP", "A", "D", 10, 2.5);
		System.out.println("Path:       " + minorPathTO.getListPaths());
		System.out.println("Distance:   " + minorPathTO.getDistance());
		System.out.println("Total Cost: R$ " + minorPathTO.getTotalCost());
	}
	
}
